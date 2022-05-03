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
    private final Server server;
    private int connections_allowed;
    private List<VirtualClient> user;

    public Proxy_s(int port,Server server) {
        this.port = port;
        this.server = server;
        this.connections_allowed = 1;
        this.user = new ArrayList<>();
    }

    public void start() {
        ExecutorService executor = Executors.newCachedThreadPool(); //Create threads when needed, but re-use existing ones as much as possible
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); //port not available
            return;
        }
        System.out.println("Eryantis Server | Welcome!");
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