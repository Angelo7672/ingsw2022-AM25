/**
 * This is the bag class, its job is to hold the 130 student tokens and provide random draws. It also announces when the student tokens are gone
 * @param bag is the list containing the 130 students in no particular order
 * @enum Colour has the following legend: GREEN-->0, RED-->1, YELLOW-->2, PINK-->3, BLUE-->4
 * @method extraction remove the first element of the list and check its color and the integer correspondent
 * @method checkVictory check if the last element has been extracted
 */
package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Bag {
    private List<Colour> bag;

    private enum Colour {GREEN, RED, YELLOW, PINK, BLUE }

    public Bag() {
        this.bag = new ArrayList<>();
        for (int i = 0; i < 26; i++) bag.add(Colour.GREEN);
        for (int i = 0; i < 26; i++) bag.add(Colour.RED);
        for (int i = 0; i < 26; i++) bag.add(Colour.YELLOW);
        for (int i = 0; i < 26; i++) bag.add(Colour.PINK);
        for (int i = 0; i < 26; i++) bag.add(Colour.BLUE);

        Collections.shuffle(bag);
    }

    public int extraction(){
        Colour tmp;

        if(bag.get(0).equals(Colour.GREEN)){ bag.remove(0); return 0; }
        else if(bag.get(0).equals(Colour.RED)){ bag.remove(0); return 1; }
        else if(bag.get(0).equals(Colour.YELLOW)){ bag.remove(0); return 2; }
        else if(bag.get(0).equals(Colour.PINK)){ bag.remove(0); return 3; }
        else if(bag.get(0).equals(Colour.BLUE)){ bag.remove(0); return 4; }

        return -1;  //se ritorna -1 si e' cannato qualcosa!
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
            else if(color==3) bag.add(Colour.BLUE);
        }
        Collections.shuffle(bag);
    }
}