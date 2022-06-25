package it.polimi.ingsw.controller;

import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.server.ControllerServer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Virtual View class listen to changes in model classes through specific listener interfaces.
 * It writes and reads file saveGame.
 */
public class VirtualView
        implements TowersListener, ProfessorsListener, SpecialListener, PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener, BagListener, NoEntryListener, SpecialStudentsListener,
        QueueListener, Serializable {
    private final GameInfo gameInfo;
    private final TurnInfo turnInfo;
    private final ArrayList<SchoolBoard> schoolBoards;
    private final ArrayList<Island> islands;
    private final ArrayList<Cloud> clouds;
    private final ArrayList<Hand> hands;
    private final ArrayList<Special> specialList; //special keeps the 3 special character of the game
    private List<Integer> bag;
    private final ArrayList<Queue> queue;
    private final transient ControllerServer server;
    private final transient Restore controller;
    private final transient int numberOfPlayers;
    private final transient String fileName;

    /**
     * Create VirtualView with its inner class which represent a photograph of the game board in an instant of play.
     * @param numberOfPlayers in this game;
     * @param expertMode game mode;
     * @param server server reference;
     * @param controller controller reference;
     * @param fileName saveGame file path;
     */
    public VirtualView(int numberOfPlayers, boolean expertMode, ControllerServer server, Restore controller, String fileName) {
        this.schoolBoards = new ArrayList<>();
        this.hands = new ArrayList<>();
        this.clouds = new ArrayList<>();
        this.islands = new ArrayList<>();
        this.specialList = new ArrayList<>();
        this.queue = new ArrayList<>();
        this.bag = new ArrayList<>();
        this.server = server;
        this.controller = controller;
        this.numberOfPlayers = numberOfPlayers;
        this.fileName = fileName;
        this.gameInfo = new GameInfo(numberOfPlayers,expertMode);
        this.turnInfo = new TurnInfo();

        for(int i = 0; i < 12; i++)
            this.islands.add(new Island());
        for(int i = 0; i < numberOfPlayers; i++)
            this.queue.add(new Queue());
    }

    /**
     * Save all virtualView's inner class which represent a certain game configuration.
     * @param expertMode if is true, save also specialList;
     */
    public void saveVirtualView(boolean expertMode){
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
            objectOut.writeObject(bag);
            if(expertMode) objectOut.writeObject(specialList);

            objectOut.close();
            outputFile.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Clear saveGame file.
     */
    public void clearFile() {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                raf.setLength(0);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Restore empty SchoolBoard on virtualView adding player of last saved game with their nickname and character.
     */
    public void restoreVirtualView(){
        ArrayList<SchoolBoard> schoolBoardsTmp;

        try{
            ObjectInputStream inputFile = new ObjectInputStream(new FileInputStream(fileName));

            inputFile.readObject();   //I have to read all object in file
            inputFile.readObject();   //I have to read all object in file
            inputFile.readObject();   //I have to read all object in file
            schoolBoardsTmp = (ArrayList<SchoolBoard>) inputFile.readObject();

            inputFile.close();

            //Virtual view restore
            for(SchoolBoard s:schoolBoardsTmp)
                addNewPlayer(s.getNickname(), s.getCharacter());
        } catch (ClassNotFoundException | IOException e) { e.printStackTrace(); }
    }

    /**
     * Read from file info about last saved game.
     * @param expertMode indicates if we have to read also info about specials;
     */
    public void restoreGame(boolean expertMode){
        try{
            ObjectInputStream inputFile = new ObjectInputStream(new FileInputStream(fileName));

            inputFile.readObject();   //I have to read all object in file, this first object is GameInfo but now is useless

            turnStatusRestore((TurnInfo) inputFile.readObject());
            queueRestore((ArrayList<Queue>) inputFile.readObject());
            schoolsRestore((ArrayList<SchoolBoard>) inputFile.readObject());
            islandsRestore((ArrayList<Island>) inputFile.readObject());
            cloudsRestore((ArrayList<Cloud>) inputFile.readObject());
            handAndCoinsRestore((ArrayList<Hand>) inputFile.readObject());
            bagRestore((ArrayList<Integer>) inputFile.readObject());
            if(expertMode) specialRestore((ArrayList<Special>) inputFile.readObject());

            inputFile.close();
        } catch (ClassNotFoundException | IOException e) { e.printStackTrace(); }
    }

    /**
     * Restore turn status of last saved game (current user which have to play and current phase of turn).
     * @param turnInfosTmp object read from saveGame file;
     */
    private void turnStatusRestore(TurnInfo turnInfosTmp){
        controller.setCurrentUser(turnInfosTmp.getCurrentUser());
        controller.setJumpPhaseForRestore(turnInfosTmp.getPhase());
    }

    /**
     * Restore queue of last saved game (it contains the ordered list of player ready for play with valueCard and maxMove).
     * @param queueTmp object read from saveGame file;
     */
    private void queueRestore(ArrayList<Queue> queueTmp){
        ArrayList<Integer> playerRef = new ArrayList<>();
        ArrayList<Integer> valueCard = new ArrayList<>();
        ArrayList<Integer> maxMoveMotherNature = new ArrayList<>();

        for(Queue q:queueTmp){
            playerRef.add(q.getPlayerRef());
            valueCard.add(q.getValueCard());
            maxMoveMotherNature.add(q.getMaxMoveMotherNature());
        }
        controller.queueRestore(playerRef,valueCard,maxMoveMotherNature);
    }

    /**
     * Restore schools of last saved game.
     * @param schoolBoardsTmp object read from saveGame file;
     */
    private void schoolsRestore(ArrayList<SchoolBoard> schoolBoardsTmp){
        for(int i = 0; i < numberOfPlayers; i++){
            int[] studentsEntrance = schoolBoardsTmp.get(i).getStudentsEntrance();
            int[] studentsTable = schoolBoardsTmp.get(i).getStudentsTable();
            int towers = schoolBoardsTmp.get(i).getTowersNumber();
            boolean[] professors = schoolBoardsTmp.get(i).getProfessors();
            String team = schoolBoardsTmp.get(i).getTeam();
            controller.schoolRestore(i,studentsEntrance,studentsTable,towers,professors,team);
        }
    }

    /**
     * Restore islands of last saved game.
     * @param islandsTmp object read from saveGame file;
     */
    private void islandsRestore(ArrayList<Island> islandsTmp){
        if(islandsTmp.size() != 12) controller.setIslandsSizeAfterRestore(islandsTmp.size());
        for(int i = 0; i < islandsTmp.size(); i++){
            if(islandsTmp.get(i).isMotherPosition()) controller.restoreMotherPose(i);
            int[] students = islandsTmp.get(i).getStudentsIsland();
            int towerValue = islandsTmp.get(i).getTowersNumber();
            String towerTeam = islandsTmp.get(i).getTowersColor();
            int inhibited = islandsTmp.get(i).getIsInhibited();
            controller.islandRestore(i,students,towerValue,towerTeam,inhibited);
        }
    }

    /**
     * Restore clouds of last saved game.
     * @param cloudsTmp object read from saveGame file;
     */
    private void cloudsRestore(ArrayList<Cloud> cloudsTmp){
        for(int i = 0; i < numberOfPlayers; i++){
            int[] students = cloudsTmp.get(i).getStudents();
            controller.cloudRestore(i,students);
        }
    }

    /**
     * Restore hands and coins of player of last saved game.
     * It also sends to the server, for each player, last played card.
     * @param handsTmp object read from saveGame file;
     */
    private void handAndCoinsRestore(ArrayList<Hand> handsTmp){
        for(int i = 0; i < numberOfPlayers; i++){
            ArrayList<String> cards = handsTmp.get(i).getCards();
            int coins = handsTmp.get(i).getCoins();
            controller.handAndCoinsRestore(i,cards,coins);
            hands.get(i).setLastCard(handsTmp.get(i).getLastPlayedCard());
        }
        for(int i = 0; i < numberOfPlayers; i++)
            server.lastCardPlayedFromAPlayer(i, getLastPlayedCard(i));
    }

    /**
     * Restore bag of last saved game.
     * @param bagTmp object read from saveGame file;
     */
    private void bagRestore(ArrayList<Integer> bagTmp){  controller.bagRestore(bagTmp); }

    /**
     * Restore specials of last saved game.
     * If in the list of specials there are special1, special7 or special11, restore also students on them.
     * If in the list of specials there is special5, restore noEntryCards on its.
     * @param specialsListTmp object read from saveGame file;
     */
    private void specialRestore(ArrayList<Special> specialsListTmp){
        for (int i = 0; i < 3; i++) {
            int indexSpecial = specialsListTmp.get(i).getIndexSpecial();
            controller.specialRestore(indexSpecial, specialsListTmp.get(i).getCost());
            if(indexSpecial == 1 || indexSpecial == 7 || indexSpecial == 11)
                controller.specialStudentRestore(indexSpecial, specialsListTmp.get(i).getColorForSpecial1or7or11());
            else if(indexSpecial == 5)
                controller.noEntryCardsRestore(specialsListTmp.get(i).getNoEntryCards());
        }
    }

    /**
     * @param indexSpecial special that we want to find in specialList;
     * @return the index in specialList of a special if it is present, else return -1.
     */
    private int findSpecial(int indexSpecial){
        for (int i = 0; i < 3; i++)
            if(specialList.get(i).getIndexSpecial() == indexSpecial) return i;
        return -1;
    }

    /**
     * Check if after restore this nickname is present in last saved game.
     * @param nickname of the player;
     * @return position of the player in the list.
     */
    public int checkRestoreNickname(String nickname){
        int checker = -1;

        for (int i = 0; i < schoolBoards.size(); i++)
            if(nickname.equals(
                    schoolBoards.get(i).getNickname()
            ) && !schoolBoards.get(i).getAlreadyConnected()
                ){
                    checker = i;
                    schoolBoards.get(i).setAlreadyConnected();
            }

        return checker;
    }

    /**
     * @return an ArrayList of already chosen characters by other players in game, so that a new player can choose an available character.
     */
    public ArrayList<String> getAlreadyChosenCharacters(){
        ArrayList<String> chosenCharacters = new ArrayList<>();

        for(SchoolBoard player:schoolBoards)
            chosenCharacters.add(player.character);

        return chosenCharacters;
    }

    /**
     * @param newNickname that a new player chosen;
     * @return if none has already chosen that nickname.
     */
    public boolean checkNewNickname(String newNickname){
        for(SchoolBoard player:schoolBoards)
            if(player.nickname.equals(newNickname)) return false;
        return true;
    }

    /**
     * @param newCharacter that a new player chosen;
     * @return if none has already chosen that character.
     */
    public boolean checkNewCharacter(String newCharacter){
        for(SchoolBoard player:schoolBoards)
            if(player.character.equals(newCharacter)) return false;
        return true;
    }

    /**
     * Add a new player in schoolBoards, so add a cloud in clouds and a hand in hands.
     * @param nickname choose by the player to add;
     * @param character choose by the player to add;
     * @return the index of the player in schoolBoards.
     */
    public int addNewPlayer(String nickname, String character){
        int player;
        schoolBoards.add(new SchoolBoard(nickname,character));
        hands.add(new Hand());
        clouds.add(new Cloud());

        player = schoolBoards.size() - 1;
        if(numberOfPlayers == 3) {
            schoolBoards.get(player).setTowersNumber(6);
            schoolBoards.get(player).setTeam(player);
        } else if(numberOfPlayers == 4){
            if(player == 0 || player == 2)
                schoolBoards.get(player).setTowersNumber(8);
            else schoolBoards.get(player).setTowersNumber(0);
            if(player == 0 || player == 1)
                schoolBoards.get(player).setTeam(0);
            else schoolBoards.get(player).setTeam(1);
        } else {
            schoolBoards.get(player).setTowersNumber(8);
            schoolBoards.get(player).setTeam(player);
        }
        return player;
    }

    public void setCurrentUser(int currentUser){ turnInfo.setCurrentUser(currentUser); }
    public void setPhase(int phase){ turnInfo.setPhase(phase); }
    public String getCharacter(int playerRef){ return this.schoolBoards.get(playerRef).character; }
    public String getLastPlayedCard(int playerRef){ return hands.get(playerRef).getLastPlayedCard(); }
    public String getNickname(int playerRef){ return this.schoolBoards.get(playerRef).nickname; }

    //Notify

    /**
     * Notify a change of students value somewhere in the model. It also sends new value to the server.
     * @param place place == 0 -> school entrance, place == 1 -> school table, place == 2 -> island, place == 3 -> cloud;
     * @param componentRef reference of the component in the respective list;
     * @param color color reference;
     * @param newStudentsValue new value to replace with the older in the corresponding list;
     */
    @Override
    public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {
        if(place == 0){
            schoolBoards.get(componentRef).setStudentsEntrance(color, newStudentsValue);
            server.studentsChangeInSchool(color, "Entrance", componentRef, newStudentsValue);
        } else if(place == 1){
            schoolBoards.get(componentRef).setStudentsTable(color, newStudentsValue);
            server.studentsChangeInSchool(color, "Table", componentRef, newStudentsValue);
        } else if(place == 2){
            islands.get(componentRef).setStudentsIsland(color, newStudentsValue);
            server.studentChangeOnIsland(componentRef, color, newStudentsValue);
        } else if(place == 3){
            clouds.get(componentRef).setCloudStudents(color, newStudentsValue);
            server.studentChangeOnCloud(componentRef, color, newStudentsValue);
        }
    }

    /**
     * Notify a change of professors value in a school. It also sends new value to the server.
     * @param playerRef player reference;
     * @param color color reference;
     * @param newProfessorValue new value to replace with the older in schoolBoards;
     */
    @Override
    public void notifyProfessors(int playerRef, int color, boolean newProfessorValue) {
        schoolBoards.get(playerRef).setProfessors(color, newProfessorValue);
        server.professorChangePropriety(playerRef, color, newProfessorValue);
    }

    /**
     * Notify new pose of mother nature. It also sends new pose to the server.
     * @param newMotherPosition mother nature reference on island;
     */
    @Override
    public void notifyMotherPosition(int newMotherPosition) {
        islands.get(newMotherPosition).setMotherPosition(true);
        server.motherChangePosition(newMotherPosition);
    }

    /**
     * Notify the card played by a player. It also sends this last played card to the server.
     * @param playerRef player reference;
     * @param assistantCard card reference;
     */
    @Override
    public void notifyPlayedCard(int playerRef, String assistantCard) {
        hands.get(playerRef).setLastCard(assistantCard);
        hands.get(playerRef).cards.remove(assistantCard);
        server.lastCardPlayedFromAPlayer(playerRef, assistantCard);
    }

    /**
     * Notify to a user his hand after restore and keeps it saved.
     * @param playerRef player reference;
     * @param hand ArrayList of cards from last saved game;
     */
    @Override
    public void notifyHand(int playerRef, ArrayList<String> hand) {
        hands.get(playerRef).setCards(hand);
        server.sendHandAfterRestore(playerRef,hand);
    }

    /**
     * Notify new value of coins of a user. It also sends new value to the server.
     * @param playerRef player reference;
     * @param newCoinsValue new coins value;
     */
    @Override
    public void notifyNewCoinsValue(int playerRef, int newCoinsValue) {
        hands.get(playerRef).setCoins(newCoinsValue);
        server.numberOfCoinsChangeForAPlayer(playerRef, newCoinsValue);
    }

    /**
     * Notify that two islands joined. It also sends new configuration to the server.
     * @param islandToDelete island to delete because joined to an adjacent island;
     */
    @Override
    public void notifyIslandChange(int islandToDelete) {
        islands.remove(islandToDelete);
        server.dimensionOfAnIslandIsChange(islandToDelete);
    }

    /**
     * Notify that number of tower has changed somewhere. It also sends new value to the server.
     * @param place place == 0 -> school, place == 1 -> island;
     * @param componentRef reference of the component in the respective list;
     * @param towersNumber new towers number;
     */
    @Override
    public void notifyTowersChange(int place, int componentRef, int towersNumber) {
        if (place == 0) {
            schoolBoards.get(componentRef).setTowersNumber(towersNumber);
            if(numberOfPlayers != 4) server.towersChangeInSchool(componentRef, towersNumber);
            else if(componentRef != 1 && componentRef != 3) server.towersChangeInSchool(componentRef, towersNumber);
        } else if (place == 1) {
            islands.get(componentRef).setTowersNumber(towersNumber);
            server.towersChangeOnIsland(componentRef, towersNumber);
        }
    }

    /**
     * Notify a tower's change color on an island. It also sends new tower's color to the server.
     * @param islandRef island reference;
     * @param newColor new tower's color;
     */
    @Override
    public void notifyTowerColor(int islandRef, int newColor) {
        islands.get(islandRef).setTowersColor(newColor);
        server.towerChangeColorOnIsland(islandRef, newColor);
    }

    /**
     * Notify that special5 put a noEntryCard on an island. It also sends island reference to the server.
     * @param islandRef island reference;
     * @param isInhibited is the number of noEntryCards on this island;
     */
    @Override
    public void notifyInhibited(int islandRef, int isInhibited) {
        islands.get(islandRef).setInhibited(isInhibited);
        server.islandInhibited(islandRef, isInhibited);
    }

    /**
     * Notify a bag extraction and in case of bag is empty set that game will end at the end of the turn.
     */
    @Override
    public void notifyBagExtraction() {
        bag.remove(0);
        if(bag.isEmpty()) controller.setEnd(true);
    }

    /**
     * Notify the entire bag to VirtualView when it is initialized or when special1 or special11 shuffle it, so that it is ready to been written on file.
     * @param bag the entire bag of the model;
     */
    @Override
    public void notifyBag(List<Integer> bag) { this.bag = bag; }

    /**
     * Notify the position on the queue of a player, so that it is ready to been written on file.
     * @param queueRef queue reference;
     * @param playerRef player reference;
     */
    @Override
    public void notifyQueue(int queueRef, int playerRef) { queue.get(queueRef).setPlayerRef(playerRef); }

    /**
     * Notify the value of a card played by a player and set it in queue ready to been written on file.
     * @param queueRef queue reference;
     * @param valueCard value of the card played;
     */
    @Override
    public void notifyValueCard(int queueRef, int valueCard) { queue.get(queueRef).setValueCard(valueCard); }

    /**
     * Notify the max movement of mother that a player can do. It also sends it to the server.
     * @param queueRef queue reference;
     * @param maxMove  max movement give by the card played;
     */
    @Override
    public void notifyMaxMove(int queueRef, int maxMove) {
        queue.get(queueRef).setMaxMoveMotherNature(maxMove);
        if(maxMove != -1) {
            server.sendMaxMovementMotherNature(
                    queue.get(queueRef).getPlayerRef(), maxMove
            );
        }
    }

    /**
     * Notify use of a special by a player.
     * @param specialRef special reference;
     * @param playerRef player reference;
     */
    @Override
    public void notifySpecial(int specialRef, int playerRef) { server.sendUsedSpecial(playerRef, specialRef); }

    /**
     * Notify specials of this match with their cost to the server and keeps it so that it is ready to been written on file.
     * @param specialList ArrayList contains specials of this game;
     * @param cost ArrayList contains the specials' cost of this game;
     */
    @Override
    public void notifySpecialList(ArrayList<Integer> specialList, ArrayList<Integer> cost) {
        for(int i = 0; i < 3; i++) {
            server.setSpecial(
                    specialList.get(i),
                    cost.get(i)
            );
            this.specialList.add(new Special(specialList.get(i),
                cost.get(i)
            ));
        }
    }

    /**
     * Notify the increase of cost of a special. It also sends the new cost to the server.
     * @param specialRef special reference;
     * @param newCost new cost;
     */
    @Override
    public void notifyIncreasedCost(int specialRef, int newCost) {
        server.setSpecial(specialRef, newCost);
        specialList.get(findSpecial(specialRef)).incSpecialCost();
    }

    /**
     * Notify the increase or decrease of the number of noEntryCards on special5 and sends it to the server;
     * @param cards number of cards on special5;
     */
    @Override
    public void notifyNoEntry(int cards) {
        server.sendInfoSpecial5(cards);
        specialList.get(findSpecial(5)).setNoEntryCards(cards);
    }

    /**
     * Notify a change of students on special1, special7 or special11 and send it to the server.
     * @param specialIndex special reference;
     * @param color color reference;
     * @param value new value of this color on this special;
     */
    @Override
    public void specialStudentsNotify(int specialIndex, int color, int value) {
        server.sendInfoSpecial1or7or11(specialIndex, color, value);
        specialList.get(findSpecial(specialIndex)).setColorForSpecial1or7or11(color,value);
    }

    /**
     * TurnInfo keeps info about the current player taking his turn and in which part of the turn we are (planningPhase or actionPhase).
     */
    private static class TurnInfo implements Serializable{
        private int currentUser;
        private String phase;

        public void setCurrentUser(int currentUser){ this.currentUser = currentUser; }

        /**
         * @param phase phase == 0 -> PlanningPhase, phase == 1 -> ActionPhase;
         */
        public void setPhase(int phase){
            if(phase == 0) this.phase = "PlanningPhase";
            else if(phase == 1) this.phase = "ActionPhase";
        }
        public int getCurrentUser() { return currentUser; }
        public String getPhase() { return phase; }
    }

    /**
     * Special keeps info about a special.
     * The attribute colorForSpecial1or7or11 is only for special1, special7 or special11.
     * The attribute noEntryCards is only for special5.
     */
    private static class Special implements Serializable{
        private final int indexSpecial;
        private int cost;
        private final int[] colorForSpecial1or7or11;
        private int noEntryCards;

        public Special(int indexSpecial, int cost) {
            this.indexSpecial = indexSpecial;
            this.cost = cost;
            this.colorForSpecial1or7or11 = new int[]{0,0,0,0,0};    //not initialized because is not for every special
            this.noEntryCards = 4;  //initialized at 4, but only special5 use it
        }

        public void incSpecialCost(){ cost++; }
        public int getIndexSpecial() { return indexSpecial; }
        public int getCost() { return cost; }
        public void setColorForSpecial1or7or11(int color, int value){ colorForSpecial1or7or11[color] = value; }
        public int[] getColorForSpecial1or7or11() { return colorForSpecial1or7or11; }
        public void setNoEntryCards(int numCards){ noEntryCards = numCards; }
        public int getNoEntryCards() { return noEntryCards; }
    }

    /**
     * SchoolBoard keeps the state of each player's school board.
     */
    private static class SchoolBoard implements Serializable{
        private final String nickname;
        private boolean alreadyConnected;
        private final String character;
        private int team; //0: white, 1: black, 2:grey
        private final int[] studentsEntrance;
        private final int[] studentsTable;
        private int towersNumber;
        private final boolean[] professors;

        public SchoolBoard(String nickname, String character){
            this.nickname = nickname;
            this.alreadyConnected = false;
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
        public void setAlreadyConnected() { this.alreadyConnected = true; }
        public boolean getAlreadyConnected() { return alreadyConnected; }
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

    /**
     * Islands keeps info about an islands.
     */
    private static class Island implements Serializable{
        private final int[] studentsIsland;
        private int towersNumber;
        private int towersColor;
        private int isInhibited;
        private boolean isMotherPosition;

        public Island() {
            this.studentsIsland = new int[]{0,0,0,0,0};
            this.towersNumber = 1;
            this.towersColor = -1;
            this.isInhibited = 0;
            this.isMotherPosition = false;
        }

        public void setTowersNumber(int towersNumber) { this.towersNumber = towersNumber; }
        public void setStudentsIsland(int color, int newValue) { this.studentsIsland[color] = newValue; }
        public void setTowersColor(int newColor){ this.towersColor = newColor; }
        public void setInhibited(int isInhibited) { this.isInhibited = isInhibited; }
        public void setMotherPosition(boolean isMotherPos) { this.isMotherPosition = isMotherPos; }
        public boolean isMotherPosition() { return isMotherPosition; }
        public int[] getStudentsIsland() { return studentsIsland; }
        public int getTowersNumber() { return towersNumber; }
        public String getTowersColor() {
           if(towersColor == 0) return "WHITE";
           else if(towersColor == 1) return "BLACK";
           else if(towersColor == 2) return "GREY";
           return "NONE";
        }
        public int getIsInhibited() { return isInhibited; }
    }

    /**
     * Cloud keeps info about a cloud.
     */
    private static class Cloud implements Serializable{
        private final int[] students;

        public Cloud(){ this.students = new int[]{0, 0, 0, 0, 0}; }

        public void setCloudStudents(int color, int newStudentsValue) { students[color] = newStudentsValue; }
        public int[] getStudents() { return students; }
    }

    /**
     * Hand keeps info about cards, last play card and coins of a player.
     */
    private static class Hand implements Serializable{
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

    /**
     * Queue keeps info about playerRef value of card played and max movement of mother nature given by the card played of a player.
     * It is an element of list of Queue used to decide who gets to play.
     */
    private static class Queue implements Serializable{
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