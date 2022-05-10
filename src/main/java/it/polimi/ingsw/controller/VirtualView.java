package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.listeners.*;

import java.util.ArrayList;

//virtual View class listen to changes in model classes through specific listener interfaces
public class VirtualView
        implements TowersListener, ProfessorsListener, SpecialListener, PlayedCardListener,
        MotherPositionListener, IslandSizeListener, CoinsListener, StudentsListener, InhibitedListener
{

    private ArrayList<SchoolBoard> schoolBoards;
    private ArrayList<Island> islands;
    private ArrayList<Cloud> clouds;
    private ArrayList<Hand> hands;
    private ArrayList<Integer> specials; //specials keeps the 3 special character for the game



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
    public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {
        if(place==0){
            schoolBoards.get(componentRef).setStudentsEntrance(color, newStudentsValue);
        }
        else if(place==1){
            schoolBoards.get(componentRef).setStudentsTable(componentRef, newStudentsValue);
        }
        else if(place==2){
            islands.get(componentRef).setStudentsIsland(color, newStudentsValue);
        }
        else if(place==3){
            clouds.get(componentRef).setCloudStudents(color, newStudentsValue);
        }
    }

    @Override
    public void notifyProfessors(int playerRef, int color, boolean newProfessorValue) {
        schoolBoards.get(playerRef).setProfessors(color, newProfessorValue);
    }

    @Override
    public void notifyMotherPosition(int newMotherPosition) {
        islands.get(newMotherPosition).setMotherPosition(true);
    }
    @Override
    public void notifyPlayedCard(int playerRef, String assistantCard) {
        hands.get(playerRef).setLastCard(assistantCard);
        hands.get(playerRef).setNumberOfCards(hands.get(playerRef).numberOfCards--);
    }
    @Override
    public void notifyNewCoinsValue(int playerRef, int newCoinsValue) {
        hands.get(playerRef).setCoins(newCoinsValue);
    }

    @Override
    public void notifyIslandSizeChange(int islandRef, int islandToDelete) {
        islands.get(islandRef).increaseSize(islands.get(islandToDelete).getSize());
        islands.remove(islandToDelete);
    }

    @Override
    public void notifyTowersChange(int place, int componentRef, int towersNumber) {
        if (place == 0) {
            schoolBoards.get(componentRef).setTowersNumber(towersNumber);
        } else if (place == 1) {
            islands.get(componentRef).setTowersNumber(towersNumber);
        }
    }

    @Override
    public void notifyTowerColor(int islandRef, int newColor) {
        islands.get(islandRef).setTowersColor(newColor);
    }

    @Override
    public void notifyInhibited(int islandRef, int isInhibited) {
        islands.get(islandRef).setInhibited(isInhibited);
    }

    @Override
    public void notifySpecial(int specialRef) { specials.add(specialRef);}


    //private class SchoolBoard keeps the state of each player's school board
    private class SchoolBoard {
        int[] studentsEntrance = new int[]{0, 0, 0, 0, 0};
        int[] studentsTable = new int[]{0, 0, 0, 0, 0};
        int towersNumber;
        boolean[] professors = new boolean[]{false, false, false, false, false};
        int team;
        
        public void setStudentsEntrance(int color, int newValue){
            this.studentsEntrance[color]=newValue;
        }
        public void setStudentsTable(int color, int newValue) {
            this.studentsTable[color]=newValue;
        }

        public void setTowersNumber(int towersNumber) {
            this.towersNumber = towersNumber;
        }
        public void setProfessors(int color, boolean newValue) {
            this.professors[color] = newValue;
        }


    }
    private class Island{
        int[] studentsIsland= new int[]{0,0,0,0,0};
        boolean isMotherPosition;
        int towersNumber;
        int towersColor;
        int size; //number of island tiles that forms the island
        int isInhibited;

        public int getSize() {
            return size;
        }

        public void increaseSize(int tilesToAdd){
            this.size=+tilesToAdd;
        }
        public void setTowersNumber(int towersNumber) {
            this.towersNumber = towersNumber;
        }

        public void setStudentsIsland(int color, int newValue) {
            this.studentsIsland[color]=newValue;
        }

        public void setMotherPosition(boolean isMotherPos) {
            this.isMotherPosition=isMotherPos;
        }
        public void setTowersColor(int newColor){ this.towersColor=newColor; }

        public void setInhibited(int isInhibited) {
            this.isInhibited=isInhibited;
        }
    }
    private class Cloud {
        int[] students = new int[]{0, 0, 0, 0, 0};

        public void setCloudStudents(int color, int newStudentsValue) {
            students[color]=newStudentsValue;
        }
    }
    private class Hand{
        int numberOfCards;
        int coins;
        String lastPlayedCard;

        public void setCoins(int coins) {
            this.coins=coins;
        }

        public void setLastCard(String lastPlayedCard) {
            this.lastPlayedCard=lastPlayedCard;
        }

        public void setNumberOfCards(int numberOfCards) {
            this.numberOfCards=numberOfCards;
        }
    }

}
