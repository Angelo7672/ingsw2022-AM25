package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.Special7;
import it.polimi.ingsw.model.Specials.Special9;

import java.util.ArrayList;

public class RoundSpecial9 extends RoundStrategy{

    Special9 special;

    public RoundSpecial9(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer,playersInfo);
        special = new Special9();
    }

    @Override
    protected Team highInfluenceTeam(int islandRef, int noColor, int playerRef) {
        int[] studentOnIsland = new int[5];
        int professorOwner;
        Team teamOwnerProfessor, teamOwnerTower;
        int influenceTeamWHITE = 0;
        int influenceTeamBLACK = 0;
        int influenceTeamGREY = 0;

        for (int i = 0; i < 5; i++) {
            if(i==noColor){
                if(useSpecial(special.getCost(), playerRef)) {
                    i++; //salta il colore bannato
                }
            }
            studentOnIsland[i] = islandsManager.getStudent(islandRef, i);
            if (studentOnIsland[i] > 0) {
                professorOwner = playerManager.getProfessorPropriety(i);
                if (professorOwner != -1) {
                    teamOwnerProfessor = playerManager.getTeam(professorOwner);
                    if (teamOwnerProfessor.equals(Team.WHITE)) influenceTeamWHITE += studentOnIsland[i];
                    else if (teamOwnerProfessor.equals(Team.BLACK)) influenceTeamBLACK += studentOnIsland[i];
                    else if (teamOwnerProfessor.equals(Team.GREY)) influenceTeamGREY += studentOnIsland[i];
                }
            }
        }
        teamOwnerTower = islandsManager.getTowerTeam(islandRef);
        if(teamOwnerTower.equals(Team.WHITE)) influenceTeamWHITE += islandsManager.getTowerValue(islandRef);
        else if(teamOwnerTower.equals(Team.BLACK)) influenceTeamBLACK += islandsManager.getTowerValue(islandRef);
        else if(teamOwnerTower.equals(Team.GREY)) influenceTeamGREY += islandsManager.getTowerValue(islandRef);

        if (influenceTeamBLACK < influenceTeamWHITE) {
            if (influenceTeamGREY < influenceTeamWHITE) return Team.WHITE;
        } else if (influenceTeamWHITE < influenceTeamBLACK) {
            if (influenceTeamGREY < influenceTeamBLACK) return Team.BLACK;
        } else if (influenceTeamWHITE < influenceTeamGREY){
            if (influenceTeamBLACK < influenceTeamGREY) return Team.GREY;
        }
        return Team.NOONE;
    }
}
