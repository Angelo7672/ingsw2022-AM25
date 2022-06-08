/**
 * This is the bag class, its job is to hold the 130 student tokens and provide random draws. It also announces when the student tokens are gone
 * @param bag is the list containing the 130 students in no particular order
 * @enum Colour has the following legend: GREEN-->0, RED-->1, YELLOW-->2, PINK-->3, BLUE-->4
 * @method extraction remove the first element of the list and check its color and the integer correspondent
 * @method checkVictory check if the last element has been extracted
 */
package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.listeners.BagListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bag {
    private List<Integer> bag;
    protected BagListener bagListener;

    public Bag() { this.bag = new ArrayList<>(); }

    public void bagInitialize(){
        for (int i = 0; i < 24; i++) bag.add(0);
        for (int i = 0; i < 24; i++) bag.add(1);
        for (int i = 0; i < 24; i++) bag.add(2);
        for (int i = 0; i < 24; i++) bag.add(3);
        for (int i = 0; i < 24; i++) bag.add(4);

        Collections.shuffle(bag);
        this.bagListener.notifyBag(bag);
    }
    public void bagRestore(List<Integer> bag){ this.bag = bag; }

    public int extraction(){
        int colorExtracted;

        colorExtracted = bag.get(0);
        this.bagListener.notifyBagExtraction();
        bag.remove(0);

        return colorExtracted;
    }

    public boolean checkVictory(){      //must be called every time an extraction is made
        return bag.isEmpty();
    }

    public void fillBag(int color, int num){
        for(int i = 0; i < num; i++)
            bag.add(color);

        Collections.shuffle(bag);
        this.bagListener.notifyBag(bag);
    }
}