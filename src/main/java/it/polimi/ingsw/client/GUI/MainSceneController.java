package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.listeners.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


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
    private HashMap<Integer, String> nicknamesMap;
    private HashMap<Integer, String> charactersMap;
    private HashMap<Integer, String> teamsMap;
    private HashMap<Integer, ImageView> charactersImageMap;
    private int gamePhase;

    private final String WIZARD = "/graphics/character_wizard.png";
    private final String WITCH = "/graphics/character_witch.png";
    private final String SAMURAI = "/graphics/character_samurai.png";
    private final String KING = "/graphics/character_king.png";

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

    @FXML private Button useSpecialButton;
    @FXML private ImageView motherNature;
    @FXML private AnchorPane islandsPane;
    @FXML private AnchorPane school1;
    @FXML private AnchorPane school2;
    @FXML private AnchorPane school3;
    @FXML private AnchorPane school4;

    @FXML private AnchorPane userInfo1;
    @FXML private AnchorPane userInfo2;
    @FXML private AnchorPane userInfo3;
    @FXML private AnchorPane userInfo4;

    @FXML private ImageView character1;
    @FXML private ImageView character2;
    @FXML private ImageView character3;
    @FXML private ImageView character4;

    @FXML private Label nickname1;
    @FXML private Label nickname2;
    @FXML private Label nickname3;
    @FXML private Label nickname4;


    @FXML private HBox player1Box;
    @FXML private HBox player2Box;
    @FXML private VBox player3Box;
    @FXML private VBox player4Box;

    public MainSceneController(){
        this.nicknamesMap =new HashMap<>();
        this.charactersMap =new HashMap<>();
        this.charactersImageMap =new HashMap<>();
        this.numberOfPlayers=4;
        this.expertMode=false;
    }

    /*
    public void initialize(){
        System.out.println("initializeMainScene");
        Platform.runLater(()->{
            setNumberOfPlayers(view.getNumberOfPlayers());
            setExpertMode(view.getExpertMode());
            for (int i = 0; i < view.getNumberOfPlayers(); i++)
                setUserInfo(view.getNickname(i), view.getCharacter(i));
            startMainScene();
        });
    }*/
    public void startMainScene() {
        Platform.runLater(()->{
            if (numberOfPlayers == 2) {
                player3Box.setVisible(false);
                player4Box.setVisible(false);
            } else if (numberOfPlayers == 3)
                player4Box.setVisible(false);

            useSpecialButton.setVisible(expertMode);
            charactersImageMap.put(0, character1);
            charactersImageMap.put(1, character2);
            charactersImageMap.put(2, character3);
            charactersImageMap.put(3, character4);

            for(int i=0; i<numberOfPlayers; i++){
                setNickname(nicknamesMap.get(i), i);
                charactersImageMap.get(i).setImage(characterToImage(charactersMap.get(i)));
            }
        });/*
        Platform.runLater(()->{

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
        });*/

    }
    public Image characterToImage(String characterName) {
        Image image = null;
        if (characterName.equalsIgnoreCase("WIZARD")) {
            image = new Image(WIZARD);
        } else if(characterName.equalsIgnoreCase( "WITCH")) {
            image = new Image(WITCH);
        } else if(characterName.equalsIgnoreCase("SAMURAI")) {
            image = new Image(SAMURAI);
        } else if (characterName.equalsIgnoreCase("KING")){
            image = new Image(KING);
        }
        return image;
    }
    public void setNickname(String nickname, int playerRef){
        if(playerRef==0){
            nickname1.setText(nickname);
        } else if(playerRef==1){
            nickname2.setText(nickname);
        } else if(playerRef==2){
            nickname3.setText(nickname);
        } else if(playerRef==3){
            nickname4.setText(nickname);
        }
    }

    public void showCards(){
        //fa comparire la finestra con le carte
        Platform.runLater(()-> {

            System.out.println("startMainScene");
            try {
                if (proxy.startPlanningPhase()) {
                    gamePhase = 1;
                    Stage stage = new Stage();
                    gui.loadScene(stage, GUI.CARDS);
                } else
                    System.out.println("errore");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });


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

    public void setUserInfo(String nickname, String character, int playerRef ) {
        nicknamesMap.put(playerRef,nickname);
        charactersMap.put(playerRef, character);
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
