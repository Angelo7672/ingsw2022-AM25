package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.NoEntryListener;
import it.polimi.ingsw.listeners.SpecialStudentsListener;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.ArrayList;

/**
 * Strategy for special card. Each special extends RoundStrategy and overrides his special methods.
 */
public abstract class RoundStrategy {
    public CloudsManager cloudsManager;
    public IslandsManager islandsManager;
    public PlayerManager playerManager;
    public QueueManager queueManager;
    public Bag bag;
    public int numberOfPlayer;

    public RoundStrategy(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        this.numberOfPlayer = numberOfPlayer;
        this.bag = bag;
        this.cloudsManager = cloudsManager;
        this.islandsManager = islandsManager;
        this.playerManager = playerManager;
        this.queueManager = queueManager;
    }

    public void initializeSpecial(){}
    public void setSpecialStudentsListener(SpecialStudentsListener specialStudentsListener){}
    public void setNoEntryListener(NoEntryListener noEntryListener){}
    public void restoreStudentSpecial(int[] students){}
    public void noEntryCardsRestore(int numCards){}

    /**
     * It changes towers on an island, remove it to the school's team of higher influence team and put it back in the weaker team's school.
     * @param islandRef is the number of the island.
     * @param noColor is the color of the special 9's effect.
     * @param playerRef is the number of the player.
     * @return true if game is over.
     */
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
        return victory1 || victory2;
    }

    /**
     * It counts influence on the selected island and return the team with higher influence.
     * @param islandRef is the number of the island.
     * @param noColor is the color of the special 9's effect.
     * @param playerRef is the number of the player.
     * @return the stronger team.
     */
    protected Team highInfluenceTeam(int islandRef, int noColor, int playerRef) {
        int[] studentOnIsland = new int[]{0,0,0,0,0};
        int professorOwner;
        Team teamOwnerProfessor, teamOwnerTower;
        int influenceTeamWHITE = 0;
        int influenceTeamBLACK = 0;
        int influenceTeamGREY = 0;
        Team highInfluenceTeam;

        for (int i = 0; i < 5; i++) {
            studentOnIsland[i] = islandsManager.getStudent(islandRef, i);   //add student of each color
            if (studentOnIsland[i] > 0) {
                professorOwner = playerManager.getProfessorPropriety(i);    //get professor's owner
                if (professorOwner != -1) {
                    teamOwnerProfessor = playerManager.getTeam(professorOwner); //get Team professor's owner
                    if (teamOwnerProfessor.equals(Team.WHITE)){ influenceTeamWHITE += studentOnIsland[i]; }
                    else if (teamOwnerProfessor.equals(Team.BLACK)){ influenceTeamBLACK += studentOnIsland[i]; }
                    else if (teamOwnerProfessor.equals(Team.GREY)) influenceTeamGREY += studentOnIsland[i];
                }
            }
        }
        teamOwnerTower = islandsManager.getTowerTeam(islandRef);    //get tower's team owner
        if(teamOwnerTower.equals(Team.WHITE)) influenceTeamWHITE += islandsManager.getTowerValue(islandRef);
        else if(teamOwnerTower.equals(Team.BLACK)) influenceTeamBLACK += islandsManager.getTowerValue(islandRef);
        else if(teamOwnerTower.equals(Team.GREY)) influenceTeamGREY += islandsManager.getTowerValue(islandRef);

        if(numberOfPlayer == 2 || numberOfPlayer == 4)
            highInfluenceTeam = compareInfluenceTeam(Team.WHITE, influenceTeamWHITE, Team.BLACK, influenceTeamBLACK);
        else {
            highInfluenceTeam = compareInfluenceTeam(Team.WHITE, influenceTeamWHITE, Team.BLACK, influenceTeamBLACK);
            if(highInfluenceTeam.equals(Team.WHITE))
                highInfluenceTeam = compareInfluenceTeam(highInfluenceTeam, influenceTeamWHITE, Team.GREY, influenceTeamGREY);
            else highInfluenceTeam = compareInfluenceTeam(Team.BLACK, influenceTeamBLACK, Team.GREY, influenceTeamGREY);
        }

        return highInfluenceTeam;
    }

    /**
     * Compare the influence of teams and return the highest.
     * @param team1 First team.
     * @param influenceTeam1 number of influence of team 1.
     * @param team2 Second team.
     * @param influenceTeam2 number of influence of team 2.
     * @return the stronger team.
     */
    private Team compareInfluenceTeam(Team team1, int influenceTeam1, Team team2, int influenceTeam2){
        if(influenceTeam1 == 0) {
            if (influenceTeam2 != 0) return team2;
            else return Team.NONE;
        }
        if(influenceTeam2 == 0) return team1;

        if(influenceTeam1 == influenceTeam2) return Team.NONE;
        else if(influenceTeam1 > influenceTeam2) return team1;
        else return team2;
    }


    /**
     * It checks the max movement that player can choose and check if its choose is valid. Then, it moves mother nature and start conquestIsland.
     * @param queueRef is the number of the player in the queue.
     * @param desiredMovement is the number of steps.
     * @param ref is the reference to a parameter used for special.
     * @return if game is over.
     * @throws NotAllowedException if chosen steps is not valid.
     */
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


    /**
     * It checks if chosen parameters are valid. Then if player want to move it in school it transfer student from entrane to table. Else if
     * in school is false remove the student from school's entrance and put it on the chosen island.
     * @param playerRef is the number of the player.
     * @param colour is the color of the student chosen.
     * @param inSchool true if it's in school or false if it's on island.
     * @param islandRef is the number of the island.
     * @throws NotAllowedException if chosen parameters are not valid.
     */
    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) throws NotAllowedException{
        if(!inSchool){
            if(islandRef < 0 || islandRef >= islandsManager.getIslandsSize()) throw new NotAllowedException();
            playerManager.transferStudent(playerRef, colour, inSchool, false);
            islandsManager.incStudent(islandRef,colour,1);
        }
        else if(inSchool) playerManager.transferStudent(playerRef, colour, inSchool, false);
    }

    public void effect(){}
    public boolean effect(int ref){ return false; }
    public boolean effect(int ref, int color){ return false; }
    public boolean effect(int ref, ArrayList<Integer> color1, ArrayList<Integer> color2){ return false; }

    abstract public int getCost();
    abstract public void setCost(int cost);
    abstract public void increaseCost();
}