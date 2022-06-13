package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;

public class LoginSceneController implements SceneController{
    private GUI gui;
    private String currentNickname;
    private String currentCharacter;
    private Exit proxy;

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

    public void setCharacter(ActionEvent e){
        if(e.getSource()==wizard)
            this.currentCharacter="WIZARD";
        else if(e.getSource()==samurai)
            this.currentCharacter="SAMURAI";
        else if(e.getSource()==witch)
            this.currentCharacter="WITCH";
        else if(e.getSource()==king)
            this.currentCharacter="KING";
    }

    public void nextPressed(ActionEvent e) throws IOException, ClassNotFoundException {
        currentNickname= this.nicknameBox.getText();
        System.out.println(currentNickname +", "+ currentCharacter);
        if(currentNickname!="" && currentCharacter!="") {
            if (proxy.setupConnection(currentNickname, currentCharacter)){
                gui.switchScene(GUI.WAITING);

            System.out.println("SetupConnection done");
        }
            else {
                showErrorMessage();
                Platform.runLater(()->{
                    try {
                        disableCharacters(proxy.getChosenCharacters());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                });

            }
        }else
            showErrorMessage();

    }

    public void showErrorMessage(){
        errorMessage.setVisible(true);
    }

    public void disableCharacters(ArrayList<String> chosenCharacters) {
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
