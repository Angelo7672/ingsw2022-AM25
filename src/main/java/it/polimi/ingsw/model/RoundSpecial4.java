package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.Special4;

public class RoundSpecial4 extends RoundStrategy{
    public RoundSpecial4(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer,playersInfo);
        special = new Special4();
    }


    @Override
    public int getSteps(int player){
        return playerManager.getLastMove(player)+2;
    }
}
