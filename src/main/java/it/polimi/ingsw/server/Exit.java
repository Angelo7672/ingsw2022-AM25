package it.polimi.ingsw.server;

import it.polimi.ingsw.server.expertmode.ExpertGame;

import java.util.ArrayList;

public interface Exit {
    void start();
    void goPlayCard(int ref);
    void unlockPlanningPhase(int ref);
    void startActionPhase(int ref);
    void unlockActionPhase(int ref);

    void gameOver();

    void sendGameInfo(int numberOfPlayers, boolean expertMode);
    void sendUserInfo(int playerRef, String nickname, String character);
    void studentsChangeInSchool(int color, String place, int componentRef, int newStudentsValue);
    void studentChangeOnIsland(int islandRef, int color, int newStudentsValue);
    void studentChangeOnCloud(int cloudRef, int color, int newStudentsValue);
    void professorChangePropriety(int playerRef, int color, boolean newProfessorValue);
    void motherChangePosition(int newMotherPosition);
    void lastCardPlayedFromAPlayer(int playerRef, String assistantCard);
    void numberOfCoinsChangeForAPlayer(int playerRef, int newCoinsValue);
    void dimensionOfAnIslandIsChange(int islandToDelete);
    void towersChangeInSchool(int playerRef, int towersNumber);
    void towersChangeOnIsland(int islandRef, int towersNumber);
    void towerChangeColorOnIsland(int islandRef, int newColor);
    void islandInhibited(int islandRef, int isInhibited);
    void setSpecial(int specialRef);
    void setExpertGame(ExpertGame expertGame);
    void sendUsedSpecial(int playerRef, int indexSpecial);
    void sendHandAfterRestore(int playerRef, ArrayList<String> hand);
}
