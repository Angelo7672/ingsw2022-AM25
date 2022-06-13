package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.Proxy_c;
import it.polimi.ingsw.client.View;
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

public class GUI extends Application {

    private static Exit proxy;
    private static View view;
    private Socket socket;
    public Stage primaryStage;
    private SceneController currentSceneController;
    private Scene currentScene;
    private int numberOfPlayers;
    private boolean expertMode;
    private HashMap<String, String> userInfo;
    protected static final String SETUP = "SetupScene.fxml";
    protected static final String LOGIN = "LoginScene.fxml";
    protected static final String MAIN = "MainScene.fxml";
    private HashMap<String, Scene> scenesMap;
    private HashMap<String, SceneController > sceneControllersMap;
    private ArrayList<String> sceneNames;

    public static void main(String[] args) {
        launch();
    }

    public GUI() {

        userInfo = new HashMap<>();
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

        //sceneSetup(stage, );

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


    public void sceneSetup(Stage stage, String sceneName) {
        /*
        Parent root = null;

        for(String sceneName : sceneNames) {
            FXMLLoader loader = new FXMLLoader();
            try {
                loader.setLocation(getClass().getResource(sceneName));
                root = loader.load();

            } catch (IOException e) {
                e.printStackTrace();
            }
            SceneController controller = loader.getController();
            scenesMap.put(sceneName, new Scene(root));
            sceneControllersMap.put(sceneName, controller);
            controller.setGUI(this);
            controller.setProxy(proxy);

        }*/

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

    }

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
        /*
        stage.setScene(currentScene);
        stage.centerOnScreen();
        stage.show();*/

        //stage.setScene(scenesMap.get(sceneName));

        System.out.println("Current scene: " + currentScene);
        System.out.println("Current controller: " + currentSceneController);

        stage.setScene(currentScene);
        stage.centerOnScreen();
    }



    public void setupView(MainSceneController controller) {

        view.setCoinsListener(controller);
        view.setInhibitedListener(controller);
        view.setIslandListener(controller);
        view.setMotherPositionListener(controller);
        view.setPlayedCardListener(controller);
        view.setProfessorsListener(controller);
        view.setStudentsListener(controller);
        view.setTowersListener(controller);
    }

    public void switchScene(String sceneName) {
        System.out.println("switched scene to "+ sceneName);
        loadScene(primaryStage, sceneName);
        System.out.println("loaded scene" + sceneName);
        if (sceneName == LOGIN) {
            initializeLoginScene();
            //loadScene(stage, LOGIN);

        } else if (sceneName == MAIN) {
            initializeMainScene();
            //loadScene(stage, MAIN);

        }
        primaryStage.show();
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setProxy(Proxy_c proxy) {
        this.proxy = proxy;
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
            try {
                System.out.println("view not started yet");
                this.view = proxy.startView();//this.view = new View(2, true); //
                System.out.println("view started");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            setupView(controller);
            controller.setView(this.view);

            controller.setNumberOfPlayers(view.getNumberOfPlayers());
            controller.setExpertMode(view.getExpertMode());
            for (int i = 0; i < view.getNumberOfPlayers(); i++)
                controller.setUserInfo(view.getNickname(i), view.getCharacter(i));

                    controller.startMainScene();
                    primaryStage.setScene(scenesMap.get(MAIN));
                    primaryStage.show();
        });


    }
}



