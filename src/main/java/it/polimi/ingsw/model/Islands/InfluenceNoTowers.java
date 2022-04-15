package it.polimi.ingsw.model.Islands;

import it.polimi.ingsw.model.Team;

import java.util.ArrayList;

public class InfluenceNoTowers implements InfluenceStrategy{

    @Override
    public Team highestInfluenceTeam(ArrayList<Integer> prof, IslandsManager.Island island, int noColor, int player) {
        int inflP1 = 0, inflP2 = 0, inflP3 = 0;
        for(int i=0; i<5; i++) {
            if(island.getNumStudents(i)>0)
                //aggiunge a chi possiede il prof di quel colore il numero di studenti
                //se aggungiamo una variabile influence ad ogni player ci evidiamo lo switch
                switch (prof.get(i)){
                    case(0): inflP1+=island.getNumStudents(i); break;
                    case(1): inflP2+=island.getNumStudents(i); break;
                    case(2): inflP3+=island.getNumStudents(i); break;
                }
        }

        //non so se è meglio metterli dentro ad un metodo findMax questi if
        if (inflP1>inflP2 && inflP1>inflP3) return Team.WHITE;
        if (inflP2>inflP1 && inflP2>inflP3) return Team.BLACK;
        if (inflP3>inflP2 && inflP3>inflP1) return Team.GREY;
        return Team.NOONE;
    }
}

