package it.polimi.ingsw.client.message.special;

import it.polimi.ingsw.client.message.Message;

/**
 * Special3Message contains all the chosen parameters to use it.
 */
public class Special3Message implements Message {
    private int islandRef;

    public Special3Message(int islandRef) { this.islandRef = islandRef; }

    public int getIslandRef() { return islandRef; }
}
