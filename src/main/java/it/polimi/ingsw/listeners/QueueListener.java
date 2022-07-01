package it.polimi.ingsw.listeners;

/**
 * Used in the server to notify the order of play each turn, based on the played cards
 */
public interface QueueListener {
    /**
     * Notifies the order of playing
     * @param queueRef of type int - index to the queue position of the player
     * @param playerRef of type int - index of the player
     */
    void notifyQueue(int queueRef, int playerRef);
    /**
     * Notifies the value of the played card
     * @param queueRef of type int - index to the queue position of the player
     * @param valueCard of type int - value of the card
     */
    void notifyValueCard(int queueRef, int valueCard);
    /**
     * Notifies the value of the played card
     * @param queueRef of type int - index to the queue position of the player
     * @param maxMove of type int - movement of mother nature of the card
     */
    void notifyMaxMove(int queueRef, int maxMove);
}
