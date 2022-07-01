package it.polimi.ingsw.client.message.special;

import it.polimi.ingsw.client.message.Message;

/**
 * Special9Message contains all the chosen parameters to use it.
 */
public class Special9Message implements Message {
    private final int color;

    public Special9Message(int color) { this.color = color; }

    public int getColor() { return color; }
}
