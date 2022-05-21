package it.polimi.ingsw.model;

public enum Team {
    NONE(-1),
    WHITE(0),
    BLACK(1),
    GREY(2);
    private int team;

    Team(int team){
        this.team=team;
    }

    public int getTeam() {
        return team;
    }
    public void setTeam(int value){
        this.team=value;
    }
}
