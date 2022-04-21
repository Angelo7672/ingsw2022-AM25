package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.Special10;
import it.polimi.ingsw.model.Specials.Special7;

import java.util.ArrayList;

public class RoundSpecial10 extends RoundStrategy{

    Special10 special;

    public RoundSpecial10(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer,playersInfo);
        special = new Special10();
    }

    public void effect(int playerRef, ArrayList<Integer> entranceStudent, ArrayList<Integer> tableStudent){
        if(useSpecial(special.getCost(), playerRef)) {
            for (int i = 0; i < entranceStudent.size(); i++) {
                playerManager.transferStudent(playerRef, entranceStudent.get(i), true, false);
                playerManager.removeStudentTable(playerRef, tableStudent.get(i));
                playerManager.setStudentEntrance(playerRef, tableStudent.get(i));
            }
        }
    }
}
