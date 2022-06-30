package it.polimi.ingsw.listeners;

import java.util.ArrayList;

public interface PlayedCardListener {
    void notifyPlayedCard(int playerRef, String assistantCard);
    void notifyHand(int playerRef, ArrayList<String> hand);
}
