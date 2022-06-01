package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.listeners.StudentsListener;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.ArrayList;

public class CloudsManager{
    private ArrayList<Cloud> clouds;
    protected StudentsListener studentsListener;

    private class Cloud{
        private int[] students;

        private Cloud(){
            this.students = new int[]{0, 0, 0, 0, 0};
        }

        private int[] getStudents(){ return students; }
        private int getColour(int colour){ return students[colour]; }
        private void incrStudents(int colour){ students[colour]++; }
        private void removeColour(int colour){ students[colour] = 0; }
    }

    // CloudsManager constructor, create an arrayList of Clouds.
    // Each cloud has 3 or 4 students on it, depending on the number of players
    public CloudsManager(int playerNumber){
        clouds = new ArrayList<>();

        for(int i = 0; i < playerNumber; i++){
            Cloud cloud = new Cloud();
            clouds.add(cloud);
        }
    }

    // given the index of a cloud, return the array of students on that cloud
    public int[] getStudents(int cloudIndex) {
        return clouds.get(cloudIndex).getStudents();
    }

    //given the index of a cloud and the color of the student extracted from the bag, add the student to the cloud
    public void refreshCloudStudents(int studentExtracted, int cloudIndex) {
        clouds.get(cloudIndex).incrStudents(studentExtracted);
        this.studentsListener.notifyStudentsChange(3, cloudIndex, studentExtracted,clouds.get(cloudIndex).getColour(studentExtracted));
    }

    // given the index of a cloud, it empties the cloud and returns the array of the students on the cloud
    public int[] removeStudents(int cloudIndex) throws NotAllowedException {
        int[] students = new int[5];
        boolean checker = false;

        if(cloudIndex < 0 || cloudIndex > clouds.size()) throw new NotAllowedException();   //choose an existing cloud

        for (int i = 0; i < 5; i++) {
            if(clouds.get(cloudIndex).getColour(i) != 0) checker = true;
            students[i]=clouds.get(cloudIndex).getColour(i);
            clouds.get(cloudIndex).removeColour(i);
            this.studentsListener.notifyStudentsChange(3, cloudIndex, i,clouds.get(cloudIndex).getColour(i));
        }

        if (!checker) throw new NotAllowedException();

        return students;
    }
}


