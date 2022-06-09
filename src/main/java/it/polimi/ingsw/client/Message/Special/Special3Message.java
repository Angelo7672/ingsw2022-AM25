package it.polimi.ingsw.client.Message.Special;

import it.polimi.ingsw.client.Message.Message;

public class Special3Message implements Message {
    private int islandRef;

    public Special3Message(int islandRef) { this.islandRef = islandRef; }

    public int getIslandRef() { return islandRef; }
}
