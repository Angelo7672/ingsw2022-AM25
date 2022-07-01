package it.polimi.ingsw.listeners;

/**
 * Used to notify the changes of the students on a card, for those special cards that requires students on
 */
public interface SpecialStudentsListener {
    void specialStudentsNotify(int special, int color, int value);
}
