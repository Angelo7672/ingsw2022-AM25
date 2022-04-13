package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class CloudsManagerTest {

    @Test
    // tests if the number of students returned is correct
    void removeStudentsTest2Players() {

        int playerNumber = 2;
        int color = 1;

        //inizializza le nuvole
        CloudsManager cloudsManager = new CloudsManager(playerNumber);
        for (int j = 0; j < playerNumber; j++) {
            for (int i = 0; i < 3; i++) {
                cloudsManager.refreshCloudStudents(color, j);
            }
        }
        // per ogni nuvola controlla se il numero di studenti nell'array restituito è corretto
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
   /* @Test
    void removeStudentsTest3Players(){

        int playerNumber = 3;
        CloudsManager cloudsManager = new CloudsManager(playerNumber);
        cloudsManager.setup();
        for(int i=0; i < playerNumber; i++){

            int[] arr = cloudsManager.removeStudents(i);
            int sum = 0;
            for(int j=0; j < 5; j++) {
                sum = sum + arr[j];
            }
            assertEquals(cloudsManager.getStudentNumber(), sum);
        }
    }
    @Test
    void removeStudentsTest4Players(){

        int playerNumber = 4;
        CloudsManager cloudsManager = new CloudsManager(playerNumber);
        cloudsManager.setup();
        for(int i=0; i < playerNumber; i++){

            int[] arr = cloudsManager.removeStudents(i);
            int sum = 0;
            for(int j=0; j < 5; j++) {
                sum = sum + arr[j];
            }
            assertEquals(cloudsManager.getStudentNumber(), sum);
        }
    }
}
*/
