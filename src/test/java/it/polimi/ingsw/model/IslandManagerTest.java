package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;


import static it.polimi.ingsw.model.Team.*;
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
        assertEquals(0, islandsManager.getStudent(islandsManager.sum(islandsManager.getMotherPos(), 6), 0));
    }

    @Test
    //check setup isole
    void setupIslandTest() {
        IslandsManager islandsManager = new IslandsManager();
        for(int i=0; i<12; i++){
            islandsManager.setup(0, i);
            if(i!= islandsManager.getMotherPos()&&i!=islandsManager.sum(islandsManager.getMotherPos(), 6))
                assertEquals(1, islandsManager.getStudent(i, 0)); //controllo nelle isole al di fuori delle due escluse
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
        Team influence = islandsManager.highestInfluenceTeam(prof, 0);
        assertEquals(NOONE,influence);
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
        Team influence = islandsManager.highestInfluenceTeam(prof, 0);
        assertEquals(WHITE,influence);
    }


    @Test
        //checkVictory se le isole sono = 3
    void checkVictoryTest(){
        IslandsManager islandsManager = new IslandsManager();
        islandsManager.setTower(WHITE,0);
        islandsManager.setTower(WHITE,1);
        islandsManager.setTower(WHITE,2);
        islandsManager.setTower(WHITE,3);
        islandsManager.setTower(BLACK,4);;
        islandsManager.setTower(BLACK,5);
        islandsManager.setTower(BLACK,6);
        islandsManager.setTower(GREY,7);
        islandsManager.setTower(GREY,8);
        islandsManager.setTower(GREY,9);
        islandsManager.setTower(GREY,10);
        islandsManager.setTower(GREY,11);
        islandsManager.checkAdjacentIslands(1); //0,1,2 si uniscono in 0
        islandsManager.checkAdjacentIslands(1); //0,1 si uniscono in 0
        islandsManager.checkAdjacentIslands(2); //1,2,3 si uniscono in 1
        islandsManager.checkAdjacentIslands(3); //2,3,4 si unisce in 2
        islandsManager.checkAdjacentIslands(3); //3,4 si uniscono in 2
        assertEquals(islandsManager.size(),3);
    }


}
