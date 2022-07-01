package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;

/**
 * WaitingSceneController is the controller of the WaitingScene.fxml
 * It is loaded when waiting for other players to connect
 */

public class WaitingSceneController implements SceneController{
    private GUI gui;
    private Exit proxy;

    @Override
    public void setGUI(GUI gui) {
        this.gui=gui;
    }

    @Override
    public void setProxy(Exit proxy) {
        this.proxy=proxy;
    }

}
