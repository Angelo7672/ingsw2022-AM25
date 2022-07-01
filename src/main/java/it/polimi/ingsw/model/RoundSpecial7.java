package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.SpecialStudentsListener;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.ArrayList;

/**
 * Round strategy used by special 1
 */
public class RoundSpecial7 extends RoundStrategy{
    private Special7 special;
    protected SpecialStudentsListener specialStudentsListener;

    public RoundSpecial7(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        special = new Special7();
    }

    @Override
    public void setSpecialStudentsListener(SpecialStudentsListener specialStudentsListener) { this.specialStudentsListener = specialStudentsListener; }

    @Override
    public void initializeSpecial(){
        int[] extraction = {0,0,0,0,0};
        int extracted;

        for(int i = 0; i < 6; i++) {
            extracted = bag.extraction();
            extraction[extracted]++;
        }
        special.setup(extraction);
    }

    @Override
    public void restoreStudentSpecial(int[] students){ special.specialStudentRestore(students); }

    /**
     * It checks if effect could be used, then use it.
     * @param playerRef is the number of the player.
     * @param entranceStudent is the students from the entrance.
     * @param cardStudent is the students from the card.
     * @return true if effect is used.
     */
    @Override
    public boolean effect(int playerRef, ArrayList<Integer> entranceStudent, ArrayList<Integer> cardStudent){
        if(playerManager.checkStudentsEntranceForSpecial(entranceStudent, playerRef) && special.checkStudents(cardStudent)
            && entranceStudent.size()==cardStudent.size()) {
            for (int i = 0; i < entranceStudent.size(); i++) {
                try { playerManager.removeStudentEntrance(playerRef, entranceStudent.get(i));
                } catch (NotAllowedException notAllowedException){ return false; }
                special.effect(cardStudent.get(i), entranceStudent.get(i)); //cardStudent.get(i) is the student I remove, entranceStudent.get(i) is the which one I add
                specialStudentsListener.specialStudentsNotify(7, cardStudent.get(i), getStudents(cardStudent.get(i)));
                specialStudentsListener.specialStudentsNotify(7, entranceStudent.get(i), getStudents(entranceStudent.get(i)));
                playerManager.setStudentEntrance(playerRef, cardStudent.get(i), 1);
            }
            return true;
        }
        return false;
    }

    @Override
    public void setCost(int cost){ special.setCost(cost); }
    @Override
    public int getCost(){ return special.getCost(); }
    @Override
    public void increaseCost(){ special.increaseCost(); }

    private int getStudents(int color){ return special.getStudent(color); }

    private class Special7 extends Special {
        int[] students = {0,0,0,0,0};

        public Special7(){ super(1); }

        public void setup(int[] color){
            for(int i=0; i<5;i++){
                students[i] = color[i];
                specialStudentsListener.specialStudentsNotify(7, i, students[i]);
            }
        }

        public void specialStudentRestore(int[] students){
            for(int i = 0; i < 5; i++){
                this.students[i] = students[i];
                specialStudentsListener.specialStudentsNotify(7, i, this.students[i]);
            }
        }

        public int getStudent(int color){ return students[color]; }

        @Override
        public void effect(int chosen, int extracted) {
            students[chosen]--;
            students[extracted]++;
        }

        /**
         * Check if card have enough student to use the effect.
         */
        public boolean checkStudents(ArrayList<Integer> cardStudents){
            int[] tempStudent = {0,0,0,0,0};
            for (int i = 0; i < cardStudents.size(); i++) {
                tempStudent[cardStudents.get(i)]++;
            }
            for(int i=0; i<5; i++)
                if(tempStudent[i]>students[i]) return false;
            return true;
        }
    }
}