package it.polimi.ingsw.model;

import java.util.ArrayList;

public interface GameManager {

    //Planification Phase
    boolean refreshStudentsCloud();
    void queueForPlanificationPhase();
    int readQueue(int pos);
    String playCard(int playerRef, int queueRef, String card);

    //Action Phase
    void inOrderForActionPhase();
    void useSpecial(int specialIndex, int playerRef, int ref, ArrayList<Integer> color1, ArrayList<Integer> color2);
    void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef);
    boolean moveMotherNature(int queueRef, int desiredMovement);
    void chooseCloud(int playerRef,int cloudRef);

    //to be called at the end of the turn in which refreshCloudStudents gave true
    String oneLastRide();
}
