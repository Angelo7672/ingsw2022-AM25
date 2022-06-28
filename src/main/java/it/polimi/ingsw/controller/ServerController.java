package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.ArrayList;

/**
 * ServerController is used by Server to comunicate with Controller.
 */
public interface ServerController {
    /**
     * @see Controller
     */
    void createGame();

    /**
     * @see Controller
     */
    void initializeGame();

    /**
     * @see Controller
     */
    void restoreVirtualView();

    /**
     * @see Controller
     */
    void restoreGame();

    /**
     * @see Controller
     */
    void startGame();

    /**
     * @see Controller
     */
    ArrayList<Integer> getExtractedSpecials();

    /**
     * @see Controller
     */
    int checkRestoreNickname(String nickname);

    /**
     * @see Controller
     */
    boolean userLoginNickname(String nickname);

    /**
     * @see Controller
     */
    ArrayList<String> alreadyChosenCharacters();

    /**
     * @see Controller
     */
    boolean userLoginCharacter(String character);

    /**
     * @see Controller
     */
    int addNewPlayer(String nickname, String character);

    /**
     * @see Controller
     */
    void playCard(int playerRef,String chosenAssistants) throws NotAllowedException;

    /**
     * @see Controller
     */
    void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) throws NotAllowedException;

    /**
     * @see Controller
     */
    void moveMotherNature(int desiredMovement) throws NotAllowedException, EndGameException;

    /**
     * @see Controller
     */
    void chooseCloud(int playerRef, int cloudRef) throws NotAllowedException;

    /**
     * @see Controller
     */
    boolean useStrategySimple(int indexSpecial, int playerRef, int ref);

    /**
     * @see Controller
     */
    boolean useSpecialLite(int indexSpecial, int playerRef);

    /**
     * @see Controller
     */
    boolean useSpecialSimple(int indexSpecial, int playerRef, int ref);

    /**
     * @see Controller
     */
    boolean useSpecialMedium(int indexSpecial, int playerRef, int ref, int color);

    /**
     * @see Controller
     */
    boolean useSpecialHard(int specialIndex, int playerRef, ArrayList<Integer> color1, ArrayList<Integer> color2);

    /**
     * @see Controller
     */
    void resumeTurn(int phase);

    /**
     * @see Controller
     */
    String getWinner();

    /**
     * @see Controller
     */
    boolean isExpertMode();
}
