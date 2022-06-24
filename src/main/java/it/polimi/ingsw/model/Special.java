package it.polimi.ingsw.model;

public abstract class Special {
    private int cost;

    public Special(int cost, String name){
        this.cost = cost;
    }

    public void increaseCost(){
        cost += 1;
    }
    public void setCost(int cost){ this.cost = cost; }
    public int getCost(){
        return cost;
    }
    public void effect(){}
    public void effect(int one, int two){}
}
