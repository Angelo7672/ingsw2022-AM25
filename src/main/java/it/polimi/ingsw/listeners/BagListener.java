package it.polimi.ingsw.listeners;

import java.util.List;

/**
 * Used to notify changes to the bag
 */
public interface BagListener {
    /**
     * Removes the last element from the bag list
     */
    void notifyBagExtraction();

    /**
     * Notifies the whole bag
     * Colors are listed as: 0:GREEN, 1:RED, 2:YELLOW, 3:PINK, 4:BLUE
     * @param bag is type List<Integer> - is the list of students in the bag
     */
    void notifyBag(List<Integer> bag);
}
