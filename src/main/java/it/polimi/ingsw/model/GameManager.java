package it.polimi.ingsw.model;

public interface GameManager {

    //Planification Phase
    void refreshStudentsCloud(int numberOfPlayer);
    void queueForPlanificationPhase();
    int readQueue(int pos);
    void playCard(int playerRef, String card);

    //Action Phase
    //readQueue
    void inOrderForActionPhase();
    void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef);
    boolean moveMotherNature(int queueRef, int desiredMovement);
    void chooseCloud(int playerRef,int cloudRef);
}
