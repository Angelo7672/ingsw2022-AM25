package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.exception.NotAllowedException;
import it.polimi.ingsw.server.ControllerServer;

import java.io.*;

public class Controller implements ServerController{
    private String[] chosenAssistants;
    private int currentUser;
    private VirtualView virtualView;
    private GameManager gameManager;
    private RoundController roundController;
    private boolean expertMode;
    private boolean end;
    private String winner;
    private int[] specials;


    public Controller(int numberOfPlayers, boolean isExpert, ControllerServer server){
        this.expertMode = isExpert;
        this.gameManager = new Game(isExpert, numberOfPlayers);
        this.virtualView = new VirtualView(numberOfPlayers, specials);
        this.roundController = new RoundController(this,this.gameManager,server,numberOfPlayers);
        roundController.start();
        this.end = false;
        this.winner = "NONE";

        gameManager.setStudentsListener(virtualView);
        gameManager.setTowerListener(virtualView);
        gameManager.setProfessorsListener(virtualView);
        gameManager.setPlayedCardListener(virtualView);
        gameManager.setSpecialListener(virtualView);
        gameManager.setCoinsListener(virtualView);
        gameManager.setMotherPositionListener(virtualView);
        gameManager.setIslandSizeListener(virtualView);
        gameManager.setInhibitedListener(virtualView);
    }

    public void eryantis(){
        while (winner.equals("NONE")) {
            roundController.planningPhase();
            roundController.actionPhase();
            if (end) {
                winner = oneLastRide();
                roundController.gameOver();
            }
        }
    }

    //Planning Phase
    @Override
    public void playCard(int playerRef, String chosenAssistants) throws NotAllowedException {
        try {
            end = gameManager.playCard(playerRef,currentUser,chosenAssistants);
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
                winner = oneLastRide();
                throw new EndGameException();
            }
        }catch (NotAllowedException exception){ throw new NotAllowedException(); }
    }
    @Override
    public void chooseCloud(int playerRef, int cloudRef) throws NotAllowedException {
        try {
            gameManager.chooseCloud(playerRef,cloudRef);
        }catch (NotAllowedException exception){ throw new NotAllowedException(); }
    }

    public String oneLastRide(){ return gameManager.oneLastRide(); }
    public void saveVirtualView(String fileName){
        try{
            //File gameStateFile = new File(fileName);
            //gameStateFile.createNewFile();

            ObjectOutputStream outputFile= new ObjectOutputStream(new FileOutputStream(fileName, false));
            outputFile.writeObject(this.virtualView);
            outputFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void restoreVirtualView(String fileName){
        try{
            ObjectInputStream inputFile = new ObjectInputStream(new FileInputStream(fileName));
            this.virtualView = (VirtualView) inputFile.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public int getCurrentUser() { return currentUser; }
    public void setCurrentUser(int currentUser) { this.currentUser = currentUser; }
    public void incrCurrentUser(){ currentUser++; }
    public void setEnd(boolean end) { this.end = end; }
}