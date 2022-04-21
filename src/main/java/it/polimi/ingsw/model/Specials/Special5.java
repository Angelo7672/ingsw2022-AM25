package it.polimi.ingsw.model.Specials;

public class Special5 extends Special{
    private int noEntry;

    public Special5(){
        super(2, "special5");
        noEntry=4;
    }


    public int getNoEntry(){
        return noEntry;
    }

    public void effect(){
        noEntry+=1;
    }

    public void decreaseNoEntry(){
        noEntry-=1;
    }

}
