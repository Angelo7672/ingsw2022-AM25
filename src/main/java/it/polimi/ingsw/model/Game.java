package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Game contains all manager of the model. It is the connection point between the other classes of this package.
 * Also, it is the class that is exposed to the controller.
 */
public class Game implements GameManager{
    private final ArrayList<RoundStrategy> roundStrategies;
    private final CloudsManager cloudsManager;
    private final IslandsManager islandsManager;
    private final PlayerManager playerManager;
    private final QueueManager queueManager;
    private final Bag bag;
    private int indexSpecial;
    private int refSpecial;
    protected SpecialListener specialListener;
    private final boolean expertMode;
    private final ArrayList<Integer> extractedSpecials;

    /**
     * Create the game, but it isn't initialized yet.
     * @param expertMode is the modality of this match;
     * @param numberOfPlayer in this match;
     */
    public Game(Boolean expertMode, int numberOfPlayer){
        this.expertMode = expertMode;
        this.extractedSpecials = new ArrayList<>();
        this.roundStrategies = new ArrayList<>();
        this.bag = new Bag();
        this.cloudsManager = new CloudsManager(numberOfPlayer,this.bag);
        this.playerManager = new PlayerManager(numberOfPlayer,this.bag);
        this.islandsManager = new IslandsManager();
        this.queueManager = new QueueManager(numberOfPlayer,this.playerManager);

        //This means that there isn't any special effect active
        indexSpecial = 0;
        refSpecial = -1;

        Round round = new Round(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        roundStrategies.add(round); //normal turn without expert

        if(expertMode) {
            RoundStrategyFactory roundStrategyFactor = new RoundStrategyFactory(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);

            ArrayList<Integer> random = new ArrayList<>();
            for (int i = 1; i <= 12; i++)
                random.add(i);  //I have 12 int ordered now
            Collections.shuffle(random);
            while (random.size() != 3)
                random.remove(0);
            //now I have 3 random int
            Collections.sort(random);

            /*for (int i = 0; i < 3; i++) {
                roundStrategies.add(
                        roundStrategyFactor.getRoundStrategy(
                                random.get(i)
                        )
                );
                this.extractedSpecials.add(
                        random.get(i)
                );
            }*/

            //INSERISCI GLI SPECIAL CHE VUOI FARE USCIRE
            roundStrategies.add( roundStrategyFactor.getRoundStrategy(4));
            roundStrategies.add( roundStrategyFactor.getRoundStrategy(7));
            roundStrategies.add( roundStrategyFactor.getRoundStrategy(11));
            this.extractedSpecials.add(4);
            this.extractedSpecials.add(7);
            this.extractedSpecials.add(11);
        }
    }
    private Assistant stringToAssistant(String string){
        if(string.equalsIgnoreCase("LION")) return Assistant.LION;
        else if(string.equalsIgnoreCase("GOOSE")) return Assistant.GOOSE;
        else if(string.equalsIgnoreCase("CAT")) return Assistant.CAT;
        else if(string.equalsIgnoreCase("EAGLE")) return Assistant.EAGLE;
        else if(string.equalsIgnoreCase("FOX")) return Assistant.FOX;
        else if(string.equalsIgnoreCase("LIZARD")) return Assistant.LIZARD;
        else if(string.equalsIgnoreCase("OCTOPUS")) return Assistant.OCTOPUS;
        else if(string.equalsIgnoreCase("DOG")) return Assistant.DOG;
        else if(string.equalsIgnoreCase("ELEPHANT")) return Assistant.ELEPHANT;
        else if(string.equalsIgnoreCase("TURTLE")) return Assistant.TURTLE;
        return Assistant.NONE;
    }
    private Team stringToTeam(String string){
        if(string.equalsIgnoreCase("WHITE")) return Team.WHITE;
        else if(string.equalsIgnoreCase("BLACK")) return Team.BLACK;
        else if(string.equalsIgnoreCase("GREY")) return Team.GREY;
        return Team.NONE;
    }

    /**
     * Create a new game from start.
     */
    @Override
    public void initializeGame(){
        bag.bagInitialize();
        playerManager.initializeSchool();
        islandsManager.islandsInitialize();
        if(expertMode) {
            specialListener.notifySpecialList(extractedSpecials, getSpecialCost());
            for (int i = 1; i < 4; i++)
                roundStrategies.get(i).initializeSpecial();
        }
    }

    //Restore Game
    /**
     * @see PlayerManager with its restoreSingleSchool();
     */
    @Override
    public void schoolRestore(int playerRef, int[] studentsEntrance, int[] studentsTable, int towers, boolean[] professors, String team){
        playerManager.restoreSingleSchool(playerRef,studentsEntrance,studentsTable,towers,professors,stringToTeam(team));
    }

    /**
     * @see PlayerManager with its restoreHandAndCoins();
     */
    @Override
    public void handAndCoinsRestore(int playerRef, ArrayList<String> cards, int coins){
        ArrayList<Assistant> hand = new ArrayList<>();

        for (String card : cards) hand.add(stringToAssistant(card));

        playerManager.restoreHandAndCoins(playerRef, hand, coins);
    }

    /**
     * @see CloudsManager with its restoreClouds.
     */
    @Override
    public void cloudRestore(int cloudRef, int[] students){ cloudsManager.restoreClouds(cloudRef,students); }

    /**
     * Set archipelago size after restore.
     * @param size of archipelago;
     */
    @Override
    public void setIslandsSizeAfterRestore(int size){ islandsManager.setIslandsSizeAfterRestore(size); }

    /**
     * @see IslandsManager with its restoreIslands.
     */
    @Override
    public void islandRestore(int islandRef, int[] students, int towerValue, String towerTeam, int inhibited){
        islandsManager.restoreIslands(islandRef,students,towerValue,stringToTeam(towerTeam),inhibited);
    }

    /**
     * @see IslandsManager with its restoreMotherPose.
     */
    @Override
    public void restoreMotherPose(int islandRef){ islandsManager.restoreMotherPose(islandRef); }

    /**
     * @see Bag with its bagRestore.
     */
    @Override
    public void bagRestore(List<Integer> bag){ this.bag.bagRestore(bag); }

    /**
     * @see QueueManager with its queueRestore.
     */
    @Override
    public void queueRestore(ArrayList<Integer> playerRef, ArrayList<Integer> valueCard, ArrayList<Integer> maxMoveMotherNature){
        queueManager.queueRestore(playerRef,valueCard,maxMoveMotherNature);
    }

    //Planning Phase

    /**
     * @see CloudsManager with its refreshStudentsCloud.
     */
    @Override
    public void refreshStudentsCloud(){ cloudsManager.refreshStudentsCloud(); }

    /**
     * @see QueueManager with its queueForPlanificationPhase.
     */
    @Override
    public void queueForPlanificationPhase(){ queueManager.queueForPlanificationPhase(); }

    /**
     * @see QueueManager with its playCard.
     */
    @Override
    public boolean playCard(int playerRef, int queueRef, String card, ArrayList<String> alreadyPlayedCard) throws NotAllowedException {
        ArrayList<Assistant> alreadyPlayedAssistant = new ArrayList<>();

        for (String assistant:alreadyPlayedCard)
            alreadyPlayedAssistant.add(stringToAssistant(assistant));

        return queueManager.playCard(playerRef, queueRef, stringToAssistant(card), alreadyPlayedAssistant);
    }

    //Action Phase
    /**
     * @see QueueManager with its inOrderForActionPhase.
     */
    @Override
    public void inOrderForActionPhase(){ queueManager.inOrderForActionPhase(); }

    /**
     * It depends on which special is active.
     * @see RoundStrategy
     * @see RoundSpecial2
     * with their moveStudent.
     */
    @Override
    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) throws NotAllowedException{
        boolean stop = false;

        if(!expertMode) roundStrategies.get(indexSpecial).moveStudent(playerRef, colour, inSchool, islandRef);
        else
            for(int i = 0; i < 3 && !stop; i++)
                if(indexSpecial == extractedSpecials.get(i)){
                    stop = true;
                    roundStrategies.get(i+1).moveStudent(playerRef, colour, inSchool, islandRef);
                }
            if(!stop) roundStrategies.get(indexSpecial).moveStudent(playerRef, colour, inSchool, islandRef);
        //setSpecial(0,-1);
    }

