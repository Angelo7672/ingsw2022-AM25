package it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.client.Exit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
/**
 * SetupSceneController is the controller class of SetupScene.fxml
 * SetupScene is to be shown only to the first player to choose number of player and expert mode
 */
public class SetupSceneController implements SceneController {

    private int numberOfPlayers = 0;
    private String expertMode;
    private GUI gui;
    private Exit proxy;

    @FXML private AnchorPane setupScene;
    @FXML private Label message;
    @FXML private RadioButton twoPlayersButton;
    @FXML private RadioButton threePlayersButton;
    @FXML private RadioButton fourPlayersButton;
    @FXML private RadioButton yesButton;
    @FXML private RadioButton noButton;
    @FXML private Button nextButton;
    @FXML private Label errorMessage;

    /**
     * Constructor method, creates an instance of SetupSceneController and initializes the attributes
     */
    public SetupSceneController(){
        this.numberOfPlayers=0;
        this.expertMode="";
    }

    /**
     * Based on the source of the event, sets the number of players
     * @param e of type ActionEvent - the action performed on the button
     */
    public void setNumberOfPlayers(ActionEvent e) {
        if (e.getSource() == twoPlayersButton)
            this.numberOfPlayers = 2;
        else if (e.getSource() == threePlayersButton)
            this.numberOfPlayers = 3;
        else if (e.getSource() == fourPlayersButton)
            this.numberOfPlayers = 4;
    }

    /**
     * Based on the source of the event, sets the expert mode
     * @param e of type ActionEvent - the action performed on the button
     */
    public void setExpertMode(ActionEvent e) {
        if (e.getSource() == yesButton)
            this.expertMode = "y";
        else if (e.getSource() == noButton)
            this.expertMode = "n";
    }

    /**
     * Called when nextButton is pressed
     * Calls proxy method setupGame
     * @param e
     */
    public void nextPressed(ActionEvent e) {
        System.out.println(numberOfPlayers + ", " + expertMode);
        if(numberOfPlayers!=0 && expertMode!="") {
            if (proxy.setupGame(numberOfPlayers, expertMode) == true) {
                gui.switchScene(GUI.LOGIN);
            } else
                showErrorMessage();
        }
        else showErrorMessage();

    }

    public void showErrorMessage(){
        errorMessage.setVisible(true);
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


