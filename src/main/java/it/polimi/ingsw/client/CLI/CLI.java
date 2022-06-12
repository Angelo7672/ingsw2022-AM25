package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.PlayerConstants;
import it.polimi.ingsw.client.Proxy_c;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.controller.listeners.*;
import it.polimi.ingsw.server.answer.SavedGameAnswer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class CLI implements Runnable, TowersListener, ProfessorsListener, SpecialListener, PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener{

    private final Exit proxy;
    private final Scanner scanner;
    private boolean active;
    private final PlayerConstants constants;
    private View view;
    private final Socket socket;
    private String winner;
    private Printable printable;
    private String ANSI_RESET = "\u001B[0m";
    private String ANSI_RED = "\u001B[31m";
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
        else if(result.equals("LoginRestore")){
            loginRestore();
        }
        else  if (result.equals("Server Sold Out")){
            System.out.println();
            System.out.print(ANSI_RED+SPACE+result+ANSI_RESET);
            return;
        }
        if(!restoreGame) {
            setupConnection();
        }
        view = proxy.startView();
        view.setCoinsListener(this);
        view.setInhibitedListener(this);
        view.setIslandListener(this);
        view.setMotherPositionListener(this);
        view.setPlayedCardListener(this);
        view.setProfessorsListener(this);
        view.setStudentsListener(this);
        view.setTowersListener(this);
        printable = new Printable(view);
        System.out.println();
        System.out.println(SPACE+"Game is started! Wait for your turn...");
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
                System.out.print(SPACE+"Choose a character: "+ availableCharacters + " ");
                character = scanner.next().toUpperCase(Locale.ROOT);
                if (!availableCharacters.contains(character)) {
                    System.out.println();
                    System.out.println(ANSI_RED+SPACE+"Error, choose an available character"+ANSI_RESET);
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
                System.out.println(ANSI_RED+SPACE+"Error, try again"+ANSI_RESET);
            }
        }
    }

    public void setupGame(){
        int numberOfPlayers;
        String expertMode;
        while (true){
            try {
                while (true){
                    System.out.println();
                    System.out.print(SPACE+"Insert number of player: ");
                    String intString = scanner.next();
                    numberOfPlayers = Integer.parseInt(intString);
                    if(numberOfPlayers<2 || numberOfPlayers > 4){
                        System.out.println();
                        System.out.println(ANSI_RED+SPACE+"Error, insert a number between 2 and 4"+ANSI_RESET);
                    }
                    else break;
                }
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
                    System.out.println(ANSI_RED+SPACE+"Error, try again"+ANSI_RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println(ANSI_RED+SPACE+"Error, insert a number"+ANSI_RESET);
            } catch (IOException e) {
                System.err.println("io");
            } catch (ClassNotFoundException e) {
                System.err.println("class error");
            }
        }
    }

    private boolean savedGame() throws IOException, ClassNotFoundException {
        SavedGameAnswer savedGame = (SavedGameAnswer) proxy.getMessage();
        String decision;
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
                    System.out.println(ANSI_RED+SPACE+"Error, insert y or n"+ANSI_RESET);
                }
            }while(decision == null);
            if (proxy.savedGame(decision)) break;
            else {
                System.out.println();
                System.out.println(ANSI_RED+SPACE+"Error, try again"+ANSI_RESET);
            }
        }
        if(decision.equals("n")) return false;
        while (true){
            if(proxy.readyForLogin()) {
                loginRestore();
                return true;
            }
        }
    }

    private void loginRestore() throws IOException, ClassNotFoundException {
        while (true){
            System.out.println();
            System.out.println("Insert nickname for restore last game: ");
            String nickname = scanner.next();
            if(proxy.setupConnection(nickname, null)) return;
            else {
                System.out.println();
                System.out.print(ANSI_RED+SPACE+"Error, insert your previous nickname"+ANSI_RESET);
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
                System.out.println(ANSI_RED+SPACE+"Error, insert a number"+ANSI_RESET);
                useSpecial();
            }
            special = special-1;
            if(!proxy.checkSpecial(special)) {
                System.out.println();
                System.out.println(ANSI_RED+SPACE+"Error, special not present"+ANSI_RESET);
                useSpecial();
            }
            if(special(special)) return;
        }
        System.out.println();
        System.out.println(ANSI_RED+SPACE+"Error, try again"+ANSI_RESET);
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
                    System.out.print(ANSI_RED+SPACE+"Error, enter an existing color"+ANSI_RESET);
                    return false;
                }
                return proxy.useSpecial(special, translateColor(color), null, null);
            }
            return false;
        }catch (NumberFormatException e){
            System.out.println();
            System.out.println(ANSI_RED+SPACE+"Error, insert a number"+ANSI_RESET);
            return false;
        }
    }

    public void playCard() throws IOException, ClassNotFoundException {
        printable.cli();
        System.out.println();
        System.out.print(SPACE+"Which card do you want to play? ");
        String card;
        try {
            card = scanner.next();
        } catch (InputMismatchException e) {
            System.out.println();
            System.out.println(ANSI_RED+SPACE+"Error, try again"+ANSI_RESET);
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
        printable.cli();
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
                        System.out.println(ANSI_RED+SPACE+"Error, enter an existing color"+ANSI_RESET);
                        color = null;
                    }
                }
                while(where==null) {
                    System.out.println();
                    System.out.print(SPACE+"Where do you want to move the student? School or Island ");
                    where = scanner.next();
                    if(!where.equalsIgnoreCase("island")&&!where.equalsIgnoreCase("school")){
                        System.out.println();
                        System.out.println(ANSI_RED+SPACE+"Error, insert school or island"+ANSI_RESET);
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
                            System.out.println(ANSI_RED+SPACE+"Error, insert an existing island"+ANSI_RESET);
                            islandRef = -1;
                        }
                    }
                }
                accepted = proxy.moveStudent(colorInt, where, islandRef);
                if (accepted.equals("transfer complete")) finished = true;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println();
                System.out.println(ANSI_RED+SPACE+"Error, try again"+ANSI_RESET);
            } catch (NumberFormatException e){
                System.out.println();
                System.out.println(ANSI_RED+SPACE+"Error, insert a number"+ANSI_RESET);
            }
        } while (!finished);
        constants.setStudentMoved(true);
    }

    public void moveMotherNature() throws IOException, ClassNotFoundException {
        int steps = -1;
        try {
            do {
                printable.printMotherNature();
                System.out.println();
                System.out.print(SPACE+"How many steps do you want to move Mother Nature? Maximum number of steps "+view.getMaxStepsMotherNature()+" ");
                String intString = scanner.next();
                steps = Integer.parseInt(intString);
                if(steps>view.getMaxStepsMotherNature()) {
                    System.out.println();
                    System.out.println(ANSI_RED+SPACE+"Error, insert a number between 1 and "+view.getMaxStepsMotherNature()+ANSI_RESET);
                    steps=-1;
                }
            } while (steps <= 0);
        }catch (NumberFormatException e){
            System.out.println();
            System.out.println(ANSI_RED+SPACE+"Error, insert a number."+ANSI_RESET);
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
                printable.printCloud();
                System.out.println();
                System.out.print(SPACE+"Which cloud do you want? ");
                String intString = scanner.next();
                cloud = Integer.parseInt(intString);
                if(cloud<=0 || cloud>view.getNumberOfPlayers()) {
                    cloud = -1;
                    System.out.println();
                    System.out.println(ANSI_RED+SPACE+"Error, insert an existing cloud."+ANSI_RESET);
                }
            } while (cloud == -1);
        }catch (NumberFormatException e){
            System.out.println();
            System.out.println(ANSI_RED+SPACE+"Error, insert a number."+ANSI_RESET);
            return;
        }
        cloud = cloud-1;
        String result = proxy.chooseCloud(cloud);
        if (result.equalsIgnoreCase("ok")) {
            constants.setCloudChosen(true);
            System.out.println();
            printable.cli();
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

    @Override
    public void notifyNewCoinsValue(int playerRef, int newCoinsValue) {
        if(constants.isStartGame()) {
            printable.printCoins(playerRef);
        }
    }

    @Override
    public void notifyInhibited(int islandRef, int isInhibited) {
        if(constants.isStartGame()) {
            System.out.println();
            System.out.print("New play: "+"\t"+" island "+islandRef+" No Entry tiles: "+isInhibited);
        }
    }

    @Override
    public void notifyIslandChange(int islandToDelete) {
        if(constants.isStartGame()) {
            System.out.println();
            System.out.print("New play: "+"\t"+" island "+islandToDelete+" had been united.");
        }
    }

    @Override
    public void notifyMotherPosition(int newMotherPosition) {
        if(constants.isStartGame()) {
            System.out.println();
            System.out.print("New play: "+"\t");
            printable.printMotherNature();
        }
    }

    @Override
    public void notifyPlayedCard(int playerRef, String assistantCard) {
        if(constants.isStartGame()) {
            printable.printLastCard();
        }
    }

    @Override
    public void notifyHand(int playerRef, ArrayList<String> hand) {

    }

    @Override
    public void notifyProfessors(int playerRef, int color, boolean newProfessorValue) {
        if(constants.isStartGame()) {
            printable.printProf(playerRef);
        }
    }

    @Override
    public void notifySpecial(int specialRef) {
        printable.cli();
    }

    @Override
    public void notifySpecialName(String specialName) {

    }

    @Override
    public void notifyPlayedSpecial(int specialRef) {
        printable.cli();
    }

    @Override
    public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {
        if(constants.isStartGame()&&!constants.isEndTurn()) {
            printable.printStudentsChange(place, componentRef);
        }
    }

    @Override
    public void notifyTowersChange(int place, int componentRef, int towersNumber) {
        if(constants.isStartGame()&&!constants.isEndTurn()) {
            printable.printTowersChange(place, componentRef);
        }
    }

    @Override
    public void notifyTowerColor(int islandRef, int newColor) {
        if(constants.isStartGame()&&!constants.isEndTurn()) {
            printable.printTowersOwner(islandRef);
        }
    }
}