package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

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

    private void showErrorMessage(){
        errorMessage.setVisible(true);
    }

    @FXML
    void blueButtonPressed(ActionEvent event) {
        try {
            if (proxy.useSpecial(special, 4)) {

            } else showErrorMessage();
        }catch (IOException e){}
    }

    @FXML
    void greenButtonPressed(ActionEvent event) {
        try {
            if (proxy.useSpecial(special, 0)) {

            } else showErrorMessage();
        }catch (IOException e){}
    }

    @FXML
    void pinkButtonPressed(ActionEvent event) {
        try {
            if (proxy.useSpecial(special, 3)) {

            } else showErrorMessage();
        }catch (IOException e){}
    }

    @FXML
    void redButtonPressed(ActionEvent event) {
        try {
            if (proxy.useSpecial(special, 1)) {

            } else showErrorMessage();
        }catch (IOException e){}
    }

    @FXML
    void yellowButtonPressed(ActionEvent event) {
        try {
            if (proxy.useSpecial(special, 2)) {

            } else showErrorMessage();
        }catch (IOException e){}
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
