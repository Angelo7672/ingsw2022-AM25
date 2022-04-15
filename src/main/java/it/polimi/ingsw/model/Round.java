package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Islands.IslandsManager;
import it.polimi.ingsw.model.Specials.SpecialsManager;

import java.util.ArrayList;

public class Round extends RoundStrategy{

    private CloudsManager cloudsManager;
    private IslandsManager islandsManager;
    private PlayerManager playerManager;
    private SpecialsManager specialsManager;
    private Bag bag;

    public Round(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer,playersInfo);
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

    public void conquestIsland(int pos, String strategy, int noColor, int player){
        ArrayList<Integer> prof = playerManager.profOwners(strategy, player);
        Team playerInfluence = islandsManager.highestInfluenceTeam(prof, pos, strategy, noColor, player);
        if(playerInfluence != islandsManager.getTowerTeam(pos) && playerInfluence != Team.NOONE) { //se l'influenza è cambiata e se è != -1
            towerChange(playerInfluence.getTeam(), pos);
            islandsManager.checkAdjacentIslands(pos);
        }
    }

    public void useSpecial(int special, int player){
        if(playerManager.affordSpecial(specialsManager.getCost(special), player)){
            playerManager.removeCoin(player, specialsManager.getCost(special));
            specialsManager.increaseCost(special);
        }
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

}
