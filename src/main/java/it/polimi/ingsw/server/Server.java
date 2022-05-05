
package it.polimi.ingsw.server;

import java.util.List;

    public class Server implements Entrance{
        private int numberOfPlayer;
        private boolean expertMode;
        private Proxy_s proxy;
        private List<Integer> g;

        public Server(int port){
            this.proxy = new Proxy_s(1337,this);
            proxy.start();
        }

        public void setNumberOfPlayer(int numberOfPlayer) { this.numberOfPlayer = numberOfPlayer; }
        public void setExpertMode(boolean expertMode) { this.expertMode = expertMode; }

        public int getNumberOfPlayer() { return numberOfPlayer; }
        public boolean isExpertMode() { return expertMode; }

        public static void main(String[] args) {
            Server server = new Server(1337);

        }
    }

