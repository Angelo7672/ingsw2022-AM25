package it.polimi.ingsw.server;

import it.polimi.ingsw.server.expertmode.ExpertGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Proxy_s is server's proxy, it manages connections with clients and excess connections.
 */
public class Proxy_s implements Exit {
    private final Entrance server;
    private ServerSocket serverSocket;
    private int connectionsAllowed;
    private final List<VirtualClient> user;
    private final ExecutorService executor;
    private int limiter;
    private int clientReady;
    private int start;
    private boolean first;
    private boolean restoreGame;

    /**
     * Create ServerSocket.
     * @param port where to create the server;
     * @param server to which the proxy is connected;
     */
    public Proxy_s(int port, Entrance server) {
        this.server = server;
        try { serverSocket = new ServerSocket(port);
        }catch (IOException e){
            System.err.println("Port not available");
            server.exitError();
        }
        this.connectionsAllowed = 1;   //initialized a 1, so that proxy is ready for first client which will change this attribute
        this.user = new ArrayList<>();
        this.executor = Executors.newCachedThreadPool(); //Create threads when needed, but re-use existing ones as much as possible
        this.limiter = 0;
        this.clientReady = 0;
        this.start = 0;
        this.first = true;
    }

    /**
     * Start proxy so that it is possible to accept connections (from 2 to 4 connections, the others are immediately discharged with a SoldOut)
     */
    @Override
    public void start() {
        SoldOut soldOut = new SoldOut(serverSocket);

        try {
            System.out.println("Waiting for players ...");

            VirtualClient firstClient = new VirtualClient(serverSocket.accept(), server, this);
            System.out.println("Connected players: " + limiter);
            user.add(firstClient);
            executor.submit(firstClient);   //send VirtualClient to executor

            while(connectionsAllowed == 1) synchronized (this) { this.wait(); } //stop here until the first player decides the number of players in the game

            VirtualClient secondClient = new VirtualClient(serverSocket.accept(), server, this);   //There must be two players to play
            System.out.println("Connected players: " + limiter);
            user.add(secondClient);
            executor.submit(secondClient);

            while (limiter < connectionsAllowed) {  //accept also other players (if we play in 3 or 4 players)
                VirtualClient virtualClient = new VirtualClient(serverSocket.accept(), server, this);
                user.add(virtualClient);
                System.out.println("Connected players: " + limiter);
                executor.submit(virtualClient);
            }

            soldOut.start();    //from this moment all other connections are discharged
            synchronized (this){ this.wait(); }

            if(restoreGame) server.restoreGame();   //If the first player decides to load the save
            else {  //otherwise, initialize a new game
                server.createGame();
                server.initializeGame();
            }
            virtualClientInOrder(); //order VirtualClient, in the order they logged in
            if(start != connectionsAllowed) synchronized (this){ this.wait(); } //wait for everyone to be ready
            server.startGame();
        } catch (IOException | InterruptedException e) {
            System.err.println("Connection lost with a client!");
            clientDisconnected();
            server.exitError();
        }
    }

    /**
     * @see VirtualClient
     * @param ref client reference;
     */
    @Override
    public void goPlayCard(int ref){ user.get(ref).sendPlayCard(); }

    /**
     * @see VirtualClient
     * @param ref client reference;
     */
    @Override
    public void unlockPlanningPhase(int ref){ user.get(ref).unlockPlanningPhase(); }

    /**
     * @see VirtualClient
     * @param ref client reference;
     */
    @Override
    public void startActionPhase(int ref){ user.get(ref).sendStartTurn(); }

    /**
     * @see VirtualClient
     * @param ref client reference;
     */
    @Override
    public void unlockActionPhase(int ref){ user.get(ref).unlockActionPhase(); }

    /**
     * @see VirtualClient
     * @param ref client reference;
     */
    @Override
    public void sendMaxMovementMotherNature(int ref, int maxMovement){ user.get(ref).sendMaxMovementMotherNature(maxMovement); }

    /**
     * Send at every client info about this game.
     * @see VirtualClient
     * @param numberOfPlayers number of players in this game;
     * @param expertMode game mode;
     */
    @Override
    public void sendGameInfo(int numberOfPlayers, boolean expertMode){
        for (VirtualClient client:user)
            client.sendGameInfo(numberOfPlayers, expertMode);
    }

