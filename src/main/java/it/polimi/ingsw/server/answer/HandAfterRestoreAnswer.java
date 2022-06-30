package it.polimi.ingsw.server.answer;

import java.util.ArrayList;

/**
 * HandAfterRestoreAnswer contains info about cards of last saved game of the recipient player.
 */
public class HandAfterRestoreAnswer implements Answer{
    private ArrayList<String> hand;

    public HandAfterRestoreAnswer(ArrayList<String> hand) {
        for (String h:hand)
            System.out.println(h);
        this.hand = hand; }

    public ArrayList<String> getHand() { return hand; }
}