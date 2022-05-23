package it.polimi.ingsw.client.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends Application {

    public static void main(String[] args){
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("src/main/resources/MainScene.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("src/main/resources/graphics/eriantys_logo.png")));
        Scene startMenu = new Scene(root, 500, 500);
        stage.setScene(startMenu);
        stage.setTitle("Eryantis");
        stage.setResizable(false);
        stage.show();

    }



}
