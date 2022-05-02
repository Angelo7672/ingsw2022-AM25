package it.polimi.ingsw.client;

import java.io.IOException;

public class Client implements Entrance{

    public static void main(String[] args) {
        Proxy_c proxy = new Proxy_c("127.0.0.1", 1337);
        try {
            proxy.start();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
