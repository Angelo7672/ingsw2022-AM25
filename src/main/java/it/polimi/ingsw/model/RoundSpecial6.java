package it.polimi.ingsw.model;

public class RoundSpecial6 extends RoundStrategy{

    Special6 special;

    public RoundSpecial6(int numberOfPlayer, String[] playersInfo, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, Bag bag){
        super(numberOfPlayer,playersInfo, cloudsManager, islandsManager, playerManager, bag);
        special = new Special6();
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

        if (influenceTeamBLACK < influenceTeamWHITE) {
            if (influenceTeamGREY < influenceTeamWHITE) return Team.WHITE;
        } else if (influenceTeamWHITE < influenceTeamBLACK) {
            if (influenceTeamGREY < influenceTeamBLACK) return Team.BLACK;
        } else if (influenceTeamWHITE < influenceTeamGREY){
            if (influenceTeamBLACK < influenceTeamGREY) return Team.GREY;
        }
        return Team.NOONE;
    }

    @Override
    public int getCost(){
        return special.getCost();
    }
    @Override
    public void increaseCost(){
        special.increaseCost();
    }
    @Override
    public String getName(){
        return special.getName();
    }


    private class Special6 extends Special {

        public Special6(){
            super(3, "special6");
        }

    }

}