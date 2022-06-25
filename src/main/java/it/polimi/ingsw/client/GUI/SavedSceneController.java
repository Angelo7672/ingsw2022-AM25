package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import java.io.IOException;

public class SavedSceneController implements SceneController{

    private GUI gui;
    private Exit proxy;
    @FXML private Label numberOfPlayers;
    @FXML private Label expertMode;
    @FXML private Button YES;
    @FXML private Button NO;


    public void initializedScene(int numberOfPlayers, String expertMode){
        System.out.println(Thread.currentThread());
        Platform.runLater(()-> {
            System.out.println("initialize scene method");
            this.numberOfPlayers.setText(Integer.toString(numberOfPlayers));
            this.expertMode.setText(expertMode);
        });
    }

    public void YESPressed(ActionEvent e) throws IOException {
        proxy.savedGame("y");;
        gui.switchScene(GUI.LOGINRESTORE);
    }

    public void NOPressed(ActionEvent e) throws IOException {
        proxy.savedGame("n");
        gui.switchScene(GUI.SETUP);
    }





    @Override
    public void setGUI(GUI gui) {this.gui = gui;}
    @Override
    public void setProxy(Exit proxy) {this.proxy = proxy;}
}
