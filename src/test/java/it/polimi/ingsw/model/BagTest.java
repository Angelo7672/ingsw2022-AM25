package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BagTest {
   /* Bag bag;
    
    @BeforeEach
    void initialization(){
        bag = new Bag();
        bag.bagInitialize();
    }

    @Test
    @DisplayName("First test: check if all 120 numbers drawn are compatible with the legend")
    void bagInit(){
        int test;

        for(int i = 0; i < 120; i++) {
            test = bag.extraction();
            assertTrue(test >= 0, "A number outside the legend has been extracted");
            assertTrue(test < 5, "A number outside the legend has been extracted");
        }
    }*/


   /* @Test
    @DisplayName("Third test: extracts 130 students and check the victory")
    void checkVictory(){
        Bag bag = new Bag();
        int tmp;

        for(int i = 0; i < 119; i++) {
            tmp = bag.extraction();
            assertFalse(bag.checkVictory(),"The bag should not be empty");
        }
        tmp = bag.extraction();
        assertTrue(bag.checkVictory(),"The bag be empty");
    }
*/
}