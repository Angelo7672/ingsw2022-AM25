package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;

import java.util.ArrayList;

public class Controller implements ServerController{
    private String[] chosenAssistants;
    private VirtualView virtualView;
    private GameManager gameManager;
    private boolean expertMode;
    private int numberOfPlayers;
    private int[] specials;


    public Controller(int numberOfPlayers, boolean isExpert){
        this.numberOfPlayers = numberOfPlayers;
        this.expertMode = isExpert;
        this.gameManager = new Game(isExpert, numberOfPlayers);
        this.virtualView = new VirtualView(numberOfPlayers, specials);

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


    //Planning Phase
    @Override
    public boolean refreshStudentsCloud(){ return gameManager.refreshStudentsCloud(); }
    @Override
    public void queueForPlanificationPhase(){ gameManager.queueForPlanificationPhase(); }
    @Override
    public int readQueue(int pos){ return gameManager.readQueue(pos); }
    @Override
    public ArrayList<String> getHand(int playerRef){
        //metodo di virtula view per avere la mano di carte
    }
    @Override
    public ArrayList<String> getPlayedCardsInThisTurn(){
        //metodo di virtual view per avere tutte le last card di questo turno
    }
    @Override
    public void playCard(int playerRef,int currentPlayer,String chosenAssistants){
        gameManager.playCard(playerRef, currentPlayer, chosenAssistants);
    }
    public String oneLastRide(){ return gameManager.oneLastRide(); }

    //Action Phase
    @Override
    public void inOrderForActionPhase(){ gameManager.inOrderForActionPhase(); }
    @Override
    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef){
        gameManager.moveStudent(playerRef,colour,inSchool,islandRef);
    }
    @Override
    public boolean moveMotherNature(int queueRef, int desiredMovement){
        gameManager.moveMotherNature(queueRef,desiredMovement);
    }
    @Override
    public void chooseCloud(int playerRef, int cloudRef){
        gameManager.chooseCloud(playerRef,cloudRef);
    }




}