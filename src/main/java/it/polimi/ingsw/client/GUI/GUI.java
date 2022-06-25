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
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener, UserInfoListener,
        SpecialStudentsListener {

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
    protected static final String SAVED = "SavedScene.fxml";
    protected static final String LOGINRESTORE = "LoginRestoreScene.fxml";
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
    private HashMap<String, SceneController> sceneControllersMap; // maps the scene name with the scene controller

    public static void main(String[] args) {
        launch();
    }

    public GUI() {
        //taskSucceded=false;
        active = true;
        scenesMap = new HashMap<>();
        sceneControllersMap = new HashMap<>();
        constants = new PlayerConstants();
        view = new View();
        isMainScene = false;
        initialStudentsIsland = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            initialStudentsIsland.add(new int[]{0, 0, 0, 0, 0});
        }
        initialStudentsEntrance = new ArrayList<>();
        initialStudentsCloud = new ArrayList<>();
        initialTowersSchool = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            initialStudentsEntrance.add(new int[]{0, 0, 0, 0, 0});
            initialTowersSchool.put(i, 8);
            initialStudentsCloud.add(new int[]{0, 0, 0, 0, 0});
        }


        // planningPhaseService= new PlanningPhaseService(this);

        //actionPhaseService= new ActionPhaseService();


        setViewService = new SetViewService(this);
        setViewService.setOnSucceeded(workerStateEvent -> {
            System.out.println("on succeded");
            View view = setViewService.getValue();
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

        initializeMainService = new InitializeMainService();
        initializeMainService.setOnSucceeded(workerStateEvent -> {
            System.out.println("initializemain service on succeded");
            Boolean ok = initializeMainService.getValue();
            if (ok) {
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

        isMainSceneInitialized = false;
        areListenerSet = false;

        scenesSetup();
        //planningPhaseService= new PlanningPhaseService(this);
        //actionPhaseService= new ActionPhaseService();

        String result = null;
        try {
            result = proxy.first();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        if (result.equals("SavedGame")) {
            SavedGameAnswer savedGame = (SavedGameAnswer) proxy.getMessage();
            initializedSavedScene(savedGame.getNumberOfPlayers(), savedGame.isExpertMode());
            primaryStage.setScene(scenesMap.get(SAVED));
            primaryStage.centerOnScreen();
        } else if (result.equals("SetupGame")) {
            primaryStage.setScene(scenesMap.get(SETUP));
            primaryStage.centerOnScreen();

        } else if (result.equals("Server Sold Out")) {
            System.out.println(result);

        } else if (result.equals("Not first")) {
            primaryStage.setScene(scenesMap.get(LOGIN));
            primaryStage.centerOnScreen();
        } else if (result.equals("LoginRestore")) {
            primaryStage.setScene(scenesMap.get(LOGINRESTORE));
            primaryStage.centerOnScreen();
        }
    }

    //called when the GUI is launched, load all the scenes in advance, mapping them and setting the controllers
    public void scenesSetup() {
        String[] scenes = new String[]{SAVED, LOGINRESTORE, SETUP, LOGIN, MAIN, CARDS, WAITING, CLOUDS};
        try {
            for (String scene : scenes) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + scene));
                scenesMap.put(scene, new Scene(loader.load()));
                SceneController controller = loader.getController();
                controller.setGUI(this);
                controller.setProxy(proxy);
                sceneControllersMap.put(scene, controller);
            }
        } catch (IOException e) {
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

    public void phaseHandler(String phase) {
        System.out.println("started phase handler!");
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        switch (phase) {
            case "SetView" -> setViewService.start();

            case "InitializeMain" -> {
                setViewService.cancel();
                initializeMainService.start();
            }
            case "PlanningPhase" -> {
                initializeMainService.cancel();
                planningPhaseService = new PlanningPhaseService(this);
                planningPhaseService.start();
                switchScene(MAIN);
            }
            case "PlayCard" -> loadScene(CARDS);

            case "ActionPhase" -> {
                System.out.println("actionPhase");
                actionPhaseService= new ActionPhaseService();
                actionPhaseService.start();
            }
            case "StartTurn" -> controller.setCurrentPlayer();
        }
    }


    public class PlanningPhaseService extends Service<Boolean> {
        Boolean result;
        GUI gui;

        public PlanningPhaseService(GUI gui) {
            this.gui = gui;
            result = false;
        }

        @Override
        protected Task<Boolean> createTask() {
            return new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    System.out.println("prima di startPlanning");
                    result = proxy.startPlanningPhase();
                    System.out.println("dopo start planning");
                    return result;
                }
            };
        }

        @Override
        protected void succeeded() {
            System.out.println("Planning phase service on succeded");
            //CardsSceneController controller= (CardsSceneController) sceneControllersMap.get(CARDS);
            //controller.sceneInitialize();
            phaseHandler("PlayCard");
        }

        @Override
        protected void failed(){
            System.out.println("Task failed");
        }
    }

    public class ActionPhaseService extends Service<Boolean> {

        @Override
        protected Task<Boolean> createTask() {
            return new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    Boolean result = proxy.startActionPhase();
                    return result;
                }
            };
        }

        @Override
        protected void succeeded() {
            System.out.println("Planning phase service on succeded");
            phaseHandler("StartTurn");
        }

        @Override
        protected void failed(){
            System.out.println("Task failed");
        }
    }

    public class SetViewService extends Service<View> {
        private GUI gui;

        public SetViewService(GUI gui) {
            this.gui = gui;
        }

        @Override
        protected Task<View> createTask() {
            return new Task<View>() {
                @Override
                protected View call() throws Exception {
                    View view = proxy.startView();

                    view.setCoinsListener(gui);
                    view.setInhibitedListener(gui);
                    view.setIslandListener(gui);
                    view.setMotherPositionListener(gui);
                    view.setPlayedCardListener(gui);
                    view.setProfessorsListener(gui);
                    view.setStudentsListener(gui);
                    view.setTowersListener(gui);
                    view.setUserInfoListener(gui);
                    view.setSpecialStudentsListener(gui);

                    return view;
                }
            };
        }
    }

    public class InitializeMainService extends Service<Boolean> {

        @Override
        protected Task<Boolean> createTask() {
            return new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    System.out.println(Thread.currentThread());
                    Boolean ok;
                    MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
                    CloudsSceneController cloudsSceneController = (CloudsSceneController) sceneControllersMap.get(CLOUDS);
                    controller.setNumberOfPlayers(view.getNumberOfPlayers());
                    cloudsSceneController.initializeCloudScene(view.getNumberOfPlayers());
                    controller.setExpertMode(view.getExpertMode());
                    controller.initializeScene();
                    ok = true;

                    return ok;
                }
            };
        }
    }

    public void initializedSavedScene(int numberOfPlayer, boolean expertMode) {
        SavedSceneController controller = (SavedSceneController) sceneControllersMap.get(SAVED);
        String expertModeString;
        if (expertMode) expertModeString = "YES";
        else expertModeString = "NO";
        controller.initializedScene(numberOfPlayer, expertModeString);
    }

    //used to load a scene in a new stage (window), instead of the primaryStage
    public void loadScene(String sceneName) {
        Stage stage = new Stage();
        if(sceneName.equals(CARDS)){
            CardsSceneController controller = (CardsSceneController) sceneControllersMap.get(sceneName);
            stage.setScene(scenesMap.get(sceneName));
        }
        stage.centerOnScreen();
        stage.show();

    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setProxy(Proxy_c proxy) {
        this.proxy = proxy;
    }

    public void setYourNickname(String yourNickname) {
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        controller.setYourNickname(yourNickname);
    }

    public void setNotYourTurn() {
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        controller.setActionAllowed(-1);
    }


    @Override
    public void userInfoNotify(String nickname, String character, int playerRef) {
        Platform.runLater(()->{
            System.out.println("NOTIFY: userInfo: "+nickname+" "+character+" "+playerRef);
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setNickname(nickname, playerRef);
            controller.setCharacter(character, playerRef);
        });

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
        Platform.runLater(() -> {
            System.out.println("NOTIFY: islandChange- to delete: "+islandToDelete);
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.unifyIsland(islandToDelete);
        });
    }

    @Override
    public void notifyMotherPosition(int newMotherPosition) {
        Platform.runLater(() -> {
            System.out.println("NOTIFY: Mother Position : "+newMotherPosition);
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            //if(isMainSceneInitialized){
            controller.setMotherPosition(newMotherPosition);
            //} else
            //this.initialMotherPosition=newMotherPosition;

        });

    }

    @Override
    public void notifyPlayedCard(int playerRef, String assistantCard) {
        Platform.runLater(() -> {
            System.out.println("NOTIFY: Played Card: "+assistantCard+" "+playerRef);
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
        Platform.runLater(() -> {
            System.out.println("NOTIFY: professors: "+playerRef+ ","+color+","+newProfessorValue);
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setProfessor(playerRef, color, newProfessorValue);
        });
    }

    @Override
    public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {
        Platform.runLater(() -> {

            System.out.println("NOTIFY: student change: "+place+","+componentRef+","+color+","+newStudentsValue);;
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
        Platform.runLater(() -> {
            System.out.println("NOTIFY: towers change: "+place +","+componentRef+","+towersNumber);
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            //if(isMainSceneInitialized){
            if (place == 0) {
                controller.setTowersSchool(componentRef, towersNumber);
            } else if (place == 1) {
                controller.setTowersIsland(componentRef, towersNumber);
            }
        });
    }

    @Override
    public void notifyTowerColor(int islandRef, int newColor) {
        Platform.runLater(() -> {
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            System.out.println("NOTIFY tower color: "+islandRef+","+newColor);
            controller.setTowerColor(islandRef, newColor);
        });
    }


    @Override
    public void specialStudentsNotify(int special, int color, int value) {

    }

}



