package it.polimi.ingsw.client;

import java.io.IOException;
import java.util.ArrayList;

public interface Exit {
    void phaseHandler(String phase) throws IOException, ClassNotFoundException;
    void setupGame();
}
