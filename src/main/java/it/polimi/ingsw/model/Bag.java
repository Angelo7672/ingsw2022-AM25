package it.polimi.ingsw.model;

import java.util.Random;

public class Bag {
    private Colour bag[];

    private enum Colour{ GREEN, RED, YELLOW, PINK, BLUE ,NONE};

    public Bag(){
        this.bag = new Colour[] { Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,Colour.GREEN,
                                  Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED,
                                  Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,Colour.YELLOW,
                                  Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,Colour.PINK,
                                  Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE,Colour.BLUE
                                };
        randomize();
    }

    private void randomize(){
        Random r = new Random();
        for(int i = 0; i < 130; i++){
            int j = r.nextInt(130);
            Colour tmp = bag[i];
            bag[i] = bag[j];
            bag[j] = tmp;
        }
    }

    public int extraction(){
        int i = 0;
        Colour tmp;

        while(bag[i].equals(Colour.NONE) && i <= bag.length) i++;
        tmp = bag[i];
        bag[i] = Colour.NONE;

        if(tmp.equals(Colour.GREEN)) return 0;
        else if(tmp.equals(Colour.RED)) return 1;
        else if(tmp.equals(Colour.YELLOW)) return 2;
        else if(tmp.equals(Colour.PINK)) return 3;
        else if(tmp.equals(Colour.BLUE)) return 4;

        return -1;  //se ritorna -1 si e' cannato qualcosa!
    }

    public boolean checkVictory(){      //deve essere chiamato ogni volta che si fa un'estrazione
        if(bag[bag.length].equals(Colour.NONE)) return true;
        return false;
    }
}
