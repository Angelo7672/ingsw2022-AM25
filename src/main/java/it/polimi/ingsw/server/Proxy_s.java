package it.polimi.ingsw.server;

import it.polimi.ingsw.server.expertmode.ExpertGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Proxy_s implements Exit {
    private final int port;
    private final Entrance server;
    private ServerSocket serverSocket;
    private int connectionsAllowed;
    private List<VirtualClient> user;
    private final ExecutorService executor;
    private int limiter;
    private int clientReady;
    private int start;
    private boolean first;
    private boolean restoreGame;

    public Proxy_s(int port,Entrance server) {
        this.port = port;
        this.server = server;
        try { serverSocket = new ServerSocket(port);
        }catch (IOException e){
            System.out.println("port not available");
            server.exitError();
        }
        this.connectionsAllowed = -1;
        this.user = new ArrayList<>();
        this.executor = Executors.newCachedThreadPool(); //Create threads when needed, but re-use existing ones as much as possible
        this.limiter = 0;
        this.clientReady = 0;
        this.start = 0;
        this.first = true;
    }

    @Override
    public void start() {
        SoldOut soldOut = new SoldOut(serverSocket);

        try {
            System.out.println("Waiting for players ...");

            VirtualClient firstClient = new VirtualClient(serverSocket.accept(), server, this);
            System.out.println("Connected players: " + limiter);
            user.add(firstClient);
            executor.submit(firstClient);
            VirtualClient secondClient = new VirtualClient(serverSocket.accept(), server, this);   //There must be two players to play
            System.out.println("Connected players: " + limiter);
            user.add(secondClient);

            while(connectionsAllowed == -1) synchronized (this) { this.wait(); }
            executor.submit(secondClient);

            while (limiter < connectionsAllowed) {
                VirtualClient virtualClient = new VirtualClient(serverSocket.accept(), server, this);
                user.add(virtualClient);
                System.out.println("Connected players: " + limiter);
                executor.submit(virtualClient);
            }

            soldOut.start();
            synchronized (this){ this.wait(); }

            if(restoreGame) server.restoreGame();
            else {
                server.createGame();
                server.initializeGame();
            }
            virtualClientInOrder();
            if(start != connectionsAllowed) synchronized (this){ this.wait(); }
            server.startGame();
        } catch (IOException e) {
            System.err.println("Connection lost with a client!");
            server.exitError();
        }catch (InterruptedException ex){
            System.err.println("Connection lost with a client!");
            server.exitError();
        }
    }

    @Override
    public void goPlayCard(int ref){ user.get(ref).sendPlayCard(); }
    @Override
    public void unlockPlanningPhase(int ref){ user.get(ref).unlockPlanningPhase(); }

    @Override
    public void startActionPhase(int ref){ user.get(ref).sendStartTurn(); }
    @Override
    public void unlockActionPhase(int ref){ user.get(ref).unlockActionPhase(); }
    @Override
    public void sendMaxMovementMotherNature(int ref, int maxMovement){ user.get(ref).sendMaxMovementMotherNature(maxMovement); }

    @Override
    public void sendGameInfo(int numberOfPlayers, boolean expertMode){
        for (VirtualClient client:user)
            client.sendGameInfo(numberOfPlayers, expertMode);
    }
    @Override
    public void sendUserInfo(int playerRef, String nickname, String character){
        for (VirtualClient client:user)
            client.sendUserInfo(playerRef, nickname, character);
    }
    @Override
    public void studentsChangeInSchool(int color, String place, int componentRef, int newStudentsValue){
        for (VirtualClient client:user)
            client.studentsChangeInSchool(color, place, componentRef, newStudentsValue);
    }
    @Override
    public void studentChangeOnIsland(int islandRef, int color, int newStudentsValue){
        for (VirtualClient client:user)
            client.studentChangeOnIsland(islandRef, color, newStudentsValue);
    }
    @Override
    public void studentChangeOnCloud(int cloudRef, int color, int newStudentsValue){
        for (VirtualClient client:user)
            client.studentChangeOnCloud(cloudRef, color, newStudentsValue);
    }
    @Override
    public void professorChangePropriety(int playerRef, int color, boolean newProfessorValue){
        for (VirtualClient client:user)
            client.professorChangePropriety(playerRef, color, newProfessorValue);
    }
    @Override
    public void motherChangePosition(int newMotherPosition){
        for (VirtualClient client:user)
            client.motherChangePosition(newMotherPosition);
    }
    @Override
    public void lastCardPlayedFromAPlayer(int playerRef, String assistantCard){
        for (VirtualClient client:user)
            client.lastCardPlayedFromAPlayer(playerRef, assistantCard);
    }
    @Override
    public void numberOfCoinsChangeForAPlayer(int playerRef, int newCoinsValue){
        for (VirtualClient client:user)
            client.numberOfCoinsChangeForAPlayer(playerRef, newCoinsValue);
    }
    @Override
    public void dimensionOfAnIslandIsChange(int islandToDelete){
        for (VirtualClient client:user)
            client.dimensionOfAnIslandIsChange(islandToDelete);
    }
    @Override
    public void towersChangeInSchool(int playerRef, int towersNumber){
        for (VirtualClient client:user)
            client.towersChangeInSchool(playerRef,towersNumber);
    }
    @Override
    public void towersChangeOnIsland(int islandRef, int towersNumber){
        for (VirtualClient client:user)
            client.towersChangeOnIsland(islandRef, towersNumber);
    }
    @Override
    public void towerChangeColorOnIsland(int islandRef, int newColor){
        for (VirtualClient client:user)
            client.towerChangeColorOnIsland(islandRef, newColor);
    }
    @Override
    public void islandInhibited(int islandRef, int isInhibited){
        for (VirtualClient client:user)
            client.islandInhibited(islandRef, isInhibited);
    }
    @Override
    public void setSpecial(int specialRef, int cost){
        for (VirtualClient client:user)
            client.setSpecial(specialRef,cost);
    }
    @Override
    public void setExpertGame(ExpertGame expertGame){
        for (VirtualClient client:user)
            client.setExpertGame(expertGame);
    }
    @Override
    public void sendUsedSpecial(int playerRef, int indexSpecial){
        for (VirtualClient client:user)
            client.sendUsedSpecial(playerRef,indexSpecial);
    }
    @Override
    public void sendHandAfterRestore(int playerRef, ArrayList<String> hand){ user.get(playerRef).sendHandAfterRestore(hand); }
    @Override
    public void sendInfoSpecial1or7or11(int specialIndex, int studentColor, int value){
        for (VirtualClient client:user)
            client.sendInfoSpecial1or7or11(specialIndex, studentColor, value);
    }
    @Override
    public void sendInfoSpecial5(int cards) {
        for (VirtualClient client : user)
            client.sendInfoSpecial5(cards);
    }

    @Override
    public void gameOver(){
        String winner = server.endGame();

        for (VirtualClient client:user)
            client.sendWinner(winner);
    }

    public void clientDisconnected(int clientLost){
        for(int i = 0; i < user.size(); i++)
            if(i != clientLost) user.get(i).closeSocket();
    }

    public void incrLimiter(){ this.limiter++; }
    public boolean isFirst() {
        boolean tmp = first;
        first = false;
        return tmp;
    }
    public synchronized void thisClientIsReady(){
        clientReady++;
        if(clientReady == connectionsAllowed) this.notify();
    }
    public synchronized void startGame(){
        start++;
        if(start == connectionsAllowed) this.notify();
    }
    public int getConnectionsAllowed() { return connectionsAllowed; }
    public void setConnectionsAllowed(int connectionsAllowed) {
        this.connectionsAllowed = connectionsAllowed;
        synchronized (this){ this.notify(); }
    }
    public boolean isRestoreGame() { return restoreGame; }
    public void setRestoreGame(boolean restoreGame) { this.restoreGame = restoreGame; }
    private void virtualClientInOrder(){ Collections.sort(user, VirtualClient::compareTo); }
}