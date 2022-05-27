package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.exception.EndGameException;

public interface Entrance {
    void startController(int numberOfPlayers, boolean expertMode);
    boolean userLogin(String nickname, String character, int playerRef);

    boolean userPlayCard(int playerRef, String assistant);

    boolean userMoveStudent(int playerRef, int colour, boolean inSchool, int islandRef);
    boolean userMoveMotherNature(int desiredMovement) throws EndGameException;
    boolean userChooseCloud(int playerRef, int cloudRef);

    void resumeTurn();

    void exitError();
}
