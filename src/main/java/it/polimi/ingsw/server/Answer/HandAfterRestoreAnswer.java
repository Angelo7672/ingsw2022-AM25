package it.polimi.ingsw.server.Answer;

import java.util.ArrayList;

public class HandAfterRestoreAnswer implements Answer{
    private ArrayList<String> hand;

    public HandAfterRestoreAnswer(ArrayList<String> hand) { this.hand = hand; }

    public ArrayList<String> getHand() { return hand; }
}
