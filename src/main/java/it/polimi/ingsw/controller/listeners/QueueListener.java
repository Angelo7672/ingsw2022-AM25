package it.polimi.ingsw.controller.listeners;

public interface QueueListener {
    public void notifyQueue(int playerRef);
    public void notifyResetQueue();
}
