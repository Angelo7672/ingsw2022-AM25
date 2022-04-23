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

        playerManager.queueForPlanificationPhase(numberOfPlayer);
        //giocare carte
        //Fai test aggressivi

    }

}