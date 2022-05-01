package it.polimi.ingsw.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

//virtual View class listen to PropertyChanges in model classes
public class VirtualView implements PropertyChangeListener {

    private ArrayList<SchoolBoard> schoolBoards;
    private ArrayList<Island> islands;
    private ArrayList<Cloud> clouds;
    private ArrayList<Hand> hands;


    public VirtualView(int numberOfPlayers) {
        schoolBoards = new ArrayList<>();
        }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        if (propertyName.equals("studentsEntrance")) {
            int[] modifiedValue = (int[]) evt.getNewValue();
            schoolBoards.get(modifiedValue[5]).setStudentsEntrance(modifiedValue);
            /*it receives an array of 6 cells, the first 5 are the colours of the students - all the position are 0 except
            the one that's been modified. The last position is the reference to the player*/

        } else if (propertyName.equals("studentsTable")) {
            int[] modifiedValue = (int[]) evt.getNewValue();
            schoolBoards.get(modifiedValue[5]).setStudentsTable(modifiedValue);
        } else if (propertyName.equals(("towersInSchool"))) {
            int[] modifiedValue = (int[]) evt.getNewValue();
            schoolBoards.get(modifiedValue[1]).setTowers(modifiedValue[0]);
        } else if (propertyName.equals("professors")){
            //da finire
        }

    }
    //private class SchoolBoard keeps the state of each player's school board
    private class SchoolBoard {
        int[] studentsEntrance = new int[]{0, 0, 0, 0, 0};
        int[] studentsTable = new int[]{0, 0, 0, 0, 0};
        int towers;
        boolean[] professors = new boolean[]{false, false, false, false, false};

        public void setStudentsEntrance(int[] newEntranceValue) {
            for (int i = 0; i < 5; i++) {
                if (newEntranceValue[i] != 0) {
                    this.studentsEntrance[i] += newEntranceValue[i];
                }
            }
        }

        public void setStudentsTable(int[] newTableValue){
            for (int i = 0; i < 5; i++) {
                this.studentsTable[i] += newTableValue[i];
            }
        }

        public void setTowers(int towers) {
            this.towers = towers;
        }

        public boolean[] getProfessors() {
            return professors;
        }

        public void setProfessors(boolean[] professors) {
            this.professors = professors;
        }
    }
    private class Island{

    }
    private class Cloud{

    }
    private class Hand{

    }

}
