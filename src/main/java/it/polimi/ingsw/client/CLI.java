package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.listeners.*;
import it.polimi.ingsw.server.Answer.Answer;
import it.polimi.ingsw.server.Answer.LoginRestoreAnswer;
import it.polimi.ingsw.server.Answer.SavedGameAnswer;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class CLI implements Runnable, TowersListener, ProfessorsListener, SpecialListener, PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener, BagListener,
        QueueListener {

    private final Exit proxy;
    private final Scanner scanner;
    private boolean active;
    private final PlayerConstants constants;
    private View view;
    private final Socket socket;
    private String winner;

    private String ANSI_RESET = "\u001B[0m";
    private String ANSI_BLACK = "\u001B[30m";
    private String ANSI_RED = "\u001B[31m";
    private String ANSI_GREEN = "\u001B[32m";
    private String ANSI_YELLOW = "\u001B[33m";
    private String ANSI_BLUE = "\u001B[34m";
    private String ANSI_PURPLE = "\u001B[35m";
    private String ANSI_CYAN = "\u001B[36m";
    private String ANSI_WHITE = "\u001B[37m";
    private String  UNDERLINE = "\u001B[4m";
    private String SPACE = "\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";


    public CLI(Socket socket) throws IOException{
        this.socket = socket;
        scanner = new Scanner(System.in);
        active = true;
        constants = new PlayerConstants();
        proxy = new Proxy_c(socket);
    }

    public void setup() throws IOException, ClassNotFoundException, InterruptedException {
        boolean restoreGame=false;
        System.out.println();
        System.out.println(SPACE+"Waiting for server...");
        String result = proxy.first();
        if(result.equals("SavedGame")){
            restoreGame = true;
            if(!savedGame()){
                setupGame();
                setupConnection();
            }
        }
        else if(result.equals("SetupGame")) {
            setupGame();
        }
        else  if (result.equals("Server Sold Out")){
            System.err.println(SPACE+result);
            return;
        }
        if(!restoreGame) {
            setupConnection();
        }
        view = proxy.startView();
        view.setBagListener(this);
        view.setCoinsListener(this);
        view.setInhibitedListener(this);
        view.setIslandListener(this);
        view.setMotherPositionListener(this);
        view.setPlayedCardListener(this);
        view.setProfessorsListener(this);
        view.setStudentsListener(this);
        view.setTowersListener(this);
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
                System.out.println();
                System.out.print(SPACE+"Insert your nickname: ");
                nickname = scanner.next();
            } while (nickname == null);
            do {
                System.out.println();
                System.out.print(SPACE+"Choose a character: "+availableCharacters.toString()+ " ");
                character = scanner.next().toUpperCase(Locale.ROOT);
                if (!availableCharacters.contains(character)) {
                    System.out.println();
                    System.err.println("Error, choose an available character");
                    character=null;
                }
            } while (character == null);
            if (proxy.setupConnection(nickname, character)) {
                System.out.println();
                System.out.println(SPACE+"Setup Connection done, waiting for players...");
                break;
            }
            else {
                System.out.println();
                System.err.println(SPACE+"Error, try again");
            }
        }
    }

    public void setupGame(){
        int numberOfPlayers;
        String expertMode;
        while (true){
            try {
                System.out.println();
                System.out.print(SPACE+"Insert number of player ");
                do {
                    String intString = scanner.next();
                    numberOfPlayers = Integer.parseInt(intString);
                }while (numberOfPlayers<2 || numberOfPlayers>4);
                do{
                    System.out.println();
                    System.out.print(SPACE+"Expert mode? [y/n] ");
                    expertMode = scanner.next();
                }while(expertMode==null);
                if (proxy.setupGame(numberOfPlayers, expertMode)) {
                    return;
                }
                else {
                    System.out.println();
                    System.err.println(SPACE+"Error, try again");
                }
            } catch (NumberFormatException e) {
                System.err.println("Error, insert a number");
            } catch (IOException e) {
                System.err.println("io");
            } catch (ClassNotFoundException e) {
                System.err.println("class error");
            }
        }
    }

    private boolean savedGame() throws IOException, ClassNotFoundException {
        SavedGameAnswer savedGame = (SavedGameAnswer) proxy.getMessage();
        String decision = null;
        while(true) {
            System.out.println();
            System.out.print(SPACE+"There is a game started: number of players: " + savedGame.getNumberOfPlayers() + " . Expert mode: ");
            if (savedGame.isExpertMode()) System.out.println("yes");
            else System.out.println("no");
            System.out.println();
            System.out.print(SPACE+"Do you want to continue it? [y/n] ");
            do{
                decision = scanner.next();
                if(!decision.equalsIgnoreCase("n") && !decision.equalsIgnoreCase("y")){
                    decision=null;
                    System.out.println();
                    System.err.println(SPACE+"Error, insert y or n");
                }
            }while(decision == null);
            if (proxy.savedGame(decision)) break;
            else {
                System.out.println();
                System.err.println(SPACE+"Error, try again.");
            }
        }
        if(decision.equals("n")) return false;
        while (true){
            System.out.println();
            System.out.print(SPACE+"Insert nickname for restore last game: ");
            String nickname = scanner.next();
            if(proxy.setupConnection(nickname, null)) return true;
            else {
                System.out.println();
                System.err.println(SPACE+"Error, insert your previous nickname");
            }
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void useSpecial() throws IOException, ClassNotFoundException {
        System.out.println();
        System.out.print(SPACE+"Do you want to use a special card? [y/n] ");
        String answer = scanner.next();
        if (answer.equalsIgnoreCase("n")) return;
        else if (answer.equalsIgnoreCase("y")) {
            int special = -1;
            try {
                do {
                    System.out.println();
                    System.out.print(SPACE+"Which special do you want to use? Insert number ");
                    String intString = scanner.next();
                    special = Integer.parseInt(intString);
                } while (special == -1);
            }catch (NumberFormatException e){
                System.out.println();
                System.err.println(SPACE+"Error, insert a number.");
                useSpecial();
            }
            special = special-1;
            if(!proxy.checkSpecial(special)) {
                System.out.println();
                System.err.println(SPACE+"Error, special not present");
                useSpecial();
            }
            if(special(special)) return;
        }
        System.out.println();
        System.err.println(SPACE+"Error, try again");
        useSpecial();
    }

    private boolean special(int special) throws IOException, ClassNotFoundException {
        try {
            if (special == 2 || special == 4 || special == 6 || special == 8) {
                return proxy.useSpecial(special, 0, null, null);
            } else if (special == 1) {
                System.out.println();
                System.out.print(SPACE+"Which student do you want to move? Insert color ");
                String color = scanner.next();
                if (translateColor(color) == -1) return false;
                ArrayList<Integer> color1 = new ArrayList<>();
                color1.add(translateColor(color));
                int island = -1;
                do {
                    System.out.println();
                    System.out.print(SPACE+"In witch island? Insert the number ");
                    String intString = scanner.next();
                    island = Integer.parseInt(intString);
                } while (island == -1);
                island = island - 1;
                return proxy.useSpecial(special, island, color1, null);
            } else if (special == 3 || special == 5) {
                int island = -1;
                do {
                    System.out.println();
                    System.out.print(SPACE+"Which island? Insert the number ");
                    String intString = scanner.next();
                    island = Integer.parseInt(intString);
                } while (island == -1);
                island = island - 1;
                return proxy.useSpecial(special, island, null, null);
            } else if (special == 7) {
                ArrayList<Integer> entranceStudents = new ArrayList<>();
                ArrayList<Integer> cardStudents = new ArrayList<>();
                String color;
                for (int i = 0; i < 3; i++) {
                    System.out.println();
                    System.out.print(SPACE+"Which student on the card?" );
                    color = scanner.next();
                    if (translateColor(color) == -1) return false;
                    cardStudents.add(translateColor(color));
                    System.out.println();
                    System.out.print(SPACE+"Which student in the entrance? ");
                    color = scanner.next();
                    if (translateColor(color) == -1) return false;
                    entranceStudents.add(translateColor(color));
                    if (i < 2) {
                        System.out.println();
                        System.out.print(SPACE+"Do you want to move student again? [Y/N] ");
                        String answer = scanner.next();
                        if (answer.equalsIgnoreCase("n")) break;
                    }
                    if (proxy.useSpecial(special, 0, entranceStudents, cardStudents)) return true;
                }
            } else if (special == 9) {
                System.out.println();
                System.out.print(SPACE+"Which color? ");
                String color = scanner.next();
                if (translateColor(color) == -1) return false;
                return proxy.useSpecial(special, translateColor(color), null, null);
            } else if (special == 10) {
                ArrayList<Integer> entranceStudents = new ArrayList<>();
                ArrayList<Integer> tableStudents = new ArrayList<>();
                String color;
                for (int i = 0; i < 2; i++) {
                    System.out.println();
                    System.out.print(SPACE+"Which student on the table? ");
                    color = scanner.next();
                    if (translateColor(color) == -1) return false;
                    tableStudents.add(translateColor(color));
                    System.out.println();
                    System.out.print(SPACE+"Which student in the entrance? ");
                    color = scanner.next();
                    if (translateColor(color) == -1) return false;
                    entranceStudents.add(translateColor(color));
                    if (i < 1) {
                        System.out.println();
                        System.out.print(SPACE+"Do you want to move student again? [Y/N] ");
                        String answer = scanner.next();
                        if (answer.equalsIgnoreCase("n")) break;
                    }
                }
                return proxy.useSpecial(special, 0, entranceStudents, tableStudents);
            } else if (special == 11) {
                System.out.println();
                System.out.print(SPACE+"Which student do you want to move? Insert color ");
                String color = scanner.next();
                if (translateColor(color) == -1) return false;
                ArrayList<Integer> color1 = new ArrayList<>();
                color1.add(translateColor(color));
                return proxy.useSpecial(special, -1, color1, null);
            } else if (special == 12) {
                System.out.println();
                System.out.print(SPACE+"Which color? Insert color ");
                String color = scanner.next();
                if (translateColor(color) == -1) {
                    System.out.println();
                    System.err.println(SPACE+"Error, enter an existing color");
                    return false;
                }
                return proxy.useSpecial(special, translateColor(color), null, null);
            }
            return false;
        }catch (NumberFormatException e){
            System.out.println();
            System.err.println(SPACE+"Error, insert a number.");
            return false;
        }
    }

    public void playCard() throws IOException, ClassNotFoundException {
        cli();
        System.out.println();
        System.out.print(SPACE+"Which card do you want to play? ");
        String card;
        try {
            card = scanner.next();
        } catch (InputMismatchException e) {
            System.out.println();
            System.err.println(SPACE+"Error, try again");
            return;
        }
        String result = proxy.playCard(card);
        if (!result.equalsIgnoreCase("ok")) System.out.println(result);
        else {
            constants.setCardPlayed(true);
            System.out.println();
            System.out.println(SPACE+"It's your opponent turn, wait...");
        }
    }

    public void moveStudents() {
        cli();
        boolean finished = false;
        do {
            String accepted;
            String color=null;
            String where=null;
            int colorInt=-1;
            int islandRef = -1;
            try {
                while(color==null) {
                    System.out.println();
                    System.out.print(SPACE+"Which student do you want to move? Insert color ");
                    color = scanner.next();
                    colorInt = translateColor(color);
                    if(colorInt==-1) {
                        System.out.println();
                        System.err.println(SPACE+"Error, enter an existing color");
                        color = null;
                    }
                }
                while(where==null) {
                    System.out.println();
                    System.out.print(SPACE+"Where do you want to move the student? School or Island ");
                    where = scanner.next();
                    if(!where.equalsIgnoreCase("island")&&!where.equalsIgnoreCase("school")){
                        System.out.println();
                        System.err.println(SPACE+"Error, insert school or island");
                        where=null;
                    }
                }
                if (where.equalsIgnoreCase("island")) {
                    while (islandRef==-1) {
                        System.out.println();
                        System.out.print(SPACE+"Which island? insert the number ");
                        String intString = scanner.next();
                        islandRef = Integer.parseInt(intString);
                        islandRef = islandRef - 1;
                        if(islandRef<0 || islandRef>=view.getIslandSize()){
                            System.out.println();
                            System.err.println(SPACE+"Error, insert an existing island");
                            islandRef = -1;
                        }
                    }
                }
                accepted = proxy.moveStudent(colorInt, where, islandRef);
                if (accepted.equals("transfer complete")) finished = true;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println();
                System.err.println(SPACE+"Error, try again");
            } catch (NumberFormatException e){
                System.out.println();
                System.err.println(SPACE+"Error, insert a number.");
            }
        } while (!finished);
        constants.setStudentMoved(true);
    }

    public void moveMotherNature() throws IOException, ClassNotFoundException {
        int steps = -1;
        try {
            do {
                printMotherNature();
                System.out.println();
                System.out.print(SPACE+"How many steps do you want to move Mother Nature? Maximum number of steps "+view.getMaxStepsMotherNature()+" ");
                String intString = scanner.next();
                steps = Integer.parseInt(intString);
                if(steps>view.getMaxStepsMotherNature()) {
                    System.out.println();
                    System.out.println(SPACE+"Error, insert a number between 1 and "+view.getMaxStepsMotherNature());
                    steps=-1;
                }
            } while (steps <= 0);
        }catch (NumberFormatException e){
            System.out.println();
            System.err.println(SPACE+"error, insert a number.");
            return;
        }
        String result = proxy.moveMotherNature(steps);
        if (result.equalsIgnoreCase("ok")) constants.setMotherMoved(true);
        else System.out.println(result);
    }

    public void chooseCloud() throws IOException, ClassNotFoundException {
        int cloud = -1;
        constants.setEndTurn(true);
        try{
            do {
                printCloud();
                System.out.println();
                System.out.print(SPACE+"Which cloud do you want? ");
                String intString = scanner.next();
                cloud = Integer.parseInt(intString);
                if(cloud<=0 || cloud>view.getNumberOfPlayers()) {
                    cloud = -1;
                    System.out.println();
                    System.err.println(SPACE+"Error, enter an existing number");
                }
            } while (cloud == -1);
        }catch (NumberFormatException e){
            System.out.println();
            System.err.println(SPACE+"error, insert a number");
            return;
        }
        cloud = cloud-1;
        String result = proxy.chooseCloud(cloud);
        if (result.equalsIgnoreCase("ok")) {
            constants.setCloudChosen(true);
            System.out.println();
            cli();
            System.out.println(SPACE+"it's your opponent turn, wait...");
            constants.setEndTurn(false);
        }
        else System.out.println(result);
    }

    @Override
    public void run() {
        try {
            setup();
            while (active) {
                while(true) {
                    if (proxy.startPlanningPhase()){
                        constants.resetAll();
                        break;
                    }
                }
                while (!constants.isCloudChosen()) {
                    turn();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
        System.err.println("io / class in run");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scanner.close();
    }

    public void turn() throws IOException, ClassNotFoundException {
        winner = proxy.getWinner();
        if(winner!=null){
            System.out.println("Game Over, the winner is "+winner);
            socket.close();
        }
        if (!constants.isSpecialUsed() && constants.isActionPhaseStarted() && view.getExpertMode()) useSpecial();
        phaseHandler(constants.lastPhase());
    }

    public void phaseHandler(String phase) throws IOException, ClassNotFoundException {
        if(!constants.isStartGame()) constants.setStartGame(true);
        if(phase.equals("PlayCard")) {
            playCard();
        }
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

    private void printIsland(int i){
        System.out.println("\t"+"Island " + (i + 1) + ": Students:" + ANSI_GREEN +" Green "+ view.getStudentsIsland(i)[0 ] + ANSI_RED  + ", Red " + view.getStudentsIsland(i)[1] +
                ANSI_YELLOW +", Yellow " + view.getStudentsIsland(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsIsland(i)[3] + ANSI_BLUE + ", Blue " + view.getStudentsIsland(i)[4]+ANSI_RESET);
        if (view.getInhibited(i) != 0) System.out.print("\t"+"\t"+"     No Entry tiles: " + view.getInhibited(i) + ". ");
        if(view.getTowersColor(i)==0) System.out.print("\t"+"\t"+"      Towers Team: " + "WHITE" + ANSI_RESET);
        else if(view.getTowersColor(i)==1) System.out.print("\t"+"\t"+"      Towers Team: " + ANSI_BLACK+ "BLACK" + ANSI_RESET);
        else if(view.getTowersColor(i)==2) System.out.print("\t"+"\t"+"      Towers Team: " + ANSI_WHITE+ "GREY" + ANSI_RESET);
        else if(view.getTowersColor(i)==-1) System.out.print("\t"+"\t"+"      Towers Team: " + "NO ONE" + ANSI_RESET);
        System.out.println(". Towers value: " + view.getIslandTowers(i));
    }
    private void printMotherNature(){
        System.out.println();
        System.out.println("\t"+UNDERLINE+"Mother Nature"+ANSI_RESET+" is on island " + (view.getMotherPosition()+1));//aggiungere
        System.out.println();
    }
    private void printNickname(int i){
        System.out.print("\t"+"School of " + view.getNickname(i)+" ");
    }
    private void printEntrance(int i){
        System.out.println(" Entrance students:" + ANSI_GREEN + " Green " + view.getStudentsEntrance(i)[0] + ANSI_RED  + ", Red " + view.getStudentsEntrance(i)[1] +
                ANSI_YELLOW + ", Yellow " + view.getStudentsEntrance(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsEntrance(i)[3] + ANSI_BLUE + ", Blue " + view.getStudentsEntrance(i)[4]+ANSI_RESET);
    }
    private void printTable(int i){
        System.out.println(" Table students:" + ANSI_GREEN + " Green " + view.getStudentsTable(i)[0] + ANSI_RED  + ", Red " + view.getStudentsTable(i)[1] +
                ANSI_YELLOW + ", Yellow " + view.getStudentsTable(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsTable(i)[3] + ANSI_BLUE + ", Blue " + view.getStudentsTable(i)[4]+ANSI_RESET);
    }
    private void printProf(int i){
        System.out.println(" Professor: " + ANSI_GREEN + "Green "+ view.getProfessors(i)[0] + ANSI_RED  + ", Red " + view.getProfessors(i)[1] +
                ANSI_YELLOW + ", Yellow " + view.getProfessors(i)[2] +ANSI_PURPLE + ", Pink " + view.getProfessors(i)[3] + ANSI_BLUE+ ", Blue " + view.getProfessors(i)[4]+ANSI_RESET);
    }

    private void printCloud(){
        System.out.println();
        System.out.println(UNDERLINE+"CLOUDS:"+ANSI_RESET);
        for (int i = 0; i < view.getNumberOfPlayers(); i++) {
            if (view.getStudentsCloud(i)[0]+view.getStudentsCloud(i)[1]+view.getStudentsCloud(i)[2]+view.getStudentsCloud(i)[3]+view.getStudentsCloud(i)[4] != 0){
                System.out.println("\t"+"Cloud " + (i + 1) + ": Students:" +ANSI_GREEN + " Green " + view.getStudentsCloud(i)[0] + ANSI_RED + ", Red " + view.getStudentsCloud(i)[1] +
                        ANSI_YELLOW + ", Yellow " + view.getStudentsCloud(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsCloud(i)[3] + ANSI_BLUE+ ", Blue " + view.getStudentsCloud(i)[4]+ANSI_RESET);
            }
        }
        System.out.println();
    }

    private void printLastCard(){
        System.out.print(UNDERLINE+"LAST PLAYED CARDS:"+ANSI_RESET);
        for (int i = 0; i < view.getNumberOfPlayers(); i++) {
            System.out.print(" "+view.getNickname(i) + ": " + view.getLastCard(i) + ". ");
        }
        System.out.println();
    }

    public void cli(){

        System.out.println(System.lineSeparator().repeat(10));
        /*System.out.print("\033[H\033[2J");
        System.out.flush();*/
        System.out.println(UNDERLINE+"ISLANDS"+ANSI_RESET);
        for (int i = 0; i < view.getIslandSize(); i++) {
            System.out.println("\t"+"Island " + (i + 1) + ": Students:" + ANSI_GREEN +" Green "+ view.getStudentsIsland(i)[0 ] + ANSI_RED  + ", Red " + view.getStudentsIsland(i)[1] +
                    ANSI_YELLOW +", Yellow " + view.getStudentsIsland(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsIsland(i)[3] + ANSI_BLUE + ", Blue " + view.getStudentsIsland(i)[4]+ANSI_RESET);
            if (view.getInhibited(i) != 0) System.out.print("\t"+"\t"+"  No Entry tiles: " + view.getInhibited(i) + ". ");
            if(view.getTowersColor(i)==0) System.out.print("\t"+"\t"+"  Towers Team: " + "WHITE" + ANSI_RESET);
            else if(view.getTowersColor(i)==1) System.out.print("\t"+"\t"+"  Towers Team: " + ANSI_BLACK+ "BLACK" + ANSI_RESET);
            else if(view.getTowersColor(i)==2) System.out.print("\t"+"\t"+"  Towers Team: " + ANSI_WHITE+ "GREY" + ANSI_RESET);
            else if(view.getTowersColor(i)==-1) System.out.print("\t"+"\t"+"  Towers Team: " + "NO ONE" + ANSI_RESET);
            System.out.println(". Towers value: " + view.getIslandTowers(i));
        }
        printMotherNature();
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
        }

        System.out.println(UNDERLINE+"CLOUDS"+ANSI_RESET);
        for (int i = 0; i < view.getNumberOfPlayers(); i++) {
            if (view.getStudentsCloud(i)[0]+view.getStudentsCloud(i)[1]+view.getStudentsCloud(i)[2]+view.getStudentsCloud(i)[3]+view.getStudentsCloud(i)[4] != 0){
                System.out.println("\t"+"Cloud " + (i + 1) + ": Students:" +ANSI_GREEN + " Green " + view.getStudentsCloud(i)[0] + ANSI_RED + ", Red " + view.getStudentsCloud(i)[1] +
                        ANSI_YELLOW + ", Yellow " + view.getStudentsCloud(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsCloud(i)[3] + ANSI_BLUE+ ", Blue " + view.getStudentsCloud(i)[4]+ANSI_RESET);
            }
        }
        System.out.println();

        System.out.println(UNDERLINE+"LAST PLAYED CARDS"+ANSI_RESET);
        System.out.print("\t");
        for (int i = 0; i < view.getNumberOfPlayers(); i++) {
            System.out.print(view.getNickname(i) + ": " + view.getLastCard(i) + ". ");
        }
        System.out.println();
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

    @Override
    public void notifyBagExtraction() {

    }

    @Override
    public void notifyBag(List<Integer> bag) {

    }

    @Override
    public void notifyNewCoinsValue(int playerRef, int newCoinsValue) {
        cli();
    }

    @Override
    public void notifyInhibited(int islandRef, int isInhibited) {
        cli();
    }

    @Override
    public void notifyIslandChange(int islandToDelete) {

    }

    @Override
    public void notifyMotherPosition(int newMotherPosition) {
        if(constants.isStartGame()) printMotherNature();
    }

    @Override
    public void notifyPlayedCard(int playerRef, String assistantCard) {
        if(constants.isStartGame()) printLastCard();
    }

    @Override
    public void notifyHand(int playerRef, ArrayList<String> hand) {

    }

    @Override
    public void notifyProfessors(int playerRef, int color, boolean newProfessorValue) {
        if(constants.isStartGame()) {
            printNickname(playerRef);
            printProf(playerRef);
        }
    }


    @Override
    public void notifyQueue(int queueRef, int playerRef) {

    }

    @Override
    public void notifyValueCard(int queueRef, int valueCard) {

    }

    @Override
    public void notifyMaxMove(int queueRef, int maxMove) {

    }


    @Override
    public void notifySpecial(int specialRef) {
        cli();
    }

    @Override
    public void notifySpecialName(String specialName) {

    }

    @Override
    public void notifyPlayedSpecial(int specialRef) {
        cli();
    }

    @Override
    public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {
        if(constants.isStartGame()&&!constants.isEndTurn()) {
            switch (place) {
                case (0): {
                    printNickname(componentRef);
                    printEntrance(componentRef);
                    break;
                }
                case (1): {
                    printNickname(componentRef);
                    printTable(componentRef);
                    break;
                }
                case (2): {
                    printIsland(componentRef);
                    break;
                }
            }
        }
    }

    @Override
    public void notifyTowersChange(int place, int componentRef, int towersNumber) {

    }

    @Override
    public void notifyTowerColor(int islandRef, int newColor) {

    }
}
