package it.polimi.ingsw.client.Message;

public class CheckSpecial implements Message{
    private final int special;

    public CheckSpecial(int special) {
        this.special = special;
    }

    public int getSpecial() {
        return special;
    }
}
