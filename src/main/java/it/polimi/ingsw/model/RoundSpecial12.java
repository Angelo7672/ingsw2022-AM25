package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.Special12;
import it.polimi.ingsw.model.Specials.Special7;

import java.util.ArrayList;

public class RoundSpecial12 extends RoundStrategy{

    Special12 special;

    public RoundSpecial12(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer, playersInfo);
        special =new Special12();
    }

    public void effect(int color, int playerRef){
        if(useSpecial(special.getCost(), playerRef)) {
            for (int i = 0; i < numberOfPlayer; i++) {
                if (playerManager.getStudentTable(i, color) >= 3) {
                    for(int j=0; j<3; j++) {
                        playerManager.removeStudentTable(i, color);
                    }
                    bag.fillBag(color, 3);
                } else {
                    bag.fillBag(color, playerManager.getStudentTable(i, color)); //da fare prima di decrease perchè dopo non ho piu il num di studenti tolti
                    while(playerManager.getStudentTable(i, color) != 0) playerManager.removeStudentTable(i, color);

                }
            }
        }

    }

}
