package it.polimi.ingsw.model;

import java.util.Random;

public class Bag {
    private Colour bag[];

    private enum Colour{ GREEN, RED, YELLOW, PINK, BLUE };

    public Bag(){
        this.bag = new Colour[] { Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,
                                  Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,
                                  Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,
                                  Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,
                                  Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE
                                };

        private void randomize(){
            Random r = new Random();
            for(int i = 0; i < 130; i++){
                int j = r.nextInt(131);
                Colour tmp = bag[i];
                bag[i] = bag[j];
                bag[j] = tmp;
            }
        }

    }

}
