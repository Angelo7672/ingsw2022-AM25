package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.ArrayList;

public class RoundSpecial7 extends RoundStrategy{
    Special7 special;

    public RoundSpecial7(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        special = new Special7();
        int[] extraction = {0,0,0,0,0};
        for(int i=0; i<6; i++){
            extraction[bag.extraction()]++;
        }
        special.setup(extraction);
    }

    @Override
    public boolean effect(int playerRef, ArrayList<Integer> entranceStudent, ArrayList<Integer> cardStudent){
        if(playerManager.checkStudentsEntrance(entranceStudent, playerRef) && special.checkStudents(cardStudent)) {
            for (int i = 0; i < entranceStudent.size(); i++) {
                special.effect(cardStudent.get(i), entranceStudent.get(i));
                playerManager.setStudentEntrance(playerRef, cardStudent.get(i));
                try { playerManager.removeStudentEntrance(playerRef, entranceStudent.get(i));
                }catch (NotAllowedException notAllowedException){ return false; }
            }
            return true;
        }
        return false;
    }

    @Override
    public int getStudents(int color){return special.getStudent(color);}
    @Override
    public int getCost(){ return special.getCost(); }
    @Override
    public void increaseCost(){ special.increaseCost(); }
    @Override
    public String getName(){ return special.getName(); }

    private class Special7 extends Special {
        int[] students = {0,0,0,0,0};

        public Special7(){ super(1, "special7"); }

        public void setup(int[] color){
            for(int i=0; i<5;i++)
                students[i]+=color[i];
        }
        public int getStudent(int color){ return students[color]; }

        @Override
        public void effect(int chosen, int extracted) {
            if(getStudent(chosen)>0) {
                students[chosen]--;
                students[extracted]++;
            }
        }

        public boolean checkStudents(ArrayList<Integer> students){
            for(int i=0; i<students.size(); i++)
                if(getStudent(students.get(i))==0) return false;
            return true;
        }
    }
}