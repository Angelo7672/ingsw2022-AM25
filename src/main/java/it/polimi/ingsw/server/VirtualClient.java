package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class VirtualClient implements Runnable{
    private final Socket socket;
    private final Server server;
    private final Proxy_s proxy;

    public VirtualClient(Socket socket, Server server, Proxy_s proxy){
        this.socket = socket;
        this.server = server;
        this.proxy = proxy;
        run();
    }

    @Override
    public void run() {
        if(proxy.getConnections_allowed() == 1) {
            gameSetting();
        }
        System.out.println(server.getNumberOfPlayer());
        System.out.println(server.isExpertMode());
    }

    public void gameSetting(){
        String msg1 = "Benvenuto! Sei il primo! In quanti si gioca?";
        String msg2 = "Esperto si o no?";
        String res;
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(msg1);
            out.flush();
            res = in.nextLine();
            if(res.equals("2")){
                proxy.setConnections_allowed(2);
                server.setNumberOfPlayer(2);
            }
            else if(res.equals("3")){
                proxy.setConnections_allowed(3);
                server.setNumberOfPlayer(3);
            }
            else if(res.equals("4")){
                proxy.setConnections_allowed(4);
                server.setNumberOfPlayer(4);
            }
            out.println(msg2);
            out.flush();
            res = in.nextLine();
            if(res.equals("si")) server.setExpertMode(true);
            else if(res.equals("no")) server.setExpertMode(false);
            in.close();
            out.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }


}
