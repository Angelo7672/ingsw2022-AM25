package it.polimi.ingsw.client.message;

import java.util.ArrayList;

public class UseSpecial implements Message{
    private final int indexSpecial;

    public UseSpecial(int indexSpecial) {
        this.indexSpecial = indexSpecial;
    }

    public int getIndexSpecial() {
        return indexSpecial;
    }


}
