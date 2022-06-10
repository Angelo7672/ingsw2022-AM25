package it.polimi.ingsw.server.expertmode;

import java.util.ArrayList;
import java.util.List;

public class ExpertGame extends Thread{
    private boolean special1,special2,special3,special4,special5,special6,
                    special7,special8,special9,special10,special11,special12;
    private List<Boolean> specialOfThisMatch;

    public ExpertGame(ArrayList<Integer> extractedSpecial){ //voglio che extractedSpecial ordinato
        this.specialOfThisMatch = new ArrayList<>();

        for(Integer special:extractedSpecial){
            switch (special){
                case 1:


            }
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
