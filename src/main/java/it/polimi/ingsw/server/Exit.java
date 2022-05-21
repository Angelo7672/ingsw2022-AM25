package it.polimi.ingsw.server;

public interface Exit {
    void start();
    void goPlayCard(int ref);
    void unlockPlanningPhase(int ref);
    void startActionPhase(int ref);
    void unlockActionPhase(int ref);
}
