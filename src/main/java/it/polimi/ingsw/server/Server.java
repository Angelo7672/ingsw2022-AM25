package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameInfo;
import it.polimi.ingsw.controller.ServerController;

import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.model.exception.NotAllowedException;
import it.polimi.ingsw.server.expertmode.ExpertGame;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Server creates the server, comunicate with controller and send to it data received from clients. It also comunicate with the proxy and send to it data elaborated by controller.
 */
public class Server implements Entrance,ControllerServer{
    private ServerController controller;
    private final Exit proxy;
    private ExpertGame expertGame;
    private final String filename;

    /**
     * Create proxy and start it.
     * @param port choose for our server;
     */
    public Server(int port){
        this.filename = "saveGame.bin"; //file where we save our game for restore it in a second moment
        this.proxy = new Proxy_s(port,this);
        proxy.start();
    }

    /**
     * @return true if saveGame file is not empty.
     */
    @Override
    public boolean checkFile(){
        File file = new File(filename);
        return file.length() != 0;
    }

    /**
     * Restore GameInfo from last saved game.
     * @return a List of integer where the first item is the number of players in last saved game and the second item if it's 0 last saved game is in not exert mode, if it's 1
     * last saved game is in expert mode.
     */
    @Override
    public List<Integer> lastSavedGame(){
        List<Integer> lastPlayed = new ArrayList<>();
        GameInfo tmp;

        try{
            ObjectInputStream inputFile = new ObjectInputStream(new FileInputStream(filename));
            tmp = (GameInfo) inputFile.readObject();
            lastPlayed.add(tmp.getNumberOfPlayer());
            if(tmp.isExpertMode()) lastPlayed.add(1);
            else lastPlayed.add(0);
        } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

        return lastPlayed;
    }

    /**
     * Create a new controller.
     * @param numberOfPlayers number of players connected;
     * @param expertMode indicates the game mode we want;
     */
    @Override
    public void startController(int numberOfPlayers, boolean expertMode){ controller = new Controller(numberOfPlayers,expertMode,this, filename); }

    /**
     * @return the game mode of this match;
     */
    @Override
    public boolean isExpertMode(){ return controller.isExpertMode(); }

    /**
     * Create a new empty game.
     */
    @Override
    public void createGame(){ controller.createGame(); }

    /**
     * Initialize a new game.
     */
    @Override
    public void initializeGame(){ controller.initializeGame(); }

    /**
     * Restore VirtualView adding players of last saved game ready for check it when client reconnected.
     */
    @Override
    public void restoreVirtualView(){ controller.restoreVirtualView(); }

    /**
     * Restore last saved game.
     */
    @Override
    public void restoreGame(){ controller.restoreGame(); }

    /**
     * Start the game.
     */
    @Override
    public void startGame(){ controller.startGame(); }

    /**
     * Set Server ready for a game in expert mode.
     */
    @Override
    public void setExpertGame(){
        expertGame = new ExpertGame(this, controller.getExtractedSpecials());
        proxy.setExpertGame(expertGame);
    }
    @Override
    public ExpertGame getExpertGame(){ return expertGame; }
    @Override
    public int checkRestoreNickname(String nickname){ return controller.checkRestoreNickname(nickname); }

    @Override
    public ArrayList<String> alreadyChosenCharacters(){ return controller.alreadyChosenCharacters(); }
    @Override
    public int userLogin(String nickname, String character){
        String[] characters = new String[]{"WIZARD","KING","WITCH","SAMURAI"};
        boolean checker = false;

        if(!controller.userLoginNickname(nickname)) return -1;
        for(int i = 0; i < 4 && !checker; i++)
            if(character.equalsIgnoreCase(characters[i])) checker = true;
        if (!checker || !controller.userLoginCharacter(character)) return -1;

        return controller.addNewPlayer(nickname,character);
    }

    //Planning Phase
    @Override
    public void goPlayCard(int playerRef){ proxy.goPlayCard(playerRef); }
    @Override
    public void unlockPlanningPhase(int playerRef){ proxy.unlockPlanningPhase(playerRef); }
    @Override
    public boolean userPlayCard(int playerRef, String assistant){
        try { controller.playCard(playerRef,assistant);
        }catch (NotAllowedException exception){ return false;}

        return true;
    }

