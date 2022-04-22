package it.polimi.ingsw.model;

public interface GameManager {

    //Planification Phase
    void refreshStudentsCloud();
    void queueForPlanificationPhase();
    int readQueue(int pos);
    void playCard(int playerRef, String card);

    //Action Phase
    //readQueue
    void inOrderForActionPhase();
    void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef);
    boolean moveMotherNature(int queueRef, int desiredMovement, int noColor, int islandRef,  String special, int index);
    void chooseCloud(int playerRef,int cloudRef);

}
