package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.SpecialStudentsListener;

/**
 * Round strategy used by special 1
 */
public class RoundSpecial1 extends RoundStrategy{
    private Special1 special;
    protected SpecialStudentsListener specialStudentsListener;
    
    public RoundSpecial1(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        this.special = new Special1();
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

    /**
     * It checks if effect could be used, then use it.
     * @param islandRef is the island's number.
     * @param color is the student's color.
     * @return true if effect is used.
     */
    @Override
    public boolean effect(int islandRef, int color){
        int extracted = -1;
        if(color < 5 && color > -1 && getStudents(color) > 0 && getStudents(color) > 0){
            if(!bag.checkVictory()) extracted = bag.extraction();
            special.effect(color, extracted);   //color is the student I remove, extracted is the one I add
            specialStudentsListener.specialStudentsNotify(1, color, getStudents(color));
            if(!bag.checkVictory()) specialStudentsListener.specialStudentsNotify(1, extracted, getStudents(extracted));
            islandsManager.incStudent(islandRef, color, 1);
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

    private class Special1 extends Special {
        private int[] students= {0,0,0,0,0};

        private Special1(){
            super(1);
        }

        public void setup(int[] color){
            for(int i = 0; i < 5; i++){
                students[i] = color[i];
                specialStudentsListener.specialStudentsNotify(1, i, students[i]);
            }
        }

        public void specialStudentRestore(int[] students){
            for(int i = 0; i < 5; i++){
                this.students[i] = students[i];
                specialStudentsListener.specialStudentsNotify(1, i, this.students[i]);
            }
        }

        private int getStudent(int color){ return students[color]; }

        @Override
        public void effect(int chosen, int extracted) {
            if(getStudent(chosen) > 0) {
                students[chosen]--;
                if(extracted != -1) students[extracted]++;
            }
            for(int i = 0; i < 5; i++)
                specialStudentsListener.specialStudentsNotify(1, i, students[i]);
        }
    }
}