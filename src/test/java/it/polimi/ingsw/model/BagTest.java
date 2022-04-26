package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BagTest {
    @Test
    @DisplayName("First test: check if all 130 numbers drawn are compatible with the legend")
    void bagInit(){
        Bag bag = new Bag();
        int test;

        for(int i = 0; i < 120; i++) {
            test = bag.extraction();
            assertTrue(test >= 0, "A number outside the legend has been extracted");
            assertTrue(test < 5, "A number outside the legend has been extracted");
        }
    }

    /**
    @Test       //e' corretto questo test? Tecnicamente c'e' la prob che siano tutti uguali
    @DisplayName("Second test: create a bag and compare its contents with three other bags")
    void differentBag() {
        Bag cavia = new Bag();
        Bag cavia1 = new Bag();
        Bag cavia2 = new Bag();
        Bag cavia3 = new Bag();

        assertAll(
                ()->assertNotEquals(cavia,cavia1,"The probability that they are equal is low"),      //uno non significa niente
                ()->assertNotEquals(cavia,cavia2,"The probability that they are equal is low"),      //due puo' essere una coincidenza
                ()->assertNotEquals(cavia,cavia3,"The probability that they are equal is low")       //tre abbiamo un problema
        );
    }
     */

    @Test
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

}