package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.exception.NotAllowedException;
import it.polimi.ingsw.server.ControllerServer;

import java.io.*;

public class Controller implements ServerController{
    private int currentUser;
    private VirtualView virtualView;
    private GameManager gameManager;
    private RoundController roundController;
    private boolean expertMode;
    private String winner;
    private String fileName;


    public Controller(int numberOfPlayers, boolean isExpert, ControllerServer server){
        this.expertMode = isExpert;
        this.gameManager = new Game(isExpert, numberOfPlayers);
        this.virtualView = new VirtualView(numberOfPlayers);
        this.roundController = new RoundController(this,this.gameManager,server,numberOfPlayers);
        this.winner = "NONE";
        this.fileName = "";
        roundController.start();    //non va qua!

        gameManager.setStudentsListener(virtualView);
        gameManager.setTowerListener(virtualView);
        gameManager.setProfessorsListener(virtualView);
        gameManager.setPlayedCardListener(virtualView);
        gameManager.setSpecialListener(virtualView);
        gameManager.setCoinsListener(virtualView);
        gameManager.setMotherPositionListener(virtualView);
        gameManager.setIslandListener(virtualView);
        gameManager.setInhibitedListener(virtualView);
    }

    @Override
    public boolean userLoginNickname(String nickname, int playerRef){ return nickname.equals(virtualView.getNickname(playerRef)); }
    @Override
    public boolean userLoginCharacter(String character, int playerRef){ return character.equals(virtualView.getCharacter(playerRef)); }
    @Override
    public void addNewPlayer(String nickname, String character){ virtualView.addNewPlayer(nickname,character); }

    //Planning Phase
    public String getLastPlayedCard(int playerRef){ return virtualView.getLastPlayedCard(playerRef); }

    @Override
    public void playCard(int playerRef, String chosenAssistants) throws NotAllowedException {
        try {
            roundController.setEnd(gameManager.playCard(playerRef,currentUser,chosenAssistants));
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
        try {
            gameManager.chooseCloud(playerRef,cloudRef);
        }catch (NotAllowedException exception){ throw new NotAllowedException(); }
    }

    public void oneLastRide(){ winner = gameManager.oneLastRide(); }

    @Override
    public void resumeTurn(){ roundController.notify(); }

    public void saveVirtualView(){
        try{
            //File gameStateFile = new File(fileName);
            //gameStateFile.createNewFile();

            ObjectOutputStream outputFile = new ObjectOutputStream(new FileOutputStream(fileName, false));
            outputFile.writeObject(this.virtualView);
            outputFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void restoreVirtualView(){
        try{
            ObjectInputStream inputFile = new ObjectInputStream(new FileInputStream(fileName));
            this.virtualView = (VirtualView) inputFile.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }   //da chiedere

    public void clearFile(String fileName) {
        try{
            File file = new File(fileName);
            if (file.exists()) {
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                raf.setLength(0);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public int getCurrentUser() { return currentUser; }
    public void setCurrentUser(int currentUser) { this.currentUser = currentUser; }
    public void incrCurrentUser(){ currentUser++; }
    public String getWinner() { return winner; }
}