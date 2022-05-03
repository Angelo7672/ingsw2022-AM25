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
    private ArrayList<Special> specials;
    private int currentPlayer;
    private int currentColour;
    private int currentIsland;
    private int currentCloud;


    public VirtualView(int numberOfPlayers, int[] specials ) {
        this.schoolBoards = new ArrayList<>();
        this.hands = new ArrayList<>();
        this.clouds = new ArrayList<>();
        this.islands = new ArrayList<>();
        this.specials = new ArrayList<>();

        for(int i=0; i<numberOfPlayers; i++){
            schoolBoards.add(new SchoolBoard());
            hands.add(new Hand());
            clouds.add(new Cloud());
        }
        for(int i=0; i<12; i++){
            this.islands.add(new Island());
        }

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("currentPlayer")){
            this.currentPlayer=(int) evt.getNewValue();
        }
        else if (propertyName.equals("currentColour")){
            this.currentColour=(int) evt.getNewValue();
        }
        else if (propertyName.equals("currentIsland")){
            this.currentIsland=(int) evt.getNewValue();
        }
        else if (propertyName.equals("currentCloud")){
            this.currentCloud=(int) evt.getNewValue();
        }
        else if (propertyName.equals("studentsEntrance")) {
            schoolBoards.get(currentPlayer).setStudentsEntrance(currentColour, (int) evt.getNewValue());

        } else if (propertyName.equals("studentsTable")) {
            schoolBoards.get(currentPlayer).setStudentsTable(currentColour, (int) evt.getNewValue());

        } else if (propertyName.equals("professors")){
            schoolBoards.get(currentPlayer).setProfessors(currentColour, (boolean) evt.getNewValue()) ;

        } else if(propertyName.equals("towersSchool")) {

        } else if (propertyName.equals("coins")){
            hands.get(currentPlayer).setCoins((int) evt.getNewValue());

        } else if(propertyName.equals("studentsIsland")){
            islands.get(currentIsland).setStudentsIsland(currentColour, (int) evt.getNewValue());

        } else if(propertyName.equals("motherPosition")){
            islands.get(currentIsland).setMotherPosition(true);

        } else if(propertyName.equals("towersIslands")){

        } else if(propertyName.equals("studentsCloud")){
            clouds.get(currentCloud).setCloudStudents((int[]) evt.getNewValue());

        } else if(propertyName.equals("lastPlayedCard")){
            hands.get(currentPlayer).setLastCard((String) evt.getNewValue());

        } else if(propertyName.equals("cards")){
            hands.get(currentPlayer).setCards((int) evt.getNewValue());
        }

    }
    //private class SchoolBoard keeps the state of each player's school board
    private class SchoolBoard {
        int[] studentsEntrance = new int[]{0, 0, 0, 0, 0};
        int[] studentsTable = new int[]{0, 0, 0, 0, 0};
        int towers;
        boolean[] professors = new boolean[]{false, false, false, false, false};
        
        public void setStudentsEntrance(int color, int newValue){
            this.studentsEntrance[color]=newValue;
        }
        public void setStudentsTable(int color, int newValue) {
            this.studentsTable[color]=newValue;
        }

        public void setTowers(int towers) {
            this.towers = towers;
        }
        public void setProfessors(int color, boolean newValue) {
            this.professors[color] = newValue;
        }


    }
    private class Island{
        int[] studentsIsland= new int[]{0,0,0,0,0};
        boolean isMotherPosition;
        int towers;
        int team;

        public void setStudentsIsland(int colour, int newValue) {
            this.studentsIsland[colour]=newValue;
        }

        public void setMotherPosition(boolean isMotherPos) {
            this.isMotherPosition=isMotherPos;
        }
    }
    private class Cloud{
        int[] students= new int[]{0,0,0,0,0};

        public void setCloudStudents(int[] students) {
            for(int i=0; i<5; i++){
                this.students[i]=students[i];
            }
        }
    }
    private class Hand{
        int cards;
        int coins;
        String lastPlayedCard;

        public void setCoins(int coins) {
            this.coins=coins;
        }

        public void setLastCard(String lastPlayedCard) {
            this.lastPlayedCard=lastPlayedCard;
        }

        public void setCards(int cards) {
            this.cards=cards;
        }
    }
    private class Special{
        String name;
        int cost;
    }

}
