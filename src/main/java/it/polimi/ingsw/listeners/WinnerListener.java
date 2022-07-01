package it.polimi.ingsw.listeners;

import java.io.IOException;

/**
 * Used to notify when the game is over because someone won
 */
public interface WinnerListener {
    void notifyWinner() throws IOException;
}
