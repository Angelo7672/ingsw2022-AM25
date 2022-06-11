package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.controller.listeners.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;


public class MainSceneController implements SceneController, TowersListener, ProfessorsListener, SpecialListener, PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener, Initializable {

    private GUI gui;
    private View view;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Exit proxy;
    private int numberOfPlayers;
    private boolean expertMode;
    private HashMap<String, String> userInfo;

    @FXML private ImageView island1;
    @FXML private ImageView island2;
    @FXML private ImageView island3;
    @FXML private ImageView island4;
    @FXML private ImageView island5;
    @FXML private ImageView island6;
    @FXML private ImageView island7;
    @FXML private ImageView island8;
    @FXML private ImageView island9;
    @FXML private ImageView island10;
    @FXML private ImageView island11;
    @FXML private ImageView island12;

    @FXML private ImageView schoolBoard1;
    @FXML private ImageView schoolBoard2;
    @FXML private ImageView schoolBoard3;
    @FXML private ImageView schoolBoard4;

    @FXML private Button useSpecialButton;

    @FXML private ImageView motherNature;

    public MainSceneController(){
        this.userInfo=new HashMap<>();
        this.numberOfPlayers=4;
        this.expertMode=false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        if (numberOfPlayers == 2) {
            schoolBoard3.setVisible(false);
            schoolBoard4.setVisible(false);
        } else if (numberOfPlayers == 3)
            schoolBoard4.setVisible(false);

        if (expertMode == false) {
            useSpecialButton.setVisible(false);
        }
    }


    public void setView(View view){
        this.view=view;
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui=gui;
    }

    @Override
    public void setProxy(Exit proxy) {
        this.proxy=proxy;
    }


    public void setNumberOfPlayers(int numberOfPlayers){
        this.numberOfPlayers=numberOfPlayers;
    }

    public void setExpertMode(boolean expertMode) {
        this.expertMode = expertMode;
    }

    public void setUserInfo(String nickname, String character) {
        userInfo.put(nickname, character);
    }

    @Override
    public void notifyNewCoinsValue(int playerRef, int newCoinsValue) {

    }

    @Override
    public void notifyInhibited(int islandRef, int isInhibited) {

    }

    @Override
    public void notifyIslandChange(int islandToDelete) {

    }

    @Override
    public void notifyMotherPosition(int newMotherPosition) {

    }

    @Override
    public void notifyPlayedCard(int playerRef, String assistantCard) {

    }

    @Override
    public void notifyHand(int playerRef, ArrayList<String> hand) {

    }

    @Override
    public void notifyProfessors(int playerRef, int color, boolean newProfessorValue) {

    }

    @Override
    public void notifySpecial(int specialRef) {

    }

    @Override
    public void notifySpecialName(String specialName) {

    }

    @Override
    public void notifyPlayedSpecial(int specialRef) {

    }

    @Override
    public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {

    }

    @Override
    public void notifyTowersChange(int place, int componentRef, int towersNumber) {

    }

    @Override
    public void notifyTowerColor(int islandRef, int newColor) {

    }





}
