package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.listeners.*;
import it.polimi.ingsw.server.ControllerServer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//virtual View class listen to changes in model classes through specific listener interfaces
public class VirtualView
        implements TowersListener, ProfessorsListener, SpecialListener, PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener, BagListener,
        QueueListener, Serializable {
    private GameInfo gameInfo;
    private TurnInfo turnInfo;
    private ArrayList<SchoolBoard> schoolBoards;
    private ArrayList<Island> islands;
    private ArrayList<Cloud> clouds;
    private ArrayList<Hand> hands;
    private ArrayList<Integer> specials; //specials keeps the 3 special character for the game
    private List<Integer> bag;
    private ArrayList<Queue> queue;
    private transient ControllerServer server;
    private transient Controller controller;
    private transient int numberOfPlayers;
    private transient String fileName;

    public VirtualView(int numberOfPlayers, boolean expertMode, ControllerServer server, Controller controller, String fileName) {
        this.schoolBoards = new ArrayList<>();
        this.hands = new ArrayList<>();
        this.clouds = new ArrayList<>();
        this.islands = new ArrayList<>();
        this.specials = new ArrayList<>();
        this.queue = new ArrayList<>();
        this.bag = new ArrayList<>();
        this.server = server;
        this.controller = controller;
        this.numberOfPlayers = numberOfPlayers;
        this.fileName = fileName;
        this.gameInfo = new GameInfo(numberOfPlayers,expertMode);
        this.turnInfo = new TurnInfo();

        for(int i=0; i<12; i++)
            this.islands.add(new Island());
        for(int i=0; i<numberOfPlayers; i++){
            this.queue.add(new Queue());
        }
    }

    public void saveVirtualView(){
        try{
            clearFile();
            FileOutputStream outputFile = new FileOutputStream(fileName);
            ObjectOutputStream objectOut = new ObjectOutputStream(outputFile);

            objectOut.writeObject(gameInfo);
            objectOut.writeObject(turnInfo);
            objectOut.writeObject(queue);
            objectOut.writeObject(schoolBoards);
            objectOut.writeObject(islands);
            objectOut.writeObject(clouds);
            objectOut.writeObject(hands);
            objectOut.writeObject(specials);
            objectOut.writeObject(bag);

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
    public void restoreGame(){
        TurnInfo turnInfosTmp;
        ArrayList<Queue> queueTmp;
        ArrayList<SchoolBoard> schoolBoardsTmp;
        ArrayList<Island> islandsTmp;
        ArrayList<Cloud> cloudsTmp;
        ArrayList<Hand> handsTmp;
        ArrayList<Integer> bagTmp;
        Object tmp;

        try{
            ObjectInputStream inputFile = new ObjectInputStream(new FileInputStream(fileName));

            tmp = inputFile.readObject();   //I have to read all object in file, this first object is GameInfo but now is useless
            turnInfosTmp = (TurnInfo) inputFile.readObject();
            queueTmp = (ArrayList<Queue>) inputFile.readObject();
            schoolBoardsTmp = (ArrayList<SchoolBoard>) inputFile.readObject();
            islandsTmp = (ArrayList<Island>) inputFile.readObject();
            cloudsTmp = (ArrayList<Cloud>) inputFile.readObject();
            handsTmp = (ArrayList<Hand>) inputFile.readObject();
            bagTmp = (ArrayList<Integer>) inputFile.readObject();

            inputFile.close();
            System.out.println("Restore game");

            //Turn status restore
            controller.setCurrentUser(turnInfosTmp.getCurrentUser());
            controller.setPhase(turnInfosTmp.getPhase());
            //Queue Restore
            ArrayList<Integer> playerRef = new ArrayList<>();
            ArrayList<Integer> valueCard = new ArrayList<>();
            ArrayList<Integer> maxMoveMotherNature = new ArrayList<>();
            for(Queue q:queueTmp){
                playerRef.add(q.getPlayerRef());
                valueCard.add(q.getValueCard());
                maxMoveMotherNature.add(q.getMaxMoveMotherNature());
            }
            controller.queueRestore(playerRef,valueCard,maxMoveMotherNature);
            //Schools Restore
            for(int i = 0; i < numberOfPlayers; i++){
                int[] studentsEntrance = schoolBoardsTmp.get(i).getStudentsEntrance();
                int[] studentsTable = schoolBoardsTmp.get(i).getStudentsTable();
                int towers = schoolBoardsTmp.get(i).getTowersNumber();
                boolean[] professors = schoolBoardsTmp.get(i).getProfessors();
                String team = schoolBoardsTmp.get(i).getTeam();
                controller.schoolRestore(i,studentsEntrance,studentsTable,towers,professors,team);
                controller.addNewPlayer(schoolBoardsTmp.get(i).getNickname(), schoolBoardsTmp.get(i).getNickname());
            }
            //Islands Restore
            if(islandsTmp.size() != 12) controller.setIslandsSizeAfterRestore(islandsTmp.size());
            for(int i = 0; i < islandsTmp.size(); i++){
                //if(islandsTmp.get(i).isMotherPosition()) controller.restoreMotherPose(i);
                int[] students = islandsTmp.get(i).getStudentsIsland();
                int towerValue = islandsTmp.get(i).getTowersNumber();
                String towerTeam = islandsTmp.get(i).getTowersColor();
                int inhibited = islandsTmp.get(i).getIsInhibited();
                controller.islandRestore(i,students,towerValue,towerTeam,inhibited);
            }
            //CloudsRestore
            for(int i = 0; i < numberOfPlayers; i++){
                int[] students = cloudsTmp.get(i).getStudents();
                controller.cloudRestore(i,students);
            }
            //Hands and Coins restore
            for(int i = 0; i < numberOfPlayers; i++){
                ArrayList<String> cards = handsTmp.get(i).getCards();
                int coins = handsTmp.get(i).getCoins();
                controller.handAndCoinsRestore(i,cards,coins);
            }
            //Bag Restore
            controller.bagRestore(bagTmp);
        } catch (FileNotFoundException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace();
        } catch (ClassNotFoundException e) { e.printStackTrace(); }
    }
    public int checkRestoreNickname(String nickname){
        int checker = -1;

        for (int i = 0; i < schoolBoards.size(); i++)
            if(nickname.equals(
                    schoolBoards.get(i).getNickname()
            )) checker = i;
        return checker;
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

    public void setCurrentUser(int currentUser){ turnInfo.setCurrentUser(currentUser); }
    public void setPhase(int phase){ turnInfo.setPhase(phase); }

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
        hands.get(playerRef).cards.remove(assistantCard);
        server.lastCardPlayedFromAPlayer(playerRef, assistantCard);
    }
    @Override
    public void notifyHand(int playerRef, ArrayList<String> hand) {
        hands.get(playerRef).setCards(hand);
        server.sendHandAfterRestore(playerRef,hand);
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
    public void notifyBagExtraction() { bag.remove(0); }
    @Override
    public void notifyBag(List<Integer> bag) { this.bag = bag; }
    @Override
    public void notifyQueue(int queueRef, int playerRef) { queue.get(queueRef).setPlayerRef(playerRef); }
    @Override
    public void notifyValueCard(int queueRef, int valueCard) { queue.get(queueRef).setValueCard(valueCard); }
    @Override
    public void notifyMaxMove(int queueRef, int maxMove) { queue.get(queueRef).setMaxMoveMotherNature(maxMove); }

    private class TurnInfo implements Serializable{
        private int currentUser;
        private String phase;

        public void setCurrentUser(int currentUser){ this.currentUser = currentUser; }
        public void setPhase(int phase){
            if(phase == 0) this.phase = "PlanningPhase";
            else if(phase == 1) this.phase = "ActionPhase";
        }
        public int getCurrentUser() { return currentUser; }
        public String getPhase() { return phase; }
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
        public String getNickname() { return nickname; }
        public String getCharacter() { return character; }
        public String getTeam() {
            if(team == 0) return "WHITE";
            else if(team == 1) return "BLACK";
            else if(team == 2) return "GREY";
            return "NONE";
        }
        public int[] getStudentsEntrance() { return studentsEntrance; }
        public int[] getStudentsTable() { return studentsTable; }
        public int getTowersNumber() { return towersNumber; }
        public boolean[] getProfessors() { return professors; }
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

        public void setTowersNumber(int towersNumber) { this.towersNumber = towersNumber; }
        public void setStudentsIsland(int color, int newValue) { this.studentsIsland[color] = newValue; }
        public void setMotherPosition(boolean isMotherPos) { this.isMotherPosition=isMotherPos; }
        public void setTowersColor(int newColor){ this.towersColor=newColor; }
        public void setInhibited(int isInhibited) { this.isInhibited=isInhibited; }
        public int[] getStudentsIsland() { return studentsIsland; }
        public boolean isMotherPosition() { return isMotherPosition; }
        public int getTowersNumber() { return towersNumber; }
        public String getTowersColor() {
           if(towersColor == 0) return "WHITE";
           else if(towersColor == 1) return "BLACK";
           else if(towersColor == 2) return "GREY";
           return "NONE";
        }
        public int getIsInhibited() { return isInhibited; }
    }
    private class Cloud implements Serializable{
        private int[] students;

        public Cloud(){ this.students = new int[]{0, 0, 0, 0, 0}; }

        public void setCloudStudents(int color, int newStudentsValue) { students[color] = newStudentsValue; }
        public int[] getStudents() { return students; }
    }
    private class Hand implements Serializable{
        int coins;
        String lastPlayedCard;
        ArrayList<String> cards;

        public Hand(){
            this.coins = 1;
            this.cards = new ArrayList<>();
            cards.add("LION"); cards.add("GOOSE"); cards.add("CAT"); cards.add("EAGLE"); cards.add("FOX");
            cards.add("LIZARD"); cards.add("OCTOPUS"); cards.add("DOG"); cards.add("ELEPHANT"); cards.add("TURTLE");
        }

        public void setCoins(int coins) { this.coins = coins; }
        public void setLastCard(String lastPlayedCard) { this.lastPlayedCard = lastPlayedCard; }
        public int getCoins() { return coins; }
        public String getLastPlayedCard() { return lastPlayedCard; }
        public ArrayList<String> getCards() { return cards; }
        public void setCards(ArrayList<String> cards) { this.cards = cards;}
    }
    private class Queue implements Serializable{
        private int playerRef;
        private int valueCard;
        private int maxMoveMotherNature;

        public void setPlayerRef(int playerRef) { this.playerRef = playerRef; }
        public void setValueCard(int valueCard) { this.valueCard = valueCard; }
        public void setMaxMoveMotherNature(int maxMoveMotherNature) { this.maxMoveMotherNature = maxMoveMotherNature; }
        public int getPlayerRef() { return playerRef; }
        public int getValueCard() { return valueCard; }
        public int getMaxMoveMotherNature() { return maxMoveMotherNature; }
    }
}