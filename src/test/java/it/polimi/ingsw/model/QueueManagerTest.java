package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.model.exception.NotAllowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QueueManagerTest {
    PlayerManager playerManager2P, playerManager3P, playerManager4P;
    Bag bag;
    QueueManager queueManager2P,queueManager3P,queueManager4P;

    /**
     * Initialize QueueManager, PlayerManager and Bag with their listeners.
     */
    @BeforeEach
    void initialization() {
        bag = new Bag();
        bag.bagListener = new BagListener() {
            @Override
            public void notifyBagExtraction() {}
            @Override
            public void notifyBag(List<Integer> bag) {}
        };
        bag.bagInitialize();
        playerManager2P = new PlayerManager(2, bag);
        playerManager3P = new PlayerManager(3, bag);
        playerManager4P = new PlayerManager(4, bag);
        for (PlayerManager playerManager : Arrays.asList(playerManager2P, playerManager3P, playerManager4P)) {
            playerManager.towersListener = new TowersListener() {
                @Override
                public void notifyTowersChange(int place, int componentRef, int towersNumber) {}
                @Override
                public void notifyTowerColor(int islandRef, int newColor) {}
            };
            playerManager.professorsListener = (playerRef, color, newProfessorValue) -> {};
            playerManager.coinsListener = (playerRef, newCoinsValue) -> {};
            playerManager.playedCardListener = new PlayedCardListener() {
                @Override
                public void notifyPlayedCard(int playerRef, String assistantCard) {}
                @Override
                public void notifyHand(int playerRef, ArrayList<String> hand) {}
            };
            playerManager.studentsListener = (place, componentRef, color, newStudentsValue) -> {};
            playerManager.initializeSchool();
        }
        queueManager2P = new QueueManager(2, playerManager2P);
        queueManager3P = new QueueManager(3, playerManager3P);
        queueManager4P = new QueueManager(4, playerManager4P);
        for (QueueManager queueManager : Arrays.asList(queueManager2P, queueManager3P, queueManager4P)) {
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
    }

    /**
     * Test initialization of queue.
     */
    @Test
    @DisplayName("First test: check queue initialization for first Planing Phase")
    void queueInit(){
        assertTrue(queueManager4P.readQueue(0) >= 0 && queueManager4P.readQueue(0) <=4,"The first player must have index of one of the four players");

        queueManager4P.queueForPlanificationPhase();
        for(int i = 0; i < 4; i++){    //i = 3 implies that the queue is finished
            if(queueManager4P.readQueue(i) != 3 && i != 3) assertEquals(queueManager4P.readQueue(i) + 1, queueManager4P.readQueue(i+1),"We are turning clockwise with the players arranged at the table in the order 0 1 2 3");
            else if(queueManager4P.readQueue(i) == 3 && i != 3) assertEquals(0, queueManager4P.readQueue(i+1),"We are turning clockwise with the players arranged at the table in the order 0 1 2 3");
        }
    }

    /**
     * Check queue for action phase.
     */
    @Test
    @DisplayName("Second test: check queue for Action Phase")
    void actionPhase(){
        int numberOfPlayer = 4;
        boolean win;
        ArrayList<Assistant> alreadyPlayedCards = new ArrayList<>();

        try {
            queueManager4P.queueForPlanificationPhase();
            //Each player knows the card to play, but we don't know the queue order in advance for the first planning step. For this reason the for loop and the if
            for(int i = 0; i < numberOfPlayer; i++) {
                if(queueManager4P.readQueue(i) == 0){
                    win = queueManager4P.playCard(0, i, Assistant.DOG, alreadyPlayedCards);   //First Player
                    alreadyPlayedCards.add(Assistant.DOG);
                    assertFalse(win, "Nobody won this round");
                }else if(queueManager4P.readQueue(i) == 1){
                    win = queueManager4P.playCard(1, i, Assistant.EAGLE, alreadyPlayedCards);   //Second Player
                    alreadyPlayedCards.add(Assistant.EAGLE);
                    assertFalse(win, "Nobody won this round");
                }else if(queueManager4P.readQueue(i) == 2){
                    win = queueManager4P.playCard(2, i, Assistant.ELEPHANT, alreadyPlayedCards);   //Third Player
                    alreadyPlayedCards.add(Assistant.ELEPHANT);
                    assertFalse(win, "Nobody won this round");
                }else if(queueManager4P.readQueue(i) == 3){
                    win = queueManager4P.playCard(3, i, Assistant.FOX, alreadyPlayedCards);   //Fourth Player
                    alreadyPlayedCards.add(Assistant.FOX);
                    assertFalse(win, "Nobody won this round");
                }
            }

            queueManager4P.inOrderForActionPhase();

            assertAll(
                    ()->assertEquals(1,queueManager4P.readQueue(0),"According to our simulation, the order of play must be 1 3 0 2"),
                    ()->assertEquals(3,queueManager4P.readQueue(1),"According to our simulation, the order of play must be 1 3 0 2"),
                    ()->assertEquals(0,queueManager4P.readQueue(2),"According to our simulation, the order of play must be 1 3 0 2"),
                    ()->assertEquals(2,queueManager4P.readQueue(3),"According to our simulation, the order of play must be 1 3 0 2")
            );
            assertAll(
                    ()->assertEquals(2,queueManager4P.readMaxMotherNatureMovement(0),"The EAGLE card has been played -> 2"),
                    ()->assertEquals(3,queueManager4P.readMaxMotherNatureMovement(1),"The FOX card has been played -> 3"),
                    ()->assertEquals(4,queueManager4P.readMaxMotherNatureMovement(2),"The DOG card has been played -> 4"),
                    ()->assertEquals(5,queueManager4P.readMaxMotherNatureMovement(3),"The ELEPHANT card has been played -> 5")
            );
        }catch (NotAllowedException notAllowedException) { notAllowedException.printStackTrace(); }
    }

    /**
     * Test cases in which some players have to play an already played card.
     */
    @Test
    @DisplayName("Third test: extreme cases")
    void extremeCases(){
        int numberOfPlayer = 4;
        boolean win, checker = false;
        ArrayList<Assistant> alreadyPlayedCards = new ArrayList<>();
        ArrayList<Assistant> alreadyPlayedCards1 = new ArrayList<>();

        try {
            queueManager4P.queueForPlanificationPhase();
            for(int i = 0; i < numberOfPlayer; i++) {   //initialize hands of players for our test
                if(queueManager4P.readQueue(i) == 0){
                    queueManager4P.playCard(0, i, Assistant.FOX, alreadyPlayedCards);    //First Player
                    queueManager4P.playCard(0, i, Assistant.LIZARD, alreadyPlayedCards);
                    queueManager4P.playCard(0, i, Assistant.OCTOPUS, alreadyPlayedCards);
                    queueManager4P.playCard(0, i, Assistant.DOG, alreadyPlayedCards);
                    queueManager4P.playCard(0, i, Assistant.ELEPHANT, alreadyPlayedCards);
                    queueManager4P.playCard(0, i, Assistant.TURTLE, alreadyPlayedCards);
                }else if(queueManager4P.readQueue(i) == 1){
                    queueManager4P.playCard(1, i, Assistant.LION, alreadyPlayedCards);   //Second Player
                    queueManager4P.playCard(1, i, Assistant.CAT, alreadyPlayedCards);
                    queueManager4P.playCard(1, i, Assistant.LIZARD, alreadyPlayedCards);
                    queueManager4P.playCard(1, i, Assistant.OCTOPUS, alreadyPlayedCards);
                    queueManager4P.playCard(1, i, Assistant.DOG, alreadyPlayedCards);
                    queueManager4P.playCard(1, i, Assistant.ELEPHANT, alreadyPlayedCards);
                    queueManager4P.playCard(1, i, Assistant.TURTLE, alreadyPlayedCards);
                }else if(queueManager4P.readQueue(i) == 2){
                    queueManager4P.playCard(2, i, Assistant.LION, alreadyPlayedCards);   //Third Player
                    queueManager4P.playCard(2, i, Assistant.FOX, alreadyPlayedCards);
                    queueManager4P.playCard(2, i, Assistant.LIZARD, alreadyPlayedCards);
                    queueManager4P.playCard(2, i, Assistant.OCTOPUS, alreadyPlayedCards);
                    queueManager4P.playCard(2, i, Assistant.DOG, alreadyPlayedCards);
                    queueManager4P.playCard(2, i, Assistant.ELEPHANT, alreadyPlayedCards);
                    queueManager4P.playCard(2, i, Assistant.TURTLE, alreadyPlayedCards);
                }else if(queueManager4P.readQueue(i) == 3){
                    queueManager4P.playCard(3, i, Assistant.LION, alreadyPlayedCards);   //Fourth Player
                    queueManager4P.playCard(3, i, Assistant.CAT, alreadyPlayedCards);
                    queueManager4P.playCard(3, i, Assistant.FOX, alreadyPlayedCards);
                    queueManager4P.playCard(3, i, Assistant.LIZARD, alreadyPlayedCards);
                    queueManager4P.playCard(3, i, Assistant.DOG, alreadyPlayedCards);
                    queueManager4P.playCard(3, i, Assistant.ELEPHANT, alreadyPlayedCards);
                    queueManager4P.playCard(3, i, Assistant.TURTLE, alreadyPlayedCards);
                }
            }

            //This first part serves, based on the cards that will be played, just to know who will start the next planning phase
            for(int i = 0; i < numberOfPlayer; i++) {
                if(queueManager4P.readQueue(i) == 0){
                    win = queueManager4P.playCard(0, i, Assistant.LION, alreadyPlayedCards);   //First Player
                    assertFalse(win, "Nobody won this round");
                }else if(queueManager4P.readQueue(i) == 1){
                    win = queueManager4P.playCard(1, i, Assistant.FOX, alreadyPlayedCards);   //Second Player
                    assertFalse(win, "Nobody won this round");
                }else if(queueManager4P.readQueue(i) == 2){
                    win = queueManager4P.playCard(2, i, Assistant.CAT, alreadyPlayedCards);   //Third Player
                    assertFalse(win, "Nobody won this round");
                }else if(queueManager4P.readQueue(i) == 3){
                    win = queueManager4P.playCard(3, i, Assistant.OCTOPUS, alreadyPlayedCards);   //Fourth Player
                    assertFalse(win, "Nobody won this round");
                }
            }
            queueManager4P.inOrderForActionPhase();

            //Player 0 starts; the previous step was for this
            //Order of play: 0 1 2 3
            queueManager4P.queueForPlanificationPhase();
            for(int i = 0; i < numberOfPlayer; i++) {
                if(queueManager4P.readQueue(i) == 0){
                    win = queueManager4P.playCard(0, i, Assistant.EAGLE, alreadyPlayedCards);   //First Player
                    alreadyPlayedCards.add(Assistant.EAGLE);
                    assertFalse(win, "Nobody won this round");
                }else if(queueManager4P.readQueue(i) == 1){
                    win = queueManager4P.playCard(1, i, Assistant.GOOSE, alreadyPlayedCards);   //Second Player
                    alreadyPlayedCards.add(Assistant.GOOSE);
                    assertFalse(win, "Nobody won this round");
                }else if(queueManager4P.readQueue(i) == 2){
                    win = queueManager4P.playCard(2, i, Assistant.GOOSE, alreadyPlayedCards);   //Third Player, card already played by the first player
                    alreadyPlayedCards.add(Assistant.GOOSE);
                    assertFalse(win, "Nobody won this round");
                }else if(queueManager4P.readQueue(i) == 3){
                    win = queueManager4P.playCard(3, i, Assistant.EAGLE, alreadyPlayedCards);   //Fourth Player, card already played by the second player
                    alreadyPlayedCards.add(Assistant.EAGLE);
                    assertFalse(win, "Nobody won this round");
                }
            }
            queueManager4P.inOrderForActionPhase();
            assertAll(
                    ()->assertEquals(1,queueManager4P.readQueue(0),"The second player played GOOSE"),
                    ()->assertEquals(2,queueManager4P.readQueue(1),"The third player played GOOSE"),
                    ()->assertEquals(0,queueManager4P.readQueue(2),"The first player played EAGLE"),
                    ()->assertEquals(3,queueManager4P.readQueue(3),"The fourth player played EAGLE")
            );
        }catch (NotAllowedException notAllowedException){ notAllowedException.printStackTrace(); return; }

        //Player 1 starts (previous planning phase); order of play: 1 2 3 0
        try{
            queueManager4P.queueForPlanificationPhase();
            for(int i = 0; i < numberOfPlayer; i++) {
                if(queueManager4P.readQueue(i) == 0){
                    win = queueManager4P.playCard(0, i, Assistant.GOOSE, alreadyPlayedCards1);   //First Player
                    alreadyPlayedCards1.add(Assistant.GOOSE);
                    assertFalse(win, "First player has already a card");
                }else if(queueManager4P.readQueue(i) == 1){
                    win = queueManager4P.playCard(1, i, Assistant.EAGLE, alreadyPlayedCards1);   //Second Player
                    alreadyPlayedCards1.add(Assistant.EAGLE);
                    assertTrue(win, "Second's card finished");
                }else if(queueManager4P.readQueue(i) == 2){
                    win = queueManager4P.playCard(2, i, Assistant.EAGLE, alreadyPlayedCards1);   //Third Player
                    alreadyPlayedCards1.add(Assistant.EAGLE);
                    assertTrue(win, "Third's card finished");
                }else if(queueManager4P.readQueue(i) == 3){
                    win = queueManager4P.playCard(3, i, Assistant.GOOSE, alreadyPlayedCards1);   //Fourth Player
                    alreadyPlayedCards1.add(Assistant.GOOSE);
                    assertTrue(win, "Fourth's card finished");
                }
            }
        }catch (NotAllowedException notAllowedException){ checker = true; }
        assertTrue(checker, "The first player had a different card from those already played to play");
     }
}