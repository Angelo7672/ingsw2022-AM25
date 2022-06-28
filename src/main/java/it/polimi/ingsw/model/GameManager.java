package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.ArrayList;
import java.util.List;

/**
 * This interface is used to interface Game to Controller
 */
public interface GameManager {

    /**
     * @see Game
     */
    void initializeGame();

    /**
     * @see Game
     */
    void createSpecial();

    /**
     * @see Game
     */
    ArrayList<Integer> getExtractedSpecials();

    /**
     * @see Game
     */
    ArrayList<Integer> getSpecialCost();

    //Planification Phase

    /**
     * @see Game
     */
    void refreshStudentsCloud();

    /**
     * @see Game
     */
    void queueForPlanificationPhase();

    /**
     * @see Game
     */
    int readQueue(int pos);

    /**
     * @see Game
     */
    boolean playCard(int playerRef, int queueRef, String card, ArrayList<String> alreadyPlayedCard) throws NotAllowedException;

    //Action Phase

    /**
     * @see Game
     */
    void inOrderForActionPhase();

    /**
     * @see
     */
    boolean useStrategySimple(int indexSpecial, int playerRef, int ref);

    /**
     * @see Game
     */
    boolean useSpecialLite(int indexSpecial, int playerRef);

    /**
     * @see Game
     */
    boolean useSpecialSimple(int indexSpecial, int playerRef, int ref);

    /**
     * @see Game
     */
    boolean useSpecialMedium(int indexSpecial, int playerRef, int ref, int color);

    /**
     * @see Game
     */
    boolean useSpecialHard(int indexSpecial, int playerRef, ArrayList<Integer> color1, ArrayList<Integer> color2);

    /**
     * @see Game
     */
    void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) throws NotAllowedException;

    /**
     * @see Game
     */
    boolean moveMotherNature(int queueRef, int desiredMovement) throws NotAllowedException;

    /**
     * @see Game
     */
    void chooseCloud(int playerRef,int cloudRef) throws NotAllowedException;

    /**
     * To be called at the end of the turn in which refreshCloudStudents or playCard gave true or if bag is empty.
     * @see Game
     */
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

    /**
     * @see Game
     */
    void schoolRestore(int playerRef, int[] studentsEntrance, int[] studentsTable, int towers, boolean[] professors, String team);

    /**
     * @see Game
     */
    void handAndCoinsRestore(int playerRef, ArrayList<String> cards, int coins);

    /**
     * @see Game
     */
    void cloudRestore(int cloudRef, int[] students);

    /**
     * @see Game
     */
    void setIslandsSizeAfterRestore(int size);

    /**
     * @see Game
     */
    void islandRestore(int islandRef, int[] students, int towerValue, String towerTeam, int inhibited);

    /**
     * @see Game
     */
    void restoreMotherPose(int islandRef);

    /**
     * @see Game
     */
    void bagRestore(List<Integer> bag);

    /**
     * @see Game
     */
    void queueRestore(ArrayList<Integer> playerRef, ArrayList<Integer> valueCard, ArrayList<Integer> maxMoveMotherNature);

    /**
     * @see Game
     */
    void specialRestore(int specialIndex, int cost);

    /**
     * @see Game
     */
    void specialStudentRestore(int indexSpecial, int[] students);

    /**
     * @see Game
     */
    void noEntryCardsRestore(int numCards);
}
