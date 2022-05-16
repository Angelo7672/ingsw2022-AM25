package it.polimi.ingsw.model;

import java.util.ArrayList;

public class RoundSpecial11 extends RoundStrategy{

    Special11 special;

    public RoundSpecial11(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, bag);
        special =new Special11();
        int[] extraction = {0,0,0,0,0};
        for(int i=0; i<4; i++){
            extraction[bag.extraction()]++;
        }
        special.setup(extraction);
    }

    @Override
    public boolean effect(int playerRef, ArrayList<Integer> color1, ArrayList<Integer> null2){
        if(special.checkStudents(color1)) {
            playerManager.setStudentTable(playerRef, color1.get(0));
            int extracted = bag.extraction();
            special.effect(color1.get(0), extracted);
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


    private class Special11 extends Special {

        private int[] students= {0,0,0,0,0};

        public Special11(){
            super(2, "special11");
        }

        public void setup(int[] color){
            for(int i=0; i<5;i++){
                students[i]+=color[i];
            }
        }

        public int getStudent(int color){
            return students[color];
        }
        public void setStudent(int color){
            students[color]++;
        }

        @Override
        public void effect(int chosen, int extracted) {
            students[chosen]--;
            students[extracted]++;
        }

        public boolean checkStudents(ArrayList<Integer> students){
            for(int i=0; i<students.size(); i++){
                if(getStudent(students.get(i))==0) return false;
            }
            return true;
        }

    }
}
