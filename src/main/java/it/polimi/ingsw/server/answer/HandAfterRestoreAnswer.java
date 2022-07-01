package it.polimi.ingsw.server.answer;

import java.util.ArrayList;

/**
 * HandAfterRestoreAnswer contains info about cards of last saved game of the recipient player.
 */
public class HandAfterRestoreAnswer implements Answer{
    private ArrayList<String> hand;

    /**
     * Create an answer contains info about cards of last saved game of the recipient player.
     * @param hand hand of a player of last saved game;
     */
    public HandAfterRestoreAnswer(ArrayList<String> hand) {
        for (String h:hand)
            System.out.println(h);  //TODO: togli
        this.hand = hand; }

    public ArrayList<String> getHand() { return hand; }
}