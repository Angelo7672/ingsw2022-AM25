package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Message.*;
import it.polimi.ingsw.server.Answer.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class VirtualClient implements Runnable{
    private final Socket socket;
    private final Entrance server;
    private final Proxy_s proxy;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private final int playerRef;
    private boolean victory;

    public VirtualClient(Socket socket, Entrance server, Proxy_s proxy, int playerRef){
        this.socket = socket;
        this.server = server;
        this.proxy = proxy;
        this.playerRef = playerRef;
        this.victory = false;
        proxy.incrLimiter();
        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (...) {

        }
    }

    @Override
    public void run() {
        Message tmp;

        while (!victory || ...){
            try {
                tmp = (Message) input.readObject();
                if (tmp instanceof PingMessage) {
                    //TODO: mandare alla classe Pong
                } else if (tmp instanceof CardMessage) {
                    planningPhase((CardMessage) tmp);
                }else if(tmp instanceof MoveStudent || tmp instanceof UseSpecial){
                    actionPhase(tmp);
                } else if (tmp instanceof GenericMessage) {
                    if (proxy.getConnections_allowed() == 1) gameSetting(tmp);
                    loginClient(tmp);
                }
            }
        }
    }


    private void gameSetting(Message tmp){
        SetupGameMessage setup_msg = new SetupGameMessage("Welcome! You are the first! How many players?\nExpert mode yes or no?");
        SetupGame res;

        if(((GenericMessage) tmp).getMessage().equals("Ready for login!")) {
            proxy.setConnections_allowed(0);
            try {
                output.writeObject(setup_msg);
                output.flush();
            } catch (...){

            }
            try {
                res = (SetupGame) input.readObject();
                proxy.setConnections_allowed(res.getPlayersNumber());
                server.setNumberOfPlayer(res.getPlayersNumber());
                server.setExpertMode(res.getExpertMode());
                server.startGame();
            } catch () {

            }
        }
    }

    private void loginClient(Message tmp){
        SetupConnection login;

        if(((GenericMessage) tmp).getMessage().equals("Ready for login!")){
            output.writeObject(new GenericAnswer("Ready for login!"));
            output.flush();
            try{
                tmp = (Message) input.readObject();
                if(tmp instanceof SetupConnection) {
                    login = (SetupConnection) tmp;
                    server.userLogin(login.getNickname(), login.getCharacter(), playerRef);
                    output.writeObject(new LoginMessage(server.getNumberOfPlayer(),server.isExpertMode(),/*TODO metodo che guarda team in virtual view*/));
                    output.flush();
                }
            }catch (...){

            }
        }
    }

    private void planningPhase(CardMessage card) {

        try {
            card = (CardMessage) input.readObject();
            output.writeObject(new GenericAnswer("ok"));
            server.userPlayCard(playerRef, card.getCard());
        }catch () {

        }
    }

    private void actionPhase(Message tmp) {
        MoveStudent student_movement;
        MoveMotherNature mother_movement;
        UseSpecial special;
        ChosenCloud cloud;

        try{
            tmp = (Message) input.readObject();
            if(tmp instanceof MoveStudent){
                student_movement = (MoveStudent) tmp;
                output.writeObject(new GenericAnswer("ok"));
                server.userMoveStudent(playerRef,student_movement.getColor(),student_movement.isInSchool(),student_movement.getIslandRef());
            }else if(tmp instanceof UseSpecial){
                special = (UseSpecial) tmp;
                output.writeObject(new GenericAnswer("ok"));
            }
        }catch (...) {

        }
        try{
            tmp = (Message) input.readObject();
            if(tmp instanceof MoveStudent){
                student_movement = (MoveStudent) tmp;
                output.writeObject(new GenericAnswer("ok"));
                server.userMoveStudent(playerRef,student_movement.getColor(),student_movement.isInSchool(),student_movement.getIslandRef());
            }else if(tmp instanceof UseSpecial){
                special = (UseSpecial) tmp;
                output.writeObject(new GenericAnswer("ok"));
            }
        }catch (...) {

        }
        try{
            tmp = (Message) input.readObject();
            if(tmp instanceof MoveStudent){
                student_movement = (MoveStudent) tmp;
                output.writeObject(new GenericAnswer("ok"));
                server.userMoveStudent(playerRef,student_movement.getColor(),student_movement.isInSchool(),student_movement.getIslandRef());
            }else if(tmp instanceof UseSpecial){
                special = (UseSpecial) tmp;
                output.writeObject(new GenericAnswer("ok"));
            }
        }catch (...) {

        }
        try {
            tmp = (Message) input.readObject();
            if(tmp instanceof MoveMotherNature) {
                mother_movement = (MoveMotherNature) tmp;
                output.writeObject(new GenericAnswer("ok"));
                server.userMoveMotherNature(mother_movement.getDesiredMovement());
            }else if(tmp instanceof UseSpecial){
                special = (UseSpecial) tmp;
                output.writeObject(new GenericAnswer("ok"));
            }
        }catch (...) {

        }
        try {
            tmp = (Message) input.readObject();
            if(tmp instanceof ChosenCloud) {
                cloud = (ChosenCloud) tmp;
                output.writeObject(new GenericAnswer("ok"));
                server.userChooseCloud(playerRef,cloud.getCloud());
            }else if(tmp instanceof UseSpecial){
                special = (UseSpecial) tmp;
                output.writeObject(new GenericAnswer("ok"));
            }
        }catch (...) {

        }
    }

    public void sendInfoCard(ArrayList<String> listCards){
        CardsMessage msg = new CardsMessage(listCards);

        try {
            output.writeObject(msg);
            output.flush();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public void sendStartTurn(){
        StartTurn msg = new StartTurn();

        try {
            output.writeObject(msg);
            output.flush();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public void sendCloud(ArrayList<Integer> availableClouds){
        CloudList msg = new CloudList(availableClouds);

        try {
            output.writeObject(msg);
            output.flush();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }










}
