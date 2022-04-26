package it.polimi.ingsw.controller;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

//GameBoard keeps track of the current state of the game
public class GameBoard {

    private PropertyChangeSupport gameListeners = new PropertyChangeSupport(this);


    private int numberOfPlayers;
    private String[] playerInfo;
    public ArrayList<String> assistantCards;


    public GameBoard(int numberOfPlayers){
        this.numberOfPlayers = numberOfPlayers;
        assistantCards = new ArrayList<>();
        playerInfo = new String[numberOfPlayers];
    }



}
