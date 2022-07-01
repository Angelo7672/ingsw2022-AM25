package it.polimi.ingsw.listeners;

/**
 * Notifies if the player has in his school the professor of a given color. If so, newProfessorValue is true
 */
public interface ProfessorsListener {
    public void notifyProfessors(int playerRef, int color, boolean newProfessorValue);
}
