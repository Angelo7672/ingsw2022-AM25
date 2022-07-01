package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CloudsSceneController implements SceneController  {
    private int chosenCloud;
    private GUI gui;
    private Exit proxy;

    private View view;

    @FXML private AnchorPane cloud1;
    @FXML private Button cloud1Button;
    @FXML private AnchorPane cloud2;
    @FXML private Button cloud2Button;
    @FXML private AnchorPane cloud3;
    @FXML private Button cloud3Button;
    @FXML private AnchorPane cloud4;
    @FXML private Button cloud4Button;
    @FXML private Button confirmButton;
    @FXML private Label cloudsLabel;
    @FXML private Label errorMessage;

    @FXML
    public void chooseCloud(ActionEvent evt){
        if(evt.getSource()==cloud1Button){
            chosenCloud=0;
        } else if(evt.getSource()==cloud2Button){
            chosenCloud=1;
        } else if(evt.getSource()==cloud3Button){
            chosenCloud=2;
        } else if(evt.getSource()==cloud4Button){
            chosenCloud=3;
        }
    }
    @FXML
    public void confirmPressed(){
        String result = proxy.chooseCloud(chosenCloud);

        if(result.equalsIgnoreCase("ok")){
            errorMessage.setVisible(false);
            gui.setConstants("Reset");
            gui.setNotYourTurn();
            disableConfirm();
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
            gui.phaseHandler("PlanningPhase");

        } else {
            errorMessage.setVisible(true);
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

    public void setStudentsCloud(int cloudRef, int color, int newStudentValue){
        Label studentLabel = null;
        ImageView studentImage = null;

        if(cloudRef==0) {
            studentLabel = (Label) cloud1.getChildren().get(color + 6);
            studentImage = (ImageView) cloud1.getChildren().get(color + 1);
        } else if (cloudRef==1){
            studentLabel = (Label) cloud2.getChildren().get(color + 6);
            studentImage = (ImageView) cloud2.getChildren().get(color + 1);
        } else if (cloudRef==2){
            studentLabel = (Label) cloud3.getChildren().get(color + 6);
            studentImage = (ImageView) cloud3.getChildren().get(color + 1);
        } else if (cloudRef==3){
            studentLabel = (Label) cloud4.getChildren().get(color + 6);
            studentImage = (ImageView) cloud4.getChildren().get(color + 1);
        }
        studentLabel.setText(String.valueOf(newStudentValue));
        if(newStudentValue!=0){
            studentImage.setVisible(true);
            studentLabel.setVisible(true);
        } else {
            studentImage.setVisible(false);
            studentLabel.setVisible(false);
        }
    }

    public void initializeCloudScene(int numberOfPlayers) {
        if(numberOfPlayers==2){
            cloud3.setVisible(false);
            cloud3Button.setDisable(true);
            cloud4.setVisible(false);
            cloud4Button.setDisable(true);
        }
        else if(numberOfPlayers==3){
            cloud4.setVisible(false);
            cloud4Button.setDisable(true);
        }
    }

    public void enableConfirm(){
        this.confirmButton.setDisable(false);
        this.cloudsLabel.setVisible(true);
    }

    public void disableConfirm(){
        this.confirmButton.setDisable(true);
        this.cloudsLabel.setVisible(false);

    }

}



