package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.listeners.*;

import java.util.ArrayList;

//virtual View class listen to changes in model classes through specific listener interfaces
public class VirtualView
        implements TowerIslandListener, TowerSchoolListener, StudentsTableListener, StudentsEntranceListener,
        StudentIslandListener, StudentsCloudListener , ProfessorsListener, PlayedSpecialListener, PlayedCardListener,
        MotherPositionListener, IslandSizeListener, CoinsListener
{

    private ArrayList<SchoolBoard> schoolBoards;
    private ArrayList<Island> islands;
    private ArrayList<Cloud> clouds;
    private ArrayList<Hand> hands;
    private ArrayList<Special> specials;


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
    public void notifyStudentsEntrance(int playerRef, int colour, int newStudentsValue) {
        schoolBoards.get(playerRef).setStudentsEntrance(colour, newStudentsValue);

    }
    @Override
    public void notifyStudentsTable(int playerRef, int colour, int newStudentsValue) {
        schoolBoards.get(playerRef).setStudentsTable(colour, newStudentsValue);
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
        hands.get(playerRef).numberOfCards--;
    }
    @Override
    public void notifyNewCoinsValue(int playerRef, int newCoinsValue) {
        hands.get(playerRef).setCoins(newCoinsValue);
    }
    @Override
    public void notifyStudentsIsland(int islandRef, int color, int newStudentValue) {
        islands.get(islandRef).setStudentsIsland(color, newStudentValue);
    }
    @Override
    public void notifyStudentsCloud(int cloudRef, int color, int newStudentValue) {
        clouds.get(cloudRef).setCloudStudents(color, newStudentValue);
    }
    @Override
    public void notifyTowersIsland(int islandRef, int towersNumber) {
        islands.get(islandRef).setTowersNumber(towersNumber);
    }
    @Override
    public void notifyTowersSchool(int playerRef, int towersNumber) {
        schoolBoards.get(playerRef).setTowersNumber(towersNumber);
    }

    @Override
    public void notifyIslandSizeChange(int islandRef, int islandToDelete) {
        islands.get(islandRef).increaseSize(islands.get(islandToDelete).getSize());
        islands.remove(islandToDelete);
    }

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
        int towerColor;
        int size; //number of island tiles that forms the island

        public int getSize() {
            return size;
        }

        public void increaseSize(int tilesToAdd){
            this.size=+tilesToAdd;
        }
        public void setTowersNumber(int towersNumber) {
            this.towersNumber = towersNumber;
        }

        public void setStudentsIsland(int colour, int newValue) {
            this.studentsIsland[colour]=newValue;
        }

        public void setMotherPosition(boolean isMotherPos) {
            this.isMotherPosition=isMotherPos;
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
    private class Special{
        String name;
        int cost;
    }

}
