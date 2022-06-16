package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.PlayerConstants;
import it.polimi.ingsw.client.Proxy_c;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.server.answer.SavedGameAnswer;

import java.awt.desktop.SystemEventListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class CLI implements Runnable, TowersListener, ProfessorsListener, SpecialListener, PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener, WinnerListener, DisconnectedListener{

    private final Exit proxy;
    private final Scanner scanner;
    private boolean active;
    private final PlayerConstants constants;
    private View view;
    private final Socket socket;
    private Printable printable;
    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_RED = "\u001B[31m";
    private final String SPACE = "\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";


    public CLI(Socket socket) throws IOException{
        this.socket = socket;
        scanner = new Scanner(System.in);
        active = true;
        constants = new PlayerConstants();
        proxy = new Proxy_c(socket);
        proxy.setDisconnectedListener(this);
    }

    private void setup() throws IOException, ClassNotFoundException, InterruptedException {
        boolean savedGame=false;
        boolean gameRestored = false;
        System.out.println();
        System.out.println(SPACE+"Waiting for server...");
        String result = proxy.first();
        if(result.equals("SavedGame")){
            savedGame = true;

            if(!savedGame()){
                setupGame();
                setupConnection();
            }
            else {
                System.out.println();
                System.out.println(SPACE + "Setup Connection done, waiting for players...");
                gameRestored = true;
            }
        }
        else if(result.equals("SetupGame")) {
            setupGame();
        }
        else if(result.equals("LoginRestore")){
            savedGame = true;
            gameRestored = true;
            loginRestore();
            System.out.println();
            System.out.println(SPACE+"Setup Connection done, waiting for players...");
        }
        else  if (result.equals("Server Sold Out")){
            System.out.println();
            System.out.print(ANSI_RED+SPACE+result+ANSI_RESET);
            socket.close();
            scanner.close();
            setActive(false);
        }
        if(!savedGame) {
            setupConnection();
        }
        //view.setDisconnectedListener(this);
        view = proxy.startView();
        view.setCoinsListener(this);
        view.setInhibitedListener(this);
        view.setIslandListener(this);
        view.setMotherPositionListener(this);
        view.setPlayedCardListener(this);
        view.setProfessorsListener(this);
        view.setStudentsListener(this);
        view.setTowersListener(this);
        view.setSpecialListener(this);
        view.setWinnerListener(this);
        printable = new Printable(view);
        System.out.println();
        System.out.println(SPACE+"Game is started! Wait for your turn...");
        if(gameRestored){
            setPhase();
        }
    }

    private void setPhase() throws IOException, ClassNotFoundException {
        String phase = proxy.getPhase();
        if(phase.equals("Start your Action Phase!")){
            constants.setStartGame(true);
            constants.setPlanningPhaseStarted(true);
            constants.setCardPlayed(true);
            constants.setActionPhaseStarted(true);
        }
        else if(phase.equals("Play card!")){
            constants.setStartGame(true);
            constants.setPlanningPhaseStarted(true);
        }
    }

    private void setupConnection() throws IOException, ClassNotFoundException {
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
                nickname = readNext();
            } while (nickname == null);
            do {
                System.out.println();
                System.out.print(SPACE+"Choose a character: "+ availableCharacters + " ");
                character = readNext().toUpperCase(Locale.ROOT);
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

    private void setupGame(){
        int numberOfPlayers;
        String expertMode;
        while (true){
            try {
                while (true){
                    System.out.println();
                    System.out.print(SPACE+"Insert number of player: ");
                    String intString = readNext();
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
                    expertMode = readNext();
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
            do{
                System.out.println();
                System.out.print(SPACE+"Do you want to continue it? [y/n] ");
                decision = readNext();
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
            System.out.print(SPACE+"Insert nickname for restore last game: ");
            String nickname = readNext();
            if(proxy.setupConnection(nickname, null)) return;
            else {
                System.out.println();
                System.out.print(ANSI_RED+SPACE+"Error, insert your previous nickname"+ANSI_RESET);
            }
        }
    }

    private void useSpecial() throws IOException, ClassNotFoundException {
        System.out.println();
        System.out.print(SPACE+"Do you want to use a special card? [y/n] ");
        String answer = readNext();
        if (answer.equalsIgnoreCase("n")) return;
        else if (answer.equalsIgnoreCase("y")) {
            int special = -1;
            printable.printSpecialList();
            do {
                System.out.println();
                System.out.print(SPACE+"Which special do you want to use? Insert the number ");
                String intString = readNext();
                special = Integer.parseInt(intString);
            } while (special == -1);
            int specialIndex = view.getSpecialIndex(special);
            if(specialIndex == -1){
                System.out.println();
                System.out.println(ANSI_RED+SPACE+"Error, special not present"+ANSI_RESET);
                useSpecial();
            }
            if(!proxy.checkSpecial(specialIndex)) {
                System.out.println();
                System.out.println(ANSI_RED+SPACE+"Error, special not present"+ANSI_RESET);
                useSpecial();
            }
            if(special==2 || special==4 || special==6 || special==8 ) return;
            special(special);
        }
        System.out.println();
        System.out.println(ANSI_RED+SPACE+"Error, try again"+ANSI_RESET);
        useSpecial();
    }

    private boolean special(int special) throws IOException, ClassNotFoundException {
        while(true) {
            try {
                if (special == 1) {
                    String colorString;
                    int color;
                    while (true) {
                        System.out.println();
                        System.out.print(SPACE + "Which student do you want to move? Insert color ");
                        colorString = readNext();
                        color = translateColor(colorString);
                        if (color == -1) {
                            System.out.println();
                            System.out.println(ANSI_RED + SPACE + "Error, insert an existing color." + ANSI_RESET);
                        }
                        else break;
                    }
                    int island;
                    do {
                        System.out.println();
                        System.out.print(SPACE + "In witch island? Insert the number ");
                        String intString = readNext();
                        island = Integer.parseInt(intString);
                        if(island < 1 || island > 12 ) {
                            System.out.println(ANSI_RED + SPACE + "Error, insert an existing island." + ANSI_RESET);
                            island = -1;
                        }
                    } while (island == -1);
                    island = island - 1;
                    if(proxy.useSpecial(special, island, color)) return true;
                    else System.out.println(ANSI_RED + SPACE + "Move not allowed, try again." + ANSI_RESET);
                } else if (special == 3 || special == 5) {
                    int island;
                    do {
                        System.out.println();
                        System.out.print(SPACE + "Which island? Insert the number ");
                        String intString = readNext();
                        island = Integer.parseInt(intString);
                        if(island < 1 || island > 12 ) {
                            System.out.println(ANSI_RED + SPACE + "Error, insert an existing island." + ANSI_RESET);
                            island = -1;
                        }
                    } while (island == -1);
                    island = island - 1;
                    if(proxy.useSpecial(special, island)) return true;
                    else System.out.println(ANSI_RED + SPACE + "Move not allowed, try again." + ANSI_RESET);
                } else if (special == 7) {
                    ArrayList<Integer> entranceStudents = new ArrayList<>();
                    ArrayList<Integer> cardStudents = new ArrayList<>();
                    String color;
                    for (int i = 0; i < 3; i++) {
                        while (true) {
                            System.out.println();
                            System.out.print(SPACE + "Which student on the card?");
                            color = readNext();
                            if (translateColor(color) == -1) {
                                System.out.println();
                                System.out.println(ANSI_RED + SPACE + "Error, insert an existing color." + ANSI_RESET);
                            } else break;
                        }
                        cardStudents.add(translateColor(color));
                        while (true) {
                            System.out.println();
                            System.out.print(SPACE + "Which student in the entrance? ");
                            color = readNext();
                            if (translateColor(color) == -1) {
                                System.out.println();
                                System.out.println(ANSI_RED + SPACE + "Error, insert an existing color." + ANSI_RESET);
                            } else break;
                        }
                        entranceStudents.add(translateColor(color));
                        if (i < 2) {
                            System.out.println();
                            System.out.print(SPACE + "Do you want to move student again? [Y/N] ");
                            String answer = readNext();
                            if (answer.equalsIgnoreCase("n")) break;
                        }
                    }
                    if (proxy.useSpecial(special, entranceStudents, cardStudents)) return true;
                    else System.out.println(ANSI_RED + SPACE + "Move not allowed, try again." + ANSI_RESET);
                } else if (special == 9) {
                    String color;
                    while (true) {
                        System.out.println();
                        System.out.print(SPACE + "Which color? ");
                        color = readNext();
                        if (translateColor(color) == -1) {
                            System.out.println();
                            System.out.println(ANSI_RED + SPACE + "Error, insert an existing color." + ANSI_RESET);
                        }
                        else break;
                    }
                    if (proxy.useSpecial(special, translateColor(color))) return true;
                    else System.out.println(ANSI_RED + SPACE + "Move not allowed, try again." + ANSI_RESET);
                } else if (special == 10) {
                    ArrayList<Integer> entranceStudents = new ArrayList<>();
                    ArrayList<Integer> tableStudents = new ArrayList<>();
                    String color;
                    for (int i = 0; i < 2; i++) {
                        while (true) {
                            System.out.println();
                            System.out.print(SPACE + "Which student on the table? ");
                            color = readNext();
                            if (translateColor(color) == -1) {
                                System.out.println();
                                System.out.println(ANSI_RED + SPACE + "Error, insert an existing color." + ANSI_RESET);
                            }
                            else break;
                        }
                        tableStudents.add(translateColor(color));
                        while (true) {
                            System.out.println();
                            System.out.print(SPACE + "Which student in the entrance? ");
                            color = readNext();
                            if (translateColor(color) == -1) {
                                System.out.println();
                                System.out.println(ANSI_RED + SPACE + "Error, insert an existing color." + ANSI_RESET);
                            }
                            else break;
                        }
                        entranceStudents.add(translateColor(color));
                        if (i < 1) {
                            System.out.println();
                            System.out.print(SPACE + "Do you want to move student again? [Y/N] ");
                            String answer = readNext();
                            if (answer.equalsIgnoreCase("n")) break;
                        }
                    }
                    if (proxy.useSpecial(special, entranceStudents, tableStudents)) return true;
                    else System.out.println(ANSI_RED + SPACE + "Move not allowed, try again." + ANSI_RESET);
                } else if (special == 11) {
                    String color;
                    while (true) {
                        System.out.println();
                        System.out.print(SPACE + "Which student do you want to move? Insert color ");
                        color = readNext();
                        if (translateColor(color) == -1) {
                            System.out.println();
                            System.out.println(ANSI_RED + SPACE + "Error, insert an existing color." + ANSI_RESET);
                        }
                        else break;
                    }
                    if (proxy.useSpecial(special, translateColor(color))) return true;
                    else System.out.println(ANSI_RED + SPACE + "Move not allowed, try again." + ANSI_RESET);
                } else if (special == 12) {
                    String color;
                    while (true) {
                        System.out.println();
                        System.out.print(SPACE + "Which color? Insert color ");
                        color = readNext();
                        if (translateColor(color) == -1) {
                            System.out.println();
                            System.out.print(ANSI_RED + SPACE + "Error, enter an existing color" + ANSI_RESET);
                        }
                        else break;
                    }
                    if (proxy.useSpecial(special, translateColor(color))) return true;
                    else System.out.println(ANSI_RED + SPACE + "Move not allowed, try again." + ANSI_RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println(ANSI_RED + SPACE + "Error, insert a number" + ANSI_RESET);
            }
        }
    }

    private void playCard() throws IOException, ClassNotFoundException {
        System.out.println("play card");
        printable.cli();
        while (true) {
            System.out.println();
            System.out.print(SPACE + "Which card do you want to play? ");
            String card = null;
            card = readNext();
            String result = proxy.playCard(card);
            if (!result.equalsIgnoreCase("ok")) {
                System.out.println();
                System.out.println(ANSI_RED + SPACE + result + ANSI_RESET);
            }
            else {
                constants.setCardPlayed(true);
                System.out.println();
                System.out.println(SPACE + "It's your opponent turn, wait...");
                return;
            }
        }
    }

    private void moveStudents() {
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
                    color = readNext();
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
                    where = readNext();
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
                        String intString = readNext();
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
                else if(accepted.equals("move not allowed")) {
                    System.out.println();
                    System.out.println(ANSI_RED+SPACE+"Move not allowed"+ANSI_RESET);
                }
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

    private void moveMotherNature() throws IOException, ClassNotFoundException {
        int steps = -1;
        try {
            do {
                System.out.println();
                printable.printMotherNature();
                System.out.print(SPACE+"How many steps do you want to move Mother Nature? Maximum number of steps "+view.getMaxStepsMotherNature()+" ");
                String intString = readNext();
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

    private void chooseCloud() throws IOException, ClassNotFoundException {
        int cloud = -1;
        constants.setEndTurn(true);
        try{
            do {
                printable.printCloud();
                System.out.println();
                System.out.print(SPACE+"Which cloud do you want? ");
                String intString = readNext();
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
            constants.setPlanningPhaseStarted(false);
        }
        else {
            System.out.println();
            System.out.println(ANSI_RED+SPACE+result+ANSI_RESET);
            System.out.println();
        }
    }

    @Override
    public void run() {
        try {
            setup();
            while (active) {
                if (!constants.isPlanningPhaseStarted()){
                    proxy.startPlanningPhase();
                    constants.resetAll();
                    constants.setPlanningPhaseStarted(true);
                }
                while (!constants.isCloudChosen()&&active) {
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

    private void setActive(boolean active) {
        this.active = active;
    }

    private void turn() throws IOException, ClassNotFoundException {
        if (!constants.isSpecialUsed() && constants.isActionPhaseStarted() && view.getExpertMode()) useSpecial();
        phaseHandler(constants.lastPhase());
    }

    private void phaseHandler(String phase) throws IOException, ClassNotFoundException {
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

    private String readNext(){
        String string = scanner.next();
        scanner.nextLine();
        return string;
    }

    private void disconnectClient() throws IOException {
        System.out.println();
        System.out.println(ANSI_RED+SPACE+"One of the player is offline, Game over."+ANSI_RESET);
        socket.close();
        setActive(false);
        System.exit(-1);
    }

    private void winner() throws IOException {
        printable.cli();
        System.out.println();
        System.out.println(ANSI_RED+SPACE+"Game over, the winner is "+view.getWinner()+"."+ANSI_RESET);
        socket.close();
        setActive(false);
        System.exit(0);
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
            System.out.println("New play: "+"\t"+"\t"+"island "+islandRef+" No Entry tiles: "+isInhibited);
        }
    }

    @Override
    public void notifyIslandChange(int islandToDelete) {
        if(constants.isStartGame()) {
            //System.out.println();
            System.out.println("New play: "+"\t"+"\t"+" island "+(islandToDelete+1)+" had been united.");
            //System.out.println();
        }
    }

    @Override
    public void notifyMotherPosition(int newMotherPosition) {
        if(constants.isStartGame()) {
            System.out.println();
            System.out.print("New play: "+"\t"+"\t");
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
    public void notifySpecial(int specialRef, int playerRef) {
        if(constants.isStartGame()) {
            System.out.println();
            System.out.print("New play: "+"\t"+"\t");
            System.out.println("Player "+view.getNickname(playerRef)+" used "+view.getSpecialName(specialRef));
        }
    }

    @Override
    public void notifySpecialList(ArrayList<Integer> specialsList, ArrayList<Integer> cost) {
        if(constants.isStartGame()) {

        }
    }

    @Override
    public void notifyIncreasedCost(int specialRef, int newCost) {

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

    @Override
    public void notifyDisconnected() throws IOException {
        disconnectClient();
    }

    @Override
    public void notifyWinner() throws IOException {
        winner();
    }

}
