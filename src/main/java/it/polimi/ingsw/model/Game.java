package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game implements GameManager{
    private ArrayList<RoundStrategy> roundStrategies;
    private CloudsManager cloudsManager;
    private IslandsManager islandsManager;
    private PlayerManager playerManager;
    private QueueManager queueManager;
    private Bag bag;
    private int numberOfPlayer;
    private int indexSpecial;
    private int refSpecial;
    protected SpecialListener specialListener;
    private boolean expertMode;
    private ArrayList<Integer> extractedSpecials;

    public Game(Boolean expertMode, int numberOfPlayer){
        this.expertMode = expertMode;
        this.extractedSpecials = new ArrayList<>();
        this.roundStrategies = new ArrayList<>();
        this.numberOfPlayer = numberOfPlayer;
        this.bag = new Bag();
        this.cloudsManager = new CloudsManager(numberOfPlayer,this.bag);
        this.playerManager = new PlayerManager(numberOfPlayer,this.bag);
        this.islandsManager = new IslandsManager();
        this.queueManager = new QueueManager(numberOfPlayer,this.playerManager);

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

            for (int i = 0; i < 3; i++) {
                roundStrategies.add(
                        roundStrategyFactor.getRoundStrategy(
                                random.get(i)
                        )
                );
                this.extractedSpecials.add(
                        random.get(i)
                );
            }
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

    @Override
    public void initializeGame(){
        bag.bagInitialize();
        playerManager.initializeSchool();
        islandsManager.islandsInitialize();
        if(expertMode) {
            specialListener.notifySpecialList(extractedSpecials, getSpecialCost());
            for (int i = 0; i < 3; i++)
                roundStrategies.get(i).initializeSpecial();
        }
    }
    @Override
    public void schoolRestore(int playerRef, int[] studentsEntrance, int[] studentsTable, int towers, boolean[] professors, String team){
        playerManager.restoreSingleSchool(playerRef,studentsEntrance,studentsTable,towers,professors,stringToTeam(team));
    }
    @Override
    public void handAndCoinsRestore(int playerRef, ArrayList<String> cards, int coins){
        ArrayList<Assistant> hand = new ArrayList<>();

        for(int i = 0; i < cards.size(); i++)
            hand.add(stringToAssistant(cards.get(i)));

        playerManager.restoreHandAndCoins(playerRef, hand, coins);
    }
    @Override
    public void cloudRestore(int cloudRef, int[] students){
        cloudsManager.restoreClouds(cloudRef,students);
    }
    @Override
    public void setIslandsSizeAfterRestore(int size){ islandsManager.setIslandsSizeAfterRestore(size); }
    @Override
    public void islandRestore(int islandRef, int[] students, int towerValue, String towerTeam, int inhibited){
        islandsManager.restoreIslands(islandRef,students,towerValue,stringToTeam(towerTeam),inhibited);
    }
    @Override
    public void restoreMotherPose(int islandRef){ islandsManager.restoreMotherPose(islandRef); }
    @Override
    public void bagRestore(List<Integer> bag){ this.bag.bagRestore(bag); }
    @Override
    public void queueRestore(ArrayList<Integer> playerRef, ArrayList<Integer> valueCard, ArrayList<Integer> maxMoveMotherNature){
        queueManager.queueRestore(playerRef,valueCard,maxMoveMotherNature);
    }

    @Override
    public boolean refreshStudentsCloud(){ return cloudsManager.refreshStudentsCloud(); }
    @Override
    public void queueForPlanificationPhase(){ queueManager.queueForPlanificationPhase(); }
    @Override
    public boolean playCard(int playerRef, int queueRef, String card, ArrayList<String> alreadyPlayedCard) throws NotAllowedException {
        ArrayList<Assistant> alreadyPlayedAssistant = new ArrayList<>();

        for (String assistant:alreadyPlayedCard)
            alreadyPlayedAssistant.add(stringToAssistant(assistant));

        return queueManager.playCard(playerRef, queueRef, stringToAssistant(card), alreadyPlayedAssistant);
    }

    @Override
    public void inOrderForActionPhase(){ queueManager.inOrderForActionPhase(); }
    @Override
    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) throws NotAllowedException{
        roundStrategies.get(indexSpecial).moveStudent(playerRef, colour, inSchool, islandRef);
        setSpecial(0,-1);
    }

    @Override
    public boolean moveMotherNature(int queueRef, int desiredMovement) throws NotAllowedException {
        boolean victory;

        victory = roundStrategies.get(indexSpecial).moveMotherNature(queueRef, desiredMovement, refSpecial);    //can throw NotAllowedException
        setSpecial(0,-1);
        if(expertMode) checkNoEntry(); //possiamo mettere un boolean in game per farlo attivare solo se si usa questo special nella partita

        return victory;
    }
    private void checkNoEntry(){
        int index=-1;
        for(int i=0; i<3; i++){
            if(roundStrategies.get(i).getName().equals("special5")) index=i;
        }
        if(index!=-1) roundStrategies.get(index).effect();
    }
    @Override
    public void chooseCloud(int playerRef,int cloudRef) throws NotAllowedException {
        int[] students;

        students = cloudsManager.removeStudents(cloudRef);
        for(int i = 0; i < 5 ; i++)
            playerManager.setStudentEntrance(playerRef,i,students[i]);
        setSpecial(0,0);
    }

    @Override
    public int readQueue(int pos){ return queueManager.readQueue(pos); }

    @Override
    public String oneLastRide(){ return String.valueOf(playerManager.checkVictory()); }

    @Override
    public boolean useSpecialLite(int indexSpecial, int playerRef){
        if(affordSpecial(indexSpecial, playerRef)) {
            setSpecial(indexSpecial, -1);
            this.specialListener.notifySpecial(indexSpecial, playerRef);
            playerManager.removeCoin(playerRef, roundStrategies.get(indexSpecial).getCost());
            roundStrategies.get(indexSpecial).increaseCost();
            this.specialListener.notifyIncreasedCost(indexSpecial, roundStrategies.get(indexSpecial).getCost());
        } else return false;
        setSpecial(0, -1);
        return true;
    }
    @Override
    public boolean useSpecialSimple(int indexSpecial, int playerRef, int ref){
        boolean checker = false;

        if(affordSpecial(indexSpecial, playerRef)) {
            setSpecial(indexSpecial, ref);
            this.specialListener.notifySpecial(indexSpecial, playerRef);
            checker = roundStrategies.get(indexSpecial).effect(ref);
            if(checker) {
                //fa outofbound
                playerManager.removeCoin(playerRef, roundStrategies.get(indexSpecial).getCost());
                roundStrategies.get(indexSpecial).increaseCost();
                this.specialListener.notifyIncreasedCost(indexSpecial, roundStrategies.get(indexSpecial).getCost());
            }
            setSpecial(0, -1);
        }

        return checker;
    }
    @Override
    public boolean useSpecialMedium(int indexSpecial, int playerRef, int ref, int color){
        boolean checker = false;

        if (affordSpecial(indexSpecial, playerRef)) {
            setSpecial(indexSpecial, ref);
            this.specialListener.notifySpecial(indexSpecial, playerRef);
            checker = roundStrategies.get(indexSpecial).effect(ref, color);
            if(checker) {
                playerManager.removeCoin(playerRef, roundStrategies.get(indexSpecial).getCost());
                roundStrategies.get(indexSpecial).increaseCost();
                this.specialListener.notifyIncreasedCost(indexSpecial, roundStrategies.get(indexSpecial).getCost());
            }
            setSpecial(0, -1);
        }

        return checker;
    }
    @Override
    public boolean useSpecialHard(int indexSpecial, int playerRef, ArrayList<Integer> color1, ArrayList<Integer> color2){
        boolean checker = false;

        if(affordSpecial(indexSpecial, playerRef)) {
            setSpecial(indexSpecial, playerRef);
            this.specialListener.notifySpecial(indexSpecial, playerRef);
            checker = roundStrategies.get(indexSpecial).effect(playerRef, color1, color2);
            if(checker) {
                playerManager.removeCoin(playerRef, roundStrategies.get(indexSpecial).getCost());
                roundStrategies.get(indexSpecial).increaseCost();
                this.specialListener.notifyIncreasedCost(indexSpecial, roundStrategies.get(indexSpecial).getCost());
            }
            setSpecial(0, -1);
        }

        return checker;
    }
    private boolean affordSpecial(int indexSpecial, int playerRef){
        for(int i = 0; i < 3; i++)
            if(indexSpecial == extractedSpecials.get(i))
                return playerManager.getCoins(playerRef) >= roundStrategies.get(i+1).getCost();
        return false;
    }
    public void setSpecial(int indexSpecial, int refSpecial){
        this.indexSpecial = indexSpecial;
        this.refSpecial = refSpecial;
    }

    /*public int findName(int index){
        if(roundStrategies.get(index).getName().equals("special1")) return 2;
        if(roundStrategies.get(index).getName().equals("special5")) return 1;
        if(roundStrategies.get(index).getName().equals("special7")) return 3;
        if(roundStrategies.get(index).getName().equals("special10")) return 3;
        if(roundStrategies.get(index).getName().equals("special11")) return 2;
        if(roundStrategies.get(index).getName().equals("special12")) return 1;
        return -1;
    }*/
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

    @Override
    public void setStudentsListener(StudentsListener listener){
        playerManager.studentsListener = listener;
        islandsManager.studentListener=listener;
        cloudsManager.studentsListener=listener;
    }
    @Override
    public void setTowerListener(TowersListener listener) {
        playerManager.towersListener= listener;
        islandsManager.towersListener= listener;
    }
    @Override
    public void setProfessorsListener(ProfessorsListener listener){ playerManager.professorsListener = listener; }
    @Override
    public void setPlayedCardListener(PlayedCardListener listener){
        queueManager.playedCardListener = listener;
        playerManager.playedCardListener=listener;
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
        for(int i = 0; i < 3; i++)
            roundStrategies.get(i).setSpecialStudentsListener(specialStudentsListener);
    }
    @Override
    public void setNoEntryListener(NoEntryListener noEntryListener){
        for(int i = 0; i < 3; i++)
            roundStrategies.get(i).setNoEntryListener(noEntryListener);
    }
}