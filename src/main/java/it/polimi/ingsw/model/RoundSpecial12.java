package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.ArrayList;

public class RoundSpecial12 extends RoundStrategy{
    Special12 special;

    public RoundSpecial12(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        special =new Special12();
    }

    @Override
    public boolean effect(int color){
        for (int i = 0; i < numberOfPlayer; i++) {
            try {
                if (playerManager.getStudentTable(i, color) >= 3) {
                    for (int j = 0; j < 3; j++)
                        playerManager.removeStudentTable(i, color);
                    bag.fillBag(color, 3);
                } else {
                    bag.fillBag(color, playerManager.getStudentTable(i, color));
                    while (playerManager.getStudentTable(i, color) != 0) playerManager.removeStudentTable(i, color);
                }
            }catch (NotAllowedException notAllowedException) { return false; }
        }
        return true;
    }

    @Override
    public int getCost(){ return special.getCost(); }
    @Override
    public void increaseCost(){ special.increaseCost(); }
    @Override
    public String getName(){ return special.getName(); }

    private class Special12 extends Special {
        public Special12(){
            super(3, "special12");
        }
    }
}