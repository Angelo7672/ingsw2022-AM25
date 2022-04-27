package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;

public class Game implements GameManager{
    private ArrayList<RoundStrategy> roundStrategies;
    private CloudsManager cloudsManager;
    private IslandsManager islandsManager;
    private PlayerManager playerManager;
    private Bag bag;
    private int numberOfPlayer;
    private int indexSpecial;
    private int refSpecial;


    public Game(Boolean expertMode, int numberOfPlayer, String[] playersInfo){
        roundStrategies = new ArrayList<>();
        this.numberOfPlayer = numberOfPlayer;
        this.cloudsManager = new CloudsManager(numberOfPlayer);
        this.islandsManager = new IslandsManager();
        this.playerManager = new PlayerManager(numberOfPlayer, playersInfo);
        this.bag = new Bag();
        indexSpecial = 0;
        refSpecial = -1;
        if(numberOfPlayer == 2 || numberOfPlayer == 4){
            for(int j = 0; j < numberOfPlayer; j++)
                for (int i = 0; i < 7; i++) playerManager.setStudentEntrance(j,bag.extraction());
        }else if(numberOfPlayer == 3){
            for(int j = 0; j < numberOfPlayer; j++)
                for (int i = 0; i < 9; i++) playerManager.setStudentEntrance(j,bag.extraction());
        }
        Round round = new Round(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
        roundStrategies.add(round);
        if(expertMode){
            RoundStrategyFactory roundStrategyFactor = new RoundStrategyFactory(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
            ArrayList<Integer> random = new ArrayList<>();
            for(int i=1; i<=12; i++) random.add(i); //riempio con numeri da 1 a 12
            Collections.shuffle(random); //mischio i numeri
            for(int i=0; i<3; i++) roundStrategies.add(roundStrategyFactor.getRoundStrategy(random.get(i))); //aggiungo lo special estratto
        }
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
    public int readQueue(int pos) {
        return roundStrategies.get(0).readQueue(pos);
    }

    @Override
    public boolean playCard(int playerRef, int queRef, String card) {
        return roundStrategies.get(0).playCard(playerRef, queRef, card);
    }

    @Override
    public void inOrderForActionPhase() {
        roundStrategies.get(0).inOrderForActionPhase();
    }

    @Override
    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) {
        roundStrategies.get(indexSpecial).moveStudent(playerRef, colour, inSchool, islandRef);
        setSpecial(0,-1);
    }

    @Override
    public boolean moveMotherNature(int queueRef, int desiredMovement) {
        boolean victory = false;
        roundStrategies.get(indexSpecial).moveMotherNature(queueRef, desiredMovement, refSpecial);
        setSpecial(0,-1);
        checkNoEntry(); //possiamo mettere un boolean in game per farlo attivare solo se si usa questo special nella partita
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
    public void chooseCloud(int playerRef, int cloudRef) {
        roundStrategies.get(0).chooseCloud(playerRef, cloudRef);
    }

    @Override
    public String oneLastRide() {
        return roundStrategies.get(0).oneLastRide();
    }

    //to do before moveStudent
    public void effect(int ref, ArrayList<Integer> color1, ArrayList<Integer> color2){
        roundStrategies.get(indexSpecial).effect(ref,color1, color2);
    }

    @Override
    public void useSpecial(int indexSpecial, int playerRef, int ref, ArrayList<Integer> color1, ArrayList<Integer> color2){
        if(affordSpecial(indexSpecial, playerRef)) {
            setSpecial(indexSpecial, ref);
            if(findName(indexSpecial)) {
                effect(ref, color1, color2);
                setSpecial(0, -1);
            }
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
        this.indexSpecial=indexSpecial;
        this.refSpecial = refSpecial;
    }

    public boolean findName(int index){
        if(roundStrategies.get(index).getName().equals("special1")) return true;
        if(roundStrategies.get(index).getName().equals("special5")) return true;
        if(roundStrategies.get(index).getName().equals("special7")) return true;
        if(roundStrategies.get(index).getName().equals("special10")) return true;
        if(roundStrategies.get(index).getName().equals("special11")) return true;
        if(roundStrategies.get(index).getName().equals("special12")) return true;
        return false;
    }


}
