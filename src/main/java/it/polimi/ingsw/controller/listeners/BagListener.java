package it.polimi.ingsw.controller.listeners;

import it.polimi.ingsw.model.Bag;

import java.util.List;

public interface BagListener {
    enum Colour {GREEN, RED, YELLOW, PINK, BLUE }

    public void notifyBagExtraction(int color);
    void notifyBag(List<Bag.Colour> bag);


}
//color: 0:GREEN, 1:RED, 2:YELLOW, 3:PINK, 4:BLUE
