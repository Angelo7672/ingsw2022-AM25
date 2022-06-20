package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.StudentsListener;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.ArrayList;

/**
 * CloudsManger keeps track of all information regarding clouds with their students.
 * Notify method send changes from CloudsManager to VirtualView.
 */
public class CloudsManager{
    private final int numberOfPlayer;
    private final ArrayList<Cloud> clouds;
    private final Bag bag;
    protected StudentsListener studentsListener;

    /**
     * CloudsManager constructor, create an arrayList of Clouds.
     * @param numberOfPlayer in this game;
     * @param bag contains the student tokens to be extracted;
     */
    public CloudsManager(int numberOfPlayer, Bag bag){
        this.numberOfPlayer = numberOfPlayer;
        this.bag = bag;
        this.clouds = new ArrayList<>();

        for(int i = 0; i < numberOfPlayer; i++){
            Cloud cloud = new Cloud();
            clouds.add(cloud);
        }
    }

    /**
     * Put students on clouds.
     * Each cloud has 3 or 4 students on it, depending on the number of players.
     */
    public void refreshStudentsCloud(){
        if(numberOfPlayer == 2 || numberOfPlayer ==4 )
            for (int j = 0; j < numberOfPlayer && !bag.checkVictory(); j++)
                for (int i = 0; i < 3 && !bag.checkVictory(); i++)
                    refreshCloudStudents(bag.extraction(), j);
        else if(numberOfPlayer == 3)
            for (int j = 0; j < numberOfPlayer && !bag.checkVictory(); j++)
                for (int i = 0; i < 4 && !bag.checkVictory(); i++)
                    refreshCloudStudents(bag.extraction(), j);
    }

    /**
     * Given the index of a cloud and the color of the student extracted from the bag, add the student to the cloud.
     * @param studentExtracted colour of student extracted;
     * @param cloudIndex reference to the cloud;
     */
    private void refreshCloudStudents(int studentExtracted, int cloudIndex) {
        clouds.get(cloudIndex).incrStudents(studentExtracted);
        this.studentsListener.notifyStudentsChange(3, cloudIndex, studentExtracted,clouds.get(cloudIndex).getColour(studentExtracted));
    }

    /**
     * Restore clouds.
     * @param cloudIndex reference of cloud to restore;
     * @param students on that cloud in the last game;
     */
    public void restoreClouds(int cloudIndex, int[] students){
        for (int i = 0; i < 5; i++)
            setStudents(cloudIndex,i,students[i]);
    }

    /**
     * Increase students of a colour in a cloud.
     * @param cloudIndex reference of cloud;
     * @param colour reference of a colour;
     * @param studentsOfThisColor number of students of this colour to add to the cloud;
     */
    private void setStudents(int cloudIndex, int colour, int studentsOfThisColor){
        for(int i = 0; i < studentsOfThisColor; i++) {
            clouds.get(cloudIndex).incrStudents(colour);
            this.studentsListener.notifyStudentsChange(3, cloudIndex, colour, clouds.get(cloudIndex).getColour(colour));
        }
    }

    /**
     * Given the index of a cloud, it empties the cloud and returns the array of the students on the cloud.
     * @param cloudIndex reference of cloud;
     * @return the array of the students on the cloud;
     * @throws NotAllowedException throw in case the cloud is empty;
     */
    public int[] removeStudents(int cloudIndex) throws NotAllowedException {
        int[] students = new int[5];
        boolean checker = false;

        if(cloudIndex < 0 || cloudIndex > clouds.size()) throw new NotAllowedException();   //choose an existing cloud

        for (int i = 0; i < 5; i++) {
            if(clouds.get(cloudIndex).getColour(i) != 0) checker = true;    //check if the cloud isn't empty
            students[i] = clouds.get(cloudIndex).getColour(i);
            clouds.get(cloudIndex).removeColour(i);
            this.studentsListener.notifyStudentsChange(3, cloudIndex, i, clouds.get(cloudIndex).getColour(i));
        }

        if (!checker) throw new NotAllowedException();

        return students;
    }

    /**
     * This class contain info of a cloud with its students.
     */
    private static class Cloud{
        private final int[] students;

        /**
         * Initially the cloud is empty.
         */
        private Cloud(){ this.students = new int[]{0, 0, 0, 0, 0}; }

        private int getColour(int colour){ return students[colour]; }
        private void incrStudents(int colour){ students[colour]++; }
        private void removeColour(int colour){ students[colour] = 0; }
    }
}


