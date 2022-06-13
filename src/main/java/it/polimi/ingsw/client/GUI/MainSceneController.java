package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.controller.listeners.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;


public class MainSceneController implements SceneController, TowersListener, ProfessorsListener,PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener {

    private GUI gui;
    private View view;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Exit proxy;
    private int numberOfPlayers;
    private boolean expertMode;
    private HashMap<String, String> userInfo;
    private int gamePhase;

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
    @FXML private ImageView character1;
    @FXML private AnchorPane islandsPane;
    @FXML private AnchorPane school1;
    @FXML private AnchorPane school2;
    @FXML private AnchorPane school3;
    @FXML private AnchorPane school4;
    @FXML private AnchorPane userInfo1;
    @FXML private AnchorPane userInfo2;
    @FXML private HBox player1Box;
    @FXML private HBox player2Box;
    @FXML private VBox player3Box;
    @FXML private VBox player4Box;

    public MainSceneController(){
        this.userInfo=new HashMap<>();
        this.numberOfPlayers=4;
        this.expertMode=false;
    }


    public void startMainScene() {
        Platform.runLater(()->{
            if (numberOfPlayers == 2) {
                player3Box.setVisible(false);
                player4Box.setVisible(false);
            } else if (numberOfPlayers == 3)
                player4Box.setVisible(false);

            if (expertMode == false) {
                useSpecialButton.setVisible(false);
            }
            for(int i=0; i<numberOfPlayers; i++){
                //userInfo.
            }
            System.out.println("startMainScene");
            try {
                if(proxy.startPlanningPhase()) {
                    gamePhase = 1;
                    //showCards();
                }
                else
                    System.out.println("errore");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void showCards(){
        //fa comparire la finestra con le carte
        Stage stage = new Stage();
        gui.loadScene(stage, GUI.CARDS);

    }


    public void setView(View view){
        this.view=view;
        System.out.println("view set");
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
    public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {
        if(place==0)
            System.out.println("student in entrance");
        else if(place==1){
            System.out.println("students at table");
        } else if(place==2)
            System.out.println("student in island");
        else if(place==3){
            System.out.println("students on cloud");
        }
    }

    @Override
    public void notifyTowersChange(int place, int componentRef, int towersNumber) {

    }

    @Override
    public void notifyTowerColor(int islandRef, int newColor) {

    }





}
