package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SchoolTest {
    @Test
    @DisplayName("First test: check school initialization for 4 players")
    void schoolInit(){
        int numberOfPlayer = 4;
        String[] playerInfo = {"Giorgio","SAMURAI","Marco","KING","Dino","WIZARD","Gloria","WITCH"};
        PlayerManager playerManager = new PlayerManager(numberOfPlayer,playerInfo);

        //We want to check all the school boards


    }

    @Test
    @DisplayName("Second test: we make students move around the school")
    void schoolStudent(){

    }

    @Test
    @DisplayName("Third test: we control the movements of the towers with three players")
    void schoolTower(){

    }

    @Test
    @DisplayName("Third-bis test: we control the movements of the towers with four players")
    void schoolTowerBis(){

    }

    @Test
    @DisplayName("")
    void test(){

    }
}
