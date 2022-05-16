package it.polimi.ingsw.server;

import java.io.IOException;
import java.util.ArrayList;

public interface Exit {
    void start();
    void goPlayCard(int ref, ArrayList<String> playedCardsInThisTurn);
    void startActionPhase(int ref);
}
