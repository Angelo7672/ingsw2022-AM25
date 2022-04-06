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

        /*this.bag = new Colour[] { Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,
                                  Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,
                                  Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,
                                  Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,
                                  Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE
                                };
        randomize();*/
    }

    /*private void randomize(){
        Random r = new Random();
        for(int i = 0; i < 130; i++){
            int j = r.nextInt(131);
            Colour tmp = bag[i];
            bag[i] = bag[j];
            bag[j] = tmp;
        }*/


    public int extraction(){
        Colour tmp;

        if(bag.get(0).equals(Colour.GREEN)){ bag.remove(0); return 0; }
        else if(bag.get(0).equals(Colour.RED)){ bag.remove(0); return 1; }
        else if(bag.get(0).equals(Colour.YELLOW)){ bag.remove(0); return 2; }
        else if(bag.get(0).equals(Colour.PINK)){ bag.remove(0); return 3; }
        else if(bag.get(0).equals(Colour.BLUE)){ bag.remove(0); return 4; }
        /*while(bag[i].equals(Colour.NONE) && i <= bag.length) i++;
        tmp = bag[i];
        bag[i] = Colour.NONE;

        if(tmp.equals(Colour.GREEN)) return 0;
        else if(tmp.equals(Colour.RED)) return 1;
        else if(tmp.equals(Colour.YELLOW)) return 2;
        else if(tmp.equals(Colour.PINK)) return 3;
        else if(tmp.equals(Colour.BLUE)) return 4;*/

        return -1;  //se ritorna -1 si e' cannato qualcosa!
    }

    public boolean checkVictory(){      //must be called every time an extraction is made
        return bag.isEmpty();
        /*if(bag[bag.length].equals(Colour.NONE)) return true;*/
    }
}
