package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.Special3;

import java.util.ArrayList;

public class RoundSpecial3 extends RoundStrategy{

    Special3 special;

    public RoundSpecial3(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer,playersInfo);
        special = new Special3();
    }

    @Override
    public boolean moveMotherNature(int queueRef, int desiredMovement, int noColor, int islandRef){
        int maxMovement;
        boolean victory1 = false;
        boolean victory2 = false;

        if(useSpecial(special.getCost(), queueRef)) victory1 = effect(islandRef, noColor, queueRef);

        maxMovement = playerManager.readMaxMotherNatureMovement(queueRef);
        if(desiredMovement > 0 && desiredMovement <= maxMovement ){
            islandsManager.moveMotherNature(desiredMovement);
            if(islandsManager.getInhibited(islandsManager.getMotherPos())){
                islandsManager.setInhibited(islandsManager.getMotherPos(), false);
                for(int i=0; i<3; i++){
                    if(specialsManager.getName(i).equals("special5")) specialsManager.getSpecial(i).effect();
                }
            }
            else {
                victory2 = conquestIsland(islandsManager.getMotherPos(), noColor, queueRef);
            }
        }

        if(victory1 || victory2 ) return true;
        return false;
    }

    private boolean effect(int islandRef, int noColor, int playerRef){
        boolean victory = false;
        if(islandsManager.getInhibited(islandRef)){
            islandsManager.setInhibited(islandsManager.getMotherPos(), false);
            for(int i=0; i<3; i++){
                if(specialsManager.getName(i).equals("special5")) specialsManager.getSpecial(i).effect();
            }
        }
        else {
            victory = conquestIsland(islandsManager.getMotherPos(), noColor, playerRef);
        }
        return victory;
    }

}
