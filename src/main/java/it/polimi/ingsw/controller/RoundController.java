package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.server.ControllerServer;

/**
 * RoundController manage the round subdivided in planningPhase and actionPhase.
 */
public class RoundController extends Thread{
    private final ControllerServer server;
    private final GameManager gameManager;
    private final int numberOfPlayers;
    private boolean end;
    private boolean jumpToActionPhase;
    private boolean restoreGame;
    private final Match controller;

    /**
     * Create a new RoundController ready to start.
     * @param controller controller reference;
     * @param gameManager gameManager reference;
     * @param server server reference;
     * @param numberOfPlayers in this match;
     * @param jumpToActionPhase indicates if, when restored, we have to jump to action phase;
     */
    public RoundController(Match controller, GameManager gameManager, ControllerServer server, int numberOfPlayers, boolean jumpToActionPhase){
        this.server = server;
        this.gameManager = gameManager;
        this.numberOfPlayers = numberOfPlayers;
        this.end = false;
        this.jumpToActionPhase = jumpToActionPhase;
        if(jumpToActionPhase) restoreGame = true;
        this.controller = controller;
    }

    /**
     * While there isn't a winner team, loop planning and action phase.
     * When game have to finish at the end of turn, check which team win this match and send it to server.
     */
    @Override
    public void run(){
        while (controller.getWinner().equals("NONE")) {
            if(!jumpToActionPhase) planningPhase(); //jumpToActionPhase is use when restore a game which is in action phase
            else jumpToActionPhase = false;
            actionPhase();
            if (end) {
                System.out.println("RoundController - EndGame");
                controller.oneLastRide();
                server.gameOver();
            }
        }
    }

    /**
     * Manage planning phase. Comunicate with server and wait user decision.
     */
    private synchronized void planningPhase(){
        gameManager.refreshStudentsCloud();
        gameManager.queueForPlanificationPhase();
        for(controller.setCurrentUser(0); controller.getCurrentUser() < numberOfPlayers; controller.incrCurrentUser()){
            server.unlockPlanningPhase(gameManager.readQueue(controller.getCurrentUser()));
            server.goPlayCard(gameManager.readQueue(controller.getCurrentUser()));
            try { this.wait();
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    /**
     * Manage action phase. Comunicate with server and wait user decision.
     * Save the game at the beginning and at the end of action phase of a player.
     */
    private synchronized void actionPhase(){
        try{
            if(!restoreGame) {
                gameManager.inOrderForActionPhase();
                controller.saveGame();
                for (controller.setCurrentUser(0); controller.getCurrentUser() < numberOfPlayers; controller.incrCurrentUser()) {
                    server.unlockActionPhase(gameManager.readQueue(controller.getCurrentUser()));
                    server.startActionPhase(gameManager.readQueue(controller.getCurrentUser()));
                    this.wait();
                    controller.saveGame();
                }
            }else {
                restoreGame = false;
                while(controller.getCurrentUser() < numberOfPlayers){
                    server.unlockActionPhase(gameManager.readQueue(controller.getCurrentUser()));
                    server.startActionPhase(gameManager.readQueue(controller.getCurrentUser()));
                    this.wait();
                    controller.saveGame();
                    controller.incrCurrentUser();
                }
            }
        } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void setEnd(boolean end) { this.end = end; }
}