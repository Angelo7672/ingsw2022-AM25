package it.polimi.ingsw.server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class VirtualClientTest {
    Socket socket;
    ObjectInputStream input;
    ObjectOutputStream output;

    public VirtualClientTest() {
        try {
            this.socket = new Socket("127.0.0.1", 2525);
            this.input = new ObjectInputStream(socket.getInputStream());
            this.output = new ObjectOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Bisogna istanziare un server e creare dei client farlocchi
     */

    //Test per inizializzazione

    //Test per fase pianificazione

    //Test per fase azione
}