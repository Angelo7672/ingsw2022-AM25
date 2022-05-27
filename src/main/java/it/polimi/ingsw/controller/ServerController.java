package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.model.exception.NotAllowedException;

public interface ServerController {
    void startGame();
    boolean userLoginNickname(String nickname,int playerRef);
    boolean userLoginCharacter(String character, int playerRef);
    void addNewPlayer(String nickname, String character);
    void playCard(int playerRef,String chosenAssistants) throws NotAllowedException;
    void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) throws NotAllowedException;
    void moveMotherNature(int desiredMovement) throws NotAllowedException, EndGameException;
    void chooseCloud(int playerRef, int cloudRef) throws NotAllowedException;
    void resumeTurn();
}
