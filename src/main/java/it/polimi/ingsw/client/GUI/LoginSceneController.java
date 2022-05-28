package it.polimi.ingsw.client.GUI;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class LoginSceneController {
    private GUI gui;
    private Stage stage;
    private Scene scene;
    private String currentNickname;
    private String currentCharacter;
    @FXML
    TextField nicknameBox;
    Label nicknameMessage;
    Button wizard;
    Button witch;
    Button samurai;

    public LoginSceneController(GUI gui){
        this.gui = gui;
    }
    public void nicknameSet(ActionEvent e){
        this.currentNickname = e.getSource().toString();
    }
    public void characterSet(ActionEvent e){
        this.currentCharacter= e.getSource().toString();
    }
    public void next(ActionEvent e) throws IOException, ClassNotFoundException {
        gui.setupConnection(currentNickname, currentCharacter);
    }

    public void printNicknameMessage(){
        this.nicknameMessage.setText("The nickname is already taken");
        this.nicknameMessage.setVisible(true);
    }
    public void hideNicknameMessage(){
        this.nicknameMessage.setVisible(false);
    }


}
