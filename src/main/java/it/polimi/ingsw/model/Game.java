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
            RoundStrategyFactor roundStrategyFactor = new RoundStrategyFactor(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
            ArrayList<Integer> random = new ArrayList<>();
            for(int i=1; i<=12; i++) random.add(i); //riempio con numeri da 1 a 12
            Collections.shuffle(random); //mischio i numeri
            for(int i=0; i<3; i++) roundStrategies.add(roundStrategyFactor.getRoundStrategy(random.get(i))); //aggiungo lo special estratto
        }
    }

    public Boolean useSpecial(int special, int playerRef){
        if(playerManager.getCoins(playerRef) >= roundStrategies.get(special).getCost()){
            playerManager.removeCoin(playerRef, roundStrategies.get(special).getCost());
            roundStrategies.get(special).increaseCost();
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
        roundStrategies.get(0).moveStudent(playerRef, colour, inSchool, islandRef);
    }

    @Override
    public boolean moveMotherNature(int queueRef, int desiredMovement, int noColor, int islandRef, String special, int index) {
        boolean victory = false;
        if(useSpecial(index, queueRef)) victory = roundStrategies.get(index).moveMotherNature(queueRef, desiredMovement, noColor, islandRef);
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

    //vanno prima di moveStudent
    public void effect(int special, int playerRef, ArrayList<Integer> entranceStudent, ArrayList<Integer> student){
        if(useSpecial(special, playerRef)) roundStrategies.get(special).effect(playerRef,entranceStudent, student);
    }
    public void effect(int special, int playerRef, int ref, int color){
        if(useSpecial(special, playerRef)) roundStrategies.get(special).effect(ref, color);
    }
    public void effect(int special, int playerRef, int ref){
        if(useSpecial(special, playerRef)) roundStrategies.get(special).effect(ref);
    }

    /*@Override //al posto di int index si puo fare un metodo che lo restituisce, dipende come vogliamo implementarlo. oppure con index si può togliere lo switch
    public boolean moveMotherNature(int queueRef, int desiredMovement, int noColor, int islandRef, String special, int index) {
        switch(special){
            case("special3"): {
                roundStrategies.get(index).moveMotherNature(queueRef, desiredMovement, noColor, islandRef);
                break;
            }
            case("special4"): {
                roundStrategies.get(index).moveMotherNature(queueRef, desiredMovement, noColor, islandRef);
                break;
            }
            case("special6"): {
                roundStrategies.get(index).moveMotherNature(queueRef, desiredMovement, noColor, islandRef);
                break;
            }
            case("special8"): {
                roundStrategies.get(index).moveMotherNature(queueRef, desiredMovement, noColor, islandRef);
                break;
            }
            case("special9"): {
                roundStrategies.get(index).moveMotherNature(queueRef, desiredMovement, noColor, islandRef);
                break;
            }
            default: roundStrategies.get(0).moveMotherNature(queueRef,desiredMovement,-1,islandRef);
        }

        return false;
    }*/

    @Override
    public void chooseCloud(int playerRef, int cloudRef) {
        roundStrategies.get(0).chooseCloud(playerRef, cloudRef);
    }

    @Override
    public String oneLastRide() {
        return roundStrategies.get(0).oneLastRide();
    }
}
