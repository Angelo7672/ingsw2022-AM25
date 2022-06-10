package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.exception.NotAllowedException;
import it.polimi.ingsw.server.ControllerServer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller implements ServerController{
    private int currentUser;
    private ControllerServer server;
    private VirtualView virtualView;
    private GameManager gameManager;
    private RoundController roundController;
    private boolean jumpPhaseForRestore;
    private int numberOfPlayers;
    private boolean expertMode;
    private String winner;
    private String fileName;

    public Controller(int numberOfPlayers, boolean isExpert, ControllerServer server){
        this.expertMode = isExpert;
        this.numberOfPlayers = numberOfPlayers;
        this.jumpPhaseForRestore = false;
        this.server = server;
        this.fileName = "saveGame.bin";
        this.virtualView = new VirtualView(numberOfPlayers, isExpert, server, this, fileName);
        this.winner = "NONE";
    }

    @Override
    public void startGame(boolean gameSave){
        server.sendGameInfo(numberOfPlayers, expertMode);   //at every client
        for(int i = 0; i < numberOfPlayers; i++)
            server.sendUserInfo(i, virtualView.getNickname(i), virtualView.getCharacter(i));
        this.gameManager = new Game(expertMode, numberOfPlayers);
        gameManager.setStudentsListener(virtualView);
        gameManager.setTowerListener(virtualView);
        gameManager.setProfessorsListener(virtualView);
        gameManager.setPlayedCardListener(virtualView);
        gameManager.setSpecialListener(virtualView);
        gameManager.setCoinsListener(virtualView);
        gameManager.setMotherPositionListener(virtualView);
        gameManager.setIslandListener(virtualView);
        gameManager.setInhibitedListener(virtualView);
        gameManager.setBagListener(virtualView);
        gameManager.setQueueListener(virtualView);

        if(gameSave) virtualView.restoreGame();
        else gameManager.initializeGame();

        this.roundController = new RoundController(this,this.gameManager,server,numberOfPlayers,jumpPhaseForRestore);
        roundController.start();
    }

    @Override
    public ArrayList<String> alreadyChosenCharacters(){ return virtualView.getAlreadyChosenCharacters(); }
    @Override
    public boolean userLoginNickname(String nickname){ return virtualView.checkNewNickname(nickname); }
    @Override
    public boolean userLoginCharacter(String character){ return virtualView.checkNewCharacter(character); }
    @Override
    public void addNewPlayer(String nickname, String character){ virtualView.addNewPlayer(nickname,character); }

    //Planning Phase
    public String getLastPlayedCard(int playerRef){ return virtualView.getLastPlayedCard(playerRef); }
    @Override
    public void playCard(int playerRef, String chosenAssistants) throws NotAllowedException {
        ArrayList<String> alreadyPlayedCard = new ArrayList<>();

        for(int i = 0; i < currentUser; i++)
            alreadyPlayedCard.add(getLastPlayedCard(gameManager.readQueue(i)));

        try { roundController.setEnd(gameManager.playCard(playerRef,currentUser,chosenAssistants,alreadyPlayedCard));
        }catch (NotAllowedException exception){ throw new NotAllowedException(); }
    }

    //Action Phase
    @Override
    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) throws NotAllowedException {
        try { gameManager.moveStudent(playerRef, colour, inSchool, islandRef);
        }catch (NotAllowedException exception){ throw new NotAllowedException(); }
    }
    @Override
    public void moveMotherNature(int desiredMovement) throws NotAllowedException,EndGameException {
        try {
            if(gameManager.moveMotherNature(currentUser,desiredMovement)) {
                oneLastRide();
                throw new EndGameException();
            }
        }catch (NotAllowedException exception){ throw new NotAllowedException(); }
    }
    @Override
    public void chooseCloud(int playerRef, int cloudRef) throws NotAllowedException {
        try { gameManager.chooseCloud(playerRef,cloudRef);
        }catch (NotAllowedException exception){ throw new NotAllowedException(); }
    }
    @Override
    public boolean useSpecialLite(int indexSpecial, int playerRef){
        return gameManager.useSpecialLite(indexSpecial, playerRef);
    }
    @Override
    public boolean useSpecialSimple(int indexSpecial, int playerRef, int ref){
        return gameManager.useSpecialSimple(indexSpecial,playerRef,ref);
    }
    @Override
    public boolean useSpecialMedium(int indexSpecial, int playerRef, int ref, int color){
        return gameManager.useSpecialMedium(indexSpecial,playerRef,ref,color);
    }
    @Override
    public boolean useSpecialHard(int specialIndex, int playerRef, int ref, ArrayList<Integer> color1, ArrayList<Integer> color2){
        return gameManager.useSpecialHard(specialIndex,playerRef,ref,color1,color2);
    }

    @Override
    public void resumeTurn(int phase){
        synchronized(roundController) {
            if(currentUser + 1 >= numberOfPlayers) virtualView.setCurrentUser(0);   //check if the last one player of queue played
            else virtualView.setCurrentUser(currentUser+1);

            if(phase == 1 && currentUser + 1 == numberOfPlayers) virtualView.setPhase(phase); //if it's planning phase write at the end
            else if(phase == 0) virtualView.setPhase(phase);    //if it's action phase write every time

            roundController.notify();
        }
    }

    @Override
    public String getWinner() { return winner; }
    public void oneLastRide(){
        winner = gameManager.oneLastRide();
        clearFile();
    }

    public void saveGame(){ virtualView.saveVirtualView(); }
    public void clearFile(){ virtualView.clearFile(); }
    public void setPhase(String phase){
        if (phase.equals("ActionPhase")) jumpPhaseForRestore = true;
        else if(phase.equals("PlanningPhase")) jumpPhaseForRestore = false;
    }
    public void schoolRestore(int playerRef, int[] studentsEntrance, int[] studentsTable, int towers, boolean[] professors, String team){
        gameManager.schoolRestore(playerRef,studentsEntrance,studentsTable,towers,professors,team);
    }
    public void handAndCoinsRestore(int playerRef, ArrayList<String> cards, int coins){
        gameManager.handAndCoinsRestore(playerRef,cards,coins);
    }
    public void cloudRestore(int cloudRef, int[] students){
        gameManager.cloudRestore(cloudRef,students);
    }
    public void setIslandsSizeAfterRestore(int size){
        gameManager.setIslandsSizeAfterRestore(size);
    }
    public void islandRestore(int islandRef, int[] students, int towerValue, String towerTeam, int inhibited){
        gameManager.islandRestore(islandRef,students,towerValue,towerTeam,inhibited);
    }
    public void restoreMotherPose(int islandRef){
        gameManager.restoreMotherPose(islandRef);
    }
    public void bagRestore(List<Integer> bag){
        gameManager.bagRestore(bag);
    }
    public void queueRestore(ArrayList<Integer> playerRef, ArrayList<Integer> valueCard, ArrayList<Integer> maxMoveMotherNature){
        gameManager.queueRestore(playerRef,valueCard,maxMoveMotherNature);
    }
    @Override
    public int checkRestoreNickname(String nickname){ return virtualView.checkRestoreNickname(nickname); }

    @Override
    public boolean isExpertMode() { return expertMode; }
    public int getCurrentUser() { return currentUser; }
    public void setCurrentUser(int currentUser) { this.currentUser = currentUser; }
    public void incrCurrentUser(){ currentUser++; }
}