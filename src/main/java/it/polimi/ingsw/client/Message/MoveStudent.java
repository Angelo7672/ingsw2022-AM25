package it.polimi.ingsw.client.Message;

public class MoveStudent implements Message{
    public final int color;
    public final boolean inSchool;
    public final int islandRef;

    public MoveStudent(int color, boolean inSchool) {
        this.color = color;
        this.inSchool = inSchool;
        this.islandRef = -1;
    }

    public MoveStudent(int color, int islandRef) {
        this.color = color;
        this.inSchool = false;
        this.islandRef = islandRef;
    }
}
