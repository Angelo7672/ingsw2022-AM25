package it.polimi.ingsw.client;

import java.util.Locale;
import java.util.Scanner;

public class Client {


    public static void main(String[] args) {
        System.out.println("Eriantys");
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to use CLI o GUI?");
        String graph = scanner.nextLine().toUpperCase(Locale.ROOT);
        if(graph.equals("CLI")) {
            CLI cli = new CLI();
            cli.run();
        }

    }
}
