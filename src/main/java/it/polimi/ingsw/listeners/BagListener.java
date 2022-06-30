package it.polimi.ingsw.listeners;

import java.util.List;

public interface BagListener {
    enum Colour {GREEN, RED, YELLOW, PINK, BLUE }

    void notifyBagExtraction(); //removes the last element (0) from Bag list
    void notifyBag(List<Integer> bag);


}
//color: 0:GREEN, 1:RED, 2:YELLOW, 3:PINK, 4:BLUE
