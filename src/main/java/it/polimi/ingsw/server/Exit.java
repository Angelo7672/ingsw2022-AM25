package it.polimi.ingsw.server;

import it.polimi.ingsw.server.expertmode.ExpertGame;

import java.util.ArrayList;

/**
 * Exit interface is used to send to clients information from Server.
 */
public interface Exit {
    /**
     * @see Proxy_s
     */
    void start();

    /**
     * @see Proxy_s
     */
    void goPlayCard(int ref);

    /**
     * @see Proxy_s
     */
    void unlockPlanningPhase(int ref);

    /**
     * @see Proxy_s
     */
    void startActionPhase(int ref);

    /**
     * @see Proxy_s
     */
    void unlockActionPhase(int ref);

    /**
     * @see Proxy_s
     */
    void sendMaxMovementMotherNature(int ref, int maxMovement);

    /**
     * @see Proxy_s
     */
    void gameOver();

    /**
     * @see Proxy_s
     */
    void sendGameInfo(int numberOfPlayers, boolean expertMode);

    /**
     * @see Proxy_s
     */
    void sendUserInfo(int playerRef, String nickname, String character);

    /**
     * @see Proxy_s
     */
    void studentsChangeInSchool(int color, String place, int componentRef, int newStudentsValue);

    /**
     * @see Proxy_s
     */
    void studentChangeOnIsland(int islandRef, int color, int newStudentsValue);

    /**
     * @see Proxy_s
     */
    void studentChangeOnCloud(int cloudRef, int color, int newStudentsValue);

    /**
     * @see Proxy_s
     */
    void professorChangePropriety(int playerRef, int color, boolean newProfessorValue);

    /**
     * @see Proxy_s
     */
    void motherChangePosition(int newMotherPosition);

    /**
     * @see Proxy_s
     */
    void lastCardPlayedFromAPlayer(int playerRef, String assistantCard);

    /**
     * @see Proxy_s
     */
    void numberOfCoinsChangeForAPlayer(int playerRef, int newCoinsValue);

    /**
     * @see Proxy_s
     */
    void dimensionOfAnIslandIsChange(int islandToDelete);

    /**
     * @see Proxy_s
     */
    void towersChangeInSchool(int playerRef, int towersNumber);

    /**
     * @see Proxy_s
     */
    void towersChangeOnIsland(int islandRef, int towersNumber);

    /**
     * @see Proxy_s
     */
    void towerChangeColorOnIsland(int islandRef, int newColor);

    /**
     * @see Proxy_s
     */
    void islandInhibited(int islandRef, int isInhibited);

    /**
     * @see Proxy_s
     */
    void setSpecial(int specialRef, int cost);

    /**
     * @see Proxy_s
     */
    void setExpertGame(ExpertGame expertGame);

    /**
     * @see Proxy_s
     */
    void sendUsedSpecial(int playerRef, int indexSpecial);

    /**
     * @see Proxy_s
     */
    void sendHandAfterRestore(int playerRef, ArrayList<String> hand);

    /**
     * @see Proxy_s
     */
    void sendInfoSpecial1or7or11(int specialIndex, int studentColor, int value);

    /**
     * @see Proxy_s
     */
    void sendInfoSpecial5(int cards);
}