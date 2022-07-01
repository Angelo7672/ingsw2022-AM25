package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;

/**
 * SceneController interface is implemented by all the controllers of the scenes of the game
 */
public interface SceneController {
    void setGUI(GUI gui);
    void setProxy(Exit proxy);
}
