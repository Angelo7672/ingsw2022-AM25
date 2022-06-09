package it.polimi.ingsw.client.GUI;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class MainSceneController implements SceneController{
    private GUI gui;
    private Stage stage;
    private Scene scene;
    private Parent root;
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

    public MainSceneController(){

    }

    public void setTowersOnIsland(int islandRef, int towersNumber){
        //if(islandRef==1){


    }

    public void setTowersInSchool(int schoolRef, int towersNumber){

    }

    @Override
    public void setGUI(GUI gui) {
        this.gui=gui;
    }
}
