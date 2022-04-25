package it.polimi.ingsw.controller;

import java.beans.PropertyChangeSupport;

//fotografia della plancia di gioco
public class GameBoard {

    private PropertyChangeSupport gameListeners = new PropertyChangeSupport(this);

    /*
    private int numberOfPlayers;
    private String playerInfo;
    public ArrayList<String> assistantCards;

    public GameBoard(int numberOfPlayers){
        this.numberOfPlayers = numberOfPlayers;
        assistantCards = new ArrayList<>();
    }

    /*
    public GameBoard(int numberOfPlayers, ) {
        playersInfo = new ArrayList<>();
        this.numberOfPlayers = numberOfPlayers;
        addNewPlayer();
    }

    public void addNewPlayer(String nickname, String chosenWizard ){
        if(playersInfo.size()<numberOfPlayers)
            playersInfo.add(nickname);
            playersInfo.add(chosenWizard);
    }
    */

}
