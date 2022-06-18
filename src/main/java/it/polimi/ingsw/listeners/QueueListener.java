package it.polimi.ingsw.listeners;

public interface QueueListener {
    void notifyQueue(int queueRef, int playerRef);
    void notifyValueCard(int queueRef, int valueCard);
    void notifyMaxMove(int queueRef, int maxMove);
}
