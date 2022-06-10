package it.polimi.ingsw.client.GUI;

import java.io.IOException;

public interface SceneController {
    public void setGUI(GUI gui);
    public void switchScene() throws IOException;
}
