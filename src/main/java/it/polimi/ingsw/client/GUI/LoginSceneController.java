package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.util.ArrayList;

/**
 * LoginSceneController is the controller for LoginScene.fxml
 * The user insert his nickname and choose a character
 */
public class LoginSceneController implements SceneController{
    private GUI gui;
    private String currentNickname;
    private String currentCharacter;
    private Exit proxy;
    private final Service<Boolean> loginService;

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

    /**
     * Constructor method, creates an instance of LoginSceneController and initializes the attributes
     */
    public LoginSceneController(){
        this.currentNickname="";
        this.currentCharacter="";
        this.loginService= new LoginService();
        loginService.setOnSucceeded(workerStateEvent -> {
            Boolean result = loginService.getValue();
            if(result){
                gui.setYourNickname(currentNickname);
                gui.phaseHandler("SetView");
            }
            else{
                showErrorMessage();
                disableCharacters(proxy.getChosenCharacters());
                gui.switchScene(GUI.LOGIN);
            }
        });
    }

    /**
     * LoginService is started when the user chose nickname and character, after pressing confirm
     * Calls proxy method
     */
    private class LoginService extends Service<Boolean> {
        @Override
        protected Task<Boolean> createTask() {
            return new Task<>() {
                @Override
                protected Boolean call() {
                    Boolean result = proxy.setupConnection(currentNickname, currentCharacter);
                    return result;
                }
            };
        }
    }

    /**
     * Based on the button pressed, sets the current character
     * @param e of type ActionEvent - the click of the button
     */
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
    /**
     * Called when next button is pressed. Starts LoginService, and while waiting switches to WaitingScene
     */
    public void nextPressed(ActionEvent e) {
        currentNickname= this.nicknameBox.getText();
        if(!currentNickname.equals("") && !currentCharacter.equals("")) {
            if(loginService.getState()== Worker.State.READY)
                loginService.start();
            else
                loginService.restart();
            gui.switchScene(GUI.WAITING);
        }
        else showErrorMessage();
    }

    public void showErrorMessage(){
        errorMessage.setVisible(true);
    }

    /**
     * Disables the buttons of the characters that have been chosen already
     * @param chosenCharacters of type ArrayList<String> - characters that are already been chosen
     */
    public void disableCharacters(ArrayList<String> chosenCharacters) {
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
