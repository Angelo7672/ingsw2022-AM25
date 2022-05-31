package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Message.*;
import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.server.Answer.*;
import it.polimi.ingsw.server.Answer.ViewMessage.*;

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
    private boolean connectionExpired;
    private final int playerRef;
    private boolean clientInitialization;
    private boolean gameSetupInitialization;
    private boolean loginInitialization;
    private GameSetup gameSetup;
    private Object setupLocker;
    private Object objGame ;
    private boolean oneCardAtaTime;
    private boolean readyPlanningPhase;
    private boolean readyActionPhase;
    private boolean victory;
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
        this.connectionExpired = false;
        this.playerRef = playerRef;
        this.victory = false;
        this.clientInitialization = true;
        this.gameSetupInitialization = false;
        this.loginInitialization = false;
        this.setupLocker = new Object();
        this.gameSetup = new GameSetup();
        this.objGame = new Object();
        gameSetup.start();
        this.readyPlanningPhase = false;
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
            this.input = new ObjectInputStream(socket.getInputStream());
            this.output = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException e) { clientConnectionExpired(e); }
    }

    @Override
    public void run() {
        Message tmp;
        boolean first = true;

        try {
            this.socket.setSoTimeout(15000);
            while (!victory || !connectionExpired){
                tmp = (Message) input.readObject();

                if (tmp instanceof PingMessage) {
                    this.socket.setSoTimeout(15000);    //reset timeout
                    send(new PongAnswer());

                }else if(readyPlanningPhase) { //Planning Phase msg
                    readyPlanningPhase = false;
                    if (tmp instanceof GenericMessage) {
                        roundPartOne.setPlanningMsg(tmp);
                        if (!error) synchronized(planLocker){ planLocker.notify(); }
                        else {
                            error = false;
                            synchronized(errorLocker){ errorLocker.notify(); }
                        }
                    }

                } else if(oneCardAtaTime) { //Planning Phase msg
                    oneCardAtaTime = false;
                    if (tmp instanceof CardMessage) {
                        roundPartOne.setPlanningMsg(tmp);
                        if (!error) synchronized(planLocker){ planLocker.notify(); }
                        else {
                            error = false;
                            synchronized(errorLocker){ errorLocker.notify(); }
                        }
                    }

                } else if(readyActionPhase) {    //Action Phase msg
                    readyActionPhase = false;
                    System.out.println("action"+playerRef);
                    roundPartTwo.setActionMsg(tmp);
                    if (!error) synchronized (actionLocker) { actionLocker.notify(); }
                    else {
                        error = false;
                        synchronized (errorLocker) { errorLocker.notify(); }
                    }

                } else if (clientInitialization) {  //login msg
                    clientInitialization = false;
                    if (tmp instanceof GenericMessage) {
                        gameSetup.setSetupMsg(tmp);
                        if (first) {
                            first = false;
                            synchronized (objGame) { objGame.notify(); }
                        } else if (!error) synchronized (setupLocker) { setupLocker.notify(); }
                        else {
                            error = false;
                            synchronized (errorLocker) { errorLocker.notify(); }
                        }
                    }
                }else if(loginInitialization){  //nickname and character msg
                    loginInitialization = false;
                    if(tmp instanceof SetupConnection){
                        gameSetup.setSetupMsg(tmp);
                        if(!error) synchronized (setupLocker) { setupLocker.notify(); }
                        else {
                            error = false;
                            synchronized(errorLocker){ errorLocker.notify(); }
                        }
                    }
                }else if (gameSetupInitialization){ //game setup msg
                    gameSetupInitialization = false;
                    if(tmp instanceof SetupGame){
                        gameSetup.setSetupMsg(tmp);
                        if(!error) synchronized (setupLocker) { setupLocker.notify(); }
                        else {
                            error = false;
                            synchronized(errorLocker){ errorLocker.notify(); }
                        }
                    }
                }else System.out.println("errore!"); //ovviamente da cambiare
            }
        }catch (SocketException socketException){
            clientConnectionExpired(socketException);
        }catch (IOException | ClassNotFoundException e){
            System.err.println(e.getMessage());
            System.out.println("Client disconnected!");
            connectionExpired = true;
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
    
    public void sendGameInfo(int numberOfPlayers, boolean expertMode){ send(new GameInfoAnswer(numberOfPlayers,expertMode)); }
    public void sendUserInfo(int playerRef, String nickname, String character){ send(new UserInfoAnswer(playerRef,nickname,character)); }
    public void studentsChangeInSchool(int color, String place, int componentRef, int newStudentsValue){ send(new SchoolStudentMessage(color,place,componentRef,newStudentsValue)); }
    public void studentChangeOnIsland(int islandRef, int color, int newStudentsValue){ send(new IslandStudentMessage(islandRef,color,newStudentsValue)); }
    public void studentChangeOnCloud(int cloudRef, int color, int newStudentsValue){ send(new CloudStudentMessage(cloudRef,color,newStudentsValue)); }
    public void professorChangePropriety(int playerRef, int color, boolean newProfessorValue){ send(new ProfessorMessage(playerRef,newProfessorValue,color));}
    public void motherChangePosition(int newMotherPosition){ send(new MotherPositionMessage(newMotherPosition)); }
    public void lastCardPlayedFromAPlayer(int playerRef, String assistantCard){ send(new LastCardMessage(playerRef,assistantCard)); }
    public void numberOfCoinsChangeForAPlayer(int playerRef, int newCoinsValue){ send(new CoinsMessage(newCoinsValue,playerRef)); }
    public void dimensionOfAnIslandIsChange(int islandToDelete){ send(new UnifiedIsland(islandToDelete)); }
    public void towersChangeInSchool(int playerRef, int towersNumber){ send(new SchoolTowersMessage(playerRef,towersNumber)); }
    public void towersChangeOnIsland(int islandRef, int towersNumber){ send(new IslandTowersNumberMessage(islandRef,towersNumber)); }
    public void towerChangeColorOnIsland(int islandRef, int newColor){ send(new IslandTowersColorMessage(islandRef,newColor)); }
    public void islandInhibited(int islandRef, int isInhibited){ send(new InhibitedIslandMessage(islandRef,isInhibited)); }
    public void setSpecial(int specialRef){ send(new SetSpecialAnswer(specialRef)); }

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
                    send(new SetupGameMessage());
                    synchronized (setupLocker) {
                        gameSetupInitialization = true;
                        setupLocker.wait();
                        setupGame();
                    }
                } else {
                    send(new GenericAnswer("error"));
                    synchronized (errorLocker) {
                        clientInitialization = true;
                        error = true;
                        errorLocker.wait();
                        gameSetting();
                    }
                }
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        private void setupGame() {
            SetupGame msg = (SetupGame) setupMsg;

            try {
                if (msg.getPlayersNumber() >= 2 && msg.getPlayersNumber() <= 4) {
                    proxy.setConnectionsAllowed(msg.getPlayersNumber());
                    server.startController(msg.getPlayersNumber(),msg.getExpertMode());
                    synchronized (proxy){ proxy.notify(); }
                    send(new GenericAnswer("ok"));
                    synchronized (setupLocker) {
                        clientInitialization = true;
                        setupLocker.wait();
                    }
                } else {
                    send(new GenericAnswer("error"));
                    synchronized (errorLocker) {
                        gameSetupInitialization = true;
                        error = true;
                        errorLocker.wait();
                        setupGame();
                    }
                }
            }catch(InterruptedException ex){ ex.printStackTrace(); }
        }
        private void loginClient(){
            GenericMessage msg = (GenericMessage) setupMsg;

            try {
                if (msg.getMessage().equals("Ready for login!")) {
                    send(new LoginMessage(server.alreadyChosenCharacters()));
                    synchronized (setupLocker) {
                        loginInitialization = true;
                        setupLocker.wait();
                        setupConnection();
                    }
                } else {
                    send(new GenericAnswer("error"));
                    synchronized (errorLocker) {
                        clientInitialization = true;
                        error = true;
                        errorLocker.wait();
                        loginClient();
                    }
                }
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        private void setupConnection() {
            SetupConnection msg = (SetupConnection) setupMsg;
            boolean checker;

            checker = server.userLogin(msg.getNickname(), msg.getCharacter());
            try {
                if (checker) {
                    send(new GenericAnswer("ok"));
                    synchronized (setupLocker){
                        clientInitialization = true;
                        setupLocker.wait();
                        readyStart();
                    }
                } else {
                    //send(new GenericAnswer("error"));
                    send(new LoginMessage(server.alreadyChosenCharacters()));
                    synchronized (errorLocker) {
                        loginInitialization = true;
                        error = true;
                        errorLocker.wait();
                        setupConnection();
                    }
                }
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        private void readyStart(){
            GenericMessage msg = (GenericMessage) setupMsg;

            try {
                if (!msg.getMessage().equals("Ready to start")) {
                    send(new GenericAnswer("error"));
                    synchronized (errorLocker) {
                        clientInitialization = true;
                        error = true;
                        errorLocker.wait();
                        readyStart();
                    }
                }else{
                    proxy.thisClientIsReady();
                    readyPlanningPhase = true;  //controlla bene, questo fa ricevere il ready for play card
                }
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
                        readyPlanningPhase();
                        readyForAction();
                        server.resumeTurn();
                    }
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
        private void readyPlanningPhase(){
            GenericMessage msg = (GenericMessage) planningMsg;

            try {
                if (!msg.getMessage().equals("Ready for Planning Phase")) {
                    send(new GenericAnswer("error"));
                    synchronized (errorLocker) {
                        oneCardAtaTime = true;
                        error = true;
                        errorLocker.wait();
                        readyPlanningPhase();
                    }
                }else{
                    synchronized (planLocker){ planLocker.wait(); }
                    planningPhase();
                }
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        private void planningPhase() {
            CardMessage cardMessage = (CardMessage) planningMsg;
            boolean checker;

            try {
                checker = server.userPlayCard(playerRef, cardMessage.getCard());
                if(checker){
                    send(new GenericAnswer("ok"));
                    readyPlanningPhase = true;  //dai un nome migliore
                    synchronized (planLocker){ planLocker.wait(); }  //wait ready for action phase msg
                }
                else{
                    send(new MoveNotAllowedAnswer());
                    synchronized (errorLocker) {
                        oneCardAtaTime = true;
                        error = true;
                        errorLocker.wait();
                        planningPhase();
                    }
                }
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        private void readyForAction(){
            GenericMessage readyMsg = (GenericMessage) planningMsg;

            try {
                if (!readyMsg.getMessage().equals("Ready for Action Phase")) {
                    send(new GenericAnswer("error"));
                    synchronized (errorLocker) {
                        oneCardAtaTime = true;
                        error = true;
                        errorLocker.wait();
                        readyForAction();
                    }
                }
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }

        public void setPlanningMsg(Message msg) { this.planningMsg = msg; }
    }

    private class RoundPartTwo extends Thread{
        private Message actionMsg;
        private int numberOfPlayer;
        private boolean studentLocker;
        private int studentCounter;
        private boolean studentAlt;
        private boolean motherLocker;
        private boolean cloudLocker;

        public RoundPartTwo(){
            this.numberOfPlayer = proxy.getConnectionsAllowed();
            this.studentLocker = true;
            this.studentCounter = 0;
            this.studentAlt = false;
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
            boolean go = true;

            if(studentLocker) {
                studentLocker = false;
                while (go){
                    if (actionMsg instanceof MoveStudent){
                        moveStudent();
                        go = false;
                    }
                    else send(new GenericAnswer("error"));
                }
                go = true;
            } if(motherLocker) {
                motherLocker = false;
                while(go) {
                    if (actionMsg instanceof MoveMotherNature){
                        moveMotherNature();
                        go = false;
                    }
                    else send(new GenericAnswer("error"));
                }
                go = true;
            } if(cloudLocker) {
                cloudLocker = false;
                while (go) {
                    if (actionMsg instanceof ChosenCloud){
                        chooseCloud();
                        go = false;
                    }
                    else send(new GenericAnswer("error"));
                }
            }
        }

        private void moveStudent(){
            MoveStudent studentMovement = (MoveStudent) actionMsg;
            boolean checker;

            checker = server.userMoveStudent(playerRef, studentMovement.getColor(), studentMovement.isInSchool(), studentMovement.getIslandRef());
            System.out.println("ciao"+playerRef);
            try {
                if (checker) {
                    studentCounter++;
                    if (numberOfPlayer == 2 || numberOfPlayer == 4) {
                        if (studentCounter == 3) {
                            studentCounter = 0;
                            motherLocker = true;
                            readyActionPhase = true;
                            send(new GenericAnswer("transfer complete"));
                            System.out.println("transfer complete");
                            synchronized (actionLocker){ actionLocker.wait(); }
                        }else if (studentCounter < 3) {
                            readyActionPhase = true;
                            send(new GenericAnswer("ok"));
                            System.out.println("ok");
                            synchronized (actionLocker){ actionLocker.wait(); } //attenzione potrebbe arrivare lo special
                            moveStudent();
                        }
                    } else if (numberOfPlayer == 3) {
                        if (studentCounter == 3) {
                            studentCounter = 0;
                            motherLocker = true;
                            readyActionPhase = true;
                            send(new GenericAnswer("transfer complete"));
                            synchronized (actionLocker){ actionLocker.wait(); }
                        }else if (studentCounter < 4) {
                            readyActionPhase = true;
                            send(new GenericAnswer("ok"));
                            synchronized (actionLocker){ actionLocker.wait(); } //attenzione potrebbe arrivare lo special
                            moveStudent();
                        }
                    }
                } else {
                    send(new MoveNotAllowedAnswer());
                    synchronized (errorLocker){
                        readyActionPhase = true;
                        error = true;
                        errorLocker.wait();
                        moveStudent();
                    }
                }
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        private void moveMotherNature(){
            MoveMotherNature motherMovement = (MoveMotherNature) actionMsg;
            boolean checker;

            try {
                checker = server.userMoveMotherNature(motherMovement.getDesiredMovement());
                if (checker) {
                    cloudLocker = true;
                    readyActionPhase = true;
                    send(new GenericAnswer("ok"));
                    synchronized (actionLocker){ actionLocker.wait(); }
                } else {
                    send(new MoveNotAllowedAnswer());   //controlla dove va a controllare motherMaxMove (special)
                    synchronized (errorLocker){
                        readyActionPhase = true;
                        error = true;
                        errorLocker.wait();
                        moveMotherNature();
                    }
                }
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
                    send(new GenericAnswer("ok"));
                    readyActionPhase = true;
                    synchronized (actionLocker){ actionLocker.wait(); }
                    readyPlanningPhase();
                } else {
                    send(new MoveNotAllowedAnswer());
                    synchronized (errorLocker){
                        readyActionPhase = true;
                        error = true;
                        errorLocker.wait();
                        chooseCloud();
                    }
                }
            } catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        private void readyPlanningPhase(){
            GenericMessage readyMsg = (GenericMessage) actionMsg;

            try {
                if (!readyMsg.getMessage().equals("Ready for Planning Phase")) {
                    send(new GenericAnswer("error"));
                    synchronized (errorLocker) {
                        readyActionPhase = true;
                        error = true;
                        errorLocker.wait();
                        readyPlanningPhase();
                    }
                }
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }

        public void setActionMsg(Message actionMsg) { this.actionMsg = actionMsg; }
    }

    private void send(Answer serverAnswer){
        try {
            output.reset();
            output.writeObject(serverAnswer);
            output.flush();
        }catch (IOException e){ clientConnectionExpired(e); }
    }

    private void clientConnectionExpired(IOException e){
        System.err.println(e.getMessage());
        System.out.println("Client disconnected!");
        connectionExpired = true;
        //metodo per notificare tutti
        server.exitError();
    }

    public void closeSocket(){
        try {
            send(new DisconnectedAnswer());
            this.socket.close();
        }catch (IOException e){ clientConnectionExpired(e); }
    }
}