package it.polimi.ingsw.listeners;

/**
 * Used to notify a change in the coins value for a given player
 */
public interface CoinsListener {
    public void notifyNewCoinsValue(int playerRef, int newCoinsValue);
}
