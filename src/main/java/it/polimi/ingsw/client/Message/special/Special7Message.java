package it.polimi.ingsw.client.Message.special;

import it.polimi.ingsw.client.Message.Message;

import java.util.ArrayList;

public class Special7Message implements Message {
    private ArrayList<Integer> entranceStudent;
    private ArrayList<Integer> cardStudent;

    public Special7Message(ArrayList<Integer> entranceStudent, ArrayList<Integer> cardStudent) {
        this.entranceStudent = entranceStudent;
        this.cardStudent = cardStudent;
    }

    public ArrayList<Integer> getEntranceStudent() { return entranceStudent; }
    public ArrayList<Integer> getCardStudent() { return cardStudent; }
}
