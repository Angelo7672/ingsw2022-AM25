package it.polimi.ingsw.server;

import java.util.ArrayList;

/**
 * ControllerServer is used by Controller to comunicate with Server;
 */
public interface ControllerServer {
    /**
     * @see Server
     */
    void setExpertGame();

    /**
     * @see Server
     */
    void goPlayCard(int playerRef);

    /**
     * @see Server
     */
    void unlockPlanningPhase(int playerRef);

    /**
     * @see Server
     */
    void startActionPhase(int playerRef);

    /**
     * @see Server
     */
    void unlockActionPhase(int playerRef);

    /**
     * @see Server
     */
    void sendMaxMovementMotherNature(int playerRef, int maxMovement);

    /**
     * @see Server
     */
    void gameOver();

    /**
     * @see Server
     */
    void sendGameInfo(int numberOfPlayers, boolean expertMode);

    /**
     * @see Server
     */
    void sendUserInfo(int playerRef, String nickname, String character);

    /**
     * @see Server
     */
    void studentsChangeInSchool(int color, String place, int componentRef, int newStudentsValue);

    /**
     * @see Server
     */
    void studentChangeOnIsland(int islandRef, int color, int newStudentsValue);

    /**
     * @see Server
     */
    void studentChangeOnCloud(int cloudRef, int color, int newStudentsValue);

    /**
     * @see Server
     */
    void professorChangePropriety(int playerRef, int color, boolean newProfessorValue);

    /**
     * @see Server
     */
    void motherChangePosition(int newMotherPosition);

    /**
     * @see Server
     */
    void lastCardPlayedFromAPlayer(int playerRef, String assistantCard);

    /**
     * @see Server
     */
    void numberOfCoinsChangeForAPlayer(int playerRef, int newCoinsValue);

    /**
     * @see Server
     */
    void dimensionOfAnIslandIsChange(int islandToDelete);

    /**
     * @see Server
     */
    void towersChangeInSchool(int playerRef, int towersNumber);

    /**
     * @see Server
     */
    void towersChangeOnIsland(int islandRef, int towersNumber);

    /**
     * @see Server
     */
    void towerChangeColorOnIsland(int islandRef, int newColor);

    /**
     * @see Server
     */
    void islandInhibited(int islandRef, int isInhibited);

    /**
     * @see Server
     */
    void setSpecial(int specialRef, int cost);

    /**
     * @see Server
     */
    void sendUsedSpecial(int playerRef, int indexSpecial);

    /**
     * @see Server
     */
    void sendHandAfterRestore(int playerRef, ArrayList<String> hand);

    /**
     * @see Server
     */
    void sendInfoSpecial1or7or11(int specialIndex, int studentColor, int value);

    /**
     * @see Server
     */
    void sendInfoSpecial5(int cards);
}
