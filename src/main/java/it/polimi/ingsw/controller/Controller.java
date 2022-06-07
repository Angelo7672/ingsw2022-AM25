package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.exception.NotAllowedException;
import it.polimi.ingsw.server.ControllerServer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Controller implements ServerController{
    private int currentUser;
    private ControllerServer server;
    private VirtualView virtualView;
    private GameManager gameManager;
    private RoundController roundController;
    private int numberOfPlayers;
    private boolean expertMode;
    private String winner;
    private String fileName;

    public Controller(int numberOfPlayers, boolean isExpert, ControllerServer server){
        this.expertMode = isExpert;
        this.numberOfPlayers = numberOfPlayers;
        this.server = server;
        this.fileName = "saveGame.bin";
        this.virtualView = new VirtualView(numberOfPlayers, server, fileName);
        this.winner = "NONE";
    }

    @Override
    public void startGame(){
        server.sendGameInfo(numberOfPlayers, expertMode);   //at every client
        for(int i = 0; i < numberOfPlayers; i++)
            server.sendUserInfo(i, virtualView.getNickname(i), virtualView.getCharacter(i));
        this.gameManager = new Game(expertMode, numberOfPlayers);
        gameManager.setStudentsListener(virtualView);
        gameManager.setTowerListener(virtualView);
        gameManager.setProfessorsListener(virtualView);
        gameManager.setPlayedCardListener(virtualView);
        gameManager.setSpecialListener(virtualView);
        gameManager.setCoinsListener(virtualView);
        gameManager.setMotherPositionListener(virtualView);
        gameManager.setIslandListener(virtualView);
        gameManager.setInhibitedListener(virtualView);
        gameManager.setBagListener(virtualView);
        gameManager.setQueueListener(virtualView);
        gameManager.initializeGame();
        this.roundController = new RoundController(this,this.gameManager,server,numberOfPlayers);
        roundController.start();
    }

    @Override
    public ArrayList<String> alreadyChosenCharacters(){ return virtualView.getAlreadyChosenCharacters(); }
    @Override
    public boolean userLoginNickname(String nickname){ return virtualView.checkNewNickname(nickname); }
    @Override
    public boolean userLoginCharacter(String character){ return virtualView.checkNewCharacter(character); }
    @Override
    public void addNewPlayer(String nickname, String character){ virtualView.addNewPlayer(nickname,character); }

    //Planning Phase
    public String getLastPlayedCard(int playerRef){ return virtualView.getLastPlayedCard(playerRef); }
    @Override
    public void playCard(int playerRef, String chosenAssistants) throws NotAllowedException {
        ArrayList<String> alreadyPlayedCard = new ArrayList<>();

        for(int i = 0; i < currentUser; i++)
            alreadyPlayedCard.add(getLastPlayedCard(gameManager.readQueue(i)));

        try { roundController.setEnd(gameManager.playCard(playerRef,currentUser,chosenAssistants,alreadyPlayedCard));
        }catch (NotAllowedException exception){ throw new NotAllowedException(); }
    }

    //Action Phase
    @Override
    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) throws NotAllowedException {
        try { gameManager.moveStudent(playerRef, colour, inSchool, islandRef);
        }catch (NotAllowedException exception){ throw new NotAllowedException(); }
    }
    @Override
    public void moveMotherNature(int desiredMovement) throws NotAllowedException,EndGameException {
        try {
            if(gameManager.moveMotherNature(currentUser,desiredMovement)) {
                oneLastRide();
                throw new EndGameException();
            }
        }catch (NotAllowedException exception){ throw new NotAllowedException(); }
    }
    @Override
    public void chooseCloud(int playerRef, int cloudRef) throws NotAllowedException {
        try { gameManager.chooseCloud(playerRef,cloudRef);
        }catch (NotAllowedException exception){ throw new NotAllowedException(); }
    }
    @Override
    public boolean useSpecialSimple(int indexSpecial, int playerRef, int ref){
        return gameManager.useSpecialSimple(indexSpecial,playerRef,ref);
    }
    @Override
    public boolean useSpecialMedium(int indexSpecial, int playerRef, int ref, int color){
        return gameManager.useSpecialMedium(indexSpecial,playerRef,ref,color);
    }
    @Override
    public boolean useSpecialHard(int specialIndex, int playerRef, int ref, ArrayList<Integer> color1, ArrayList<Integer> color2){
        return gameManager.useSpecialHard(specialIndex,playerRef,ref,color1,color2);
    }

    @Override
    public void resumeTurn(){ synchronized(roundController) { roundController.notify();} }

    @Override
    public String getWinner() { return winner; }
    public void oneLastRide(){ winner = gameManager.oneLastRide(); }

    public void saveGame(){ virtualView.saveVirtualView(); }
    public void clearFile(){ virtualView.clearFile(); }
    public void restoreVirtualView(){
    /*
        try{
            ObjectInputStream inputFile = new ObjectInputStream(new FileInputStream(fileName));
            this.schoolBoards = (ArrayList<SchoolBoard>) inputFile.readObject();
            this.islands= (ArrayList<Island>) inputFile.readObject();
            this.clouds=(ArrayList<Cloud>) inputFile.readObject();
            this.hands=(ArrayList<Hand>) inputFile.readObject();
            this.specials=(ArrayList<Integer>) inputFile.readObject();
            this.bag=(ArrayList<Integer>) inputFile.readObject();
            this.playedCards=(ArrayList<String>) inputFile.readObject();
            this.queue=(ArrayList<Integer>) inputFile.readObject();
            inputFile.close();
        } catch (FileNotFoundException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace();
        } catch (ClassNotFoundException e) { e.printStackTrace(); }
     */
    }

    @Override
    public boolean isExpertMode() { return expertMode; }
    public int getCurrentUser() { return currentUser; }
    public void setCurrentUser(int currentUser) { this.currentUser = currentUser; }
    public void incrCurrentUser(){ currentUser++; }
}