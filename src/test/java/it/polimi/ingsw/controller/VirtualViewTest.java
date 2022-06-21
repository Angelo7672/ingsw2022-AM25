package it.polimi.ingsw.controller;

import it.polimi.ingsw.server.ControllerServer;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VirtualViewTest {
    VirtualView virtualView;

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
        Controller controller = new Controller(2, true, controllerServer);
        virtualView = new VirtualView(2, true, controllerServer, controller, "saveGame.bin");
    }


}