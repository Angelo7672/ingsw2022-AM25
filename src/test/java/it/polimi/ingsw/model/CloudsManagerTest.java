package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.BagListener;
import it.polimi.ingsw.listeners.StudentsListener;
import it.polimi.ingsw.model.exception.NotAllowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertEquals;

class CloudsManagerTest {
   CloudsManager cloudsManager2P;
   CloudsManager cloudsManager3P;
   CloudsManager cloudsManager4P;
   Bag bag;

    @BeforeEach
    void initialization(){
        bag = new Bag();
        bag.bagListener = new BagListener() {
            @Override
            public void notifyBagExtraction() {}
            @Override
            public void notifyBag(List<Integer> bag) {}
        };
        bag.bagInitialize();
        cloudsManager2P = new CloudsManager(2,bag);
        cloudsManager3P = new CloudsManager(3,bag);
        cloudsManager4P = new CloudsManager(4,bag);
        for (CloudsManager cloudsManager : Arrays.asList(cloudsManager2P, cloudsManager3P, cloudsManager4P)) {
            cloudsManager.studentsListener = new StudentsListener() {
                @Override
                public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {}
            };
        }
    }

    @Test
    @DisplayName("Tests if the number of students returned is correct for 2 players")
    void removeStudentsTest2Players() {
        int playerNumber = 2;

        cloudsManager2P.refreshStudentsCloud();
        //for each cloud, checks if the number of students returned is correct
        for (int i = 0; i < playerNumber; i++) {
            try {
                int[] arr = cloudsManager2P.removeStudents(i);
                int sum = 0;
                for (int j = 0; j < 5; j++)
                    sum = sum + arr[j];
                assertEquals(3, sum);
            }catch (NotAllowedException notAllowedException){ notAllowedException.printStackTrace(); return;}
        }
    }

    @Test
    @DisplayName("Tests if the number of students returned is correct for 3 players")
    void removeStudentsTest3Players() {
        int playerNumber = 3;

        cloudsManager3P.refreshStudentsCloud();

        //for each cloud, checks if the number of students returned is correct
        for (int i = 0; i < playerNumber; i++) {
            try {
                int[] arr = cloudsManager3P.removeStudents(i);
                int sum = 0;
                for (int j = 0; j < 5; j++)
                    sum = sum + arr[j];
                assertEquals(4, sum);
            }catch (NotAllowedException notAllowedException){ notAllowedException.printStackTrace(); return;}
        }
    }

    @Test
    @DisplayName("Tests if the number of students returned is correct for 4 players")
    void removeStudentsTest4Players() {
        int playerNumber = 4;

        cloudsManager4P.refreshStudentsCloud();

        //for each cloud, checks if the number of students returned is correct
        for (int i = 0; i < playerNumber; i++) {
            try {
                int[] arr = cloudsManager4P.removeStudents(i);
                int sum = 0;
                for (int j = 0; j < 5; j++)
                    sum = sum + arr[j];
                assertEquals(3, sum);
            }catch (NotAllowedException notAllowedException){ notAllowedException.printStackTrace(); return;}
        }
    }
}
