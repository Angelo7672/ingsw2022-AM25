package it.polimi.ingsw.client;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.constants.PlayerConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CLI implements Runnable {

    private Proxy_c proxy;
    private Scanner scanner;
    private boolean active;
    private PlayerConstants constants;


    public CLI() {
        scanner = new Scanner(System.in);
        active = true;
        constants = new PlayerConstants();
    }

    public void setup() throws IOException, ClassNotFoundException {
        System.out.println(">Insert the server IP address: ");
        System.out.print(">");
        String address = scanner.next();
        System.out.println(">Insert the server port: ");
        System.out.print(">");
        int port = scanner.nextInt();
        Constants.setAddress(address);
        Constants.setPort(port);
        proxy = new Proxy_c();
        if (!proxy.start()) {
            System.out.println("Some errors occurred, try again.");
            Client.main(null);
        }
        if(Constants.getNumberOfPlayers()==0){
            while(true) {
                try {
                    System.out.println("Insert number of player");
                    int numberOfPlayers = scanner.nextInt();
                    System.out.println("Expert mode? [y/n]");
                    String expertMode = scanner.next();
                    if (proxy.setupGame(numberOfPlayers, expertMode)) break;
                } catch (InputMismatchException e) {
                    System.out.println("Mismatch error");
                    Client.main(null);
                }
            }
        }
        while (true) {
            String nickname;
            String character;
            do {
                System.out.println("Insert your nickname: ");
                nickname = scanner.next();
            } while (nickname == null);
            do {
                System.out.println("Insert your character: ");
                character = scanner.next();
            } while (character == null);
            if (proxy.setupConnection(nickname, character)) break;
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void turn() throws IOException, ClassNotFoundException {
        scanner.reset();
        if (!constants.isSpecialUsed()) useSpecial();
        phaseHandler(constants.lastPhase());
    }

    public void phaseHandler(String phase) throws IOException, ClassNotFoundException {
        switch (phase) {
            case ("PlayCard"):
                playCard();
                break;
            case ("MoveStudent"):
                moveStudents();
                break;
            case ("MoveMother"):
                moveMotherNature();
                break;
            case ("ChoseCloud"):
                chooseCloud();
                break;
        }
    }

    public void useSpecial() throws IOException, ClassNotFoundException {
        System.out.println("Do you want to use a special card? [y/n]");
        String answer = scanner.next();
        if (answer.equalsIgnoreCase("n")) return;
        else if (answer.equalsIgnoreCase("y")) {
            int special = -1;
            do {
                System.out.println("Which special do you want to use? Insert number");
                special = scanner.nextInt();
            } while (special == -1);
            if(!proxy.checkSpecial(special)) {
                System.out.println("Error, special not present");
                useSpecial();
            }
            if(special(special)) return;
        }
        System.out.println("Error, try again");
        useSpecial();
    }

    private boolean special(int special) throws IOException, ClassNotFoundException {
        if (special == 2 || special == 4 || special == 6 || special == 8)
            if(proxy.useSpecial(special, 0, null, null)) return true;
        else if (special == 1) {
            System.out.println("Which student do you want to move? Insert color");
            String color = scanner.next();
            if(translateColor(color)==-1) return false;
            ArrayList<Integer> color1 = new ArrayList<>();
            color1.add(translateColor(color));
            int island = -1;
            do {
                System.out.println("In witch island? Insert the number");
                island = scanner.nextInt();
            } while (island == -1);
            if(proxy.useSpecial(special, island, color1, null)) return true;
        } else if (special == 3 || special == 5) {
            int island =-1;
            do {
                System.out.println("Which island? Insert the number");
                island = scanner.nextInt();
            } while(island==-1);
            if(proxy.useSpecial(special, island, null, null)) return true;
        } else if(special == 7){
            ArrayList<Integer> entranceStudents = new ArrayList<>();
            ArrayList<Integer> cardStudents = new ArrayList<>();
            String color;
            for(int i=0; i<3; i++){
                System.out.println("Which student on the card?");
                color = scanner.next();
                if(translateColor(color)==-1) return false;
                cardStudents.add(translateColor(color));
                System.out.println("Which student in the entrance?");
                color = scanner.next();
                if(translateColor(color)==-1) return false;
                entranceStudents.add(translateColor(color));
                if(i<2) {
                    System.out.println("Do you want to move student again? [Y/N]");
                    String answer = scanner.next();
                    if(answer.equalsIgnoreCase("n")) break;
                }
                if(proxy.useSpecial(special, 0, entranceStudents, cardStudents)) return true;
            }
        } else if(special == 9){
            System.out.println("Which color?");
            String color = scanner.next();
            if(translateColor(color)==-1) return false;
            if(proxy.useSpecial(special, translateColor(color), null, null)) return true;
        } else if(special == 10){
            ArrayList<Integer> entranceStudents = new ArrayList<>();
            ArrayList<Integer> tableStudents = new ArrayList<>();
            String color;
            for(int i=0; i<2; i++) {
                System.out.println("Which student on the table?");
                color = scanner.next();
                if (translateColor(color) == -1) return false;
                tableStudents.add(translateColor(color));
                System.out.println("Which student in the entrance?");
                color = scanner.next();
                if (translateColor(color) == -1) return false;
                entranceStudents.add(translateColor(color));
                if (i < 1) {
                    System.out.println("Do you want to move student again? [Y/N]");
                    String answer = scanner.next();
                    if (answer.equalsIgnoreCase("n")) break;
                }
            }
            if(proxy.useSpecial(special, 0, entranceStudents, tableStudents)) return true;
        } else if(special==11){
            System.out.println("Which student do you want to move? Insert color");
            String color = scanner.next();
            if(translateColor(color)==-1) return false;
            ArrayList<Integer> color1 = new ArrayList<>();
            color1.add(translateColor(color));
            if(proxy.useSpecial(special, -1, color1, null)) return true;
        } else if(special==12){
            System.out.println("Which color? Insert color");
            String color = scanner.next();
            if(translateColor(color)==-1) return false;
            if(proxy.useSpecial(special, translateColor(color), null, null)) return true;
        }
        return false;
    }

    public void playCard() throws IOException, ClassNotFoundException {
        System.out.println("Which card do you want to play? Insert the card number");
        int card;
        try {
            card = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Error, try again");
            return;
        }
        if (proxy.playCard(card)) constants.setCardPlayed(true);
    }

    public void moveStudents() {
        boolean finished = false;
        do {
            String color;
            String where;
            int islandRef = -1;
            try {
                System.out.println("Which student do you want to move? Insert color");
                color = scanner.next();
                int colorInt = translateColor(color);
                System.out.println("Where do you want to move the student? School or Island");
                where = scanner.next();
                if (where.equalsIgnoreCase("island")) {
                    System.out.println("which island? insert the number");
                    islandRef = scanner.nextInt();
                }
                if (proxy.moveStudent(colorInt, where, islandRef)) finished = true;
            } catch (InputMismatchException e) {
                System.out.println("Error, try again");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } while (!finished);
        constants.setStudentMoved(true);
    }

    public void moveMotherNature() throws IOException, ClassNotFoundException {
        int steps = -1;
        try {
            do {
                System.out.println("How many steps?");
                steps = scanner.nextInt();
            } while (steps <= 0);
        }catch (InputMismatchException e){
            System.out.println("error, try again");
            return;
        }
        if (proxy.moveMotherNature(steps)) constants.setMotherMoved(true);
    }

    public void chooseCloud() throws IOException, ClassNotFoundException {
        int cloud = -1;
        try{
            do {
                System.out.println("Which cloud do you want)");
                cloud = scanner.nextInt();
            } while (cloud == -1);
        }catch (InputMismatchException e){
            System.out.println("error, try again");
            return;
        }
        if (proxy.chooseCloud(cloud)) constants.setCloudChosen(true);
    }

    @Override
    public void run() {
        try {
            setup();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        while (active) {
            try {
                if (proxy.clientWait()) constants.resetAll();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
            while (!constants.isEndTurn()) {
                try {
                    turn();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        scanner.close();

    }

    private int translateColor(String color) {
        switch (color.toLowerCase()) {
            case ("green"):
                return 0;
            case ("red"):
                return 1;
            case ("yellow"):
                return 2;
            case ("pink"):
                return 3;
            case ("blue"):
                return 4;
            default:
                return -1;
        }


    }
}
