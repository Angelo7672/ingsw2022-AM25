package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class CardsSceneController implements SceneController{
    private String playedCard;
    private GUI gui;
    private Exit proxy;
    private ArrayList<String> alreadyPlayedCards;

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

    public CardsSceneController(){
        alreadyPlayedCards= new ArrayList<>();
    }

    @FXML
    public void setPlayedCard(ActionEvent event) {
        if(event.getSource()==lionButton){
            playedCard="LION";
        } else if(event.getSource()==gooseButton){
            playedCard="GOOSE";
        } else if(event.getSource()==catButton){
            playedCard="CAT";
        } else if(event.getSource()==eagleButton){
            playedCard="EAGLE";
        } else if(event.getSource()==foxButton){
            playedCard="FOX";
        } else if(event.getSource()==lizardButton){
            playedCard="LIZARD";
        } else if(event.getSource()==octopusButton){
            playedCard="OCTOPUS";
        } else if(event.getSource()==dogButton) {
            playedCard = "DOG";
        } else if(event.getSource()==elephantButton) {
            playedCard = "ELEPHANT";
        } else if(event.getSource()==turtleButton) {
            playedCard = "TURTLE";
        }
    }

    //quello che succede quando viene premuto il bottone confirm
    @FXML
    void confirmPressed(ActionEvent event) {
        //Platform.runLater(()->{
            try {
                String result = proxy.playCard(playedCard); //qui chiama il metodo del proxy
                Stage stage = (Stage) confirmButton.getScene().getWindow();
                stage.close();
                alreadyPlayedCards.add(playedCard);

                //se la risposta è ok, chiude la scena
                if (result.equalsIgnoreCase("ok")) {
                    //Platform.runLater(()->{
                        gui.constants.setCardPlayed(true);
                        //Stage stage = (Stage) confirmButton.getScene().getWindow();
                        //stage.close();
                        gui.switchScene(GUI.MAIN);
                        //questo fa chiamare di nuovo il metodo startGame in MainSceneController

                    //});

                } else if(result.equalsIgnoreCase("move not allowed")){
                    System.out.println("move not allowed");
                } else {
                    System.out.println("Error");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        //});

    }

    public void sceneInitialize(){
        //Platform.runLater(()->{
            for(String card : alreadyPlayedCards){
                if(card.equalsIgnoreCase("lion")){
                    lionButton.setVisible(false);
                } else if(card.equalsIgnoreCase("goose")){
                    gooseButton.setVisible(false);
                } else if(card.equalsIgnoreCase("cat")){
                    catButton.setVisible(false);
                } else if(card.equalsIgnoreCase("eagle")){
                    eagleButton.setVisible(false);
                } else if(card.equalsIgnoreCase("fox")){
                    foxButton.setVisible(false);
                } else if(card.equalsIgnoreCase("lizard")){
                    lizardButton.setVisible(false);
                } else if(card.equalsIgnoreCase("octopus")){
                    octopusButton.setVisible(false);
                } else if(card.equalsIgnoreCase("dog")){
                    dogButton.setVisible(false);
                } else if(card.equalsIgnoreCase("elephant")){
                    elephantButton.setVisible(false);
                } else if(card.equalsIgnoreCase("turtle")){
                    turtleButton.setVisible(false);
                }
            }
        //});

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
