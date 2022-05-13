package it.polimi.ingsw.server.Answer;

public class MoveMotherNatureMessage {
    private final int maxMovement;

    public MoveMotherNatureMessage(int maxMovement) {
        this.maxMovement = maxMovement;
    }

    public int getMaxMovement() {
        return maxMovement;
    }
}
