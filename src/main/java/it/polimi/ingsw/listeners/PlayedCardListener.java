package it.polimi.ingsw.listeners;

import java.util.ArrayList;

public interface PlayedCardListener {
    public void notifyPlayedCard(int playerRef, String assistantCard);
    public void notifyHand(int playerRef, ArrayList<String> hand);
}
