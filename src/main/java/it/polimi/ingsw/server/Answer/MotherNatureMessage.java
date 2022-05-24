package it.polimi.ingsw.server.Answer;

public class MotherNatureMessage implements Answer{
    private final int pos;


    public MotherNatureMessage(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
