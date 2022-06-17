package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.listeners.*;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
import java.lang.reflect.Array;
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
    private HashMap<Integer, AnchorPane> schoolMap;
    private HashMap<Integer, AnchorPane> towersMap;
    private HashMap<Integer, AnchorPane> entrancesMap;
    private HashMap<Integer, AnchorPane> tablesMap;
    private HashMap<Integer, AnchorPane> professorsMap;


    private int gamePhase;

    private final String WIZARD = "/graphics/character_wizard.png";
    private final String WITCH = "/graphics/character_witch.png";
    private final String SAMURAI = "/graphics/character_samurai.png";
    private final String KING = "/graphics/character_king.png";

    private final String GREENPROF = "/graphics/wooden_pieces/greenProf3D.png";
    private final String REDPROF = "/graphics/wooden_pieces/redProf3D.png";
    private final String YELLOWPROF = "/graphics/wooden_pieces/yellowProf3D.png";
    private final String PINKPROF = "/graphics/wooden_pieces/pinkProf3D.png";
    private final String BLUEPROF = "/graphics/wooden_pieces/blueProf3D.png";

    private final String GREENSTUDENT = "/graphics/wooden_pieces/greenStudent3D.png";
    private final String REDSTUDENT = "/graphics/wooden_pieces/redStudent3D.png";
    private final String YELLOWSTUDENT = "/graphics/wooden_pieces/yellowStudent3D.png";
    private final String PINKSTUDENT = "/graphics/wooden_pieces/pinkStudent3D.png";
    private final String BLUESTUDENT = "/graphics/wooden_pieces/blueStudent3D.png";

    private final String BLACKTOWER = "/graphics/wooden_pieces/black_tower.png";
    private final String WHITETOWER = "/graphics/wooden_pieces/white_tower.png";
    private final String GREYTOWER = "/graphics/wooden_pieces/grey_tower.png";



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
        this.towersMap = new HashMap<>();
        this.entrancesMap = new HashMap<>();
        this.tablesMap = new HashMap<>();
        this.professorsMap=new HashMap<>();
        this.schoolMap = new HashMap<>();

    }

    public void startMainScene() {
        Platform.runLater(()->{
            System.out.println("startMainScene");
            if (numberOfPlayers == 2) {
                player3Box.setVisible(false);
                player4Box.setVisible(false);

            } else if (numberOfPlayers == 3) {
                player4Box.setVisible(false);
            }

            useSpecialButton.setVisible(expertMode);

            charactersImageMap.put(0, character1);
            charactersImageMap.put(1, character2);
            charactersImageMap.put(2, character3);
            charactersImageMap.put(3, character4);

            schoolMap.put(0, school1);
            schoolMap.put(1, school2);
            schoolMap.put(2, school3);
            schoolMap.put(3, school4);


            for(int i=0; i<numberOfPlayers; i++){
                setNickname(nicknamesMap.get(i), i);
                charactersImageMap.get(i).setImage(characterToImage(charactersMap.get(i)));

                //maps the children of school panes, 0:school imageView, 1: entrance, 2: table 3: professors, 4: towers
                this.entrancesMap.put(i, (AnchorPane) schoolMap.get(i).getChildren().get(1)); //maps all the entrances
                this.tablesMap.put(i, (AnchorPane) schoolMap.get(i).getChildren().get(2)); //maps all the tables
                this.professorsMap.put(i, (AnchorPane) schoolMap.get(i).getChildren().get(3)); //maps all the professors panes
                this.towersMap.put(i, (AnchorPane) schoolMap.get(i).getChildren().get(4));//maps all the towers panes

            }
            System.out.println(entrancesMap+"\n");
            System.out.println(tablesMap+"\n");
            System.out.println(professorsMap+"\n");
            System.out.println(towersMap+"\n");

            AnchorPane island;
            for(int i=1; i<=12; i++){ //starts from 1 because 0 is useSpecialButton
                island = (AnchorPane) islandsPane.getChildren().get(i);
                islandsMap.put(i-1, island);
            }

            schoolsInitialization();
            islandsInitialization();

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

    public void islandsInitialization(){
        System.out.println("Island initialization");
        AnchorPane island;
        System.out.println(islandsMap+ " is island map ");

        for(int i=0; i<12; i++){
            island = islandsMap.get(i);
            System.out.println("Island:" +island);
            System.out.println("Index: "+i);

            for(int j=1; j<=13;j++) {
                island.getChildren().get(j).setVisible(false);
                System.out.println("Child :"+island.getChildren().get(j));
            }
        }

    }

    public void schoolsInitialization(){
        /*for(int i=0; i<numberOfPlayers; i++){
            for(int j=0; j<entrancesMap.get(i).getChildren().size(); j++){
                entrancesMap.get(i).getChildren().get(j).setVisible(false);
            }
        }*/
        ImageView tower;
        for (int i = 0; i < towersMap.get(0).getChildren().size(); i++) {
            tower = (ImageView) towersMap.get(0).getChildren().get(i);
            tower.setImage(new Image(WHITETOWER));
        }
        if(numberOfPlayers==2){
            for(int i=0; i<towersMap.get(1).getChildren().size(); i++){
                tower = (ImageView) towersMap.get(1).getChildren().get(i);
                tower.setImage(new Image(BLACKTOWER));
            }
        }
        if(numberOfPlayers==3){
            for(int i=0; i<towersMap.get(1).getChildren().size(); i++){
                tower = (ImageView) towersMap.get(1).getChildren().get(i);
                tower.setImage(new Image(BLACKTOWER));
            }
            for(int i=0; i<towersMap.get(2).getChildren().size(); i++){
                tower = (ImageView) towersMap.get(2).getChildren().get(i);
                tower.setImage(new Image(GREYTOWER));
            }
        }
        if(numberOfPlayers==4) {
            for (int i = 0; i < towersMap.get(1).getChildren().size(); i++) {
                tower = (ImageView) towersMap.get(1).getChildren().get(i);
                tower.setImage(new Image(WHITETOWER));
            }
            for (int i = 0; i < towersMap.get(2).getChildren().size(); i++) {
                tower = (ImageView) towersMap.get(2).getChildren().get(i);
                tower.setImage(new Image(BLACKTOWER));
            }
            for (int i = 0; i < towersMap.get(3).getChildren().size(); i++) {
                tower = (ImageView) towersMap.get(3).getChildren().get(i);
                tower.setImage(new Image(BLACKTOWER));
            }
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

    public void setStudentsEntrance(int playerRef, int color, int newStudentsValue) {
        Label studentLabel;
        ImageView studentImage;
        System.out.println("setting students of color: "+color+" in school "+playerRef+" with a value of "+newStudentsValue);

        studentLabel = (Label) entrancesMap.get(playerRef).getChildren().get(color+5); //Labels are located 5 position after images
        studentLabel.setText(String.valueOf(newStudentsValue));
        studentImage = (ImageView) entrancesMap.get(playerRef).getChildren().get(color);
        if (newStudentsValue != 0) {
            studentLabel.setVisible(true);
            studentImage.setVisible(true);
        } else {
                studentLabel.setVisible(false);
                studentImage.setVisible(false);
            }
        }



    public void setStudentsTable(int playerRef, int color, int newStudentsValue){
        /*Label studentLabel;
        ImageView studentImage;

        studentLabel = (Label) entrancesMap.get(playerRef).getChildren().get(color+4); //Labels are located 4 position after images
        studentLabel.setText(String.valueOf(newStudentsValue));
        studentImage = (ImageView) entrancesMap.get(playerRef).getChildren().get(color);
        if (newStudentsValue != 0) {
            studentLabel.setVisible(true);
            studentImage.setVisible(true);
        } else {
            studentLabel.setVisible(false);
            studentImage.setVisible(false);
        }*/
    }


    public void setStudentsIsland(int islandRef, int color, int newStudentsValue){
        AnchorPane island = islandsMap.get(islandRef);
        Label studentLabel;
        ImageView studentImage;
        studentImage= (ImageView) island.getChildren().get(color+3);
        studentLabel= (Label) island.getChildren().get(color+8);
        studentLabel.setText(String.valueOf(newStudentsValue));
        studentImage.setVisible(true);
        studentLabel.setVisible(true);

    }

    public void setStudentsCloud(int componentRef, int color, int newStudentsValue){

    }







}
