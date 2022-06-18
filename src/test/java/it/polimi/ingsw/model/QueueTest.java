package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class QueueTest {
    PlayerManager playerManager2P, playerManager3P, playerManager4P;
    Bag bag;
    QueueManager queueManager2P,queueManager3P,queueManager4P;

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
            playerManager.professorsListener = new ProfessorsListener() {
                @Override
                public void notifyProfessors(int playerRef, int color, boolean newProfessorValue) {}
            };
            playerManager.coinsListener = new CoinsListener() {
                @Override
                public void notifyNewCoinsValue(int playerRef, int newCoinsValue) {}
            };
            playerManager.playedCardListener = new PlayedCardListener() {
                @Override
                public void notifyPlayedCard(int playerRef, String assistantCard) {}
                @Override
                public void notifyHand(int playerRef, ArrayList<String> hand) {}
            };
            playerManager.studentsListener = new StudentsListener() {
                @Override
                public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {}
            };
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

    /*@Test
    @DisplayName("First test: check queue initialization for first Planing Phase")
    void queueInit(){
        int numberOfPlayer = 4;
        String[] playerInfo = {"Giorgio","SAMURAI","Marco","KING","Dino","WIZARD","Gloria","WITCH"};
        PlayerManager playerManager = new PlayerManager(numberOfPlayer,playerInfo);

        assertTrue(playerManager.readQueue(0) >= 0 && playerManager.readQueue(0) <=4,"The first player must have index of one of the four players");

        playerManager.queueForPlanificationPhase(numberOfPlayer);
        for(int i = 0; i < numberOfPlayer; i++){    //i = 3 implies that the queue is finished
            if(playerManager.readQueue(i) != 3 && i != 3) assertEquals(playerManager.readQueue(i) + 1, playerManager.readQueue(i+1),"We are turning clockwise with the players arranged at the table in the order 0 1 2 3");
            else if(playerManager.readQueue(i) == 3 && i != 3) assertEquals(0, playerManager.readQueue(i+1),"We are turning clockwise with the players arranged at the table in the order 0 1 2 3");
        }
    }

    @Test
    @DisplayName("Second test: check queue for Action Phase")
    void actionPhase(){
        int numberOfPlayer = 4;
        String[] playerInfo = {"Giorgio","SAMURAI","Marco","KING","Dino","WIZARD","Gloria","WITCH"};
        PlayerManager playerManager = new PlayerManager(numberOfPlayer,playerInfo);
        boolean win;
        int i;

        playerManager.queueForPlanificationPhase(numberOfPlayer);
        //Each player knows the card to play, but we don't know the queue order in advance for the first planning step. For this reason the for loop and the if
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                win = playerManager.playCard(0, i, Assistant.DOG);   //First Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 1){
                win = playerManager.playCard(1, i, Assistant.EAGLE);   //Second Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 2){
                win = playerManager.playCard(2, i, Assistant.ELEPHANT);   //Third Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 3){
                win = playerManager.playCard(3, i, Assistant.FOX);   //Fourth Player
                assertFalse(win, "Nobody won this round");
            }
        }

        playerManager.inOrderForActionPhase();

        assertAll(
                ()->assertEquals(1,playerManager.readQueue(0),"According to our simulation, the order of play must be 1 3 0 2"),
                ()->assertEquals(3,playerManager.readQueue(1),"According to our simulation, the order of play must be 1 3 0 2"),
                ()->assertEquals(0,playerManager.readQueue(2),"According to our simulation, the order of play must be 1 3 0 2"),
                ()->assertEquals(2,playerManager.readQueue(3),"According to our simulation, the order of play must be 1 3 0 2")
        );
        assertAll(
                ()->assertEquals(2,playerManager.readMaxMotherNatureMovement(0),"The EAGLE card has been played -> 2"),
                ()->assertEquals(3,playerManager.readMaxMotherNatureMovement(1),"The FOX card has been played -> 3"),
                ()->assertEquals(4,playerManager.readMaxMotherNatureMovement(2),"The DOG card has been played -> 4"),
                ()->assertEquals(5,playerManager.readMaxMotherNatureMovement(3),"The ELEPHANT card has been played -> 5")
        );
    }

    @Test
    @DisplayName("Third test: testing of some planning phases")
    void aLargeNumberOfActionPhase(){
        int numberOfPlayer = 4;
        String[] playerInfo = {"Giorgio","SAMURAI","Marco","KING","Dino","WIZARD","Gloria","WITCH"};
        PlayerManager playerManager = new PlayerManager(numberOfPlayer,playerInfo);
        Boolean win;
        Team winner;
        int i;
        int[] studentsGiorgio = new int[]{0,0,1,1,2,2,3,3,4,4};

        for(i = 0; i < 10; i++) playerManager.setStudentEntrance(0,studentsGiorgio[i]);
        for(i = 0; i < 10; i++) playerManager.transferStudent(0,studentsGiorgio[i],true,false);

        //First hand
        playerManager.queueForPlanificationPhase(numberOfPlayer);
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                win = playerManager.playCard(0, i, Assistant.LION);   //First Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 1){
                win = playerManager.playCard(1, i, Assistant.FOX);   //Second Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 2){
                win = playerManager.playCard(2, i, Assistant.CAT);   //Third Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 3){
                win = playerManager.playCard(3, i, Assistant.OCTOPUS);   //Fourth Player
                assertFalse(win, "Nobody won this round");
            }
        }
        playerManager.inOrderForActionPhase();
        assertAll(
                ()->assertEquals(0,playerManager.readQueue(0),"According to our simulation, the order of play must be 0 2 1 3"),
                ()->assertEquals(2,playerManager.readQueue(1),"According to our simulation, the order of play must be 0 2 1 3"),
                ()->assertEquals(1,playerManager.readQueue(2),"According to our simulation, the order of play must be 0 2 1 3"),
                ()->assertEquals(3,playerManager.readQueue(3),"According to our simulation, the order of play must be 0 2 1 3")
        );
        //Second hand
        playerManager.queueForPlanificationPhase(numberOfPlayer);
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                win = playerManager.playCard(0, i, Assistant.EAGLE);   //First Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 1){
                win = playerManager.playCard(1, i, Assistant.CAT);   //Second Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 2){
                win = playerManager.playCard(2, i, Assistant.OCTOPUS);   //Third Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 3){
                win = playerManager.playCard(3, i, Assistant.LIZARD);   //Fourth Player
                assertFalse(win, "Nobody won this round");
            }
        }
        playerManager.inOrderForActionPhase();
        assertAll(
                ()->assertEquals(1,playerManager.readQueue(0),"According to our simulation, the order of play must be 1 0 3 2"),
                ()->assertEquals(0,playerManager.readQueue(1),"According to our simulation, the order of play must be 1 0 3 2"),
                ()->assertEquals(3,playerManager.readQueue(2),"According to our simulation, the order of play must be 1 0 3 2"),
                ()->assertEquals(2,playerManager.readQueue(3),"According to our simulation, the order of play must be 1 0 3 2")
        );
        //Third hand
        playerManager.queueForPlanificationPhase(numberOfPlayer);
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                win = playerManager.playCard(0, i, Assistant.FOX);   //First Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 1){
                win = playerManager.playCard(1, i, Assistant.DOG);   //Second Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 2){
                win = playerManager.playCard(2, i, Assistant.LIZARD);   //Third Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 3){
                win = playerManager.playCard(3, i, Assistant.TURTLE);   //Fourth Player
                assertFalse(win, "Nobody won this round");
            }
        }
        playerManager.inOrderForActionPhase();
        assertAll(
                ()->assertEquals(0,playerManager.readQueue(0),"According to our simulation, the order of play must be 0 2 1 3"),
                ()->assertEquals(2,playerManager.readQueue(1),"According to our simulation, the order of play must be 0 2 1 3"),
                ()->assertEquals(1,playerManager.readQueue(2),"According to our simulation, the order of play must be 0 2 1 3"),
                ()->assertEquals(3,playerManager.readQueue(3),"According to our simulation, the order of play must be 0 2 1 3")
        );
        //Fourth hand
        playerManager.queueForPlanificationPhase(numberOfPlayer);
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                win = playerManager.playCard(0, i, Assistant.ELEPHANT);   //First Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 1){
                win = playerManager.playCard(1, i, Assistant.LIZARD);   //Second Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 2){
                win = playerManager.playCard(2, i, Assistant.DOG);   //Third Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 3){
                win = playerManager.playCard(3, i, Assistant.LION);   //Fourth Player
                assertFalse(win, "Nobody won this round");
            }
        }
        playerManager.inOrderForActionPhase();
        assertAll(
                ()->assertEquals(3,playerManager.readQueue(0),"According to our simulation, the order of play must be 3 1 2 0"),
                ()->assertEquals(1,playerManager.readQueue(1),"According to our simulation, the order of play must be 3 1 2 0"),
                ()->assertEquals(2,playerManager.readQueue(2),"According to our simulation, the order of play must be 3 1 2 0"),
                ()->assertEquals(0,playerManager.readQueue(3),"According to our simulation, the order of play must be 3 1 2 0")
        );
        //Fifth hand
        playerManager.queueForPlanificationPhase(numberOfPlayer);
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                win = playerManager.playCard(0, i, Assistant.CAT);   //First Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 1){
                win = playerManager.playCard(1, i, Assistant.EAGLE);   //Second Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 2){
                win = playerManager.playCard(2, i, Assistant.TURTLE);   //Third Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 3){
                win = playerManager.playCard(3, i, Assistant.FOX);   //Fourth Player
                assertFalse(win, "Nobody won this round");
            }
        }
        playerManager.inOrderForActionPhase();
        assertAll(
                ()->assertEquals(0,playerManager.readQueue(0),"According to our simulation, the order of play must be 0 1 3 2"),
                ()->assertEquals(1,playerManager.readQueue(1),"According to our simulation, the order of play must be 0 1 3 2"),
                ()->assertEquals(3,playerManager.readQueue(2),"According to our simulation, the order of play must be 0 1 3 2"),
                ()->assertEquals(2,playerManager.readQueue(3),"According to our simulation, the order of play must be 0 1 3 2")
        );
        //Sixth hand
        playerManager.queueForPlanificationPhase(numberOfPlayer);
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                win = playerManager.playCard(0, i, Assistant.LIZARD);   //First Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 1){
                win = playerManager.playCard(1, i, Assistant.LION);   //Second Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 2){
                win= playerManager.playCard(2, i, Assistant.FOX);   //Third Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 3){
                win = playerManager.playCard(3, i, Assistant.CAT);   //Fourth Player
                assertFalse(win, "Nobody won this round");
            }
        }
        playerManager.inOrderForActionPhase();
        assertAll(
                ()->assertEquals(1,playerManager.readQueue(0),"According to our simulation, the order of play must be 1 3 2 0"),
                ()->assertEquals(3,playerManager.readQueue(1),"According to our simulation, the order of play must be 1 3 2 0"),
                ()->assertEquals(2,playerManager.readQueue(2),"According to our simulation, the order of play must be 1 3 2 0"),
                ()->assertEquals(0,playerManager.readQueue(3),"According to our simulation, the order of play must be 1 3 2 0")
        );
        //Seventh hand
        playerManager.queueForPlanificationPhase(numberOfPlayer);
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                win = playerManager.playCard(0, i, Assistant.DOG);   //First Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 1){
                win = playerManager.playCard(1, i, Assistant.OCTOPUS);   //Second Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 2){
                win = playerManager.playCard(2, i, Assistant.LION);   //Third Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 3){
                win = playerManager.playCard(3, i, Assistant.EAGLE);   //Fourth Player
                assertFalse(win, "Nobody won this round");
            }
        }
        playerManager.inOrderForActionPhase();
        assertAll(
                ()->assertEquals(2,playerManager.readQueue(0),"According to our simulation, the order of play must be 2 3 1 0"),
                ()->assertEquals(3,playerManager.readQueue(1),"According to our simulation, the order of play must be 2 3 1 0"),
                ()->assertEquals(1,playerManager.readQueue(2),"According to our simulation, the order of play must be 2 3 1 0"),
                ()->assertEquals(0,playerManager.readQueue(3),"According to our simulation, the order of play must be 2 3 1 0")
        );
        //Eighth hand
        playerManager.queueForPlanificationPhase(numberOfPlayer);
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                win = playerManager.playCard(0, i, Assistant.GOOSE);   //First Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 1){
                win = playerManager.playCard(1, i, Assistant.ELEPHANT);   //Second Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 2){
                win = playerManager.playCard(2, i, Assistant.EAGLE);   //Third Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 3){
                win = playerManager.playCard(3, i, Assistant.DOG);   //Fourth Player
                assertFalse(win, "Nobody won this round");
            }
        }
        playerManager.inOrderForActionPhase();
        assertAll(
                ()->assertEquals(0,playerManager.readQueue(0),"According to our simulation, the order of play must be 0 2 3 1"),
                ()->assertEquals(2,playerManager.readQueue(1),"According to our simulation, the order of play must be 0 2 3 1"),
                ()->assertEquals(3,playerManager.readQueue(2),"According to our simulation, the order of play must be 0 2 3 1"),
                ()->assertEquals(1,playerManager.readQueue(3),"According to our simulation, the order of play must be 0 2 3 1")
        );
        //Nineth hand
        playerManager.queueForPlanificationPhase(numberOfPlayer);
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                win = playerManager.playCard(0, i, Assistant.TURTLE);   //First Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 1){
                win= playerManager.playCard(1, i, Assistant.GOOSE);   //Second Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 2){
                win = playerManager.playCard(2, i, Assistant.ELEPHANT);   //Third Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 3){
                win = playerManager.playCard(3, i, Assistant.GOOSE);   //Fourth Player
                assertFalse(win, "Nobody won this round");
            }
        }
        playerManager.inOrderForActionPhase();
        assertAll(
                ()->assertEquals(1,playerManager.readQueue(0),"According to our simulation, the order of play must be 1 3 2 0"),
                ()->assertEquals(3,playerManager.readQueue(1),"According to our simulation, the order of play must be 1 3 2 0"),
                ()->assertEquals(2,playerManager.readQueue(2),"According to our simulation, the order of play must be 1 3 2 0"),
                ()->assertEquals(0,playerManager.readQueue(3),"According to our simulation, the order of play must be 1 3 2 0")
        );
        //Tenth hand
        playerManager.queueForPlanificationPhase(numberOfPlayer);
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                win = playerManager.playCard(0, i, Assistant.OCTOPUS);   //First Player
                assertTrue(win, "Cards over");
            }else if(playerManager.readQueue(i) == 1){
                playerManager.playCard(1, i, Assistant.TURTLE);   //Second Player
            }else if(playerManager.readQueue(i) == 2){
                playerManager.playCard(2, i, Assistant.GOOSE);   //Third Player
            }else if(playerManager.readQueue(i) == 3){
                playerManager.playCard(3, i, Assistant.ELEPHANT);   //Fourth Player
            }
        }
        playerManager.inOrderForActionPhase();
        assertAll(
                ()->assertEquals(2,playerManager.readQueue(0),"According to our simulation, the order of play must be 2 0 3 1"),
                ()->assertEquals(0,playerManager.readQueue(1),"According to our simulation, the order of play must be 2 0 3 1"),
                ()->assertEquals(3,playerManager.readQueue(2),"According to our simulation, the order of play must be 2 0 3 1"),
                ()->assertEquals(1,playerManager.readQueue(3),"According to our simulation, the order of play must be 2 0 3 1")
        );
        //GAME OVER, Cards over
        assertTrue(playerManager.checkVictory().equals(Team.WHITE),"Giorgio is the player with the most influence and belongs to Team WHITE");
    }

   @Test
   @DisplayName("Fourth test: extreme cases")
   void extremeCases(){
        int numberOfPlayer = 4;
        String[] playerInfo = {"Giorgio","SAMURAI","Marco","KING","Dino","WIZARD","Gloria","WITCH"};
        PlayerManager playerManager = new PlayerManager(numberOfPlayer,playerInfo);
        Boolean win;
        int i;

        //This first part serves, based on the cards that will be played, just to know who will start the next planning phase
        playerManager.queueForPlanificationPhase(numberOfPlayer);
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                win = playerManager.playCard(0, i, Assistant.LION);   //First Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 1){
                win = playerManager.playCard(1, i, Assistant.FOX);   //Second Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 2){
                win = playerManager.playCard(2, i, Assistant.CAT);   //Third Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 3){
                win = playerManager.playCard(3, i, Assistant.OCTOPUS);   //Fourth Player
                assertFalse(win, "Nobody won this round");
            }
        }
        playerManager.inOrderForActionPhase();

        //Player 0 starts; the previous step was for this
        //Order of play: 0 1 2 3
        playerManager.queueForPlanificationPhase(numberOfPlayer);
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                win = playerManager.playCard(0, i, Assistant.TURTLE);   //First Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 1){
                win = playerManager.playCard(1, i, Assistant.LION);   //Second Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 2){
                win = playerManager.playCard(2, i, Assistant.TURTLE);   //Third Player, card already played by the first player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 3){
                win = playerManager.playCard(3, i, Assistant.LION);   //Fourth Player, card already played by the second player
                assertFalse(win, "Nobody won this round");
            }
        }
        playerManager.inOrderForActionPhase();
        assertAll(
                ()->assertEquals(1,playerManager.readQueue(0),"The second player played LION"),
                ()->assertEquals(3,playerManager.readQueue(1),"The fourth player played LION"),
                ()->assertEquals(0,playerManager.readQueue(2),"The first player played TURTLE"),
                ()->assertEquals(2,playerManager.readQueue(3),"The third player played TURTLE")
        );
        //Player 1 starts (previous planning phase); order of play: 1 2 3 0
        playerManager.queueForPlanificationPhase(numberOfPlayer);
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                win = playerManager.playCard(0, i, Assistant.DOG);   //First Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 1){
                win = playerManager.playCard(1, i, Assistant.DOG);   //Second Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 2){
                win = playerManager.playCard(2, i, Assistant.DOG);   //Third Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 3){
                win = playerManager.playCard(3, i, Assistant.DOG);   //Fourth Player
                assertFalse(win, "Nobody won this round");
            }
        }
        playerManager.inOrderForActionPhase();
        assertAll(
               ()->assertEquals(1,playerManager.readQueue(0),"All players have played DOG, the order 1 2 3 0"),
               ()->assertEquals(2,playerManager.readQueue(1),"All players have played DOG, the order 1 2 3 0"),
               ()->assertEquals(3,playerManager.readQueue(2),"All players have played DOG, the order 1 2 3 0"),
               ()->assertEquals(0,playerManager.readQueue(3),"All players have played DOG, the order 1 2 3 0")
        );
        //Player 1 starts (previous planning phase); order of play: 1 2 3 0
        playerManager.queueForPlanificationPhase(numberOfPlayer);
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                win = playerManager.playCard(0, i, Assistant.CAT);   //First Player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 1){
                win = playerManager.playCard(1, i, Assistant.EAGLE);   //Second Player, card already played by the second player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 2){
                win = playerManager.playCard(2, i, Assistant.EAGLE);   //Third Player, card already played by the second player
                assertFalse(win, "Nobody won this round");
            }else if(playerManager.readQueue(i) == 3){
                win = playerManager.playCard(3, i, Assistant.EAGLE);   //Fourth Player, card already played by the third player
                assertFalse(win, "Nobody won this round");
            }
        }
        playerManager.inOrderForActionPhase();
        assertAll(
                ()->assertEquals(0,playerManager.readQueue(0),"The first player played CAT"),
                ()->assertEquals(1,playerManager.readQueue(1),"The second player played EAGLE"),
                ()->assertEquals(2,playerManager.readQueue(2),"The third player played EAGLE"),
                ()->assertEquals(3,playerManager.readQueue(3),"The fourth player played EAGLE")
        );
    }*/
}