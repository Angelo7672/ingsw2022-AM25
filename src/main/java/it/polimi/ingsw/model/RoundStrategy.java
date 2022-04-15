package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Islands.IslandsManager;
import it.polimi.ingsw.model.Specials.Special;
import it.polimi.ingsw.model.Specials.SpecialsManager;

import java.util.ArrayList;

public abstract class RoundStrategy {

    public CloudsManager cloudsManager;
    public IslandsManager islandsManager;
    public PlayerManager playerManager;
    public SpecialsManager specialsManager;
    public Bag bag;
    public Special special;

    public RoundStrategy(int numberOfPlayer, String[] playersInfo){
        this.bag = new Bag();
        this.cloudsManager = new CloudsManager(numberOfPlayer);
        refreshStudentsCloud(numberOfPlayer);
        this.islandsManager = new IslandsManager();
        this.playerManager = new PlayerManager(numberOfPlayer, playersInfo);
        specialsManager = new SpecialsManager();
    }
    public void refreshStudentsCloud(int numberOfPlayer){

        if(numberOfPlayer == 2 || numberOfPlayer ==4 ) {
            for (int j = 0; j < numberOfPlayer; j++) {
                for (int i = 0; i < 3; i++) {
                    cloudsManager.refreshCloudStudents(bag.extraction(), j);
                }
            }
        } else if(numberOfPlayer==3) {
            for (int j = 0; j < numberOfPlayer; j++) {
                for (int i = 0; i < 4; i++) {
                    cloudsManager.refreshCloudStudents(bag.extraction(), j);
                }
            }
        }
    }

    private void towerChange(int player, int pos){

        if(islandsManager.getTowerTeam(pos).toString()!="NOONE"){
            playerManager.placeTower(islandsManager.getTowerTeam(pos).getTeam(), islandsManager.getTowerValue(pos));
        }
        islandsManager.setTowerTeam(player, islandsManager.getTowerTeam(pos));
        playerManager.removeTower(islandsManager.getTowerTeam(pos).getTeam(), islandsManager.getTowerValue(pos));

    }

    public Boolean useSpecial(int special, int player){
        if(playerManager.affordSpecial(specialsManager.getCost(special), player)){
            playerManager.removeCoin(player, specialsManager.getCost(special));
            specialsManager.increaseCost(special);
            return true;
        }
        return false;
    }

    public void moveStudent(int playerRef, int colour, boolean inSchool){
        if(!inSchool){
            playerManager.transferStudent(playerRef, colour, inSchool);
            //metodo di IslandsManager per mettere studente su isola
        }
        else if(inSchool)
            playerManager.transferStudent(playerRef, colour, inSchool);
    }

    public void playCard(int playerRef,int cardRef){
        //togli da hand la carta e aggiorna la queue
    }

    public void fillEntrance(int playerRef,int cloudRef){
        for(int i = 0; i < 5 ; i++){
            playerManager.setStudentEntrance(playerRef, cloudsManager.removeStudents(cloudRef)[i]);
        }
    }

    public void conquestIsland(int pos, String strategy, int noColor, int player){
        ArrayList<Integer> prof = playerManager.profOwners(strategy, player);
        Team playerInfluence = highestInfluenceTeam(prof, pos, strategy, noColor, player);
        if(playerInfluence != islandsManager.getTowerTeam(pos) && playerInfluence != Team.NOONE) { //se l'influenza è cambiata e se è != -1
            towerChange(playerInfluence.getTeam(), pos);
            islandsManager.checkAdjacentIslands(pos);
        }
    }

    //da fare override con special
    private Team highestInfluenceTeam(ArrayList<Integer> prof, int pos, int noColor, int player) {
        int inflP1 = 0, inflP2 = 0, inflP3 = 0;
        for(int i=0; i<5; i++) {
            if(islandsManager.getStudent(pos, i)>0)
                //aggiunge a chi possiede il prof di quel colore il numero di studenti
                //se aggungiamo una variabile influence ad ogni player ci evidiamo lo switch
                switch (prof.get(i)){
                    case(0): inflP1+=island.getNumStudents(i); break;
                    case(1): inflP2+=island.getNumStudents(i); break;
                    case(2): inflP3+=island.getNumStudents(i); break;
                }
        }

        //sommo le torri all'influenza
        switch (island.getTowerTeam().toString()){
            case("WHITE"): inflP1 += island.getTowerValue(); break;
            case("BLACK"): inflP2 += island.getTowerValue(); break;
            case("GREY"): inflP3 += island.getTowerValue(); break;
        }

        //non so se è meglio metterli dentro ad un metodo findMax questi if
        if (inflP1>inflP2 && inflP1>inflP3) return Team.WHITE;
        if (inflP2>inflP1 && inflP2>inflP3) return Team.BLACK;
        if (inflP3>inflP2 && inflP3>inflP1) return Team.GREY;
        return Team.NOONE;
    }


}
