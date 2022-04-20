package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;


import static it.polimi.ingsw.model.Team.*;
import static org.junit.jupiter.api.Assertions.*;

public class IslandManagerTest {
    Round round;

    @Test
    @DisplayName("Test if the island of Mother Nature and the one in front are empty")
    void setupIslandMotherTest() {
        IslandsManager islandsManager = new IslandsManager();
        assertEquals(0, islandsManager.getStudent(islandsManager.getMotherPos(), 0));
        assertEquals(0, islandsManager.getStudent(islandsManager.circularArray(islandsManager.getMotherPos(), 6), 0));
    }

    @Test
    @DisplayName("Test if islands are filled correctly")
    void setupIslandTest() {
        IslandsManager islandsManager = new IslandsManager();
        ArrayList<Integer> numStudentPerIsland = new ArrayList<>();
        for(int i=0; i<12; i++){ //count the number of student on each island and fill the array
            int numStudents = 0;
            for(int j=0; j<5; j++) numStudents += islandsManager.getStudent(i,j);
            numStudentPerIsland.add(numStudents);
        }
        for(int i=0; i<12; i++) {
            if (i != islandsManager.getMotherPos() && i != islandsManager.circularArray(islandsManager.getMotherPos(), 6)){
                assertEquals(1, numStudentPerIsland.get(i)); //controllo nelle isole al di fuori delle due escluse
            }
            else {
                assertEquals(0, numStudentPerIsland.get(i));
            }
        }
    }

    @Test
    @DisplayName("Test if checkVitcory return true when there are only 3 islands")
    void checkVictoryTest(){
        Round round;
        IslandsManager islandsManager = new IslandsManager();
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