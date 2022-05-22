package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Proxy_s implements Exit {
    private final int port;
    private final Entrance server;
    private int connectionsAllowed;
    private List<VirtualClient> user;
    private final ExecutorService executor;
    private int limiter;
    public boolean stop;

    public Proxy_s(int port,Entrance server) {
        this.port = port;
        this.server = server;
        this.connectionsAllowed = 1;
        this.user = new ArrayList<>();
        this.executor = Executors.newCachedThreadPool(); //Create threads when needed, but re-use existing ones as much as possible
        this.limiter = 0;
        this.stop = false;
    }

    @Override
    public void start() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Eryantis Server | Welcome!");
            System.out.println("Waiting for players ...");
            while (limiter < 4 && !stop) {
                try {
                    VirtualClient virtualClient = new VirtualClient(serverSocket.accept(), server, this, limiter);
                    System.out.println("Connected players:" + limiter);
                    user.add(virtualClient);
                    executor.submit(virtualClient);
                    if(limiter == connectionsAllowed) stop = true;
                } catch (IOException e) {
                    //CHE FACCIO?
                }
            }
            System.out.println("ciao");
        } catch (IOException e) {
             //port not available
             //CHE FACCIO?
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

    public void incrLimiter(){ this.limiter++; }
    public int getConnectionsAllowed() { return connectionsAllowed; }
    public void setConnectionsAllowed(int connectionsAllowed) { this.connectionsAllowed = connectionsAllowed; }
}