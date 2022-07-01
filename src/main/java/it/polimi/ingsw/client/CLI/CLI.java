package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.PlayerConstants;
import it.polimi.ingsw.client.Proxy_c;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.server.answer.SavedGameAnswer;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/**
 * Command Line Interface
 */
public class CLI implements Runnable, UserInfoListener, TowersListener, ProfessorsListener, SpecialListener, PlayedCardListener,
        MotherPositionListener, IslandListener, CoinsListener, StudentsListener, InhibitedListener, WinnerListener, DisconnectedListener,
        NoEntryClientListener, ServerOfflineListener, SpecialStudentsListener, RestoreCardsListener{

    private final Exit proxy;
    private final Scanner scanner;
    private boolean active;
    private final PlayerConstants constants;
    private View view;
    private final Socket socket;
    private Printable printable;
    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_RED = "\u001B[31m";
    private final String SPACE = "\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t"+"\t";
    private final Object lock;
    private int playCount;


    /**
     * Constructor allocated the variable of the class and set the listener
     * @param socket
     * @param proxy
     * @throws IOException
     */
    public CLI(Socket socket, Exit proxy) throws IOException{
        this.socket = socket;
        scanner = new Scanner(System.in);
        active = true;
        constants = new PlayerConstants();
        this.proxy = proxy;
        proxy.setDisconnectedListener(this);
        proxy.setServerOfflineListener(this);
        lock = new Object();
        playCount=3;
    }


    /**
     * Setup is the first method which starts the other method depending on the answer of the server. It could be setupGame if the player is the first, savedGame if there's a previous game and
     * the player is the first, LoginRestore if there's a previous game and player is not the first and, setupConnection if the player is not the first. After the login listeners are set.
     * If the game is restored then setup calls setPhase to know where the game was interrupted and set it.
     */
    private void setup()  {
        boolean savedGame=false;
        boolean gameRestored = false;
        String result = proxy.first();
        if(result.equals("SavedGame")){
            savedGame = true;

            if(!savedGame()){
                setupGame();
                setupConnection();
            }
            else {
                System.out.println();
                System.out.println(SPACE + "Setup Connection " + ", waiting for players...");
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
        else if(result.equals("Server Sold Out")) {
            System.out.println();
            System.out.print(ANSI_RED+SPACE+"Server Sold Out, game over."+ANSI_RESET);
            try {
                socket.close();
                scanner.close();
            }catch (IOException e){}
            setActive(false);
            System.exit(-1);
        }
        if(!savedGame) {
            setupConnection();
        }
        view = proxy.startView();
        view.setUserInfoListener(this);
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
        view.setNoEntryListener(this);
        view.setSpecialStudentsListener(this);
        view.setRestoreCardsListener(this);
        proxy.setView();
        printable = new Printable(view);
        System.out.println();
        System.out.println(SPACE+"Game is started! Wait for your turn...");
        if(gameRestored){
            setPhase();
        }
    }

    /**
     * It calls proxy.getPhase to know where the game was interrupted and then set the constant.
     */
    private void setPhase(){
        constants.setStartGame(true);
        String phase = proxy.getPhase();
        if(phase.equals("Start your Action Phase!")){
            constants.setPlanningPhaseStarted(true);
            constants.setCardPlayed(true);
            constants.setActionPhaseStarted(true);
        }
        else if(phase.equals("Play card!")){
            constants.setPlanningPhaseStarted(true);
        }
    }


    /**
     * it get form the server the already chosen characters, it asks to the player nickname and character and send it to the proxy.
     */
    private void setupConnection() {
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

    /**
     * it asks to the player the number of players and expert mode. Then send it to the proxy.
     */
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
            }
        }
    }


    /**
     * it gets from the proxy the information about the game and asks to the player if it wants to restore the game.
     * @return true if player want to restore the game, else return false.

     */
    private boolean savedGame(){
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


    /**
     * it asks to the player the previous nickname chosen, then send it to the proxy.
     */
    private void loginRestore(){
        while (true){
            System.out.println();
            System.out.print(SPACE+"Insert your previous nickname for restore last game: ");
            String nickname = readNext();
            if(proxy.setupConnection(nickname, null)) return;
            else {
                System.out.println();
                System.out.println(ANSI_RED+SPACE+"Error, insert your previous nickname"+ANSI_RESET);
            }
        }
    }

    /**
     * it asks to the player if it wants to use a special, if "n" the method return, else if it wants to use a special send the chosen one to the proxy.
     * if proxy's method return true then useSpecial call the method special if special number is different from 2, 4, 6, 8.
     */
    private void useSpecial() {
        cli();
        String answer;
        System.out.println();
        System.out.print(SPACE + "Do you want to use a special card? [y/n] ");
        answer = readNext();
        if (answer.equalsIgnoreCase("n")) return;
        else if (answer.equalsIgnoreCase("y")) {
            int special = -1;
            printable.printSpecialList();
            do {
                try {
                    System.out.println();
                    System.out.print(SPACE + "Which special do you want to use? Insert the number ");
                    String intString = readNext();
                    special = Integer.parseInt(intString);
                } catch (NumberFormatException e) {
                    System.out.println();
                    System.out.println(ANSI_RED + SPACE + "Error, insert a number" + ANSI_RESET);
                    turn();
                    return;
                }
            } while (special == -1);
            if (!proxy.checkSpecial(special)) {
                System.out.println();
                System.out.println(ANSI_RED + SPACE + "Move not allowed" + ANSI_RESET);
                turn();
            } else {
                System.out.println();
                if (special == 2 || special == 4 || special == 6 || special == 8) {
                    constants.setSpecialUsed(true);
                }
                else if (special(special)) {
                    constants.setSpecialUsed(true);
                } else turn();
            }
        }
        else turn();
    }

    /**
     * For each special it asks what the player wants to move or use depending on the special effect. Then send it to proxy.
     * @param special is the number of the special chosen.
     * @return true if server accepts the special, else return false.
     */
    private boolean special(int special){
            while (true) {
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
                            } else break;
                        }
                        int island;
                        do {
                            System.out.println();
                            System.out.print(SPACE + "In which island? Insert the number ");
                            String intString = readNext();
                            island = Integer.parseInt(intString);
                            if (island < 1 || island > 12) {
                                System.out.println(ANSI_RED + SPACE + "Error, insert an existing island." + ANSI_RESET);
                                island = -1;
                            }
                        } while (island == -1);
                        island = island - 1;
                        System.out.println();
                        if (proxy.useSpecial(special, color, island)) return true;
                        else System.out.println(ANSI_RED + SPACE + "Move not allowed, try again." + ANSI_RESET);
                        return false;
                    } else if (special == 3 || special == 5) {
                        int island;
                        do {
                            System.out.println();
                            System.out.print(SPACE + "Which island? Insert the number ");
                            String intString = readNext();
                            island = Integer.parseInt(intString);
                            if (island < 1 || island > 12) {
                                System.out.println(ANSI_RED + SPACE + "Error, insert an existing island." + ANSI_RESET);
                                island = -1;
                            }
                        } while (island == -1);
                        island = island - 1;
                        System.out.println();
                        if (proxy.useSpecial(special, island)) return true;
                        else System.out.println(ANSI_RED + SPACE + "Move not allowed, try again." + ANSI_RESET);
                        return false;
                    } else if (special == 7) {
                        ArrayList<Integer> entranceStudents = new ArrayList<>();
                        ArrayList<Integer> cardStudents = new ArrayList<>();
                        String color;
                        for (int i = 0; i < 3; i++) {
                            int colorNum;
                            while (true) {
                                System.out.println();
                                System.out.print(SPACE + "Which student on the card? ");
                                color = readNext();
                                colorNum = translateColor(color);
                                if (colorNum == -1) {
                                    System.out.println();
                                    System.out.println(ANSI_RED + SPACE + "Error, insert an existing color." + ANSI_RESET);
                                } else break;
                            }
                            cardStudents.add(colorNum);
                            while (true) {
                                System.out.println();
                                System.out.print(SPACE + "Which student in the entrance? ");
                                color = readNext();
                                colorNum = translateColor(color);
                                if (colorNum == -1) {
                                    System.out.println();
                                    System.out.println(ANSI_RED + SPACE + "Error, insert an existing color." + ANSI_RESET);
                                } else break;
                            }
                            entranceStudents.add(colorNum);
                            if (i < 2) {
                                String answer=null;
                                while (answer==null) {
                                    System.out.println();
                                    System.out.print(SPACE + "Do you want to move student again? [Y/N] ");
                                    answer = readNext();
                                    if (!answer.equalsIgnoreCase("n") && !answer.equalsIgnoreCase("y")) answer=null;
                                }
                                if(answer.equalsIgnoreCase("n")) break;
                            }
                        }
                        System.out.println();
                        if (proxy.useSpecial(special, entranceStudents, cardStudents)) return true;
                        else System.out.println(ANSI_RED + SPACE + "Move not allowed, try again." + ANSI_RESET);
                        return false;
                    } else if (special == 9) {
                        String color;
                        while (true) {
                            System.out.println();
                            System.out.print(SPACE + "Which color? ");
                            color = readNext();
                            if (translateColor(color) == -1) {
                                System.out.println();
                                System.out.println(ANSI_RED + SPACE + "Error, insert an existing color." + ANSI_RESET);
                            } else break;
                        }
                        System.out.println();
                        if (proxy.useSpecial(special, translateColor(color))) return true;
                        else System.out.println(ANSI_RED + SPACE + "Move not allowed, try again." + ANSI_RESET);
                        return false;
                    } else if (special == 10) {
                        ArrayList<Integer> entranceStudents = new ArrayList<>();
                        ArrayList<Integer> tableStudents = new ArrayList<>();
                        String color;
                        for (int i = 0; i < 2; i++) {
                            int colorNum;
                            while (true) {
                                System.out.println();
                                System.out.print(SPACE + "Which student on the table? ");
                                color = readNext();
                                colorNum = translateColor(color);
                                if (colorNum == -1) {
                                    System.out.println();
                                    System.out.println(ANSI_RED + SPACE + "Error, insert an existing color." + ANSI_RESET);
                                } else break;
                            }
                            tableStudents.add(colorNum);
                            while (true) {
                                System.out.println();
                                System.out.print(SPACE + "Which student in the entrance? ");
                                color = readNext();
                                colorNum = translateColor(color);
                                if (colorNum == -1) {
                                    System.out.println();
                                    System.out.println(ANSI_RED + SPACE + "Error, insert an existing color." + ANSI_RESET);
                                } else break;
                            }
                            entranceStudents.add(colorNum);
                            if (i < 1) {
                                String answer=null;
                                while(answer == null) {
                                    System.out.println();
                                    System.out.print(SPACE + "Do you want to move student again? [Y/N] ");
                                    answer = readNext();
                                    if (!answer.equalsIgnoreCase("n") && !answer.equalsIgnoreCase("y")) answer=null;
                                }
                                if(answer.equalsIgnoreCase("n")) break;
                            }
                        }
                        System.out.println();
                        if (proxy.useSpecial(special, entranceStudents, tableStudents)) return true;
                        else System.out.println(ANSI_RED + SPACE + "Move not allowed, try again." + ANSI_RESET);
                        return false;
                    } else if (special == 11) {
                        String color;
                        while (true) {
                            System.out.println();
                            System.out.print(SPACE + "Which student do you want to move? Insert color ");
                            color = readNext();
                            if (translateColor(color) == -1) {
                                System.out.println();
                                System.out.println(ANSI_RED + SPACE + "Error, insert an existing color." + ANSI_RESET);
                            } else break;
                        }
                        System.out.println();
                        if (proxy.useSpecial(special, translateColor(color))) return true;
                        else System.out.println(ANSI_RED + SPACE + "Move not allowed, try again." + ANSI_RESET);
                        return false;
                    } else if (special == 12) {
                        String color;
                        while (true) {
                            System.out.println();
                            System.out.print(SPACE + "Which color? Insert color ");
                            color = readNext();
                            if (translateColor(color) == -1) {
                                System.out.println();
                                System.out.print(ANSI_RED + SPACE + "Error, enter an existing color" + ANSI_RESET);
                            } else break;
                        }
                        System.out.println();
                        if (proxy.useSpecial(special, translateColor(color))) return true;
                        else System.out.println(ANSI_RED + SPACE + "Move not allowed, try again." + ANSI_RESET);
                        return false;
                    }
                } catch (NumberFormatException e) {
                    System.out.println();
                    System.out.println(ANSI_RED + SPACE + "Error, insert a number" + ANSI_RESET);
                }
            }
    }

    /**
     * It asks to the player which card it wants to play and send it to the proxy. If server accepts it the constant about card played is set true.
     */
    private void playCard() {
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
            } else {
                constants.setCardPlayed(true);
                System.out.print(SPACE + "It's your opponent turn, wait...");
                System.out.println();
                return;
            }
        }
    }

    /**
     * It asks to the player which student it wants to move and where. Then send it to the proxy. If proxy return ok the loop is interrupted, if return transfer complete
     * the loop is interrupted and the constant students moved is set true, else if proxy return move not allowed the loop continue.
     */
    private void moveStudents() {
        String accepted;
        cli();
        boolean moveNotAllowed;
        do{
            moveNotAllowed = false;
            String color=null;
            String where=null;
            int colorInt=-1;
            int islandRef = -1;
            while (color == null) {
                synchronized (lock) {
                    System.out.println();
                    System.out.print(SPACE + "Which student do you want to move? Insert color ");
                    color = readNext();
                    colorInt = translateColor(color);
                    if (colorInt == -1) {
                        System.out.println();
                        System.out.println(ANSI_RED + SPACE + "Error, enter an existing color" + ANSI_RESET);
                        color = null;
                    }
                }
            }
            while (where == null) {
                synchronized (lock) {
                    System.out.println();
                    System.out.print(SPACE + "Where do you want to move the student? School or Island ");
                    where = readNext();
                    if (!where.equalsIgnoreCase("island") && !where.equalsIgnoreCase("school")) {
                        System.out.println();
                        System.out.println(ANSI_RED + SPACE + "Error, insert school or island" + ANSI_RESET);
                        where = null;
                    }
                }
            }
            System.out.println();
            if (where.equalsIgnoreCase("island")) {
                synchronized (lock) {
                    while (islandRef == -1) {
                        System.out.print(SPACE + "Which island? insert the number ");
                        String intString = readNext();
                        islandRef = Integer.parseInt(intString);
                        islandRef = islandRef - 1;
                        System.out.println();
                        if (islandRef < 0 || islandRef >= view.getIslandSize()) {
                            System.out.println();
                            System.out.println(ANSI_RED + SPACE + "Error, insert an existing island" + ANSI_RESET);
                            islandRef = -1;
                        }
                    }
                }
            }
            accepted = proxy.moveStudent(colorInt, where, islandRef);
            if (accepted.equals("transfer complete")) constants.setStudentMoved(true);
            else if (accepted.equals("move not allowed")) {
                System.out.println(ANSI_RED + SPACE + "Move not allowed" + ANSI_RESET);
                moveNotAllowed = true;
            }
        }while(moveNotAllowed);
    }


    /**
     * It asks to the player how many steps it wants to move mother nature, then send the number of steps to the proxy. If the server's answer is ok constant mother moved is set true.
     */
    private void moveMotherNature() {
        int steps = -1;
            try {
                do {
                    printable.printMotherNature();
                    System.out.print(SPACE + "How many steps do you want to move Mother Nature? Maximum number of steps " + view.getMaxStepsMotherNature() + " ");
                    String intString = readNext();
                    steps = Integer.parseInt(intString);
                    if (steps > view.getMaxStepsMotherNature()) {
                        System.out.println();
                        System.out.println(ANSI_RED + SPACE + "Error, insert a number between 1 and " + view.getMaxStepsMotherNature() + ANSI_RESET);
                        steps = -1;
                    }
                } while (steps <= 0);
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println(ANSI_RED + SPACE + "Error, insert a number." + ANSI_RESET);
                return;
            }
            String result = proxy.moveMotherNature(steps);
            if (result.equalsIgnoreCase("ok")) constants.setMotherMoved(true);
            else System.out.println(result);
    }


    /**
     * It asks to the player which cloud it wants, then send the cloud number to the proxy. If the server's answer is ok constant cloud chosen is set true.
     */
    private void chooseCloud() {
        int cloud = -1;
        constants.setEndTurn(true);
            try {
                do {
                    printable.printCloud();
                    System.out.print(SPACE + "Which cloud do you want? ");
                    String intString = readNext();
                    cloud = Integer.parseInt(intString);
                    if (cloud <= 0 || cloud > view.getNumberOfPlayers()) {
                        cloud = -1;
                        System.out.println();
                        System.out.println(ANSI_RED + SPACE + "Error, insert an existing cloud." + ANSI_RESET);
                    }
                } while (cloud == -1);
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println(ANSI_RED + SPACE + "Error, insert a number." + ANSI_RESET);
                return;
            }
            cloud = cloud - 1;
            String result = proxy.chooseCloud(cloud);
            if (result.equalsIgnoreCase("ok")) {
                playCount = 4;
                constants.setCloudChosen(true);
                System.out.println();
                printable.cli();
                System.out.println();
                System.out.println(SPACE + "it's your opponent turn, wait...");
                constants.setEndTurn(false);
                constants.setPlanningPhaseStarted(false);
            } else {
                System.out.println();
                System.out.println(ANSI_RED + SPACE + result + ANSI_RESET);
                System.out.println();
            }
    }


    /**
     * Use the play counter to print the entire cli every 4 plays.
     */
    private void cli(){
        if(playCount==3){
            printable.cli();
            playCount=0;
        } else playCount++;
    }

    /**
     * First set up the game. Then a loop is starting until the game is active. Loop starts the phase of the game at the right moment using constants.
     */
    @Override
    public void run() {
        setup();
        while (active) {
            if (!constants.isPlanningPhaseStarted()){
                proxy.startPlanningPhase();
                constants.resetAll();
                constants.setPlanningPhaseStarted(true);
            }
            while (!constants.isCloudChosen()) {
                turn();
            }
        }
        scanner.close();
    }

    private void setActive(boolean active) {
        this.active = active;
    }

    /**
     * it starts the right phase which player have to do. First, set the game started so prints about initialization of the game aren't printed.
     * Then, if in that action phase the special have not been used and the game is in expert mode, it asks to player if it wants to use a special.
     * Finally call phase handler with the phase that have to be done.
     */
    private void turn() {
        if(!constants.isStartGame()) constants.setStartGame(true);
        if (!constants.isSpecialUsed() && constants.isActionPhaseStarted() && view.getExpertMode()) useSpecial();
        phaseHandler(constants.lastPhase());
    }

    /**
     * it calls the method of the last phase.
     * @param phase is the phase that have to be done.
     */
    private void phaseHandler(String phase) {
        if(phase.equals("PlayCardAnswer")) playCard();
        else if(!constants.isActionPhaseStarted()) {
            constants.setActionPhaseStarted(proxy.startActionPhase());
        }
        else {
            switch (phase) {
                case ("MoveStudent") -> moveStudents();
                case ("MoveMother") -> moveMotherNature();
                case ("ChooseCloud") -> chooseCloud();
            }
        }
    }


    /**
     * It converts the string in to the corresponding number.
     * @param color is the color which have to be translated.
     * @return the corresponding number.
     */
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


    /**
     * Read from input the first word and skip the other.
     * @return the input string.
     */
    private String readNext(){
        String string = scanner.next();
        scanner.nextLine();
        return string;
    }

    /**
     * If server is offline close the socket, set active false and close the client.
     */
    private void serverOffline() {
        synchronized (lock) {
            System.out.println();
            System.out.println(ANSI_RED + SPACE + "Server is offline, Game over." + ANSI_RESET);
            try {
                socket.close();
            }catch (IOException e){serverOffline();}
            setActive(false);
            System.exit(-1);
        }
    }

    /**
     * If one of the player disconnects then close the socket, set active false and close the client.
     */
    private void disconnectClient() {
        synchronized (lock) {
            System.out.println();
            System.out.println(ANSI_RED + SPACE + "One of the player is offline, Game over." + ANSI_RESET);
            try {
                socket.close();
            }catch (IOException e){serverOffline();}
            setActive(false);
            System.exit(-1);
        }
    }

    /**
     * If game is over, return the winner, close the socket, set active false and close the client.
     */
    private void winner() {
        synchronized (lock) {
            printable.cli();
            System.out.println();
            System.out.println(ANSI_RED + SPACE + "Game over, the winner is " + view.getWinner() + "." + ANSI_RESET);
            try {
                socket.close();
            }catch (IOException e){serverOffline();}
            setActive(false);
            System.exit(0);
        }
    }

    /**
     * See Listeners package.
     */
    @Override
    public void notifyNewCoinsValue(int playerRef, int newCoinsValue) {
        if(view.getExpertMode() && constants.isStartGame()) {
            synchronized (lock) {
                printable.printCoins(playerRef);
            }
        }
    }

    /**
     * See Listeners package.
     */
    @Override
    public void notifyInhibited(int islandRef, int isInhibited) {
        if(constants.isStartGame()) {
            synchronized (lock) {
                System.out.println("New play: " + "\t" + "\t" + "island " + (islandRef + 1) + " No Entry tiles: " + isInhibited);
                System.out.println();
            }
        }
    }

    /**
     * See Listeners package.
     */
    @Override
    public void notifyIslandChange(int islandToDelete) {
        if(constants.isStartGame()) {
            synchronized (lock) {
                //System.out.println();
                System.out.println("New play: " + "\t" + "\t" + " island " + (islandToDelete + 1) + " had been united.");
            }
        }
    }

    /**
     * See Listeners package.
     */
    @Override
    public void notifyMotherPosition(int newMotherPosition) {
        if(constants.isStartGame()) {
            synchronized (lock) {
                System.out.println();
                printable.printMotherNature();
            }
        }
    }

    /**
     * See Listeners package.
     */
    @Override
    public void notifyPlayedCard(int playerRef, String assistantCard) {
        if(constants.isStartGame()) {
            synchronized (lock) {
                printable.printLastCard();
            }
        }
    }

    @Override
    public void notifyHand(int playerRef, ArrayList<String> hand) {

    }

    @Override
    public void notifyProfessors(int playerRef, int color, boolean newProfessorValue) {
        if(constants.isStartGame()) {
            synchronized (lock) {
                printable.printProf(playerRef);
            }
        }
    }

    /**
     * See Listeners package.
     */
    @Override
    public void notifySpecial(int specialRef, int playerRef) {
        if(constants.isStartGame()) {
            synchronized (lock) {
                System.out.print("New play: " + "\t" + "\t");
                System.out.println("Player " + view.getNickname(playerRef) + " used special " + view.getSpecialName(view.getSpecialIndex(specialRef)));
                System.out.println();
            }
        }
    }

    /**
     * See Listeners package.
     */
    @Override
    public void notifySpecialList(ArrayList<Integer> specialsList, ArrayList<Integer> cost) {}

    /**
     * See Listeners package.
     */
    @Override
    public void notifyIncreasedCost(int specialRef, int newCost) {}

    /**
     * See Listeners package.
     */
    @Override
    public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {
        if(constants.isStartGame()&&!constants.isEndTurn()) {
            synchronized (lock) {
                printable.printStudentsChange(place, componentRef);
            }
        }
    }

    /**
     * See Listeners package.
     */
    @Override
    public void notifyTowersChange(int place, int componentRef, int towersNumber) {
        if(constants.isStartGame()&&!constants.isEndTurn()) {
            synchronized (lock) {
                printable.printTowersChange(place, componentRef);
            }
        }
    }

    /**
     * See Listeners package.
     */
    @Override
    public void notifyTowerColor(int islandRef, int newColor) {
        if(constants.isStartGame()&&!constants.isEndTurn()) {
            synchronized (lock) {
                printable.printTowersOwner(islandRef);
            }
        }
    }

    /**
     * See Listeners package.
     */
    @Override
    public void notifyDisconnected(){
        disconnectClient();
    }

    /**
     * See Listeners package.
     */
    @Override
    public void notifyWinner() {
        winner();
    }

    /**
     * See Listeners package.
     */
    @Override
    public void notifyServerOffline(){
        serverOffline();
    }

    /**
     * See Listeners package.
     */
    @Override
    public void specialStudentsNotify(int special, int color, int value) {

    }

    /**
     * See Listeners package.
     */
    @Override
    public void userInfoNotify(String nickname, String Character, int playerRef) {

    }

    /**
     * See Listeners package.
     */
    @Override
    public void notifyNoEntry(int special, int newValue) {
        if(constants.isStartGame()) {
            synchronized (lock) {
                System.out.print("New play: " + "\t" + "\t");
                System.out.println("No Entry tiles: " + newValue);
                System.out.println();
            }
        }
    }


    /**
     * See Listeners package.
     */
    @Override
    public void restoreCardsNotify(ArrayList<String> hand) {}
}
