package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Message.*;
import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.server.Answer.*;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class VirtualClient implements Runnable{
    private final Socket socket;
    private final Entrance server;
    private final Proxy_s proxy;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private final int playerRef;
    private boolean clientInitialization;
    private boolean gameSetupInitialization;
    private GameSetup gameSetup;
    private Object setupLocker;
    private Object objGame ;
    private boolean oneCardAtaTime;
    private boolean readyActionPhase;
    private boolean victory;
    private Message tmp;
    private Object planLocker;
    private RoundPartOne roundPartOne;
    private RoundPartTwo roundPartTwo;
    private Object actionLocker;
    private Object errorLocker;
    private boolean error;

    public VirtualClient(Socket socket, Entrance server, Proxy_s proxy, int playerRef){
        this.socket = socket;
        this.server = server;
        this.proxy = proxy;
        this.playerRef = playerRef;
        this.victory = false;
        this.clientInitialization = true;
        this.gameSetupInitialization = false;
        this.setupLocker = new Object();
        this.gameSetup = new GameSetup();
        this.objGame = new Object();
        gameSetup.start();
        this.oneCardAtaTime = false;
        this.readyActionPhase = false;
        this.planLocker = new Object();
        this.roundPartOne = new RoundPartOne();
        roundPartOne.start();
        this.actionLocker = new Object();
        this.roundPartTwo = new RoundPartTwo();
        roundPartTwo.start();
        this.errorLocker = new Object();
        this.error = false;
        proxy.incrLimiter();
        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException e) { clientConnectionExpired(e); }
    }

    @Override
    public void run() {
        boolean first = true;

        try {
            this.socket.setSoTimeout(15000);    //come faccio a notificare lo spegnimento del socket?
            while (!victory){
                tmp = (Message) input.readObject();

                if (tmp instanceof PingMessage) {
                    this.socket.setSoTimeout(15000);    //reset timeout

                } else if(oneCardAtaTime) { //Planning Phase msg
                    oneCardAtaTime = false;
                    if (tmp instanceof CardMessage) {
                        roundPartOne.setPlanningMsg(tmp);
                        if (!error) synchronized(planLocker){ planLocker.notify(); }
                        else {
                            error = false;
                            synchronized(errorLocker){ errorLocker.notify(); }
                        }
                    } // ne va fatto uno per ready action phase

                } else if(readyActionPhase){    //Action Phase msg
                    readyActionPhase = false;
                    if (tmp instanceof MoveStudent || tmp instanceof UseSpecial) {  //da sistemare
                        roundPartTwo.setActionMsg(tmp);
                        if(!error) synchronized(actionLocker){ actionLocker.notify(); }
                        else {
                            error = false;
                            synchronized(errorLocker){ errorLocker.notify(); }
                        }
                    } // ne va fatto uno per mother nature, per choose cloud, ready play card

                } else if (clientInitialization) {  //login msg
                    clientInitialization = false;
                    if(tmp instanceof GenericMessage) {
                        gameSetup.setSetupMsg(tmp);
                        if(first){
                            first = false;
                            synchronized (objGame){ objGame.notify(); }
                        }else if (!error) synchronized (setupLocker) { setupLocker.notify(); }
                        else {
                            error = false;
                            synchronized(errorLocker){ errorLocker.notify(); }
                        }
                    }
                }else if (gameSetupInitialization){ //game setup msg
                    gameSetupInitialization = false;
                    if(tmp instanceof SetupGame){
                        gameSetup.setSetupMsg(tmp);
                        if(!error) synchronized (setupLocker) {setupLocker.notify(); }
                        else {
                            error = false;
                            synchronized(errorLocker){ errorLocker.notify(); }
                        }
                    }
                }else System.out.println("errore"); //ovviamente da cambiare
            }
        }catch (SocketException socketException){
            //CHE FACCIO?
        }catch (IOException | ClassNotFoundException e){
            System.err.println(e.getMessage());
            System.out.println("Client disconnected!");
            //metodo per notificare tutti
            server.exitError();
        }
    }

    public void unlockPlanningPhase(){ oneCardAtaTime = true; }
    public void unlockActionPhase(){ readyActionPhase = true; }

    //Message to client
    public void sendPlayCard(){
        try {
            output.writeObject(new PlayCard());
            output.flush();
        }catch (IOException e){ clientConnectionExpired(e); }
    }
    public void sendStartTurn(){
        try {
            output.writeObject(new StartTurn());
            output.flush();
        }catch (IOException e){ clientConnectionExpired(e); }
    }

    private class GameSetup extends Thread{
        Message setupMsg;

        @Override
        public void run(){
            try{
                synchronized (objGame){
                    objGame.wait();
                    if (proxy.isFirst()) gameSetting();
                        loginClient();
                }
            }catch (InterruptedException e) { e.printStackTrace(); }
        }

        private void gameSetting(){
            GenericMessage msg = (GenericMessage) setupMsg;

            try {
                if (msg.getMessage().equals("Ready for login!")) {
                    //proxy.setConnectionsAllowed(0);
                    output.writeObject(new SetupGameMessage());
                    output.flush();
                    synchronized (setupLocker) {
                        gameSetupInitialization = true;
                        setupLocker.wait();
                        setupGame();
                    }
                } else {
                    output.writeObject(new GenericAnswer("error"));
                    output.flush();
                    synchronized (errorLocker) {
                        clientInitialization = true;
                        error = true;
                        errorLocker.wait();
                        gameSetting();
                    }
                }
            }catch (IOException e) { clientConnectionExpired(e);
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        private void setupGame(){
            SetupGame msg = (SetupGame) setupMsg;

            if(msg.getPlayersNumber() >= 2 && msg.getPlayersNumber() <= 4) {
                proxy.setConnectionsAllowed(msg.getPlayersNumber());
                server.startGame(msg.getPlayersNumber(),msg.getExpertMode());
            }else {
                try {
                    output.writeObject(new GenericAnswer("error"));
                    output.flush();
                    synchronized (errorLocker) {
                        gameSetupInitialization = true;
                        error = true;
                        errorLocker.wait();
                        setupGame();
                    }
                }catch (IOException e) { clientConnectionExpired(e);
                }catch (InterruptedException ex) { ex.printStackTrace(); }
            }
        }
        private void loginClient(){
            GenericMessage msg = (GenericMessage) setupMsg;

            try {
                if (msg.getMessage().equals("Ready for login!")) {
                    output.writeObject(new GenericAnswer("Ready for login!"));
                    output.flush();
                    synchronized (setupLocker) {
                        clientInitialization = true;
                        setupLocker.wait();
                        setupConnection();
                    }
                } else {
                    output.writeObject(new GenericAnswer("error"));
                    output.flush();
                    synchronized (errorLocker) {
                        clientInitialization = true;
                        error = true;
                        errorLocker.wait();
                        loginClient();
                    }
                }
            }catch (IOException e) { clientConnectionExpired(e);
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        private void setupConnection() {
            SetupConnection msg = (SetupConnection) setupMsg;
            boolean checker;

            checker = server.userLogin(msg.getNickname(), msg.getCharacter(), playerRef);
            try {
                if (checker) {
                    output.writeObject((new GenericAnswer("ok")));
                    output.flush();
                    synchronized (setupLocker){
                        clientInitialization = true;
                        setupLocker.wait();
                        readyStart();
                    }
                } else {
                    output.writeObject(new GenericAnswer("error"));
                    output.flush();
                    synchronized (errorLocker) {
                        clientInitialization = true;
                        error = true;
                        errorLocker.wait();
                        setupConnection();
                    }
                }
            }catch (IOException e) { clientConnectionExpired(e);
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        private void readyStart(){
            GenericMessage msg = (GenericMessage) setupMsg;

            try {
                if (!msg.getMessage().equals("Ready for play card")) {
                    output.writeObject(new GenericAnswer("error"));
                    output.flush();
                    synchronized (errorLocker) {
                        clientInitialization = true;
                        error = true;
                        errorLocker.wait();
                        readyStart();
                    }
                }
            }catch (IOException e) { clientConnectionExpired(e);
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }

        public void setSetupMsg(Message msg) { this.setupMsg = msg; }
    }

    private class RoundPartOne extends Thread{
        Message planningMsg;

        @Override
        public void run(){
            try {
                while (!victory) {
                    synchronized (planLocker) {
                        planLocker.wait();
                        planningPhase();
                        oneCardAtaTime = true;
                        planLocker.wait();  //wait ready for action phase msg
                        readyForAction();
                        server.resumeTurn();
                    }
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
        }

        private void planningPhase() {
            CardMessage cardMessage = (CardMessage) planningMsg;
            boolean checker;

            try {
                checker = server.userPlayCard(playerRef, cardMessage.getCard());
                if(checker) {
                    output.writeObject(new GenericAnswer("ok"));
                    output.flush();
                }else{
                    output.writeObject(new MoveNotAllowedAnswer());
                    output.flush();
                    synchronized (errorLocker) {
                        oneCardAtaTime = true;
                        error = true;
                        errorLocker.wait();
                        planningPhase();
                    }
                }
            }catch (IOException e) { clientConnectionExpired(e);
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        private void readyForAction(){
            GenericMessage readyMsg = (GenericMessage) planningMsg;

            if (!readyMsg.getMessage().equals("Ready for action phase")){
                try {
                    output.writeObject(new GenericAnswer("error"));
                    output.flush();
                    synchronized (errorLocker) {
                        oneCardAtaTime = true;
                        error = true;
                        errorLocker.wait();
                        readyForAction();
                    }
                }catch (IOException e) { clientConnectionExpired(e);
                }catch (InterruptedException ex) { ex.printStackTrace(); }
            }
        }

        public void setPlanningMsg(Message msg) { this.planningMsg = msg; }
    }

    private class RoundPartTwo extends Thread{
        private Message actionMsg;
        private int numberOfPlayer;
        private boolean studentLocker;
        private int studentCounter;
        private boolean motherLocker;
        private boolean cloudLocker;

        public RoundPartTwo(){
            this.numberOfPlayer = proxy.getConnectionsAllowed();
            this.studentLocker = true;
            this.studentCounter = 0;
            this.motherLocker = false;
            this.cloudLocker = false;
        }

        @Override
        public void run(){
            try {
                while (!victory) {
                    synchronized (actionLocker) {
                        actionLocker.wait();
                        actionPhase();
                        server.resumeTurn();
                    }
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
        }

        private void actionPhase() {
            if(studentLocker) {
                studentLocker = false;
                if (actionMsg instanceof MoveStudent) {
                    if (numberOfPlayer == 2 || numberOfPlayer == 4) {
                        if (studentCounter < 3) moveStudent();
                        else {
                            //TODO: transfer complete alla fine di tutti gli studenti Generic Answer
                            motherLocker = true;
                        }
                    } else if (numberOfPlayer == 3) {
                        if (studentCounter < 4) moveStudent();
                        else {
                            //TODO: transfer complete alla fine di tutti gli studenti Generic Answer
                            motherLocker = true;
                        }
                    }
                }
            }

            if(motherLocker) {
                motherLocker = false;
                if (actionMsg instanceof MoveMotherNature) moveMotherNature();
            }

            if(cloudLocker) {
                cloudLocker = false;
                if (actionMsg instanceof ChosenCloud) chooseCloud();
            }

        }

        private void moveStudent(){
            MoveStudent studentMovement = (MoveStudent) actionMsg;
            boolean checker;

            checker = server.userMoveStudent(playerRef, studentMovement.getColor(), studentMovement.isInSchool(), studentMovement.getIslandRef());
            try {
                if (checker) {
                    output.writeObject(new GenericAnswer("ok"));
                    output.flush();
                    studentCounter++;
                    studentLocker = true;
                    actionPhase();
                } else {
                    output.writeObject(new MoveNotAllowedAnswer());
                    output.flush();
                    synchronized (errorLocker){
                        readyActionPhase = true;
                        error = true;
                        errorLocker.wait();
                        studentLocker = true;
                        actionPhase();  //bisogna essere sicuri che sia MoveStudent, forse serve una portineria interna
                    }
                }
            }catch (IOException e) { clientConnectionExpired(e);
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        private void moveMotherNature(){
            MoveMotherNature motherMovement = (MoveMotherNature) actionMsg;
            boolean checker;

            try {
                checker = server.userMoveMotherNature(motherMovement.getDesiredMovement());
                if (checker) {
                    output.writeObject(new GenericAnswer("ok"));
                    output.flush();
                    cloudLocker = true;
                } else {
                    output.writeObject(new MoveNotAllowedAnswer());
                    output.flush();
                    synchronized (errorLocker){
                        readyActionPhase = true;
                        error = true;
                        errorLocker.wait();
                        motherLocker = true;
                        moveMotherNature();  //bisogna essere sicuri che sia MoveStudent, forse serve una portineria interna
                    }
                }
            } catch (IOException e) { clientConnectionExpired(e);
            } catch (InterruptedException ex) { ex.printStackTrace();
            } catch (EndGameException endGameException) {
                //TODO: game over blocca tutto
            }
        }
        private void chooseCloud(){
            ChosenCloud cloud = (ChosenCloud) actionMsg;
            boolean checker;

            checker = server.userChooseCloud(playerRef,cloud.getCloud());
            try {
                if(checker) {
                    output.writeObject(new GenericAnswer("ok"));
                    output.flush();
                } else {
                    output.writeObject(new MoveNotAllowedAnswer());
                    output.flush();
                    synchronized (errorLocker){
                        readyActionPhase = true;
                        error = true;
                        errorLocker.wait();
                        cloudLocker = true;
                        actionPhase();  //bisogna essere sicuri che sia MoveStudent, forse serve una portineria interna
                    }
                }
            } catch (IOException e) { clientConnectionExpired(e);
            } catch (InterruptedException ex) { ex.printStackTrace(); }
        }

        public void setActionMsg(Message actionMsg) { this.actionMsg = actionMsg; }
    }

    private void clientConnectionExpired(IOException e){
        System.err.println(e.getMessage());
        System.out.println("Client disconnected!");
        //metodo per notificare tutti
        server.exitError();
    }
}