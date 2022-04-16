package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.Special11;

import java.util.ArrayList;

public class RoundSpecial11 extends RoundStrategy{

    public RoundSpecial11(int numberOfPlayer, String[] playersInfo, ArrayList<Integer> color){
        super(numberOfPlayer, playersInfo, color);
        special =new Special11();
    }

    public void effect(int color, int player){
        if(useSpecial(special.getCost(), player)) {
            playerManager.increaseStudentTable(player, color);
            int extracted = bag.extraction();
            special.effect(color, extracted);
        }
    }

}
