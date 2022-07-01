package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * CardsSceneController is the controller of the CardsScene.fxml
 */
public class CardsSceneController implements SceneController{
    private String playedCard;
    private GUI gui;
    private Exit proxy;
    private final HashMap<Integer, String> currentPlayedCards;

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
    @FXML private Label errorMessage;
    @FXML private Button confirmButton;
    @FXML private Label cardsLabel;

    /**
     * Constructor method, creates an instance of CardsSceneController and initializes the attributes
     */
    public CardsSceneController(){
        currentPlayedCards= new HashMap<>();
        playedCard="";
    }

    /**
     * Service called when the user plays an assistant card, calls proxy method
     * When the state is set to succeeded goes to the following phase
     */
    public class PlayCardService extends Service<String>{

        @Override
        protected Task<String> createTask() {
            return new Task<>() {
                @Override
                protected String call(){
                    String result = null;
                    result = proxy.playCard(playedCard);
                    return result;
                }
            };

        }
        @Override
        protected void succeeded(){
            String result = this.getValue();
            if (result.equalsIgnoreCase("ok")) {
                gui.setConstants("CardPlayed");
                errorMessage.setVisible(false);
                disableCard(playedCard);
                disableConfirm();
                Stage stage = (Stage) confirmButton.getScene().getWindow();
                stage.close();
                gui.setConstants("CardPlayed");
                gui.phaseHandler("ActionPhase");
            } else
                showErrorMessage();
        }
    }

    /**
     * Used to restore your assistant cards
     * @param card of type ArrayList<String> - cards that have already been played
     */
    public void restoreCards(ArrayList<String> card){
        for (String s : card) {
            disableCard(s);
        }
    }

    /**
     * Sets the card chosen based on what button has been clicked by the user
     * @param event
     */
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

    /**
     * When the button confirm is pressed, starts PlayCardService
     * @param event
     */
    @FXML
    void confirmPressed(ActionEvent event) {
        if (!playedCard.equals("")) {
            PlayCardService playCardService = new PlayCardService();
            playCardService.start();
        } else {
            showErrorMessage();
        }
    }
    public void showErrorMessage(){
        errorMessage.setVisible(true);
    }

    /**
     * Hides the cards that have already been used by the player
     * @param card of type String - card to hide
     */
    public void disableCard(String card){
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

    @Override
    public void setGUI(GUI gui) {
        this.gui=gui;
    }

    @Override
    public void setProxy(Exit proxy) {
        this.proxy=proxy;
    }

    /**
     * Enables the confirm button
     */
    public void enableConfirm(){
        this.confirmButton.setDisable(false);
        this.cardsLabel.setVisible(true);
    }
    /**
     * Disables the confirm button
     */
    public void disableConfirm(){
        this.confirmButton.setDisable(true);
        this.cardsLabel.setVisible(false);
    }

}
