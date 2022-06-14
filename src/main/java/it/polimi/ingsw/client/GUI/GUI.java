package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
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

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class GUI extends Application implements TowersListener, ProfessorsListener, PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener {

    private static Exit proxy;
    private View view;
    private Socket socket;
    public Stage primaryStage;
    private SceneController currentSceneController;
    private Scene currentScene;
    private int numberOfPlayers;
    private boolean expertMode;
    private HashMap<Integer, String> nicknames;
    private HashMap<Integer, String> characters;
    protected static final String SETUP = "SetupScene.fxml";
    protected static final String LOGIN = "LoginScene.fxml";
    protected static final String WAITING = "WaitingScene.fxml";
    protected static final String MAIN = "MainScene.fxml";
    protected static final String CARDS = "CardsScene.fxml";

    private HashMap<String, Scene> scenesMap;
    private HashMap<String, SceneController > sceneControllersMap;
    private ArrayList<String> sceneNames;

    public static void main(String[] args) {
        launch();
    }

    public GUI() {

        nicknames = new HashMap<>();
        characters = new HashMap<>();
        scenesMap = new HashMap<>();
        sceneControllersMap = new HashMap<>();
        sceneNames=new ArrayList<>();

    }

    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException, InterruptedException {
        primaryStage = stage;
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
        primaryStage.setTitle("Eriantys");
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();


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
            //if(!savedGame()){
            //    loadScene(stage,SETUP);
            //}
        }
        else if (result.equals("SetupGame")) {
            loadScene(stage, SETUP);
            //sceneSetup(stage, SETUP);
        } else if (result.equals("Server Sold Out")) {
            System.out.println(result);
        } else if (result.equals("Not first")) {
            loadScene(stage, LOGIN);
            //sceneSetup(stage, LOGIN);
        }

    }

    /*
    public void sceneSetup(Stage stage, String sceneName) {

        loadScene(stage, sceneName);

        if (sceneName == LOGIN) {
            initializeLoginScene();
            //loadScene(stage, LOGIN);

        } else if (sceneName == MAIN) {
            initializeMainScene();
            //loadScene(stage, MAIN);

        }
        stage.setScene(scenesMap.get(sceneName));
        stage.centerOnScreen();
        stage.show();

    }*/

    public void loadScene(Stage stage, String sceneName) {

        Parent root = null;
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("/fxml/" + sceneName));
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
        currentScene = new Scene(root);
        scenesMap.put(sceneName, currentScene);
        currentSceneController = loader.getController();
        currentSceneController.setGUI(this);
        currentSceneController.setProxy(proxy);
        sceneControllersMap.put(sceneName, currentSceneController);

        System.out.println("Current scene: " + currentScene);
        System.out.println("Current controller: " + currentSceneController);

        stage.setScene(currentScene);
        stage.centerOnScreen();
    }

    /*
    public void setupView(MainSceneController controller) {
        view.setCoinsListener(controller);
        view.setInhibitedListener(controller);
        view.setIslandListener(controller);
        view.setMotherPositionListener(controller);
        view.setPlayedCardListener(controller);
        view.setProfessorsListener(controller);
        view.setStudentsListener(controller);
        view.setTowersListener(controller);
    }*/
    public void setupView(){
        view.setCoinsListener(this);
        view.setInhibitedListener(this);
        view.setIslandListener(this);
        view.setMotherPositionListener(this);
        view.setPlayedCardListener(this);
        view.setProfessorsListener(this);
        view.setStudentsListener(this);
        view.setTowersListener(this);
    }

    public void switchScene(String sceneName) {
        System.out.println("switched scene to " + sceneName);
        loadScene(primaryStage, sceneName);
        System.out.println("loaded scene" + sceneName);
        if (sceneName == LOGIN) {
            //initializeLoginScene();
        } else if (sceneName == MAIN) {
            initializeMainScene();
            /*Platform.runLater(()->{
                MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
                setupView(controller);
                controller.setView(this.view);
                controller.initialize();
                controller.startMainScene();
            });*/

            //primaryStage.show();
        } else if (sceneName == WAITING) {
            setView();

        }
        primaryStage.show();
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setProxy(Proxy_c proxy) {
        this.proxy = proxy;
    }

    public void setView(){
        Platform.runLater(()->{
            try {
                System.out.println("view not started yet");

                View view = proxy.startView();
                if(view!=null){
                    this.view=view;
                    setupView();
                    System.out.println("view started");
                    switchScene(MAIN);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });



    }

    public void initializeLoginScene() {
        System.out.println("initializeLoginScene");
        Platform.runLater(()->{
            //LoginSceneController controller = (LoginSceneController) currentSceneController;
            LoginSceneController controller = (LoginSceneController) sceneControllersMap.get(LOGIN);
            try {
                ArrayList<String> characters = proxy.getChosenCharacters();
                System.out.println("getting chosen characters");
                controller.disableCharacters(characters);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });


    }

    public void initializeMainScene() {
        System.out.println("initializeMainScene");

        Platform.runLater(()->{

            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setNumberOfPlayers(view.getNumberOfPlayers());
            controller.setExpertMode(view.getExpertMode());
            for (int i = 0; i < view.getNumberOfPlayers(); i++)
                controller.setUserInfo(view.getNickname(i), view.getCharacter(i), i);

            controller.startMainScene();
            controller.showCards();
        });
    }


    @Override
    public void notifyNewCoinsValue(int playerRef, int newCoinsValue) {

    }

    @Override
    public void notifyInhibited(int islandRef, int isInhibited) {

    }

    @Override
    public void notifyIslandChange(int islandToDelete) {

    }

    @Override
    public void notifyMotherPosition(int newMotherPosition) {

    }

    @Override
    public void notifyPlayedCard(int playerRef, String assistantCard) {

    }

    @Override
    public void notifyHand(int playerRef, ArrayList<String> hand) {

    }

    @Override
    public void notifyProfessors(int playerRef, int color, boolean newProfessorValue) {

    }

    @Override
    public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {

    }

    @Override
    public void notifyTowersChange(int place, int componentRef, int towersNumber) {

    }

    @Override
    public void notifyTowerColor(int islandRef, int newColor) {

    }
}



