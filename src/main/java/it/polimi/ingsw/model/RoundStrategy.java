package it.polimi.ingsw.model;

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

    private Assistant stringToAssistant(String string){
        if(string.equalsIgnoreCase("LION")) return Assistant.LION;
        else if(string.equalsIgnoreCase("GOOSE")) return Assistant.GOOSE;
        else if(string.equalsIgnoreCase("CAT")) return Assistant.CAT;
        else if(string.equalsIgnoreCase("EAGLE")) return Assistant.EAGLE;
        else if(string.equalsIgnoreCase("FOX")) return Assistant.FOX;
        else if(string.equalsIgnoreCase("LIZARD")) return Assistant.LIZARD;
        else if(string.equalsIgnoreCase("OCTOPUS")) return Assistant.OCTOPUS;
        else if(string.equalsIgnoreCase("DOG")) return Assistant.DOG;
        else if(string.equalsIgnoreCase("ELEPHANT")) return Assistant.ELEPHANT;
        else if(string.equalsIgnoreCase("TURTLE")) return Assistant.TURTLE;
        return Assistant.NONE;
    }

    private String toString(Team team){
        if(team.equals(Team.WHITE)) return "WHITE";
        else if(team.equals(Team.BLACK)) return "BLACK";
        else if(team.equals(Team.GREY)) return "GREY";
        return "NONE";
    }

    protected boolean conquestIsland(int islandRef, int noColor, int playerRef){
        Team teamStronger = highInfluenceTeam(islandRef, noColor, playerRef);
        Team teamWeaker = Team.NONE;
        int[] infoTower;    //in the first cell there is the number of towers built, in the second there is the previous owner of the towers
        boolean victory1 = false, victory2 = false;

        if(teamStronger != Team.NONE){
            infoTower = islandsManager.towerChange(islandRef,teamStronger);
            victory1 = islandsManager.checkVictory();
            victory2 = playerManager.removeTower(teamStronger,infoTower[0]);
            if(infoTower[1] == 0) teamWeaker = Team.WHITE;
            else if(infoTower[1] == 1) teamWeaker = Team.BLACK;
            else if(infoTower[1] == 2) teamWeaker = Team.GREY;
            playerManager.placeTower(teamWeaker,infoTower[0]);
        }
        if(victory1 || victory2) return true;

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

    public boolean moveMotherNature(int queueRef, int desiredMovement, int ref) {
        int maxMovement;
        boolean victory = false;

        maxMovement = queueManager.readMaxMotherNatureMovement(queueRef);
        if (desiredMovement > 0 && desiredMovement <= maxMovement) {
            islandsManager.moveMotherNature(desiredMovement);
            if (islandsManager.getInhibited(islandsManager.getMotherPos())>0) {
                islandsManager.decreaseInhibited(islandsManager.getMotherPos());
            } else {
                int playerRef = readQueue(queueRef);
                victory = conquestIsland(islandsManager.getMotherPos(), ref, playerRef);
            }
        }
        return victory;
    }

    public boolean refreshStudentsCloud(){  //Poi da cambiare e mettere tutto in cloudManager
        boolean lastTurn = false;   //if true, the students are finished

        if(numberOfPlayer == 2 || numberOfPlayer ==4 ) {
            for (int j = 0; j < numberOfPlayer && !lastTurn; j++) {
                for (int i = 0; i < 3; i++) {
                    cloudsManager.refreshCloudStudents(bag.extraction(), j);
                    lastTurn = bag.checkVictory();
                }
            }
        } else if(numberOfPlayer==3) {
            for (int j = 0; j < numberOfPlayer && !lastTurn; j++) {
                for (int i = 0; i < 4; i++) {
                    cloudsManager.refreshCloudStudents(bag.extraction(), j);
                    lastTurn = bag.checkVictory();
                }
            }
        }
        return lastTurn;
    }

    public void queueForPlanificationPhase(){ queueManager.queueForPlanificationPhase(); }
    public boolean playCard(int playerRef, int queueRef, String card) throws NotAllowedException {
        return queueManager.playCard(playerRef, queueRef, stringToAssistant(card));
    }

    public void inOrderForActionPhase(){ queueManager.inOrderForActionPhase(); }

    public int readQueue(int pos){ return queueManager.readQueue(pos); }

    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) throws NotAllowedException{
        if(!inSchool){
            playerManager.transferStudent(playerRef, colour, inSchool, false);
            islandsManager.incStudent(islandRef,colour);
        }
        else if(inSchool)
            playerManager.transferStudent(playerRef, colour, inSchool, false);
    }

    public String oneLastRide(){ return toString(playerManager.checkVictory()); }

    public void chooseCloud(int playerRef,int cloudRef) throws NotAllowedException {
        int[] students;

        students = cloudsManager.removeStudents(cloudRef);
        for(int i = 0; i < 5 ; i++) playerManager.setStudentEntrance(playerRef,students[i]);
    }
    public void effect(){}
    public boolean effect(int ref){return false;}
    public boolean effect(int ref, int color){return false;}
    public boolean effect(int ref, ArrayList<Integer> color1, ArrayList<Integer> color2){ return false; }
    public int getStudents(int color){return -1;}

    abstract public int getCost();
    abstract public void increaseCost();
    abstract public String getName();
}