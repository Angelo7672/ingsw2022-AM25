package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Proxy_c;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.controller.listeners.TowersListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.InputMismatchException;

public class GUI extends Application implements TowersListener  {

    private static Proxy_c proxy;
    private View view;
    private Socket socket;
    private LoginSceneController loginSceneController;
    private SetupSceneController setupSceneController;
    // private GUIcontroller currentController;

    public static void main(String[] args){
        launch();
    }

    public GUI() throws IOException {
        //loginSceneController = new LoginSceneController();
        //this.socket = socket;
        //this.proxy = new Proxy_c(socket, this);
    }
    @Override
    public void init(){

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SetupScene.fxml"));
        try {
            Scene loginScene= new Scene(loader.load());
            SetupSceneController controller = loader.getController();
            controller.setGui(this);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void setup(Stage stage) throws IOException, ClassNotFoundException {
        String result = proxy.first();
        if(result.equals("SetupGame")) {
            FXMLLoader loader = new FXMLLoader();

            Parent root = loader.load(getClass().getResource("/fxml/SetupScene.fxml"));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
            Scene loginScene = new Scene(root);
            stage.setScene(loginScene);
            stage.setTitle("Eriantys");
            stage.setResizable(false);
            stage.show();
        }
        else  if (result.equals("Server Sold Out")){
            System.out.println(result);
            return;
        }
        //setupConnection();
        view = proxy.startView();
    }

    private Proxy_c getProxy() {
        return proxy;
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            setup(stage);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }/*
        FXMLLoader loader = new FXMLLoader();

        Parent root = loader.load(getClass().getResource("/fxml/SetupScene.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
        Scene loginScene = new Scene(root);
        stage.setScene(loginScene);
        stage.setTitle("Eriantys");
        stage.setResizable(false);
        stage.show();*/

    }
    public Parent sceneControllerSetup() throws IOException {
        FXMLLoader loader = FXMLLoader.load(getClass().getResource("/fxml/LoginScene.fxml"));
        loginSceneController = loader.getController();
        loginSceneController.setGui(this);
        return loader.load((getClass().getResource("/fxml/LoginScene.fxml")));
    }

    public static void setupConnection(String nickname, String character) throws IOException, ClassNotFoundException {
        boolean ok = proxy.setupConnection(nickname, character);
        //System.out.println("ciao");
        System.out.println(ok);

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

}


