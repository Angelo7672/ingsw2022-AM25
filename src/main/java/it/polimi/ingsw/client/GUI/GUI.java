package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
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

public class GUI extends Application implements TowersListener  {

    private View view;
    private Socket socket;
    private Proxy_c proxy;
    private LoginSceneController loginSceneController;
    // private GUIcontroller currentController;

    public static void main(String[] args){
        launch();
    }

    public GUI() throws IOException {

        //this.socket = socket;
        //this.proxy = new Proxy_c(socket, this);
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        Parent root = loader.load(getClass().getResource("/fxml/LoginScene.fxml"));
        //LoginSceneController controller = loader.getController();
        //controller.setGui(this);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
        Scene loginScene = new Scene(root);
        stage.setScene(loginScene);
        stage.setTitle("Eriantys");
        stage.setResizable(false);
        stage.show();


    }/*
    public void sceneControllerSetup() throws IOException {
        FXMLLoader loader = FXMLLoader.load(getClass().getResource("/fxml/LoginScene.fxml"));
        LoginSceneController controller = loader.getController();
        controller.setGui(this);
    }*/

    public void setupConnection(String nickname, String character) throws IOException, ClassNotFoundException {
        //proxy.setupConnection(nickname, character);
        System.out.println("ciao");
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

}


