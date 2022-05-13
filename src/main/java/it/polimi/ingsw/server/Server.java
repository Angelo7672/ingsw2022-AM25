package it.polimi.ingsw.server;

import java.util.List;

public class Server implements Entrance{
    private int numberOfPlayer;
    private boolean expertMode;
    private Exit proxy;
    private List<Integer> g;

    public Server(int port){
        this.proxy = new Proxy_s(port,this);
        proxy.start();
    }

    @Override
    public void setNumberOfPlayer(int numberOfPlayer) { this.numberOfPlayer = numberOfPlayer; }
    @Override
    public void setExpertMode(boolean expertMode) { this.expertMode = expertMode; }

    public static void main(String[] args) {
        Server server = new Server(2525);

    }
}

