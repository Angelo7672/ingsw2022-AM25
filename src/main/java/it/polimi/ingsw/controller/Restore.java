package it.polimi.ingsw.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * This interface is used by VirtualView for comunicate with controller.
 */
public interface Restore {

    /**
     * @see Controller
     */
    void setJumpPhaseForRestore(String phase);

    /**
     * @see Controller
     */
    void schoolRestore(int playerRef, int[] studentsEntrance, int[] studentsTable, int towers, boolean[] professors, String team);

    /**
     * @see Controller
     */
    void handAndCoinsRestore(int playerRef, ArrayList<String> cards, int coins);

    /**
     * @see Controller
     */
    void cloudRestore(int cloudRef, int[] students);

    /**
     * @see Controller
     */
    void setIslandsSizeAfterRestore(int size);

    /**
     * @see Controller
     */
    void islandRestore(int islandRef, int[] students, int towerValue, String towerTeam, int inhibited);

    /**
     * @see Controller
     */
    void restoreMotherPose(int islandRef);

    /**
     * @see Controller
     */
    void bagRestore(List<Integer> bag);

    /**
     * @see Controller
     */
    void queueRestore(ArrayList<Integer> playerRef, ArrayList<Integer> valueCard, ArrayList<Integer> maxMoveMotherNature);

    /**
     * @see Controller
     */
    void specialRestore(int specialIndex, int cost);

    /**
     * @see Controller
     */
    void specialListRestore();

    /**
     * @see Controller
     */
    void specialStudentRestore(int specialIndex, int[] students);

    /**
     * @see Controller
     */
    void noEntryCardsRestore(int numCards);


    /**
     * @see Controller
     */
    void setCurrentUser(int currentUser);

    /**
     * @see Controller
     */
    void setEndBag(boolean end);
}
