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

    public Proxy_s(int port,Entrance server) {
        this.port = port;
        this.server = server;
        this.connections_allowed = 1;
        this.user = new ArrayList<>();
    }

    @Override
    public void start() {
        //ExecutorService executor = Executors.newCachedThreadPool(); //Create threads when needed, but re-use existing ones as much as possible
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); //port not available
            return;
        }
        System.out.println("Eryantis Server | Welcome!");
        System.out.println("Waiting for players ...");
        try {
            Socket socket = serverSocket.accept();
            user.add(new VirtualClient(socket,server,this));
        } catch (IOException e) {

        }
        for (int i = 1; i < connections_allowed; i++) {
            try {
                Socket socket = serverSocket.accept();
                user.add(new VirtualClient(socket,server,this));
            } catch (IOException e) {
                break; // entrerei qui se serverSocket venisse chiuso
            }
        }
    }

    public int getConnections_allowed() { return connections_allowed; }
    public void setConnections_allowed(int connections_allowed) { this.connections_allowed = connections_allowed; }
}