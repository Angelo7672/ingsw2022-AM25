package it.polimi.ingsw.client.GUI;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;


public class MainSceneController {
    private GUI gui;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ArrayList<Label> islands;
    private ArrayList<Label> schoolBoards;

    public MainSceneController(){
    }

}
