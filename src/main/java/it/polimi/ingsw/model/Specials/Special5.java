package it.polimi.ingsw.model.Specials;

public class Special5 extends Special{
    private int noEntry;

    Special5(){
        super(2, "special5");
        noEntry=4;
    }

    /*public void effect() {

    }*/

    public int getNoEntry(){
        return noEntry;
    }

    public void increaseNoEntry(){
        noEntry+=1;
    }

    public void decreaseNoEntry(){
        noEntry-=1;
    }

}
