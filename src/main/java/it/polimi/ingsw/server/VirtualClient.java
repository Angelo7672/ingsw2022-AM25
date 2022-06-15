package it.polimi.ingsw.server;

import it.polimi.ingsw.client.message.*;
import it.polimi.ingsw.client.message.special.*;
import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.server.answer.*;
import it.polimi.ingsw.server.answer.viewmessage.*;
import it.polimi.ingsw.server.expertmode.ExpertGame;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class VirtualClient implements Runnable, Comparable<VirtualClient>{
    private final Socket socket;
    private final Entrance server;
    private final Proxy_s proxy;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private boolean connectionExpired;
    private Integer playerRef;
    private boolean expertMode;
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
    private ExpertGame expertGame;
    private boolean special1;
    private boolean special3;
    private boolean special5;
    private boolean special7;
    private boolean special9;
    private boolean special10;
    private boolean special11;
    private boolean special12;
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
        this.gameSetup = new GameSetup(this);
        this.objGame = new Object();
        gameSetup.start();
        this.readyPlanningPhase = false;
        this.oneCardAtaTime = false;
        this.readyActionPhase = false;
        this.special1 = false;
        this.special3 = false;
        this.special5 = false;
        this.special7 = false;
        this.special9 = false;
        this.special10 = false;
        this.special11 = false;
        this.special12 = false;
        this.planLocker = new Object();
        this.actionLocker = new Object();
        this.errorLocker = new Object();
        this.error = false;
        try {
            this.input = new ObjectInputStream(socket.getInputStream());
            this.output = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException e) { clientConnectionExpired(e); }
        proxy.incrLimiter();
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
                    roundPartTwo.setActionMsg(tmp);
                    if (!error) synchronized (actionLocker) { actionLocker.notify(); }
                    else {
                        error = false;
                        synchronized (errorLocker) { errorLocker.notify(); }
                    }

                }else if(special1) {
                    special1 = false;
                    if (tmp instanceof Special1Message) {
                        expertGame.setSpecialMsg(1, tmp);
                        expertGame.wakeUp(1);
                    } else send(new GenericAnswer("error"));
                }else if(special3) {
                    special3 = false;
                    if (tmp instanceof Special3Message) {
                        expertGame.setSpecialMsg(3, tmp);
                        expertGame.wakeUp(3);
                    } else send(new GenericAnswer("error"));
                }else if(special5) {
                    special5 = false;
                    if (tmp instanceof Special5Message) {
                        expertGame.setSpecialMsg(5, tmp);
                        expertGame.wakeUp(5);
                    } else send(new GenericAnswer("error"));
                }else if(special7){
                    special7 = false;
                    if(tmp instanceof Special7Message){
                        expertGame.setSpecialMsg(7, tmp);
                        expertGame.wakeUp(7);
                    } else send(new GenericAnswer("error"));
                }else if(special9){
                    special9 = false;
                    if(tmp instanceof Special9Message){
                        expertGame.setSpecialMsg(9, tmp);
                        expertGame.wakeUp(9);
                    } else send(new GenericAnswer("error"));
                }else if(special10){
                    special10 = false;
                    if(tmp instanceof Special10Message){
                        expertGame.setSpecialMsg(10, tmp);
                        expertGame.wakeUp(10);
                    } else send(new GenericAnswer("error"));
                }else if(special11) {
                    special11 = false;
                    if (tmp instanceof Special11Message) {
                        expertGame.setSpecialMsg(11, tmp);
                        expertGame.wakeUp(11);
                    } else send(new GenericAnswer("error"));
                }else if(special12){
                    special12 = false;
                    if(tmp instanceof Special12Message){
                        expertGame.setSpecialMsg(12, tmp);
                        expertGame.wakeUp(12);
                    } else send(new GenericAnswer("error"));

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
                }else System.out.println("errore! "+playerRef); //ovviamente da cambiare
            }
        }catch (SocketException socketException){ clientConnectionExpired(socketException);
        }catch (IOException | ClassNotFoundException e){
            System.err.println(e.getMessage());
            System.out.println("Client disconnected!");
            connectionExpired = true;
            proxy.clientDisconnected(playerRef);
            server.exitError();
        }
    }

    public void unlockPlanningPhase(){ oneCardAtaTime = true; }
    public void unlockActionPhase(){ readyActionPhase = true; }

    //Message to client
    public void sendPlayCard(){ send(new PlayCard()); }
    public void sendStartTurn(){ send(new StartTurn()); }
    public void sendWinner(String winner){ send(new GameOverAnswer(winner)); }

    public void send(Answer serverAnswer){
        try {
            output.writeObject(serverAnswer);
            output.flush();
            output.reset();
        }catch (IOException e){ clientConnectionExpired(e); }
    }
    private void clientConnectionExpired(IOException e){
        System.err.println(e.getMessage());
        System.out.println("Client disconnected!");
        connectionExpired = true;
        proxy.clientDisconnected(playerRef);
        server.exitError();
    }
    public void closeSocket(){
        try {
            send(new DisconnectedAnswer());
            System.out.println("disconnesso: "+playerRef);
            this.socket.close();
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
    public void setSpecial(int specialRef, int cost){ send(new SetSpecialAnswer(specialRef,cost)); System.out.println("special"+specialRef); System.out.println("cost:"+cost);}
    public void sendUsedSpecial(int playerRef, int indexSpecial){ send(new UseSpecialAnswer(playerRef,indexSpecial)); }
    public void sendHandAfterRestore(ArrayList<String> hand){ send(new HandAfterRestoreAnswer(hand)); }

    public void setPlayerRef(int playerRef) { this.playerRef = playerRef; }
    public int getPlayerRef() { return playerRef; }

    public void setExpertGame(ExpertGame expertGame){ this.expertGame = expertGame; }
    public void setSpecial1() { this.special1 = true; }
    public void setSpecial3() { this.special3 = true; }
    public void setSpecial5() { this.special5 = true; }
    public void setSpecial7() { this.special7 = true; }
    public void setSpecial9() { this.special9 = true; }
    public void setSpecial10() { this.special10 = true; }
    public void setSpecial11() { this.special11 = true; }
    public void setSpecial12() { this.special12 = true; }

    @Override
    public int compareTo(VirtualClient v){ return playerRef.compareTo(v.getPlayerRef()); }

    private class GameSetup extends Thread{
        private final VirtualClient virtualClient;
        Message setupMsg;

        public GameSetup(VirtualClient virtualClient){ this.virtualClient = virtualClient; }

        @Override
        public void run(){
            try{
                synchronized (objGame){
                    objGame.wait();
                    if (proxy.isFirst()) gameSetting();
                    loginClient();
                }
                expertMode = server.isExpertMode();
                roundPartOne = new RoundPartOne();
                roundPartTwo = new RoundPartTwo(expertMode,virtualClient);
                roundPartOne.start();
                roundPartTwo.start();
            }catch (InterruptedException e) { e.printStackTrace(); }
        }

        private void gameSetting(){
            GenericMessage msg = (GenericMessage) setupMsg;

            try {
                if (msg.getMessage().equals("Ready for login!")) {
                    if(server.checkFile()){
                        List<Integer> savedGame = server.lastSavedGame();
                        SavedGameAnswer saveMsg = new SavedGameAnswer(savedGame);
                        send(saveMsg);
                        synchronized (setupLocker) {
                            clientInitialization = true;
                            setupLocker.wait();
                            userDecision(saveMsg.getNumberOfPlayers(),saveMsg.isExpertMode());
                        }
                    } else {
                        send(new SetupGameMessage());
                        synchronized (setupLocker) {
                            gameSetupInitialization = true;
                            setupLocker.wait();
                            setupGame();
                        }
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
        private void userDecision(int numberOfPlayers, Boolean expertMode){
            GenericMessage msg = (GenericMessage) setupMsg;

            try {
                if (msg.getMessage().equals("y")){
                    proxy.setRestoreGame(true);
                    proxy.setConnectionsAllowed(numberOfPlayers);
                    server.startController(numberOfPlayers,expertMode);
                    server.createGame();
                    server.restoreVirtualView();
                    send(new GenericAnswer("ok"));
                    synchronized (setupLocker) {
                        clientInitialization = true;
                        setupLocker.wait();
                    }
                } else if (msg.getMessage().equals("n")){
                    proxy.setRestoreGame(false);
                    send(new GenericAnswer("ok"));
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
                        userDecision(numberOfPlayers,expertMode);
                    }
                }
            } catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        private void setupGame() {
            SetupGame msg = (SetupGame) setupMsg;

            try {
                if (msg.getPlayersNumber() >= 2 && msg.getPlayersNumber() <= 4) {
                    server.startController(msg.getPlayersNumber(),msg.getExpertMode());
                    proxy.setConnectionsAllowed(msg.getPlayersNumber());
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
                    if(proxy.isRestoreGame()){
                        send(new LoginRestoreAnswer());
                        synchronized (setupLocker) {
                            loginInitialization = true;
                            setupLocker.wait();
                            readyRestore();
                        }
                    }else {
                        send(new LoginMessage(server.alreadyChosenCharacters()));
                        synchronized (setupLocker) {
                            loginInitialization = true;
                            setupLocker.wait();
                            setupConnection();
                        }
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
        private void readyRestore(){
            SetupConnection msg = (SetupConnection) setupMsg;
            int checker;

            try {
                checker = server.checkRestoreNickname(msg.getNickname());
                if (checker != -1){
                    setPlayerRef(checker);
                    send(new GenericAnswer("ok"));
                    synchronized (setupLocker){
                        clientInitialization = true;
                        setupLocker.wait();
                        readyStart();
                    }
                }
                else {
                    send(new GenericAnswer("error"));
                    synchronized (errorLocker) {
                        loginInitialization = true;
                        error = true;
                        errorLocker.wait();
                        readyRestore();
                    }
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
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
                    synchronized (setupLocker) {
                        clientInitialization = true;  //controlla bene, questo fa ricevere il ready for play card
                        setupLocker.wait();
                        if(proxy.isRestoreGame()) readyToPlay();
                        else readyPlanningPhase();
                    }
                }
            }catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        private void readyToPlay(){
            GenericMessage msg = (GenericMessage) setupMsg;

            try{
                if(!msg.getMessage().equals("Ready to play!")){
                    send(new GenericAnswer("error"));
                    synchronized (errorLocker) {
                        clientInitialization = true;
                        error = true;
                        errorLocker.wait();
                        readyToPlay();
                    }
                }
            } catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        private void readyPlanningPhase(){
            GenericMessage msg = (GenericMessage) setupMsg;

            try {
                if (!msg.getMessage().equals("Ready for Planning Phase")) {
                    send(new GenericAnswer("error"));
                    synchronized (errorLocker) {
                        clientInitialization = true;
                        error = true;
                        errorLocker.wait();
                        readyPlanningPhase();
                    }
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
                        planningPhase();
                        readyForAction();
                        server.resumeTurn(1);
                    }
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
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
        private final VirtualClient virtualClient;
        private Message actionMsg;
        private int numberOfPlayer;
        private boolean studentLocker;
        private int studentCounter;
        private boolean motherLocker;
        private boolean cloudLocker;
        private final boolean expertMode;

        public RoundPartTwo(Boolean expertMode, VirtualClient virtualClient){
            this.virtualClient = virtualClient;
            this.expertMode = expertMode;
            this.studentLocker = false;
            this.studentCounter = 0;
            this.motherLocker = false;
            this.cloudLocker = false;
        }

        @Override
        public void run(){
            try {
                numberOfPlayer = proxy.getConnectionsAllowed();
                while (!victory) {
                    synchronized (actionLocker) {
                        actionLocker.wait();
                        studentLocker = true;
                        actionPhase();
                        server.resumeTurn(0);
                    }
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
        }

        private void actionPhase() {
            boolean go;

            try {
                if (studentLocker) {
                    studentLocker = false;
                    go = true;
                    while (go) {
                        if (actionMsg instanceof MoveStudent) {
                            if (numberOfPlayer == 2 || numberOfPlayer == 4) {
                                moveStudent();
                                if (studentCounter == 3) {
                                    studentCounter = 0;
                                    motherLocker = true;
                                    readyActionPhase = true;
                                    send(new GenericAnswer("transfer complete"));
                                    synchronized (actionLocker) { actionLocker.wait(); }
                                    go = false;
                                } else if (studentCounter < 3) {
                                    readyActionPhase = true;
                                    send(new GenericAnswer("ok"));
                                    synchronized (actionLocker) { actionLocker.wait(); } //attenzione potrebbe arrivare lo special
                                }
                            } else if (numberOfPlayer == 3) {
                                moveStudent();
                                if (studentCounter == 4) {
                                    studentCounter = 0;
                                    motherLocker = true;
                                    readyActionPhase = true;
                                    send(new GenericAnswer("transfer complete"));
                                    synchronized (actionLocker) { actionLocker.wait(); }
                                    go = false;
                                } else if (studentCounter < 4) {
                                    readyActionPhase = true;
                                    send(new GenericAnswer("ok"));
                                    synchronized (actionLocker) { actionLocker.wait(); } //attenzione potrebbe arrivare lo special
                                }
                            }
                        }else if(expertMode){
                            if(actionMsg instanceof UseSpecial){
                                if (!expertGame.effect(
                                        ((UseSpecial) actionMsg).getIndexSpecial(), playerRef, virtualClient)
                                ) send(new MoveNotAllowedAnswer());
                            }
                        } else send(new GenericAnswer("error"));
                    }
                }
                if (motherLocker) {
                    motherLocker = false;
                    if (actionMsg instanceof MoveMotherNature) moveMotherNature();
                    else if(expertMode){
                        if(actionMsg instanceof UseSpecial){
                            if (!expertGame.effect(
                                    ((UseSpecial) actionMsg).getIndexSpecial(), playerRef, virtualClient)
                            ) send(new MoveNotAllowedAnswer());
                        }
                    } else send(new GenericAnswer("error"));
                }
                if (cloudLocker) {
                    cloudLocker = false;
                    if (actionMsg instanceof ChosenCloud) chooseCloud();
                    else if(expertMode){
                        if(actionMsg instanceof UseSpecial){
                            if (!expertGame.effect(
                                    ((UseSpecial) actionMsg).getIndexSpecial(), playerRef, virtualClient)
                            ) send(new MoveNotAllowedAnswer());
                        }
                    } else send(new GenericAnswer("error"));
                }
            }catch (InterruptedException e) { e.printStackTrace(); }
        }

        private void moveStudent(){
            MoveStudent studentMovement = (MoveStudent) actionMsg;
            boolean checker;

            checker = server.userMoveStudent(playerRef, studentMovement.getColor(), studentMovement.isInSchool(), studentMovement.getIslandRef());
            try {
                if (checker) studentCounter++;
                else {
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
                    send(new MoveNotAllowedAnswer());
                    synchronized (errorLocker){
                        readyActionPhase = true;
                        error = true;
                        errorLocker.wait();
                        moveMotherNature();
                    }
                }
            } catch (InterruptedException ex) { ex.printStackTrace();
            } catch (EndGameException endGameException) { proxy.gameOver(); }
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
}