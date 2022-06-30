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

/**
 * GUI class starts the graphical user interface. It loads all the scenes, receives the updates from the View class
 * and calls the correct methods, and it allows the user to make specific action in each phase of the turn.
 * In order to receive updates, it implements Listeners interfaces
 * @see it.polimi.ingsw.listeners
 * @see Application
 */
public class GUI extends Application implements TowersListener, ProfessorsListener, PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, RestoreCardsListener, InhibitedListener, UserInfoListener,
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

    //protected boolean active;
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
    //protected boolean isMainScene;

    private ArrayList<int[]> initialStudentsIsland; //da togliere
    private ArrayList<int []> initialStudentsEntrance;
    private ArrayList<int []> initialStudentsCloud; //da togliere
    private HashMap<Integer, Integer> initialTowersSchool; //da togliere
    /**
     * Maps the name of the fxml file with the scene itself
     */
    private HashMap<String, Scene> scenesMap;

    /**
     * Maps the name of the fxml file with its controller
     */
    private HashMap<String, SceneController> sceneControllersMap;

    public static void main(String[] args) {
        launch();
    }

    /**
     * Constructor creates a new GUI instance
     */
    public GUI() {
        scenesMap = new HashMap<>();
        sceneControllersMap = new HashMap<>();
        constants = new PlayerConstants();
        isMainSceneInitialized = false;
        actionAllowed = -1;

        planningPhaseService= new PlanningPhaseService(this);
        actionPhaseService= new ActionPhaseService();
        setViewService = new SetViewService(this);
        /**
         * Sets what happens when the service succeeds: the view is set and the phaseHandler is called with the next phase
         * @see SetViewService
         */
        setViewService.setOnSucceeded(workerStateEvent -> {
            View view = setViewService.getValue();
            this.view = view;
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setView(view);
            phaseHandler("InitializeMain");
        });
        /**
         * Sets what happens when the service succeeds: the view is set and the phaseHandler is called with the next phase
         * @see InitializeMainService
         */
        initializeMainService = new InitializeMainService();
        initializeMainService.setOnSucceeded(workerStateEvent -> {
            switchScene(MAIN);
            proxy.setView();
            if(!gameRestored){
                phaseHandler("PlanningPhase");
            }
            else {
                switchScene(MAIN);
                getPhaseService.start();
            }
        });

        /**
         * Sets what happens when the service succeeds: the last phase is recovered when restoring the game,
         * and the correct methods are called
         */
        getPhaseService = new GetPhaseService();
        getPhaseService.setOnSucceeded(workerStateEvent -> {
            String phase = getPhaseService.getValue();
            if(phase.equals("Play card!")) phaseHandler("PlayCardAnswer");
            else if(phase.equals("Start your Action Phase!")) {
                setConstants("CardPlayed");
                phaseHandler("StartTurnAnswer");
            }
        });

    }

    /**
     * @see Application#start(Stage)
     * @param stage
     */
    @Override
    public void start(Stage stage){
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
        scenesSetup();

        /*try {
            proxy.setDisconnectedListener(this);
            proxy.setServerOfflineListener(this);
            proxy.setSoldOutListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        /**
         * Calls the first method of the game: based on the answer returned by proxy.first() calls the correct phase
         * -SavedGame : a file save of an old game is present and the client is the first to connect, sets the scene to SAVED
         * -SetupGame: the current client was the first to connect, so it can choose the number of players and game mode,
         * sets the scene to SETUP
         * -Not first: the client was not the first to connect, it can only choose its nickname and character,
         * sets the scene to LOGIN
         * -LoginRestore: there is a game save, but the client was not the first to connect, sets the scene to LOGINRESTORE
         * @see SavedSceneController
         * @see LoginSceneController
         * @see SetupSceneController
         * @see LoginRestoreSceneController
         */
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
            GameOverSceneController controller = (GameOverSceneController) sceneControllersMap.get(GAMEOVER);
            controller.setSoldOut();
            primaryStage.setScene(scenesMap.get(GAMEOVER));
        } else if (result.equals("Not first")) {
            primaryStage.setScene(scenesMap.get(LOGIN));
            primaryStage.centerOnScreen();
        } else if (result.equals("LoginRestore")) {
            setGameRestored();
            primaryStage.setScene(scenesMap.get(LOGINRESTORE));
            primaryStage.centerOnScreen();
        }
    }
    /**
     * called when the GUI is laucnhed, loads all the scenes in advance and maps the name with the scene itself,
     * and with its controller in two hashmaps. It also sets a reference to the GUI and Proxy in every controller
     */
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
    /**
     * Switches to the specified scene, keeping the same stage
     * @param sceneName of type String - the name of the scene
     */
    public void switchScene(String sceneName) {
        primaryStage.setScene(scenesMap.get(sceneName));
        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    /**
     * phaseHandler receives the name of the current phase of the game, and calls the correct methods
     * @param phase of type String - the name of the phase to start
     * @see SetViewService
     * @see InitializeMainService
     * @see PlanningPhaseService
     * @see ActionPhaseService
     * @see GUI#loadScene(String)
     */
    public void phaseHandler(String phase) {
        System.out.println("started phase handler!");
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        CardsSceneController cardsSceneController = (CardsSceneController) sceneControllersMap.get(CARDS);
        CloudsSceneController cloudsSceneController = (CloudsSceneController) sceneControllersMap.get(CLOUDS);
        switch (phase) {
            case "SetView" -> setViewService.start();
            case "InitializeMain" -> {
                //setViewService.cancel();
                initializeMainService.start();
            }
            case "PlanningPhase" -> {
                //initializeMainService.cancel();
                //PlanningPhaseService planningPhaseService = new PlanningPhaseService(this);
                if(planningPhaseService.getState().equals(Worker.State.READY))
                    planningPhaseService.start();
                else
                    planningPhaseService.restart();
            }
            case "PlayCardAnswer" -> {
                setConstants("PlanningPhase");
                cardsSceneController.enableConfirm();
                loadScene(CARDS);
            }

            case "ActionPhase" -> {
                //ActionPhaseService actionPhaseService= new ActionPhaseService();
                if(actionPhaseService.getState().equals(Worker.State.READY))
                    actionPhaseService.start();
                else
                    actionPhaseService.restart();
            }
            case "StartTurnAnswer" -> {
                controller.setCurrentPlayer();
                setConstants("ActionPhase");
            }
            case "ChooseCloud" -> {
                //specialSceneController.disableConfirm();
                cloudsSceneController.enableConfirm();
                loadScene(CLOUDS);
            }
        }
    }

    /* action allowed specials:
    special 1 -> 3
    special 3 -> 4
    special 4 -> 8
    special 5 -> 5
    special 7 -> 6
    special 10 -> 7
     */

    /**
     *
     * @param special
     */
    public void useSpecial(int special){
        if(special == 3 ){
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setActionAllowed(4);
        }
        else if(special == 4){
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setActionAllowed(8);
        }
        else if(special == 5){
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setActionAllowed(5);
        }
        else if(special == 10){
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setActionAllowed(7);
        }
        else if(special == 9||special == 12){
            Special9or12SceneController controller = (Special9or12SceneController) sceneControllersMap.get(SPECIALS9OR12);
            controller.setSpecial(special);
            loadScene(SPECIALS9OR12);
        }
    }

    //special 7
    public void useSpecial(int special, ArrayList<Integer> cardStudents){
        if(special == 7){
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setFromCardToEntrance(cardStudents);
            controller.setActionAllowed(6);
        }

    }
    //special 1
    public void useSpecial(int special, int color){
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        controller.setCardStudent(color);
        controller.setActionAllowed(3);
    }

    public void specialNotAllowed(){
        SpecialsSceneController specialsSceneController = (SpecialsSceneController) sceneControllersMap.get(SPECIALS);
        MainSceneController mainSceneController = (MainSceneController) sceneControllersMap.get(MAIN);
        specialsSceneController.resetScene();
        if(constants.lastPhase().equals("ChooseCloud")) phaseHandler("ChooseCloud");
        else if(constants.lastPhase().equals("MoveStudent")) mainSceneController.setActionAllowed(0);
        else if(constants.lastPhase().equals("MoveMother")) mainSceneController.setActionAllowed(1);
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
                    view.setNoEntryListener(gui);
                    proxy.setServerOfflineListener(gui);
                    proxy.setDisconnectedListener(gui);

                    view.setRestoreCardsListener(gui);
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
                protected Boolean call() {
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

    public void setConstants(String phase){
        switch(phase){
            case "PlanningPhase"  -> constants.setPlanningPhaseStarted(true);
            case "CardPlayed" -> constants.setCardPlayed(true);
            case "ActionPhase" -> constants.setActionPhaseStarted(true);
            case "StudentsMoved" -> constants.setStudentMoved(true);
            case "MotherNatureMoved" -> constants.setMotherMoved(true);
            case "SpecialUsed" -> {
                constants.setSpecialUsed(true);
                SpecialsSceneController controller = (SpecialsSceneController) sceneControllersMap.get(SPECIALS);
                controller.resetScene();
                MainSceneController mainSceneController = (MainSceneController) sceneControllersMap.get(MAIN);
                if(constants.lastPhase().equals("ChooseCloud")) phaseHandler("ChooseCloud");
                else if(constants.lastPhase().equals("MoveStudent")) mainSceneController.setActionAllowed(0);
                else if(constants.lastPhase().equals("MoveMother")) mainSceneController.setActionAllowed(1);
            }
            case "Reset" -> constants.resetAll();
        }
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
        Platform.runLater(()->{
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setNewCoinsValue(playerRef, newCoinsValue);
        });
    }

    @Override
    public void notifySpecial(int specialRef, int playerRef) {
        Platform.runLater(()->{
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setSpecialPlayed(specialRef, playerRef);
        });
    }

    @Override
    public void notifySpecialList(ArrayList<Integer> specialList, ArrayList<Integer> cost) {
        Platform.runLater(() -> {
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
        Platform.runLater(() -> {
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setInhibitedIsland(islandRef, isInhibited);
        });
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
    public void restoreCardsNotify(ArrayList<String> hand) {
        Platform.runLater(() -> {
           CardsSceneController controller = (CardsSceneController) sceneControllersMap.get(CARDS);
           controller.restoreCards(hand);
        });

    }

    @Override
    public void notifyTowersChange(int place, int componentRef, int towersNumber) {
        Platform.runLater(() -> {
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
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
        Platform.runLater(() -> {
            SpecialsSceneController controller = (SpecialsSceneController) sceneControllersMap.get(SPECIALS);
            controller.setStudent(special, color, value);
        });
    }

    @Override
    public void notifyNoEntry(int special, int newValue) {
        Platform.runLater(() -> {
            SpecialsSceneController controller = (SpecialsSceneController) sceneControllersMap.get(SPECIALS);
            controller.setNoEntry(special, newValue);
        });
    }


    @Override
    public void notifyDisconnected() {
        Platform.runLater(()->{
            GameOverSceneController controller = (GameOverSceneController) sceneControllersMap.get(GAMEOVER);
            controller.setClientDisconnected();
            loadScene(GAMEOVER);
            primaryStage.close();
        });

    }

    @Override
    public void notifyServerOffline() {
        Platform.runLater(()->{
            GameOverSceneController controller = (GameOverSceneController) sceneControllersMap.get(GAMEOVER);
            controller.setServerOffline();
            loadScene(GAMEOVER);
            primaryStage.close();
        });

    }

    @Override
    public void notifyWinner() {
        Platform.runLater(()->{
            System.out.println("NOTIFY WINNER");
            String winner = view.getWinner();
            System.out.println("winner is"+winner);
            GameOverSceneController controller = (GameOverSceneController) sceneControllersMap.get(GAMEOVER);
            controller.setWinner(winner);
            loadScene(GAMEOVER);
            primaryStage.close();
        });

    }
}



