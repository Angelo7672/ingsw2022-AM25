package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GameOverSceneController implements SceneController {
    GUI gui;
    Exit proxy;
    int exitStatus;

    @FXML private Button exitButton;
    @FXML private Label gameOverLabel;
    @FXML private Label messageLabel;

    @FXML
    void exitGame(ActionEvent event) {
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

    public void setServerOffline() {
        exitStatus=-1;
        messageLabel.setText("Server is offline");
        messageLabel.setVisible(true);
    }

    public void setClientDisconnected() {
        exitStatus=-1;
        messageLabel.setText("Client disconnected");
        messageLabel.setVisible(true);
    }

    public void setWinner(String winner) {
        exitStatus=0;
        messageLabel.setText("The winner is: "+winner);
        messageLabel.setVisible(true);
    }
}
