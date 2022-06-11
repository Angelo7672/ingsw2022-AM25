package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.Proxy_c;
import it.polimi.ingsw.client.View;
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
    protected static final String SETUP = "setupScene.fxml";
    protected static final String LOGIN = "loginScene.fxml";
    protected static final String MAIN = "mainScene.fxml";

    public static void main(String[] args) {
        launch();
    }

    public GUI() {
        userInfo = new HashMap<>();
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
        if (result.equals("SetupGame")) {
            //loadScene(stage, SETUP);
            sceneSetup(stage, SETUP);
        } else if (result.equals("Server Sold Out")) {
            System.out.println(result);
        } else if (result.equals("Not first")) {
            //loadScene(stage, LOGIN);
            sceneSetup(stage, LOGIN);
        }

    }

    public void sceneSetup(Stage stage, String sceneName) {
        loadScene(stage, sceneName);

        if (sceneName == LOGIN) {
            initializeLoginScene();
            //loadScene(stage, LOGIN);

        } else if (sceneName == MAIN) {
            initializeMainScene();
            //loadScene(stage, MAIN);

        }/*
        stage.setScene(currentScene);
        stage.centerOnScreen();
        stage.show();*/

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
        currentSceneController = loader.getController();
        currentSceneController.setGUI(this);
        currentSceneController.setProxy(proxy);

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

    public void switchScene(String scene) {
        loadScene(primaryStage, scene);
        primaryStage.show();
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setProxy(Proxy_c proxy) {
        this.proxy = proxy;
    }


    public void initializeLoginScene() {
        Platform.runLater(()->{
            LoginSceneController controller = (LoginSceneController) currentSceneController;
            try {
                ArrayList<String> characters = proxy.getChosenCharacters();
                controller.disableCharacters(characters);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });


    }

    public void initializeMainScene() {
        Platform.runLater(()->{
            try {
            this.view = proxy.startView();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                MainSceneController controller = (MainSceneController) currentSceneController;
                setupView(controller);
                controller.setView(this.view);

                controller.setNumberOfPlayers(view.getNumberOfPlayers());
                controller.setExpertMode(view.getExpertMode());
                for (int i = 0; i < view.getNumberOfPlayers(); i++)
                    controller.setUserInfo(view.getNickname(i), view.getCharacter(i));
        });


    }
}



