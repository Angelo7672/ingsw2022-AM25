package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.Special7;

public class RoundSpecial7 extends RoundStrategy{

    public RoundSpecial7(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer, playersInfo);
        special = new Special7();
    }


    public void effect(int entranceStudent, int cardStudent, int player){
        special.effect(cardStudent, entranceStudent);
        playerManager.setStudentEntrance(player, cardStudent);
        playerManager.removeStudentEntrance(player, entranceStudent);
    }
}

