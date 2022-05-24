package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.listeners.*;

import java.io.Serializable;
import java.util.ArrayList;

//virtual View class listen to changes in model classes through specific listener interfaces
public class VirtualView
        implements TowersListener, ProfessorsListener, SpecialListener, PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener, Serializable {

    private ArrayList<SchoolBoard> schoolBoards;
    private ArrayList<Island> islands;
    private ArrayList<Cloud> clouds;
    private ArrayList<Hand> hands;
    private ArrayList<Integer> specials; //specials keeps the 3 special character for the game
    private ArrayList<String> playedCards;
    private int numberOfPlayers;

    public VirtualView(int numberOfPlayers) {
        this.schoolBoards = new ArrayList<>();
        this.hands = new ArrayList<>();
        this.clouds = new ArrayList<>();
        this.islands = new ArrayList<>();
        this.specials = new ArrayList<>();
        this.playedCards = new ArrayList<>();
        this.numberOfPlayers=numberOfPlayers;

        for(int i=0; i<12; i++)
            this.islands.add(new Island());
    }

    public String getLastPlayedCard(int playerRef){ return hands.get(playerRef).lastPlayedCard; }
    public String getNickname(int playerRef){ return this.schoolBoards.get(playerRef).nickname; }
    public String getCharacter(int playerRef){ return this.schoolBoards.get(playerRef).nickname; }
    public void addNewPlayer(String nickname, String character){
        int player;
        schoolBoards.add(new SchoolBoard(nickname,character));
        hands.add(new Hand());
        clouds.add(new Cloud());

        player = schoolBoards.size()-1;
        if(numberOfPlayers==3) {
            schoolBoards.get(player).setTowersNumber(6);
            schoolBoards.get(player).setTeam(player);
        } else if(numberOfPlayers==4){
            if(player==0 || player== 2)
                schoolBoards.get(player).setTowersNumber(8);
            else schoolBoards.get(player).setTowersNumber(0);
            if(player==0 || player==1)
                schoolBoards.get(player).setTeam(0);
            else schoolBoards.get(player).setTeam(1);
        } else {
            schoolBoards.get(player).setTowersNumber(8);
            schoolBoards.get(player).setTeam(player);
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
    public void notifyIslandChange(int islandToDelete) {
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
        String nickname;
        String character;
        int team; //0: white, 1: black, 2:grey

        int[] studentsEntrance;
        int[] studentsTable;
        int towersNumber;
        boolean[] professors;

        public SchoolBoard(String nickname, String character){
            this.nickname=nickname;
            this.character=character;
            this.studentsEntrance = new int[]{0, 0, 0, 0, 0};
            this.studentsTable  = new int[]{0, 0, 0, 0, 0};
            this.professors = new boolean[]{false, false, false, false, false};

        }
        
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
        public void setTeam(int team) {
            this.team=team;
        }
    }
    private class Island{
        int[] studentsIsland;
        boolean isMotherPosition;
        int towersNumber;
        int towersColor;
        int isInhibited;


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
        int[] students;

        public Cloud(){
            students = new int[]{0, 0, 0, 0, 0};
        }
        public void setCloudStudents(int color, int newStudentsValue) {
            students[color]=newStudentsValue;
        }
    }
    private class Hand{
        int numberOfCards;
        int coins;
        String lastPlayedCard;

        public Hand(){
            numberOfCards=10;
            coins=1;
        }

        public void setCoins(int coins) {
            this.coins=coins;
        }

        public void setLastCard(String lastPlayedCard) {
            this.lastPlayedCard=lastPlayedCard;
        }
        public String getLastCard(){ return lastPlayedCard;}

        public void setNumberOfCards(int numberOfCards) {
            this.numberOfCards=numberOfCards;
        }
    }

}
