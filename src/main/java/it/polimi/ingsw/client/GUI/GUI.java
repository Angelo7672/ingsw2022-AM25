package it.polimi.ingsw.client.GUI;

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
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener, BagListener,
        QueueListener  {

    private static Proxy_c proxy;
    private View view;
    private Socket socket;
    private LoginSceneController loginSceneController;
    private SetupSceneController setupSceneController;

    public static void main(String[] args){
        launch();
    }

        @Override
        public void init(){

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SetupScene.fxml"));
        try {
            Scene setupScene= new Scene(loader.load());
            SetupSceneController controller = loader.getController();
            controller.setGui(this);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void setup(Stage stage) {

        String result = null;
        try {
            result = proxy.first();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (result.equals("SetupGame")) {
            FXMLLoader loader = new FXMLLoader();

            Parent root = null;
            try {
                root = loader.load(getClass().getResource("/fxml/SetupScene.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
            Scene loginScene = new Scene(root);
            stage.setScene(loginScene);
            stage.setTitle("Eriantys");
            stage.setResizable(false);
            stage.show();
        } else if (result.equals("Server Sold Out")) {
            System.out.println(result);
        } else if (result.equals("Not first")) {

            FXMLLoader loader = new FXMLLoader();

            Parent root = null;
            try {
                root = loader.load(getClass().getResource("/fxml/LoginScene.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
            Scene loginScene = new Scene(root);
            stage.setScene(loginScene);
            stage.setTitle("Eriantys");
            stage.setResizable(false);
            stage.show();
        }
        /*
        try {
            view = proxy.startView();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        view.setBagListener(this);
        view.setCoinsListener(this);
        view.setInhibitedListener(this);
        view.setIslandListener(this);
        view.setMotherPositionListener(this);
        view.setPlayedCardListener(this);
        view.setProfessorsListener(this);
        view.setStudentsListener(this);
        view.setTowersListener(this);
    }*/
    }




    private Proxy_c getProxy() {
        return proxy;
    }

    @Override
    public void start(Stage stage) throws IOException {
        setup(stage);

    }



    public Parent sceneControllerSetup() throws IOException {
        FXMLLoader loader = FXMLLoader.load(getClass().getResource("/fxml/LoginScene.fxml"));
        loginSceneController = loader.getController();
        loginSceneController.setGui(this);
        return loader.load((getClass().getResource("/fxml/LoginScene.fxml")));
    }

    public static ArrayList<String> sendAvaiableCharacters() throws IOException, ClassNotFoundException {
        ArrayList<String> chosenCharacters = proxy.getChosenCharacters();
        ArrayList<String> availableCharacters = new ArrayList<>();
        if(!chosenCharacters.contains("WIZARD")) availableCharacters.add("WIZARD");
        if(!chosenCharacters.contains("KING")) availableCharacters.add("KING");
        if(!chosenCharacters.contains("WITCH")) availableCharacters.add("WITCH");
        if(!chosenCharacters.contains("SAMURAI")) availableCharacters.add("SAMURAI");
        return availableCharacters;

    }
    public static void setupConnection(String nickname, String character) throws IOException, ClassNotFoundException {
        if (proxy.setupConnection(nickname, character)) {
            System.out.println("SetupConnection done");
        }
        else System.out.println("Error, try again");


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
        }
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
            //controller.setTowersOnIsland();
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


    /*
    public void setup(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("/fxml/LoginScene.fxml"));

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
        Scene loginScene = new Scene(root);
        stage.setScene(loginScene);



    }*/
    public void setSetupSceneController(SetupSceneController controller){
        this.setupSceneController=controller;
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
    public void notifyProfessors(int playerRef, int color, boolean newProfessorValue) {

    }

    @Override
    public void notifyQueue(int playerRef) {

    }

    @Override
    public void notifyResetQueue() {

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


