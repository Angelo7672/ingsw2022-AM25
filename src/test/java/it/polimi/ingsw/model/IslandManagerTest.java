package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;


import static it.polimi.ingsw.model.Team.*;
import static org.junit.jupiter.api.Assertions.*;

public class IslandManagerTest {
    IslandsManager islandsManager;

    @BeforeEach
    void initialization(){
        islandsManager = new IslandsManager();
        islandsManager.islandListener = new IslandListener() {
            @Override
            public void notifyIslandChange(int islandToDelete) {}
        };
        islandsManager.towersListener = new TowersListener() {
            @Override
            public void notifyTowersChange(int place, int componentRef, int towersNumber) {}

            @Override
            public void notifyTowerColor(int islandRef, int newColor) {}
        };
        islandsManager.motherPositionListener = new MotherPositionListener() {
            @Override
            public void notifyMotherPosition(int newMotherPosition) {}
        };
        islandsManager.inhibitedListener = new InhibitedListener() {
            @Override
            public void notifyInhibited(int islandRef, int isInhibited) {}
        };
        islandsManager.studentListener = new StudentsListener() {
            @Override
            public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {}
        };
        islandsManager.islandsInitialize();
    }

    @Test
    @DisplayName("Test if the island of Mother Nature and the one in front are empty")
    void setupIslandMotherTest() {
        assertEquals(0, islandsManager.getStudent(islandsManager.getMotherPos(), 0));
        assertEquals(0, islandsManager.getStudent(islandsManager.circularArray(islandsManager.getMotherPos(), 6), 0));
    }

    @Test
    @DisplayName("Test if islands are filled correctly")
    void setupIslandTest() {
        ArrayList<Integer> numStudentPerIsland = new ArrayList<>();
        for(int i=0; i<12; i++){ //count the number of student on each island and fill the array
            int numStudents = 0;
            for(int j=0; j<5; j++) numStudents += islandsManager.getStudent(i,j);
            numStudentPerIsland.add(numStudents);
        }
        for(int i=0; i<12; i++) {
            if (i != islandsManager.getMotherPos() && i != islandsManager.circularArray(islandsManager.getMotherPos(), 6)){
                assertEquals(1, numStudentPerIsland.get(i)); //check number of students = 1
            }
            else {
                assertEquals(0, numStudentPerIsland.get(i));
            }
        }
    }


    @Test
    @DisplayName("Test if checkVitcory return true when there are only 3 islands")
    void checkVictoryTest(){
        assertEquals(false, islandsManager.checkVictory());
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

    @Test
    @DisplayName("Test returned array of towerChange")
    void towerChangeTest(){
        int[] returnItem = new int[2];
        returnItem = islandsManager.towerChange(0, WHITE); //if old team not equals new one and is no one
        assertEquals(1, returnItem[0]);
        assertEquals(-1, returnItem[1]);

        returnItem = islandsManager.towerChange(0, BLACK); //if old team not equals new one and is not no one
        assertEquals(1, returnItem[0]);
        assertEquals(0, returnItem[1]);

        returnItem = islandsManager.towerChange(0, BLACK); //if old team equals new one
        assertEquals(0, returnItem[0]);
        assertEquals(-1, returnItem[1]);

    }

    @Test
    @DisplayName("Test get/set methods")
    void getSetMethod(){

        //student methods
        int tempIslandStudent0 = islandsManager.getStudent(0,0);
        islandsManager.incStudent(0, 0,1);
        assertEquals(tempIslandStudent0+1, islandsManager.getStudent(0,0));

        //mother methods
        int motherPos = islandsManager.getMotherPos();
        islandsManager.moveMotherNature(7);
        assertEquals(islandsManager.circularArray(motherPos, 7), islandsManager.getMotherPos());

        //tower value methods
        islandsManager.towerChange(0, WHITE);
        assertEquals(WHITE, islandsManager.getTowerTeam(0));
        assertEquals(1, islandsManager.getTowerValue(0));

        //size methods
        islandsManager.towerChange(1, WHITE);
        assertEquals(2, islandsManager.getTowerValue(0));
        assertEquals(11, islandsManager.size());

        //inhibition methods
        assertEquals(0, islandsManager.getInhibited(0));
        islandsManager.increaseInhibited(0);
        islandsManager.increaseInhibited(0);
        assertEquals(2, islandsManager.getInhibited(0));
        islandsManager.decreaseInhibited(0);
        assertEquals(1, islandsManager.getInhibited(0));


    }
}
