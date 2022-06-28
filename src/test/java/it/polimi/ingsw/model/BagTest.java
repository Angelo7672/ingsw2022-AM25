package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.BagListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {
    Bag bag;
    List<Integer> bagRestore;

    /**
     * Initialize bag with its listeners.
     */
    @BeforeEach
    void initialization() {
        bagRestore = new ArrayList<>();
        bag = new Bag();
        bag.bagListener = new BagListener() {
            @Override
            public void notifyBagExtraction() {}
            @Override
            public void notifyBag(List<Integer> bag) {
                bagRestore = bag;
            }
        };
        bag.bagInitialize();
    }

    /**
     * Test students' extraction.
     */
    @Test
    @DisplayName("First test: check if all 120 numbers drawn are compatible with the legend")
    void bagInit() {
        int test;

        for (int i = 0; i < 120; i++) {
            test = bag.extraction();
            assertTrue(test >= 0, "A number outside the legend has been extracted");
            assertTrue(test < 5, "A number outside the legend has been extracted");
        }
    }

    /**
     * Check if bag is empty after 120 extractions.
     */
    @Test
    @DisplayName("Second test: extracts 120 students and check the victory")
    void checkVictory(){
        for(int i = 0; i < 119; i++) {
            bag.extraction();
            assertFalse(bag.checkVictory(),"The bag should not be empty");
        }
        bag.extraction();
        assertTrue(bag.checkVictory(),"The bag be empty");
    }

    /**
     * Restore a bag of three students
     */
    @Test
    @DisplayName("Third test: bag restore")
    void restoreBag(){
        List<Integer> bagTmp = new ArrayList<>();
        bagTmp.add(1);bagTmp.add(2);bagTmp.add(3);  //add three students

        bag.bagRestore(bagTmp);
        for(int i = 0; i < bagTmp.size(); i++)
            assertEquals(bagTmp.get(i),bagRestore.get(i));
    }
}