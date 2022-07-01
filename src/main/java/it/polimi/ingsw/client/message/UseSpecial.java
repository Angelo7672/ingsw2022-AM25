package it.polimi.ingsw.client.message;

/**
 * UseSpecial contains the chosen number of special.
 */
public class UseSpecial implements Message{
    private final int indexSpecial;

    public UseSpecial(int indexSpecial) {
        this.indexSpecial = indexSpecial;
    }

    public int getIndexSpecial() {
        return indexSpecial;
    }


}
