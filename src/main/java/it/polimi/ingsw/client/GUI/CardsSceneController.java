package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class CardsSceneController implements SceneController{
    private String playedCard;
    private GUI gui;
    private Exit proxy;

    @FXML private Button lionButton;
    @FXML private Button gooseButton;
    @FXML private Button catButton;
    @FXML private Button eagleButton;
    @FXML private Button foxButton;
    @FXML private Button lizardButton;
    @FXML private Button octopusButton;
    @FXML private Button dogButton;
    @FXML private Button elephantButton;
    @FXML private Button turtleButton;

    @FXML private Button confirmButton;


    @FXML
    public void setPlayedCard(ActionEvent event) {
        if(event.getSource()==lionButton){
            playedCard="lion";
        } else if(event.getSource()==gooseButton){
            playedCard="goose";
        } else if(event.getSource()==catButton){
            playedCard="cat";
        } else if(event.getSource()==eagleButton){
            playedCard="eagle";
        } else if(event.getSource()==foxButton){
            playedCard="fox";
        } else if(event.getSource()==lizardButton){
            playedCard="lizard";
        } else if(event.getSource()==octopusButton){
            playedCard="octopus";
        } else if(event.getSource()==dogButton) {
            playedCard = "dog";
        } else if(event.getSource()==elephantButton) {
            playedCard = "elephant";
        } else if(event.getSource()==turtleButton) {
            playedCard = "turtle";
        }
    }

    @FXML
    void confirmPressed(ActionEvent event) {
        try {
            String answer = proxy.playCard(playedCard);
            //if(answer==)
            //
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void setGUI(GUI gui) {
        this.gui=gui;
    }

    @Override
    public void setProxy(Exit proxy) {
        this.proxy=proxy;
    }
}
