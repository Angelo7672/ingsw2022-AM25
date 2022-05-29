package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class GUI extends Application {

    private View view;
    private Socket socket;

    public static void main(String[] args){
        launch();
    }


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        Parent root = loader.load(getClass().getResource("/fxml/LoginScene.fxml"));
        //stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/cranio_logo.png")));
        Scene loginScene = new Scene(root);
        stage.setScene(loginScene);
        //setup(stage);
        stage.setTitle("Eryantis");
        stage.setResizable(false);
        stage.show();

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
