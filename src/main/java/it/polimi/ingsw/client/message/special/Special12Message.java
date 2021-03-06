package it.polimi.ingsw.client.message.special;

import it.polimi.ingsw.client.message.Message;

/**
 * Special12Message contains all the chosen parameters to use it.
 */
public class Special12Message implements Message {
    private final int color;

    public Special12Message(int color) { this.color = color; }

    public int getColor() { return color; }
}

