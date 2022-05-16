package it.polimi.ingsw.model;

import java.util.ArrayList;

public class RoundSpecial10 extends RoundStrategy{

    Special10 special;

    public RoundSpecial10(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, bag);
        special = new Special10();
    }

    @Override
    public boolean effect(int playerRef, ArrayList<Integer> entranceStudent, ArrayList<Integer> tableStudent){
        if(playerManager.checkStudentsEntrance(entranceStudent, playerRef)&&playerManager.checkStudentsTable(tableStudent,playerRef)) {
            for (int i = 0; i < entranceStudent.size(); i++) {
                playerManager.transferStudent(playerRef, entranceStudent.get(i), true, false);
                playerManager.removeStudentTable(playerRef, tableStudent.get(i));
                playerManager.setStudentEntrance(playerRef, tableStudent.get(i));
            }
            return true;
        }
        return false;
    }
    @Override
    public int getCost(){
        return special.getCost();
    }
    @Override
    public void increaseCost(){
        special.increaseCost();
    }
    @Override
    public String getName(){
        return special.getName();
    }

    private class Special10 extends Special {

        public Special10(){
            super(1, "special10");
        }
    }
}
