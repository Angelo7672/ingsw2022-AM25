package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameTest {
    String[] playersInfo = {"Giorgio", "SAMURAI", "Marco", "KING", "Dino", "WIZARD"};
    Game game = new Game(true, 3, playersInfo);

}
