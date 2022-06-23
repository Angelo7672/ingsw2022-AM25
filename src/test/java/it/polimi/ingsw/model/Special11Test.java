package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.model.exception.NotAllowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Special11Test implements SpecialStudentsListener {
    int numberOfPlayer=3;
    boolean expertMode = true;
    private Bag bag = new Bag();
    private List<Integer> bagRestore;
    private IslandsManager islandsManager;
    private CloudsManager cloudsManager;
    private PlayerManager playerManager;
    private QueueManager queueManager;
    int cardStudent;
    int studentTable;

    @BeforeEach
    void initialization(){
        islandsManager = new IslandsManager();
        islandsManager.islandListener = new IslandListener() {
            @Override
            public void notifyIslandChange(int islandToDelete) {}
        };
        islandsManager.towersListener = new TowersListener() {
            @Override
            public void notifyTowersChange(int place, int componentRef, int towersNumber) {}

            @Override
            public void notifyTowerColor(int islandRef, int newColor) {}
        };
        islandsManager.motherPositionListener = new MotherPositionListener() {
            @Override
            public void notifyMotherPosition(int newMotherPosition) {}
        };
        islandsManager.inhibitedListener = new InhibitedListener() {
            @Override
            public void notifyInhibited(int islandRef, int isInhibited) {}
        };
        islandsManager.studentListener = new StudentsListener() {
            @Override
            public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {}
        };
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
        cloudsManager.studentsListener = new StudentsListener() {
            @Override
            public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {}
        };


        playerManager = new PlayerManager(numberOfPlayer, bag);
        playerManager.towersListener = new TowersListener() {
            @Override
            public void notifyTowersChange(int place, int componentRef, int towersNumber) {}
            @Override
            public void notifyTowerColor(int islandRef, int newColor) {}
        };
        playerManager.professorsListener = new ProfessorsListener() {
            @Override
            public void notifyProfessors(int playerRef, int color, boolean newProfessorValue) {}
        };
        playerManager.coinsListener = new CoinsListener() {
            @Override
            public void notifyNewCoinsValue(int playerRef, int newCoinsValue) {
            }
        };
        playerManager.playedCardListener = new PlayedCardListener() {
            @Override
            public void notifyPlayedCard(int playerRef, String assistantCard) {}
            @Override
            public void notifyHand(int playerRef, ArrayList<String> hand) {
            }
        };
        playerManager.studentsListener = new StudentsListener() {
            @Override
            public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {
                if(place==1 && componentRef == 0 & color==0) studentTable = newStudentsValue;
            }
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

        playerManager.initializeSchool();

    }
    @Override
    public void specialStudentsNotify(int special, int color, int value) {
        if(special==11 && color==0) cardStudent = value;
    }

    @Test
    @DisplayName("Test if RoundSpecial11's effect is correct")
    void effectRoundSpecial11Test() throws NotAllowedException {
        RoundStrategy round = new RoundSpecial11(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        round.setSpecialStudentsListener(this);
        round.initializeSpecial();
        int color1 = 0;
        boolean done = false;
        playerManager.setStudentTable(0,0,1);
        if (cardStudent > 0) {
            int tempTableStudent = studentTable;
            done = round.effect(0, color1);
            assertEquals(true, done);
            assertEquals(tempTableStudent+1, playerManager.getStudentTable(0, 0));
        } else {
            done = round.effect(0, color1);
            assertEquals(false, done);
        }
    }
}
