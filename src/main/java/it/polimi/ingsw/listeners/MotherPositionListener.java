package it.polimi.ingsw.listeners;

/**
 * Notifies the index of the island where Mother Nature is currently located, from 0 to 11
 */
public interface MotherPositionListener {
    void notifyMotherPosition(int newMotherPosition);
}
