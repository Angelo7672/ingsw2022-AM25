package it.polimi.ingsw.client;

import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.server.answer.viewmessage.*;

import java.io.IOException;
import java.util.ArrayList;

public class View {

    private int numberOfPlayers;
    private int turnCounter;
    private ArrayList<SchoolBoard> schoolBoards;
    private ArrayList<Island> islands;
    private ArrayList<Cloud> clouds;
    private ArrayList<Hand> hands;
    private ArrayList<Special> specials; //specials keeps the 3 special character for the game
    private ArrayList<Assistant> cards;
    private boolean expertMode;
    private TowersListener towersListener;
    private CoinsListener coinsListener;
    private InhibitedListener inhibitedListener;
    private IslandListener islandListener;
    private MotherPositionListener motherPositionListener;
    private PlayedCardListener playedCardListener;
    private ProfessorsListener professorsListener;
    private SpecialListener specialListener;
    private StudentsListener studentsListener;
    private WinnerListener winnerListener;
    private DisconnectedListener disconnectedListener;
    private int maxStepsMotherNature;
    private int motherNaturePos;
    private String winner;
    private int special;
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
        cards.add(Assistant.LION); cards.add(Assistant.GOOSE); cards.add(Assistant.CAT); cards.add(Assistant.EAGLE); cards.add(Assistant.FOX);
        cards.add(Assistant.LIZARD); cards.add(Assistant.OCTOPUS); cards.add(Assistant.DOG); cards.add(Assistant.ELEPHANT); cards.add(Assistant.TURTLE);

