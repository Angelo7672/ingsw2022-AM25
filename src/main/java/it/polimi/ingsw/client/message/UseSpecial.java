package it.polimi.ingsw.client.Message;

import java.util.ArrayList;

public class UseSpecial implements Message{
    private final int specialIndex;
    private final int ref;
    private final ArrayList<Integer> color1;
    private final ArrayList<Integer> color2;

    public UseSpecial(int specialIndex, int ref, ArrayList<Integer> color1, ArrayList<Integer> color2) {
        this.specialIndex = specialIndex;
        this.ref = ref;
        this.color1 = color1;
        this.color2 = color2;
    }

    public int getSpecialIndex() {
        return specialIndex;
    }

    public int getRef() {
        return ref;
    }

    public ArrayList<Integer> getColor1() {
        return color1;
    }

    public ArrayList<Integer> getColor2() {
        return color2;
    }
}
