package it.polimi.ingsw.listeners;

public interface QueueListener {
    public void notifyQueue(int queueRef, int playerRef);
    public void notifyValueCard(int queueRef, int valueCard);
    public void notifyMaxMove(int queueRef, int maxMove);

}