        if(expertMode){
            for (int i=0; i<3; i++){
                specials.add(new Special(0, -1));
            }
        }
    }

    public void setIslandTowers(IslandTowersNumberMessage msg) {
        islands.get(msg.getIslandRef()).setTowersNumber(msg.getTowersNumber());
        this.towersListener.notifyTowersChange(1, msg.getIslandRef(), msg.getTowersNumber());
    }
    public void setStudentsIsland(IslandStudentMessage msg) {
        islands.get(msg.getIslandRef()).setStudentsIsland(msg.getColor(), msg.getNewValue());
        this.studentsListener.notifyStudentsChange(2, msg.getIslandRef(), msg.getColor(),msg.getNewValue());
    }
    public void setMotherPosition(MotherPositionMessage msg) {
        motherNaturePos = msg.getMotherPosition();
        this.motherPositionListener.notifyMotherPosition(msg.getMotherPosition());
    }
    public void setMaxStepsMotherNature(int steps){
        maxStepsMotherNature = steps;
    }
    public void setTowersColor(IslandTowersColorMessage msg){
        islands.get(msg.getIslandRef()).setTowersColor(msg.getColor());
        this.towersListener.notifyTowerColor(msg.getIslandRef(), msg.getColor());
    }
    public void setInhibited(InhibitedIslandMessage msg) {
        islands.get(msg.getIslandRef()).setInhibited(msg.getInhibited());
        this.inhibitedListener.notifyInhibited(msg.getIslandRef(),msg.getInhibited() );
    }
    public void removeUnifiedIsland(UnifiedIsland msg){
        islands.remove(msg.getUnifiedIsland());
        this.islandListener.notifyIslandChange(msg.getUnifiedIsland());
    }

    public void setSchoolStudents(SchoolStudentMessage msg){
        if(msg.getMessage().equalsIgnoreCase("Entrance")) {
            schoolBoards.get(msg.getComponentRef()).setStudentsEntrance(msg.getColor(), msg.getNewValue());
            this.studentsListener.notifyStudentsChange(0, msg.getComponentRef(), msg.getColor(), msg.getNewValue());
        }
        else if (msg.getMessage().equalsIgnoreCase("Table")) {
            schoolBoards.get(msg.getComponentRef()).setStudentsTable(msg.getColor(), msg.getNewValue());
            this.studentsListener.notifyStudentsChange(1, msg.getComponentRef(), msg.getColor(), msg.getNewValue());
        }
    }
    public void setSchoolTowers(SchoolTowersMessage msg){
        schoolBoards.get(msg.getPlayerRef()).setTowersNumber(msg.getTowers());
        this.towersListener.notifyTowersChange(0, msg.getPlayerRef(),msg.getTowers());
    }
    public void setProfessors(ProfessorMessage msg){
        schoolBoards.get(msg.getPlayerRef()).setProfessors(msg.getColor(), msg.isProfessor());
        this.professorsListener.notifyProfessors(msg.getPlayerRef(),msg.getColor(),msg.isProfessor());
    }
    public void setUserInfo(UserInfoAnswer msg) {
        schoolBoards.get(msg.getPlayerRef()).setNickname(msg.getNickname());
        schoolBoards.get(msg.getPlayerRef()).setCharacter(msg.getCharacter());
        //this.userInfoListener(playerRef, nickname, character);
    }

    public void setClouds(CloudStudentMessage msg){
        clouds.get(msg.getCloudRef()).setCloudStudents(msg.getColor(), msg.getNewValue());
        this.studentsListener.notifyStudentsChange(3, msg.getCloudRef(), msg.getColor(), msg.getNewValue());
    }

    public void setCoins(CoinsMessage msg){
        hands.get(msg.getPlayerRef()).setCoins(msg.getPlayerRef());
        this.coinsListener.notifyNewCoinsValue(msg.getPlayerRef(), hands.get(msg.getPlayerRef()).coins);}

    public void setLastCard(LastCardMessage msg){
        if(turnCounter == numberOfPlayers){
            turnCounter=0;
            for (int i = 0; i < numberOfPlayers; i++) {
                hands.get(i).setLastCard("");
            }
        }
        turnCounter++;
        hands.get(msg.getPlayerRef()).setLastCard(msg.getCard());
        this.playedCardListener.notifyPlayedCard(msg.getPlayerRef(),msg.getCard());
    }
    public void setNumberOfCards(NumberOfCardsMessage msg){
        hands.get(msg.getPlayerRef()).setNumberOfCards(msg.getNumberOfCards());
    }

    public void setSpecialUsed(UseSpecialAnswer msg){
        special = msg.getSpecialIndex();
        specialListener.notifySpecial(msg.getSpecialIndex(), msg.getPlayerRef());
    }

    public void setSpecials(ArrayList<Special> specials){
        this.specials = specials;
        //for(Special s: specials)
            //this.specialListener.notifySpecialName(s.getName());
    }
    public void setSpecialCost(int cost, int special){specials.get(special).setCost(cost);}
    public void setSpecialName(int name, int special){specials.get(special).setName(name);}
    public void setSpecialStudents(int color, int newValue, int special){specials.get(special).setStudents(color, newValue);}
    public void setNoEntry(int noEntry, int special){specials.get(special).setNoEntry(noEntry);}

    public void setCards(String card){
        int index=-1;
        for (int i = 0; i < cards.size(); i++) {
            if(cards.get(i).toString().equalsIgnoreCase(card)){
                index = i;
                break;
            }
        }
        setMaxStepsMotherNature(cards.get(index).getMovement());
        cards.remove(index);
    }

    public void restoreCards(ArrayList<String> hand){
        for (int i = 0; i < cards.size(); i++) {
            boolean thereIs = false;
            for(int j=0; j<hand.size(); j++){
                if(cards.get(i).toString().equals(hand.get(j))) {
                    thereIs = true;
                    break;
                }
            }
            if(!thereIs) {
                cards.remove(i);
                i--;
            }
        }
    }

    public int getIslandTowers(int islandRef){ return islands.get(islandRef).getTowersNumber();}
    public int getTowersColor(int islandRef){ return islands.get(islandRef).getTowersColor();}
    public int getMotherPosition(){return motherNaturePos;}
    public int getMaxStepsMotherNature(){return maxStepsMotherNature;}
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
    public int getSpecialName(int special){return specials.get(special).getName();}
    public int[]  getSpecialStudents(int special){return specials.get(special).getStudents();}
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

    public void setTowersListener(TowersListener towersListener) {
        this.towersListener = towersListener;
    }

    public void setCoinsListener(CoinsListener coinsListener) {
        this.coinsListener = coinsListener;
    }

    public void setInhibitedListener(InhibitedListener inhibitedListener) {
        this.inhibitedListener = inhibitedListener;
    }

    public void setIslandListener(IslandListener islandListener) {
        this.islandListener = islandListener;
    }

    public void setMotherPositionListener(MotherPositionListener motherPositionListener) {
        this.motherPositionListener = motherPositionListener;
    }

    public void setPlayedCardListener(PlayedCardListener playedCardListener) {
        this.playedCardListener = playedCardListener;
    }

    public void setProfessorsListener(ProfessorsListener professorsListener) {
        this.professorsListener = professorsListener;
    }

    public void setSpecialListener(SpecialListener specialListener) {
        this.specialListener = specialListener;
    }

    public void setStudentsListener(StudentsListener studentsListener) {
        this.studentsListener = studentsListener;
    }

    public void setWinnerListener(WinnerListener winnerListener){this.winnerListener=winnerListener;}

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
        int towersNumber;
        int towersColor;
        int isInhibited;

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
        private int name;
        private int[]  students;
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
