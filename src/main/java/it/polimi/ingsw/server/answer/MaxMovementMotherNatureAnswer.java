package it.polimi.ingsw.server.answer;

public class MaxMovementMotherNatureAnswer implements Answer{
    private final int maxMovement;

    public MaxMovementMotherNatureAnswer(int maxMovement) { this.maxMovement = maxMovement; }

    public int getMaxMovement() { return maxMovement; }
}
