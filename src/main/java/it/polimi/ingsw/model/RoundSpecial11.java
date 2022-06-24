package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.SpecialStudentsListener;
import it.polimi.ingsw.model.exception.NotAllowedException;

public class RoundSpecial11 extends RoundStrategy{
    private Special11 special;
    protected SpecialStudentsListener specialStudentsListener;

    public RoundSpecial11(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        special = new Special11();
    }

    @Override
    public void initializeSpecial(){
        int[] extraction = {0,0,0,0,0};
        int extracted;

        for(int i = 0; i < 4; i++) {
            extracted = bag.extraction();
            extraction[extracted]++;
        }
        special.setup(extraction);
    }

    @Override
    public void restoreStudentSpecial(int[] students){ special.specialStudentRestore(students); }

    @Override
    public boolean effect(int playerRef, int color){
        if(getStudents(color) > 0) {
            try { playerManager.setStudentTable(playerRef, color, 1);
            } catch (NotAllowedException notAllowedException){ return false; }
            int extracted = bag.extraction();
            special.effect(color, extracted);   //color is the student I remove, extracted is the which one I add
            specialStudentsListener.specialStudentsNotify(11, color, getStudents(color));
            specialStudentsListener.specialStudentsNotify(11, extracted, getStudents(extracted));
            return true;
        }
        return false;
    }

    @Override
    public void setSpecialStudentsListener(SpecialStudentsListener specialStudentsListener) { this.specialStudentsListener = specialStudentsListener; }

    private int getStudents(int color){ return special.getStudent(color); }

    @Override
    public void setCost(int cost){ special.setCost(cost); }
    @Override
    public int getCost(){ return special.getCost(); }
    @Override
    public void increaseCost(){ special.increaseCost(); }

    private class Special11 extends Special {
        private int[] students= {0,0,0,0,0};

        public Special11(){
            super(2, "special11");
        }

        public void setup(int[] color){
            for(int i = 0; i < 5; i++){
                students[i] = color[i];
                specialStudentsListener.specialStudentsNotify(11, i, students[i]);
            }
        }

        public void specialStudentRestore(int[] students){
            for(int i = 0; i < 5; i++){
                this.students[i] = students[i];
                specialStudentsListener.specialStudentsNotify(11, i, this.students[i]);
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
    }
}