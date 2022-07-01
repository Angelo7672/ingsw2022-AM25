package it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.client.Exit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * CloudsSceneController is the controller for CloudsScene.fxml
 */
public class CloudsSceneController implements SceneController  {
    private int chosenCloud;
    private GUI gui;
    private Exit proxy;
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

    /**
     * Set the chosen cloud, based on the source of the click
     * @param evt of type ActionEvent - is the event performed
     */
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

    /**
     * Called when confirm button is pressed, calls the proxy method to choose the cloud
     */
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

    /**
     * When the notify arrives, sets the students on the cloud, changing the value of the label and ImageView
     * Students ImageView are from index 0 to 4 of the children list of Cloud panes, label from 5 to 9
     * @param cloudRef of type int - index of the cloud
     * @param color of type int - color
     * @param newStudentValue of type int - new value of the students of this color
     */
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

    /**
     * Based on the number of players, hides the unnecessary clouds
     * @param numberOfPlayers of type int
     */
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
    /**
     * Enables the confirm button
     */
    public void enableConfirm(){
        this.confirmButton.setDisable(false);
        this.cloudsLabel.setVisible(true);
    }

    /**
     * Disables the confirm button
     */
    public void disableConfirm(){
        this.confirmButton.setDisable(true);
        this.cloudsLabel.setVisible(false);

    }

}



