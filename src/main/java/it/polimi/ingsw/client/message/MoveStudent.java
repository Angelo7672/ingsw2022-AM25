package it.polimi.ingsw.client.message;

/**
 * MoveStudent contains the chosen student to be moved and the place where it has to be moved..
 */
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
