package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.exception.EndGameException;

import java.util.ArrayList;

public interface Entrance {
    void startController(int numberOfPlayers, boolean expertMode);
    boolean isExpertMode();
    void startGame();
    ArrayList<String> alreadyChosenCharacters();
    boolean userLogin(String nickname, String character);

    boolean userPlayCard(int playerRef, String assistant);

    boolean userMoveStudent(int playerRef, int colour, boolean inSchool, int islandRef);
    boolean userMoveMotherNature(int desiredMovement) throws EndGameException;
    boolean userChooseCloud(int playerRef, int cloudRef);
    boolean useSpecialSimple(int indexSpecial, int playerRef, int ref);
    boolean useSpecialMedium(int indexSpecial, int playerRef, int ref, int color);
    boolean useSpecialHard(int specialIndex, int playerRef, int ref, ArrayList<Integer> color1, ArrayList<Integer> color2);

    void resumeTurn();

    String endGame();

    void exitError();
}
