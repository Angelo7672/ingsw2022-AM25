package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.Special8;

import java.util.ArrayList;

public class RoundSpecial8 extends RoundStrategy{

    public RoundSpecial8(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer, playersInfo);
        special = new Special8();
    }

    @Override
    public Team highestInfluenceTeam(ArrayList<Integer> prof, int pos, int noColor, int player) {
        int inflP1 = 0, inflP2 = 0, inflP3 = 0;
        for(int i=0; i<5; i++) {
            if(islandsManager.getStudent(pos, i)>0)
                //aggiunge a chi possiede il prof di quel colore il numero di studenti
                //se aggungiamo una variabile influence ad ogni player ci evidiamo lo switch
                switch (prof.get(i)){
                    case(0): inflP1+=islandsManager.getStudent(pos, i); break;
                    case(1): inflP2+=islandsManager.getStudent(pos, i); break;
                    case(2): inflP3+=islandsManager.getStudent(pos, i); break;
                }
        }

        //sommo le torri all'influenza
        switch (islandsManager.getTowerTeam(pos).toString()){
            case("WHITE"): inflP1 += islandsManager.getTowerValue(pos); break;
            case("BLACK"): inflP2 += islandsManager.getTowerValue(pos); break;
            case("GREY"): inflP3 += islandsManager.getTowerValue(pos); break;
        }

        switch(player){
            case(0): inflP1+=2; break;
            case (1): inflP2+=2; break;
            case(2): inflP3+=2; break;
        }

        //non so se è meglio metterli dentro ad un metodo findMax questi if
        if (inflP1>inflP2 && inflP1>inflP3) return Team.WHITE;
        if (inflP2>inflP1 && inflP2>inflP3) return Team.BLACK;
        if (inflP3>inflP2 && inflP3>inflP1) return Team.GREY;
        return Team.NOONE;
    }


}
