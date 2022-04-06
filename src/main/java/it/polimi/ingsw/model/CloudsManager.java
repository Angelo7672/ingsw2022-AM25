package it.polimi.ingsw.model;

import java.util.ArrayList;

public class CloudsManager /*implements GameSetup*/ {

    private ArrayList<Cloud> clouds;
    private Bag b;

    private class Cloud{
        private int[] students;
        private int studentNumber;

        // Cloud constructor
        public Cloud(int studentNumber){
            this.studentNumber = studentNumber;
            this.students = new int[]{0, 0, 0, 0, 0};
        }

        //fill the cloud with students extracted from the bag
        public void refreshCloudStudents() {
            for (int i = 0; i < studentNumber; i++)
                students[b.extraction()]++;
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

    // return the number of students on a single cloud
    public int getStudentNumber() {
        return clouds.get(0).studentNumber;
    }

    // given the index of a cloud, return the array of students on that cloud
    public int[] getStudents(int cloudIndex) {
        return clouds.get(cloudIndex).students;
    }

    // overrides GameSetup's method
    // called when the game starts, fills each cloud with students
    // @Override
    public void setup (){
        for(Cloud c: clouds){
            c.refreshCloudStudents();
        }

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


