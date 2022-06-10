package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.listeners.*;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.ArrayList;
import java.util.List;

public interface GameManager {
    void initializeGame();
    ArrayList<Integer> getExtractedSpecials();

    //Planification Phase
    boolean refreshStudentsCloud();
    void queueForPlanificationPhase();
    int readQueue(int pos);
    boolean playCard(int playerRef, int queueRef, String card, ArrayList<String> alreadyPlayedCard) throws NotAllowedException;

    //Action Phase
    void inOrderForActionPhase();


    boolean useSpecialLite(int indexSpecial, int playerRef);
    boolean useSpecialSimple(int indexSpecial, int playerRef, int ref);
    boolean useSpecialMedium(int indexSpecial, int playerRef, int ref, int color);
    boolean useSpecialHard(int indexSpecial, int playerRef, int ref, ArrayList<Integer> color1, ArrayList<Integer> color2);
    void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) throws NotAllowedException;
    boolean moveMotherNature(int queueRef, int desiredMovement) throws NotAllowedException;
    void chooseCloud(int playerRef,int cloudRef) throws NotAllowedException;

    //to be called at the end of the turn in which refreshCloudStudents gave true
    String oneLastRide();

    //to add the virtualView as a listener for model classes
    void setStudentsListener(StudentsListener listener);
    void setTowerListener(TowersListener listener);
    void setProfessorsListener(ProfessorsListener listener);
    void setPlayedCardListener(PlayedCardListener listener);
    void setSpecialListener(SpecialListener listener);
    void setCoinsListener(CoinsListener listener);
    void setIslandListener(IslandListener listener);
    void setMotherPositionListener(MotherPositionListener listener);
    void setInhibitedListener(InhibitedListener listener);
    void setBagListener(BagListener listener);
    void setQueueListener(QueueListener listener);

    //Restore game
    void schoolRestore(int playerRef, int[] studentsEntrance, int[] studentsTable, int towers, boolean[] professors, String team);
    void handAndCoinsRestore(int playerRef, ArrayList<String> cards, int coins);
    void cloudRestore(int cloudRef, int[] students);
    void setIslandsSizeAfterRestore(int size);
    void islandRestore(int islandRef, int[] students, int towerValue, String towerTeam, int inhibited);
    void restoreMotherPose(int islandRef);
    void bagRestore(List<Integer> bag);
    void queueRestore(ArrayList<Integer> playerRef, ArrayList<Integer> valueCard, ArrayList<Integer> maxMoveMotherNature);
}
