package it.polimi.ingsw.model;

public abstract class Special {
    private int cost;
    private String name;

    public Special(int cost, String name){
        this.cost = cost;
        this.name = name;
    }

    public void increaseCost(){
        cost += 1;
    }
    public void setCost(int cost){ this.cost = cost; }
    public String getName(){
        return name;
    }
    public int getCost(){
        return cost;
    }
    public void effect(){}
    public void effect(int one, int two){}
}
