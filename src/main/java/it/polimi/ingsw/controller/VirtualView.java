package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.listeners.*;
import it.polimi.ingsw.server.ControllerServer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

//virtual View class listen to changes in model classes through specific listener interfaces
public class VirtualView
        implements TowersListener, ProfessorsListener, SpecialListener, PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener, BagListener,
        QueueListener, Serializable {

    private ArrayList<SchoolBoard> schoolBoards;
    private ArrayList<Island> islands;
    private ArrayList<Cloud> clouds;
    private ArrayList<Hand> hands;
    private ArrayList<Integer> specials; //specials keeps the 3 special character for the game
    private ArrayList<String> playedCards;
    private List<Integer> bag;
    private ArrayList<Queue> queue;
    private transient ControllerServer server;
    private int numberOfPlayers;
    private String fileName;

    public VirtualView(int numberOfPlayers, ControllerServer server, String fileName) {
        this.schoolBoards = new ArrayList<>();
        this.hands = new ArrayList<>();
        this.clouds = new ArrayList<>();
        this.islands = new ArrayList<>();
        this.specials = new ArrayList<>();
        this.playedCards = new ArrayList<>();
        this.queue = new ArrayList<>();
        this.bag = new ArrayList<>();
        this.server = server;
        this.numberOfPlayers = numberOfPlayers;
        this.fileName = fileName;

        for(int i=0; i<12; i++)
            this.islands.add(new Island());
    }

    public void saveVirtualView(){
        try{
            clearFile();
            FileOutputStream outputFile = new FileOutputStream(fileName);
            ObjectOutputStream objectOut = new ObjectOutputStream(outputFile);

            objectOut.writeObject(schoolBoards);
            objectOut.writeObject(islands);
            objectOut.writeObject(clouds);
            objectOut.writeObject(hands);
            objectOut.writeObject(specials);
            objectOut.writeObject(bag);
            objectOut.writeObject(playedCards);
            objectOut.writeObject(queue);

            objectOut.close();
            outputFile.close();
        } catch (FileNotFoundException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace(); }
    }
    public void clearFile() {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                raf.setLength(0);
            }
        } catch (FileNotFoundException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public boolean checkFile(){
        File file = new File(fileName);
        if (file.length() != 0)
            return true;
        return false;
    }


    public ArrayList<String> getAlreadyChosenCharacters(){
        ArrayList<String> chosenCharacters = new ArrayList<>();

        for(SchoolBoard player:schoolBoards)
            chosenCharacters.add(player.character);

        return chosenCharacters;
    }
    public boolean checkNewNickname(String newNickname){
        for(SchoolBoard player:schoolBoards)
            if(player.nickname.equals(newNickname)) return false;
        return true;
    }
    public boolean checkNewCharacter(String newCharacter){
        for(SchoolBoard player:schoolBoards)
            if(player.character.equals(newCharacter)) return false;
        return true;
    }
    public String getCharacter(int playerRef){ return this.schoolBoards.get(playerRef).character; }
    public String getLastPlayedCard(int playerRef){ return hands.get(playerRef).lastPlayedCard; }
    public String getNickname(int playerRef){ return this.schoolBoards.get(playerRef).nickname; }
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
            server.studentsChangeInSchool(color, "Entrance", componentRef, newStudentsValue);
        } else if(place==1){
            schoolBoards.get(componentRef).setStudentsTable(componentRef, newStudentsValue);
            server.studentsChangeInSchool(color, "Table", componentRef, newStudentsValue);
        } else if(place==2){
            islands.get(componentRef).setStudentsIsland(color, newStudentsValue);
            server.studentChangeOnIsland(componentRef, color, newStudentsValue);
        } else if(place==3){
            clouds.get(componentRef).setCloudStudents(color, newStudentsValue);
            server.studentChangeOnCloud(componentRef, color, newStudentsValue);
        }
    }
    @Override
    public void notifyProfessors(int playerRef, int color, boolean newProfessorValue) {
        schoolBoards.get(playerRef).setProfessors(color, newProfessorValue);
        server.professorChangePropriety(playerRef, color, newProfessorValue);
    }
    @Override
    public void notifyMotherPosition(int newMotherPosition) {
        islands.get(newMotherPosition).setMotherPosition(true);
        server.motherChangePosition(newMotherPosition);
    }
    @Override
    public void notifyPlayedCard(int playerRef, String assistantCard) {
        hands.get(playerRef).setLastCard(assistantCard);
        hands.get(playerRef).setNumberOfCards(hands.get(playerRef).numberOfCards--);
        hands.get(playerRef).cards.remove(assistantCard);
        server.lastCardPlayedFromAPlayer(playerRef, assistantCard);
    }
    @Override
    public void notifyNewCoinsValue(int playerRef, int newCoinsValue) {
        hands.get(playerRef).setCoins(newCoinsValue);
        server.numberOfCoinsChangeForAPlayer(playerRef, newCoinsValue);
    }
    @Override
    public void notifyIslandChange(int islandToDelete) {
        islands.remove(islandToDelete);
        server.dimensionOfAnIslandIsChange(islandToDelete);
    }
    @Override
    public void notifyTowersChange(int place, int componentRef, int towersNumber) {
        if (place == 0) {
            schoolBoards.get(componentRef).setTowersNumber(towersNumber);
            server.towersChangeInSchool(componentRef, towersNumber);
        } else if (place == 1) {
            islands.get(componentRef).setTowersNumber(towersNumber);
            server.towersChangeOnIsland(componentRef, towersNumber);
        }
    }
    @Override
    public void notifyTowerColor(int islandRef, int newColor) {
        islands.get(islandRef).setTowersColor(newColor);
        server.towerChangeColorOnIsland(islandRef, newColor);
    }
    @Override
    public void notifyInhibited(int islandRef, int isInhibited) {
        islands.get(islandRef).setInhibited(isInhibited);
        server.islandInhibited(islandRef, isInhibited);
    }
    @Override
    public void notifySpecial(int specialRef) {
        specials.add(specialRef);
        server.setSpecial(specialRef);
    }

    @Override
    public void notifySpecialName(String specialName) {

    }

    @Override
    public void notifyPlayedSpecial(int specialRef) {

    }

    @Override
    public void notifyBagExtraction() {
        bag.remove(0);
    }
    @Override
    public void notifyBag(List<Integer> bag) {
        this.bag = bag;
    }
    @Override
    public void notifyQueue(int queueRef, int playerRef) {

        queue.get(queueRef).setPlayerRef(playerRef);
    }

    @Override
    public void notifyValueCard(int queueRef, int valueCard) {
        queue.get(queueRef).setValueCard(valueCard);
    }

    @Override
    public void notifyMaxMove(int queueRef, int maxMove) {
        queue.get(queueRef).setMaxMoveMotherNature(maxMove);
    }

    @Override
    public void notifyResetQueue() {
        for(int i=0; i< queue.size(); i++)
            queue.remove(i);
    }

    //private class SchoolBoard keeps the state of each player's school board
    private class SchoolBoard implements Serializable{
        private String nickname;
        private String character;
        private int team; //0: white, 1: black, 2:grey
        private int[] studentsEntrance;
        private int[] studentsTable;
        private int towersNumber;
        private boolean[] professors;

        public SchoolBoard(String nickname, String character){
            this.nickname = nickname;
            this.character = character;
            this.studentsEntrance = new int[]{0, 0, 0, 0, 0};
            this.studentsTable  = new int[]{0, 0, 0, 0, 0};
            this.professors = new boolean[]{false, false, false, false, false};
        }
        
        public void setStudentsEntrance(int color, int newValue){ this.studentsEntrance[color] = newValue; }
        public void setStudentsTable(int color, int newValue) { this.studentsTable[color] = newValue; }
        public void setTowersNumber(int towersNumber) { this.towersNumber = towersNumber; }
        public void setProfessors(int color, boolean newValue) { this.professors[color] = newValue; }
        public void setTeam(int team) { this.team = team; }
    }

    private class Island implements Serializable{
        private int[] studentsIsland;
        private boolean isMotherPosition;
        private int towersNumber;
        private int towersColor;
        private int isInhibited;

        public Island() {
            this.studentsIsland = new int[]{0,0,0,0,0};
            this.isMotherPosition = false;
            this.towersNumber = 1;
            this.towersColor = -1;
            this.isInhibited = 0;
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
    private class Cloud implements Serializable{
        private int[] students;

        public Cloud(){
            this.students = new int[]{0, 0, 0, 0, 0};
        }

        public void setCloudStudents(int color, int newStudentsValue) {
            students[color] = newStudentsValue;
        }
    }
    private class Hand implements Serializable{
        int numberOfCards;
        int coins;
        String lastPlayedCard;
        List<String> cards;

        public Hand(){
            this.numberOfCards = 10;
            this.coins = 1;
            this.cards= new ArrayList<>();
            cards.add("LION"); cards.add("GOOSE"); cards.add("CAT"); cards.add("EAGLE"); cards.add("FOX");
            cards.add("LIZARD"); cards.add("OCTOPUS"); cards.add("DOG"); cards.add("ELEPHANT"); cards.add("TURTLE");

        }

        public void setCoins(int coins) {
            this.coins = coins;
        }

        public void setLastCard(String lastPlayedCard) {
            this.lastPlayedCard = lastPlayedCard;
        }
        public String getLastCard(){ return lastPlayedCard;}
        public void setNumberOfCards(int numberOfCards) {
            this.numberOfCards = numberOfCards;
        }
    }
    private class Queue implements Serializable{

        private int playerRef;
        private int valueCard;
        private int maxMoveMotherNature;

        public void setPlayerRef(int playerRef) {
            this.playerRef = playerRef;
        }
        public void setValueCard(int valueCard) {
            this.valueCard = valueCard;
        }

        public void setMaxMoveMotherNature(int maxMoveMotherNature) {
            this.maxMoveMotherNature = maxMoveMotherNature;
        }
    }
}