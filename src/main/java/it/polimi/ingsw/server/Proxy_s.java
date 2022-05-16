package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Proxy_s implements Exit {
    private final int port;
    private final Entrance server;
    private int connections_allowed;
    private List<VirtualClient> user;
    private List<Pong> checkConnection;
    private final ExecutorService executor;
    private int limiter;
    public boolean stop;

    public Proxy_s(int port,Entrance server) {
        this.port = port;
        this.server = server;
        this.connections_allowed = 1;
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
        } catch (IOException e) {
            System.err.println(e.getMessage()); //port not available
            return;
        }
        System.out.println("Eryantis Server | Welcome!");
        System.out.println("Waiting for players ...");
        while (limiter < 4 && !stop) {
            try {
                VirtualClient virtualClient = new VirtualClient(serverSocket.accept(), server, this, limiter);
                System.out.println("Connected players:" + limiter);
                user.add(virtualClient);
                executor.submit(virtualClient);
                if(limiter == connections_allowed) stop = true;
            } catch (IOException e) {
                break; // entrerei qui se serverSocket venisse chiuso
            }
        }
        System.out.println("ciao");
    }



    @Override
    public void goPlayCard(int ref, ArrayList<String> playedCardsInThisTurn){ user.get(ref).sendInfoCard(playedCardsInThisTurn); }
    @Override
    public void startActionPhase(int ref){ user.get(ref).sendStartTurn(); }

    public void incrLimiter(){ this.limiter++; }
    public int getConnections_allowed() { return connections_allowed; }
    public void setConnections_allowed(int connections_allowed) { this.connections_allowed = connections_allowed; }

}