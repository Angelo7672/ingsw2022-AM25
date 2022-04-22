package it.polimi.ingsw.model;

import java.util.ArrayList;

public class RoundSpecial11 extends RoundStrategy{

    Special11 special;

    public RoundSpecial11(int numberOfPlayer, String[] playersInfo, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, Bag bag){
        super(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
        special =new Special11();
    }

    @Override
    public void effect(int playerRef, int color){
        playerManager.setStudentTable(playerRef, color);
        int extracted = bag.extraction();
        special.effect(color, extracted);
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


    private class Special11 extends Special {

        public Special11(){
            super(2, "special11");
        }
        private ArrayList<Integer> students;


        public void setup(ArrayList<Integer> color){
            for(int i=0; i<5;i++){
                sum(color.get(i), i);
            }
        }
        public int getStudent(int color){
            return students.get(color);
        }

        //num = numero di studenti da sommare
        private void sum(int num, int color){
            students.set(color, students.get(color)+num);
        }

        @Override
        public void effect(int chosen, int extracted) {
            sum(1, -chosen);
            sum(1, extracted);
        }

    }
}
