package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Islands.IslandsManager;
import it.polimi.ingsw.model.Specials.Special3;

import java.util.ArrayList;

public class RoundSpecial3 extends RoundStrategy{

    public RoundSpecial3(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer,playersInfo);
        special = new Special3();
    }

    @Override
    public void conquestIsland(int pos, String strategy, int noColor, int player){
        ArrayList<Integer> prof = playerManager.profOwners(strategy, player);
        Team playerInfluence = highestInfluenceTeam(prof, pos, strategy, noColor, player);
        if(playerInfluence != islandsManager.getTowerTeam(pos) && playerInfluence != Team.NOONE) { //se l'influenza è cambiata e se è != -1
            towerChange(playerInfluence.getTeam(), pos);
            islandsManager.checkAdjacentIslands(pos);
        }
        //mother pos da mettere ovuqnue!!
        playerInfluence = highestInfluenceTeam(prof, islandsManager.getMotherPos(), strategy, noColor, player);
        if(playerInfluence != islandsManager.getTowerTeam(pos) && playerInfluence != Team.NOONE) { //se l'influenza è cambiata e se è != -1
            towerChange(playerInfluence.getTeam(), pos);
            islandsManager.checkAdjacentIslands(pos);
        }

    }
}
