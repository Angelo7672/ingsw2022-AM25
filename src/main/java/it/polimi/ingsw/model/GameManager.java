package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.listeners.*;

import java.util.ArrayList;

public interface GameManager {

    //Planification Phase
    boolean refreshStudentsCloud();
    void queueForPlanificationPhase();
    int readQueue(int pos);
    boolean playCard(int playerRef, int queueRef, String card);

    //Action Phase
    void inOrderForActionPhase();
    void useSpecial(int specialIndex, int playerRef, int ref, ArrayList<Integer> color1, ArrayList<Integer> color2);
    void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef);
    boolean moveMotherNature(int queueRef, int desiredMovement);
    void chooseCloud(int playerRef,int cloudRef);

    //to be called at the end of the turn in which refreshCloudStudents gave true
    String oneLastRide();

    //to add the virtualView as a listener for model classess
    public void setStudentsEntranceListener(StudentsEntranceListener listener);
    public void setStudentsTableListener(StudentsTableListener listener);
    public void setTowerSchoolListener(TowerSchoolListener listener);
    public void setProfessorsListener(ProfessorsListener listener);
    public void setPlayedCardListener(PlayedCardListener listener);
    public void setPlayedSpecialListener(PlayedSpecialListener listener);
    public void setCoinsListener(CoinsListener listener);
    public void setIslandSizeListener(IslandSizeListener listener);
    public void setStudentsIslandListener(StudentIslandListener listener);
    public void setTowersIslandListener(TowerIslandListener listener);
    public void setMotherPositionListener(MotherPositionListener listener);
}
