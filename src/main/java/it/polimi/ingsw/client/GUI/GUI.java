package it.polimi.ingsw.client.GUI;

import com.sun.tools.javac.Main;
import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.PlayerConstants;
import it.polimi.ingsw.client.Proxy_c;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.server.answer.SavedGameAnswer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static com.sun.javafx.application.PlatformImpl.runAndWait;

public class GUI extends Application implements TowersListener, ProfessorsListener, PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener {

    private static Exit proxy;
    private View view;
    private Socket socket;
    protected Stage primaryStage;

    private Thread planningPhaseThread;
    private Thread actionPhaseThread;

    protected boolean active;
    protected boolean isMainSceneInitialized;
    protected boolean areListenerSet;
    protected boolean isViewSet;
    protected static final String SETUP = "SetupScene.fxml";
    protected static final String LOGIN = "LoginScene.fxml";
    protected static final String WAITING = "WaitingScene.fxml";
    protected static final String MAIN = "MainScene.fxml";
    protected static final String CARDS = "CardsScene.fxml";
    protected static final String CLOUDS = "CardsScene.fxml";
    protected PlayerConstants constants;
    protected boolean isMainScene;
    private int initialMotherPosition;
    private ArrayList<int[]> initialStudentsIsland;
    private ArrayList<int []> initialStudentsEntrance;
    private HashMap<Integer, Integer> initialTowersSchool;

    private HashMap<String, Scene> scenesMap; //maps the scene name with the scene itself
    private HashMap<String, SceneController > sceneControllersMap; // maps the scene name with the scene controller

    public static void main(String[] args) {
        launch();
    }

