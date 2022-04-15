package it.polimi.ingsw.model;

public enum Team {
    NOONE(-1),
    WHITE(0),
    BLACK(1),
    GREY(2);

    int value;

    Team(int value){
        this.value=value;
    }

    public int getTeam(){
        return value;
    }



}
