package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class Special7Test implements SpecialStudentsListener {

    int numberOfPlayer=3;
    boolean expertMode = true;
    private Bag bag = new Bag();
    private List<Integer> bagRestore;
    private IslandsManager islandsManager;
    private CloudsManager cloudsManager;
    private PlayerManager playerManager;
    private QueueManager queueManager;
    ArrayList<Integer> specialStudents;
    int entranceStudent0;
    int entranceStudent1;
    int cardStudent0;
    int cardStudent1;

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
            if(place==0 && color==0 && componentRef==0) entranceStudent0 = newStudentsValue;
            else if(place==0 && color==1 && componentRef==0) entranceStudent1 = newStudentsValue;
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
        if(special==7 && color==0) cardStudent0 = value;
        else if(special==7 && color==1) cardStudent1 = value;
    }

    @Test
    @DisplayName("Test if RoundSpecial7's effect is correct")
    void effectRoundSpecial7Test() {
        RoundStrategy round = new RoundSpecial7(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        round.setSpecialStudentsListener(this);
        ArrayList<Integer> color1 = new ArrayList<>();
        ArrayList<Integer> color2 = new ArrayList<>();
        color1.add(0);
        color2.add(1);
        boolean done = round.effect(0, color1,color2);
        assertFalse(done);
        playerManager.setStudentEntrance(0,0, 1);
        round.initializeSpecial();
        int tempCardStudent0 = cardStudent0;
        int tempCardStudent1 = cardStudent1;
        int tempEntranceStudent0 = entranceStudent0;
        int tempEntranceStudent1 = entranceStudent1;
        done = round.effect(0, color1,color2);
        if(done){
            assertEquals(tempCardStudent0+1,cardStudent0);
            assertEquals(tempCardStudent1-1, cardStudent1);
            assertEquals(tempEntranceStudent0-1,entranceStudent0);
            assertEquals(tempEntranceStudent1+1,entranceStudent1);
        }
        else{
            assertEquals(tempCardStudent0, cardStudent0);
            assertEquals(tempCardStudent1, cardStudent1);
            assertEquals(tempEntranceStudent0, entranceStudent0);
            assertEquals(tempEntranceStudent1, entranceStudent1);
        }

    }
}
