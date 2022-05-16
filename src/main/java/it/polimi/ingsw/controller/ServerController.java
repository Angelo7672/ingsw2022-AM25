package it.polimi.ingsw.controller;

import java.util.ArrayList;

public interface ServerController {
    boolean refreshStudentsCloud();
    void queueForPlanificationPhase();
    int readQueue(int pos);
    void playCard(int playerRef,int currentPlayer,String chosenAssistants);
    ArrayList<String> getHand(int playerRef);
    ArrayList<String> getPlayedCardsInThisTurn();
    void inOrderForActionPhase();
    void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef);
    boolean moveMotherNature(int queueRef, int desiredMovement);
    void chooseCloud(int playerRef, int cloudRef);
}
