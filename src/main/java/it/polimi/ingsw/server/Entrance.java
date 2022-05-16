package it.polimi.ingsw.server;

public interface Entrance {
    void setNumberOfPlayer(int numberOfPlayer);
    void setExpertMode(boolean expertMode);
    int getNumberOfPlayer();
    boolean isExpertMode();
    void startGame();
    void userLogin(String nickname, String character, int playerRef);
    void userPlayCard(int playerRef, String assistant);
    void userMoveStudent(int playerRef, int colour, boolean inSchool, int islandRef);
    void userMoveMotherNature(int desideredMovement);
    void userChooseCloud(int playerRef, int cloudRef);
}
