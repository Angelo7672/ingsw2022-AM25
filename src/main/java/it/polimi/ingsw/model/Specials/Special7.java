package it.polimi.ingsw.model.Specials;

import java.util.ArrayList;

public class Special7 extends Special{

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
