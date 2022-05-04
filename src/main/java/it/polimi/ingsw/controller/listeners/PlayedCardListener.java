package it.polimi.ingsw.controller.listeners;

public interface PlayedCardListener {
    public void notifyPlayedCard(int playerRef, String assistantCard);
}
