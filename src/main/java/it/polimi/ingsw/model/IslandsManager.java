package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.listeners.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class IslandsManager {
    private ArrayList<Island> islands;
    private Island i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12;
    private int motherPos;
    protected StudentsListener studentListener;
    protected TowersListener towersListener;
    protected MotherPositionListener motherPositionListener;
    protected IslandListener islandListener;
    protected InhibitedListener inhibitedListener;

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
    }

    public void islandsInitialize(){
        Random rand = new Random();
        motherPos = rand.nextInt(12);
        this.motherPositionListener.notifyMotherPosition(motherPos);

        ArrayList<Integer> miniBag = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            miniBag.add(i);
            miniBag.add(i);
        }
        Collections.shuffle(miniBag);
        for(int i = 0; i < 12 && !miniBag.isEmpty(); i++) {
            if (i != motherPos && i != circularArray(motherPos, 6)){
                incStudent(i,miniBag.get(0),1);
                miniBag.remove(0);
            }
        }
    }
    public void setIslandsSizeAfterRestore(int size){
        int toDelete = 12 - size;

        for(int i = 0; i < toDelete; i++) {
            islands.remove(i);
            this.islandListener.notifyIslandChange(i);
        }
    }
    public void restoreIslands(int islandRef, int[] students, int towerValue, Team towerTeam, int inhibited){
        //Students
        for(int i = 0; i < 5; i++)
            incStudent(islandRef,i,students[i]);
        //Tower
        islands.get(islandRef).incTowerValue(towerValue);
        islands.get(islandRef).setTowerTeam(towerTeam);
        this.towersListener.notifyTowersChange(2, islandRef, getTowerValue(islandRef));
        this.towersListener.notifyTowerColor(islandRef, islands.get(islandRef).getTowerTeam().getTeam());
        //Special
        for(int i = 0; i < inhibited; i++)
            increaseInhibited(islandRef);
    }
    public void restoreMotherPose(int islandRef){
        motherPos = islandRef;
        this.motherPositionListener.notifyMotherPosition(motherPos);
    }

    public int getIslandsSize(){ return islands.size(); }

    public void incStudent(int island, int color, int studentOfThisColor){
        for(int i = 0; i < studentOfThisColor; i ++) {
            islands.get(island).incStudents(color);
        }
        this.studentListener.notifyStudentsChange(2, island, color, getStudent(island, color));
    }

    public void moveMotherNature(int steps) {
        motherPos = circularArray(motherPos, steps);
        this.motherPositionListener.notifyMotherPosition(motherPos);
    }
    public int getMotherPos(){ return motherPos; }

    public int getStudent(int island, int color){ return islands.get(island).getNumStudents(color); }

    public Team getTowerTeam(int islandRef){ return islands.get(islandRef).getTowerTeam(); }
    public int getTowerValue(int islandRef){ return islands.get(islandRef).getTowerValue(); }
    public int[] towerChange(int islandRef, Team team) {
        int[] returnItem = new int[2];  //in the first cell there is the number of towers built, in the second there is the previous owner of the towers

        if(!islands.get(islandRef).getTowerTeam().equals(team)) { //if new team not equals old one
            if (islands.get(islandRef).getTowerTeam().equals(Team.NONE)) { //if old team is no one
                islands.get(islandRef).setTowerTeam(team);
                this.towersListener.notifyTowersChange(2, islandRef, getTowerValue(islandRef));
                this.towersListener.notifyTowerColor(islandRef, islands.get(islandRef).getTowerTeam().getTeam());
                checkAdjacentIslands(islandRef);
                returnItem[0] = 1;
                returnItem[1] = -1;
                return returnItem;
            }
            //if old team is not none
            returnItem[1] = islands.get(islandRef).getTowerTeam().getTeam();
            islands.get(islandRef).setTowerTeam(team);
            this.towersListener.notifyTowerColor(islandRef, islands.get(islandRef).getTowerTeam().getTeam());
            checkAdjacentIslands(islandRef);
            returnItem[0] = islands.get(islandRef).getTowerValue();
            return returnItem;
        }
        //if new team equals old one
        returnItem[0] = 0;
        returnItem[1] = -1;
        return returnItem;
    }

    private void checkAdjacentIslands(int pos) {
        int posTemp;

        posTemp = circularArray(pos,-1);    //sx tower
        if(checkAdjacent(pos, posTemp)) pos = circularArray(pos,-1);;

        posTemp = circularArray(pos,1);     //dx tower
        checkAdjacent(pos, posTemp);
    }
    private boolean checkAdjacent(int pos, int posTemp){
        if (islands.get(pos).getTowerTeam() == islands.get(posTemp).getTowerTeam()) {
            for (int i = 0; i < 5; i++) {   //move student from postemp to pos
                if(islands.get(posTemp).getNumStudents(i)!=0) {
                    islands.get(pos).copyStudents(i, islands.get(pos).getNumStudents(i) + islands.get(posTemp).getNumStudents(i));
                    this.studentListener.notifyStudentsChange(2, pos, i, islands.get(pos).getNumStudents(i));
                }
            }
            islands.get(pos).incTowerValue(islands.get(posTemp).getTowerValue()); //tower value increase
            this.towersListener.notifyTowersChange(1,pos, islands.get(pos).getTowerValue());
            islands.remove(posTemp); //island delete
            this.islandListener.notifyIslandChange(posTemp);
            if(motherPos == posTemp || motherPos == pos){ //Move mother, if was there from the eliminated island
                if(posTemp>pos) {
                    motherPos = pos;
                    this.motherPositionListener.notifyMotherPosition(motherPos);
                }
                else {
                    motherPos = pos-1;
                    this.motherPositionListener.notifyMotherPosition(motherPos);
                }

            }
            return true;
        }
        return false;
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

    public int getInhibited(int islandRef){ return islands.get(islandRef).getInhibited(); }
    public void increaseInhibited(int islandRef){
        islands.get(islandRef).increaseInhibited();
        this.inhibitedListener.notifyInhibited(islandRef, getInhibited(islandRef));
    }
    public void decreaseInhibited(int islandRef){
        islands.get(islandRef).decreaseInhibited();
        this.inhibitedListener.notifyInhibited(islandRef, getInhibited(islandRef));
    }
    public int size(){ return islands.size(); }

    private class Island {
        private int[] students;
        private int towerValue;
        private Team towerTeam;
        private int inhibited;

        private Island(){
            this.students = new int[]{0,0,0,0,0};
            this.towerValue = 1;
            this.towerTeam = Team.NONE;
            inhibited = 0;
        }

        private Team getTowerTeam(){ return towerTeam; }
        private int getTowerValue(){ return towerValue; }
        private void setTowerTeam(Team team){ towerTeam = team; }
        private void incTowerValue(int value){ towerValue+=value; }
        private int getNumStudents(int color){ return students[color]; }
        private void copyStudents(int color, int nStudents){ students[color] = nStudents; }
        private void incStudents(int color){ students[color]++; }
        private int getInhibited(){ return inhibited; }
        private void increaseInhibited(){ this.inhibited++; }
        private void decreaseInhibited(){this.inhibited--;}
    }
}