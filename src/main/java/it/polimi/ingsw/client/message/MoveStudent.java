package it.polimi.ingsw.client.message;

public class MoveStudent implements Message{
    private final int color;
    private final boolean inSchool;
    private final int islandRef;

    public MoveStudent(int color, boolean inSchool, int islandRef) {
        this.color = color;
        this.inSchool = inSchool;
        this.islandRef = islandRef;
    }

    public int getColor() {
        return color;
    }

    public boolean isInSchool() {
        return inSchool;
    }

    public int getIslandRef() {
        return islandRef;
    }
}
