package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.PlayerConstants;
import it.polimi.ingsw.client.Proxy_c;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.server.answer.SavedGameAnswer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.iq80.snappy.Main;

import javax.swing.*;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class GUI extends Application implements TowersListener, ProfessorsListener, PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener, UserInfoListener {

    private static Exit proxy;
    private View view;
    private Socket socket;
    protected Stage primaryStage;

    private Service<Boolean> planningPhaseService;
    private Service<Boolean> actionPhaseService;
    private Service<View> setViewService;
    private Service<Boolean> initializeMainService;

    private Boolean taskSucceded;

    protected boolean active;
    protected boolean isMainSceneInitialized;
    protected boolean areListenerSet;
    protected boolean isViewSet;
    protected static final String SETUP = "SetupScene.fxml";
    protected static final String LOGIN = "LoginScene.fxml";
    protected static final String WAITING = "WaitingScene.fxml";
    protected static final String MAIN = "MainScene.fxml";
    protected static final String CARDS = "CardsScene.fxml";
    protected static final String CLOUDS = "CloudsScene.fxml";
    protected PlayerConstants constants;
    protected boolean isMainScene;
    private int initialMotherPosition;
    private ArrayList<int[]> initialStudentsIsland;
    private ArrayList<int []> initialStudentsEntrance;
    private ArrayList<int []> initialStudentsCloud;
    private HashMap<Integer, Integer> initialTowersSchool;

    private HashMap<String, Scene> scenesMap; //maps the scene name with the scene itself
    private HashMap<String, SceneController > sceneControllersMap; // maps the scene name with the scene controller

    public static void main(String[] args) {
        launch();
    }

    public GUI() {
        //taskSucceded=false;
        active=true;
        scenesMap = new HashMap<>();
        sceneControllersMap = new HashMap<>();
        constants = new PlayerConstants();
        view = new View();
        isMainScene=false;
        initialStudentsIsland = new ArrayList<>();
        for(int i=0; i<12; i++){
            initialStudentsIsland.add(new int[]{0,0,0,0,0});
        }
        initialStudentsEntrance = new ArrayList<>();
        initialStudentsCloud= new ArrayList<>();
        initialTowersSchool = new HashMap<>();
        for(int i=0; i<4; i++){
            initialStudentsEntrance.add(new int[]{0,0,0,0,0});
            initialTowersSchool.put(i, 8);
            initialStudentsCloud.add(new int[]{0,0,0,0,0});
        }


       // planningPhaseService= new PlanningPhaseService(this);

        //actionPhaseService= new ActionPhaseService();


        setViewService= new SetViewService(this);
        setViewService.setOnSucceeded(workerStateEvent -> {
            System.out.println("on succeded");
            View view= setViewService.getValue();
            this.view = view;
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setView(view);
            //initializeMainScene();
            //proxy.setView();
            /*
            this.view.setCoinsListener(this);
            this.view.setInhibitedListener(this);
            this.view.setIslandListener(this);
            this.view.setMotherPositionListener(this);
            this.view.setPlayedCardListener(this);
            this.view.setProfessorsListener(this);
            this.view.setStudentsListener(this);
            this.view.setTowersListener(this);
            this.view.setUserInfoListener(this);
            proxy.setView()*/
            //System.out.println("view is: "+view);

            //isViewSet = true;
            //System.out.println("set done");
            //phaseHandler("PlanningPhase");
            phaseHandler("InitializeMain");
            //startGame();
            });

        initializeMainService= new InitializeMainService();
        initializeMainService.setOnSucceeded(workerStateEvent -> {
            System.out.println("initializemain service on succeded");
            Boolean ok= initializeMainService.getValue();
            if(ok){
                proxy.setView();
                phaseHandler("PlanningPhase");
            }

        });
}

    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException, InterruptedException {
        primaryStage = stage;
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
        primaryStage.setTitle("Eriantys");
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();

        isMainSceneInitialized=false;
        areListenerSet=false;

        scenesSetup();

        String result = null;
        try {
            result = proxy.first();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(result.equals("SavedGame")){
            SavedGameAnswer savedGame = (SavedGameAnswer) proxy.getMessage();
            //da scrivere
        }
        else if (result.equals("SetupGame")) {
            primaryStage.setScene(scenesMap.get(SETUP));
            primaryStage.centerOnScreen();

        } else if (result.equals("Server Sold Out")) {
            System.out.println(result);

        } else if (result.equals("Not first")) {
            primaryStage.setScene(scenesMap.get(LOGIN));
            primaryStage.centerOnScreen();
        }
        else if(result.equals("SavedGame")){

        }
        else if(result.equals("LoginRestore")){

        }
    }

    //called when the GUI is launched, load all the scenes in advance, mapping them and setting the controllers
    public void scenesSetup() {
        String[] scenes = new String[]{SETUP, LOGIN, MAIN, CARDS, WAITING, CLOUDS};
            try {
                for(String scene: scenes) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + scene));
                    scenesMap.put(scene, new Scene(loader.load()));
                    SceneController controller = loader.getController();
                    controller.setGUI(this);
                    controller.setProxy(proxy);
                    sceneControllersMap.put(scene, controller);
                }
            } catch(IOException e) {
                e.printStackTrace();
            }

    }

    //switch the scene, keeps the same stage
    //initialize the scene if necessary, passing parameters to the controller
    public void switchScene(String sceneName) {
        primaryStage.setScene(scenesMap.get(sceneName));
        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    public void phaseHandler(String phase){
        PlanningPhaseService planningPhaseService = new PlanningPhaseService(this);
        ActionPhaseService actionPhaseService = new ActionPhaseService();
        //SetViewService setViewService = new SetViewService(this);

        System.out.println("started phase handler!");
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN) ;
        switch (phase){
            case "SetView"-> {
                setViewService.start();
            }
            case "InitializeMain"-> {
                    setViewService.cancel();
                    initializeMainService.start();
            }
            case "PlanningPhase"->
                    {
                        //PlanningPhaseService planningPhaseService = new PlanningPhaseService(this);
                        initializeMainService.cancel();
                        //if (planningPhaseService.getState()== Worker.State.READY)
                          //  planningPhaseService.restart();
                        //else{
                        planningPhaseService.start();
                        //}
                        //startPlanningPhase();
                        //System.out.println("planning finished");
                    }
            case "PlayCard"-> {
                switchScene(CARDS);


            }
            case "ActionPhase"-> {
                System.out.println("actionPhase");
                //if(actionPhaseService.getState()==Worker.State.READY)
                    //actionPhaseService.start();
                //else {
                 //ActionPhaseService actionPhaseService= new ActionPhaseService();
                 actionPhaseService.start();
               // }
                switchScene(MAIN);

            }
            case "StartTurn"-> {
                controller.setCurrentPlayer();
            }

            }
    }

    public class PlanningPhaseService extends Service<Boolean>{
        Boolean result;
        GUI gui;

        public PlanningPhaseService(GUI gui){
            this.gui=gui;
            result=false;
        }
        @Override
        protected Task<Boolean> createTask() {
            System.out.println("planningPhaseService started");
            return new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    System.out.println("calling startPlanningPhase");
                    result= proxy.startPlanningPhase();
                    System.out.println("called startPlanningPhase: "+result);
                    return result;
                }
            };
        }
        @Override
        protected void succeeded() {
            phaseHandler("PlayCard");
        }
    }
    public class ActionPhaseService extends Service<Boolean>{

        @Override
        protected Task<Boolean> createTask() {
            return new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception{
                    Boolean result= proxy.startActionPhase();
                    return result;
                }
            };
        }
        @Override
        protected void succeeded(){
            phaseHandler("StartTurn");

        }
    }

    public class SetViewService extends Service<View>{
        private GUI gui;
        public SetViewService(GUI gui){
            this.gui=gui;
        }
        @Override
        protected Task<View> createTask() {
            return new Task<View>() {
                @Override
                protected View call() throws Exception {
                    View view = proxy.startView();
                    //isViewSet = true;

                    view.setCoinsListener(gui);
                    view.setInhibitedListener(gui);
                    view.setIslandListener(gui);
                    view.setMotherPositionListener(gui);
                    view.setPlayedCardListener(gui);
                    view.setProfessorsListener(gui);
                    view.setStudentsListener(gui);
                    view.setTowersListener(gui);
                    view.setUserInfoListener(gui);

                    return view;
                }
            };
        }

        /*@Override
        protected void succeeded(){
            View view= setViewService.getValue();
            gui.view = view;
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setView(view);
            phaseHandler("InitializeMain");
        }*/

    }

    public class InitializeMainService extends Service<Boolean>{

        @Override
        protected Task<Boolean> createTask() {
            return new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    System.out.println(Thread.currentThread());
                    Boolean ok=false;
                    System.out.println("initialize main scene");
                    System.out.println("View is: "+view);
                    //Platform.runLater(()->{
                        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
                        CloudsSceneController cloudsSceneController= (CloudsSceneController) sceneControllersMap.get(CLOUDS);
                        controller.setNumberOfPlayers(view.getNumberOfPlayers());
                        cloudsSceneController.initializeCloudScene(view.getNumberOfPlayers());
                        controller.setExpertMode(view.getExpertMode());
                        System.out.println("aaaaa");
                        System.out.println("Number of players: "+view.getNumberOfPlayers());
                        controller.initializeScene();
                        System.out.println("initializedScene completed");

                    //});
                    ok=true;
                    //do {
                        //for (int i = 0; i < view.getNumberOfPlayers(); i++) {
                           // System.out.println("bbbbbb");
                            //controller.setUserInfo(view.getNickname(i), view.getCharacter(i), i);
                            //controller.setUserInfo(nickname, character, i);
                            //System.out.println("User info: " + view.getNickname(i) + " " + view.getCharacter(i));
                        //}
                   // } while (!view.isInitializedView());




                    //sendInitialInformation();
                    //phaseHandler("PlanningPhase");
                    System.out.println("returning ok: "+ok);
                    return ok;
                }
            };
        }

        /*@Override
        protected void succeeded(){
           Boolean ok= initializeMainService.getValue();
           if(ok){
                proxy.setView();
                phaseHandler("PlanningPhase");
           }
        }*/
    }

    /*public void initializeMainScene() {
        System.out.println("initialize main scene");
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        CloudsSceneController cloudsSceneController= (CloudsSceneController) sceneControllersMap.get(CLOUDS);
        controller.setNumberOfPlayers(view.getNumberOfPlayers());
        cloudsSceneController.initializeCloudScene(view.getNumberOfPlayers());
        controller.setExpertMode(view.getExpertMode());
        System.out.println("1.number of players and expert mode are set: "+view.getNumberOfPlayers()+" "+view.getExpertMode());

        /*for (int i = 0; i < view.getNumberOfPlayers(); i++) {
               /* do {
                    view.getNickname(i);
                    view.getCharacter(i);
                } while (view.getNickname(i)==null || view.getCharacter(i)==null);


            controller.setUserInfo(view.getNickname(i), view.getCharacter(i), i);
            System.out.println("2.set Nickname and character : "+view.getNickname(i)+view.getCharacter(i));
            //controller.setUserInfo(nickname, character, i);
        }
        System.out.println("3. Nick and character set");

        controller.initializeScene();
        //sendInitialInformation();
        //System.out.println("calling phase handler");
        phaseHandler("PlanningPhase");
        //startGame();

    }*/
    /*
    //used to load a scene in a new stage (window), instead of the primaryStage
    public void loadScene(String sceneName) {
        Stage stage = new Stage();
        currentScene=sceneName;
        if(sceneName.equals(CARDS)){
            //Platform.runLater(()->{
                CardsSceneController controller = (CardsSceneController) sceneControllersMap.get(sceneName);
                controller.sceneInitialize();
                stage.setScene(scenesMap.get(sceneName));
            //});
        }
        stage.centerOnScreen();
        stage.show();

    }*/

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setProxy(Proxy_c proxy) {
        this.proxy = proxy;
    }

    /*public void sendInitialInformation(){
        if (isMainSceneInitialized) {
            System.out.println("send initial information");
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);;
            controller.setMotherPosition(initialMotherPosition);
            System.out.println("set mother pos: "+initialMotherPosition);
            for (int i = 0; i < view.getNumberOfPlayers(); i++) {
                for (int j = 0; j < 5; j++) {
                    System.out.println("Initial students entrance :"+initialStudentsEntrance.get(i)[j]);
                    controller.setStudentsEntrance(i, j, initialStudentsEntrance.get(i)[j]);
                }
                controller.setTowersSchool(i, initialTowersSchool.get(i));
                System.out.println("initial towers school: "+initialTowersSchool.get(i));
                }

            for (int i = 0; i < initialStudentsIsland.size(); i++) {
                for (int j = 0; j < 5; j++) {
                    controller.setStudentsIsland(i, j, initialStudentsIsland.get(i)[j]);
                    System.out.println("initial students island: "+initialStudentsIsland.get(i)[j]);
                }
            }
        }
        System.out.println("ccccc");
        CloudsSceneController controller= (CloudsSceneController) sceneControllersMap.get(CLOUDS);
        for(int i=0; i< view.getNumberOfPlayers(); i++){
            for (int j = 0; j < 5; j++) {
                controller.setStudentsCloud(i, j, initialStudentsCloud.get(i)[j]);
                System.out.println("Initial students cloud: "+initialStudentsCloud.get(i)[j]);
            }
        }
        System.out.println("initial information sent");
    }*/


    @Override
    public void userInfoNotify(String nickname, String character, int playerRef) {
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        controller.setNickname(nickname, playerRef);
        controller.setCharacter(character, playerRef);
    }


    @Override
    public void notifyNewCoinsValue(int playerRef, int newCoinsValue) {
        //controller.setNewCoinsValue(playerRef, newCoinsValue);
    }

    @Override
    public void notifyInhibited(int islandRef, int isInhibited) {
        //controller.setInhibitedIsland(islandRef, isInhibited);
    }

    @Override
    public void notifyIslandChange(int islandToDelete) {
        Platform.runLater(()->{
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.unifyIsland(islandToDelete);
        });
    }

    @Override
    public void notifyMotherPosition(int newMotherPosition) {
        Platform.runLater(()->{
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            //if(isMainSceneInitialized){
            controller.setMotherPosition(newMotherPosition);
            //} else
                //this.initialMotherPosition=newMotherPosition;

        });

    }

    @Override
    public void notifyPlayedCard(int playerRef, String assistantCard) {
        Platform.runLater(()->{
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setLastPlayedCard(playerRef, assistantCard);
        });

    }
    //restore
    @Override
    public void notifyHand(int playerRef, ArrayList<String> hand) {
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
    }

    @Override
    public void notifyProfessors(int playerRef, int color, boolean newProfessorValue) {
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        Platform.runLater(()->{
            controller.setProfessor(playerRef,color,newProfessorValue);
        });
    }

    @Override
    public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {
        Platform.runLater(() -> {
            MainSceneController mainSceneController = (MainSceneController) sceneControllersMap.get(MAIN);
            CloudsSceneController cloudsSceneController = (CloudsSceneController) sceneControllersMap.get(CLOUDS);

            //if(isMainSceneInitialized){
                switch (place) {
                        case 0 -> mainSceneController.setStudentsEntrance(componentRef, color, newStudentsValue);
                        case 1 -> mainSceneController.setStudentsTable(componentRef, color, newStudentsValue);
                        case 2 -> mainSceneController.setStudentsIsland(componentRef, color, newStudentsValue);
                        case 3 -> cloudsSceneController.setStudentsCloud(componentRef, color, newStudentsValue);
                    }
            //}
            /*else{
                int[] students;
                switch (place) {
                    case 0 -> {
                        students= initialStudentsEntrance.get(componentRef);
                        students[color]= newStudentsValue;
                    }
                    case 2 -> {
                        students= initialStudentsIsland.get(componentRef);
                        students[color]= newStudentsValue;
                    }

                    case 3 -> {
                        students= initialStudentsCloud.get(componentRef);
                        students[color]=newStudentsValue;
                    }

                }
            }*/
        });
    }


    @Override
    public void notifyTowersChange(int place, int componentRef, int towersNumber) {
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        Platform.runLater(()->{
            //if(isMainSceneInitialized){
                if(place==0){
                    controller.setTowersSchool(componentRef, towersNumber);
                } else if(place==1){
                    controller.setTowersIsland(componentRef, towersNumber);
                }
            //}
            /*else{
                if(place==0){
                   initialTowersSchool.remove(componentRef);
                   initialTowersSchool.put(componentRef, towersNumber);
                }
            }*/

        });
    }

    @Override
    public void notifyTowerColor(int islandRef, int newColor) {
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        Platform.runLater(()->{
           // if(isMainSceneInitialized){
                //controller.setTowerColor()
            //}

        });
    }

    public void setYourNickname(String yourNickname) {
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        controller.setYourNickname(yourNickname);
    }

    public void setNotYourTurn() {
        MainSceneController controller= (MainSceneController) sceneControllersMap.get(MAIN);
        controller.setActionAllowed(-1);
    }


}



