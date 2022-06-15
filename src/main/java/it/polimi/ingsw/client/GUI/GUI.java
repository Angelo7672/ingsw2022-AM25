package it.polimi.ingsw.client.GUI;

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

    protected static final String SETUP = "SetupScene.fxml";
    protected static final String LOGIN = "LoginScene.fxml";
    protected static final String WAITING = "WaitingScene.fxml";
    protected static final String MAIN = "MainScene.fxml";
    protected static final String CARDS = "CardsScene.fxml";
    private PlayerConstants constants;

    private HashMap<String, Scene> scenesMap; //maps the scene name with the scene itself
    private HashMap<String, SceneController > sceneControllersMap; // maps the scene name with the scene controller

    public static void main(String[] args) {
        launch();
    }

    public GUI() {
        scenesMap = new HashMap<>();
        sceneControllersMap = new HashMap<>();
        constants = new PlayerConstants();
        view = new View();
    }

    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException, InterruptedException {
        primaryStage = stage;
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
        primaryStage.setTitle("Eriantys");
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();

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

    //used to load a scene in a new stage (window), instead of the primaryStage
    public void loadScene(String sceneName) {
        Stage stage = new Stage();
        stage.setScene(scenesMap.get(sceneName));
        stage.centerOnScreen();
    }

    //set the GUI as listener for the updates from the View
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
    //called when the GUI is launched, load all the scenes in advance, mapping them and setting the controllers
    public void scenesSetup() {

        String[] scenes = new String[]{SETUP, LOGIN, MAIN, CARDS};
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
        System.out.println("switched to scene: "+ sceneName);
        primaryStage.show();
        primaryStage.centerOnScreen();

        if (sceneName.equals(LOGIN)) {
            //initializeLoginScene();

        } else if (sceneName.equals(MAIN)) {
            initializeMainScene();

        } else if (sceneName.equals(WAITING)) {
            //switched to when login is completed, calls setView
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;
            try {
                loader.setLocation(getClass().getResource("/fxml/" + sceneName));
                root = loader.load();
                primaryStage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("currently showing WAITING scene");
            setView();
        }
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setProxy(Proxy_c proxy) {
        this.proxy = proxy;
    }

    //tries to start the view while the gui is in WaitingScene, if succedes switches to main scene
    public void setView(){
        Platform.runLater(()->{
            try {
                System.out.println("view not started yet");

                View view = proxy.startView();
                if(view!=null){
                    this.view = view;
                    setupView();
                    System.out.println("view started");
                    switchScene(MAIN);
                }
                else System.out.println("Errore");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
    /*
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
    }*/

    //called when main scene is set in switchScene method
    public void initializeMainScene() {
        System.out.println("initializeMainScene");

        Platform.runLater(()->{

            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            controller.setNumberOfPlayers(view.getNumberOfPlayers());
            controller.setExpertMode(view.getExpertMode());
            for (int i = 0; i < view.getNumberOfPlayers(); i++)
                controller.setUserInfo(view.getNickname(i), view.getCharacter(i), i);

            controller.startMainScene();
            //controller.showCards();
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
        /*
        Platform.runLater(()->{
            //loadScene(primaryStage, MAIN);
            MainSceneController controller = (MainSceneController) sceneControllersMap.get(MAIN);
            switch (place) {
                case 0 -> controller.setStudentsEntrance(componentRef, color, newStudentsValue);
                case 1 -> controller.setStudentsTable(componentRef, color, newStudentsValue);
                case 2 -> controller.setStudentsIsland(componentRef, color, newStudentsValue);
                case 3 -> controller.setStudentsCloud(componentRef, color, newStudentsValue);
            }
        });*/
    }

    @Override
    public void notifyTowersChange(int place, int componentRef, int towersNumber) {

    }

    @Override
    public void notifyTowerColor(int islandRef, int newColor) {

    }
}



