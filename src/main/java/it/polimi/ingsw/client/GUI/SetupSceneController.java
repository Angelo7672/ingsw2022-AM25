package it.polimi.ingsw.client.GUI;
//setupScene is to be shown only to the first player to choose number of player and expert mode

import it.polimi.ingsw.client.Proxy_c;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class SetupSceneController implements Initializable {

    private int numberOfPlayers;
    private String expertMode;
    private Proxy_c proxy;
    private GUI gui;

    @FXML private AnchorPane setupScene;
    @FXML private Label message;
    @FXML private Button twoPlayersButton;
    @FXML private Button threePlayersButton;
    @FXML private Button fourPlayersButton;
    @FXML private Button yesButton;
    @FXML private Button noButton;
    @FXML private Button nextButton;


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

    public void nextPressed(ActionEvent e) {
        System.out.println(numberOfPlayers + ", " + expertMode);

        if(/*gui.setupGame(numberOfPlayers, expertMode*/ true) {
            try {
                switchScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else
            System.out.println("Errore");
    }

    public void switchScene() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("/fxml/LoginScene.fxml"));
        Scene startMenu = new Scene(root);
        stage.setScene(startMenu);
        stage.show();
    }

    public void setGui(GUI gui){
        this.gui = gui;
    }
    public void setProxy(Proxy_c proxy){
        this.proxy=proxy;
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {

    }
}


