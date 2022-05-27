package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Entrance;
import it.polimi.ingsw.client.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;

public class GUI extends Application {

    private Entrance proxy;
    private View view;
    private final Socket socket;
    private LoginSceneController loginSceneController;

    public GUI(Socket socket) throws IOException {
        this.socket = socket;
        //proxy = new Proxy_c(socket,this);
        this.loginSceneController = new LoginSceneController();
    }

    public static void main(String[] args){
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("/fxml/LoginScene.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
        Scene loginScene = new Scene(root);
        stage.setScene(loginScene);
        stage.setTitle("Eryantis");
        stage.setResizable(false);
        stage.show();

    }
    public void setupConnection(String nickname, String character) throws IOException, ClassNotFoundException {
        proxy.setupConnection(nickname,character);
    }

    public void setupGame(int numberOfPlayers, String expertMode) throws IOException, ClassNotFoundException {
        proxy.setupGame(numberOfPlayers, expertMode);
    }

    public void switchScene(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("/fxml/MainScene.fxml"));
        Scene startMenu = new Scene(root, 500, 500);
        stage.setScene(startMenu);
        stage.setTitle("Eryantis");
        stage.setResizable(false);
        stage.show();
    }



}
