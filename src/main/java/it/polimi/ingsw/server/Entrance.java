package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.server.expertmode.ExpertGame;

import java.util.ArrayList;
import java.util.List;

public interface Entrance {
    boolean checkFile();
    List<Integer> lastSavedGame();
    int checkRestoreNickname(String nickname);

    void startController(int numberOfPlayers, boolean expertMode);
    boolean isExpertMode();
    void createGame();
    void initializeGame();
    void restoreVirtualView();
    void restoreGame();
    void startGame();
    ExpertGame getExpertGame();
    ArrayList<String> alreadyChosenCharacters();
    int userLogin(String nickname, String character);

    boolean userPlayCard(int playerRef, String assistant);

    boolean userMoveStudent(int playerRef, int colour, boolean inSchool, int islandRef);
    boolean userMoveMotherNature(int desiredMovement) throws EndGameException;
    boolean userChooseCloud(int playerRef, int cloudRef);
    boolean useSpecialLite(int indexSpecial, int playerRef);
    boolean useSpecialSimple(int indexSpecial, int playerRef, int ref);
    boolean useSpecialMedium(int indexSpecial, int playerRef, int ref, int color);
    boolean useSpecialHard(int indexSpecial, int playerRef, ArrayList<Integer> color1, ArrayList<Integer> color2);

    void resumeTurn(int phase);

    String endGame();

    void exitError();

    /**
     * @see Server
     */
    void gameOver();
}
