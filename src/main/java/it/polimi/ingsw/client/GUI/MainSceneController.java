package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.listeners.*;
import javafx.application.Platform;
import javafx.beans.Observable;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class MainSceneController implements SceneController {

    private GUI gui;
    private View view;
    private Exit proxy;
    private int numberOfPlayers;
    private boolean expertMode;
    private HashMap<Integer, String> nicknamesMap;
    private HashMap<Integer, String> charactersMap;
    private HashMap<Integer, ImageView> charactersImageMap;
    private HashMap<Integer, AnchorPane> islandsMap;

    private int gamePhase;

    private final String WIZARD = "/graphics/character_wizard.png";
    private final String WITCH = "/graphics/character_witch.png";
    private final String SAMURAI = "/graphics/character_samurai.png";
    private final String KING = "/graphics/character_king.png";


    @FXML private Button useSpecialButton;
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
        this.islandsMap= new HashMap<>();

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
            System.out.println("startMainScene");
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
            AnchorPane island;

            for(int i=1; i<=12; i++){ //starts from 1 because 0 is useSpecialButton
                island = (AnchorPane) islandsPane.getChildren().get(i);
                islandsMap.put(i-1, island);
            }
            gui.isMainScene=true;
        });
        /*
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
    public void setMotherPosition(int islandRef){
        System.out.println("trying to set Mother Position");
        ImageView motherNature;
        System.out.println("about to start for loop");
        for(int i=0; i<islandsMap.size(); i++){
            System.out.println("in for loop");
            motherNature = (ImageView) islandsMap.get(i).getChildren().get(1); //children 1 is always MotherNature
            System.out.println("This node is: "+islandsMap.get(i).getChildren().get(1));
            if(islandRef == i){
                motherNature.setVisible(true);
                System.out.println("MotherNAture should be visible on island "+ i);
            } else
                motherNature.setVisible(false);
        }
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
        Label nicknameLabel;
        if(playerRef==0){
            nicknameLabel= (Label) userInfo1.getChildren().get(1);
            nicknameLabel.setText(nickname);
        } else if(playerRef==1){
            nicknameLabel= (Label) userInfo2.getChildren().get(1);
            nicknameLabel.setText(nickname);
        } else if(playerRef==2){
            nicknameLabel= (Label) userInfo3.getChildren().get(1);
            nicknameLabel.setText(nickname);
        } else if(playerRef==3) {
            nicknameLabel= (Label) userInfo4.getChildren().get(1);
            nicknameLabel.setText(nickname);
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
                    gui.loadScene(GUI.CARDS);
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

    public void setStudentsEntrance(int componentRef, int color, int newStudentsValue){

    }
    public void setStudentsTable(int schoolRef, int color, int newStudentsValue){
        /*
        int i=0;
        switch (schoolRef){

            case 1 -> {
                //school1.getChildren().add(i, );
                school1.getChildren().get(3).setLayoutX(school1.getChildren().get(i).getLayoutX());
                //AnchorPane island = (AnchorPane) islandsPane.lookup("#island4");
                //island.lookup()
                //school1.getChildren().get()
            }
        }*/
    }

    public void setStudentsIsland(int islandRef, int color, int newStudentsValue){
        AnchorPane island = islandsMap.get(islandRef);
        Label studentLabel;
        if(color == 0){
            studentLabel= (Label) island.getChildren().get(8);
            studentLabel.setText(String.valueOf(newStudentsValue));
        } else if(color == 1){
            studentLabel= (Label) island.getChildren().get(9);
            studentLabel.setText(String.valueOf(newStudentsValue));
        } else if(color == 2) {
            studentLabel = (Label) island.getChildren().get(10);
            studentLabel.setText(String.valueOf(newStudentsValue));
        } else if(color == 3) {
            studentLabel = (Label) island.getChildren().get(11);
            studentLabel.setText(String.valueOf(newStudentsValue));
        } else if(color == 4) {
            studentLabel = (Label) island.getChildren().get(12);
            studentLabel.setText(String.valueOf(newStudentsValue));
        }

    }

    public void setStudentsCloud(int componentRef, int color, int newStudentsValue){

    }







}
