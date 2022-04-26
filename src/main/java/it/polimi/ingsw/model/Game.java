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


    public Game(Boolean expertMode, int numberOfPlayer, String[] playersInfo){
        roundStrategies = new ArrayList<>();
        this.numberOfPlayer = numberOfPlayer;
        this.cloudsManager = new CloudsManager(numberOfPlayer);
        this.islandsManager = new IslandsManager();
        this.playerManager = new PlayerManager(numberOfPlayer, playersInfo);
        this.bag = new Bag();
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
            indexSpecial = 0;
            RoundStrategyFactory roundStrategyFactor = new RoundStrategyFactory(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
            ArrayList<Integer> random = new ArrayList<>();
            for(int i=1; i<=12; i++) random.add(i); //riempio con numeri da 1 a 12
            Collections.shuffle(random); //mischio i numeri
            for(int i=0; i<3; i++) roundStrategies.add(roundStrategyFactor.getRoundStrategy(random.get(i))); //aggiungo lo special estratto
        }
    }

    public Boolean useSpecial(int playerRef){
        if(playerManager.getCoins(playerRef) >= roundStrategies.get(indexSpecial).getCost()){
            playerManager.removeCoin(playerRef, roundStrategies.get(indexSpecial).getCost());
            roundStrategies.get(indexSpecial).increaseCost();
            return true;
        }
        return false;
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
    public String playCard(int playerRef, int queRef, String card) {
        return roundStrategies.get(0).playCard(playerRef, queRef, card);
    }

    @Override
    public void inOrderForActionPhase() {
        roundStrategies.get(0).inOrderForActionPhase();
    }

    @Override
    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) {
        if(findName(0,indexSpecial) &&
                useSpecial(playerRef)) roundStrategies.get(indexSpecial).moveStudent(playerRef, colour, inSchool, islandRef);
        else roundStrategies.get(0).moveStudent(playerRef, colour, inSchool, islandRef);
    }

    @Override
    public boolean moveMotherNature(int queueRef, int desiredMovement, int noColor, int islandRef, String special, int specialIndex) {
        boolean victory = false;
        if(findName(1,specialIndex) &&
                useSpecial(queueRef)) victory = roundStrategies.get(specialIndex).moveMotherNature(queueRef, desiredMovement, noColor, islandRef);
        else victory = roundStrategies.get(0).moveMotherNature(queueRef, desiredMovement, noColor, islandRef);
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
    public void effect(int playerRef, ArrayList<Integer> color1, ArrayList<Integer> color2, int specialIndex){
        if(findName(2, specialIndex)&&useSpecial(playerRef))  roundStrategies.get(specialIndex).effect(playerRef,color1, color2);
    }

    public void setIndexSpecial(int index){
        indexSpecial=index;
    }



    public boolean findName(int method, int index){
        switch(method){
            case(0):{
                if(roundStrategies.get(index).getName().equals("special2")) return true;
            }
            case (1):{
                if(roundStrategies.get(index).getName().equals("special1")) return true;
                if(roundStrategies.get(index).getName().equals("special3")) return true;
                if(roundStrategies.get(index).getName().equals("special4")) return true;
                if(roundStrategies.get(index).getName().equals("special5")) return true;
                if(roundStrategies.get(index).getName().equals("special6")) return true;
                if(roundStrategies.get(index).getName().equals("special8")) return true;
                if(roundStrategies.get(index).getName().equals("special9")) return true;
                if(roundStrategies.get(index).getName().equals("special11")) return true;
                if(roundStrategies.get(index).getName().equals("special12")) return true;
            }
            case(2):{
                if(roundStrategies.get(index).getName().equals("special7")) return true;
                if(roundStrategies.get(index).getName().equals("special10")) return true;
            }
            default:return false;
        }
    }

}
