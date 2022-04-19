package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class IslandsManager {
    private ArrayList<Island> islands;
    private Island i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12;
    private int motherPos;

    public IslandsManager() {
        i1 = new Island(); i2 = new Island(); i3 = new Island();
        i4 = new Island(); i5 = new Island(); i6 = new Island();
        i7 = new Island(); i8 = new Island(); i9 = new Island();
        i10 = new Island(); i11 = new Island(); i12 = new Island();
        islands = new ArrayList<>();
        islands.add(i1); islands.add(i2); islands.add(i3);
        islands.add(i4); islands.add(i5); islands.add(i6);
        islands.add(i7); islands.add(i8); islands.add(i9);
        islands.add(i10); islands.add(i11); islands.add(i12);

        Random rand = new Random();
        motherPos = rand.nextInt(12);

        ArrayList<Integer> miniBag = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            miniBag.add(i);
            miniBag.add(i);
        }
        Collections.shuffle(miniBag);
        for(int i = 0; i < 12 && !miniBag.isEmpty(); i++) {
            if (i != motherPos && i != motherPos + 6){
                islands.get(i).incStudents(miniBag.get(0));
                miniBag.remove(0);
            }
        }
    }

    public void incStudent(int island, int color){ islands.get(island).incStudents(color); }

    public void moveMotherNature(int steps) { motherPos = circularArray(motherPos, steps); }
    public int getMotherPos(){ return motherPos; }

    public int getStudent(int island, int color){ return islands.get(island).getNumStudents(color); }

    public Team getTowerTeam(int islandRef){ return islands.get(islandRef).getTowerTeam(); }
    public int getTowerValue(int islandRef){ return islands.get(islandRef).getTowerValue(); }
    public int[] towerChange(int islandRef, Team team) {
        int[] returnItem = new int[2];  //in the first cell there is the number of towers built, in the second there is the previous owner of the towers

        if(!islands.get(islandRef).getTowerTeam().equals(team)) {
            if (islands.get(islandRef).getTowerTeam().equals(Team.NOONE)) {
                islands.get(islandRef).setTowerTeam(team);
                checkAdjacentIslands(islandRef);
                returnItem[0] = 1;
                returnItem[1] = -1;
                return returnItem;
            }

            returnItem[1] = islands.get(islandRef).getTowerTeam().getTeam();
            islands.get(islandRef).setTowerTeam(team);
            checkAdjacentIslands(islandRef);
            returnItem[0] = islands.get(islandRef).getTowerValue();
            return returnItem;
        }

        returnItem[0] = 0;
        returnItem[1] = -1;
        return returnItem;
    }
    private void checkAdjacentIslands(int pos) {
        int posTemp;

        posTemp = circularArray(pos,-1);    //sx tower
        checkAdjacent(pos, posTemp);
        pos = circularArray(pos,-1);

        posTemp = circularArray(pos,1);     //dx tower
        checkAdjacent(pos, posTemp);
    }
    private void checkAdjacent(int pos, int posTemp){
        if (islands.get(pos).getTowerTeam() == islands.get(posTemp).getTowerTeam()) {
            for (int i = 0; i < 5; i++) {   //high student on an island
                islands.get(pos).copyStudents(i,islands.get(pos).getNumStudents(i) + islands.get(posTemp).getNumStudents(i));
            }
            islands.get(pos).incTowerValue(islands.get(posTemp).getTowerValue()); //tower value increase
            islands.remove(posTemp); //island delete
            if(motherPos == posTemp) motherPos = pos;   //I move mother, if there was one, from the eliminated island
        }
    }
    public boolean checkVictory(){
        if(islands.size()==3) return true;
        return false;
    }

    //public momentaneo
    public int circularArray(int pos, int number){  //add numbers to not leave the array
        pos += number;
        if(pos >= islands.size()) pos -= islands.size();
        else if(pos < 0) pos += islands.size();
        return pos;
    }

    private class Island {
        private int[] students;
        private int towerValue;
        private Team towerTeam;

        private Island(){
            this.students = new int[]{0,0,0,0,0};
            this.towerValue = 1;
            this.towerTeam = Team.NOONE;
        }

        private Team getTowerTeam(){ return towerTeam; }
        private int getTowerValue(){ return towerValue; }
        private void setTowerTeam(Team team){ towerTeam = team; }
        private void incTowerValue(int value){ towerValue+=value; }
        private int getNumStudents(int color){ return students[color]; }
        private void copyStudents(int color, int nStudents){ students[color] = nStudents; }
        private void incStudents(int color){ students[color]++; }
    }

}

