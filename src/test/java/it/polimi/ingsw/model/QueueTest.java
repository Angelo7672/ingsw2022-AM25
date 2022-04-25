package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QueueTest {
    @Test
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
        Team winner;
        int i;

        playerManager.queueForPlanificationPhase(numberOfPlayer);
        //Each player knows the card to play, but we don't know the queue order in advance for the first planning step. For this reason the for loop and the if
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                winner = playerManager.playCard(0, i, Assistant.DOG);   //First Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
            }else if(playerManager.readQueue(i) == 1){
                winner = playerManager.playCard(1, i, Assistant.EAGLE);   //Second Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
            }else if(playerManager.readQueue(i) == 2){
                winner = playerManager.playCard(2, i, Assistant.ELEPHANT);   //Third Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
            }else if(playerManager.readQueue(i) == 3){
                winner = playerManager.playCard(3, i, Assistant.FOX);   //Fourth Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
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
        Team winner;
        int i;
        int[] studentsGiorgio = new int[]{0,0,1,1,2,2,3,3,4,4};

        for(i = 0; i < 10; i++) playerManager.setStudentEntrance(0,studentsGiorgio[i]);
        for(i = 0; i < 10; i++) playerManager.transferStudent(0,studentsGiorgio[i],true,false);

        //First hand
        playerManager.queueForPlanificationPhase(numberOfPlayer);
        for(i = 0; i < numberOfPlayer; i++) {
            if(playerManager.readQueue(i) == 0){
                winner = playerManager.playCard(0, i, Assistant.LION);   //First Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 1){
                winner = playerManager.playCard(1, i, Assistant.FOX);   //Second Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 2){
                winner = playerManager.playCard(2, i, Assistant.CAT);   //Third Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 3){
                winner = playerManager.playCard(3, i, Assistant.OCTOPUS);   //Fourth Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
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
                winner = playerManager.playCard(0, i, Assistant.EAGLE);   //First Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 1){
                winner = playerManager.playCard(1, i, Assistant.CAT);   //Second Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 2){
                winner = playerManager.playCard(2, i, Assistant.OCTOPUS);   //Third Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 3){
                winner = playerManager.playCard(3, i, Assistant.LIZARD);   //Fourth Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
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
                winner = playerManager.playCard(0, i, Assistant.FOX);   //First Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 1){
                winner = playerManager.playCard(1, i, Assistant.DOG);   //Second Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 2){
                winner = playerManager.playCard(2, i, Assistant.LIZARD);   //Third Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 3){
                winner = playerManager.playCard(3, i, Assistant.TURTLE);   //Fourth Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
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
                winner = playerManager.playCard(0, i, Assistant.ELEPHANT);   //First Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 1){
                winner = playerManager.playCard(1, i, Assistant.LIZARD);   //Second Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 2){
                winner = playerManager.playCard(2, i, Assistant.DOG);   //Third Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 3){
                winner = playerManager.playCard(3, i, Assistant.LION);   //Fourth Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
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
                winner = playerManager.playCard(0, i, Assistant.CAT);   //First Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 1){
                winner = playerManager.playCard(1, i, Assistant.EAGLE);   //Second Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 2){
                winner = playerManager.playCard(2, i, Assistant.TURTLE);   //Third Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 3){
                winner = playerManager.playCard(3, i, Assistant.FOX);   //Fourth Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
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
                winner = playerManager.playCard(0, i, Assistant.LIZARD);   //First Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 1){
                winner = playerManager.playCard(1, i, Assistant.LION);   //Second Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 2){
                winner = playerManager.playCard(2, i, Assistant.FOX);   //Third Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 3){
                winner = playerManager.playCard(3, i, Assistant.CAT);   //Fourth Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
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
                winner = playerManager.playCard(0, i, Assistant.DOG);   //First Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 1){
                winner = playerManager.playCard(1, i, Assistant.OCTOPUS);   //Second Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 2){
                winner = playerManager.playCard(2, i, Assistant.LION);   //Third Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 3){
                winner = playerManager.playCard(3, i, Assistant.EAGLE);   //Fourth Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
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
                winner = playerManager.playCard(0, i, Assistant.GOOSE);   //First Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 1){
                winner = playerManager.playCard(1, i, Assistant.ELEPHANT);   //Second Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 2){
                winner = playerManager.playCard(2, i, Assistant.EAGLE);   //Third Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 3){
                winner = playerManager.playCard(3, i, Assistant.DOG);   //Fourth Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
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
                winner = playerManager.playCard(0, i, Assistant.TURTLE);   //First Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 1){
                winner = playerManager.playCard(1, i, Assistant.GOOSE);   //Second Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 2){
                winner = playerManager.playCard(2, i, Assistant.ELEPHANT);   //Third Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 3){
                winner = playerManager.playCard(3, i, Assistant.GOOSE);   //Fourth Player
                assertTrue(winner.equals(Team.NOONE), "Nobody won this round");
                System.out.println(playerManager.getHand(0));
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
                winner = playerManager.playCard(0, i, Assistant.OCTOPUS);   //First Player
                //assertTrue(winner.equals(Team.WHITE), "WHITE won this round");
                assertTrue(playerManager.checkIfCardsFinished(0));
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 1){
                playerManager.playCard(1, i, Assistant.TURTLE);   //Second Player
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 2){
                playerManager.playCard(2, i, Assistant.GOOSE);   //Third Player
                System.out.println(playerManager.getHand(0));
            }else if(playerManager.readQueue(i) == 3){
                playerManager.playCard(3, i, Assistant.ELEPHANT);   //Fourth Player
                System.out.println(playerManager.getHand(0));
            }
        }
        System.out.println(playerManager.checkVictory());
        playerManager.inOrderForActionPhase();
        assertAll(
                ()->assertEquals(2,playerManager.readQueue(0),"According to our simulation, the order of play must be 2 0 3 1"),
                ()->assertEquals(0,playerManager.readQueue(1),"According to our simulation, the order of play must be 2 0 3 1"),
                ()->assertEquals(3,playerManager.readQueue(2),"According to our simulation, the order of play must be 2 0 3 1"),
                ()->assertEquals(1,playerManager.readQueue(3),"According to our simulation, the order of play must be 2 0 3 1")
        );
    }

}