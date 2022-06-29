package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.model.exception.NotAllowedException;
import it.polimi.ingsw.server.ControllerServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    Controller controller;

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
        controller = new Controller(2, true , controllerServer, "saveGameTest.bin");
        controller.addNewPlayer("Angelo", "WIZARD");
        controller.addNewPlayer("Ginevra","WITCH");
        controller.createGame();
    }

    @Test
    @DisplayName("First test: method of planning phase")
    void planningPhase(){
        boolean checker = true;

        controller.initializeGame();
        controller.startGame();
        try{
            synchronized (this){ wait(1000); }
            controller.playCard(0,"LION");
            controller.playCard(1,"EAGLE");
        }catch (NotAllowedException notAllowedException){ checker = false;
        } catch (InterruptedException e) { e.printStackTrace(); }

        assertEquals(true,checker);
    }

    @Test
    @DisplayName("Second test: method of action phase")
    void actionPhase() {
        boolean checker = true;
        boolean end = false;
        boolean ok = true;
        int i = 0;

        controller.initializeGame();
        controller.startGame();
        try { synchronized (this){ wait(1000); }
        } catch (InterruptedException e) { e.printStackTrace(); }

        while (ok) {
            try {
                ok = false;
                checker = true;
                controller.moveStudent(0, i, true, -1);
            } catch (NotAllowedException notAllowedException) {
                i++;
                ok = true;
                checker = false;
            }
        }

        assertTrue(checker);

        try { controller.moveMotherNature(10);
        } catch (NotAllowedException notAllowedException) { checker = false;
        } catch (EndGameException endGameException) { end = true; }
        assertFalse("Checker is false because the player didn't play a card", checker);
        assertFalse("Game is not over",end);

        checker = true; //restore checker

        try { controller.chooseCloud(0, 0);
        } catch (NotAllowedException notAllowedException) { checker = false; }
        assertTrue(checker);
    }

    @Test
    @DisplayName("test if special lite work correctly")
    public void useSpecialLiteTest(){
        //Always false because we haven't enough money.
        assertFalse(controller.useSpecialLite(2,0));
        assertFalse(controller.useSpecialLite(6,0));
        assertFalse(controller.useSpecialLite(8,0));
    }

    @Test
    @DisplayName("test if special simple work correctly")
    public void useSpecialSimpleTest(){
        //Always false because we haven't enough money.
        assertFalse(controller.useSpecialSimple(5,0, 1));
        assertFalse(controller.useSpecialSimple(11,0, 1));
        assertFalse(controller.useSpecialSimple(12,0, 1));
    }

    @Test
    @DisplayName("Test if special3 work correctly")
    public void useSpecial3Test() {
        //Always false because we haven't enough money.
        try {
            assertFalse(controller.useSpecial3(0, 1));
            assertFalse(controller.useSpecial3(0, 15));
        }catch (EndGameException endGameException){ endGameException.printStackTrace(); }
    }
    /*
    @Test
    @DisplayName("test if special medium work correctly")
    public void useSpecialMediumTest(){
        assertFalse(controller.useStrategySimple(1,0,2));

        assertTrue(controller.useStrategySimple(1, 0, 1));
    }*/

    @Test
    @DisplayName("test if special strategy work correctly")
    public void useStrategySimple(){
        assertFalse(controller.useSpecialSimple(5,0, 1));
        assertFalse(controller.useSpecialSimple(11,0, 1));
        assertFalse(controller.useSpecialSimple(12,0, 1));
    }
/*
    @Test
    @DisplayName("test if special hard work correcly")
    public void useSpecialHardTest(){
        ArrayList<Integer> color1 = new ArrayList<>();
        ArrayList<Integer> color2 = new ArrayList<>();
        color2.add(0); color2.add(2);
        color1.add(1); color1.add(3);

        assertFalse(controller.useSpecialHard(7,0,color1, color2));
        assertTrue(controller.useSpecialHard(7, 0, color1, color2));
    }*/


}