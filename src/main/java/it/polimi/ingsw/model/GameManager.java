package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.ArrayList;
import java.util.List;

/**
 * This interface is used to interface Game to Controller
 */
public interface GameManager {
    void initializeGame();
    ArrayList<Integer> getExtractedSpecials();
    ArrayList<Integer> getSpecialCost();

    //Planification Phase
    void refreshStudentsCloud();
    void queueForPlanificationPhase();
    int readQueue(int pos);
    boolean playCard(int playerRef, int queueRef, String card, ArrayList<String> alreadyPlayedCard) throws NotAllowedException;

    //Action Phase
    void inOrderForActionPhase();
    boolean useSpecialLite(int indexSpecial, int playerRef);
    boolean useSpecialSimple(int indexSpecial, int playerRef, int ref);
    boolean useSpecialMedium(int indexSpecial, int playerRef, int ref, int color);
    boolean useSpecialHard(int indexSpecial, int playerRef, ArrayList<Integer> color1, ArrayList<Integer> color2);
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
    void setSpecialStudentsListener(SpecialStudentsListener specialStudentsListener);
    void setNoEntryListener(NoEntryListener noEntryListener);

    //Restore game
    void schoolRestore(int playerRef, int[] studentsEntrance, int[] studentsTable, int towers, boolean[] professors, String team);
    void handAndCoinsRestore(int playerRef, ArrayList<String> cards, int coins);
    void cloudRestore(int cloudRef, int[] students);
    void setIslandsSizeAfterRestore(int size);
    void islandRestore(int islandRef, int[] students, int towerValue, String towerTeam, int inhibited);
    void restoreMotherPose(int islandRef);
    void bagRestore(List<Integer> bag);
    void queueRestore(ArrayList<Integer> playerRef, ArrayList<Integer> valueCard, ArrayList<Integer> maxMoveMotherNature);
    void specialRestore(int specialIndex, int cost);
    void specialStudentRestore(int[] students);
    void noEntryCardsRestore(int numCards);
}
