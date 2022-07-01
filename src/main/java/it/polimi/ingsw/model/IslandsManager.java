package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * IslandManager contains all the information about islands and mother nature.
 */
public class IslandsManager {
    private final ArrayList<Island> islands;
    private int motherPos;
    protected StudentsListener studentListener;
    protected TowersListener towersListener;
    protected MotherPositionListener motherPositionListener;
    protected IslandListener islandListener;
    protected InhibitedListener inhibitedListener;

    /**
     * Constructor add 12 island in the Arraylist.
     */
    public IslandsManager() {
        islands = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            islands.add(new Island());
    }

    /**
     * Initialize islands using miniBag, an arrayList filled with two students per color.
     * Mother position is extracted randomly.
     */
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

    /**
     * Restore the right number of islands.
     * @param size is the size of the saved ArrayList.
     */
    public void setIslandsSizeAfterRestore(int size){
        int toDelete = 12 - size;

        for(int i = 0; i < toDelete; i++) {
            islands.remove(0);
            this.islandListener.notifyIslandChange(0);
        }
    }

    /**
     * Restore all the components on island.
     * @param islandRef the islands reference;
     * @param students array of students which were on the island;
     * @param towerValue number of towers which were on the island;
     * @param towerTeam Team who owned the island;
     * @param inhibited number of no entry tiles which were on island.
     */
    public void restoreIslands(int islandRef, int[] students, int towerValue, Team towerTeam, int inhibited){
        //Students
        for(int i = 0; i < 5; i++)
            incStudent(islandRef,i,students[i]);
        //Tower
        islands.get(islandRef).setTowerValue(towerValue);
        islands.get(islandRef).setTowerTeam(towerTeam);
        this.towersListener.notifyTowersChange(1, islandRef, getTowerValue(islandRef));
        this.towersListener.notifyTowerColor(islandRef, islands.get(islandRef).getTowerTeam().getTeam());
        //Special
        for(int i = 0; i < inhibited; i++)
            increaseInhibited(islandRef);
    }

    /**
     * Restore the position of mother nature.
     * @param islandRef the islands reference.
     */
    public void restoreMotherPose(int islandRef){
        motherPos = islandRef;
        this.motherPositionListener.notifyMotherPosition(motherPos);
    }

    public int getIslandsSize(){ return islands.size(); }

    /**
     * Increase the number of students on the island.
     * @param island the islands reference;
     * @param color the color reference;
     * @param studentOfThisColor number of students.
     */
    public void incStudent(int island, int color, int studentOfThisColor){
        for(int i = 0; i < studentOfThisColor; i ++)
            islands.get(island).incStudents(color);

        this.studentListener.notifyStudentsChange(2, island, color, getStudent(island, color));
    }
    public int getStudent(int island, int color){ return islands.get(island).getNumStudents(color); }

    /**
     * Move mother nature.
     * @param steps number of steps.
     */
    public void moveMotherNature(int steps) {
        motherPos = circularArray(motherPos, steps);
        this.motherPositionListener.notifyMotherPosition(motherPos);
    }
    public int getMotherPos(){ return motherPos; }

    public Team getTowerTeam(int islandRef){ return islands.get(islandRef).getTowerTeam(); }
    public int getTowerValue(int islandRef){ return islands.get(islandRef).getTowerValue(); }

