package it.polimi.ingsw.server;

public interface ControllerServer {
    void goPlayCard(int playerRef);
    void unlockPlanningPhase(int playerRef);
    void startActionPhase(int playerRef);
    void unlockActionPhase(int playerRef);

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
}
