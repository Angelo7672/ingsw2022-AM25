package it.polimi.ingsw.client.message.special;

import it.polimi.ingsw.client.message.Message;

/**
 * Special1Message contains all the chosen parameters to use it.
 */
public class Special1Message implements Message {
    private final int color;
    private final int islandRef;

    public Special1Message(int color, int islandRef) {
        this.color = color;
        this.islandRef = islandRef;
    }

    public int getColor() { return color; }
    public int getIslandRef() { return islandRef; }
}
