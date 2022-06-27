package it.polimi.ingsw.client;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.GUI.GUI;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    private static Socket socket;
    private static String SPACE = "\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";
    private static String ANSI_GREEN = "\u001B[32m";
    private static String ANSI_RED = "\u001B[31m";
    private static String ANSI_RESET = "\u001B[0m";
    private static String  UNDERLINE = "\u001B[4m";


    public static void main(String[] args) throws IOException { //non si mette il lancio di eccezioni nel main! Falle catturare dai catch, printa qualcosa a schermo e poi fai System.exit
        System.out.println(SPACE+ANSI_GREEN+UNDERLINE+"ERIANTYS"+ANSI_RESET);
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.print(SPACE+"Do you want to use CLI or GUI? ");
        String graph = scanner.next();
        if(graph.equalsIgnoreCase("CLI")) {
            try {
                socket = new Socket("127.0.0.1", 2525);
            } catch (IOException e) {
                System.out.println(SPACE+ANSI_RED+"Some errors occurred, try again."+ANSI_RESET);
                return;
            }
            System.out.println(SPACE+"Connection established, waiting for server...");
            Exit proxy = new Proxy_c(socket);
            CLI cli = new CLI(socket, proxy);
            cli.run();
        }
        else if (graph.equalsIgnoreCase("GUI")) {
            try {
                socket = new Socket("127.0.0.1", 2525);
            } catch (IOException e) {
                System.out.println(ANSI_RED+SPACE+"Some errors occurred, try again."+ANSI_RESET);
                return;
            }
            System.out.println(SPACE+"Connection established");
            Exit proxy = new Proxy_c(socket);
            GUI gui = new GUI();
            gui.setSocket(socket);
            gui.setProxy(proxy);
            System.out.println(SPACE+"GUI is starting...");
            gui.main(null);

        }
        else {
            System.err.println("Error! Try again.");
            return;
        }
    }
}
