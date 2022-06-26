package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.Server;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Eriantys {
    public static void main(String[] args) {
        System.out.println("Welcome to Eriantys game!\nWhat do you want to launch?");
        System.out.println("0 -> Server\n1 -> Client");
        Scanner scanner = new Scanner(System.in);
        int userInput;

        try{
            userInput = scanner.nextInt();
            switch (userInput){
                case 0 -> Server.main(null);
                case 1 -> Client.main(null);
            }
        }catch (InputMismatchException inputMismatchException){
            System.out.println("Error, try inserting a valid number");
            System.exit(-1);
        }catch (IOException e) {

        }
    }
}
