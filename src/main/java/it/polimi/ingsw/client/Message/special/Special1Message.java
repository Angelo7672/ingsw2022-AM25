package it.polimi.ingsw.client.message.special;

import it.polimi.ingsw.client.message.Message;

public class Special1Message implements Message {
    private int color;
    private int islandRef;

    public Special1Message(int color, int islandRef) {
        this.color = color;
        this.islandRef = islandRef;
    }

    public int getColor() { return color; }
    public int getIslandRef() { return islandRef; }
}
