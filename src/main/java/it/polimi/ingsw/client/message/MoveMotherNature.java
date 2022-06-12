package it.polimi.ingsw.client.message;


public class MoveMotherNature implements Message{

    private final int desiredMovement;

    public MoveMotherNature(int desiredMovement) {
        this.desiredMovement = desiredMovement;
    }

    public int getDesiredMovement() {
        return desiredMovement;
    }
}
