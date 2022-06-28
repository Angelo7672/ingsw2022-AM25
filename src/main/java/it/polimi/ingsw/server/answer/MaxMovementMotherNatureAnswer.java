package it.polimi.ingsw.server.answer;

/**
 * MaxMovementMotherNatureAnswer contains info about the max movement of mother nature for recipient player.
 */
public class MaxMovementMotherNatureAnswer implements Answer{
    private final int maxMovement;

    public MaxMovementMotherNatureAnswer(int maxMovement) { this.maxMovement = maxMovement; }

    public int getMaxMovement() { return maxMovement; }
}
