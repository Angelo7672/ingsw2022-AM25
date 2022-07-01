package it.polimi.ingsw.client.message.special;

import it.polimi.ingsw.client.message.Message;

/**
 * Special5Message contains all the chosen parameters to use it.
 */
public class Special5Message implements Message {
    private int islandRef;

    public Special5Message(int islandRef) { this.islandRef = islandRef; }

    public int getIslandRef() { return islandRef; }
}
