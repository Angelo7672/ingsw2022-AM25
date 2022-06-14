package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.StudentsListener;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.ArrayList;

public class CloudsManager{
    private int numberOfPlayer;
    private ArrayList<Cloud> clouds;
    private Bag bag;
    boolean lastTurn;
    protected StudentsListener studentsListener;

    // CloudsManager constructor, create an arrayList of Clouds.
    // Each cloud has 3 or 4 students on it, depending on the number of players
    public CloudsManager(int numberOfPlayer, Bag bag){
        this.numberOfPlayer = numberOfPlayer;
        this.bag = bag;
        this.lastTurn = false;   //if true, the students are finished
        this.clouds = new ArrayList<>();

        for(int i = 0; i < numberOfPlayer; i++){
            Cloud cloud = new Cloud();
            clouds.add(cloud);
        }
    }

    public boolean refreshStudentsCloud(){
        if(numberOfPlayer == 2 || numberOfPlayer ==4 ) {
            for (int j = 0; j < numberOfPlayer && !lastTurn; j++) {
                for (int i = 0; i < 3 && !lastTurn; i++) {
                    refreshCloudStudents(bag.extraction(), j);
                    lastTurn = bag.checkVictory();
                }
            }
        } else if(numberOfPlayer == 3) {
            for (int j = 0; j < numberOfPlayer && !lastTurn; j++) {
                for (int i = 0; i < 4 && !lastTurn; i++) {
                    refreshCloudStudents(bag.extraction(), j);
                    lastTurn = bag.checkVictory();
                }
            }
        }
        return lastTurn;
    }
    //given the index of a cloud and the color of the student extracted from the bag, add the student to the cloud
    private void refreshCloudStudents(int studentExtracted, int cloudIndex) {
        clouds.get(cloudIndex).incrStudents(studentExtracted);
        this.studentsListener.notifyStudentsChange(3, cloudIndex, studentExtracted,clouds.get(cloudIndex).getColour(studentExtracted));
    }

    public void restoreClouds(int cloudIndex, int[] students){
        for (int i = 0; i < 5; i++)
            setStudents(cloudIndex,i,students[i]);
    }

    private void setStudents(int cloudIndex, int colour, int studentsOfThisColor){
        for(int i = 0; i < studentsOfThisColor; i++) {
            clouds.get(cloudIndex).incrStudents(colour);
            this.studentsListener.notifyStudentsChange(3, cloudIndex, colour, clouds.get(cloudIndex).getColour(colour));
        }
    }
    // given the index of a cloud, it empties the cloud and returns the array of the students on the cloud
    public int[] removeStudents(int cloudIndex) throws NotAllowedException {
        int[] students = new int[5];
        boolean checker = false;

        if(cloudIndex < 0 || cloudIndex > clouds.size()) throw new NotAllowedException();   //choose an existing cloud

        for (int i = 0; i < 5; i++) {
            if(clouds.get(cloudIndex).getColour(i) != 0) checker = true;    //check if the cloud isn't empty
            students[i] = clouds.get(cloudIndex).getColour(i);
            clouds.get(cloudIndex).removeColour(i);
            this.studentsListener.notifyStudentsChange(3, cloudIndex, i,clouds.get(cloudIndex).getColour(i));
        }

        if (!checker) throw new NotAllowedException();

        return students;
    }

    private class Cloud{
        private int[] students;

        private Cloud(){
            this.students = new int[]{0, 0, 0, 0, 0};
        }

        private int getColour(int colour){ return students[colour]; }
        private void incrStudents(int colour){ students[colour]++; }
        private void removeColour(int colour){ students[colour] = 0; }
    }
}


