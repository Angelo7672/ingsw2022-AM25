package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.model.exception.NotAllowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Special10Test {
    int numberOfPlayer=3;
    boolean expertMode = true;
    private Bag bag = new Bag();
    private List<Integer> bagRestore;
    private IslandsManager islandsManager;
    private CloudsManager cloudsManager;
    private PlayerManager playerManager;
    private QueueManager queueManager;
    int entranceStudent0;
    int entranceStudent1;

   @BeforeEach
    void initialization(){
        islandsManager = new IslandsManager();
        islandsManager.islandListener = islandToDelete -> {};
        islandsManager.towersListener = new TowersListener() {
            @Override
            public void notifyTowersChange(int place, int componentRef, int towersNumber) {}

            @Override
            public void notifyTowerColor(int islandRef, int newColor) {}
        };
        islandsManager.motherPositionListener = newMotherPosition -> {};
        islandsManager.inhibitedListener = (islandRef, isInhibited) -> {};
        islandsManager.studentListener = (place, componentRef, color, newStudentsValue) -> {};
        islandsManager.islandsInitialize();

        bagRestore = new ArrayList<>();
        bag = new Bag();
        bag.bagListener = new BagListener() {
            @Override
            public void notifyBagExtraction() {}
            @Override
            public void notifyBag(List<Integer> bag) {
                bagRestore = bag;
            }
        };
        bag.bagInitialize();

        cloudsManager = new CloudsManager(numberOfPlayer, bag);
        cloudsManager.studentsListener = (place, componentRef, color, newStudentsValue) -> {};


        playerManager = new PlayerManager(numberOfPlayer, bag);
        playerManager.towersListener = new TowersListener() {
            @Override
            public void notifyTowersChange(int place, int componentRef, int towersNumber) {}
            @Override
            public void notifyTowerColor(int islandRef, int newColor) {}
        };
        playerManager.professorsListener = (playerRef, color, newProfessorValue) -> {};
        playerManager.coinsListener = (playerRef, newCoinsValue) -> {
        };
        playerManager.playedCardListener = new PlayedCardListener() {
            @Override
            public void notifyPlayedCard(int playerRef, String assistantCard) {}
            @Override
            public void notifyHand(int playerRef, ArrayList<String> hand) {
            }
        };
        playerManager.studentsListener = (place, componentRef, color, newStudentsValue) -> {
            if(place==0 && componentRef == 0 & color==0) entranceStudent0 = newStudentsValue;
            else if(place==0 && componentRef == 0 & color==1) entranceStudent1 = newStudentsValue;
        };

        queueManager = new QueueManager(numberOfPlayer, playerManager);
        queueManager.queueListener = new QueueListener() {
            @Override
            public void notifyQueue(int queueRef, int playerRef) {}
            @Override
            public void notifyValueCard(int queueRef, int valueCard) {}
            @Override
            public void notifyMaxMove(int queueRef, int maxMove) {}
        };
        queueManager.playedCardListener = new PlayedCardListener() {
            @Override
            public void notifyPlayedCard(int playerRef, String assistantCard) {}
            @Override
            public void notifyHand(int playerRef, ArrayList<String> hand) {}
        };

    }

    @Test
    @DisplayName("Test if RoundSpecial10's effect is correct")
    void effectRoundSpecial10Test() throws NotAllowedException {
        RoundStrategy round = new RoundSpecial10(numberOfPlayer, cloudsManager, islandsManager, playerManager,queueManager, bag);
        ArrayList<Integer> color1 = new ArrayList<>();
        ArrayList<Integer> color2 = new ArrayList<>();
        color1.add(0);
        color2.add(1);
        boolean done = round.effect(0, color1,color2);
        assertFalse(done);
        playerManager.setStudentEntrance(0,0, 1);
        playerManager.setStudentEntrance(0,1, 1);
        playerManager.transferStudent(0,1,true,false);
        int tempEntranceStudent0 = entranceStudent0;
        int tempEntranceStudent1 = entranceStudent1;
        int tableStudent0 = playerManager.getStudentTable(0,0);
        int tableStudent1 = playerManager.getStudentTable(0,1);
        done = round.effect(0, color1, color2);
        assertEquals(tableStudent0+1,playerManager.getStudentTable(0,0));
        assertEquals(tableStudent1-1, playerManager.getStudentTable(0,1));
        assertEquals(tempEntranceStudent0-1,entranceStudent0);
        assertEquals(tempEntranceStudent1+1,entranceStudent1);
        assertTrue(done);
    }
}
