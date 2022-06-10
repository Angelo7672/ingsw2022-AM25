package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginSceneController implements SceneController, Initializable {
    private GUI gui;
    private String currentNickname;
    private String currentCharacter;
    private Exit proxy;
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
        this.currentNickname="";
        this.currentCharacter="";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //disableCharacters(gui.getChosenCharacters());
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
        currentNickname= this.nicknameBox.getText();
        System.out.println(currentNickname +", "+ currentCharacter);
        if(currentNickname!="" && currentCharacter!="") {
            if (proxy.setupConnection(currentNickname, currentCharacter))
                gui.switchScene(GUI.MAIN);
            else
                showErrorMessage();
        }else
            showErrorMessage();
    }

    public void showErrorMessage(){
        errorMessage.setVisible(true);
    }


    public void disableCharacters(ArrayList<String> chosenCharacters) {
        System.out.println(chosenCharacters);
        //se il personaggio è già stato scelto
        for(String character: chosenCharacters){
            if(character.equalsIgnoreCase("WIZARD")){
                wizardImage.setImage((new Image(getClass().getResourceAsStream("/graphics/character_wizard_taken.png"))));
                wizard.setDisable(true);
            }
            else if(character.equalsIgnoreCase("SAMURAI")){
                samuraiImage.setImage((new Image(getClass().getResourceAsStream("/graphics/character_samurai_taken.png"))));
                samurai.setDisable(true);
            }
            else if(character.equalsIgnoreCase("WITCH")){
                witchImage.setImage((new Image(getClass().getResourceAsStream("/graphics/character_witch_taken.png"))));
                witch.setDisable(true);
            }
            else if(character.equalsIgnoreCase("KING")){
                kingImage.setImage((new Image(getClass().getResourceAsStream("/graphics/character_king_taken.png"))));
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


    @Override
    public void setGUI(GUI gui) {
        this.gui=gui;
    }

    @Override
    public void setProxy(Exit proxy) {
        this.proxy=proxy;
    }



}
