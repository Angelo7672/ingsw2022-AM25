package it.polimi.ingsw.client.message.special;

import it.polimi.ingsw.client.message.Message;

/**
 * Special11Message contains all the chosen parameters to use it.
 */
public class Special11Message implements Message {
    private final int color;

    public Special11Message(int color) { this.color = color; }

    public int getColor() { return color; }
}
