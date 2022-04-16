package it.polimi.ingsw.model;

import it.polimi.ingsw.model.RoundStrategy;
import it.polimi.ingsw.model.Specials.*;
import it.polimi.ingsw.model.Team;

import java.util.ArrayList;

public class RoundSpecial2 extends RoundStrategy {

    public RoundSpecial2(int numberOfPlayer, String[] playersInfo, ArrayList<Integer> color){
        super(numberOfPlayer,playersInfo, color);
        special = new Special2();
    }

    private ArrayList<Integer> profOwner(){
        ArrayList<Integer> prof = new ArrayList<>();

        return prof;
    }

    @Override
    public void conquestIsland(int pos, String strategy, int noColor, int player){
        ArrayList<Integer> prof = playerManager.profOwners(strategy, player);
        Team playerInfluence = highestInfluenceTeam(prof, pos, noColor, player);
        if(playerInfluence != islandsManager.getTowerTeam(pos) && playerInfluence != Team.NOONE) { //se l'influenza è cambiata e se è != -1
            towerChange(playerInfluence.getValue(), pos);
            islandsManager.checkAdjacentIslands(pos);
        }
    }


}
