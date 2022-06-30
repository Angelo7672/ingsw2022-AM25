package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.exception.NotAllowedException;
import it.polimi.ingsw.server.ControllerServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class controls model and communicates with server.
 * It creates game and initialize it or restore it from a previous match.
 */
public class Controller implements ServerController, Match, Restore{
    private int currentUser;
    private final ControllerServer server;
    private final VirtualView virtualView;
    private GameManager gameManager;
    private RoundController roundController;
    private boolean jumpPhaseForRestore;
    private final int numberOfPlayers;
    private final boolean expertMode;
    private String winner;

    /**
     * Create controller ready for manage a new (or restored) game.
     * @param numberOfPlayers of this match;
     * @param isExpert indicate our game mode;
     * @param server reference with server;
     * @param fileName where save game;
     */
    public Controller(int numberOfPlayers, boolean isExpert, ControllerServer server, String fileName){
        this.expertMode = isExpert;
        this.numberOfPlayers = numberOfPlayers;
        this.jumpPhaseForRestore = false;
        this.server = server;
        this.virtualView = new VirtualView(numberOfPlayers, isExpert, server, this, fileName);
        this.winner = "NONE";
    }

    /**
     * @see VirtualView
     */
    @Override
    public ArrayList<String> alreadyChosenCharacters(){ return virtualView.getAlreadyChosenCharacters(); }

    /**
     * @see VirtualView
     */
    @Override
    public boolean userLoginNickname(String nickname){ return virtualView.checkNewNickname(nickname); }

    /**
     * @see VirtualView
     */
    @Override
    public boolean userLoginCharacter(String character){ return virtualView.checkNewCharacter(character); }

    /**
     * @see VirtualView
     */
    @Override
    public int addNewPlayer(String nickname, String character){ return virtualView.addNewPlayer(nickname,character); }

    /**
     * Create a new empty game.
     * @param restore indicates if there is a game to restore.
     */
    @Override
    public void createGame(boolean restore){
        gameManager = new Game(expertMode, numberOfPlayers);

        if(expertMode && !restore){ //if restore is true, don't create specials because restore special do it
            gameManager.createSpecial();
            gameManager.setSpecialStudentsListener(virtualView);
            gameManager.setNoEntryListener(virtualView);
        }
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
    }

    /**
     * Initialize a new game, then get info about it and players and send it to the server.
     */
    @Override
    public void initializeGame(){
        server.sendGameInfo(numberOfPlayers, expertMode);   //at every client
        for(int i = 0; i < numberOfPlayers; i++)
            server.sendUserInfo(i, virtualView.getNickname(i), virtualView.getCharacter(i));    //send info about player

        gameManager.initializeGame();
        if(expertMode) server.setExpertGame();
    }

    /**
     * @see VirtualView
     */
    @Override
    public void restoreVirtualView(){ virtualView.restoreVirtualView(); }

    /**
     * Restore last saved game, then get info about it and players and send it to the server.
     */
    @Override
    public void restoreGame(){  //restore game
        server.sendGameInfo(numberOfPlayers, expertMode);   //at every client
        for(int i = 0; i < numberOfPlayers; i++)
            server.sendUserInfo(i, virtualView.getNickname(i), virtualView.getCharacter(i));    //send info about player
        virtualView.restoreGame(expertMode);
        if(expertMode) server.setExpertGame();
    }

    /**
     * @see VirtualView
     */
    @Override
    public int checkRestoreNickname(String nickname){ return virtualView.checkRestoreNickname(nickname); }

    /**
     * Create RoundController and start (or resume) game.
     */
    @Override
    public void startGame(){
        this.roundController = new RoundController(this,this.gameManager,server,numberOfPlayers,jumpPhaseForRestore);
        roundController.start();
        System.out.println("GAME ON!");
    }

    /**
     * @see Game
     */
    @Override
    public ArrayList<Integer> getExtractedSpecials(){ return gameManager.getExtractedSpecials(); }

    //Planning Phase
    /**
     * @see VirtualView
     */
    private String getLastPlayedCard(int playerRef){ return virtualView.getLastPlayedCard(playerRef); }

    /**
     * Method to play a card: it collects in an ArrayList already played cards in this turn and send to model it with the card chosen.
     * It also set, if player finished his cards, the game will end at the end of the turn.
     * @param playerRef reference to the player which play the card;
     * @param chosenAssistants the card played;
     * @throws NotAllowedException if player doesn't have that card in his hand or someone just played this card in this turn;
     */
    @Override
    public void playCard(int playerRef, String chosenAssistants) throws NotAllowedException {
        ArrayList<String> alreadyPlayedCard = new ArrayList<>();

        for(int i = 0; i < currentUser; i++)
            alreadyPlayedCard.add(getLastPlayedCard(gameManager.readQueue(i)));

        setEndCard(gameManager.playCard(playerRef, currentUser, chosenAssistants, alreadyPlayedCard));
    }

