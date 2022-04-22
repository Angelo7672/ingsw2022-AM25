package it.polimi.ingsw.model;

import java.util.ArrayList;

public class RoundSpecial7 extends RoundStrategy{

    Special7 special;

    public RoundSpecial7(int numberOfPlayer, String[] playersInfo, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, Bag bag){
        super(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
        special = new Special7();
    }

    @Override
    public void effect(int playerRef, ArrayList<Integer> entranceStudent, ArrayList<Integer> cardStudent){
        for(int i=0; i<entranceStudent.size(); i++){
            special.effect(cardStudent.get(i), entranceStudent.get(i));
            playerManager.setStudentEntrance(playerRef, cardStudent.get(i));
            playerManager.removeStudentEntrance(playerRef, entranceStudent.get(i));
        }
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

    private class Special7 extends Special {

        private ArrayList<Integer> students;

        public Special7(){
            super(1, "special7");
            students = new ArrayList<>();
            for(int i=0; i<5; i++) students.add(0);
        }

        public void setup(ArrayList<Integer> color){
            for(int i=0; i<5;i++){
                sum(color.get(i), i);
            }
        }

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

