package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.Special11;

public class RoundSpecial11 extends RoundStrategy{

    public RoundSpecial11(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer, playersInfo);
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
