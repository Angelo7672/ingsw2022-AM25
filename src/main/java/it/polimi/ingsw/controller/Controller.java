package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;

public class Controller {

    private RoundController roundController;
    private String[] chosenAssistants;
    private VirtualView virtualView;
    private GameManager gameManager;
    private int[] specials;


    public Controller(int numberOfPlayers, boolean isExpert, String[] playersInfo){
        roundController = new RoundController(isExpert, numberOfPlayers, playersInfo, this);
        virtualView = new VirtualView(numberOfPlayers , specials);
        gameManager = new Game(isExpert, numberOfPlayers, playersInfo);

        gameManager.setStudentsEntranceListener(virtualView);
        gameManager.setStudentsTableListener(virtualView);
        gameManager.setTowerSchoolListener(virtualView);
        gameManager.setProfessorsListener(virtualView);
        gameManager.setPlayedCardListener(virtualView);
        gameManager.setPlayedSpecialListener(virtualView);
        gameManager.setCoinsListener(virtualView);
        gameManager.setIslandSizeListener(virtualView);
        gameManager.setMotherPositionListener(virtualView);
        gameManager.setTowersIslandListener(virtualView);
        gameManager.setIslandSizeListener(virtualView);

    }




}