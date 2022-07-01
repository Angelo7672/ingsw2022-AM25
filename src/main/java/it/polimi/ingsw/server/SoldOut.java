package it.polimi.ingsw.server;

import it.polimi.ingsw.server.answer.SoldOutAnswer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SoldOut reply with an answer that notifies you that there are no more connections available in the current game.
 */
public class SoldOut extends Thread{
    private final ExecutorService executor;
    private final ServerSocket serverSocket;

    /**
     * Create SoldOut class.
     * @param serverSocket server reference;
     */
    public SoldOut(ServerSocket serverSocket){
        this.executor = Executors.newCachedThreadPool();
        this.serverSocket = serverSocket;
    }

    /**
     * If received a new connection, submits it to executor.
     */
    @Override
    public void run(){
        while(true){
            try {
                Expired expired = new Expired(serverSocket.accept());
                executor.submit(expired);   //send to executor new socket
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    /**
     * Expired is a new Thread that send a SoldOutAnswer to client and close socket.
     */
    private static class Expired implements Runnable{
        private final Socket socket;

        /**
         * Create an expired class.
         * @param socket socket reference;
         */
        public Expired(Socket socket){ this.socket = socket; }

        /**
         * Sends SoldOut answer and close socket.
         */
        @Override
        public void run() {
            try {
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                output.reset();
                output.writeObject(new SoldOutAnswer());
                System.err.println("A client tried to connect, but there were no connections available!");
                output.flush();
                //this.socket.setSoTimeout(5000); //in any case, close the socket after 4 seconds
                //TODO: final check
                this.wait(5000);
                socket.close();
            } catch (SocketException socketException){ System.err.println("A client tried to connect, but there were no connections available!");
            } catch (IOException | InterruptedException e) { e.printStackTrace(); }
        }
    }
}