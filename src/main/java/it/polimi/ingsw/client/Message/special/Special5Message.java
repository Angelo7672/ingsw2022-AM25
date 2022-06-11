package it.polimi.ingsw.client.Message.special;

import it.polimi.ingsw.client.Message.Message;

public class Special5Message implements Message {
    private int islandRef;

    public Special5Message(int islandRef) { this.islandRef = islandRef; }

    public int getIslandRef() { return islandRef; }
}
