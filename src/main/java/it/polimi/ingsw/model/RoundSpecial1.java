package it.polimi.ingsw.model;

import java.util.ArrayList;

public class RoundSpecial1 extends RoundStrategy{
    Special1 special;
    
    public RoundSpecial1(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        this.special = new Special1();
        int[] extraction = {0,0,0,0,0};
        for(int i=0; i<4; i++)
            extraction[bag.extraction()]++;
        special.setup(extraction);
    }

    @Override
    public boolean effect(int islandRef, ArrayList<Integer> color1, ArrayList<Integer> color2){
        if(getStudents(color1.get(0))>0){
            int extracted = bag.extraction();
            special.effect(color1.get(0), extracted);
            islandsManager.incStudent(islandRef, color1.get(0));
            return true;
        }
        return false;
    }

    @Override
    public int getStudents(int color){return special.getStudent(color);}

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
        private int[] students= {0,0,0,0,0};

        private Special1(){
            super(1, "special1");
        }

        public void setup(int[] color){
            for(int i=0; i<5;i++)
                students[i]+=color[i];
        }

        public int getStudent(int color){return students[color]; }

        @Override
        public void effect(int chosen, int extracted) {
            if(getStudent(chosen)>0) {
                students[chosen]--;
                students[extracted]++;
            }
        }
    }
}