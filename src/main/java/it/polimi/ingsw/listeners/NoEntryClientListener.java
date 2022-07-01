package it.polimi.ingsw.listeners;

/**
 * Used on the client to notify the changes caused by special card 5
 */
public interface NoEntryClientListener {
    void notifyNoEntry(int special, int newValue);
}
