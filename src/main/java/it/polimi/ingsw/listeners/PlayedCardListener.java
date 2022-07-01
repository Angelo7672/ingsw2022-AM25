package it.polimi.ingsw.listeners;

import java.util.ArrayList;

/**
 * Used to notify when a card has been played and the list of cards the player currently has
 */
public interface PlayedCardListener {
    /**
     * Notifies the last played card
     * @param playerRef of type int - index of the player
     * @param assistantCard of type String - name of the assistant
     */
    void notifyPlayedCard(int playerRef, String assistantCard);
    /**
     * Used to restore the card after loading a file saving, notifies all the cards of a player
     * @param playerRef of type int - index of the player
     * @param hand of type rrayList<String> - names of the assistants
     */
    void notifyHand(int playerRef, ArrayList<String> hand);
}
