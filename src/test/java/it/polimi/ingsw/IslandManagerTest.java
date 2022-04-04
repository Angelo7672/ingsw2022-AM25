package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IslandManagerTest {

    @Test
    //check setup isole vuote posMother e davanti a mother
    void setupIslandMotherTest() {
        IslandsManager islandsManager = new IslandsManager();
        for(int i=0; i<12; i++){
            islandsManager.setup(0, i);
        }
        assertEquals(0, islandsManager.getStudent(islandsManager.getMotherPos(), 0));
        assertEquals(0, islandsManager.getStudent(islandsManager.getMotherPos()+5, 0));
    }

    @Test
    //check setup isole
    void setupIslandTest() {
        IslandsManager islandsManager = new IslandsManager();
        for(int i=0; i<12; i++){
            islandsManager.setup(0, i);
            assertEquals(1, islandsManager.getStudent(i, 0));
        }
    }

    @Test
    //test calcolo influenza -1
    void influenceNoOneTest(){
        IslandsManager islandsManager = new IslandsManager();
        ArrayList<Integer> prof = new ArrayList<>();
        for(int i=0; i<5; i++) {
            islandsManager.incStudent(0, i);
            prof.add(-1);
        }
        int influence = islandsManager.highestInfluenceTeam(prof, 0);
        assertEquals(-1,influence);
    }

    @Test
    //test calcolo influenza
    void influenceSomeOneTest(){
        IslandsManager islandsManager = new IslandsManager();
        ArrayList<Integer> prof = new ArrayList<>();
        for(int i=0; i<5; i++) {
            islandsManager.incStudent(0, i);
            prof.add(0);
        }
        int influence = islandsManager.highestInfluenceTeam(prof, 0);
        assertEquals(0,influence);
    }

    @Test
    //checkVictory se le isole sono = 3
    void checkVictoryTest(){
        IslandsManager islandsManager = new IslandsManager();
        islandsManager.setTower(0,0);
        islandsManager.setTower(0,11);
        islandsManager.setTower(0,1);
        islandsManager.checkAdjacentIslands(0); //0,1,11 si uniscono in 0
        islandsManager.setTower(0,1);
        islandsManager.checkAdjacentIslands(0); //1 si unisce in 0
        islandsManager.setTower(1,1);
        islandsManager.setTower(1,2);
        islandsManager.setTower(1,3);
        islandsManager.checkAdjacentIslands(2); //1,2,3 si uniscono in 1
        islandsManager.setTower(2,2);
        islandsManager.setTower(2,3);
        islandsManager.setTower(2,4);
        islandsManager.checkAdjacentIslands(3); //2,3,4 si uniscono in 2
        islandsManager.setTower(2,3);
        islandsManager.setTower(2,4);
        islandsManager.checkAdjacentIslands(3); //3,4 si uniscono in 2
        assertTrue(islandsManager.checkVictory());
    }


}
