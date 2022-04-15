package it.polimi.ingsw.model;

public enum Assistant {
    LION(1, 1), GOOSE(2, 1), CAT(3, 2), EAGLE(4, 2), FOX(5, 3),
    LIZARD(6, 3), OCTOPUS(7, 4), DOG(8, 4), ELEPHANT(9, 5), TURTLE(10, 5),
    NONE(-1,-1);
    private final int value;
    private final int movement;

    Assistant(int value, int movement) {
            this.value = value;
            this.movement = movement;
    }

    public int getValue() { return value; }
    public int getMovement() { return movement; }
}
