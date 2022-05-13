package it.polimi.ingsw.controller.listeners;

/**
 *
 */
public interface StudentsListener {
    public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue);
}
//place is where the change is done: 0 entrance, 1 table, 2 island, 3 cloud
//componentRef is the index of the component (playerRef, islandRef, cloudRef)