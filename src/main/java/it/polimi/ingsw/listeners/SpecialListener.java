package it.polimi.ingsw.listeners;

import java.util.ArrayList;

/**
 * Used to notify special cards
 */
public interface SpecialListener {
    /**
     * Notifies the last played special
     * @param specialRef of type int - index of the special from 1 to 12
     * @param playerRef of type int - index of the player
     */
    void notifySpecial(int specialRef, int playerRef);

    /**
     * Notifies the 3 special for the current game, as an arrayList, with the corresponding costs
     * @param specialList of type ArrayList<Integer> - indexes of the specials
     * @param cost of type ArrayList<Integer> - cost for each special
     */
    void notifySpecialList(ArrayList<Integer> specialList, ArrayList<Integer> cost);

    /**
     * Notifies when a special's cost is increased as a consequence of its usage
     * @param specialRef of type int - index of the special
     * @param newCost of type int - new coins value
     */
    void notifyIncreasedCost(int specialRef, int newCost);
}
