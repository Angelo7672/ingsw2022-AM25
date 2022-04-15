package it.polimi.ingsw.model;

import java.util.ArrayList;

public class CloudsManager /*implements GameSetup*/ {

    private ArrayList<Cloud> clouds;

    private class Cloud{
        private int[] students;
        private int studentNumber;

        // Cloud constructor
        public Cloud(int studentNumber){
            this.studentNumber = studentNumber;
            this.students = new int[]{0, 0, 0, 0, 0};
        }

    }

    // CloudsManager constructor, create an arrayList of Clouds.
    // Each cloud has 3 or 4 students on it, depending on the number of players */
    public CloudsManager(int playerNumber){
        clouds = new ArrayList<>();
        int studentsNumber;
        if(playerNumber == 3)
            studentsNumber = 4;
        else
            studentsNumber = 3;

        for(int i = 0; i < playerNumber; i++){
            Cloud cloud = new Cloud(studentsNumber);
            clouds.add(cloud);
        }
    }


    // given the index of a cloud, return the array of students on that cloud
    public int[] getStudents(int cloudIndex) {
        return clouds.get(cloudIndex).students;
    }

    //given the index of a cloud and the color of the student extracted from the bag, add the student to the cloud
    public void refreshCloudStudents(int studentExtracted, int cloudIndex) {
        clouds.get(cloudIndex).students[studentExtracted]++;

    }


    // given the index of a cloud, it empties the cloud and returns the array of the students on the cloud
    public int[] removeStudents(int cloudIndex) {

        Cloud cloud = clouds.get(cloudIndex);
        int[] students = new int[cloud.students.length];

        for (int i = 0; i < cloud.students.length; i++) {
            students[i]=clouds.get(cloudIndex).students[i];
            clouds.get(cloudIndex).students[i]=0;
        }
        return students;
    }
}

