package it.polimi.ingsw.listeners;

import java.util.ArrayList;

/**
 * Used to restore the cards of a player when loading a file save of the game
 */
public interface RestoreCardsListener {
    void restoreCardsNotify(ArrayList<String> hand);
}
