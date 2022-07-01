package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.exception.EndGameException;

import java.util.ArrayList;
import java.util.List;

/**
 * Entrance interface is used to send incoming messages from clients to Server.
 */
public interface Entrance {
    /**
     * @see Server
     */
    boolean checkFile();

    /**
     * @see Server
     */
    List<Integer> lastSavedGame();

    /**
     * @see Server
     */
    int checkRestoreNickname(String nickname);

    /**
     * @see Server
     */
    void startController(int numberOfPlayers, boolean expertMode);

    /**
     * @see Server
     */
    boolean isExpertMode();

    /**
     * @see Server
     */
    void createGame(boolean restore);

    /**
     * @see Server
     */
    void initializeGame();

    /**
     * @see Server
     */
    void restoreVirtualView();

    /**
     * @see Server
     */
    void restoreGame();

    /**
     * @see Server
     */
    void startGame();

    /**
     * @see Server
     */
    ArrayList<String> alreadyChosenCharacters();

    /**
     * @see Server
     */
    int userLogin(String nickname, String character);

    /**
     * @see Server
     */
    boolean userPlayCard(int playerRef, String assistant);

    /**
     * @see Server
     */
    boolean userMoveStudent(int playerRef, int colour, boolean inSchool, int islandRef);

    /**
     * @see Server
     */
    boolean userMoveMotherNature(int desiredMovement) throws EndGameException;

    /**
     * @see Server
     */
    boolean userChooseCloud(int playerRef, int cloudRef);

    /**
     * @see Server
     */
    boolean useStrategySimple(int indexSpecial, int playerRef, int ref);

    /**
     * @see Server
     */
    boolean useSpecial3(int playerRef, int islandRef) throws EndGameException;

    /**
     * @see Server
     */
    boolean useSpecialLite(int indexSpecial, int playerRef);

    /**
     * @see Server
     */
    boolean useSpecialSimple(int indexSpecial, int playerRef, int ref);

    /**
     * @see Server
     */
    boolean useSpecialMedium(int indexSpecial, int playerRef, int ref, int color);

    /**
     * @see Server
     */
    boolean useSpecialHard(int indexSpecial, int playerRef, ArrayList<Integer> color1, ArrayList<Integer> color2);

    /**
     * @see Server
     */
    void resumeTurn(int phase);

    /**
     * @see Server
     */
    String endGame();

    /**
     * @see Server
     */
    void exitError();

    /**
     * @see Server
     */
    void gameOver();
}
