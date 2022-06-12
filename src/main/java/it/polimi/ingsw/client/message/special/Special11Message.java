package it.polimi.ingsw.client.message.special;

import it.polimi.ingsw.client.message.Message;

public class Special11Message implements Message {
    private int color;

    public Special11Message(int color) { this.color = color; }

    public int getColor() { return color; }
}