    //Action Phase
    @Override
    public void startActionPhase(int playerRef){ proxy.startActionPhase(playerRef); }
    @Override
    public void unlockActionPhase(int playerRef){ proxy.unlockActionPhase(playerRef); }
    @Override
    public void sendMaxMovementMotherNature(int playerRef, int maxMovement){ proxy.sendMaxMovementMotherNature(playerRef,maxMovement); }
    @Override
    public boolean userMoveStudent(int playerRef, int colour, boolean inSchool, int islandRef){
        try { controller.moveStudent(playerRef, colour, inSchool, islandRef);
        }catch (NotAllowedException exception){ return false; }

        return true;
    }
    @Override
    public boolean userMoveMotherNature(int desiredMovement) throws EndGameException {
        try { controller.moveMotherNature(desiredMovement);
        } catch (NotAllowedException exception) { return false;
        } catch (EndGameException endGameException){ throw new EndGameException(); }

        return true;
    }
    @Override
    public boolean userChooseCloud(int playerRef, int cloudRef){
        try { controller.chooseCloud(playerRef,cloudRef);
        }catch (NotAllowedException exception){ return false; }

        return true;
    }
    @Override
    public boolean useSpecialLite(int indexSpecial, int playerRef){
        return controller.useSpecialLite(indexSpecial, playerRef);
    }
    @Override
    public boolean useSpecialSimple(int indexSpecial, int playerRef, int ref){
        return controller.useSpecialSimple(indexSpecial,playerRef,ref);
    }
    @Override
    public boolean useSpecialMedium(int indexSpecial, int playerRef, int ref, int color){
        return controller.useSpecialMedium(indexSpecial,playerRef,ref,color);
    }
    @Override
    public boolean useSpecialHard(int specialIndex, int playerRef, ArrayList<Integer> color1, ArrayList<Integer> color2){
        return controller.useSpecialHard(specialIndex,playerRef,color1,color2);
    }

    @Override
    public void resumeTurn(int phase){ controller.resumeTurn(phase); }

    @Override
    public String endGame(){ return controller.getWinner(); }
    @Override
    public void gameOver(){
        proxy.gameOver();
        System.out.println("GAME OVER!");
        System.out.println(controller.getWinner() + " WIN!");
        System.exit(0);
    }

    @Override
    public void sendGameInfo(int numberOfPlayers, boolean expertMode){ proxy.sendGameInfo(numberOfPlayers, expertMode); }
    @Override
    public void sendUserInfo(int playerRef, String nickname, String character){ proxy.sendUserInfo(playerRef, nickname, character);}
    @Override
    public void studentsChangeInSchool(int color, String place, int componentRef, int newStudentsValue){ proxy.studentsChangeInSchool(color, place, componentRef, newStudentsValue); }
    @Override
    public void studentChangeOnIsland(int islandRef, int color, int newStudentsValue){ proxy.studentChangeOnIsland(islandRef, color, newStudentsValue); }
    @Override
    public void studentChangeOnCloud(int cloudRef, int color, int newStudentsValue){ proxy.studentChangeOnCloud(cloudRef, color, newStudentsValue); }
    @Override
    public void professorChangePropriety(int playerRef, int color, boolean newProfessorValue){ proxy.professorChangePropriety(playerRef, color, newProfessorValue);}
    @Override
    public void motherChangePosition(int newMotherPosition){ proxy.motherChangePosition(newMotherPosition); }
    @Override
    public void lastCardPlayedFromAPlayer(int playerRef, String assistantCard){ proxy.lastCardPlayedFromAPlayer(playerRef, assistantCard); }
    @Override
    public void numberOfCoinsChangeForAPlayer(int playerRef, int newCoinsValue){ proxy.numberOfCoinsChangeForAPlayer(playerRef, newCoinsValue); }
    @Override
    public void dimensionOfAnIslandIsChange(int islandToDelete){ proxy.dimensionOfAnIslandIsChange(islandToDelete); }
    @Override
    public void towersChangeInSchool(int playerRef, int towersNumber){ proxy.towersChangeInSchool(playerRef, towersNumber); }
    @Override
    public void towersChangeOnIsland(int islandRef, int towersNumber){ proxy.towersChangeOnIsland(islandRef, towersNumber); }
    @Override
    public void towerChangeColorOnIsland(int islandRef, int newColor){ proxy.towerChangeColorOnIsland(islandRef, newColor); }
    @Override
    public void islandInhibited(int islandRef, int isInhibited){ proxy.islandInhibited(islandRef, isInhibited); }
    @Override
    public void setSpecial(int specialRef, int cost){ proxy.setSpecial(specialRef, cost); }
    @Override
    public void sendUsedSpecial(int playerRef, int indexSpecial){ proxy.sendUsedSpecial(playerRef, indexSpecial); }
    @Override
    public void sendHandAfterRestore(int playerRef, ArrayList<String> hand){ proxy.sendHandAfterRestore(playerRef, hand); }
    @Override
    public void sendInfoSpecial1or7or11(int specialIndex, int studentColor, int value){ proxy.sendInfoSpecial1or7or11(specialIndex, studentColor, value); }
    @Override
    public void sendInfoSpecial5(int cards){ proxy.sendInfoSpecial5(cards); }

    @Override
    public void exitError(){ System.exit(-1); }

    public static void main(String[] args) {
        System.out.println("Eriantys Server | Welcome!");
        //todo: chiedere all'utente di inserire la porta
        new Server(2525);
    }
}