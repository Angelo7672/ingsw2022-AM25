package it.polimi.ingsw.server;

import it.polimi.ingsw.server.Answer.SoldOutAnswer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
    private boolean first;

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
        this.first = true;
    }

    @Override
    public void start() {
        SoldOut soldOut = new SoldOut();

        try {
            System.out.println("Eryantis Server | Welcome!");
            System.out.println("Waiting for players ...");
            VirtualClient firstClient = new VirtualClient(serverSocket.accept(), server, this, limiter);
            System.out.println("Connected players: " + limiter);
            user.add(firstClient);
            executor.submit(firstClient);
            VirtualClient secondClient = new VirtualClient(serverSocket.accept(), server, this, limiter);   //There must be two players to play
            System.out.println("Connected players: " + limiter);
            user.add(secondClient);
            if(connectionsAllowed == -1) synchronized (this) { this.wait(); }
            while (limiter < connectionsAllowed) {
                VirtualClient virtualClient = new VirtualClient(serverSocket.accept(), server, this, limiter);
                System.out.println("Connected players: " + limiter);
                user.add(virtualClient);
            }
            /*if(connectionsAllowed < limiter){
                if(limiter - connectionsAllowed == 1){
                    user.get(2).closeSocket();
                    user.remove(2);
                }
                else if(limiter - connectionsAllowed == 2){
                    user.get(3).closeSocket();
                    user.remove(3);
                    user.get(2).closeSocket();
                    user.remove(2);
                }
            }*/
            for(int i = 1; i < connectionsAllowed; i++)
                executor.submit(user.get(i));

            soldOut.start();

            synchronized (this){ this.wait(); }
            server.startGame();

        } catch (IOException e) {
            System.err.println(e.getMessage());
            server.exitError();
        }catch (InterruptedException ex){ ex.printStackTrace(); }
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
    public void setSpecial(int specialRef){
        for (VirtualClient client:user)
            client.setSpecial(specialRef);
    }


    public void incrLimiter(){ this.limiter++; }
    public boolean isFirst() {
        boolean tmp = first;
        first = false;
        return tmp;
    }
    public void thisClientIsReady(){
        clientReady++;
        if(clientReady == connectionsAllowed) synchronized (this){ this.notify(); }
    }
    public int getConnectionsAllowed() { return connectionsAllowed; }
    public void setConnectionsAllowed(int connectionsAllowed) { this.connectionsAllowed = connectionsAllowed; }

    private class SoldOut extends Thread{
        @Override
        public void run(){
            while(true){
                try {
                    Socket socket = serverSocket.accept();
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    output.reset();
                    output.writeObject(new SoldOutAnswer());
                    output.flush();
                    socket.close();
                    System.out.println("A client tried to connect, but there were no connections available!");
                }catch (IOException e) { e.printStackTrace(); }
            }
        }
    }
}