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





}
