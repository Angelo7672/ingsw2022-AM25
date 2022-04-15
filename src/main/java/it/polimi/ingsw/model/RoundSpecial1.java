package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.*;


public class RoundSpecial1 extends RoundStrategy{



    public RoundSpecial1(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer, playersInfo);
        special =new Special1();
    }

    public void effect(int color, int pos, int player){
        if(useSpecial(special.getCost(), player)) {
            islandsManager.incStudent(pos, color);
            int extracted = bag.extraction();
            special.effect(color, extracted);
        }
    }

}
