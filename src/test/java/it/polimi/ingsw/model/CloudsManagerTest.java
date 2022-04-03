package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CloudsManagerTest {

    @Test
    // tests if the number of students returned is correct
    void removeStudentsTest2Players(){

        CloudsManager cloudsManager = new CloudsManager();
        cloudsManager.setup(2);
        int studentNumber = cloudsManager.getStudentNumber();
        int[] arr = cloudsManager.removeStudents();
        int sum = 0;
        for(int i=0; i < 5; i++){
            sum = sum + arr[i];
        }
        assertEquals(studentNumber, sum);

    }

    @Test
    void removeStudentsTest3Players(){

        CloudsManager cloudsManager = new CloudsManager();
        cloudsManager.setup(3);
        int studentNumber = cloudsManager.getStudentNumber();
        int[] arr = cloudsManager.removeStudents();
        int sum = 0;
        for(int i=0; i < 5; i++){
            sum = sum + arr[i];
        }
        assertEquals(studentNumber, sum);

    }

    @Test
    void removeStudentsTest4Players(){

        CloudsManager cloudsManager = new CloudsManager();
        cloudsManager.setup(4);
        int studentNumber = cloudsManager.getStudentNumber();
        int[] arr = cloudsManager.removeStudents();
        int sum = 0;
        for(int i=0; i < 5; i++){
            sum = sum + arr[i];
        }
        assertEquals(studentNumber, sum);

    }


}