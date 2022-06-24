package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.exception.NotAllowedException;
import it.polimi.ingsw.server.ControllerServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VirtualViewTest {
    VirtualView virtualView;
    Controller controller;
    Controller controller1;
    String fileName = "saveGameTest.bin";
    FileOutputStream outputFile;
    ObjectOutputStream objectOut;
    ObjectInputStream inputFile;

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
        controller = new Controller(2, false, controllerServer, fileName);
        //virtualView = new VirtualView(2, true, controllerServer, controller, fileName);
        controller.clearFile();
        controller.addNewPlayer("Angelo","WIZARD");
        controller.addNewPlayer("Ginevra","WITCH");
        controller.createGame();
        controller.initializeGame();
        controller1 = new Controller(2, true, controllerServer, fileName);

        try {
            outputFile = new FileOutputStream(fileName);
            objectOut = new ObjectOutputStream(outputFile);
            inputFile = new ObjectInputStream(new FileInputStream(fileName));
        } catch (FileNotFoundException e) { System.out.println("Error, retry"); return;
        } catch (IOException e) { System.out.println("Error, retry"); return; }

    }

    public void clearFile() {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                raf.setLength(0);
            }
        } catch (FileNotFoundException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void saveGame(){
        clearFile();
        controller.initializeGame();
        controller.startGame();

    }

   @Test
    @DisplayName("Test save and restore game")
    void saveAndRestore(){
        controller.startGame();
        controller.resumeTurn(0);
        controller.saveGame();
        controller1.createGame();
        controller1.restoreVirtualView();
        controller1.restoreGame();
        controller1.saveGame();
    }



}