package it.polimi.ingsw.server.answer;

/**
 * MaxMovementMotherNatureAnswer contains info about the max movement of mother nature for recipient player.
 */
public class MaxMovementMotherNatureAnswer implements Answer{
    private final int maxMovement;

    /**
     * Create an answer contains info about the max movement of mother nature for recipient player.
     * @param maxMovement of mother nature;
     */
    public MaxMovementMotherNatureAnswer(int maxMovement) { this.maxMovement = maxMovement; }

    public int getMaxMovement() { return maxMovement; }
}
