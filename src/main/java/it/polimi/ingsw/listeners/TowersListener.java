package it.polimi.ingsw.listeners;
/**
 * Used to notify when there's a towers change somewhere in the game
 */
public interface TowersListener {
    /**
     * @param place of type int - where the change is done: 0 school, 1 island
     * @param componentRef of type int - is the index of the component (playerRef, islandRef)
     * @param towersNumber of type int - new towers value
     */
    void notifyTowersChange(int place, int componentRef, int towersNumber);

    /**
     * @param islandRef of type int - index of the island, color can only change on islands
     * @param newColor of type int - new team on that island
     */
    void notifyTowerColor(int islandRef, int newColor);
}

