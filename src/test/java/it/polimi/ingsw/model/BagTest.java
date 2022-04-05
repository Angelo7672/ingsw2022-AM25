package it.polimi.ingsw.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {
    @Test
    @DisplayName("First test")
    void bagInit(){
        Bag b1 = new Bag();
        int test;

        test = b1.extraction();
        Assertions.assertEquals(1,test);
    }



    /*@Test
    void extraction() {
    }*/
}