    /**
     * It depends on which special is active.
     * @see RoundStrategy
     * @see RoundSpecial3
     * @see RoundSpecial4
     * @see RoundSpecial6
     * with their moveMotherNature.
     */
    @Override
    public boolean moveMotherNature(int queueRef, int desiredMovement) throws NotAllowedException {
        boolean victory = false;
        boolean stop = false;

        if(!expertMode) victory = roundStrategies.get(indexSpecial).moveMotherNature(queueRef, desiredMovement, refSpecial);
        else {
            for (int i = 0; i < 3 && !stop; i++)
                if (indexSpecial == extractedSpecials.get(i)){
                    stop = true;
                    victory = roundStrategies.get(i + 1).moveMotherNature(queueRef, desiredMovement, refSpecial);    //can throw NotAllowedException
                }
            if(!stop) victory = roundStrategies.get(indexSpecial).moveMotherNature(queueRef, desiredMovement, refSpecial);
            //setSpecial(0,-1);
            for (int i = 0; i < 3; i++)
                if (extractedSpecials.get(i) == 5) checkNoEntry(i + 1);
        }

        return victory;
    }

    /**
     * Check if the Island is inhibited.
     * @param ref position of Special5 in roundStrategies list;
     */
    private void checkNoEntry(int ref){ roundStrategies.get(ref).effect(); }

