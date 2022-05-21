package it.polimi.ingsw.server;

public interface ControllerServer {
    void goPlayCard(int playerRef);
    void unlockPlanningPhase(int playerRef);
    void startActionPhase(int playerRef);
    void unlockActionPhase(int playerRef);
}
