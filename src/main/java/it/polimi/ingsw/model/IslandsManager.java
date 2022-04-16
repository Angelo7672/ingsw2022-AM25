package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class IslandsManager {

    private ArrayList<Island> islands;
    private Island i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12;
    private int motherPos;
    private Random rand;

    private class Island {

        private int[] students = {0,0,0,0,0};
        private int towerValue;
        private Team towerTeam; //-1=no one 0=white 1=black 2=grey

        Island(){
            towerValue=1;
            towerTeam=Team.NOONE;
        }

        public Team getTowerTeam(){
            return towerTeam;
        }
        public int getTowerValue(){
            return towerValue;
        }
        public void setTowerTeam(Team team){
            towerTeam=team;
        }
        public void incTowerValue(int value){
            towerValue+=value;
        }
        public int getNumStudents(int color){
            return students[color];
        }
        public void setNumStudents(int color, int nStudents){
            students[color] = nStudents;
        }
        public void incStudents(int color){
            students[color] += 1;
        }

    }

    public IslandsManager(int[] color) {
        i1 = new Island(); i2 = new Island(); i3 = new Island();
        i4 = new Island(); i5 = new Island(); i6 = new Island();
        i7 = new Island(); i8 = new Island(); i9 = new Island();
        i10 = new Island(); i11 = new Island(); i12 = new Island();
        islands = new ArrayList<>();
        islands.add(i1); islands.add(i2); islands.add(i3);
        islands.add(i4); islands.add(i5); islands.add(i6);
        islands.add(i7); islands.add(i8); islands.add(i9);
        islands.add(i10); islands.add(i11); islands.add(i12);
        rand=new Random();
        motherPos = rand.nextInt(12);

        int j=0;
        for(int i=0; i<12; i++){
            if(i!=motherPos && i!=motherPos+6){
                islands.get(i).incStudents(color[j]);
                j++;
            }
        }
    }

    public void moveMotherNature(int steps) {
        motherPos= circularArray(motherPos, steps);
    }

    public void  incStudent(int island, int color){
        islands.get(island).incStudents(color);
    }
    public int getStudent(int island, int color){
        return islands.get(island).getNumStudents(color);
    }
    public int getTowerValue(int pos){
        return islands.get(pos).getTowerValue();
    }
    public Team getTowerTeam(int pos){
        return islands.get(pos).getTowerTeam();
    }
    public void setTowerTeam(int pos, int team){
        islands.get(pos).setTowerTeam(getTeam(team));
    }

    public Team getTeam(int player){
        switch (player){
            case(-1): return Team.NOONE;
            case(0): return Team.WHITE;
            case(1): return Team.BLACK;
            case (2): return Team.GREY;
        }
        return Team.NOONE;
    }

    //metto questo solo per fare i test finchè non sistemiamo tower change
    public void setTower(Team player, int pos){
        islands.get(pos).setTowerTeam(player);
    }


    //public momentaneo
    public int size(){return islands.size();}

    //public solo per test finchè non sistemiamo l'altro
    public void checkAdjacentIslands(int pos) {
        int posTemp;
        //torre a sx
        posTemp = circularArray(pos,-1);
        checkAdjacent(pos, posTemp);
        pos = circularArray(pos,-1);
        //torre a dx
        posTemp = circularArray(pos,1);
        checkAdjacent(pos, posTemp);
    }

    private void checkAdjacent(int pos, int posTemp){
        if (islands.get(pos).getTowerTeam() == islands.get(posTemp).getTowerTeam()) {
            //sommo studenti in un isola
            for (int i = 0; i < 5; i++) {
                islands.get(pos).setNumStudents(i, islands.get(pos).getNumStudents(i) + islands.get(posTemp).getNumStudents(i));
            }
            islands.get(pos).incTowerValue(islands.get(posTemp).getTowerValue()); //incremento valore torre
            islands.remove(posTemp); //elimino un isola
            if(motherPos == posTemp) motherPos = pos; //sposto madre, nel caso ci fosse, dall'isola eliminata
        }
    }

    //true se le isole rimangono 3
    public boolean checkVictory(){
        if(islands.size()==3) return true;
        return false;
    }

    public int getMotherPos(){
        return motherPos;
    }

    //somma numeri per non uscire dall'array
    //public momentaneo
    public int circularArray(int pos, int number){
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

        public Team getTowerTeam(){ return towerTeam; }
        public int getTowerValue(){ return towerValue; }
        public void setTowerTeam(Team team){ towerTeam=team; }
        public void incTowerValue(int value){ towerValue+=value; }
        public int getNumStudents(int color){ return students[color]; }
        public void copyStudents(int color, int nStudents){ students[color] = nStudents; }
        public void incStudents(int color){ students[color]++; }
    }

}

