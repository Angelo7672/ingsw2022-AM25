package it.polimi.ingsw.model.Specials;

import java.util.ArrayList;
import java.util.Collections;

public class SpecialsManager {

    SpecialFactory specialFactory;
    ArrayList<Special> specials;
    //Special special1, special2, special3;

    public SpecialsManager(){
        specials = new ArrayList<>();
        specialFactory=new SpecialFactory();
        ArrayList<Integer> random = new ArrayList<>();
        for(int i=1; i<=12; i++) random.add(i); //riempio con numeri da 1 a 12
        Collections.shuffle(random); //mischio i numeri
        for(int i=0; i<3; i++) specials.add(specialFactory.getSpecial(random.get(i))); //aggiungo lo special estratto
    }

    /*public void effect(int pos){
       specials.get(pos).effect();
    }*/

    public int getCost(int special){
        return specials.get(special).getCost();
    }
    public void increaseCost(int special){
        specials.get(special).increaseCost();
    }
    public void print(){
        for (int i=0; i<3; i++){
            System.out.println("specialista "+(i+1)+" "+specials.get(i).getName()+ " costa: "+specials.get(i).getCost());
        }
    }

}
