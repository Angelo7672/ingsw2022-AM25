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
    private SceneController currentSceneController;
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
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
        stage.setTitle("Eriantys");
        stage.setResizable(false);
        stage.show();

        //controllersSetup();
        setup(stage);
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + sceneName + "Scene.fxml"));
            root = loader.load();
            SceneController controller = (SceneController) loader.getController();
            controller.setGUI(this);
            currentSceneController=controller;

        } catch (IOException e) {
            e.printStackTrace();
        }
        currentScene = new Scene(root);
        stage.setScene(currentScene);

        if (sceneName == "Main") {
            setupView();
        }
    }
/*
    public void controllersSetup(){
        for(String sceneName : scenes){

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + sceneName + "Scene.fxml"));
            try {
                Parent root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SceneController sceneController= loader.getController();
            sceneController.setGUI(this);
        }
    }*/



    public static void setupConnection(String nickname, String character) {
        try {
            if (proxy.setupConnection(nickname, character)) {
                System.out.println("SetupConnection done");
            }
            else System.out.println("Error, try again");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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


    public void setSocket(Socket socket){
        this.socket=socket;
    }
    public void setProxy(Proxy_c proxy){
        this.proxy=proxy;
    }

    public static ArrayList<String> getAvailableCharacters() {
        ArrayList<String> chosenCharacters = null;
        try {
            chosenCharacters = proxy.getChosenCharacters();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String> availableCharacters = new ArrayList<>();
        if(!chosenCharacters.contains("WIZARD")) availableCharacters.add("WIZARD");
        if(!chosenCharacters.contains("KING")) availableCharacters.add("KING");
        if(!chosenCharacters.contains("WITCH")) availableCharacters.add("WITCH");
        if(!chosenCharacters.contains("SAMURAI")) availableCharacters.add("SAMURAI");

        return availableCharacters;

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


