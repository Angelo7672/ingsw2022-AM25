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
        QueueListener {

    private ArrayList<SchoolBoard> schoolBoards;
    private ArrayList<Island> islands;
    private ArrayList<Cloud> clouds;
    private ArrayList<Hand> hands;
    private ArrayList<Integer> specials; //specials keeps the 3 special character for the game
    private ArrayList<String> playedCards;
    private List<Integer> bag;
    private ArrayList<Integer> queue;
    private ControllerServer server;
    private int numberOfPlayers;
    private String fileName;

    public VirtualView(int numberOfPlayers, ControllerServer server) {
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
        this.fileName = "saveGame.bin";

        for(int i=0; i<12; i++)
            this.islands.add(new Island());
    }

    public void saveVirtualView(){
        try{
            clearFile();
            FileOutputStream outputFile = new FileOutputStream(fileName);
            ObjectOutputStream objectOut = new ObjectOutputStream(outputFile);

            objectOut.writeObject(schoolBoards);
            /*for(SchoolBoard schoolBoard : schoolBoards)
                objectOut.writeObject(schoolBoard);*/
            /*for(Island island:islands)
                objectOut.writeObject(island);
            for(Cloud cloud:clouds)
                objectOut.writeObject(cloud);
            for(int i=0; i<specials.size(); i++)
                objectOut.writeObject(specials.get(i));
            for(int i=0; i<bag.size(); i++)
                objectOut.writeObject(bag.get(i));
            for(int i=0; i<playedCards.size(); i++)
                objectOut.writeObject(playedCards.get(i));
            for(int i=0; i<queue.size(); i++)
                objectOut.writeObject(queue.get(i));*/
            objectOut.close();
            outputFile.close();
            /*
            ObjectOutputStream outputFile = new ObjectOutputStream(new FileOutputStream(fileName, false));

            outputFile.writeObject(this.schoolBoards);
            outputFile.writeObject(this.islands);
            outputFile.writeObject(this.clouds);
            outputFile.writeObject(this.hands);
            outputFile.writeObject(this.specials);
            outputFile.writeObject(this.bag);
            outputFile.writeObject(this.playedCards);
            outputFile.writeObject(this.queue);
            outputFile.close();*/
        } catch (FileNotFoundException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace(); }
    }
    public void restoreVirtualView(){
        try{
            ObjectInputStream inputFile = new ObjectInputStream(new FileInputStream(fileName));
            for(SchoolBoard s:this.schoolBoards){
                this.schoolBoards.add( (SchoolBoard) inputFile.readObject());
            for(Island i:this.islands)
                this.islands.add((Island) inputFile.readObject());
            for(Cloud c:this.clouds)
                this.clouds.add((Cloud) inputFile.readObject());
            for(int i=0; i<this.specials.size(); i++)
                this.specials.add((Integer) inputFile.readObject());
            for(int i=0; i<bag.size(); i++)
                this.bag.add((Integer) inputFile.readObject());
            for(int i=0; i<playedCards.size(); i++)
                this.playedCards.add((String) inputFile.readObject());
            for(int i=0; i<queue.size(); i++)
                this.queue.add((Integer) inputFile.readObject());
            }
            inputFile.close();
            /*
            this.schoolBoards = (ArrayList<SchoolBoard>) inputFile.readObject();
            this.islands= (ArrayList<Island>) inputFile.readObject();
            this.clouds=(ArrayList<Cloud>) inputFile.readObject();
            this.hands=(ArrayList<Hand>) inputFile.readObject();
            this.specials=(ArrayList<Integer>) inputFile.readObject();
            this.bag=(ArrayList<Integer>) inputFile.readObject();
            this.playedCards=(ArrayList<String>) inputFile.readObject();
            this.queue=(ArrayList<Integer>) inputFile.readObject();*/
        } catch (FileNotFoundException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace();
        } catch (ClassNotFoundException e) { e.printStackTrace(); }
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
    public void notifyBagExtraction() {
        bag.remove(0);
    }

    @Override
    public void notifyBag(List<Integer> bag) {
        this.bag=bag;
    }

    @Override
    public void notifyQueue(int playerRef) {
        queue.add(playerRef);
    }
    @Override
    public void notifyResetQueue() {
        for(int i=0; i< queue.size(); i++)
            queue.remove(i);
    }

    //private class SchoolBoard keeps the state of each player's school board
    private class SchoolBoard implements Serializable{
        String nickname;
        String character;
        int team; //0: white, 1: black, 2:grey
        int[] studentsEntrance;
        int[] studentsTable;
        int towersNumber;
        boolean[] professors;

        public SchoolBoard(String nickname, String character){
            super();
            this.nickname = nickname;
            this.character = character;
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

    private class Island implements Serializable{
        int[] studentsIsland;
        boolean isMotherPosition;
        int towersNumber;
        int towersColor;
        int isInhibited;

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
        int[] students;

        public Cloud(){
            this.students = new int[]{0, 0, 0, 0, 0};
        }

        public void setCloudStudents(int color, int newStudentsValue) {
            students[color]=newStudentsValue;
        }
    }
    private class Hand implements Serializable{
        int numberOfCards;
        int coins;
        String lastPlayedCard;

        public Hand(){
            this.numberOfCards=10;
            this.coins=1;
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
