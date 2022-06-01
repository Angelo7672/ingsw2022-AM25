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
    private List<Colour> bag;
    protected BagListener bagListener;

    public enum Colour {GREEN, RED, YELLOW, PINK, BLUE }

    public Bag() { this.bag = new ArrayList<>(); }

    public void bagInitialize(){
        for (int i = 0; i < 24; i++) bag.add(Colour.GREEN);
        for (int i = 0; i < 24; i++) bag.add(Colour.RED);
        for (int i = 0; i < 24; i++) bag.add(Colour.YELLOW);
        for (int i = 0; i < 24; i++) bag.add(Colour.PINK);
        for (int i = 0; i < 24; i++) bag.add(Colour.BLUE);

        Collections.shuffle(bag);
        //this.bagListener.notifyBag(bag);
    }

    public int extraction(){
        if(bag.get(0).equals(Colour.GREEN)){
            this.bagListener.notifyBagExtraction(0);
            bag.remove(0);
            return 0; }
        else if(bag.get(0).equals(Colour.RED)){
            this.bagListener.notifyBagExtraction(1);
            bag.remove(0);
            return 1; }
        else if(bag.get(0).equals(Colour.YELLOW)){
            this.bagListener.notifyBagExtraction(2);
            bag.remove(0);
            return 2; }
        else if(bag.get(0).equals(Colour.PINK)){
            this.bagListener.notifyBagExtraction(3);
            bag.remove(0);
            return 3; }
        else if(bag.get(0).equals(Colour.BLUE)){
            this.bagListener.notifyBagExtraction(4);
            bag.remove(0);
            return 4; }

        return -1;  //if it returns -1 something is wrong
    }

    public boolean checkVictory(){      //must be called every time an extraction is made
        return bag.isEmpty();
    }

    public void fillBag(int color, int num){
        for(int i=0; i<num;i++){
            if(color==0) bag.add(Colour.GREEN);
            else if(color==1) bag.add(Colour.RED);
            else if(color==2) bag.add(Colour.YELLOW);
            else if(color==3) bag.add(Colour.PINK);
            else if(color==4) bag.add(Colour.BLUE);
        }
        Collections.shuffle(bag);
        //this.bagListener.notifyBag(bag);
    }
}