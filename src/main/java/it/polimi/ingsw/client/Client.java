package it.polimi.ingsw.client;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.GUI.GUI;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

/**
 * Starts the connection with the server.
 */
public class Client {
    private static final String SPACE = "\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String  UNDERLINE = "\u001B[4m";


    /**
     * Asks ip and port of the server and then starts cli or gui.
     */
    public static void main(String[] args) {
        int port=0;
        String ip=null;
        System.out.println(SPACE+ANSI_GREEN+UNDERLINE+"ERIANTYS"+ANSI_RESET);
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        while(ip==null){
            System.out.print(SPACE+"Insert ip: ");
            ip = scanner.next();
            System.out.println();
        }
        try {
            while (port == 0) {
                System.out.print(SPACE + "Insert port: ");
                String intString = scanner.next();
                port = Integer.parseInt(intString);
                System.out.println();
            }
        }catch (NumberFormatException e) {
        System.out.println();
        System.out.println(ANSI_RED + SPACE + "Error, insert a number." + ANSI_RESET);
        return;
        }
        System.out.println();
        System.out.print(SPACE+"Do you want to use CLI or GUI? ");
        String graph = scanner.next();
        try {
            Socket socket;
            if (graph.equalsIgnoreCase("CLI")) {
                try {
                    socket = new Socket(ip, port);
                } catch (IOException e) {
                    System.out.println(SPACE + ANSI_RED + "Some errors occurred, try again." + ANSI_RESET);
                    return;
                }
                System.out.println(SPACE + "Connection established, waiting for server...");
                Exit proxy = null;
                try {
                    proxy = new Proxy_c(socket);
                } catch (SocketTimeoutException e) {
                    System.out.println();
                    System.out.println(ANSI_RED + SPACE + "Server is offline, Game over." + ANSI_RESET);
                    socket.close();
                    System.exit(-1);
                }
                CLI cli = new CLI(socket, proxy);
                cli.run();
            } else if (graph.equalsIgnoreCase("GUI")) {
                try {
                    socket = new Socket(ip, port);
                } catch (IOException e) {
                    System.out.println(ANSI_RED + SPACE + "Some errors occurred, try again." + ANSI_RESET);
                    return;
                }
                System.out.println(SPACE + "Connection established, waiting for server...");
                Exit proxy = new Proxy_c(socket);
                GUI gui = new GUI();
                proxy.setDisconnectedListener(gui);
                proxy.setServerOfflineListener(gui);
                gui.setProxy(proxy);
                System.out.println(SPACE + "GUI is starting...");
                GUI.main(null);

            } else {
                System.err.println("Error! Try again.");
            }
        }catch (IOException e){
            System.out.println();
            System.out.println(ANSI_RED + SPACE + "Server is offline, Game over." + ANSI_RESET);
            System.exit(-1);
        }
    }
}
