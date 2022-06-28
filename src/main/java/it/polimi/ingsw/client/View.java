package it.polimi.ingsw.client;

import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.server.answer.viewmessage.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class View {

    private int numberOfPlayers;
    private int turnCounter;
    private ArrayList<SchoolBoard> schoolBoards;
    private ArrayList<Island> islands;
    private ArrayList<Cloud> clouds;
    private ArrayList<Hand> hands;
    private ArrayList<Special> specials; //specials keeps the 3 special character for the game
    private ArrayList<String> cards;
    private boolean expertMode;
    private TowersListener towersListener;
    private CoinsListener coinsListener;
    private InhibitedListener inhibitedListener;
    private IslandListener islandListener;
    private MotherPositionListener motherPositionListener;
    private PlayedCardListener playedCardListener;
    private ProfessorsListener professorsListener;
    private SpecialListener specialListener;
    private NoEntryClientListener noEntryListener;
    private SpecialStudentsListener specialStudentsListener;
    private StudentsListener studentsListener;
    private WinnerListener winnerListener;
    private UserInfoListener userInfoListener;
    private RestoreCardsListener restoreCardsListener;
    private int maxStepsMotherNature;
    private int motherNaturePos;
    private String winner;
    private int specialUsed;
    private boolean initializedView;

    public View(){
        this.schoolBoards = new ArrayList<>();
        this.hands = new ArrayList<>();
        this.clouds = new ArrayList<>();
        this.islands = new ArrayList<>();
        this.specials = new ArrayList<>();
        this.cards = new ArrayList<>();
    }

    public void initializedView(int numberOfPlayers, boolean expertMode){
        this.numberOfPlayers = numberOfPlayers;
        this.expertMode = expertMode;

        initializedView = true;
        for(int i=0; i<numberOfPlayers; i++) {
            hands.add(new Hand());
            clouds.add(new Cloud());
            hands.get(i).setLastCard("");
        }
        for(int i=0; i<12; i++){
            this.islands.add(new Island());
        }
        if(numberOfPlayers==2){
            schoolBoards.add(new SchoolBoard(8,"WHITE"));
            schoolBoards.add(new SchoolBoard(8,"BLACK"));
        }
        if(numberOfPlayers==3){
            schoolBoards.add(new SchoolBoard(6,"WHITE"));
            schoolBoards.add(new SchoolBoard(6,"BLACK"));
            schoolBoards.add(new SchoolBoard(6,"GREY"));
        }
        if(numberOfPlayers==4){
            schoolBoards.add(new SchoolBoard(8,"WHITE"));
            schoolBoards.add(new SchoolBoard(0,"WHITE"));
            schoolBoards.add(new SchoolBoard(8,"BLACK"));
            schoolBoards.add(new SchoolBoard(0,"BLACK"));
        }
        cards.add("LION"); cards.add("GOOSE"); cards.add("CAT"); cards.add("EAGLE"); cards.add("FOX");
        cards.add("LIZARD"); cards.add("OCTOPUS"); cards.add("DOG"); cards.add("ELEPHANT"); cards.add("TURTLE");
    }

    //Mother
    public void setMotherPosition(int motherPosition) {
        motherNaturePos = motherPosition;
        this.motherPositionListener.notifyMotherPosition(motherPosition);
    }
    public void setMaxStepsMotherNature(int steps){
        maxStepsMotherNature = steps;
    }

    public int getMotherPosition(){return motherNaturePos;}
    public int getMaxStepsMotherNature(){return maxStepsMotherNature;}

    //Islands
    public void setIslandTowers(int islandRef, int towers) {
        islands.get(islandRef).setTowersNumber(towers);
        this.towersListener.notifyTowersChange(1, islandRef, towers);
    }
    public void setStudentsIsland(int islandRef, int color, int newValue) {
        islands.get(islandRef).setStudentsIsland(color, newValue);
        this.studentsListener.notifyStudentsChange(2, islandRef,color, newValue);
    }
    public void setTowersColor(int islandRef, int color){
        islands.get(islandRef).setTowersColor(color);
        this.towersListener.notifyTowerColor(islandRef, color);
    }
    public void removeUnifiedIsland(int islandRef){
        islands.remove(islandRef);
        this.islandListener.notifyIslandChange(islandRef);
    }
    public void setInhibited(int islandRef, int newValue) {
        islands.get(islandRef).setInhibited(newValue);
        this.inhibitedListener.notifyInhibited(islandRef,newValue);
    }

    public int getIslandSize(){return islands.size();}
    public int getIslandTowers(int islandRef){ return islands.get(islandRef).getTowersNumber();}
    public int getTowersColor(int islandRef){ return islands.get(islandRef).getTowersColor();}
    public int[] getStudentsIsland(int islandRef){ return islands.get(islandRef).getStudents();}
    public int getInhibited(int islandRef){ return islands.get(islandRef).isInhibited; }

    //School
    public void setSchoolStudents(String place, int ref, int color, int newValue){
        if(place.equalsIgnoreCase("Entrance")) {
            schoolBoards.get(ref).setStudentsEntrance(color, newValue);
            this.studentsListener.notifyStudentsChange(0, ref, color, newValue);
        }
        else if (place.equalsIgnoreCase("Table")) {
            schoolBoards.get(ref).setStudentsTable(color, newValue);
            this.studentsListener.notifyStudentsChange(1, ref, color, newValue);
        }
    }
    public void setSchoolTowers(int playerRef, int towers){
        if(towers<0) schoolBoards.get(playerRef).setTowersNumber(0);
        else if(numberOfPlayers!=4 || (playerRef!=1 && playerRef!=3)) {
            schoolBoards.get(playerRef).setTowersNumber(towers);
            this.towersListener.notifyTowersChange(0, playerRef,towers);
        }
    }
    public void setProfessors(int playerRef, int color, boolean isProfessor){
        schoolBoards.get(playerRef).setProfessors(color, isProfessor);
        this.professorsListener.notifyProfessors(playerRef,color,isProfessor);
    }
    public void setUserInfo(int playerRef, String character, String nickname) {
        schoolBoards.get(playerRef).setNickname(nickname);
        schoolBoards.get(playerRef).setCharacter(character);
        userInfoListener.userInfoNotify(nickname, character, playerRef);
    }
    public void setCoins(int playerRef, int coins){
        hands.get(playerRef).setCoins(coins);
        this.coinsListener.notifyNewCoinsValue(playerRef, hands.get(playerRef).getCoins());
    }

    public String getNickname(int playerRef){ return schoolBoards.get(playerRef).getNickname();}
    public String getCharacter(int playerRef){return schoolBoards.get(playerRef).getCharacter();}
    public int[] getStudentsEntrance(int playerRef){return schoolBoards.get(playerRef).getEntranceStudent();}
    public int[] getStudentsTable(int playerRef){return schoolBoards.get(playerRef).getTableStudent();}
    public String getTeam(int playerRef){return schoolBoards.get(playerRef).getTeam();}
    public int getSchoolTowers(int playerRef){ return schoolBoards.get(playerRef).getTowers();}
    public boolean[] getProfessors(int playerRef){ return schoolBoards.get(playerRef).getProfessors();}
    public int getCoins(int playerRef){ return hands.get(playerRef).getCoins();}

    //Clouds
    public void setClouds(int cloudRef, int color, int newValue){
        clouds.get(cloudRef).setCloudStudents(color, newValue);
        this.studentsListener.notifyStudentsChange(3, cloudRef, color, newValue);
    }

    public int[] getStudentsCloud(int cloudRef){ return clouds.get(cloudRef).getStudents();}

    //Cards
    public void setLastCard(int playerRef, String card){
        if(turnCounter == numberOfPlayers){
            turnCounter=0;
            for (int i = 0; i < numberOfPlayers; i++) {
                hands.get(i).setLastCard("");
            }
        }
        turnCounter++;
        hands.get(playerRef).setLastCard(card);
        this.playedCardListener.notifyPlayedCard(playerRef,card);
    }
    public Assistant getAssistant(String name){
        switch (name){
            case "LION" -> {
                return Assistant.LION;
            }
            case "GOOSE" -> {
                return Assistant.GOOSE;
            }
            case "CAT" -> {
                return Assistant.CAT;
            }
            case "EAGLE" -> {
                return Assistant.EAGLE;
            }
            case "FOX" -> {
                return Assistant.FOX;
            }
            case "LIZARD" -> {
                return Assistant.LIZARD;
            }
            case "OCTOPUS" -> {
                return Assistant.OCTOPUS;
            }
            case "DOG" -> {
                return Assistant.DOG;
            }
            case "ELEPHANT" -> {
                return Assistant.ELEPHANT;
            }
            case "TURTLE" -> {
                return Assistant.TURTLE;
            }
        }
        return Assistant.NONE;
    }
    public void setCards(String card){
        cards.remove(card.toUpperCase());
    }
    public void restoreCards(ArrayList<String> hand){
        ArrayList<String> cardRemoved = cards;
        cards = hand;
        cardRemoved.removeAll(cards);
        restoreCardsListener.restoreCardsNotify(cardRemoved);
    }

    public String getLastCard(int playerRef){return hands.get(playerRef).getLastCard();}
    public ArrayList<String> getCards(){ return cards; }

    //Special
    public void setSpecialUsed(int specialIndex, int playerRef){
        specialUsed = specialIndex;
        specialListener.notifySpecial(specialIndex, playerRef);
    }
    public void setSpecial(int name, int cost){
        if(specials.size()<3) {
            specials.add(new Special(cost, name));
            if(specials.size()==3) specialComplete();
        }
        else {
            int specialIndex = getSpecialIndex(name);
            specials.get(specialIndex).setCost(cost);
            specialListener.notifyIncreasedCost(specialIndex, cost);
        }
    }
    public boolean specialSet(){
        if(specials.size()==3) return true;
        return false;
    }
    public void specialComplete(){
        ArrayList<Integer> specialsName = new ArrayList<>();
        ArrayList<Integer> specialsCost = new ArrayList<>();
        ArrayList<Special> tempList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            specialsName.add(specials.get(i).getName());
        }
        Collections.sort(specialsName);
        for (int i = 0; i < 3; i++) {
            for(int j=0; j<3; j++){
                if(specialsName.get(i)==specials.get(j).getName()){
                    tempList.add(specials.get(j));
                    specialsCost.add(specials.get(j).getCost());
                    break;
                }
            }
        }
        specials = tempList;
        specialListener.notifySpecialList(specialsName, specialsCost);
    }
    public void setSpecialStudents(int color, int newValue, int special){
        int specialIndex = getSpecialIndex(special);
        specials.get(specialIndex).setStudents(color, newValue);
        specialStudentsListener.specialStudentsNotify(getSpecialName(specialIndex),color,newValue);
    }
    public void setNoEntry(int noEntry){
        int special=-1;
        for (int i = 0; i < specials.size(); i++) {
            if(specials.get(i).getName()==5){
                special=i;
                break;
            }
        }
        specials.get(special).setNoEntry(noEntry);
        noEntryListener.notifyNoEntry(special, noEntry);
    }

    public int getSpecialCost(int special){return specials.get(special).getCost();}
    public int getSpecialName(int special){return specials.get(special).getName();}
    public int[] getSpecialStudents(int special){return specials.get(special).getStudents();}
    public int getNoEntry(int special){return specials.get(special).getNoEntry();}
    public int getSpecialIndex(int special){
        int specialIndex=-1;
        for(int i=0; i<specials.size(); i++){
            if(specials.get(i).getName() == special){
                specialIndex = i;
                break;
            }
        }
        return specialIndex;
    }

    //Game info
    public int getNumberOfPlayers(){return numberOfPlayers;}
    public boolean getExpertMode(){return expertMode;}
    public String getWinner() {
        return winner;
    }
    public void setWinner(String winner) throws IOException {
        this.winner = winner;
        winnerListener.notifyWinner();
    }
    public boolean isInitializedView() {
        return initializedView;
    }

    //Listeners
    public void setUserInfoListener(UserInfoListener userInfoListener){this.userInfoListener = userInfoListener;}
    public void setTowersListener(TowersListener towersListener) {
        this.towersListener = towersListener;
    }
    public void setCoinsListener(CoinsListener coinsListener) {
        this.coinsListener = coinsListener;
    }
    public void setInhibitedListener(InhibitedListener inhibitedListener) {this.inhibitedListener = inhibitedListener;}
    public void setIslandListener(IslandListener islandListener) {
        this.islandListener = islandListener;
    }
    public void setMotherPositionListener(MotherPositionListener motherPositionListener) {this.motherPositionListener = motherPositionListener;}
    public void setPlayedCardListener(PlayedCardListener playedCardListener) {this.playedCardListener = playedCardListener;}
    public void setRestoreCardsListener(RestoreCardsListener restoreCardsListener){this.restoreCardsListener = restoreCardsListener;}
    public void setProfessorsListener(ProfessorsListener professorsListener) {this.professorsListener = professorsListener;}
    public void setSpecialListener(SpecialListener specialListener) {
        this.specialListener = specialListener;
    }
    public void setSpecialStudentsListener(SpecialStudentsListener specialStudentsListener){this.specialStudentsListener = specialStudentsListener;};
    public void setNoEntryListener(NoEntryClientListener noEntryListener){this.noEntryListener = noEntryListener;}
    public void setStudentsListener(StudentsListener studentsListener) {
        this.studentsListener = studentsListener;
    }
    public void setWinnerListener(WinnerListener winnerListener){this.winnerListener=winnerListener;}


    private class SchoolBoard {
        private String nickname;
        private String character;
        private final String team;
        private int[] studentsEntrance;
        private int[] studentsTable;
        private int towersNumber;
        private boolean[] professors;

        SchoolBoard(int towersNumber, String team){
            studentsEntrance = new int[]{0, 0, 0, 0, 0};
            studentsTable = new int[]{0, 0, 0, 0, 0};
            professors = new boolean[]{false, false, false, false, false};
            this.towersNumber = towersNumber;
            this.team = team;
            nickname ="";
            character="";
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
        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
        public void setCharacter(String character) {
            this.character = character;
        }

        public int getTowers() {
            return towersNumber;
        }
        public int[] getEntranceStudent() {
            return studentsEntrance;
        }
        public int[] getTableStudent() {
            return studentsTable;
        }
        public boolean[] getProfessors() {
            return professors;
        }
        public String getNickname(){
            return nickname;
        }
        public String getCharacter(){
            return character;
        }
        public String getTeam(){
            return team;
        }
    }

    private class Island{
        private int[] studentsIsland;
        private int towersNumber;
        private int towersColor;
        private int isInhibited;

        public Island(){
            studentsIsland=new int[]{0,0,0,0,0};
            towersColor = -1;
            towersNumber = 1;
        }

        public void setTowersNumber(int towersNumber) {
            this.towersNumber = towersNumber;
        }
        public void setStudentsIsland(int color, int newValue) {
            this.studentsIsland[color]=newValue;
        }
        public void setTowersColor(int newColor){ this.towersColor=newColor; }
        public void setInhibited(int isInhibited) {
            this.isInhibited=isInhibited;
        }

        public int[] getStudents() {return studentsIsland;}
        public int getTowersNumber(){return towersNumber;}
        public int getTowersColor(){return towersColor;}
        public int getIsInhibited(){return isInhibited;}
    }

    private class Cloud {
        private int[] students;

        public Cloud(){
            students = new int[]{0, 0, 0, 0, 0};
        }

        public void setCloudStudents(int color, int newStudentsValue) {
            students[color]=newStudentsValue;
        }
        public int[] getStudents() {
            return students;
        }
    }
    private class Hand{
        private int coins;
        private String lastPlayedCard;

        public Hand(){
            coins=1;
        }

        public void setCoins(int coins) {
            this.coins=coins;
        }
        public void setLastCard(String lastPlayedCard) {
            this.lastPlayedCard=lastPlayedCard;
        }

        public String getLastCard(){ return lastPlayedCard;}
        public int getCoins(){ return coins;}
    }

    private class Special{
        private int cost;
        private int name;
        private int[] students;
        private int noEntry;

        private Special(int cost, int name) {
            this.cost = cost;
            this.name = name;
            students = new int[]{0, 0, 0, 0, 0};
            noEntry = 4;
        }

        public int getCost() {
            return cost;
        }
        public int getName() {
            return name;
        }
        public int[] getStudents(){ return students;}
        public int getNoEntry(){
            return noEntry;
        }

        public void setCost(int cost){
            this.cost = cost;
        }
        public void setName(int name){
            this.name=name;
        }
        public void setStudents(int color, int newStudentsValue) {
            students[color]=newStudentsValue;
        }

        public void setNoEntry(int noEntry) {
            this.noEntry = noEntry;
        }
    }

}
