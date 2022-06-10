package it.polimi.ingsw.client.GUI;
//setupScene is to be shown only to the first player to choose number of player and expert mode

import it.polimi.ingsw.client.Exit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class SetupSceneController implements SceneController {

    private int numberOfPlayers = 0;
    private String expertMode;
    private GUI gui;
    private Exit proxy;

    @FXML private AnchorPane setupScene;
    @FXML private Label message;
    @FXML private Button twoPlayersButton;
    @FXML private Button threePlayersButton;
    @FXML private Button fourPlayersButton;
    @FXML private Button yesButton;
    @FXML private Button noButton;
    @FXML private Button nextButton;

    public SetupSceneController(){
        this.numberOfPlayers=0;
        this.expertMode="";
    }


    public void setNumberOfPlayers(ActionEvent e) {
        if (e.getSource() == twoPlayersButton)
            this.numberOfPlayers = 2;
        else if (e.getSource() == threePlayersButton)
            this.numberOfPlayers = 3;
        else if (e.getSource() == fourPlayersButton)
            this.numberOfPlayers = 4;
    }

    public void setExpertMode(ActionEvent e) {
        if (e.getSource() == yesButton)
            this.expertMode = "y";
        else if (e.getSource() == noButton)
            this.expertMode = "n";

    }

    public void nextPressed(ActionEvent e) throws IOException, ClassNotFoundException {
        System.out.println(numberOfPlayers + ", " + expertMode);
        if(numberOfPlayers!=0 && expertMode!="") {
            if (proxy.setupGame(numberOfPlayers, expertMode) == true) {
                gui.switchScene(GUI.LOGIN);
            } else
                System.out.println("Errore");
        }
        else System.out.println("Errore");

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


