package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.Special12;

public class RoundSpecial12 extends RoundStrategy{

    public RoundSpecial12(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer, playersInfo);
        special =new Special12();
    }

    public void effect(int color){
        for(int i=0; i<numberOfPlayer; i++){
            if(playerManager.getStudentsTable(i,color)>=3){
                playerManager.decreaseStudentTable(i,color,3);
                bag.fillBag(color, 3);
            }
            else{
                bag.fillBag(color, playerManager.getStudentsTable(i,color)); //da fare prima di decrease perchè dopo non ho piu il num di studenti tolti
                playerManager.decreaseStudentTable(i,color,playerManager.getStudentsTable(i,color));
            }
        }

    }

}
