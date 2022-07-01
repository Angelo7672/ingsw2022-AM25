package it.polimi.ingsw.listeners;

/**
 * Used to notify a change in the coins value for a given player
 */
public interface CoinsListener {
    void notifyNewCoinsValue(int playerRef, int newCoinsValue);
}
