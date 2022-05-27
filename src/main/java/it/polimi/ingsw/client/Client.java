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
            System.out.println("Some errors occurred, try again.");
            return;
        }
        System.out.println("Connection established");
        System.out.println("Do you want to use CLI o GUI?");
        String graph = scanner.next();
        if(graph.equalsIgnoreCase("CLI")) {
            CLI cli = new CLI(socket);
            cli.run();
        }
        else if (graph.equalsIgnoreCase("GUI")) {
            GUI.main(args);
        }
    }
}
