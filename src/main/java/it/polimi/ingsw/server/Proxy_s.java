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
    private boolean stop;
    private boolean first;

    public Proxy_s(int port,Entrance server) {
        this.port = port;
        this.server = server;
        this.connectionsAllowed = 4;
        this.user = new ArrayList<>();
        this.executor = Executors.newCachedThreadPool(); //Create threads when needed, but re-use existing ones as much as possible
        this.limiter = 0;
        this.stop = false;
        this.first = true;
    }

    @Override
    public void start() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Eryantis Server | Welcome!");
            System.out.println("Waiting for players ...");
            VirtualClient firstClient = new VirtualClient(serverSocket.accept(), server, this, limiter);
            System.out.println("Connected players:" + limiter);
            user.add(firstClient);
            executor.submit(firstClient);
            while (limiter < 4 && !stop) {
                VirtualClient virtualClient = new VirtualClient(serverSocket.accept(), server, this, limiter);
                System.out.println("Connected players:" + limiter);
                user.add(virtualClient);
                if(limiter == connectionsAllowed) stop = true;
            }
            if(connectionsAllowed < limiter){
                if(limiter - connectionsAllowed == 1) user.remove(2);
                else if(limiter - connectionsAllowed == 2){
                    user.remove(2); user.remove(3);
                }
            }
            for(int i = 1; i < connectionsAllowed; i++)
                executor.submit(user.get(i));
            System.out.println("ciao");

            //thread per rispondere che si e' al completo

        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.out.println("port not available");
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

    public void incrLimiter(){ this.limiter++; }
    public boolean isFirst() {
        boolean tmp = first;
        first = false;
        return tmp;
    }
    public int getConnectionsAllowed() { return connectionsAllowed; }
    public void setConnectionsAllowed(int connectionsAllowed) { this.connectionsAllowed = connectionsAllowed; }
}