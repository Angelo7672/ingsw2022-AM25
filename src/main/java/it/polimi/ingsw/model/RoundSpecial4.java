package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.Special4;

import java.util.ArrayList;

public class RoundSpecial4 extends RoundStrategy{

    Special4 special;

    public RoundSpecial4(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer,playersInfo);
        special = new Special4();
    }


    @Override
    public boolean moveMotherNature(int queueRef, int desiredMovement, int noColor, int islandRef){
        int maxMovement;
        boolean victory = false;

        if(useSpecial(special.getCost(), queueRef)) maxMovement = playerManager.readMaxMotherNatureMovement(queueRef)+2; //da incrementare anche per scelta del giocatore
        else maxMovement = playerManager.readMaxMotherNatureMovement(queueRef);

        if(desiredMovement > 0 && desiredMovement <= maxMovement){
            islandsManager.moveMotherNature(desiredMovement);
            if(islandsManager.getInhibited(islandsManager.getMotherPos())){
                islandsManager.setInhibited(islandsManager.getMotherPos(), false);
                for(int i=0; i<3; i++){
                    if(specialsManager.getName(i).equals("special5")) specialsManager.getSpecial(i).effect();
                }
            }
            else {
                victory = conquestIsland(islandsManager.getMotherPos(), noColor, queueRef);
            }
        }
        return victory;
    }
}
