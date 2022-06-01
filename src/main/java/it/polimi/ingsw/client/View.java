package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.listeners.*;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.server.Answer.ViewMessage.UnifiedIsland;
import it.polimi.ingsw.server.Answer.ViewMessage.*;

import java.util.ArrayList;

public class View {

    private final int numberOfPlayers;
    private ArrayList<SchoolBoard> schoolBoards;
    private ArrayList<Island> islands;
    private ArrayList<Cloud> clouds;
    private ArrayList<Hand> hands;
    private ArrayList<Special> specials; //specials keeps the 3 special character for the game
    private ArrayList<Assistant> cards;
    private final boolean expertMode;
    private TowersListener towersListener;
    private CoinsListener coinsListener;
    private InhibitedListener inhibitedListener;
    private IslandListener islandListener;
    private MotherPositionListener motherPositionListener;
    private PlayedCardListener playedCardListener;
    private ProfessorsListener professorsListener;
    private QueueListener queueListener;
    private SpecialListener specialListener;
    private StudentsListener studentsListener;
    private BagListener bagListener;

    public View(int numberOfPlayers, boolean expertMode){
        this.numberOfPlayers = numberOfPlayers;
        this.expertMode = expertMode;
        this.schoolBoards = new ArrayList<>();
        this.hands = new ArrayList<>();
        this.clouds = new ArrayList<>();
        this.islands = new ArrayList<>();
        this.specials = new ArrayList<>();
        this.cards = new ArrayList<>();

        for(int i=0; i<numberOfPlayers; i++) {
            hands.add(new Hand());
            clouds.add(new Cloud());
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
        cards.add(Assistant.LION); cards.add(Assistant.GOOSE); cards.add(Assistant.CAT); cards.add(Assistant.EAGLE); cards.add(Assistant.FOX);
        cards.add(Assistant.LIZARD); cards.add(Assistant.OCTOPUS); cards.add(Assistant.DOG); cards.add(Assistant.ELEPHANT); cards.add(Assistant.TURTLE);

        if(expertMode){
            for (int i=0; i<3; i++){
                specials.add(new Special(0, ""));
            }
        }
    }

    public void setIslandTowers(IslandTowersNumberMessage msg) { islands.get(msg.getIslandRef()).setTowersNumber(msg.getTowersNumber());}
    public void setStudentsIsland(IslandStudentMessage msg) { islands.get(msg.getIslandRef()).setStudentsIsland(msg.getColor(), msg.getNewValue()); }
    public void setMotherPosition(MotherPositionMessage msg) {
        islands.get(getMotherPosition()).setMotherPosition(false);
        islands.get(msg.getMotherPosition()).setMotherPosition(true);
    }
    public void setTowersColor(IslandTowersColorMessage msg){ islands.get(msg.getIslandRef()).setTowersColor(msg.getColor()); }
    public void setInhibited(InhibitedIslandMessage msg) {
        islands.get(msg.getIslandRef()).setInhibited(msg.getInhibited());
    }
    public void removeUnifiedIsland(UnifiedIsland msg){
        islands.remove(msg.getUnifiedIsland());
    }

    public void setStudents(SchoolStudentMessage msg){
        if(msg.getMessage().equalsIgnoreCase("Entrance")) schoolBoards.get(msg.getComponentRef()).setStudentsEntrance(msg.getColor(), msg.getNewValue());
        else if (msg.getMessage().equalsIgnoreCase("Table")) schoolBoards.get(msg.getComponentRef()).setStudentsTable(msg.getColor(), msg.getNewValue());
    }
    public void setSchoolTowers(SchoolTowersMessage msg){ schoolBoards.get(msg.getPlayerRef()).setTowersNumber(msg.getTowers());}
    public void setProfessors(ProfessorMessage msg){ schoolBoards.get(msg.getPlayerRef()).setProfessors(msg.getColor(), msg.isProfessor());}
    public void setUserInfo(UserInfoAnswer msg) {
        schoolBoards.get(msg.getPlayerRef()).setNickname(msg.getNickname());
        schoolBoards.get(msg.getPlayerRef()).setCharacter(msg.getCharacter());
    }

    public void setClouds(CloudStudentMessage msg){ clouds.get(msg.getCloudRef()).setCloudStudents(msg.getColor(), msg.getNewValue()); }

    public void setCoins(CoinsMessage msg){ hands.get(msg.getPlayerRef()).setCoins(msg.getPlayerRef());}
    public void setLastCard(LastCardMessage msg){
        hands.get(msg.getPlayerRef()).setLastCard(msg.getCard());
        cards.remove(msg.getCard());
    }
    public void setNumberOfCards(NumberOfCardsMessage msg){hands.get(msg.getPlayerRef()).setNumberOfCards(msg.getNumberOfCards());}

    public void setSpecials(ArrayList<Special> specials){
        this.specials = specials;
    }
    public void setSpecialCost(int cost, int special){specials.get(special).setCost(cost);}
    public void setSpecialName(String name, int special){specials.get(special).setName(name);}
    public void setSpecialStudents(int color, int newValue, int special){specials.get(special).setStudents(color, newValue);}
    public void setNoEntry(int noEntry, int special){specials.get(special).setNoEntry(noEntry);}

    public void setCards(Assistant card){cards.remove(card);}

    public int getIslandTowers(int islandRef){ return islands.get(islandRef).getTowersNumber();}
    public int getTowersColor(int islandRef){ return islands.get(islandRef).getTowersColor();}
    public Boolean isMotherPosition(int islandRef){ return islands.get(islandRef).isMother();}
    public int getMotherPosition(){
        for (int i = 0; i < getIslandSize(); i++) {
            if(islands.get(i).isMother()) return i+1;
        }
        return 0;
    }
    public int[] getStudentsIsland(int islandRef){ return islands.get(islandRef).getStudents();}
    public int getInhibited(int islandRef){ return islands.get(islandRef).isInhibited; }

    public String getNickname(int playerRef){ return schoolBoards.get(playerRef).getNickname();}
    public String getCharacter(int playerRef){return schoolBoards.get(playerRef).getCharacter();}
    public int[] getStudentsEntrance(int playerRef){return schoolBoards.get(playerRef).getEntranceStudent();}
    public int[] getStudentsTable(int playerRef){return schoolBoards.get(playerRef).getTableStudent();}
    public String getTeam(int playerRef){return schoolBoards.get(playerRef).getTeam();}
    public int getSchoolTowers(int playerRef){ return schoolBoards.get(playerRef).getTowers();}
    public boolean[] getProfessors(int playerRef){ return schoolBoards.get(playerRef).getProfessors();}

    public int[] getStudentsCloud(int cloudRef){ return clouds.get(cloudRef).getStudents();}

    public int getCoins(int playerRef){ return hands.get(playerRef).getCoins();}
    public String getLastCard(int playerRef){return hands.get(playerRef).getLastCard();}
    public int getNumberOfCards(int playerRef){return hands.get(playerRef).getNumberOfCards();}

    public ArrayList<Assistant> getCards(){ return cards; }

    public int getIslandSize(){return islands.size();}
    public int getNumberOfPlayers(){return numberOfPlayers;}
    public boolean getExpertMode(){return expertMode;}

    public int getSpecialCost(int special){return specials.get(special).getCost();}
    public String getSpecialName(int special){return specials.get(special).getName();}
    public int[]  getSpecialStudents(int special){return specials.get(special).getStudents();}
    public int getNoEntry(int special){return specials.get(special).getNoEntry();}


    private class SchoolBoard {
        String nickname;
        String character;
        final String team;
        int[] studentsEntrance;
        int[] studentsTable;
        int towersNumber;
        boolean[] professors;

        SchoolBoard(int towersNumber, String team){
            studentsEntrance = new int[]{0, 0, 0, 0, 0};
            studentsTable = new int[]{0, 0, 0, 0, 0};
            professors = new boolean[]{false, false, false, false, false};
            this.towersNumber = towersNumber;
            this.team = team;
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
        int[] studentsIsland;
        boolean isMotherPosition;
        int towersNumber;
        int towersColor;
        int isInhibited;

        public Island(){
            studentsIsland=new int[]{0,0,0,0,0};
            towersColor = -1;
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

        public int[] getStudents() {return studentsIsland;}
        public boolean isMother() {return isMotherPosition;}
        public int getTowersNumber(){return towersNumber;}
        public int getTowersColor(){return towersColor;}
        public int getIsInhibited(){return isInhibited;}
    }
    private class Cloud {
        int[] students;

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
        public void setNumberOfCards(int numberOfCards) {
            this.numberOfCards=numberOfCards;
        }

        public String getLastCard(){ return lastPlayedCard;}
        public int getCoins(){ return coins;}
        public int getNumberOfCards(){ return numberOfCards; }
    }

    private class Special{
        private int cost;
        private String name;
        private int[]  students;
        private int noEntry;

        private Special(int cost, String name) {
            this.cost = cost;
            this.name = name;
            students = new int[]{0, 0, 0, 0, 0};
            noEntry = 4;
        }

        public int getCost() {
            return cost;
        }
        public String getName() {
            return name;
        }
        public int[] getStudents(){ return students;}
        public int getNoEntry(){
            return noEntry;
        }

        public void setCost(int cost){
            this.cost = cost;
        }
        public void setName(String name){
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
