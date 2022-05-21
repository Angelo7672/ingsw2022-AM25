package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.server.ControllerServer;

public class RoundController extends Thread{
    private ControllerServer server;
    private GameManager gameManager;
    private int numberOfPlayers;
    private boolean finish;
    private Controller controller;

    public RoundController(Controller controller,GameManager gameManager,ControllerServer server,int numberOfPlayers){
        this.server = server;
        this.gameManager = gameManager;
        this.numberOfPlayers = numberOfPlayers;
        this.finish = false;
        this.controller = controller;
    }

    @Override
    public void run(){
        while(!finish){}
    }

    public synchronized void planningPhase(){
        controller.setEnd(gameManager.refreshStudentsCloud());
        gameManager.queueForPlanificationPhase();
        for(controller.setCurrentUser(0); controller.getCurrentUser() < numberOfPlayers; controller.incrCurrentUser()){
            server.goPlayCard(gameManager.readQueue(controller.getCurrentUser()));
            server.unlockPlanningPhase(controller.getCurrentUser());
            try { this.wait();
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public synchronized void actionPhase(){
        gameManager.inOrderForActionPhase();
        for(controller.setCurrentUser(0); controller.getCurrentUser() < numberOfPlayers; controller.incrCurrentUser()){
            server.startActionPhase(gameManager.readQueue(controller.getCurrentUser()));
            server.unlockActionPhase(controller.getCurrentUser());
            try { this.wait();
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public void gameOver() { finish = true; }
}
