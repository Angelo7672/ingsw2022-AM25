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
    private boolean oneCardAtaTime;
    private boolean readyActionPhase;
    private boolean victory;
    private Message tmp;
    private Object planLocker;
    private RoundPartOne roundPartOne;
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
        gameSetup.start();
        this.oneCardAtaTime = false;
        this.readyActionPhase = false;
        this.planLocker = new Object();
        this.roundPartOne = new RoundPartOne();
        roundPartOne.start();
        this.actionLocker = new Object();
        this.errorLocker = new Object();
        this.error = false;
        proxy.incrLimiter();
        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException e) {
            //CHE FACCIO?
        }
    }

    @Override
    public void run() {

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
                        if(!error) planLocker.notify();
                        else {
                            error = false;
                            errorLocker.notify();
                        }
                    }

                } else if(readyActionPhase){
                    if (tmp instanceof MoveStudent || tmp instanceof UseSpecial) {  //da sistemare

                        readyActionPhase = false;
                        actionPhase(tmp);
                    }

                } else if (clientInitialization) {  //login msg
                    clientInitialization = false;
                    if(tmp instanceof GenericMessage) {
                        gameSetup.setSetupMsg(tmp);
                        if (!error) setupLocker.notify();
                        else {
                            error = false;
                            errorLocker.notify();
                        }
                    }
                }else if (gameSetupInitialization){ //game setup msg
                    gameSetupInitialization = false;
                    if(tmp instanceof SetupGame){
                        gameSetup.setSetupMsg(tmp);
                        if(!error) setupLocker.notify();
                        else {
                            error = false;
                            errorLocker.notify();
                        }
                    }
                }

            }
        }catch (SocketException socketException){
            //CHE FACCIO?
        }catch (IOException | ClassNotFoundException e){
            //CHE FACCIO?
        }
    }



    public void unlockPlanningPhase(){ oneCardAtaTime = true; }
    public void unlockActionPhase(){ readyActionPhase = true; }

    //Message to client
    public void sendPlayCard(){
        try {
            output.writeObject(new PlayCard());
            output.flush();
        }catch (IOException e){
            //CHE FACCIO?
        }
    }
    public void sendStartTurn(){
        try {
            output.writeObject(new StartTurn());
            output.flush();
        }catch (IOException e){
           //CHE FACCIO?
        }
    }



    private class GameSetup extends Thread{
        Message setupMsg;

        @Override
        public void run(){
            try{
                setupLocker.wait();
                synchronized (setupLocker){
                    if (proxy.getConnectionsAllowed() == 1) gameSetting();
                    loginClient();
                }
            }catch (InterruptedException e) { e.printStackTrace(); }
        }

        private void gameSetting(){
            GenericMessage msg = (GenericMessage) setupMsg;

            try {
                if (msg.getMessage().equals("Ready for login!")) {
                    proxy.setConnectionsAllowed(0);
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
            }catch (IOException e) {
            //CHE FACCIO?
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
                }catch (IOException e) {
                    //CHE FACCIO?
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
            }catch (IOException e) {
                //CHE FACCIO?
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
            }catch (IOException e) {
                //CHE FACCIO?
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
            }catch (IOException e) {
                //CHE FACCIO?
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }

        public void setSetupMsg(Message msg) { this.setupMsg = msg; }
    }

    private class RoundPartOne extends Thread{
        CardMessage planningMsg;

        @Override
        public void run(){
            try {
                planLocker.wait();
                while (!victory) {
                    synchronized (planLocker) {
                        planningPhase();
                        planLocker.wait();
                    }
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
        }

        private void planningPhase() {
            boolean checker;

            try {
                checker = server.userPlayCard(playerRef, planningMsg.getCard());
                if(checker) {
                    output.writeObject(new GenericAnswer("ok"));
                    output.flush();
                    //TODO: arriva il messaggio Ready for action phase
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
            }catch (IOException e) {
            //CHE FACCIO?
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }

        public void setPlanningMsg(Message msg) { this.planningMsg = (CardMessage) msg; }
    }

    private class RoundPartTwo extends Thread{
        Message actionMsg;

        @Override
        public void run(){

        }

        private void actionPhase(Message msg) {
            UseSpecial special;


            if(msg instanceof MoveStudent) moveStudent(msg);
            //TODO: special, transfer complete alla fine di tutti gli studenti Generic Answer
            if(msg instanceof MoveMotherNature) moveMotherNature(msg);
            //TODO: special
            if(msg instanceof ChosenCloud) chooseCloud(msg);

        }

        private void moveStudent(){
            MoveStudent studentMovement = (MoveStudent) actionMsg;
            boolean checker;

            checker = server.userMoveStudent(playerRef, studentMovement.getColor(), studentMovement.isInSchool(), studentMovement.getIslandRef());
            try {
                if (checker) {
                    output.writeObject(new GenericAnswer("ok"));
                    output.flush();
                } else {
                    output.writeObject(new MoveNotAllowedAnswer());
                    output.flush();
                    synchronized (errorLocker){
                        readyActionPhase = true;
                        error = true;
                        errorLocker.wait();
                        moveStudent();  //bisogna essere sicuri che sia MoveStudent, forse serve una portineria interna
                    }
                }
            }catch (IOException e) {
                //CHE FACCIO?
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        private void moveMotherNature(Message msg){
            MoveMotherNature motherMovement;
            Message tmp;
            boolean checker;
            boolean checker1 = false;

            motherMovement = (MoveMotherNature) msg;
            try {
                checker = server.userMoveMotherNature(motherMovement.getDesiredMovement());
                if (checker) {
                    output.writeObject(new GenericAnswer("ok"));
                    output.flush();
                } else {
                    output.writeObject(new GenericAnswer("move not allowed"));
                    output.flush();
                    while (!checker1) {
                        tmp = (Message) input.readObject();
                        if (tmp instanceof MoveMotherNature) {
                            checker1 = true;
                            moveMotherNature(tmp);
                        } else {
                            output.writeObject(new GenericAnswer("error"));
                            output.flush();
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                //CHE FACCIO?
            } catch (EndGameException endGameException) {
                //TODO: game over blocca tutto
            }
        }
        private void chooseCloud(Message msg){
            Message tmp;
            ChosenCloud cloud;
            boolean checker;
            boolean checker1 = false;

            cloud = (ChosenCloud) msg;
            checker = server.userChooseCloud(playerRef,cloud.getCloud());
            if(checker) {
                try {
                    output.writeObject(new GenericAnswer("ok"));
                    output.flush();
                } catch (IOException e) {
                    //CHE FACCIO?
                }
            } else {
                try {
                    output.writeObject(new GenericAnswer("move not allowed"));
                    output.flush();
                    while (!checker1) {
                        tmp = (Message) input.readObject();
                        if (tmp instanceof ChosenCloud) {
                            checker1 = true;
                            chooseCloud(tmp);
                        } else {
                            output.writeObject(new GenericAnswer("error"));
                            output.flush();
                        }
                    }
                } catch (IOException | ClassNotFoundException e){
                    //CHE FACCIO?
                }
            }
        }
    }
}