package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Proxy_s implements Exit{
    private int port;
    private List<Integer> user;

    public Proxy_s(int port){ this.port = port; }

    public void start() {
        ExecutorService executor = Executors.newCachedThreadPool(); //Create threads when needed, but re-use existing ones as much as possible
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); //port not available
            return;
        }
        System.out.println("Server ready");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new ServerClientHandler(socket));
            } catch(IOException e) {
                break; // entrerei qui se serverSocket venisse chiuso
            }
        }
        executor.shutdown();
    }

    private class ServerClientHandler implements Runnable {
        private Socket socket;

        public ServerClientHandler(Socket socket) { this.socket = socket; }

        public void run() {
            try {
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                // leggo e scrivo nella connessione finche' non ricevo "quit"
                while (true) {
                    String line = in.nextLine();
                    if (line.equals("quit")) {
                        break;
                    } else {
                        out.println("Received: " + line);
                        out.flush();
                    }
                }
                // chiudo gli stream e il socket
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }


}
