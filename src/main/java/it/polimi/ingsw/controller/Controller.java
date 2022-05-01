package it.polimi.ingsw.controller;


public class Controller {

    private RoundController roundController;
    private VirtualView virtualView;
    private String chosenAssistant;


    public Controller(VirtualView virtualView, int numberOfPlayers, boolean isExpert, String[] playersInfo){ //virtual View is istantiated by the Server class
        roundController = new RoundController(isExpert, numberOfPlayers, playersInfo, this);
        this.virtualView = virtualView;


    }

}