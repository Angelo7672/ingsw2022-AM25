package it.polimi.ingsw.listeners;

/**
 * Notifies how many noEntry Tiles are on a given island, from 0 to 4
 */
public interface InhibitedListener {
    void notifyInhibited(int islandRef, int isInhibited);
}
