package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    @DisplayName("First test: 3 players initialization")
    void playerInit() {
        int numberOfPlayer = 3;
        String[] playerInfo = {"Giorgio", "SAMURAI", "Marco", "KING", "Dino", "WIZARD"};
        PlayerManager playerManager = new PlayerManager(numberOfPlayer, playerInfo);

        //First Player
        assertAll(
                () -> assertEquals(Team.WHITE, playerManager.getTeam(0), "The player belongs to the WHITE team"),
                () -> assertEquals(1, playerManager.getCoins(0), "The player should have a coin")
        );
        //Second Player
        assertAll(
                () -> assertEquals(Team.BLACK, playerManager.getTeam(1), "The player belongs to the BLACK team"),
                () -> assertEquals(1, playerManager.getCoins(1), "The player should have a coin")
        );
        //Third Player
        assertAll(
                () -> assertEquals(Team.GREY, playerManager.getTeam(2), "The player belongs to the GREY team"),
                () -> assertEquals(1, playerManager.getCoins(2), "The player should have a coin")
        );
    }

    @Test
    @DisplayName("First test bis: 4 players initialization")
    void playerInit2() {
        int numberOfPlayer = 4;
        String[] playerInfo = {"Giorgio", "SAMURAI", "Marco", "KING", "Dino", "WIZARD", "Gloria", "WITCH"};
        PlayerManager playerManager = new PlayerManager(numberOfPlayer, playerInfo);

        //First Player
        assertAll(
                () -> assertEquals(Team.WHITE, playerManager.getTeam(0), "The player belongs to the WHITE team"),
                () -> assertEquals(1, playerManager.getCoins(0), "The player should have a coin")
        );
        //Second Player
        assertAll(
                () -> assertEquals(Team.WHITE, playerManager.getTeam(1), "The player belongs to the WHITE team"),
                () -> assertEquals(1, playerManager.getCoins(1), "The player should have a coin")
        );
        //Third Player
        assertAll(
                () -> assertEquals(Team.BLACK, playerManager.getTeam(2), "The player belongs to the BLACK team"),
                () -> assertEquals(1, playerManager.getCoins(2), "The player should have a coin")
        );
        //Fourth Player
        assertAll(
                () -> assertEquals(Team.BLACK, playerManager.getTeam(3), "The player belongs to the BLACK team"),
                () -> assertEquals(1, playerManager.getCoins(3), "The player should have a coin")
        );
    }

    @Test
    @DisplayName("Second test: coins control")
    void coinsControl() {
        int numberOfPlayer = 2;
        String[] playerInfo = {"Giorgio", "SAMURAI", "Marco", "KING"};
        PlayerManager playerManager = new PlayerManager(numberOfPlayer, playerInfo);
        int[] studentsGiorgio = new int[]{0, 0, 0, 2, 2, 2, 3, 3, 3, 4};
        int[] studentsMarco = new int[]{0, 0, 0, 0, 0, 0, 2, 2, 3, 3};

        //First player
        for (int i = 0; i < 10; i++) playerManager.setStudentEntrance(0, studentsGiorgio[i]);
        playerManager.transferStudent(0,0,true,false);
        playerManager.transferStudent(0,0,true,false);
        playerManager.transferStudent(0,0,true,false);
        playerManager.transferStudent(0,2,true,false);
        playerManager.transferStudent(0,2,true,false);
        playerManager.transferStudent(0,2,true,false);
        playerManager.transferStudent(0,3,true,false);
        playerManager.transferStudent(0,3,true,false);
        playerManager.transferStudent(0,3,true,false);
        playerManager.transferStudent(0,4,true,false);
        assertEquals(4,playerManager.getCoins(0),"The player has four coins");
        //Second player
        for (int i = 0; i < 10; i++) playerManager.setStudentEntrance(1, studentsMarco[i]);
        playerManager.transferStudent(1,0,true,false);
        playerManager.transferStudent(1,0,true,false);
        playerManager.transferStudent(1,0,true,false);
        playerManager.transferStudent(1,0,true,false);
        playerManager.transferStudent(1,0,true,false);
        playerManager.transferStudent(1,0,true,false);
        playerManager.transferStudent(1,2,true,false);
        playerManager.transferStudent(1,2,true,false);
        playerManager.transferStudent(1,3,true,false);
        playerManager.transferStudent(1,3,true,false);
        assertEquals(3,playerManager.getCoins(1),"The player has three coins");

        playerManager.removeCoin(0,3);
        playerManager.removeCoin(1,1);
        assertEquals(1,playerManager.getCoins(0),"The player has one coin");
        assertEquals(2,playerManager.getCoins(1),"The player has two coins");
    }
}