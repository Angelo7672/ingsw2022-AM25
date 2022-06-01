package it.polimi.ingsw.controller.listeners;

public interface BagListener {
    enum Colour {GREEN, RED, YELLOW, PINK, BLUE }

    public void notifyBagExtraction(int color);
    //void notifyBag(ArrayList<int> bag);


}
//color: 0:GREEN, 1:RED, 2:YELLOW, 3:PINK, 4:BLUE
