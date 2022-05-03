package it.polimi.ingsw.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class CloudsManager{
    private ArrayList<Cloud> clouds;
    private PropertyChangeSupport cloudsListeners = new PropertyChangeSupport(this);

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

    public void addPropertyChangeListener(PropertyChangeListener cloudsListener){
        this.cloudsListeners.addPropertyChangeListener(cloudsListener);
    }
    //add a listener to the list of listeners of this class


    // given the index of a cloud, return the array of students on that cloud
    public int[] getStudents(int cloudIndex) {
        return clouds.get(cloudIndex).getStudents();
    }

    //given the index of a cloud and the color of the student extracted from the bag, add the student to the cloud
    public void refreshCloudStudents(int studentExtracted, int cloudIndex) {
        int[] oldValue = getStudents(cloudIndex);
        clouds.get(cloudIndex).incrStudents(studentExtracted);
        this.cloudsListeners.firePropertyChange("currentCloud", null, cloudIndex);
        this.cloudsListeners.firePropertyChange("studentsCloud", oldValue, clouds.get(cloudIndex).getStudents() );
    }

    // given the index of a cloud, it empties the cloud and returns the array of the students on the cloud
    public int[] removeStudents(int cloudIndex) {
        int[] students = new int[5];

        for (int i = 0; i < 5; i++) {
            students[i]=clouds.get(cloudIndex).getColour(i);
            clouds.get(cloudIndex).removeColour(i);
        }
        this.cloudsListeners.firePropertyChange("currentCloud", null, cloudIndex);
        this.cloudsListeners.firePropertyChange("studentsCloud",students, getStudents(cloudIndex));
        return students;
    }
}


