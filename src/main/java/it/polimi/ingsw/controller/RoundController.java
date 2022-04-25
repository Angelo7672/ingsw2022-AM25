package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;

import java.util.ArrayList;

public class RoundController {
    //gestisce lo svolgimento della partita per tutti i giocatori

    private GameManager gameManager;
    private int numberOfPlayers;
    private int currentPlayer;
    private Controller controller;
    private GameBoard gameBoard;
    private ArrayList<String> assistantCards;


    public RoundController(boolean expertMode, int numberOfPlayers, String[] playersInfo, Controller controller) {
        this.gameManager = new Game(expertMode, numberOfPlayers, playersInfo);
        this.controller = controller;
        this.numberOfPlayers = numberOfPlayers;
        this.assistantCards = new ArrayList<>();
    }

    public void startPlanningPhase(){
        if(gameManager.refreshStudentsCloud() == false) {
            gameManager.queueForPlanificationPhase();
            for (int i = 0; i < numberOfPlayers; i++) {
                int currentPlayer = gameManager.readQueue(i);
                gameManager.playCard(i, currentPlayer, controller.getChosenAssistant());
                assistantCards.add(controller.getChosenAssistant());
            }
            gameManager.inOrderForActionPhase();
        }
        else if(gameManager.refreshStudentsCloud()== true)
            gameManager.oneLastRide();
    }

    public void startActionPhase(){





        }

    public void endGame(){

    }


}




