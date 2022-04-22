package it.polimi.ingsw.model;

import java.util.ArrayList;


public class RoundSpecial1 extends RoundStrategy{

    Special1 special;
    
    public RoundSpecial1(int numberOfPlayer, String[] playersInfo, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, Bag bag){
        super(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
        this.special = new Special1();
    }

    @Override
    public void effect(int islandRef, int color){
        islandsManager.incStudent(islandRef, color);
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



    private class Special1 extends Special {

        private ArrayList<Integer> students;


        private Special1(){
            super(1, "special1");
            students=new ArrayList<>();
            for(int i=0; i<5; i++) students.add(0);
        }

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
