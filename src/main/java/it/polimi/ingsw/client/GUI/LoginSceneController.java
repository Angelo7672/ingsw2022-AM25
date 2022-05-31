package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Proxy_c;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginSceneController {

    private GUI gui;
    private Stage stage;
    private Scene scene;
    private String currentNickname;
    private String currentCharacter;
    private Proxy_c proxy;

    @FXML private TextField nicknameBox;
    @FXML private AnchorPane loginScene;
    @FXML private Button wizard;
    @FXML private Button witch;
    @FXML private Button samurai;
    @FXML private Button king;
    @FXML private Button next;


    public void setCharacter(ActionEvent e){
        if(e.getSource()== wizard)
            this.currentCharacter="wizard";
        else if(e.getSource()==samurai)
            this.currentCharacter="samurai";
        else if(e.getSource()==witch)
            this.currentCharacter="witch";
        else if(e.getSource()==king)
            this.currentCharacter="king";
    }

    public void nextPressed(ActionEvent e) throws IOException, ClassNotFoundException {
        this.currentNickname =nicknameBox.getText();
        System.out.println(currentNickname +", "+ currentCharacter);
        //gui.setupConnection(currentNickname, currentCharacter);
        switchScene();
    }
    public void setGui(GUI gui) {
        this.gui=gui;
    }

    public void setProxy(Proxy_c proxy) {
        this.proxy=proxy;
    }
    /*



    public void printNicknameMessage(){
        this.nicknameMessage.setText("The nickname is already taken");
        this.nicknameMessage.setVisible(true);
    }
    public void hideNicknameMessage(){
        this.nicknameMessage.setVisible(false);
    }

    public void disableCharacter(String character){
        //se il personaggio è già stato scelto
        if(character == "wizard")
            wizard.setImage(new Image(getClass().getResourceAsStream("graphics/character_king_taken.png")));
        else if(character == "samurai")
            samurai.setImage(new Image(getClass().getResourceAsStream("graphics/character_samurai_taken.png")));
        //witch
        //king
    }
*/
    public void switchScene() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("/fxml/MainScene.fxml"));
        Scene startMenu = new Scene(root);
        stage.setScene(startMenu);
        stage.show();
    }
}
