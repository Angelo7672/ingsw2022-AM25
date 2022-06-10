package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.Proxy_c;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.controller.listeners.*;
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
import java.util.List;

public class GUI extends Application implements TowersListener, ProfessorsListener, SpecialListener, PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener, BagListener
       {

    private static Exit proxy;
    private static View view;
    private Socket socket;
    public Stage primaryStage;
    private SceneController currentSceneController;
    private Scene currentScene;
    private int numberOfPlayers;
    private String expertMode;
    private ArrayList<String> chosenCharacters;
    protected static final String SETUP = "setupScene.fxml";
    protected static final String LOGIN = "loginScene.fxml";
    protected static final String MAIN = "mainScene.fxml";

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
            loadScene(stage, SETUP);
        } else if (result.equals("Server Sold Out")) {
            System.out.println(result);
        } else if (result.equals("Not first")) {
            loadScene(stage, LOGIN);
        }
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

        stage.setScene(currentScene);
        stage.centerOnScreen();

        System.out.println("Current scene: "+ currentScene);
        System.out.println("Current controller: "+ currentSceneController);

        if (sceneName == MAIN) {
            //setupView();
        }
        else if(sceneName == LOGIN){
            Platform.runLater(()->{

                LoginSceneController controller = (LoginSceneController) currentSceneController;
                try {
                    ArrayList<String> characters=proxy.getChosenCharacters();
                    controller.disableCharacters(characters);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });

        }


    }

        /*
    public void setupView(SceneController controller){
        view.setBagListener(controller);
        view.setCoinsListener(controller);
        view.setInhibitedListener(this);
        view.setIslandListener(this);
        view.setMotherPositionListener(this);
        view.setPlayedCardListener(this);
        view.setProfessorsListener(this);
        view.setStudentsListener(this);
        view.setTowersListener(this);
    }  */

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


