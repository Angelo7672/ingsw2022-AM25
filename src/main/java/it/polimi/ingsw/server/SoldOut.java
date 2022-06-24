package it.polimi.ingsw.server;

import it.polimi.ingsw.server.answer.SoldOutAnswer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoldOut extends Thread{
    private final ExecutorService executor;
    private ServerSocket serverSocket;

    public SoldOut(ServerSocket serverSocket){
        this.executor = Executors.newCachedThreadPool();
        this.serverSocket = serverSocket;
    }

    @Override
    public void run(){
        while(true){
            try {
                Expired expired = new Expired(serverSocket.accept());
                executor.submit(expired);
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private class Expired implements Runnable{
        private Socket socket;

        public Expired(Socket socket){ this.socket = socket; }

        @Override
        public void run() {
            try {
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                output.reset();
                output.writeObject(new SoldOutAnswer());
                output.flush();
                this.socket.setSoTimeout(4000);
                input.readObject();
                socket.close();
                System.err.println("A client tried to connect, but there were no connections available!");
            } catch (SocketException socketException){ System.err.println("A client tried to connect, but there were no connections available!");
            } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
        }
    }
}