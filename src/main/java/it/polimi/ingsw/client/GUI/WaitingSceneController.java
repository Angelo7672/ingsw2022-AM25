package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;

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
