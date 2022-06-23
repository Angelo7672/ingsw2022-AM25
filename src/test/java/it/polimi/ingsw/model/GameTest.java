package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameTest {
    Game game;

    @BeforeEach
    void initialization() {
        game = new Game(false, 3);
    }



}
