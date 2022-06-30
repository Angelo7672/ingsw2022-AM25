package it.polimi.ingsw.controller;

import it.polimi.ingsw.server.ControllerServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VirtualViewTest {
    Controller controller;
    Controller controller1;
    String fileName = "saveGameTest.bin";

   @BeforeEach
    void initialization(){
        ControllerServer controllerServer = new ControllerServer() {
            @Override
            public void setExpertGame() {}
            @Override
            public void goPlayCard(int playerRef) {}
            @Override
            public void unlockPlanningPhase(int playerRef) {}
            @Override
            public void startActionPhase(int playerRef) {}
            @Override
            public void unlockActionPhase(int playerRef) {}
            @Override
            public void sendMaxMovementMotherNature(int playerRef, int maxMovement) {}
            @Override
            public void gameOver() {}
            @Override
            public void sendGameInfo(int numberOfPlayers, boolean expertMode) {}
            @Override
            public void sendUserInfo(int playerRef, String nickname, String character) {}
            @Override
            public void studentsChangeInSchool(int color, String place, int componentRef, int newStudentsValue) {}
            @Override
            public void studentChangeOnIsland(int islandRef, int color, int newStudentsValue) {}
            @Override
            public void studentChangeOnCloud(int cloudRef, int color, int newStudentsValue) {}
            @Override
            public void professorChangePropriety(int playerRef, int color, boolean newProfessorValue) {}
            @Override
            public void motherChangePosition(int newMotherPosition) {}
            @Override
            public void lastCardPlayedFromAPlayer(int playerRef, String assistantCard) {}
            @Override
            public void numberOfCoinsChangeForAPlayer(int playerRef, int newCoinsValue) {}
            @Override
            public void dimensionOfAnIslandIsChange(int islandToDelete) {}
            @Override
            public void towersChangeInSchool(int playerRef, int towersNumber) {}
            @Override
            public void towersChangeOnIsland(int islandRef, int towersNumber) {}
            @Override
            public void towerChangeColorOnIsland(int islandRef, int newColor) {}
            @Override
            public void islandInhibited(int islandRef, int isInhibited) {}
            @Override
            public void setSpecial(int specialRef, int cost) {}
            @Override
            public void sendUsedSpecial(int playerRef, int indexSpecial) {}
            @Override
            public void sendHandAfterRestore(int playerRef, ArrayList<String> hand) {}
            @Override
            public void sendInfoSpecial1or7or11(int specialIndex, int studentColor, int value) {}
            @Override
            public void sendInfoSpecial5(int cards) {}
        };
        controller = new Controller(2, true, controllerServer, fileName);
        controller.addNewPlayer("Angelo","WIZARD");
        controller.addNewPlayer("Ginevra","WITCH");
        controller.createGame(false);
        controller.initializeGame();
        controller1 = new Controller(2, true, controllerServer, fileName);
    }

    /**
     * Test persistence
     */
    @Test
    @DisplayName("First test: test save and restore game")
    void saveAndRestore(){
        controller.startGame();
        controller.resumeTurn(0);
        controller.saveGame();
        controller1.createGame(true);
        controller1.restoreVirtualView();
        controller1.restoreGame();
        assertEquals(0,controller1.checkRestoreNickname("Angelo"));
        assertEquals(1,controller1.checkRestoreNickname("Ginevra"));
        assertEquals(-1,controller1.checkRestoreNickname("Gigi"));
    }

    /**
     * Test if file is clear after game over method.
     */
    @Test
    @DisplayName("Clear file")
    void clearFile(){
        controller.startGame();
        controller.resumeTurn(0);
        controller.oneLastRide();
        File file = new File(fileName);
        assertTrue(file.length() == 0);
    }
}