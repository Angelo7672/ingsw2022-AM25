package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.Special7;

import java.util.ArrayList;

public class RoundSpecial7 extends RoundStrategy{

    Special7 special;

    public RoundSpecial7(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer, playersInfo);
        special = new Special7();
    }


    public void effect(ArrayList<Integer> entranceStudent, ArrayList<Integer> cardStudent, int playerRef){
        if(useSpecial(special.getCost(), playerRef)) {
            for(int i=0; i<entranceStudent.size(); i++){
                special.effect(cardStudent.get(i), entranceStudent.get(i));
                playerManager.setStudentEntrance(playerRef, cardStudent.get(i));
                playerManager.removeStudentEntrance(playerRef, entranceStudent.get(i));
            }
        }
    }
}

