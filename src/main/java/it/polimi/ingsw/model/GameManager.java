package it.polimi.ingsw.model;

public interface GameManager {

    //Planification Phase
    boolean refreshStudentsCloud(int numberOfPlayer);
    void queueForPlanificationPhase();
    int readQueue(int pos);
    String playCard(int playerRef, String card);

    //Action Phase
    //readQueue
    void inOrderForActionPhase();
    void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef);
    boolean moveMotherNature(int queueRef, int desiredMovement);
    void chooseCloud(int playerRef,int cloudRef);

    //to be called at the end of the turn in which refreshCloudStudents gave true
    String oneLastRide();
}
