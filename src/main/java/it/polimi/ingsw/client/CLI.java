package it.polimi.ingsw.client;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class CLI implements Runnable {

    private final Exit proxy;
    private final Scanner scanner;
    private boolean active;
    private final PlayerConstants constants;
    private View view;
    private final Socket socket;


    public CLI(Socket socket) throws IOException{
        this.socket = socket;
        scanner = new Scanner(System.in);
        active = true;
        constants = new PlayerConstants();
        proxy = new Proxy_c(socket);
    }

    public void setup() throws IOException, ClassNotFoundException {
        System.out.println("Waiting for server...");
        String result = proxy.first();
        if(result.equals("SetupGame")) {
            while(!setupGame()) System.out.println("Error, try again");
        }
        else  if (result.equals("Server Sold Out")){
            System.out.println(result);
            return;
        }
        setupConnection();
        view = proxy.startView();
    }

    public void setupConnection() throws IOException, ClassNotFoundException {
        while (true) {
            ArrayList<String> chosenCharacters = proxy.getChosenCharacters();
        ArrayList<String> availableCharacters = new ArrayList<>();
        if(!chosenCharacters.contains("WIZARD")) availableCharacters.add("WIZARD");
        if(!chosenCharacters.contains("KING")) availableCharacters.add("KING");
        if(!chosenCharacters.contains("WITCH")) availableCharacters.add("WITCH");
        if(!chosenCharacters.contains("SAMURAI")) availableCharacters.add("SAMURAI");
            String nickname;
            String character;
            do {
                System.out.println("Insert your nickname: ");
                nickname = scanner.next();
            } while (nickname == null);
            do {
                System.out.println("Choose a character: "+availableCharacters.toString());
                character = scanner.next().toUpperCase(Locale.ROOT);
                if (!availableCharacters.contains(character)) {
                    System.out.println("Error, choose an available character");
                    character=null;
                }
            } while (character == null);
            if (proxy.setupConnection(nickname, character)) {
                System.out.println("SetupConnection done");
                break;
            }
            else System.out.println("Error, try again");
        }
    }

    public boolean setupGame(){
        while(true) {
            int numberOfPlayers;
            String expertMode;
            try {
                System.out.println("Insert number of player");
                numberOfPlayers = scanner.nextInt();
                do{
                System.out.println("Expert mode? [y/n]");
                expertMode = scanner.next();
                }while(expertMode==null);
                if (proxy.setupGame(numberOfPlayers, expertMode)) return true;
                else System.out.println("Error, try again");
            } catch (InputMismatchException e) {
                return false;
            } catch (IOException e) {
                System.err.println("io");
            } catch (ClassNotFoundException e) {
                System.err.println("class error");
            }
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void useSpecial() throws IOException, ClassNotFoundException {
        cli();
        System.out.println("Do you want to use a special card? [y/n]");
        String answer = scanner.next();
        if (answer.equalsIgnoreCase("n")) return;
        else if (answer.equalsIgnoreCase("y")) {
            int special = -1;
            do {
                System.out.println("Which special do you want to use? Insert number");
                special = scanner.nextInt();
            } while (special == -1);
            special = special-1;
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
        if (special == 2 || special == 4 || special == 6 || special == 8) {
            return proxy.useSpecial(special, 0, null, null);
        }
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
            island = island-1;
            return proxy.useSpecial(special, island, color1, null);
        } else if (special == 3 || special == 5) {
            int island =-1;
            do {
                System.out.println("Which island? Insert the number");
                island = scanner.nextInt();
            } while(island==-1);
            island = island-1;
            return proxy.useSpecial(special, island, null, null);
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
            return proxy.useSpecial(special, translateColor(color), null, null);
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
            return proxy.useSpecial(special, 0, entranceStudents, tableStudents);
        } else if(special==11){
            System.out.println("Which student do you want to move? Insert color");
            String color = scanner.next();
            if(translateColor(color)==-1) return false;
            ArrayList<Integer> color1 = new ArrayList<>();
            color1.add(translateColor(color));
            return proxy.useSpecial(special, -1, color1, null);
        } else if(special==12){
            System.out.println("Which color? Insert color");
            String color = scanner.next();
            if(translateColor(color)==-1) return false;
            return proxy.useSpecial(special, translateColor(color), null, null);
        }
        return false;
    }

    public void playCard() throws IOException, ClassNotFoundException {
        cli();
        System.out.println("Which card do you want to play?");
        String card;
        try {
            card = scanner.next();
        } catch (InputMismatchException e) {
            System.err.println("Error, try again");
            return;
        }
        String result =proxy.playCard(card);
        if (!result.equalsIgnoreCase("ok")) System.out.println(result);
        else constants.setCardPlayed(true);
    }

    public void moveStudents() {
        boolean finished = false;
        do {
            cli();
            String accepted;
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
                    System.out.println("Which island? insert the number");
                    islandRef = scanner.nextInt();
                    islandRef = islandRef-1;
                }
                accepted = proxy.moveStudent(colorInt, where, islandRef);
                if (accepted.equals("transfer complete")) finished = true;
                System.out.println(accepted);
                System.out.println("acceped");
            } catch (InputMismatchException | IOException | ClassNotFoundException e) {
                System.err.println("Error, try again");
            }
        } while (!finished);
        constants.setStudentMoved(true);
    }

    public void moveMotherNature() throws IOException, ClassNotFoundException {
        cli();
        int steps = -1;
        try {
            do {
                System.out.println("How many steps do you want to move Mother Nature?");
                steps = scanner.nextInt();
            } while (steps <= 0);
        }catch (InputMismatchException e){
            System.err.println("error, try again");
            return;
        }
        String result = proxy.moveMotherNature(steps);
        if (result.equalsIgnoreCase("ok")) constants.setMotherMoved(true);
        else System.out.println(result);
    }

    public void chooseCloud() throws IOException, ClassNotFoundException {
        cli();
        int cloud = -1;
        try{
            do {
                System.out.println("Which cloud do you want?");
                cloud = scanner.nextInt();
            } while (cloud == -1);
        }catch (InputMismatchException e){
            System.err.println("error, try again");
            return;
        }
        cloud = cloud-1;
        String result = proxy.chooseCloud(cloud);
        if (result.equalsIgnoreCase("ok")) {
            constants.setCloudChosen(true);
        }
        else System.out.println(result);
    }

    @Override
    public void run() {
        try {
            setup();
            while (active) {
                if (proxy.startPlanningPhase()) constants.resetAll();
                while (!constants.isCloudChosen()) {
                    System.out.println("turn");
                    turn();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
        System.err.println("io / class in run");
        }
        scanner.close();
    }

    public void turn() throws IOException, ClassNotFoundException {
        if (!constants.isSpecialUsed() && constants.isActionPhaseStarted() && view.getExpertMode()) useSpecial();
        phaseHandler(constants.lastPhase());
    }

    public void phaseHandler(String phase) throws IOException, ClassNotFoundException {
        if(phase.equals("PlayCard")) playCard();
        else if(!constants.isActionPhaseStarted()) {
            constants.setActionPhaseStarted(proxy.startActionPhase());
        }
        else {

            switch (phase) {
                case ("MoveStudent") -> moveStudents();
                case ("MoveMother") -> moveMotherNature();
                case ("ChoseCloud") -> chooseCloud();
            }
        }
    }

    private int translateColor(String color) {
        return switch (color.toLowerCase()) {
            case ("green") -> 0;
            case ("red") -> 1;
            case ("yellow") -> 2;
            case ("pink") -> 3;
            case ("blue") -> 4;
            default -> -1;
        };
    }

    public void cli(){
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLACK = "\u001B[30m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_PURPLE = "\u001B[35m";
        String ANSI_CYAN = "\u001B[36m";
        String ANSI_WHITE = "\u001B[37m";
        String  UNDERLINE = "\u001B[4m";
        System.out.println(System.lineSeparator().repeat(100));
        /*System.out.print("\033[H\033[2J");
        System.out.flush();*/
        System.out.println(UNDERLINE+"ISLANDS"+ANSI_RESET);
        for (int i = 0; i < view.getIslandSize(); i++) {
            System.out.println("\t"+"Island " + (i + 1) + ": Students:" + ANSI_GREEN +" Green "+ view.getStudentsIsland(i)[0 ] + ANSI_RED  + ", Red " + view.getStudentsIsland(i)[1] +
                    ANSI_YELLOW +", Yellow " + view.getStudentsIsland(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsIsland(i)[3] + ANSI_BLUE + ", Blue " + view.getStudentsIsland(i)[4]+ANSI_RESET);
            if (view.getInhibited(i) != 0) System.out.print("\t"+"\t"+"\t"+"  No Entry tiles: " + view.getInhibited(i) + ". ");
            if(view.getTowersColor(i)==0) System.out.print("\t"+"\t"+"\t"+"  Towers Team: " + "WHITE" + ANSI_RESET);
            else if(view.getTowersColor(i)==1) System.out.print("\t"+"\t"+"\t"+"  Towers Team: " + ANSI_BLACK+ "BALCK" + ANSI_RESET);
            else if(view.getTowersColor(i)==2) System.out.print("\t"+"\t"+"\t"+"  Towers Team: " + ANSI_WHITE+ "GREY" + ANSI_RESET);
            else if(view.getTowersColor(i)==-1) System.out.print("\t"+"\t"+"\t"+"  Towers Team: " + "NO ONE" + ANSI_RESET);
            System.out.println(". Towers value: " + view.getIslandTowers(i));
        }
        System.out.println();
        System.out.println(UNDERLINE+"Mother Nature"+ANSI_RESET+" is on island " + view.getMotherPosition());//aggiungere
        System.out.println();

        System.out.println(UNDERLINE+"SCHOOLS"+ANSI_RESET);
        for (int i = 0; i < view.getNumberOfPlayers(); i++) {
            System.out.print("\t"+"Nickname: " + view.getNickname(i) + ". Character: " + view.getCharacter(i)+ ". Team: ");
            if(view.getTeam(i).equals("WHITE")) System.out.println(view.getTeam(i) + ".");
            if(view.getTeam(i).equals("BLACK")) System.out.println(ANSI_BLACK+view.getTeam(i) + "."+ANSI_RESET);
            if(view.getTeam(i).equals("GREY")) System.out.println(ANSI_WHITE+view.getTeam(i) + "."+ANSI_RESET);

            System.out.println("\t"+"Entrance students:" + ANSI_GREEN + " Green " + view.getStudentsEntrance(i)[0] + ANSI_RED  + ", Red " + view.getStudentsEntrance(i)[1] +
                    ANSI_YELLOW + ", Yellow " + view.getStudentsEntrance(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsEntrance(i)[3] + ANSI_BLUE + ", Blue " + view.getStudentsEntrance(i)[4]+ANSI_RESET);
            System.out.println("\t"+"Table students:" + ANSI_GREEN + " Green " + view.getStudentsTable(i)[0] + ANSI_RED  + ", Red " + view.getStudentsTable(i)[1] +
                    ANSI_YELLOW + ", Yellow " + view.getStudentsTable(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsTable(i)[3] + ANSI_BLUE + ", Blue " + view.getStudentsTable(i)[4]+ANSI_RESET);
            System.out.println("\t"+"Professor: " + ANSI_GREEN + "Green "+ view.getProfessors(i)[0] + ANSI_RED  + ", Red " + view.getProfessors(i)[1] +
                    ANSI_YELLOW + ", Yellow " + view.getProfessors(i)[2] +ANSI_PURPLE + ", Pink " + view.getProfessors(i)[3] + ANSI_BLUE+ ", Blue " + view.getProfessors(i)[4]+ANSI_RESET);
            System.out.print("\t"+"Towers number: " + view.getSchoolTowers(i) + ".");
            if (view.getExpertMode()) System.out.print(" Coins: " + view.getCoins(i));
            System.out.println();
            System.out.println();
        }
        System.out.println(UNDERLINE+"CLOUDS"+ANSI_RESET);
        for (int i = 0; i < view.getNumberOfPlayers(); i++) {
            System.out.println("\t"+"Cloud " + (i + 1) + ": Students:" +ANSI_GREEN + " Green " + view.getStudentsCloud(i)[0] + ANSI_RED + ", Red " + view.getStudentsCloud(i)[1] +
                    ANSI_YELLOW + ", Yellow " + view.getStudentsCloud(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsCloud(i)[3] + ANSI_BLUE+ ", Blue " + view.getStudentsCloud(i)[4]+ANSI_RESET);
        }
        System.out.println();

        System.out.println(UNDERLINE+"LAST PLAYED CARDS"+ANSI_RESET);
        System.out.print("\t");
        for (int i = 0; i < view.getNumberOfPlayers(); i++) {
            System.out.print(view.getNickname(i) + ": " + view.getLastCard(i) + ". ");
        }
        System.out.println();

        System.out.println(UNDERLINE+"YOUR CARDS"+ANSI_RESET);
        for (int i = 0; i < view.getCards().size(); i++) {
            System.out.println("\t"+view.getCards().get(i).name()+" value: "+view.getCards().get(i).getValue()+
                    " movement: "+view.getCards().get(i).getMovement());
        }

        if(view.getExpertMode()){
            System.out.println(UNDERLINE+"SPECIALS"+ANSI_RESET);
            for(int i=0; i<3; i++){
                System.out.print("\t"+view.getSpecialName(i)+": Cost "+view.getSpecialCost(i));
                if(specialArray(view.getSpecialName(i))) System.out.println(". Students: "+ ANSI_GREEN +" Green "+ view.getSpecialStudents(i)[0] + ANSI_RED  + ", Red " + view.getSpecialStudents(i)[1] +
                        ANSI_YELLOW +", Yellow " + view.getSpecialStudents(i)[2] + ANSI_PURPLE + ", Pink " + view.getSpecialStudents(i)[3] + ANSI_BLUE + ", Blue " + view.getSpecialStudents(i)[4]+ANSI_RESET);
                if(view.getNoEntry(i)!=-1) System.out.println(". No Entry tiles: "+view.getNoEntry(i));
            }
        }
        System.out.println();
        System.out.println();
    }

    private boolean specialArray(String name){
        if(name.equalsIgnoreCase("special1")) return true;
        if(name.equalsIgnoreCase("special7")) return true;
        if(name.equalsIgnoreCase("special8")) return true;
        return false;
    }
}
