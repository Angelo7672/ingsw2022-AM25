package it.polimi.ingsw.server;

import java.net.Socket;
import java.util.TimerTask;
import java.util.Timer;


public class Pong {
    private final int port;
    private final Entrance server;
    private final Socket socket;

    public Pong(int port, Entrance server, Socket socket) {
        this.port = port;
        this.server = server;
        this.socket = socket;
    }

    public void run(){
        while (true){
            Timer timer = new Timer();
            TimerTask task = new Offline();

            timer.schedule(task,10000);
        }

    }

    class Offline extends TimerTask{
          public void run(){
             //comunica che il client e' andato offline
         }
    }
}
