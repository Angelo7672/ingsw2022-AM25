package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.model.exception.NotAllowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SchoolTest {
    PlayerManager playerManager2P, playerManager3P, playerManager4P;
    Bag bag;

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
    }

    @Test
    @DisplayName("First test: make students move around the school")
    void schoolStudent(){
        int[] studentsGiorgio = new int[]{0,0,1,1,2,2,3,3,4,4};
        int[] studentsMarco = new int[]{0,0,0,0,0,3,3,3,4,4};

        //We add students to the entry and check for changes
        //First Player
        for(int i = 0; i < 10; i++)
            playerManager2P.setStudentEntrance(0,studentsGiorgio[i],1);
        for(int i = 0; i < 10; i++)
            try { playerManager2P.transferStudent(0,studentsGiorgio[i],true,false);
            }catch (NotAllowedException notAllowedException){ notAllowedException.printStackTrace(); return; }

        assertAll(
                ()->assertEquals(2,playerManager2P.getStudentTable(0,0),"In this test, there must be two students at each table"),
                ()->assertEquals(2,playerManager2P.getStudentTable(0,1),"In this test, there must be two students at each table"),
                ()->assertEquals(2,playerManager2P.getStudentTable(0,2),"In this test, there must be two students at each table"),
                ()->assertEquals(2,playerManager2P.getStudentTable(0,3),"In this test, there must be two students at each table"),
                ()->assertEquals(2,playerManager2P.getStudentTable(0,4),"In this test, there must be two students at each table")
        );
        assertAll(
                ()->assertEquals(0,playerManager2P.getProfessorPropriety(0),"At this point the player must own the professor"),
                ()->assertEquals(0,playerManager2P.getProfessorPropriety(1),"At this point the player must own the professor"),
                ()->assertEquals(0,playerManager2P.getProfessorPropriety(2),"At this point the player must own the professor"),
                ()->assertEquals(0,playerManager2P.getProfessorPropriety(3),"At this point the player must own the professor"),
                ()->assertEquals(0,playerManager2P.getProfessorPropriety(4),"At this point the player must own the professor")
        );
        //Second Player
        for(int i = 0; i < 10; i++)
            playerManager2P.setStudentEntrance(1,studentsMarco[i],1);
        for(int i = 0; i < 10; i++)
            try { playerManager2P.transferStudent(1,studentsMarco[i],true,false);
            } catch (NotAllowedException notAllowedException){ notAllowedException.printStackTrace(); return; }
        assertAll(
                ()->assertEquals(5,playerManager2P.getStudentTable(1,0),"In this test, there must be five students at each table"),
                ()->assertEquals(0,playerManager2P.getStudentTable(1,1),"In this test, there must be zero students at each table"),
                ()->assertEquals(0,playerManager2P.getStudentTable(1,2),"In this test, there must be zero students at each table"),
                ()->assertEquals(3,playerManager2P.getStudentTable(1,3),"In this test, there must be three students at each table"),
                ()->assertEquals(2,playerManager2P.getStudentTable(1,4),"In this test, there must be two students at each table")
        );
        assertAll(
                ()->assertEquals(1,playerManager2P.getProfessorPropriety(0),"At this point the player must own the professor"),
                ()->assertEquals(0,playerManager2P.getProfessorPropriety(1),"The professor is from Giorgio"),
                ()->assertEquals(0,playerManager2P.getProfessorPropriety(2),"The professor is from Giorgio"),
                ()->assertEquals(1,playerManager2P.getProfessorPropriety(3),"At this point the player must own the professor"),
                ()->assertEquals(0,playerManager2P.getProfessorPropriety(4),"The professor is from Giorgio")
        );
    }
}
