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
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.iq80.snappy.Main;

import javax.swing.*;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class GUI extends Application implements TowersListener, ProfessorsListener, PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener, UserInfoListener,
        SpecialStudentsListener, NoEntryClientListener, SpecialListener, DisconnectedListener, ServerOfflineListener, WinnerListener{

    private static Exit proxy;
    private View view;
    private Socket socket;
    protected Stage primaryStage;

    private Service<Boolean> planningPhaseService;
    private Service<Boolean> actionPhaseService;
    private Service<View> setViewService;
    private Service<Boolean> initializeMainService;
    private Service<String> getPhaseService;

    protected boolean active;
    protected boolean isMainSceneInitialized;
    protected boolean areListenerSet;
    private boolean gameRestored;
    private int actionAllowed;

    protected static final String SETUP = "SetupScene.fxml";
    protected static final String SAVED = "SavedScene.fxml";
    protected static final String LOGINRESTORE = "LoginRestoreScene.fxml";
    protected static final String LOGIN = "LoginScene.fxml";
    protected static final String WAITING = "WaitingScene.fxml";
    protected static final String MAIN = "MainScene.fxml";
    protected static final String CARDS = "CardsScene.fxml";
    protected static final String CLOUDS = "CloudsScene.fxml";
    protected static final String SPECIALS = "SpecialsScene.fxml";
    protected static final String SPECIALS9OR12 = "Special9or12Scene.fxml";
    protected static final String GAMEOVER = "GameOverScene.fxml";
    protected PlayerConstants constants;
    protected boolean isMainScene;

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
        active = true;
        scenesMap = new HashMap<>();
        sceneControllersMap = new HashMap<>();
        constants = new PlayerConstants();
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


        planningPhaseService= new PlanningPhaseService(this);

        actionPhaseService= new ActionPhaseService();


        setViewService = new SetViewService(this);
        setViewService.setOnSucceeded(workerStateEvent -> {
            View view = setViewService.getValue();
            this.view = view;
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setView(view);
            phaseHandler("InitializeMain");
        });

        initializeMainService = new InitializeMainService();
        initializeMainService.setOnSucceeded(workerStateEvent -> {
            Boolean ok = initializeMainService.getValue();
            if (ok) {
                proxy.setView();
                if(!gameRestored){
                    switchScene(MAIN);
                    phaseHandler("PlanningPhase");
                }
                else {
                    switchScene(MAIN);
                    getPhaseService.start();
                }
            }

        });

        getPhaseService = new GetPhaseService();
        getPhaseService.setOnSucceeded(workerStateEvent -> {
            String phase = getPhaseService.getValue();
            if(phase.equals("Play card!")) phaseHandler("PlayCardAnswer");
            else if(phase.equals("Start your Action Phase!")) phaseHandler("StartTurnAnswer");
        });

        actionAllowed = -1;
    }

    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException, InterruptedException {
        primaryStage = stage;
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
        primaryStage.setTitle("Eriantys");
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(-1);
            }
        });

        isMainSceneInitialized = false;
        areListenerSet = false;
        //firstClientService = new FirstClientService();
        //primaryStage.setScene(LOADING);
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
            setGameRestored();
            primaryStage.setScene(scenesMap.get(LOGINRESTORE));
            primaryStage.centerOnScreen();
        }
    }

    //called when the GUI is launched, load all the scenes in advance, mapping them and setting the controllers
    public void scenesSetup() {
        String[] scenes = new String[]{SAVED, LOGINRESTORE, SETUP, LOGIN, MAIN, CARDS, WAITING, CLOUDS, SPECIALS, SPECIALS9OR12, GAMEOVER};
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
        //PlanningPhaseService planningPhaseService = new PlanningPhaseService(this);
        //ActionPhaseService actionPhaseService = new ActionPhaseService();
        //SetViewService setViewService = new SetViewService(this);

        System.out.println("started phase handler!");
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        CardsSceneController cardsSceneController = (CardsSceneController) sceneControllersMap.get(CARDS);
        CloudsSceneController cloudsSceneController = (CloudsSceneController) sceneControllersMap.get(CLOUDS);
        switch (phase) {
            case "SetView" -> setViewService.start();

            case "InitializeMain" -> {
                setViewService.cancel();
                initializeMainService.start();
            }
            case "PlanningPhase" -> {
                initializeMainService.cancel();
                PlanningPhaseService planningPhaseService = new PlanningPhaseService(this);
                planningPhaseService.start();
                switchScene(MAIN);
            }
            case "PlayCardAnswer" -> {
                cardsSceneController.enableConfirm();
                loadScene(CARDS);
            }

            case "ActionPhase" -> {
                //planningPhaseService.cancel();
                ActionPhaseService actionPhaseService= new ActionPhaseService();
                actionPhaseService.start();
            }
            case "StartTurnAnswer" -> controller.setCurrentPlayer();
            case "ChooseCloud" -> {
                cloudsSceneController.enableConfirm();
                loadScene(CLOUDS);
            }
        }
    }

    public void useSpecial(int special){
        if(special == 3 ){
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setActionAllowed(3);
        }
        else if(special == 5){
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setActionAllowed(4);
        }
        else if(special == 9||special ==12){
            Special9or12SceneController controller = (Special9or12SceneController) sceneControllersMap.get(SPECIALS9OR12);
            controller.setSpecial(special);
            loadScene(SPECIALS9OR12);
        }
    }

    public void setGameRestored(){
        gameRestored = true;
    }

    private class GetPhaseService extends Service<String>{

        @Override
        protected Task<String> createTask() {
            return new Task<String>(){
                @Override
                protected String call() throws IOException {
                    String phase = proxy.getPhase();
                    return phase;
                }
            };
        }
    }


    private class FirstClientService extends Service<String>{

        @Override
        protected Task<String> createTask() {
            return new Task<String>() {
                @Override
                protected String call() throws Exception {
                    scenesSetup();
                    String result = null;
                    try {
                        result = proxy.first();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return result;
                }
            };
        }

        @Override
        protected void succeeded(){
            String result = this.getValue();
            if (result.equals("SavedGame")) {
                SavedGameAnswer savedGame = (SavedGameAnswer) proxy.getMessage();
                initializedSavedScene(savedGame.getNumberOfPlayers(), savedGame.isExpertMode());
                primaryStage.setScene(scenesMap.get(SAVED));
                primaryStage.centerOnScreen();
            } else if (result.equals("SetupGame")) {
                primaryStage.setScene(scenesMap.get(SETUP));
                primaryStage.centerOnScreen();

            } else if (result.equals("Server Sold Out")) {
                try {
                    notifyServerOffline();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (result.equals("Not first")) {
                primaryStage.setScene(scenesMap.get(LOGIN));
                primaryStage.centerOnScreen();
            } else if (result.equals("LoginRestore")) {
                setGameRestored();
                primaryStage.setScene(scenesMap.get(LOGINRESTORE));
                primaryStage.centerOnScreen();
            }
        }
    }

    private class PlanningPhaseService extends Service<Boolean> {
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
                    result = proxy.startPlanningPhase();
                    return result;
                }
            };
        }

        @Override
        protected void succeeded() {
            System.out.println("Planning phase service on succeded");
            phaseHandler("PlayCardAnswer");
        }

        @Override
        protected void failed(){
            System.out.println("Task failed");
        }
    }

    private class ActionPhaseService extends Service<Boolean> {

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
            phaseHandler("StartTurnAnswer");
        }

        @Override
        protected void failed(){
            System.out.println("Task failed");
        }
    }

    private class SetViewService extends Service<View> {
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
                    view.setSpecialListener(gui);
                    view.setWinnerListener(gui);
                    proxy.setDisconnectedListener(gui);
                    proxy.setServerOfflineListener(gui);

                    return view;
                }
            };
        }
    }

    private class InitializeMainService extends Service<Boolean> {

        @Override
        protected Task<Boolean> createTask() {
            return new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    Boolean ok;
                    MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
                    CloudsSceneController cloudsSceneController = (CloudsSceneController) sceneControllersMap.get(CLOUDS);
                    CardsSceneController cardsSceneController = (CardsSceneController) sceneControllersMap.get(CARDS);

                    controller.setNumberOfPlayers(view.getNumberOfPlayers());
                    cloudsSceneController.initializeCloudScene(view.getNumberOfPlayers());
                    cloudsSceneController.disableConfirm();
                    cardsSceneController.disableConfirm();
                    controller.setExpertMode(view.getExpertMode());
                    controller.initializeScene();
                    ok = true;

                    return ok;
                }
            };
        }
    }

    private void initializedSavedScene(int numberOfPlayer, boolean expertMode) {
        SavedSceneController controller = (SavedSceneController) sceneControllersMap.get(SAVED);
        String expertModeString;
        if (expertMode) expertModeString = "YES";
        else expertModeString = "NO";
        controller.initializedScene(numberOfPlayer, expertModeString);
    }

    //used to load a scene in a new stage (window), instead of the primaryStage
    public void loadScene(String sceneName) {
        Stage stage = new Stage();
        stage.setScene(scenesMap.get(sceneName));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
        stage.setTitle("Eriantys");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();

    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setProxy(Exit proxy) throws IOException {
        this.proxy = proxy;
        proxy.setServerOfflineListener(this);
        proxy.setDisconnectedListener(this);

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
    public void notifySpecial(int specialRef, int playerRef) {

    }

    @Override
    public void notifySpecialList(ArrayList<Integer> specialList, ArrayList<Integer> cost) {
        Platform.runLater(() -> {
            System.out.println("NOTIFY special list");
            SpecialsSceneController controller = (SpecialsSceneController) sceneControllersMap.get(SPECIALS);
            controller.initializedSpecialsScene(specialList, cost);
        });

    }

    @Override
    public void notifyIncreasedCost(int specialRef, int newCost) {
        Platform.runLater(() -> {
            SpecialsSceneController controller = (SpecialsSceneController) sceneControllersMap.get(SPECIALS);
            controller.setCoins(specialRef, newCost);
        });
    }

    @Override
    public void notifyInhibited(int islandRef, int isInhibited) {
        //controller.setInhibitedIsland(islandRef, isInhibited);
    }

    @Override
    public void notifyIslandChange(int islandToDelete) {
        Platform.runLater(() -> {
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.unifyIsland(islandToDelete);
        });
    }

    @Override
    public void notifyMotherPosition(int newMotherPosition) {
        Platform.runLater(() -> {
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setMotherPosition(newMotherPosition);
        });

    }

    @Override
    public void notifyPlayedCard(int playerRef, String assistantCard) {
        Platform.runLater(() -> {
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
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setProfessor(playerRef, color, newProfessorValue);
        });
    }

    @Override
    public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {
        Platform.runLater(() -> {
            MainSceneController mainSceneController = (MainSceneController) sceneControllersMap.get(MAIN);
            CloudsSceneController cloudsSceneController = (CloudsSceneController) sceneControllersMap.get(CLOUDS);

            switch (place) {
                case 0 -> mainSceneController.setStudentsEntrance(componentRef, color, newStudentsValue);
                case 1 -> mainSceneController.setStudentsTable(componentRef, color, newStudentsValue);
                case 2 -> mainSceneController.setStudentsIsland(componentRef, color, newStudentsValue);
                case 3 -> cloudsSceneController.setStudentsCloud(componentRef, color, newStudentsValue);
            }
        });
    }


    @Override
    public void notifyTowersChange(int place, int componentRef, int towersNumber) {
        Platform.runLater(() -> {
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
            controller.setTowerColor(islandRef, newColor);
        });
    }


    @Override
    public void specialStudentsNotify(int special, int color, int value) {
        System.out.println("NOTIFY SPECIAL STUDENT");
        Platform.runLater(() -> {
            SpecialsSceneController controller = (SpecialsSceneController) sceneControllersMap.get(SPECIALS);
            controller.setStudent(special, color, value);
        });
    }


    @Override
    public void notifyNoEntry(int special, int newValue) {
        System.out.println("NOTIFY NO ENTRY");
        Platform.runLater(() -> {
            SpecialsSceneController controller = (SpecialsSceneController) sceneControllersMap.get(SPECIALS);
            controller.setNoEntry(special, newValue);
        });
    }


    @Override
    public void notifyDisconnected() throws IOException {
        Platform.runLater(()->{
            System.out.println("client disconnected");
            GameOverSceneController controller = (GameOverSceneController) sceneControllersMap.get(GAMEOVER);
            controller.setClientDisconnected();
            loadScene(GAMEOVER);
            primaryStage.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void notifyServerOffline() throws IOException {
        Platform.runLater(()->{
            System.out.println("server offline");
            GameOverSceneController controller = (GameOverSceneController) sceneControllersMap.get(GAMEOVER);
            controller.setServerOffline();
            loadScene(GAMEOVER);
            primaryStage.close();
            /*try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        });

    }

    @Override
    public void notifyWinner() throws IOException {
        Platform.runLater(()->{
            String winner = view.getWinner();
            System.out.println("winner is"+winner);
            GameOverSceneController controller = (GameOverSceneController) sceneControllersMap.get(GAMEOVER);
            controller.setWinner(winner);
            loadScene(GAMEOVER);
            primaryStage.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}



