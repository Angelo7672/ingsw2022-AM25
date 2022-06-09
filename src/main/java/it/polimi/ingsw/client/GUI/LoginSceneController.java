package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Proxy_c;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class LoginSceneController implements SceneController {
    private GUI gui;
    private String currentNickname;
    private String currentCharacter;
    private Proxy_c proxy;
    private ArrayList<String> chosenCharacters;
    private boolean characterTaken;

    @FXML private TextField nicknameBox;
    @FXML private AnchorPane loginScene;
    @FXML private Button wizard;
    @FXML private Button witch;
    @FXML private Button samurai;
    @FXML private Button king;
    @FXML private Button next;
    @FXML private ImageView wizardImage;
    @FXML private ImageView witchImage;
    @FXML private ImageView samuraiImage;
    @FXML private ImageView kingImage;
    @FXML private Label errorMessage;


    public LoginSceneController(){
    }
    public void setCharacter(ActionEvent e){
        if(e.getSource()==wizard)
            this.currentCharacter="wizard";
        else if(e.getSource()==samurai)
            this.currentCharacter="samurai";
        else if(e.getSource()==witch)
            this.currentCharacter="witch";
        else if(e.getSource()==king)
            this.currentCharacter="king";
    }

    public void nextPressed(ActionEvent e) throws IOException, ClassNotFoundException {
        Boolean characterAvailable=false;
        ArrayList<String> availableCharacters;

        this.currentNickname =nicknameBox.getText();
        System.out.println(currentNickname +", "+ currentCharacter);
        gui.setupConnection(currentNickname, currentCharacter);

       availableCharacters = gui.getAvailableCharacters();

        for(String character: availableCharacters) {
            if (character == currentCharacter) {
                characterAvailable = true;
            }
            if (characterAvailable == false) {
                errorMessage.setVisible(true);
            } else
                switchScene();
        }

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
*/
    public void disableCharacters(ArrayList<String> chosenCharacters) {

        //se il personaggio è già stato scelto
        for(String character: chosenCharacters){
            if(character == "WIZARD"){
                wizardImage.setImage((new Image(getClass().getResourceAsStream("graphics/character_wizard_taken.png"))));
                wizard.setDisable(true);
            }
            else if(character == "SAMURAI"){
                samuraiImage.setImage((new Image(getClass().getResourceAsStream("graphics/character_samurai_taken.png"))));
                samurai.setDisable(true);
            }
            else if(character == "WITCH"){
                witchImage.setImage((new Image(getClass().getResourceAsStream("graphics/character_witch_taken.png"))));
                witch.setDisable(true);
            }
            else if(character == "KING"){
                kingImage.setImage((new Image(getClass().getResourceAsStream("graphics/character_king_taken.png"))));
                king.setDisable(true);
            }
        }
            /*
            if (character == "wizard")
                wizard.setImage(new Image(getClass().getResourceAsStream("graphics/character_king_taken.png")));
            else if (character == "samurai")
                samurai.setImage(new Image(getClass().getResourceAsStream("graphics/character_samurai_taken.png")));*/
            //witch
            //king
        }

    public void switchScene() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        try {
            root = loader.load(getClass().getResource("/fxml/MainScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene startMenu = new Scene(root);
        stage.setScene(startMenu);
        stage.show();
    }


    @Override
    public void setGUI(GUI gui) {
        this.gui=gui;
    }


}