    //Action Phase
    /**
     * @see Game
     */
    @Override
    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) throws NotAllowedException {
        gameManager.moveStudent(playerRef, colour, inSchool, islandRef);
    }

    /**
     * @see Game
     * @throws EndGameException if remain only 3 or less block of islands; we have the team winner with oneLastRide();
     */
    @Override
    public void moveMotherNature(int desiredMovement) throws NotAllowedException,EndGameException {
        if(gameManager.moveMotherNature(currentUser, desiredMovement)) {
            oneLastRide();
            throw new EndGameException();
        }
    }

    /**
     * @see Game
     */
    @Override
    public void chooseCloud(int playerRef, int cloudRef) throws NotAllowedException { gameManager.chooseCloud(playerRef,cloudRef); }

    /**
     * @see Game
     */
    @Override
    public boolean useStrategySimple(int indexSpecial, int playerRef, int ref){ return gameManager.useStrategySimple(indexSpecial,playerRef,ref); }

    /**
     * @see Game
     */
    @Override
    public boolean useSpecial3(int playerRef, int islandRef) throws EndGameException{ return gameManager.useSpecial3(playerRef,islandRef); }
    /**
     * @see Game
     */
    @Override
    public boolean useSpecialLite(int indexSpecial, int playerRef){ return gameManager.useSpecialLite(indexSpecial, playerRef); }

    /**
     * @see Game
     */
    @Override
    public boolean useSpecialSimple(int indexSpecial, int playerRef, int ref){ return gameManager.useSpecialSimple(indexSpecial,playerRef,ref); }

    /**
     * @see Game
     */
    @Override
    public boolean useSpecialMedium(int indexSpecial, int playerRef, int ref, int color){ return gameManager.useSpecialMedium(indexSpecial,playerRef,ref,color); }

    /**
     * @see Game
     */
    @Override
    public boolean useSpecialHard(int specialIndex, int playerRef, ArrayList<Integer> color1, ArrayList<Integer> color2){
        return gameManager.useSpecialHard(specialIndex,playerRef,color1,color2);
    }

    /**
     * Notify to the roundController that the turn of current user is over. It also set phase of game in virtualView.
     * @param phase: 0 is planningPhase, 1 is actionPhase;
     */
    @Override
    public void resumeTurn(int phase){
        if(currentUser + 1 >= numberOfPlayers) virtualView.setCurrentUser(0);   //check if the last one player of queue played
        else virtualView.setCurrentUser(currentUser+1);

        if(phase == 1 && currentUser + 1 == numberOfPlayers) virtualView.setPhase(1); //if it's planning phase write at the end
        else if(phase == 0 && currentUser + 1 == numberOfPlayers) virtualView.setPhase(0);  //if action phase is over, set planning phase
        else if(phase == 0 && currentUser +1 != numberOfPlayers) virtualView.setPhase(1);   //if action phase isn't over, set action phase

        synchronized (roundController){ roundController.notify(); }
    }

    /**
     * @see RoundController
     */
    private void setEndCard(boolean end){ roundController.setEndCard(end); }

    /**
     * @see RoundController
     */
    @Override
    public void setEndBag(boolean end){ roundController.setEndBag(end); }
    @Override
    public String getWinner() { return winner; }

    /**
     * Set the team winner and clear file save.
     * @see Game
     */
    @Override
    public void oneLastRide(){
        winner = gameManager.oneLastRide();
        virtualView.clearFile();
    }

    /**
     * @see VirtualView
     */
    @Override
    public void saveGame(){ virtualView.saveVirtualView(expertMode); }

    /**
     * Set the current turn phase after restore.
     * @param phase current phase;
     */
    @Override
    public void setJumpPhaseForRestore(String phase){
        if (phase.equals("ActionPhase")){ jumpPhaseForRestore = true; }
        else if(phase.equals("PlanningPhase")) jumpPhaseForRestore = false;
    }

    /**
     * @see Game
     */
    @Override
    public void schoolRestore(int playerRef, int[] studentsEntrance, int[] studentsTable, int towers, boolean[] professors, String team){
        gameManager.schoolRestore(playerRef,studentsEntrance,studentsTable,towers,professors,team);
    }

    /**
     * @see Game
     */
    @Override
    public void handAndCoinsRestore(int playerRef, ArrayList<String> cards, int coins){
        gameManager.handAndCoinsRestore(playerRef,cards,coins);
    }

    /**
     * @see Game
     */
    @Override
    public void cloudRestore(int cloudRef, int[] students){ gameManager.cloudRestore(cloudRef,students); }

    /**
     * @see Game
     */
    @Override
    public void setIslandsSizeAfterRestore(int size){ gameManager.setIslandsSizeAfterRestore(size); }

    /**
     * @see Game
     */
    @Override
    public void islandRestore(int islandRef, int[] students, int towerValue, String towerTeam, int inhibited){
        gameManager.islandRestore(islandRef,students,towerValue,towerTeam,inhibited);
    }

    /**
     * @see Game
     */
    @Override
    public void restoreMotherPose(int islandRef){ gameManager.restoreMotherPose(islandRef); }

    /**
     * @see Game
     */
    @Override
    public void bagRestore(List<Integer> bag){ gameManager.bagRestore(bag); }

    /**
     * @see Game
     */
    @Override
    public void queueRestore(ArrayList<Integer> playerRef, ArrayList<Integer> valueCard, ArrayList<Integer> maxMoveMotherNature){
        gameManager.queueRestore(playerRef,valueCard,maxMoveMotherNature);
    }

    /**
     * @see Game
     */
    @Override
    public void specialRestore(int specialIndex, int cost){ gameManager.specialRestore(specialIndex, cost); }

    /**
     * @see Game
     */
    @Override
    public void specialListRestore(){
        gameManager.setSpecialStudentsListener(virtualView);
        gameManager.setNoEntryListener(virtualView);
        gameManager.specialListRestore();
    }

    /**
     * @see Game
     */
    @Override
    public void specialStudentRestore(int specialIndex, int[] students){ gameManager.specialStudentRestore(specialIndex, students); }

    /**
     * @see Game
     */
    @Override
    public void noEntryCardsRestore(int numCards){ gameManager.noEntryCardsRestore(numCards); }

    @Override
    public boolean isExpertMode() { return expertMode; }
    @Override
    public int getCurrentUser() { return currentUser; }
    @Override
    public void setCurrentUser(int currentUser) { this.currentUser = currentUser; }
    @Override
    public void incrCurrentUser() { currentUser++; }
}