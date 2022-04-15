package it.polimi.ingsw.model.Specials;

import java.util.ArrayList;

//uno studente scelto va nella sala, poi gli viene dato uno estratto
public class Special11 extends Special{

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
