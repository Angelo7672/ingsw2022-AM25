package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.ArrayList;

public interface ServerController {
    void startGame(boolean gameSave);
    int checkRestoreNickname(String nickname);
    boolean userLoginNickname(String nickname);
    ArrayList<String> alreadyChosenCharacters();
    boolean userLoginCharacter(String character);
    void addNewPlayer(String nickname, String character);
    void playCard(int playerRef,String chosenAssistants) throws NotAllowedException;
    void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) throws NotAllowedException;
    void moveMotherNature(int desiredMovement) throws NotAllowedException, EndGameException;
    void chooseCloud(int playerRef, int cloudRef) throws NotAllowedException;
    boolean useSpecialSimple(int indexSpecial, int playerRef, int ref);
    boolean useSpecialMedium(int indexSpecial, int playerRef, int ref, int color);
    boolean useSpecialHard(int specialIndex, int playerRef, int ref, ArrayList<Integer> color1, ArrayList<Integer> color2);
    void resumeTurn(int phase);
    String getWinner();
    boolean isExpertMode();
}
