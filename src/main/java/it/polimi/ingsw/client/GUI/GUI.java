package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.Proxy_c;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.controller.listeners.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class GUI extends Application implements TowersListener, ProfessorsListener, SpecialListener, PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener, BagListener
       {

    private static Exit proxy;
    private static View view;
    private Socket socket;
    public Stage primaryStage;
    private SceneController currentSceneController;
    private SetupSceneController setupSceneController;
    private MainSceneController mainSceneController;
    private LoginSceneController loginSceneController;
    private Scene currentScene;
    private int numberOfPlayers;
    private String expertMode;
    private ArrayList<String> chosenCharacters;
    ArrayList<String> scenes;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException, InterruptedException {
        primaryStage=stage;
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
        primaryStage.setTitle("Eriantys");
        primaryStage.setResizable(false);
        primaryStage.show();
        //controllerSetup("Setup");
        //controllerSetup("Login");
        //controllerSetup("Main");

        setup(primaryStage);

    }

    public void setup(Stage stage) throws IOException, ClassNotFoundException, InterruptedException {

        String result = null;
        try {
            result = proxy.first();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (result.equals("SetupGame")) {
            loadScene(stage, "Setup");

        } else if (result.equals("Server Sold Out")) {
            System.out.println(result);
        } else if (result.equals("Not first")) {

            loadScene(stage, "Login");
        }
    }

    public void loadScene(Stage stage, String sceneName) {
        Parent root = null;
        FXMLLoader loader = new FXMLLoader();
        try {
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + sceneName + "Scene.fxml"));
            loader.setLocation(getClass().getResource("/fxml/" + sceneName + "Scene.fxml"));
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
        currentScene = new Scene(root);
        currentSceneController = (SceneController) loader.getController();

        stage.setScene(currentScene);
        currentSceneController.setGUI(this);

        System.out.println("Current scene: "+ currentScene);
        System.out.println("Current controller: "+ currentSceneController);

        /*
        if (sceneName == "Main") {
            setupView();
        }*/
    }

    public void controllerSetup(String sceneName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + sceneName + "Scene.fxml"));
        Parent root = loader.load();

        SceneController sceneController= (SceneController) loader.getController();
        sceneController.setGUI(this);

    }



    public static boolean setupConnection(String nickname, String character) {
        try {
            if (proxy.setupConnection(nickname, character))
                return true;
            return false;

        } catch (IOException e) {
            e.printStackTrace();
            return false;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }


    }

    public static boolean setupGame(int numberOfPlayers, String expertMode) {
        while(true) {
            try {
                if (proxy.setupGame(numberOfPlayers, expertMode))
                    return true;
                return false;
            } catch (InputMismatchException e) {
                return false;
            } catch (IOException e) {
                System.err.println("io");
            } catch (ClassNotFoundException e) {
                System.err.println("class error");
            }
            try {
                view = proxy.startView();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setupView(){
        view.setBagListener(this);
        view.setCoinsListener(this);
        view.setInhibitedListener(this);
        view.setIslandListener(this);
        view.setMotherPositionListener(this);
        view.setPlayedCardListener(this);
        view.setProfessorsListener(this);
        view.setStudentsListener(this);
        view.setTowersListener(this);
    }

    public void switchScene(String scene){
        loadScene(primaryStage, scene);
        primaryStage.show();
    }


    public void setSocket(Socket socket){
        this.socket=socket;
    }
    public void setProxy(Proxy_c proxy){
        this.proxy=proxy;
    }

    public static ArrayList<String> getChosenCharaters() {
        ArrayList<String> chosenCharacters = null;
        try {
            chosenCharacters = proxy.getChosenCharacters();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }/*
        ArrayList<String> availableCharacters = new ArrayList<>();
        if(!chosenCharacters.contains("WIZARD")) availableCharacters.add("WIZARD");
        if(!chosenCharacters.contains("KING")) availableCharacters.add("KING");
        if(!chosenCharacters.contains("WITCH")) availableCharacters.add("WITCH");
        if(!chosenCharacters.contains("SAMURAI")) availableCharacters.add("SAMURAI");*/

        return chosenCharacters;

    }

    @Override
    public void notifyTowersChange(int place, int componentRef, int towersNumber) {
        try {
            FXMLLoader loader = FXMLLoader.load(getClass().getResource("/fxml/MainScene.fxml"));
            MainSceneController controller = loader.getController();
            if(place == 0){
                //controller.setTowersInSchool(componentRef, towersNumber);
            }
            else if ((place==1)) {
                controller.setTowersOnIsland(componentRef, towersNumber);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyTowerColor(int islandRef, int newColor) {
        try {
            FXMLLoader loader = FXMLLoader.load(getClass().getResource("/fxml/MainScene.fxml"));
            MainSceneController controller = loader.getController();
            //controller.setTowersOnIsland();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void notifyBagExtraction() {

    }


    @Override
    public void notifyBag(List<Integer> bag) {

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
    public void notifySpecial(int specialRef) {

    }

    @Override
    public void notifySpecialName(String specialName) {

    }

    @Override
    public void notifyPlayedSpecial(int specialRef) {

    }

   @Override
   public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {

   }
       }


