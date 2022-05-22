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
    private final int playerRef;    //penso si possa eliminare
    private boolean clientInitialization;
    private boolean oneCardAtaTime;
    private boolean readyActionPhase;
    private boolean victory;

    public VirtualClient(Socket socket, Entrance server, Proxy_s proxy, int playerRef){
        this.socket = socket;
        this.server = server;
        this.proxy = proxy;
        this.playerRef = playerRef;
        this.victory = false;
        this.clientInitialization = true;
        this.oneCardAtaTime = false;
        this.readyActionPhase = false;
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
        Message tmp;

        try {
            this.socket.setSoTimeout(15000);    //come faccio a notificare lo spegnimento del socket?
            while (!victory){
                try {
                    tmp = (Message) input.readObject();
                    if (tmp instanceof PingMessage) {
                        try {
                            this.socket.setSoTimeout(15000);
                        }catch (SocketException socketException){
                        //CHE FACCIO?
                        }

                        //Thread a parte
                    } else if (tmp instanceof CardMessage) {
                        if(oneCardAtaTime) {
                            oneCardAtaTime = false;
                            planningPhase((CardMessage) tmp);
                            notifyAll();    //da cambiare
                        }
                    } else if (tmp instanceof MoveStudent || tmp instanceof UseSpecial) {  //da sistemare
                        if(readyActionPhase) {
                            readyActionPhase = false;
                            actionPhase(tmp);
                            notifyAll();    //da cambiare
                        }

                        //solo una volta, ma penso serva comunque un thread
                    } else if (clientInitialization) {
                        if(tmp instanceof GenericMessage) {
                            if (proxy.getConnectionsAllowed() == 1) gameSetting(tmp);
                            loginClient(tmp);
                            clientInitialization = false;   //only one execution
                        }
                    }
                }catch (IOException | ClassNotFoundException e){
                    //CHE FACCIO?
                }
            }
        }catch (SocketException socketException){
            //CHE FACCIO?
        }
    }





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

    private class GameSetup() extends Thread{

        private void gameSetting(Message msg){
            Message tmp;
            boolean checker = false;

            if(((GenericMessage) msg).getMessage().equals("Ready for login!")) {
                proxy.setConnectionsAllowed(0);
                try {
                    output.writeObject(new SetupGameMessage());
                    output.flush();
                    try {
                        tmp = (Message) input.readObject();
                        setupGame(tmp);
                    }catch (IOException | ClassNotFoundException e){
                        //CHE FACCIO?
                    }
                }catch (IOException e) {
                    //CHE FACCIO?
                }
            }else {
                while (!checker) {
                    try {
                        output.writeObject(new GenericAnswer("error"));
                        output.flush();
                        try {
                            tmp = (Message) input.readObject();
                            if (tmp instanceof GenericMessage) {
                                checker = true;
                                gameSetting(tmp);
                            }
                        }catch (IOException | ClassNotFoundException e){
                            //CHE FACCIO?
                        }
                    }catch (IOException e) {
                        //CHE FACCIO?
                    }
                }
            }
        }
        private void setupGame(Message msg){
            Message tmp;
            SetupGame res;

            if (msg instanceof SetupGame) {
                res = (SetupGame) msg;
                //fare check
                proxy.setConnectionsAllowed(res.getPlayersNumber());
                server.setNumberOfPlayer(res.getPlayersNumber());
                server.setExpertMode(res.getExpertMode());
                server.startGame();
            }else {
                try {
                    output.writeObject(new GenericAnswer("error"));
                    output.flush();
                    try {
                        tmp = (Message) input.readObject();
                        setupGame(tmp);
                    }catch (IOException | ClassNotFoundException e){
                        //CHE FACCIO?
                    }
                }catch (IOException e) {
                    //CHE FACCIO?
                }
            }
        }
        private void loginClient(Message tmp){
            boolean checker = false;

            if(((GenericMessage) tmp).getMessage().equals("Ready for login!")){
                try {
                    output.writeObject(new GenericAnswer("Ready for login!"));
                    output.flush();
                    try {
                        tmp = (Message) input.readObject();
                        setupConnection(tmp);
                    }catch (IOException | ClassNotFoundException e){
                        //CHE FACCIO?
                    }
                }catch (IOException e) {
                    //CHE FACCIO?
                }
            }else {
                while (!checker) {
                    try {
                        output.writeObject(new GenericAnswer("error"));
                        output.flush();
                        try {
                            tmp = (Message) input.readObject();
                            if (tmp instanceof GenericMessage) {
                                checker = true;
                                loginClient(tmp);
                            }
                        }catch (IOException | ClassNotFoundException e){
                            //CHE FACCIO?
                        }
                    }catch (IOException e) {
                        //CHE FACCIO?
                    }
                }
            }
        }
        private void setupConnection(Message msg) {
            Message tmp;
            SetupConnection login;
            boolean checker;

            if (msg instanceof SetupConnection) {
                login = (SetupConnection) msg;
                checker = server.userLogin(login.getNickname(),login.getCharacter(),playerRef);
                if(checker){
                    try {
                        output.writeObject(new LoginMessage(server.getNumberOfPlayer(), server.isExpertMode(),/*TODO metodo che guarda team in virtual view*/));
                        output.flush();
                        //mandare un ok
                        //arriva il "ready to start" generic
                    } catch (IOException e) {
                        //CHE FACCIO?
                    }
                }else{
                    try {
                        output.writeObject(new GenericAnswer("error"));
                        output.flush();
                        try {
                            tmp = (Message) input.readObject();
                            setupConnection(tmp);
                        }catch (IOException | ClassNotFoundException e){
                            //CHE FACCIO?
                        }
                    }catch (IOException e) {
                        //CHE FACCIO?
                    }
                }
            }else{
                try {
                    output.writeObject(new GenericAnswer("error"));
                    output.flush();
                    try {
                        tmp = (Message) input.readObject();
                        setupConnection(tmp);
                    }catch (IOException | ClassNotFoundException e){
                        //CHE FACCIO?
                    }
                }catch (IOException e) {
                    //CHE FACCIO?
                }
            }
        }
    }

    private class Round() extends Thread{

        //Planning Phase
        public void unlockPlanningPhase(){ oneCardAtaTime = true; }
        private void planningPhase(CardMessage card) {
            boolean checker;
            Message tmp;

            checker = server.userPlayCard(playerRef, card.getCard());
            if(checker) {
                try {
                    output.writeObject(new GenericAnswer("ok"));
                    output.flush();
                } catch (IOException e){
                    //CHE FACCIO?
                }
            }else{
                try {
                    output.writeObject(new GenericAnswer("move not allowed"));  //crea messaggio Move not allowed
                    output.flush();
                }catch (IOException e){
                    //CHE FACCIO?
                }
                while (!checker) {
                    try {
                        tmp = (Message) input.readObject();
                        if (tmp instanceof CardMessage) {
                            checker = true;
                            planningPhase((CardMessage) tmp);
                        } else {
                            try {
                                output.writeObject(new GenericAnswer("error"));
                                output.flush();
                            } catch (IOException e) {
                                //CHE FACCIO?
                            }
                        }
                    }catch (IOException | ClassNotFoundException e) {
                        //CHE FACCIO?
                    }
                }
            }
        }

        //Action Phase
        private void actionPhase(Message msg) {
            Message tmp;
            UseSpecial special;


            if(msg instanceof MoveStudent) moveStudent(msg);
            //TODO: special, transfer complete alla fine di tutti gli studenti Generic Answer
            if(msg instanceof MoveMotherNature) moveMotherNature(msg);
            //TODO: special
            if(msg instanceof ChosenCloud) chooseCloud(msg);

        }
        public void unlockActionPhase(){ readyActionPhase = true; }
        private void moveStudent(Message msg){
            Message tmp;
            MoveStudent studentMovement;
            boolean checker;
            boolean checker1 = false;

            studentMovement = (MoveStudent) msg;
            checker = server.userMoveStudent(playerRef, studentMovement.getColor(), studentMovement.isInSchool(), studentMovement.getIslandRef());
            if(checker) {
                try {
                    output.writeObject(new GenericAnswer("ok"));
                    output.flush();
                } catch (IOException e){
                    //CHE FACCIO?
                }
            } else {
                try {
                    output.writeObject(new GenericAnswer("move not allowed"));
                    output.flush();
                    while (!checker1) {
                        try {
                            tmp = (Message) input.readObject();
                            if (tmp instanceof MoveStudent) {
                                checker1 = true;
                                moveStudent(tmp);
                            } else {
                                try {
                                    output.writeObject(new GenericAnswer("error"));
                                    output.flush();
                                } catch (IOException e) {
                                    //CHE FACCIO?
                                }
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            //CHE FACCIO?
                        }
                    }
                } catch (IOException e) {
                    //CHE FACCIO?
                }
            }
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
                            try {
                                tmp = (Message) input.readObject();
                                if (tmp instanceof MoveMotherNature) {
                                    checker1 = true;
                                    moveMotherNature(tmp);
                                } else {
                                    try {
                                        output.writeObject(new GenericAnswer("error"));
                                        output.flush();
                                    } catch (IOException e) {
                                        //CHE FACCIO?
                                    }
                                }
                            } catch (IOException | ClassNotFoundException e) {
                                //CHE FACCIO?
                            }
                        }
                    }catch(IOException e){
                        //CHE FACCIO?
                    }
                }
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
                        try {
                            tmp = (Message) input.readObject();
                            if (tmp instanceof ChosenCloud) {
                                checker1 = true;
                                chooseCloud(tmp);
                            } else {
                                try {
                                    output.writeObject(new GenericAnswer("error"));
                                    output.flush();
                                } catch (IOException e) {
                                    //CHE FACCIO?
                                }
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            //CHE FACCIO?
                        }
                    }
                } catch (IOException e) {
                    //CHE FACCIO?
                }
            }
        }

    }
}