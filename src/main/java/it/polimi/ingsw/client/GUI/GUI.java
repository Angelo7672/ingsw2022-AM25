package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.PlayerConstants;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.server.answer.SavedGameAnswer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
    protected Stage primaryStage;

    private final Service<Boolean> planningPhaseService;
    private final Service<Boolean> actionPhaseService;
    private final Service<View> setViewService;
    private final Service<Boolean> initializeMainService;
    private final Service<String> getPhaseService;

    private boolean gameRestored;
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
    private final HashMap<String, Scene> scenesMap;
    private final HashMap<String, SceneController> sceneControllersMap;
    /**
     * Launches the GUI
     * @param args of type String[]
     */
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
        planningPhaseService= new PlanningPhaseService(this);
        actionPhaseService= new ActionPhaseService();
        setViewService = new SetViewService(this);
        initializeMainService = new InitializeMainService();
        getPhaseService = new GetPhaseService();
    }
    /**
     * Calls the first method of the game: based on the answer returned by proxy.first() calls the correct phase
     * -SavedGame : a file save of an old game is present and the client is the first to connect, sets the scene to SAVED
     * -SetupGame: the current client was the first to connect, so it can choose the number of players and game mode,
     * sets the scene to SETUP
     * -Not first: the client was not the first to connect, it can only choose its nickname and character,
     * sets the scene to LOGIN
     * -LoginRestore: there is a game save, but the client was not the first to connect, sets the scene to LOGINRESTORE
     * @param stage of type Stage
     * @see Application#start(Stage)
     * @see SavedSceneController
     * @see LoginSceneController
     * @see SetupSceneController
     * @see LoginRestoreSceneController
     */
    @Override
    public void start(Stage stage){
        primaryStage = stage;
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
        primaryStage.setTitle("Eriantys");
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(-1);
            }
        });
        scenesSetup();
        String result = proxy.first();
        if (result.equals("SavedGame")) {
            SavedGameAnswer savedGame = (SavedGameAnswer) proxy.getMessage();
            initializedSavedScene(savedGame.getNumberOfPlayers(), savedGame.isExpertMode());
            primaryStage.setScene(scenesMap.get(SAVED));
            primaryStage.centerOnScreen();
        } else if (result.equals("SetupGame")) {
            primaryStage.setScene(scenesMap.get(SETUP));
            primaryStage.centerOnScreen();
        } else if (result.equals("Server Sold Out")) {
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
     * Called when the GUI is launched, loads all the scenes in advance and maps the name with the scene itself,
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
            loadScene(GAMEOVER);
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
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        CardsSceneController cardsSceneController = (CardsSceneController) sceneControllersMap.get(CARDS);
        CloudsSceneController cloudsSceneController = (CloudsSceneController) sceneControllersMap.get(CLOUDS);
        switch (phase) {
            case "SetView" -> setViewService.start();
            case "InitializeMain" -> initializeMainService.start();
            case "PlanningPhase" -> {
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
                cloudsSceneController.enableConfirm();
                loadScene(CLOUDS);
            }
        }
    }
    /**
     * Called from specialSceneController when a special card is chosen. It takes the number of the special
     * and sets the action the user is allowed to make on the MainScene
     * Actions allowed for the specials are:
     * - special 1 : 3
     * - special 3 : 4
     * - special 5 : 5
     * - special 7 : 6
     * - special 10 : 7
     * - special 4 : 8
     * @param special of type int - the index of the special (out of 12)
     * @see MainSceneController#setActionAllowed(int)
     * @see SpecialsSceneController
     * @see Special9or12SceneController
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
    /**
     * Called from specialSceneController when special card 7 is chosen.
     * Sets the action the user is allowed to make on the MainScene
     * @param special of type int - the index of the special (out of 12)
     * @param cardStudents of type ArrayList<Integer> - the ArrayList of the students chosen form the card
     */
    public void useSpecial(int special, ArrayList<Integer> cardStudents){
        if(special == 7){
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setFromCardToEntrance(cardStudents);
            controller.setActionAllowed(6);
        }
    }
    /**
     * Called from specialSceneController when special card 1 is chosen.
     * Sets the action the user is allowed to make on the MainScene
     * @param special of type int - the index of the special (out of 12)
     * @param color of type int - the index of the color of the student chosen from the card
     */
    public void useSpecial(int special, int color){
        if(special == 1){
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setCardStudent(color);
            controller.setActionAllowed(3);
        }
    }
    /**
     * Called from MainSceneController when the user tries to use a special card during its turn, but it receives "move not allowed" answer
     * Resets the game to the last phase calling phaseHandler and setting the allowed action, resets the SpecialsScene
     * @see SpecialsSceneController#resetScene()
     * @see MainSceneController#setActionAllowed(int)
     * @see GUI#phaseHandler(String)
     */
    public void specialNotAllowed(){
        SpecialsSceneController specialsSceneController = (SpecialsSceneController) sceneControllersMap.get(SPECIALS);
        MainSceneController mainSceneController = (MainSceneController) sceneControllersMap.get(MAIN);
        specialsSceneController.resetScene();
        if(constants.lastPhase().equals("ChooseCloud")) phaseHandler("ChooseCloud");
        else if(constants.lastPhase().equals("MoveStudent")) mainSceneController.setActionAllowed(0);
        else if(constants.lastPhase().equals("MoveMother")) mainSceneController.setActionAllowed(1);
    }
    /**
     * Sets the gameRestored to true, if the game is already initialized
     */
    public void setGameRestored(){
        gameRestored = true;
    }
    /**
     * Used to recover the last phase of the game when a game is restored from file.
     * When the state is set to succeeded, calls the phaseHandler for the last phase
     * @see Service
     */
    private class GetPhaseService extends Service<String>{

        /**
         * @see Task
         * @return last phase of the game
         */
        @Override
        protected Task<String> createTask() {
            return new Task<>(){
                @Override
                protected String call() {
                    String phase = proxy.getPhase();
                    return phase;
                }
            };
        }
        /**
         * @see Task
         */
        @Override
        protected void succeeded(){
            String phase = getPhaseService.getValue();
            if(phase.equals("Play card!")) phaseHandler("PlayCardAnswer");
            else if(phase.equals("Start your Action Phase!")) {
                setConstants("CardPlayed");
                phaseHandler("StartTurnAnswer");
            }
        }
    }

    /**
     * Called after the login phase, calls the proxy to see if the client can start the planning phase of the game
     * When the state is set to succeeded, calls the phaseHandler method for the PlayCardAnswer phase
     * @see GUI#phaseHandler(String)
     * @see Service
     */
    private class PlanningPhaseService extends Service<Boolean> {
        Boolean result;
        GUI gui;

        /**
        * Constructor for the Service
         * @param gui of type GUI - is the current gui
         */
        public PlanningPhaseService(GUI gui) {
            this.gui = gui;
            result = false;
        }
        /**
         * @see Task
         * @return a boolean indicating if the client can start its planning phase
         */
        @Override
        protected Task<Boolean> createTask() {
            return new Task<>() {
                @Override
                protected Boolean call(){
                    result = proxy.startPlanningPhase();
                    return result;
                }
            };
        }
        /**
         * @see Task
         */
        @Override
        protected void succeeded() {
            phaseHandler("PlayCardAnswer");
        }
    }

    /**
     * Called after the client played an assistant card. It calls the proxy to see if it can start the action phase of the game
     * When the state is set to succeeded, calls the phaseHandler method for the StartTurn phase
     * @see Service
     */
    private class ActionPhaseService extends Service<Boolean> {
        /**
         * @see Task
         * @return a boolean indicating if the client can start its action phase
         */
        @Override
        protected Task<Boolean> createTask() {
            return new Task<>() {
                @Override
                protected Boolean call(){
                    Boolean result = proxy.startActionPhase();
                    return result;
                }
            };
        }
        /**
         * @see Task
         */
        @Override
        protected void succeeded() {
            phaseHandler("StartTurnAnswer");
        }
    }
    /**
     * Called after login phase, calls the proxy method to start the view, and sets the gui as view listener
     * When the state is set to succeeded, it set this.view to the returned value and calls the following phase
     */

    private class SetViewService extends Service<View> {
        private GUI gui;
        /**
         * Constructor for the Service
         * @param gui of type GUI - is the current gui
         */
        public SetViewService(GUI gui) {
            this.gui = gui;
        }

        /**
         * @see Task
         * @return the view for the current game
         */
        @Override
        protected Task<View> createTask() {
            return new Task<>() {
                @Override
                protected View call() {
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
        /**
         * @see Task
         */
        @Override
        protected void succeeded(){
            View view = setViewService.getValue();
            gui.view = view;
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setView(view);
            phaseHandler("InitializeMain");
        }
    }
    /**
     * Called after setView service, it calls the methods to initialize the MainScene of the game
     * When the state is set to succeeded, it calls setView method on the proxy and goes to the following phase
     */
    private class InitializeMainService extends Service<Boolean> {
        /**
         * @see Task
         * @return a boolean indicating if the MainScene has been initialized
         */
        @Override
        protected Task<Boolean> createTask() {
            return new Task<>() {
                @Override
                protected Boolean call() {
                    MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
                    CloudsSceneController cloudsSceneController = (CloudsSceneController) sceneControllersMap.get(CLOUDS);
                    CardsSceneController cardsSceneController = (CardsSceneController) sceneControllersMap.get(CARDS);

                    controller.setNumberOfPlayers(view.getNumberOfPlayers());
                    cloudsSceneController.initializeCloudScene(view.getNumberOfPlayers());
                    cloudsSceneController.disableConfirm();
                    cardsSceneController.disableConfirm();
                    controller.setExpertMode(view.getExpertMode());
                    controller.initializeScene();
                    return true;
                }
            };
        }
        /**
         * If there the user choose not to restore the game, it calls the phaseHandler for the planning phase,
         * else it start the service to get the last game phase and restoring it
         * @see Task
         */
        @Override
        protected void succeeded(){
            switchScene(MAIN);
            proxy.setView();
            if(!gameRestored){
                phaseHandler("PlanningPhase");
            }
            else {
                switchScene(MAIN);
                getPhaseService.start();
            }
        }
    }
    /**
     * Initialize the scene when the game is restored
     * @param numberOfPlayer of type int - number of players for this game
     * @param expertMode of type boolean - indicates if the game is in expert mode or not
     */
    private void initializedSavedScene(int numberOfPlayer, boolean expertMode) {
        SavedSceneController controller = (SavedSceneController) sceneControllersMap.get(SAVED);
        String expertModeString;
        if (expertMode) expertModeString = "YES";
        else expertModeString = "NO";
        controller.initializedScene(numberOfPlayer, expertModeString);
    }
    /**
     * Used to load a scene in a new stage (window), instead of the primaryStage
     * @param sceneName of type String - name of the scene to load
     */
    public void loadScene(String sceneName) {
        Stage stage = new Stage();
        stage.setScene(scenesMap.get(sceneName));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
        stage.setTitle("Eriantys");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
    /**
     * Sets the proxy for this client
     * @param proxy of type Exit - proxy
     */
    public void setProxy(Exit proxy) {
        this.proxy = proxy;
        proxy.setServerOfflineListener(this);
        proxy.setDisconnectedListener(this);
    }
    /**
     * Used to set the nickname of the current player in the Main Scene
     * @param yourNickname of type String - the nickname of the player on this client
     * @see MainSceneController#setYourNickname(String)
     */
    public void setYourNickname(String yourNickname) {
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        controller.setYourNickname(yourNickname);
    }

    /**
     * Sets the actionAllowed to -1 in MainSceneController, meaning the player cannot perform any action, as it's not his turn
     * @see MainSceneController#setActionAllowed(int) 
     */
    public void setNotYourTurn() {
        MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
        controller.setActionAllowed(-1);
    }

    /** Sets the given constant to true. If the constant is SpecialUsed, meaning the user tried to use a special, reset the SpecialScene
     * @param phase of type String - the name of the phase
     */
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
    /**
     * @see UserInfoListener
     */
    @Override
    public void userInfoNotify(String nickname, String character, int playerRef) {
        Platform.runLater(()->{
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setNickname(nickname, playerRef);
            controller.setCharacter(character, playerRef);
        });
    }

    /**
     * @see CoinsListener
     */
    @Override
    public void notifyNewCoinsValue(int playerRef, int newCoinsValue) {
        Platform.runLater(()->{
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setNewCoinsValue(playerRef, newCoinsValue);
        });
    }
    /**
     * @see SpecialListener
     */
    @Override
    public void notifySpecial(int specialRef, int playerRef) {
        Platform.runLater(()->{
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setSpecialPlayed(specialRef, playerRef);
        });
    }
    /**
     * @see SpecialListener
     */
    @Override
    public void notifySpecialList(ArrayList<Integer> specialList, ArrayList<Integer> cost) {
        Platform.runLater(() -> {
            SpecialsSceneController controller = (SpecialsSceneController) sceneControllersMap.get(SPECIALS);
            controller.initializedSpecialsScene(specialList, cost);
        });
    }
    /**
     * @see SpecialListener
     */
    @Override
    public void notifyIncreasedCost(int specialRef, int newCost) {
        Platform.runLater(() -> {
            SpecialsSceneController controller = (SpecialsSceneController) sceneControllersMap.get(SPECIALS);
            controller.setCoins(specialRef, newCost);
        });
    }
    /**
     * @see InhibitedListener
     */
    @Override
    public void notifyInhibited(int islandRef, int isInhibited) {
        Platform.runLater(() -> {
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setInhibitedIsland(islandRef, isInhibited);
        });
    }
    /**
     * @see IslandListener
     */
    @Override
    public void notifyIslandChange(int islandToDelete) {
        Platform.runLater(() -> {
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.unifyIsland(islandToDelete);
        });
    }
    /**
     * @see MotherPositionListener
     */
    @Override
    public void notifyMotherPosition(int newMotherPosition) {
        Platform.runLater(() -> {
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setMotherPosition(newMotherPosition);
        });
    }
    /**
     * @see PlayedCardListener
     */
    @Override
    public void notifyPlayedCard(int playerRef, String assistantCard) {
        Platform.runLater(() -> {
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setLastPlayedCard(playerRef, assistantCard);
        });

    }
    /**
     * @see PlayedCardListener
     */
    @Override
    public void notifyHand(int playerRef, ArrayList<String> hand) {
    }
    /**
     * @see ProfessorsListener
     */
    @Override
    public void notifyProfessors(int playerRef, int color, boolean newProfessorValue) {
        Platform.runLater(() -> {
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setProfessor(playerRef, color, newProfessorValue);
        });
    }

    /**
     * Sets the new number of students in the respective sceneController
     * @see StudentsListener
     */
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

    /**
     * @see RestoreCardsListener
     */
    @Override
    public void restoreCardsNotify(ArrayList<String> hand) {
        Platform.runLater(() -> {
           CardsSceneController controller = (CardsSceneController) sceneControllersMap.get(CARDS);
           controller.restoreCards(hand);
        });

    }
    /**
     * @see TowersListener
     */
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
    /**
     * @see TowersListener
     */
    @Override
    public void notifyTowerColor(int islandRef, int newColor) {
        Platform.runLater(() -> {
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setTowerColor(islandRef, newColor);
        });
    }
    /**
     * @see SpecialStudentsListener
     */
    @Override
    public void specialStudentsNotify(int special, int color, int value) {
        Platform.runLater(() -> {
            SpecialsSceneController controller = (SpecialsSceneController) sceneControllersMap.get(SPECIALS);
            controller.setStudent(special, color, value);
        });
    }
    /**
     * @see NoEntryListener
     */
    @Override
    public void notifyNoEntry(int special, int newValue) {
        Platform.runLater(() -> {
            SpecialsSceneController controller = (SpecialsSceneController) sceneControllersMap.get(SPECIALS);
            controller.setNoEntry(special, newValue);
        });
    }
    /**
     * Loads the GameOver scene when a client disconnects
     * @see DisconnectedListener
     */
    @Override
    public void notifyDisconnected() {
        Platform.runLater(()->{
            GameOverSceneController controller = (GameOverSceneController) sceneControllersMap.get(GAMEOVER);
            if(primaryStage!=null && controller!=null){
                controller.setClientDisconnected();
                loadScene(GAMEOVER);
                primaryStage.close();
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + GAMEOVER));
                try {
                    primaryStage.setScene(new Scene(loader.load()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }
    /**
     * Loads the GameOver scene when the server is offline
     * @see ServerOfflineListener
     */
    @Override
    public void notifyServerOffline() {
        Platform.runLater(()->{
            GameOverSceneController controller = (GameOverSceneController) sceneControllersMap.get(GAMEOVER);
            if(primaryStage!=null && controller!=null){
                controller.setServerOffline();
                loadScene(GAMEOVER);
                primaryStage.close();

            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + GAMEOVER));
                try {
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * Loads the GameOver scene when somebody wins the game
     * @see WinnerListener
     */
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



