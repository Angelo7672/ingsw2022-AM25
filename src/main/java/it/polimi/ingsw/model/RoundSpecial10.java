package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.Special10;

import java.util.ArrayList;

public class RoundSpecial10 extends RoundStrategy{

    public RoundSpecial10(int numberOfPlayer, String[] playersInfo, ArrayList<Integer> color){
        super(numberOfPlayer,playersInfo, color);
        special = new Special10();
    }

    public void effect(int player, int entranceColor,int tableColor){
        playerManager.transferStudent(player, entranceColor, true);
        playerManager.decreaseStudentTable(player, tableColor, 1);
        playerManager.setStudentEntrance(player, tableColor);
    }
}
