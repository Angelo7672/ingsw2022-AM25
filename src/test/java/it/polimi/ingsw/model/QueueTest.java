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

}