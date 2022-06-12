package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.server.ControllerServer;

public class RoundController extends Thread{
    private ControllerServer server;
    private GameManager gameManager;
    private int numberOfPlayers;
    private boolean end;
    private boolean jumpToActionPhase;
    private boolean restoreGame;
    private Controller controller;

    public RoundController(Controller controller,GameManager gameManager,ControllerServer server,int numberOfPlayers, boolean jumpToActionPhase){
        this.server = server;
        this.gameManager = gameManager;
        this.numberOfPlayers = numberOfPlayers;
        this.end = false;
        this.jumpToActionPhase = jumpToActionPhase;
        if(jumpToActionPhase) restoreGame = true;
        this.controller = controller;
    }

    @Override
    public void run(){
        while (controller.getWinner().equals("NONE")) {
            if(!jumpToActionPhase) planningPhase(); //jumpToActionPhase is use when restore a game which is in action phase
            else if(jumpToActionPhase) jumpToActionPhase = false;
            actionPhase();
            if (end) {
                controller.oneLastRide();
                server.gameOver();
            }
        }
    }

    private synchronized void planningPhase(){
        end = gameManager.refreshStudentsCloud();
        gameManager.queueForPlanificationPhase();
        for(controller.setCurrentUser(0); controller.getCurrentUser() < numberOfPlayers; controller.incrCurrentUser()){
            server.unlockPlanningPhase(gameManager.readQueue(controller.getCurrentUser()));
            server.goPlayCard(gameManager.readQueue(controller.getCurrentUser()));
            try { this.wait();
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
        controller.saveGame();
    }

    private synchronized void actionPhase(){
        if(!restoreGame) {
            gameManager.inOrderForActionPhase();
            for (controller.setCurrentUser(0); controller.getCurrentUser() < numberOfPlayers; controller.incrCurrentUser()) {
                server.unlockActionPhase(gameManager.readQueue(controller.getCurrentUser()));
                server.startActionPhase(gameManager.readQueue(controller.getCurrentUser()));
                System.out.println("start action "+gameManager.readQueue(controller.getCurrentUser()));
                try { this.wait();
                } catch (InterruptedException e) { e.printStackTrace(); }
                controller.saveGame();
            }
        }else if(restoreGame){
            restoreGame = false;
            for (controller.getCurrentUser(); controller.getCurrentUser() < numberOfPlayers; controller.incrCurrentUser()){
                server.unlockActionPhase(gameManager.readQueue(controller.getCurrentUser()));
                server.startActionPhase(gameManager.readQueue(controller.getCurrentUser()));
                try { this.wait();
                } catch (InterruptedException e) { e.printStackTrace(); }
                controller.saveGame();
            }
        }
    }

    public void setEnd(boolean end) { this.end = end; }
}
