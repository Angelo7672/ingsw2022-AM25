package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;

public class Controller {

    private RoundController roundController;
    private String[] chosenAssistants;
    private VirtualView virtualView;
    private GameManager gameManager;


    public Controller(int numberOfPlayers, boolean isExpert, String[] playersInfo){
        roundController = new RoundController(isExpert, numberOfPlayers, playersInfo, this);
        virtualView = new VirtualView(numberOfPlayers);
        gameManager = new Game(isExpert, numberOfPlayers, playersInfo);
        gameManager.addModelListener(virtualView);

    }




}