    /**
     * Send at every client user's info.
     * @see VirtualClient
     * @param playerRef player reference;
     * @param nickname chosen from player;
     * @param character chosen from character;
     */
    @Override
    public void sendUserInfo(int playerRef, String nickname, String character){
        for (VirtualClient client:user)
            client.sendUserInfo(playerRef, nickname, character);
    }

    /**
     * Send at every client the number of students of a color in a school.
     * @see VirtualClient
     * @param color color reference;
     * @param place place reference;
     * @param componentRef school reference;
     * @param newStudentsValue number of student of this color;
     */
    @Override
    public void studentsChangeInSchool(int color, String place, int componentRef, int newStudentsValue){
        for (VirtualClient client:user)
            client.studentsChangeInSchool(color, place, componentRef, newStudentsValue);
    }

    /**
     * Send at every client the number of students of a color on an island.
     * @see VirtualClient
     * @param islandRef island reference;
     * @param color color reference;
     * @param newStudentsValue number of student of this color;
     */
    @Override
    public void studentChangeOnIsland(int islandRef, int color, int newStudentsValue){
        for (VirtualClient client:user)
            client.studentChangeOnIsland(islandRef, color, newStudentsValue);
    }

    /**
     * Send at every client the number of students of a color on a cloud.
     * @see VirtualClient
     * @param cloudRef cloud reference;
     * @param color color reference;
     * @param newStudentsValue number of student of this color;
     */
    @Override
    public void studentChangeOnCloud(int cloudRef, int color, int newStudentsValue){
        for (VirtualClient client:user)
            client.studentChangeOnCloud(cloudRef, color, newStudentsValue);
    }

    /**
     * Send at every client the professor's new owner.
     * @see VirtualClient
     * @param playerRef player reference;
     * @param color color reference;
     * @param newProfessorValue professor presence;
     */
    @Override
    public void professorChangePropriety(int playerRef, int color, boolean newProfessorValue){
        for (VirtualClient client:user)
            client.professorChangePropriety(playerRef, color, newProfessorValue);
    }

    /**
     * Send at every client the new mother pose.
     * @see VirtualClient
     * @param newMotherPosition mother pose;
     */
    @Override
    public void motherChangePosition(int newMotherPosition){
        for (VirtualClient client:user)
            client.motherChangePosition(newMotherPosition);
    }

    /**
     * Send at every client the last card played from a player.
     * @see VirtualClient
     * @param playerRef player reference;
     * @param assistantCard played;
     */
    @Override
    public void lastCardPlayedFromAPlayer(int playerRef, String assistantCard){
        for (VirtualClient client:user)
            client.lastCardPlayedFromAPlayer(playerRef, assistantCard);
    }

    /**
     * Send at every client the new number of coins of a player.
     * @see VirtualClient
     * @param playerRef player reference;
     * @param newCoinsValue new player's coins value;
     */
    @Override
    public void numberOfCoinsChangeForAPlayer(int playerRef, int newCoinsValue){
        for (VirtualClient client:user)
            client.numberOfCoinsChangeForAPlayer(playerRef, newCoinsValue);
    }

    /**
     * Send at every client the new dimension of an island.
     * @see VirtualClient
     * @param islandToDelete island to delete because joined;
     */
    @Override
    public void dimensionOfAnIslandIsChange(int islandToDelete){
        for (VirtualClient client:user)
            client.dimensionOfAnIslandIsChange(islandToDelete);
    }

    /**
     * Send at every client the new towers' number in a school.
     * @see VirtualClient
     * @param playerRef player owner of the school;
     * @param towersNumber the new towers' number;
     */
    @Override
    public void towersChangeInSchool(int playerRef, int towersNumber){
        for (VirtualClient client:user)
            client.towersChangeInSchool(playerRef,towersNumber);
    }

    /**
     * Send at every client the new towers' number on an island.
     * @see VirtualClient
     * @param islandRef island reference;
     * @param towersNumber the new towers' number;
     */
    @Override
    public void towersChangeOnIsland(int islandRef, int towersNumber){
        for (VirtualClient client:user)
            client.towersChangeOnIsland(islandRef, towersNumber);
    }

