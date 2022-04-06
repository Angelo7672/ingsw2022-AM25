package it.polimi.ingsw.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {
    @Test
    @DisplayName("First test: check if all 130 numbers drawn are compatible with the legend")
    void bagInit(){
        Bag b1 = new Bag();
        int test;

        for(int i = 0; i < 130; i++) {
            test = b1.extraction();
            Assertions.assertTrue(test >= 0, "A number outside the legend has been extracted");
            Assertions.assertTrue(test < 5, "A number outside the legend has been extracted");
            /*Assertions.assertEquals(1,test);*/
        }
    }



    /*@Test
    void extraction() {
    }*/
}