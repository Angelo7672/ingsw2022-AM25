package it.polimi.ingsw.server.Answer;

import it.polimi.ingsw.model.Special;

import java.util.ArrayList;

public class SpecialMessage implements Answer{
    private final int[] color1;
    private final int[] color2;
    private final int refSize;
    private final int cost;
    private final String message;

    public SpecialMessage(int[] color1, int[] color2, int refSize, int cost, String message) {
        this.color1 = color1;
        this.color2 = color2;
        this.refSize = refSize;
        this.cost = cost;
        this.message = message;
    }


    @Override
    public String getMessage() {
        return message;
    }

    public int[] getColor1() {
        return color1;
    }

    public int[] getColor2() {
        return color2;
    }

    public int getCost() {
        return cost;
    }

    public int getRefSize() {
        return refSize;
    }
}
