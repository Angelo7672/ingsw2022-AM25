package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.server.VirtualClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameTest {
    Game game;

    @BeforeEach
    void initialization() {
        game = new Game(true, 3);

    }

    public void setMaxCoins(){
        ArrayList<String> cards = new ArrayList<>();
        cards.add("LION");
        game.initializeGame();
        game.handAndCoinsRestore(0, cards, 10);

    }
    public void setSchool(){
        int[] entrance = {1,1,1,1,1};
        int[] table = {1,1,1,1,1};
        boolean[] prof ={false,false,false,false,false};
        game.schoolRestore(0, entrance, table,6,prof, "WHITE");
    }

    @Test
    @DisplayName("test if special lite work correcly")
    public void useSpecialLiteTest(){
        game.initializeGame();
        boolean result = game.useSpecialLite(2,0);
        assertFalse(result);
        setMaxCoins();
        result = game.useSpecialLite(2, 0);
        assertTrue(result);

    }

    @Test
    @DisplayName("test if special simple work correcly")
    public void useSpecialSimpleTest(){
        game.initializeGame();
        boolean result = game.useSpecialSimple(5,0, 1);
        assertFalse(result);
        setMaxCoins();
        result = game.useSpecialSimple(5, 0, 1);
        assertTrue(result);
    }

    @Test
    @DisplayName("Test if special3 work correctly")
    public void useSpecial3Test() throws EndGameException {
        game.initializeGame();
        boolean result = game.useSpecial3(0, 1);
        assertFalse(result);
        setMaxCoins();
        result = game.useSpecial3(0, 15);
        assertFalse(result);
        result = game.useSpecial3(0, 1);
        assertTrue(result);
    }

    @Test
    @DisplayName("test if special medium work correcly")
    public void useSpecialMediumTest(){
        game.initializeGame();
        boolean result = game.useStrategySimple(1,0,2);
        assertFalse(result);
        setMaxCoins();
        setSchool();
        result = game.useStrategySimple(1, 0, 1);
        assertTrue(result);
    }

    @Test
    @DisplayName("test if special strategy work correcly")
    public void useStrategySimple(){
        game.initializeGame();
        boolean result = game.useSpecialMedium(9,0, 1, 1);
        assertFalse(result);
        setMaxCoins();
        setSchool();
        result = game.useSpecialMedium(9, 0, 1, 1);
        assertTrue(result);
    }

    @Test
    @DisplayName("test if special hard work correcly")
    public void useSpecialHardTest(){
        game.initializeGame();
        ArrayList<Integer> color1 = new ArrayList<>();
        ArrayList<Integer> color2 = new ArrayList<>();
        color2.add(0);
        color2.add(2);
        color1.add(1);
        color1.add(3);
        boolean result = game.useSpecialHard(7,0,color1, color2);
        assertFalse(result);
        setMaxCoins();
        setSchool();
        result = game.useSpecialHard(7, 0, color1, color2);
        assertTrue(result);
    }



}