    public GUI() {
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
        initialTowersSchool = new HashMap<>();
        for(int i=0; i<4; i++){
            initialStudentsEntrance.add(new int[]{0,0,0,0,0});
            initialTowersSchool.put(i, 8);
        }
        planningPhaseThread= new PlanningPhaseThread();
        actionPhaseThread = new ActionPhaseThread();
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

    public void startGame(){
        //Platform.runLater(()-> {
            //switchScene(WAITING);
            if (!isViewSet) {
                 setView();
            } else {
                if (!isMainSceneInitialized) {
                    initializeMainScene();
                } else {
                    if (!constants.isStartGame()) {
                        constants.setStartGame(true);
                        startGame();
                    } else{
                        if (!constants.isPlanningPhaseStarted()) {
                            phaseHandler("PlanningPhase");
                    } else
                        phaseHandler(constants.lastPhase());
                    }
                }
            }
       // });
    }

    public void setView() {
        MainSceneController controller= (MainSceneController) sceneControllersMap.get(MAIN);
        try {
            View view = proxy.startView();
            //switchScene(WAITING);
            System.out.println(view);
            //if (view != null) {
                this.view = view;
                isViewSet = true;

                controller.setView(this.view);
                view.setCoinsListener(this);
                view.setInhibitedListener(this);
                view.setIslandListener(this);
                view.setMotherPositionListener(this);
                view.setPlayedCardListener(this);
                view.setProfessorsListener(this);
                view.setStudentsListener(this);
                view.setTowersListener(this);
            //}
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startGame();

        }

    public void phaseHandler(String phase){
        System.out.println("started phase handler!");
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN) ;
        switch (phase){
            case "PlanningPhase"-> /*startPlanningPhase(); */planningPhaseThread.start();
            case "PlayCard"-> switchScene(CARDS);
            case "StartTurn"-> {
                switchScene(MAIN);
                //startActionPhase();
                actionPhaseThread.start();
            }
            case "MoveStudent"-> {
                controller.setCurrentPlayer();
                System.out.println("move a student");
            }
            //case "MoveMother"-> controller.setActionAllowed(phase);
            //case "ChoseCloud"-> controller.setActionAllowed
            //case "End Turn"->

            }
        }

    private class PlanningPhaseThread extends Thread{
        @Override
        public void run(){
            try {
                if (proxy.startPlanningPhase()) {
                    constants.setPlanningPhaseStarted(true);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(()->{
                phaseHandler("PlayCard");
            });

        }
    }


    private class ActionPhaseThread extends Thread {
        @Override
        public void run() {
            try {
                if(proxy.startActionPhase()) {
                    System.out.println("action phase starting");
                    Platform.runLater(()->{
                        //isYourTurn = true;
                        phaseHandler("MoveStudent");

                    });
                }
                else
                    System.out.println("Error");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }



    public void initializeMainScene() {
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        controller.setNumberOfPlayers(view.getNumberOfPlayers());
        controller.setExpertMode(view.getExpertMode());
        for (int i = 0; i < view.getNumberOfPlayers(); i++) {
                do {
                    view.getNickname(i);
                    view.getCharacter(i);
                } while (view.getNickname(i)==null || view.getCharacter(i)==null);

            controller.setUserInfo(view.getNickname(i), view.getCharacter(i), i);
            //controller.setUserInfo(nickname, character, i);
            System.out.println("User info: " + view.getNickname(i) + " " + view.getCharacter(i));
        }

        controller.initializeScene();
        sendInitialInformation();
        startGame();

    }
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

    public void sendInitialInformation(){
        //Platform.runLater(()->{
        if (isMainSceneInitialized) {
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setMotherPosition(initialMotherPosition);
            for (int i = 0; i < view.getNumberOfPlayers(); i++) {
                for (int j = 0; j < 5; j++) {
                    controller.setStudentsEntrance(i, j, initialStudentsEntrance.get(i)[j]);
                }
                controller.setTowersSchool(i, initialTowersSchool.get(i));
                }

            for (int i = 0; i < initialStudentsIsland.size(); i++) {
                for (int j = 0; j < 5; j++) {
                    controller.setStudentsIsland(i, j, initialStudentsIsland.get(i)[j]);
                }
            }
        }
    }

    public void startPlanningPhase() {
        switchScene(WAITING);
            while (!constants.isPlanningPhaseStarted()) {
                try {
                    if (proxy.startPlanningPhase()) {
                        constants.setPlanningPhaseStarted(true);
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Not your turn");
                //controller.setNotYourTurnMessage();
            }
            phaseHandler("PlayCard");
    }

    public void startActionPhase(){
        try {
            if(proxy.startActionPhase()) {
                System.out.println("action phase starting");
                //isYourTurn = true;
                phaseHandler("MoveStudent");
            }
            else
                System.out.println("Error");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
            if(isMainSceneInitialized){
                controller.setMotherPosition(newMotherPosition);
            } else
                this.initialMotherPosition=newMotherPosition;

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
            //CloudsSceneController cloudsSceneController = (CloudsSceneController) sceneControllersMap.get(CLOUDS);

            if(isMainSceneInitialized){
                switch (place) {
                        case 0 -> mainSceneController.setStudentsEntrance(componentRef, color, newStudentsValue);
                        case 1 -> mainSceneController.setStudentsTable(componentRef, color, newStudentsValue);
                        case 2 -> mainSceneController.setStudentsIsland(componentRef, color, newStudentsValue);
                        //case 3 -> cloudsSceneController.setStudentsCloud(componentRef, color, newStudentsValue);
                    }
            }
            else{
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

                    //case 3 -> cloudsSceneController.setStudentsCloud(componentRef, color, newStudentsValue);

                }
            }
        });
    }


    @Override
    public void notifyTowersChange(int place, int componentRef, int towersNumber) {
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        Platform.runLater(()->{
            if(isMainSceneInitialized){
                if(place==0){
                    controller.setTowersSchool(componentRef, towersNumber);
                } else if(place==1){
                    controller.setTowersIsland(componentRef, towersNumber);
                }
            }
            else{
                if(place==0){
                   initialTowersSchool.remove(componentRef);
                   initialTowersSchool.put(componentRef, towersNumber);
                }
            }

        });
    }

    @Override
    public void notifyTowerColor(int islandRef, int newColor) {
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        Platform.runLater(()->{
            if(isMainSceneInitialized){
                //controller.setTowerColor()
            }

        });
    }

    public void setYourNickname(String yourNickname) {
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        controller.setYourNickname(yourNickname);
    }


}



