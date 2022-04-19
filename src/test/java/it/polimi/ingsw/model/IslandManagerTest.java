package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;


import static it.polimi.ingsw.model.Team.*;
import static org.junit.jupiter.api.Assertions.*;

public class IslandManagerTest {
    Round round;
    int[] color={0,0,1,1,2,2,3,3,4,4,5,5};

    @Test
    @DisplayName("Test if the island of Mother Nature and the one in front are empty")
    void setupIslandMotherTest() {
        IslandsManager islandsManager = new IslandsManager(color);
        assertEquals(0, islandsManager.getStudent(islandsManager.getMotherPos(), 0));
        assertEquals(0, islandsManager.getStudent(islandsManager.circularArray(islandsManager.getMotherPos(), 6), 0));
    }

    @Test
    @DisplayName("Test if islands are filled correctly")
    void setupIslandTest() {
        int[] color={0,0,0,0,0,0,0,0,0,0};
        IslandsManager islandsManager = new IslandsManager(color);
        for(int i=0; i<12; i++){
            if(i!= islandsManager.getMotherPos()&&i!=islandsManager.circularArray(islandsManager.getMotherPos(), 6))
                assertEquals(1, islandsManager.getStudent(i, 0)); //controllo nelle isole al di fuori delle due escluse
        }
    }

    @Test
    @DisplayName("Test if checkVitcory return true when there are only 3 islands")
    void checkVictoryTest(){
        Round round;
        IslandsManager islandsManager = new IslandsManager(color);
        islandsManager.towerChange(0,WHITE);
        islandsManager.towerChange(2, WHITE);
        islandsManager.towerChange(1,WHITE); //si uniscono queste 3 in 0
        islandsManager.towerChange(1, WHITE); // si unisce in 0
        islandsManager.towerChange(1,BLACK);
        islandsManager.towerChange(3, BLACK);
        islandsManager.towerChange(2,BLACK);// si uniscono queste 3 in 1
        islandsManager.towerChange(2, GREY);
        islandsManager.towerChange(4, GREY);
        islandsManager.towerChange(3, GREY);// si uniscono queste 3 in 2
        islandsManager.towerChange(3, GREY);// si unisce in 2
        islandsManager.towerChange(3, WHITE);//si unisce in 0

        assertTrue(islandsManager.checkVictory());
    }

}
