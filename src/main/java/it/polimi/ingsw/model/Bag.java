/**
 * This is the bag class, its job is to hold the 130 student tokens and provide random draws. It also announces when the student tokens are gone
 * Colour has the following legend: GREEN-->0, RED-->1, YELLOW-->2, PINK-->3, BLUE-->4
 */
package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.BagListener;

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

    public boolean checkVictory(){ return bag.isEmpty(); }     //must be called every time an extraction is made

    public void fillBag(int color, int num){
        for(int i = 0; i < num; i++)
            bag.add(color);

        Collections.shuffle(bag);
        this.bagListener.notifyBag(bag);
    }
}