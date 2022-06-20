package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javax.swing.text.html.ImageView;
import java.io.IOException;

public class CloudsSceneController implements SceneController  {
    private int chosenCloud;
    private GUI gui;
    private Exit proxy;

    @FXML
    private AnchorPane cloud1;

    @FXML
    private Button cloud1Button;

    @FXML
    private AnchorPane cloud2;

    @FXML
    private Button cloud2Button;

    @FXML
    private AnchorPane cloud3;

    @FXML
    private Button cloud3Button;

    @FXML
    private AnchorPane cloud4;

    @FXML
    private Button cloud4Button;



    @FXML
    public void chooseCloud(ActionEvent evt){
        if(evt.getSource()==cloud1Button){
            chosenCloud=1;
        } else if(evt.getSource()==cloud2Button){
            chosenCloud=2;
        } else if(evt.getSource()==cloud3Button){
            chosenCloud=3;
        } else if(evt.getSource()==cloud4Button){
            chosenCloud=4;
        }
    }
    @FXML
    public void confirmPressed(){
        try {
            proxy.chooseCloud(chosenCloud);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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

    public void setStudentsCloud(int cloudRef, int color){
        Label studentLabel;
        ImageView studentImage;
        /*
        if(cloudRef==0){
            studentLabel= (Label) cloud1.getChildren().get(color+6);
            studentImage= (ImageView) cloud1.getChildren().get(color+1);
            studentLabel.setText(String.valueOf());
            */
        }
}

