package it.polimi.ingsw.client.Message.Special;

import it.polimi.ingsw.client.Message.Message;

public class Special12Message implements Message {
    private int color;

    public Special12Message(int color) { this.color = color; }

    public int getColor() { return color; }
}