    /**
     * Change the tower team on the island.
     * @param islandRef the islands reference;
     * @param team new team owner
     * @return array with in the first cell the number of towers built, in the second the previous owner of the towers
     */
    public int[] towerChange(int islandRef, Team team) {
        int[] returnItem = new int[2];  //in the first cell there is the number of towers built, in the second there is the previous owner of the towers

        if(!islands.get(islandRef).getTowerTeam().equals(team)) { //if new team not equals old one
            if (islands.get(islandRef).getTowerTeam().equals(Team.NONE)) { //if old team is no one
                islands.get(islandRef).setTowerTeam(team);
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
            int towerValue = islands.get(islandRef).getTowerValue();
            checkAdjacentIslands(islandRef);
            if(towerValue != -1) returnItem[0] = towerValue;
            else returnItem[0] = islands.get(islandRef).getTowerValue();
            return returnItem;
        }
        //if new team equals old one
        returnItem[0] = 0;
        returnItem[1] = -1;
        return returnItem;
    }

    /**
     * Check if the island on the left or/and on the right could be unified.
     * @param pos the islands reference;
     */
    private void checkAdjacentIslands(int pos) {
        int posTemp;

        posTemp = circularArray(pos,-1);    //left tower
        if(checkAdjacent(pos, posTemp)) {
            if(pos>posTemp) pos = circularArray(pos,-1);
        }

        posTemp = circularArray(pos,1);     //right tower
        if(checkAdjacent(pos, posTemp)) {
            if(pos>posTemp) pos = circularArray(pos,-1);
        }
    }

    /**
     * Check if the two islands have the same owner. In this case all the students are copied from posTemp island to pos island.
     * Then the tower of the island is increased and, if mother nature islands is greater of the unified islands, then it has to be moved one island back.
     * @param pos first islands reference;
     * @param posTemp second islands reference;
     * @return true if islands have been unified.
     */
    private boolean checkAdjacent(int pos, int posTemp){
        if (islands.get(pos).getTowerTeam() == islands.get(posTemp).getTowerTeam()) {
            for (int i = 0; i < 5; i++) {   //move student from posTemp to pos
                if(islands.get(posTemp).getNumStudents(i) != 0) {
                    islands.get(pos).copyStudents(i, islands.get(pos).getNumStudents(i) + islands.get(posTemp).getNumStudents(i));
                    this.studentListener.notifyStudentsChange(2, pos, i, islands.get(pos).getNumStudents(i));
                }
            }
            for (int i = 0; i < islands.get(posTemp).getInhibited(); i++) {
                increaseInhibited(pos);
            }
            islands.get(pos).incTowerValue(islands.get(posTemp).getTowerValue()); //tower value increase
            this.towersListener.notifyTowersChange(1,pos, islands.get(pos).getTowerValue());
            islands.remove(posTemp); //island delete
            this.islandListener.notifyIslandChange(posTemp);

            if(motherPos>=posTemp){
                motherPos = circularArray(pos, -1);
                motherPositionListener.notifyMotherPosition(motherPos);
            }
            return true;
        }
        return false;
    }

    /**
     * Game is over if there are just 3 island or less.
     * @return true if islands are 3 or less.
     */
    public boolean checkVictory() { return islands.size() <= 3; }

    /**
     * Sum to the number of island a number to not get out of the array.
     * @param pos the islands reference;
     * @param number the number to sum;
     * @return the result of the sum.
     */
    public int circularArray(int pos, int number){
        pos += number;
        if(pos >= islands.size()) pos -= islands.size();
        else if(pos < 0) pos += islands.size();
        return pos;
    }

    public int getInhibited(int islandRef){ return islands.get(islandRef).getInhibited(); }

    /**
     * Increase the number of no entry tiles on the island.
     * @param islandRef the islands reference;
     */
    public void increaseInhibited(int islandRef){
        islands.get(islandRef).increaseInhibited();
        this.inhibitedListener.notifyInhibited(islandRef, getInhibited(islandRef));
    }

    /**
     * Decrease the number of no entry tiles on the island.
     * @param islandRef the islands reference;
     */
    public void decreaseInhibited(int islandRef){
        islands.get(islandRef).decreaseInhibited();
        this.inhibitedListener.notifyInhibited(islandRef, getInhibited(islandRef));
    }

    /**
     * Class island contains all the variable about a single island.
     */
    private static class Island {
        private final int[] students;
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
        private void setTowerValue(int towerValue) { this.towerValue = towerValue; }
        private void incTowerValue(int value){ towerValue+=value; }
        private int getNumStudents(int color){ return students[color]; }
        private void copyStudents(int color, int nStudents){ students[color] = nStudents; }
        private void incStudents(int color){ students[color]++; }
        private int getInhibited(){ return inhibited; }
        private void increaseInhibited(){ this.inhibited++; }
        private void decreaseInhibited(){ this.inhibited--; }
    }
}