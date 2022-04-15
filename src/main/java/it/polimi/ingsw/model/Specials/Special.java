package it.polimi.ingsw.model.Specials;

public abstract class Special {

    private int cost;
    private String name;

    Special(int cost, String name){
        this.cost=cost;
        this.name=name;
    }

    //increase di uno?
    public void increaseCost(){
        cost+=1;
    }
    public String getName(){
        return name;
    }
    public int getCost(){
        return cost;
    }
    public void effect(int uno, int due){}

}
