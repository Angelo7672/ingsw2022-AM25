package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.listeners.*;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.ArrayList;
import java.util.Collections;

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

    public Game(Boolean expertMode, int numberOfPlayer){
        this.expertMode = expertMode;
        roundStrategies = new ArrayList<>();
        this.numberOfPlayer = numberOfPlayer;
        this.bag = new Bag();
        this.cloudsManager = new CloudsManager(numberOfPlayer);
        this.playerManager = new PlayerManager(numberOfPlayer,this.bag);
        this.islandsManager = new IslandsManager();
        this.queueManager = new QueueManager(numberOfPlayer,this.playerManager);

        indexSpecial = 0;
        refSpecial = -1;

        Round round = new Round(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        roundStrategies.add(round);

        if(expertMode){
            RoundStrategyFactory roundStrategyFactor = new RoundStrategyFactory(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            ArrayList<Integer> random = new ArrayList<>();
            for(int i=1; i<=12; i++) random.add(i);
            Collections.shuffle(random);
            for(int i=0; i<3; i++) roundStrategies.add(roundStrategyFactor.getRoundStrategy(random.get(i))); //aggiungo lo special estratto
        }
    }

    @Override
    public void initializeGame(){
        bag.bagInitialize();
        playerManager.initializeSchool();
        islandsManager.islandsInitialize();
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

    @Override
    public boolean refreshStudentsCloud() {
        return roundStrategies.get(0).refreshStudentsCloud();
    }
    @Override
    public void queueForPlanificationPhase() {
        roundStrategies.get(0).queueForPlanificationPhase();
    }
    @Override
    public boolean playCard(int playerRef, int queueRef, String card, ArrayList<String> alreadyPlayedCard) throws NotAllowedException {
        ArrayList<Assistant> alreadyPlayedAssistant = new ArrayList<>();

        for (String assistant:alreadyPlayedCard)
            alreadyPlayedAssistant.add(stringToAssistant(assistant));

        return queueManager.playCard(playerRef, queueRef, stringToAssistant(card), alreadyPlayedAssistant);
    }

    @Override
    public void inOrderForActionPhase() {
        roundStrategies.get(0).inOrderForActionPhase();
    }
    @Override
    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) throws NotAllowedException{
        roundStrategies.get(indexSpecial).moveStudent(playerRef, colour, inSchool, islandRef);
        setSpecial(0,-1);
    }

    public void effect(int ref, ArrayList<Integer> color1, ArrayList<Integer> color2){
        roundStrategies.get(indexSpecial).effect(ref, color1, color2);
    }
    @Override
    public boolean moveMotherNature(int queueRef, int desiredMovement) {
        boolean victory;

        victory = roundStrategies.get(indexSpecial).moveMotherNature(queueRef, desiredMovement, refSpecial);
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
        for(int i = 0; i < 5 ; i++) playerManager.setStudentEntrance(playerRef,students[i]);
    }

    @Override
    public int readQueue(int pos) {
        return roundStrategies.get(0).readQueue(pos);
    }

    @Override
    public String oneLastRide() {
        return roundStrategies.get(0).oneLastRide();
    }

    @Override
    public void useSpecial(int indexSpecial, int playerRef, int ref, ArrayList<Integer> color1, ArrayList<Integer> color2){
        if(affordSpecial(indexSpecial, playerRef)) {
            setSpecial(indexSpecial, ref);
            switch (findName(indexSpecial)) {
                case(1): roundStrategies.get(indexSpecial).effect(ref);
                case(2): roundStrategies.get(indexSpecial).effect(ref, color1.get(0));
                case(3): roundStrategies.get(indexSpecial).effect(ref, color1, color2); break;
            }
            setSpecial(0, -1);
        }
    }
    public Boolean affordSpecial(int indexSpecial, int playerRef){
        if(playerManager.getCoins(playerRef) >= roundStrategies.get(indexSpecial).getCost()){
            playerManager.removeCoin(playerRef, roundStrategies.get(indexSpecial).getCost());
            roundStrategies.get(indexSpecial).increaseCost();
            return true;
        }
        return false;
    }
    public void setSpecial(int indexSpecial, int refSpecial){
        this.indexSpecial = indexSpecial;
        this.refSpecial = refSpecial;
        this.specialListener.notifySpecial(indexSpecial);
    }

    public int findName(int index){
        if(roundStrategies.get(index).getName().equals("special1")) return 2;
        if(roundStrategies.get(index).getName().equals("special5")) return 1;
        if(roundStrategies.get(index).getName().equals("special7")) return 3;
        if(roundStrategies.get(index).getName().equals("special10")) return 3;
        if(roundStrategies.get(index).getName().equals("special11")) return 2;
        if(roundStrategies.get(index).getName().equals("special12")) return 1;
        return -1;
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
    public void setProfessorsListener(ProfessorsListener listener){
        playerManager.professorsListener = listener;
    }
    @Override
    public void setPlayedCardListener(PlayedCardListener listener){
        queueManager.playedCardListener = listener;
    }
    @Override
    public void setSpecialListener(SpecialListener listener){ this.specialListener = listener;}
    @Override
    public void setCoinsListener(CoinsListener listener){
        playerManager.coinsListener = listener;
    }
    @Override
    public void setIslandListener(IslandListener listener){ islandsManager.islandListener = listener;}
    @Override
    public void setMotherPositionListener(MotherPositionListener listener){ islandsManager.motherPositionListener = listener;}
    @Override
    public void setInhibitedListener(InhibitedListener listener){ islandsManager.inhibitedListener = listener; }

    @Override
    public void setBagListener(BagListener listener) {
        bag.bagListener=listener;
    }
    @Override
    public void setQueueListener(QueueListener listener) {
        queueManager.queueListener=listener;
    }
}