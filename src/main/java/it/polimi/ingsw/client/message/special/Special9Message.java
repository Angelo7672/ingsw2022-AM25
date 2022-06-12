package it.polimi.ingsw.client.message.special;

import it.polimi.ingsw.client.message.Message;

public class Special9Message implements Message {
    private int color;

    public Special9Message(int color) { this.color = color; }

    public int getColor() { return color; }
}
