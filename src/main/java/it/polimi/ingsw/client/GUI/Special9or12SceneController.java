package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


/**
 * Special9or12Controller is the controller of the scene used by special 9 and special 12.
 */
public class Special9or12SceneController implements SceneController{

    @FXML private Button blueButton;
    @FXML private Button greenButton;
    @FXML private Button pinkButton;
    @FXML private Button redButton;
    @FXML private Button yellowButton;
    @FXML private Label errorMessage;

    private Exit proxy;
    private GUI gui;
    private int special;

    public Special9or12SceneController(){}

    public void setSpecial(int special){
        this.special = special;
    }

    /**
     * Make error message visible.
     */
    private void showErrorMessage(){
        errorMessage.setVisible(true);
    }


    /**
     * Call proxy witch color chosen, if the effect of the special is used then it calls a method of gui to set special as used,
     * else it calls specialNotAllowed.
     * @param event is the click on the blue button.
     */
    @FXML
    void blueButtonPressed(ActionEvent event) {
        if (proxy.useSpecial(special, 4)) {
            gui.setConstants("SpecialUsed");
        } else {
            specialNotAllowed();
        }
    }

    /**
     * Call proxy witch color chosen, if the effect of the special is used then it calls a method of gui to set special as used,
     * else it calls specialNotAllowed.
     * @param event is the click on the green button.
     */
    @FXML
    void greenButtonPressed(ActionEvent event) {
        if (proxy.useSpecial(special, 0)) {
            gui.setConstants("SpecialUsed");

        } else {
            specialNotAllowed();
        }
    }

    /**
     * Call proxy witch color chosen, if the effect of the special is used then it calls a method of gui to set special as used,
     * else it calls specialNotAllowed.
     * @param event is the click on the pink button.
     */
    @FXML
    void pinkButtonPressed(ActionEvent event) {
        if (proxy.useSpecial(special, 3)) {
            gui.setConstants("SpecialUsed");

        } else {
            specialNotAllowed();
        }
    }

    /**
     * Call proxy witch color chosen, if the effect of the special is used then it calls a method of gui to set special as used,
     * else it calls specialNotAllowed.
     * @param event is the click on the red button.
     */
    @FXML
    void redButtonPressed(ActionEvent event) {
        if (proxy.useSpecial(special, 1)) {
            gui.setConstants("SpecialUsed");

        } else {
            specialNotAllowed();
        }
    }

    /**
     * Call proxy witch color chosen, if the effect of the special is used then it calls a method of gui to set special as used,
     * else it calls specialNotAllowed.
     * @param event is the click on the yellow button.
     */
    @FXML
    void yellowButtonPressed(ActionEvent event) {
        if (proxy.useSpecial(special, 2)) {
            System.out.println("true");
            gui.setConstants("SpecialUsed");

        } else {
            specialNotAllowed();
        }
    }


    /**
     * Show the error label, close the scene and call a gui's method.
     */
    private void specialNotAllowed(){
        showErrorMessage();
        Stage stage = (Stage) greenButton.getScene().getWindow();
        stage.close();
        gui.specialNotAllowed();
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void setProxy(Exit proxy) {
        this.proxy = proxy;
    }

}
