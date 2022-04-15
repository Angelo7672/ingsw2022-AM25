package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.Assert.assertEquals;

class CloudsManagerTest {

    @Test
    @DisplayName("Tests if the number of students returned is correct")
    void removeStudentsTest2Players() {
        int playerNumber = 2;
        int color = 1;
        int[] array=new int[]{0,3,0,0,0};

        //fill the clouds
        CloudsManager cloudsManager = new CloudsManager(playerNumber);
        for (int j = 0; j < playerNumber; j++) {
            for (int i = 0; i < 3; i++) {
                cloudsManager.refreshCloudStudents(color, j);
            }
        }
        assertAll(
                ()->assertArrayEquals(array,cloudsManager.getStudents(0),"Arrays must be equal"),
                ()->assertArrayEquals(array,cloudsManager.getStudents(1),"Arrays must be equal")
        );

        //for each cloud, checks if the number of students returned is correct
        for (int i = 0; i < playerNumber; i++) {

            int[] arr = cloudsManager.removeStudents(i);
            int sum = 0;
            for (int j = 0; j < 5; j++) {
                sum = sum + arr[j];
            }
            assertEquals(3, sum);
        }
    }


    @Test
    // tests if the number of students returned is correct
    void removeStudentsTest3Players() {

        int playerNumber = 3;
        int color = 2;

        CloudsManager cloudsManager = new CloudsManager(playerNumber);
        for (int j = 0; j < playerNumber; j++) {
            for (int i = 0; i < 4; i++) {
                cloudsManager.refreshCloudStudents(color, j);
            }
        }

        for (int i = 0; i < playerNumber; i++) {

            int[] arr = cloudsManager.removeStudents(i);
            int sum = 0;
            for (int j = 0; j < 5; j++) {
                sum = sum + arr[j];
            }
            assertEquals(4, sum);
        }
    }


    @Test
    // tests if the number of students returned is correct
    void removeStudentsTest4Players() {
        int playerNumber = 4;
        int color = 3;


        CloudsManager cloudsManager = new CloudsManager(playerNumber);
        for (int j = 0; j < playerNumber; j++) {
            for (int i = 0; i < 3; i++) {
                cloudsManager.refreshCloudStudents(color, j);
            }
        }

        for (int i = 0; i < playerNumber; i++) {

            int[] arr = cloudsManager.removeStudents(i);
            int sum = 0;
            for (int j = 0; j < 5; j++) {
                sum = sum + arr[j];
            }
            assertEquals(3, sum);
        }
    }
}