    /**
     * Transfer student from a cloud to player school entrance.
     * @param playerRef player reference;
     * @param cloudRef cloud reference;
     * @throws NotAllowedException if that cloud is already chosen by another player;
     */
    @Override
    public void chooseCloud(int playerRef,int cloudRef) throws NotAllowedException {
        int[] students;

        students = cloudsManager.removeStudents(cloudRef);
        for(int i = 0; i < 5 ; i++)
            playerManager.setStudentEntrance(playerRef,i,students[i]);
        setSpecial(0,-1);
    }

    /**
     * Method for specials that don't require any argument. Used by Special2, Special4, Special6, Special8.
     * @param indexSpecial special reference;
     * @param playerRef player reference;
     * @return if the operation was successful;
     */
    @Override
    public boolean useSpecialLite(int indexSpecial, int playerRef){
        if(affordSpecial(indexSpecial, playerRef)) {
            setSpecial(indexSpecial, -1);
            findSpecial(indexSpecial, playerRef);
        } else return false;
        return true;
    }

    /**
     * Method for specials that require an argument. Used by Special3, Special5, Special9, Special11, Special12.
     * @param indexSpecial special reference;
     * @param playerRef player reference;
     * @param ref argument of the special;
     * @return if the operation was successful;
     */
    @Override
    public boolean useSpecialSimple(int indexSpecial, int playerRef, int ref){
        boolean checker = false;

        if(affordSpecial(indexSpecial, playerRef)) {
            setSpecial(indexSpecial, ref);
            for(int i = 0; i < 3; i++)
                if(indexSpecial == extractedSpecials.get(i)) checker = roundStrategies.get(i+1).effect(ref);
            if(checker) findSpecial(indexSpecial, playerRef);
        }

        return checker;
    }

    /**
     * Method for specials that require two arguments. Used by Special1.
     * @param indexSpecial special reference;
     * @param playerRef player reference;
     * @param ref argument of the special;
     * @param color of the student designated;
     * @return if the operation was successful;
     */
    @Override
    public boolean useSpecialMedium(int indexSpecial, int playerRef, int ref, int color){
        boolean checker = false;

        if (affordSpecial(indexSpecial, playerRef)) {
            setSpecial(indexSpecial, ref);
            for(int i = 0; i < 3; i++)
                if(indexSpecial == extractedSpecials.get(i)) checker = roundStrategies.get(i+1).effect(ref, color);
            if(checker) findSpecial(indexSpecial, playerRef);
        }

        return checker;
    }

    /**
     * Method for specials that require two list of student. Used by Special7, Special10.
     * @param indexSpecial special reference;
     * @param playerRef player reference;
     * @param color1 first list;
     * @param color2 second list;
     * @return if the operation was successful;
     */
    @Override
    public boolean useSpecialHard(int indexSpecial, int playerRef, ArrayList<Integer> color1, ArrayList<Integer> color2){
        boolean checker = false;

        if(affordSpecial(indexSpecial, playerRef)) {
            setSpecial(indexSpecial, playerRef);
            for(int i = 0; i < 3; i++)
                if(indexSpecial == extractedSpecials.get(i)) checker = roundStrategies.get(i+1).effect(playerRef, color1, color2);
            if(checker) findSpecial(indexSpecial, playerRef);
            setSpecial(0, -1);
        }

        return checker;
    }

