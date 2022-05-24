package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SchoolTest {
    @Test
    @DisplayName("First test: check school initialization for 4 players")
    void schoolInit(){
        int numberOfPlayer = 4;
        PlayerManager playerManager = new PlayerManager(numberOfPlayer);

        //We want to check all the school boards
        //First Player
        //Entrance
        assertAll(
                ()->assertEquals(0,playerManager.getStudentEntrance(0,0),"The entrance must be empty"),
                ()->assertEquals(0,playerManager.getStudentEntrance(0,1),"The entrance must be empty"),
                ()->assertEquals(0,playerManager.getStudentEntrance(0,2),"The entrance must be empty"),
                ()->assertEquals(0,playerManager.getStudentEntrance(0,3),"The entrance must be empty"),
                ()->assertEquals(0,playerManager.getStudentEntrance(0,4),"The entrance must be empty")
        );
        //Table
        assertAll(
                ()->assertEquals(0,playerManager.getStudentTable(0,0),"The table must be empty"),
                ()->assertEquals(0,playerManager.getStudentTable(0,1),"The table must be empty"),
                ()->assertEquals(0,playerManager.getStudentTable(0,2),"The table must be empty"),
                ()->assertEquals(0,playerManager.getStudentTable(0,3),"The table must be empty"),
                ()->assertEquals(0,playerManager.getStudentTable(0,4),"The table must be empty")
        );
        //Professor
        assertAll(
                ()->assertFalse(playerManager.getProfessor(0,0),"You don't have professors at the start of the game"),
                ()->assertFalse(playerManager.getProfessor(0,1),"You don't have professors at the start of the game"),
                ()->assertFalse(playerManager.getProfessor(0,2),"You don't have professors at the start of the game"),
                ()->assertFalse(playerManager.getProfessor(0,3),"You don't have professors at the start of the game"),
                ()->assertFalse(playerManager.getProfessor(0,4),"You don't have professors at the start of the game")
        );
        //Second Player
        //Entrance
        assertAll(
                ()->assertEquals(0,playerManager.getStudentEntrance(1,0),"The entrance must be empty"),
                ()->assertEquals(0,playerManager.getStudentEntrance(1,1),"The entrance must be empty"),
                ()->assertEquals(0,playerManager.getStudentEntrance(1,2),"The entrance must be empty"),
                ()->assertEquals(0,playerManager.getStudentEntrance(1,3),"The entrance must be empty"),
                ()->assertEquals(0,playerManager.getStudentEntrance(1,4),"The entrance must be empty")
        );
        //Table
        assertAll(
                ()->assertEquals(0,playerManager.getStudentTable(1,0),"The table must be empty"),
                ()->assertEquals(0,playerManager.getStudentTable(1,1),"The table must be empty"),
                ()->assertEquals(0,playerManager.getStudentTable(1,2),"The table must be empty"),
                ()->assertEquals(0,playerManager.getStudentTable(1,3),"The table must be empty"),
                ()->assertEquals(0,playerManager.getStudentTable(1,4),"The table must be empty")
        );
        //Professor
        assertAll(
                ()->assertFalse(playerManager.getProfessor(1,0),"You don't have professors at the start of the game"),
                ()->assertFalse(playerManager.getProfessor(1,1),"You don't have professors at the start of the game"),
                ()->assertFalse(playerManager.getProfessor(1,2),"You don't have professors at the start of the game"),
                ()->assertFalse(playerManager.getProfessor(1,3),"You don't have professors at the start of the game"),
                ()->assertFalse(playerManager.getProfessor(1,4),"You don't have professors at the start of the game")
        );
        //Third Player
        //Entrance
        assertAll(
                ()->assertEquals(0,playerManager.getStudentEntrance(2,0),"The entrance must be empty"),
                ()->assertEquals(0,playerManager.getStudentEntrance(2,1),"The entrance must be empty"),
                ()->assertEquals(0,playerManager.getStudentEntrance(2,2),"The entrance must be empty"),
                ()->assertEquals(0,playerManager.getStudentEntrance(2,3),"The entrance must be empty"),
                ()->assertEquals(0,playerManager.getStudentEntrance(2,4),"The entrance must be empty")
        );
        //Table
        assertAll(
                ()->assertEquals(0,playerManager.getStudentTable(2,0),"The table must be empty"),
                ()->assertEquals(0,playerManager.getStudentTable(2,1),"The table must be empty"),
                ()->assertEquals(0,playerManager.getStudentTable(2,2),"The table must be empty"),
                ()->assertEquals(0,playerManager.getStudentTable(2,3),"The table must be empty"),
                ()->assertEquals(0,playerManager.getStudentTable(2,4),"The table must be empty")
        );
        //Professor
        assertAll(
                ()->assertFalse(playerManager.getProfessor(2,0),"You don't have professors at the start of the game"),
                ()->assertFalse(playerManager.getProfessor(2,1),"You don't have professors at the start of the game"),
                ()->assertFalse(playerManager.getProfessor(2,2),"You don't have professors at the start of the game"),
                ()->assertFalse(playerManager.getProfessor(2,3),"You don't have professors at the start of the game"),
                ()->assertFalse(playerManager.getProfessor(2,4),"You don't have professors at the start of the game")
        );
        //Fourth Player
        //Entrance
        assertAll(
                ()->assertEquals(0,playerManager.getStudentEntrance(3,0),"The entrance must be empty"),
                ()->assertEquals(0,playerManager.getStudentEntrance(3,1),"The entrance must be empty"),
                ()->assertEquals(0,playerManager.getStudentEntrance(3,2),"The entrance must be empty"),
                ()->assertEquals(0,playerManager.getStudentEntrance(3,3),"The entrance must be empty"),
                ()->assertEquals(0,playerManager.getStudentEntrance(3,4),"The entrance must be empty")
        );
        //Table
        assertAll(
                ()->assertEquals(0,playerManager.getStudentTable(3,0),"The table must be empty"),
                ()->assertEquals(0,playerManager.getStudentTable(3,1),"The table must be empty"),
                ()->assertEquals(0,playerManager.getStudentTable(3,2),"The table must be empty"),
                ()->assertEquals(0,playerManager.getStudentTable(3,3),"The table must be empty"),
                ()->assertEquals(0,playerManager.getStudentTable(3,4),"The table must be empty")
        );
        //Professor
        assertAll(
                ()->assertFalse(playerManager.getProfessor(3,0),"You don't have professors at the start of the game"),
                ()->assertFalse(playerManager.getProfessor(3,1),"You don't have professors at the start of the game"),
                ()->assertFalse(playerManager.getProfessor(3,2),"You don't have professors at the start of the game"),
                ()->assertFalse(playerManager.getProfessor(3,3),"You don't have professors at the start of the game"),
                ()->assertFalse(playerManager.getProfessor(3,4),"You don't have professors at the start of the game")
        );
    }

    /*@Test
    @DisplayName("Second test: make students move around the school")
    void schoolStudent(){
        int numberOfPlayer = 2;
        PlayerManager playerManager = new PlayerManager(numberOfPlayer);
        int[] studentsGiorgio = new int[]{0,0,1,1,2,2,3,3,4,4};
        int[] studentsMarco = new int[]{0,0,0,0,0,3,3,3,4,4};

        //We add students to the entry and check for changes
        //First Player
        for(int i = 0; i < 10; i++) playerManager.setStudentEntrance(0,studentsGiorgio[i]);
        assertAll(
                ()->assertEquals(2,playerManager.getStudentEntrance(0,0),"In this test, there must be two students of each color in the entrance"),
                ()->assertEquals(2,playerManager.getStudentEntrance(0,1),"In this test, there must be two students of each color in the entrance"),
                ()->assertEquals(2,playerManager.getStudentEntrance(0,2),"In this test, there must be two students of each color in the entrance"),
                ()->assertEquals(2,playerManager.getStudentEntrance(0,3),"In this test, there must be two students of each color in the entrance"),
                ()->assertEquals(2,playerManager.getStudentEntrance(0,4),"In this test, there must be two students of each color in the entrance")
        );
        for(int i = 0; i < 10; i++) playerManager.transferStudent(0,studentsGiorgio[i],true,false);
        assertAll(
                ()->assertEquals(2,playerManager.getStudentTable(0,0),"In this test, there must be two students at each table"),
                ()->assertEquals(2,playerManager.getStudentTable(0,1),"In this test, there must be two students at each table"),
                ()->assertEquals(2,playerManager.getStudentTable(0,2),"In this test, there must be two students at each table"),
                ()->assertEquals(2,playerManager.getStudentTable(0,3),"In this test, there must be two students at each table"),
                ()->assertEquals(2,playerManager.getStudentTable(0,4),"In this test, there must be two students at each table")
        );
        assertAll(
                ()->assertTrue(playerManager.getProfessor(0,0),"At this point the player must own the professor"),
                ()->assertTrue(playerManager.getProfessor(0,1),"At this point the player must own the professor"),
                ()->assertTrue(playerManager.getProfessor(0,2),"At this point the player must own the professor"),
                ()->assertTrue(playerManager.getProfessor(0,3),"At this point the player must own the professor"),
                ()->assertTrue(playerManager.getProfessor(0,4),"At this point the player must own the professor")
        );
        //Second Player
        for(int i = 0; i < 10; i++) playerManager.setStudentEntrance(1,studentsMarco[i]);
        assertAll(
                ()->assertEquals(5,playerManager.getStudentEntrance(1,0),"In this test, there must be five students of each color in the entrance"),
                ()->assertEquals(0,playerManager.getStudentEntrance(1,1),"In this test, there must be zero students of each color in the entrance"),
                ()->assertEquals(0,playerManager.getStudentEntrance(1,2),"In this test, there must be zero students of each color in the entrance"),
                ()->assertEquals(3,playerManager.getStudentEntrance(1,3),"In this test, there must be three students of each color in the entrance"),
                ()->assertEquals(2,playerManager.getStudentEntrance(1,4),"In this test, there must be two students of each color in the entrance")
        );
        for(int i = 0; i < 10; i++) playerManager.transferStudent(1,studentsMarco[i],true,false);
        assertAll(
                ()->assertEquals(5,playerManager.getStudentTable(1,0),"In this test, there must be five students at each table"),
                ()->assertEquals(0,playerManager.getStudentTable(1,1),"In this test, there must be zero students at each table"),
                ()->assertEquals(0,playerManager.getStudentTable(1,2),"In this test, there must be zero students at each table"),
                ()->assertEquals(3,playerManager.getStudentTable(1,3),"In this test, there must be three students at each table"),
                ()->assertEquals(2,playerManager.getStudentTable(1,4),"In this test, there must be two students at each table")
        );
        assertAll(
                ()->assertTrue(playerManager.getProfessor(1,0),"At this point the player must own the professor"),
                ()->assertFalse(playerManager.getProfessor(1,1),"The professor is from Giorgio"),
                ()->assertFalse(playerManager.getProfessor(1,2),"The professor is from Giorgio"),
                ()->assertTrue(playerManager.getProfessor(1,3),"At this point the player must own the professor"),
                ()->assertFalse(playerManager.getProfessor(1,4),"The professor is from Giorgio")
        );
        //Final check
        assertAll(
                ()->assertFalse(playerManager.getProfessor(0,0),"The professor is from Marco"),
                ()->assertFalse(playerManager.getProfessor(0,3),"The professor is from Marco")
        );
    }*/

    @Test
    @DisplayName("Third test: control the movements of the towers with three players")
    void schoolTower(){
        int numberOfPlayer = 3;
        PlayerManager playerManager = new PlayerManager(numberOfPlayer);
        boolean victory1,victory2,victory3,victory3_bis;

        //First Player
        victory1 = playerManager.removeTower(Team.BLACK,2);
        assertAll(
                ()->assertEquals(4,playerManager.getTowers(1),"We have removed two towers"),
                ()->assertFalse(victory1,"Towers are not finished")
        );
        playerManager.placeTower(Team.BLACK,1);
        assertEquals(5,playerManager.getTowers(1),"We have add a tower");
        //Second Player
        victory2 = playerManager.removeTower(Team.WHITE,5);
        assertAll(
                ()->assertEquals(1,playerManager.getTowers(0),"We have removed five towers"),
                ()->assertFalse(victory2,"Towers are not finished")
        );
        playerManager.placeTower(Team.WHITE,3);
        assertEquals(4,playerManager.getTowers(0),"We have add three towers");
        //Third Player
        victory3 = playerManager.removeTower(Team.GREY,4);
        assertFalse(victory3,"Towers are not finished");
        victory3_bis = playerManager.removeTower(Team.GREY,2);
        assertTrue(victory3_bis,"Towers are finished");
        playerManager.placeTower(Team.GREY,6);
        assertEquals(6,playerManager.getTowers(2),"We have add six towers");
    }

    @Test
    @DisplayName("Third-bis test: control the movements of the towers with four players")
    void schoolTowerBis(){
        int numberOfPlayer = 4;
        PlayerManager playerManager = new PlayerManager(numberOfPlayer);
        boolean victory1,victory2;

        //WHITE Team
        victory1 = playerManager.removeTower(Team.WHITE,2);
        assertAll(
                ()->assertEquals(6,playerManager.getTowers(0),"We have removed two towers"),
                ()->assertEquals(6,playerManager.getTowers(1),"We have removed two towers"),
                ()->assertFalse(victory1,"Towers are not finished")
        );
        playerManager.placeTower(Team.WHITE,2);
        assertAll(
                ()->assertEquals(8,playerManager.getTowers(0),"We have add two towers"),
                ()->assertEquals(8,playerManager.getTowers(1),"We have add two towers")
        );
        //BLACK Team
        victory2 = playerManager.removeTower(Team.BLACK,8);
        assertAll(
                ()->assertEquals(0,playerManager.getTowers(2),"We have removed eight towers"),
                ()->assertEquals(0,playerManager.getTowers(3),"We have removed eight towers"),
                ()->assertTrue(victory2,"Towers are finished")
        );
        playerManager.placeTower(Team.BLACK,3);
        assertAll(
                ()->assertEquals(3,playerManager.getTowers(2),"We have add three towers"),
                ()->assertEquals(3,playerManager.getTowers(3),"We have add three towers")
        );
    }
}
