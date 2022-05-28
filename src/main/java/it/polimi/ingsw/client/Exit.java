package it.polimi.ingsw.client;

import java.io.IOException;
import java.util.ArrayList;

public interface Exit {
    void setupGame();
    void setupConnection(ArrayList<String> chosenCharacters) throws IOException, ClassNotFoundException;
    void view(View view);
    void cli();
}
