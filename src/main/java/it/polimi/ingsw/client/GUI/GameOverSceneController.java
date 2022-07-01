package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/**
 * GameOverSceneController is the controller of GameOverScene.fxml
 */
public class GameOverSceneController implements SceneController {
    GUI gui;
    Exit proxy;
    int exitStatus;

    @FXML private Button exitButton;
    @FXML private Label gameOverLabel;
    @FXML private Label messageLabel;

    public GameOverSceneController(){
        this.exitStatus = -1;
    }

    /**
     * When exit button is pressed, closes the gui
     * @param event of type ActionEvent - is the click on the button
     */
    @FXML
    void exitGame(ActionEvent event) {
        try {
            gui.stop();
        } catch (Exception e) {
            System.exit(exitStatus);
        }
        System.exit(exitStatus);
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void setProxy(Exit proxy) {
        this.proxy = proxy;
    }

    /**
     * When it is notified that the server is offline
     */
    public void setServerOffline() {
        exitStatus=-1;
        messageLabel.setText("Server is offline");
        messageLabel.setVisible(true);
    }

    /**
     * * When it is notified that one client disconnected
     */
    public void setClientDisconnected() {
        exitStatus=-1;
        messageLabel.setText("Client disconnected");
        messageLabel.setVisible(true);
    }

    /**
     *  When it is notified that one client won the game
     */
    public void setWinner(String winner) {
        exitStatus=0;
        messageLabel.setTextFill(Color.BLACK);
        messageLabel.setText("The winner is: "+winner);
        messageLabel.setVisible(true);
    }
    /**
     *  When it is notified Server sold out
     */
    public void setSoldOut() {
        exitStatus=-1;
        messageLabel.setText("Server Sold Out");
        messageLabel.setVisible(true);
    }
}
