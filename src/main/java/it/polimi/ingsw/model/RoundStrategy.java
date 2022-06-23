package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.NoEntryListener;
import it.polimi.ingsw.listeners.SpecialStudentsListener;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.ArrayList;

public abstract class RoundStrategy {
    public CloudsManager cloudsManager;
    public IslandsManager islandsManager;
    public PlayerManager playerManager;
    public QueueManager queueManager;
    public Bag bag;
    public int numberOfPlayer;

    public RoundStrategy(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        this.numberOfPlayer=numberOfPlayer;
        this.bag = bag;
        this.cloudsManager = cloudsManager;
        this.islandsManager = islandsManager;
        this.playerManager = playerManager;
        this.queueManager = queueManager;
    }

    public void initializeSpecial(){}
    public void setSpecialStudentsListener(SpecialStudentsListener specialStudentsListener){}
    public void setNoEntryListener(NoEntryListener noEntryListener){}

    protected boolean conquestIsland(int islandRef, int noColor, int playerRef){
        Team teamStronger = highInfluenceTeam(islandRef, noColor, playerRef);
        Team teamWeaker = Team.NONE;
        int[] infoTower;    //in the first cell there is the number of towers built, in the second there is the previous owner of the towers
        boolean victory1 = false, victory2 = false;

        if(teamStronger != Team.NONE){
            infoTower = islandsManager.towerChange(islandRef,teamStronger);
            victory1 = islandsManager.checkVictory();
            victory2 = playerManager.removeTower(teamStronger, infoTower[0]);
            if(infoTower[1] == 0) teamWeaker = Team.WHITE;
            else if(infoTower[1] == 1) teamWeaker = Team.BLACK;
            else if(infoTower[1] == 2) teamWeaker = Team.GREY;
            playerManager.placeTower(teamWeaker,infoTower[0]);
        }
        if(victory1 || victory2){ System.out.println("conquestIsland - EndGame");return true; }

        return false;
    }
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
        return Team.NONE;
    }

    public boolean moveMotherNature(int queueRef, int desiredMovement, int ref) throws NotAllowedException {
        int maxMovement;
        boolean victory = false;

        maxMovement = queueManager.readMaxMotherNatureMovement(queueRef);

        if (desiredMovement > 0 && desiredMovement <= maxMovement) {
            islandsManager.moveMotherNature(desiredMovement);
            if (islandsManager.getInhibited(islandsManager.getMotherPos()) > 0) {   //always check if the island is inhibited
                islandsManager.decreaseInhibited(islandsManager.getMotherPos());
            } else {
                int playerRef = queueManager.readQueue(queueRef);    //check if player can conquest the island
                victory = conquestIsland(islandsManager.getMotherPos(), ref, playerRef);    //the game could finish with this command
            }
        } else throw new NotAllowedException();

        return victory;
    }

    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) throws NotAllowedException{ //da cambiare e spezzare in due
        if(!inSchool){
            if(islandRef < 0 || islandRef >= islandsManager.getIslandsSize()) throw new NotAllowedException();
            playerManager.transferStudent(playerRef, colour, inSchool, false);
            islandsManager.incStudent(islandRef,colour,1);
        }
        else if(inSchool)
            playerManager.transferStudent(playerRef, colour, inSchool, false);
    }

    public void effect(){}
    public boolean effect(int ref){ return false; }
    public boolean effect(int ref, int color){ return false; }
    public boolean effect(int ref, ArrayList<Integer> color1, ArrayList<Integer> color2){ return false; }

    public int getNoEntry(){ return -1; }

    abstract public int getCost();
    abstract public void setCost(int cost);
    abstract public void increaseCost();
    abstract public String getName();
}