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

class PlayerTest {
    PlayerManager playerManager2P, playerManager3P, playerManager4P;
    Bag bag;
    int coinsRestore;
    ArrayList<Assistant> cardsRestore;

    @BeforeEach
    void initialization() {
        cardsRestore = new ArrayList<>();
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
                public void notifyNewCoinsValue(int playerRef, int newCoinsValue) {
                    coinsRestore = newCoinsValue;
                }
            };
            playerManager.playedCardListener = new PlayedCardListener() {
                @Override
                public void notifyPlayedCard(int playerRef, String assistantCard) {}
                @Override
                public void notifyHand(int playerRef, ArrayList<String> hand) {
                    for(String s:hand)
                        cardsRestore.add(stringToAssistant(s));
                }
            };
            playerManager.studentsListener = new StudentsListener() {
                @Override
                public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {}
            };
        }
    }
    private Assistant stringToAssistant(String string){
        if(string.equalsIgnoreCase("LION")) return Assistant.LION;
        else if(string.equalsIgnoreCase("GOOSE")) return Assistant.GOOSE;
        else if(string.equalsIgnoreCase("CAT")) return Assistant.CAT;
        else if(string.equalsIgnoreCase("EAGLE")) return Assistant.EAGLE;
        else if(string.equalsIgnoreCase("FOX")) return Assistant.FOX;
        else if(string.equalsIgnoreCase("LIZARD")) return Assistant.LIZARD;
        else if(string.equalsIgnoreCase("OCTOPUS")) return Assistant.OCTOPUS;
        else if(string.equalsIgnoreCase("DOG")) return Assistant.DOG;
        else if(string.equalsIgnoreCase("ELEPHANT")) return Assistant.ELEPHANT;
        else if(string.equalsIgnoreCase("TURTLE")) return Assistant.TURTLE;
        return Assistant.NONE;
    }

    @Test
    @DisplayName("First test: 3 players initialization")
    void playerInit() {
        //First Player
        assertAll(
                () -> assertEquals(Team.WHITE, playerManager3P.getTeam(0), "The player belongs to the WHITE team"),
                () -> assertEquals(1, playerManager3P.getCoins(0), "The player should have a coin")
        );
        //Second Player
        assertAll(
                () -> assertEquals(Team.BLACK, playerManager3P.getTeam(1), "The player belongs to the BLACK team"),
                () -> assertEquals(1, playerManager3P.getCoins(1), "The player should have a coin")
        );
        //Third Player
        assertAll(
                () -> assertEquals(Team.GREY, playerManager3P.getTeam(2), "The player belongs to the GREY team"),
                () -> assertEquals(1, playerManager3P.getCoins(2), "The player should have a coin")
        );
    }

    @Test
    @DisplayName("First test-bis: 4 players initialization")
    void playerInit2() {
        //First Player
        assertAll(
                () -> assertEquals(Team.WHITE, playerManager4P.getTeam(0), "The player belongs to the WHITE team"),
                () -> assertEquals(1, playerManager4P.getCoins(0), "The player should have a coin")
        );
        //Second Player
        assertAll(
                () -> assertEquals(Team.WHITE, playerManager4P.getTeam(1), "The player belongs to the WHITE team"),
                () -> assertEquals(1, playerManager4P.getCoins(1), "The player should have a coin")
        );
        //Third Player
        assertAll(
                () -> assertEquals(Team.BLACK, playerManager4P.getTeam(2), "The player belongs to the BLACK team"),
                () -> assertEquals(1, playerManager4P.getCoins(2), "The player should have a coin")
        );
        //Fourth Player
        assertAll(
                () -> assertEquals(Team.BLACK, playerManager4P.getTeam(3), "The player belongs to the BLACK team"),
                () -> assertEquals(1, playerManager4P.getCoins(3), "The player should have a coin")
        );
    }

    @Test
    @DisplayName("Second test: coins control")
    void coinsControl() throws NotAllowedException {
        int[] studentsGiorgio = new int[]{0, 0, 0, 2, 2, 2, 3, 3, 3, 4};
        int[] studentsMarco = new int[]{0, 0, 0, 0, 0, 0, 2, 2, 3, 3};

        //First player
        for (int i = 0; i < 10; i++)
            playerManager2P.setStudentEntrance(0, studentsGiorgio[i], 1);
        playerManager2P.transferStudent(0,0,true,false);
        playerManager2P.transferStudent(0,0,true,false);
        playerManager2P.transferStudent(0,0,true,false);
        playerManager2P.transferStudent(0,2,true,false);
        playerManager2P.transferStudent(0,2,true,false);
        playerManager2P.transferStudent(0,2,true,false);
        playerManager2P.transferStudent(0,3,true,false);
        playerManager2P.transferStudent(0,3,true,false);
        playerManager2P.transferStudent(0,3,true,false);
        playerManager2P.transferStudent(0,4,true,false);
        assertEquals(4,playerManager2P.getCoins(0),"The player has four coins");
        //Second player
        for (int i = 0; i < 10; i++)
            playerManager2P.setStudentEntrance(1, studentsMarco[i], 1);
        playerManager2P.transferStudent(1,0,true,false);
        playerManager2P.transferStudent(1,0,true,false);
        playerManager2P.transferStudent(1,0,true,false);
        playerManager2P.transferStudent(1,0,true,false);
        playerManager2P.transferStudent(1,0,true,false);
        playerManager2P.transferStudent(1,0,true,false);
        playerManager2P.transferStudent(1,2,true,false);
        playerManager2P.transferStudent(1,2,true,false);
        playerManager2P.transferStudent(1,3,true,false);
        playerManager2P.transferStudent(1,3,true,false);
        assertEquals(3,playerManager2P.getCoins(1),"The player has three coins");

        playerManager2P.removeCoin(0,3);
        playerManager2P.removeCoin(1,1);
        assertEquals(1,playerManager2P.getCoins(0),"The player has one coin");
        assertEquals(2,playerManager2P.getCoins(1),"The player has two coins");
    }

    @Test
    @DisplayName("Third test: restore hand and coins")
    void restoreHandAndCoins(){
        ArrayList<Assistant> cards = new ArrayList<>();
        cards.add(Assistant.GOOSE);cards.add(Assistant.EAGLE);cards.add(Assistant.TURTLE);
        int coins = 3;
        playerManager2P.restoreHandAndCoins(0,cards,coins);
        for (int i = 0 ; i < cards.size(); i++)
            assertEquals(cards.get(i),cardsRestore.get(i));
        assertEquals(coins,coinsRestore);
    }
}