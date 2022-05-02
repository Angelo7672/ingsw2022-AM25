package it.polimi.ingsw.server;


import java.util.List;

public class Server implements Entrance{
    private int numberOfPlayer;
    private List<Integer> g;

    public static void main(String[] args) {
        Proxy_s proxy = new Proxy_s(1337);
        proxy.start();
    }

}