    /**
     * Check if player can afford a special with its coin.
     * @param indexSpecial special to check cost;
     * @param playerRef player reference;
     * @return if the player can afford the special:
     */
    private boolean affordSpecial(int indexSpecial, int playerRef){
        for(int i = 0; i < 3; i++)
            if(indexSpecial == extractedSpecials.get(i))
                return playerManager.getCoins(playerRef) >= roundStrategies.get(i+1).getCost();
        return false;
    }

    /**
     * If special is in the list, remove from the player's coins the cost of the special, then increase the cost.
     * @param indexSpecial special to check cost;
     * @param playerRef player reference;
     */
    private void findSpecial(int indexSpecial, int playerRef) {
        for(int i = 0; i < 3; i++)
            if(extractedSpecials.get(i) == indexSpecial){
                playerManager.removeCoin(playerRef, roundStrategies.get(i+1).getCost());
                roundStrategies.get(i+1).increaseCost();
                this.specialListener.notifyIncreasedCost(indexSpecial, roundStrategies.get(i+1).getCost());
            }
        this.specialListener.notifySpecial(indexSpecial, playerRef);
    }

    /**
     * Set special and its reference for this round.
     * @param indexSpecial special reference;
     * @param refSpecial reference of the special;
     */
    private void setSpecial(int indexSpecial, int refSpecial){
        this.indexSpecial = indexSpecial;
        this.refSpecial = refSpecial;
    }
    @Override
    public ArrayList<Integer> getExtractedSpecials() { return extractedSpecials; }
    @Override
    public ArrayList<Integer> getSpecialCost(){
        ArrayList<Integer> cost = new ArrayList<>();
        for(int i = 1; i < 4; i++) {
            cost.add(
                    roundStrategies.get(i)
                            .getCost()
            );
        }
        return cost;
    }

    /**
     * @see QueueManager with its readQueue;
     */
    @Override
    public int readQueue(int pos){ return queueManager.readQueue(pos); }

    /**
     * When we arrive at the end of the match return the Team winner;
     * @return return the Team winner;
     */
    @Override
    public String oneLastRide(){ return String.valueOf(playerManager.checkVictory()); }

    //Listener
    @Override
    public void setStudentsListener(StudentsListener listener){
        playerManager.studentsListener = listener;
        islandsManager.studentListener = listener;
        cloudsManager.studentsListener = listener;
    }
    @Override
    public void setTowerListener(TowersListener listener) {
        playerManager.towersListener = listener;
        islandsManager.towersListener = listener;
    }
    @Override
    public void setProfessorsListener(ProfessorsListener listener){ playerManager.professorsListener = listener; }
    @Override
    public void setPlayedCardListener(PlayedCardListener listener){
        queueManager.playedCardListener = listener;
        playerManager.playedCardListener = listener;
    }
    @Override
    public void setSpecialListener(SpecialListener listener){ this.specialListener = listener;}
    @Override
    public void setCoinsListener(CoinsListener listener){ playerManager.coinsListener = listener; }
    @Override
    public void setIslandListener(IslandListener listener){ islandsManager.islandListener = listener;}
    @Override
    public void setMotherPositionListener(MotherPositionListener listener){ islandsManager.motherPositionListener = listener;}
    @Override
    public void setInhibitedListener(InhibitedListener listener){ islandsManager.inhibitedListener = listener; }
    @Override
    public void setBagListener(BagListener listener) { bag.bagListener = listener; }
    @Override
    public void setQueueListener(QueueListener listener) { queueManager.queueListener = listener; }
    @Override
    public void setSpecialStudentsListener(SpecialStudentsListener specialStudentsListener){
        for(int i = 1; i < 4; i++)
            roundStrategies.get(i).setSpecialStudentsListener(specialStudentsListener);
    }
    @Override
    public void setNoEntryListener(NoEntryListener noEntryListener){
        for(int i = 0; i < 3; i++)
            roundStrategies.get(i).setNoEntryListener(noEntryListener);
    }
}