    /**
     * Send at every client the new towers' color on an island.
     * @see VirtualClient
     * @param islandRef island reference;
     * @param newColor new color reference;
     */
    @Override
    public void towerChangeColorOnIsland(int islandRef, int newColor){
        for (VirtualClient client:user)
            client.towerChangeColorOnIsland(islandRef, newColor);
    }

    /**
     * Send at every client the new noEntryCards number on an island.
     * @param islandRef island reference;
     * @param isInhibited noEntryCards number;
     */
    @Override
    public void islandInhibited(int islandRef, int isInhibited){
        for (VirtualClient client:user)
            client.islandInhibited(islandRef, isInhibited);
    }

    /**
     * Send at every client a special with its cost of this game.
     * @see VirtualClient
     * @param specialRef special index;
     * @param cost of the special;
     */
    @Override
    public void setSpecial(int specialRef, int cost){
        for (VirtualClient client:user)
            client.setSpecial(specialRef,cost);
    }

    /**
     * Send at every VirtualClient the class expert game with special characters of this game.
     * @see VirtualClient
     * @param expertGame expert class;
     */
    @Override
    public void setExpertGame(ExpertGame expertGame){
        for (VirtualClient client:user)
            client.setExpertGame(expertGame);
    }

    /**
     * Comunicate at every client that a player used a special.
     * @see VirtualClient
     * @param playerRef player reference;
     * @param indexSpecial special index;
     */
    @Override
    public void sendUsedSpecial(int playerRef, int indexSpecial){
        for (VirtualClient client:user)
            client.sendUsedSpecial(playerRef,indexSpecial);
    }

    /**
     * Send to a client cards of his player of last saved game.
     * @see VirtualClient
     * @param playerRef player reference;
     * @param hand ArrayList of player's assistant;
     */
    @Override
    public void sendHandAfterRestore(int playerRef, ArrayList<String> hand){ user.get(playerRef).sendHandAfterRestore(hand); }

    /**
     * Send at every client students on special1, special7 or special11.
     * @see VirtualClient
     * @param specialIndex special index, special1, special7 or special11;
     * @param studentColor student reference;
     * @param value students of this color;
     */
    @Override
    public void sendInfoSpecial1or7or11(int specialIndex, int studentColor, int value){
        for (VirtualClient client:user)
            client.sendInfoSpecial1or7or11(specialIndex, studentColor, value);
    }

    /**
     * Send at every client the number of noEntryCards on special5.
     * @see VirtualClient
     * @param cards number of noEntryCards;
     */
    @Override
    public void sendInfoSpecial5(int cards) {
        for (VirtualClient client : user)
            client.sendInfoSpecial5(cards);
    }

    /**
     * Send at every client the team winner.
     * @see VirtualClient
     */
    @Override
    public void gameOver(){
        String winner = server.endGame();

        for (VirtualClient client:user)
            client.sendWinner(winner);
    }

    /**
     * Notify all clients (except the disconnected one) that a client has disconnected.
     * @see VirtualClient
     */
    public void clientDisconnected(){
        for (VirtualClient virtualClient : user)
            virtualClient.closeSocket();
    }

    /**
     * Increment limiter (counter) of connections.
     */
    public void incrLimiter(){ this.limiter++; }

    /**
     * Return if is first, but in any case set first = false.
     * @return if is first.
     */
    public boolean isFirst() {
        boolean tmp = first;
        first = false;
        return tmp;
    }

    /**
     * Increment clientReady counter and if is it equal to connections allowed notify.
     */
    public synchronized void thisClientIsReady(){
        clientReady++;
        if(clientReady == connectionsAllowed) this.notify();
    }

    /**
     * Increment start counter and if is it equal to connections allowed notify.
     */
    public synchronized void startGame(){
        start++;
        if(start == connectionsAllowed) this.notify();
    }
    public int getConnectionsAllowed() { return connectionsAllowed; }

    /**
     * Set connections allowed then notify.
     * @param connectionsAllowed number of connections allowed;
     */
    public void setConnectionsAllowed(int connectionsAllowed) {
        this.connectionsAllowed = connectionsAllowed;
        synchronized (this){ this.notify(); }
    }
    public boolean isRestoreGame() { return restoreGame; }
    public void setRestoreGame(boolean restoreGame) { this.restoreGame = restoreGame; }

    /**
     * Order VirtualClient by playerRef.
     */
    private void virtualClientInOrder(){ user.sort(VirtualClient::compareTo); }
}