package it.polimi.ingsw.client;

import it.polimi.ingsw.client.GUI.GUI;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static Socket socket;


    public static void main(String[] args) throws IOException {
        System.out.println("Eriantys");
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket("127.0.0.1", 2525);
        } catch (IOException e) {
            System.err.println("Some errors occurred, try again.");
            return;
        }
        System.out.println("Connection established");
        System.out.println("Do you want to use CLI or GUI?");
        String graph = scanner.next();
        if(graph.equalsIgnoreCase("CLI")) {
            CLI cli = new CLI(socket);
            cli.run();
        }
        else if (graph.equalsIgnoreCase("GUI")) {
            GUI gui = new GUI();
            Proxy_c proxy = new Proxy_c(socket);
            View view;
            gui.setSocket(socket);
            gui.setProxy(proxy);
            /*try {
                view = proxy.startView();
                gui.setView(view);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            gui.main(null);

        }
        else {
            System.err.println("Error! Try again.");
            return;
        }
    }
}
