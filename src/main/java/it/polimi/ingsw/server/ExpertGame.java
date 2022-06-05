package it.polimi.ingsw.server;

import java.util.ArrayList;
import java.util.List;

public class ExpertGame extends Thread{
    private boolean special1,special2,special3,special4,special5,special6,
                    special7,special8,special9,special10,special11,special12;
    private List<Boolean> specialOfThisMatch;

    public ExpertGame(ArrayList<Integer> extractedSpecial){
        this.specialOfThisMatch = new ArrayList<>();

        List<Boolean> tmpForPickSpecials = new ArrayList<>();
        tmpForPickSpecials.add(special1);tmpForPickSpecials.add(special2);tmpForPickSpecials.add(special3);tmpForPickSpecials.add(special4);tmpForPickSpecials.add(special5);tmpForPickSpecials.add(special6);
        tmpForPickSpecials.add(special7);tmpForPickSpecials.add(special8);tmpForPickSpecials.add(special9);tmpForPickSpecials.add(special10);tmpForPickSpecials.add(special11);tmpForPickSpecials.add(special12);
        for (int i = 0; i < 3; i++){
            this.specialOfThisMatch.add(
                    tmpForPickSpecials.get(
                            extractedSpecial.get(i)
                    )
            ); //NON FUNZIONA, MEGLIO USARE IL FACTORY
        }
    }


    private void specialCharacters(){


        if(special1){

        }else if(special2){

        }else if(special3){

        }else if(special4){

        }else if(special5){

        }else if(special6){

        }else if(special7){

        }else if(special8){

        }else if(special9){

        }else if(special10){

        }else if(special11){

        }else if(special12){

        }
    }

    public void setSpecial(){
        for(Boolean special:specialOfThisMatch)
            special = true;
    }
}
