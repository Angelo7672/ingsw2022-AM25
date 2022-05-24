package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.server.Answer.UnifiedIsland;
import it.polimi.ingsw.server.Answer.ViewMessage.*;

import java.util.ArrayList;

public class View {

    int numberOfPlayers;
    private ArrayList<SchoolBoard> schoolBoards;
    private ArrayList<Island> islands;
    private ArrayList<Cloud> clouds;
    private ArrayList<Hand> hands;
    private ArrayList<Integer> specials; //specials keeps the 3 special character for the game
    private ArrayList<String> playedCards; //cose'è?
    private ArrayList<Assistant> cards;

    public View(int numberOfPlayers){
        this.numberOfPlayers = numberOfPlayers;
        this.schoolBoards = new ArrayList<>();
        this.hands = new ArrayList<>();
        this.clouds = new ArrayList<>();
        this.islands = new ArrayList<>();
        this.specials = new ArrayList<>();
        this.playedCards = new ArrayList<>();
        this.cards = new ArrayList<>();

        for(int i=0; i<numberOfPlayers; i++) {
            hands.add(new Hand());
            clouds.add(new Cloud());

        }
        for(int i=0; i<12; i++){
            this.islands.add(new Island());
        }
        if(numberOfPlayers==2){
            for(int i=0; i<numberOfPlayers; i++){
                schoolBoards.add(new SchoolBoard(8,i));
            }
        }
        if(numberOfPlayers==3){
            for(int i=0; i<numberOfPlayers; i++){
                schoolBoards.add(new SchoolBoard(6,i));
            }
        }
        if(numberOfPlayers==4){
            schoolBoards.add(new SchoolBoard(8,0));
            schoolBoards.add(new SchoolBoard(0,0));
            schoolBoards.add(new SchoolBoard(8,1));
            schoolBoards.add(new SchoolBoard(0,1));
        }
        cards.add(Assistant.LION); cards.add(Assistant.GOOSE); cards.add(Assistant.CAT); cards.add(Assistant.EAGLE); cards.add(Assistant.FOX);
        cards.add(Assistant.LION); cards.add(Assistant.OCTOPUS); cards.add(Assistant.DOG); cards.add(Assistant.ELEPHANT); cards.add(Assistant.TURTLE);
    }

    public void setIslandTowers(IslandTowersNumberMessage msg) { islands.get(msg.getIslandRef()).setTowersNumber(msg.getTowersNumber());}
    public void setStudentsIsland(IslandStudentMessage msg) { islands.get(msg.getIslandRef()).setStudentsIsland(msg.getColor(), msg.getNewValue()); }
    public void setMotherPosition(MotherPositionMessage msg) {
        islands.get(msg.getIslandRef()).setMotherPosition(msg.isMotherPosition());
    }
    public void setTowersColor(IslandTowersColorMessage msg){ islands.get(msg.getIslandRef()).setTowersColor(msg.getColor()); }
    public void setInhibited(InhibitedIslandMessage msg) {
        islands.get(msg.getIslandRef()).setInhibited(msg.getInhibited());
    }
    public void removeUnifiedIsland(UnifiedIsland msg){
        islands.remove(msg.getUnifiedIsland());
    }

    public void setStudents(SchoolStudentMessage msg){
        if(msg.getMessage().equalsIgnoreCase("Entrance")) schoolBoards.get(msg.getPlayerRef()).setStudentsEntrance(msg.getColor(), msg.getNewValue());
        else if (msg.getMessage().equalsIgnoreCase("Table")) schoolBoards.get(msg.getPlayerRef()).setStudentsTable(msg.getColor(), msg.getNewValue());
    }
    public void setSchoolTowers(SchoolTowersMessage msg){ schoolBoards.get(msg.getPlayerRef()).setTowersNumber(msg.getTowers());}
    public void setProfessors(ProfessorMessage msg){ schoolBoards.get(msg.getPlayerRef()).setProfessors(msg.getColor(), msg.isProfessor());}
    public void setNickname(NicknameMessage msg) { schoolBoards.get(msg.getPlayerRef()).setNickname(msg.getMessage());}
    public void setWizard(WizardMessage msg){ schoolBoards.get(msg.getPlayerRef()).setWizard(msg.getMessage());}

    public void setClouds(CloudStudentMessage msg){ clouds.get(msg.getCloudRef()).setCloudStudents(msg.getColor(), msg.getNewValue()); }

    public void setCoins(CoinsMessage msg){ hands.get(msg.getPlayerRef()).setCoins(msg.getPlayerRef());}
    public void setLastCard(LastCardMessage msg){
        hands.get(msg.getPlayerRef()).setLastCard(msg.getCard());
        cards.remove(msg.getCard());
    }
    public void setNumberOfCards(NumberOfCardsMessage msg){hands.get(msg.getPlayerRef()).setNumberOfCards(msg.getNumberOfCards());}

    public void setSpecials(ArrayList<Integer> specials){
        this.specials = specials;
    }

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
    public String getWizard(int playerRef){return schoolBoards.get(playerRef).getWizard();}
    public int[] getStudentsEntrance(int playerRef){return schoolBoards.get(playerRef).getEntranceStudent();}
    public int[] getStudentsTable(int playerRef){return schoolBoards.get(playerRef).getTableStudent();}
    public int getTeam(int playerRef){return schoolBoards.get(playerRef).getTeam();}
    public int getSchoolTowers(int playerRef){ return schoolBoards.get(playerRef).getTowers();}
    public boolean[] getProfessors(int playerRef){ return schoolBoards.get(playerRef).getProfessors();}

    public int[] getStudentsCloud(int cloudRef){ return clouds.get(cloudRef).getStudents();}

    public int getCoins(int playerRef){ return hands.get(playerRef).getCoins();}
    public String getLastCard(int playerRef){return hands.get(playerRef).getLastCard();}
    public int getNumberOfCards(int playerRef){return hands.get(playerRef).getNumberOfCards();}

    public ArrayList<Assistant> getCards(){ return cards; }

    public int getIslandSize(){return islands.size();}
    public int getNumberOfPlayers(){return numberOfPlayers;}


    private class SchoolBoard {
        String nickname;
        String wizard;
        int team;
        int[] studentsEntrance;
        int[] studentsTable;
        int towersNumber;
        boolean[] professors;

        SchoolBoard(int towersNumber, int team){
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
        public void setWizard(String wizard) {
            this.wizard = wizard;
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
        public String getWizard(){
            return wizard;
        }
        public int getTeam(){
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

}
