package it.polimi.ingsw.listeners;
/**
 * Used to notify when there's a student change somewhere in the game
 */
public interface StudentsListener {
    /**
     * @param place of type int - where the change is done: 0 entrance, 1 table, 2 island, 3 cloud
     * @param componentRef of type int - is the index of the component (playerRef, islandRef, cloudRef)
     * @param color of type int - color index
     * @param newStudentsValue of type int - new value
     */
    public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue);
}