package it.polimi.ingsw.model;

public class Round {
    private CloudsManager cloudsManager;
    private IslandsManager islandsManager;
    private PlayerManager playerManager;
    private Bag bag;
    private int numberOfPlayer;

    public Round(int numberOfPlayer, String[] playersInfo){
        int[] studentsForIsland = new int[9];

        this.bag = new Bag();
        this.cloudsManager = new CloudsManager(numberOfPlayer);
        refreshStudentsCloud(numberOfPlayer);
        for(int i = 0; i < 10; i++){
            studentsForIsland[i] = bag.extraction();
        }
        this.islandsManager = new IslandsManager(studentsForIsland);
        this.playerManager = new PlayerManager(numberOfPlayer, playersInfo);
        if(numberOfPlayer == 2 || numberOfPlayer == 4){
            for(int j = 0; j < numberOfPlayer; j++)
                for (int i = 0; i < 7; i++) playerManager.setStudentEntrance(j,bag.extraction());
        }else if(numberOfPlayer == 3){
            for(int j = 0; j < numberOfPlayer; j++)
                for (int i = 0; i < 9; i++) playerManager.setStudentEntrance(j,bag.extraction());
        }
        this.numberOfPlayer = numberOfPlayer;
    }

    private Assistant stringToAssistant(String string){
        if(string.equals("LION")) return Assistant.LION;
        else if(string.equals("GOOSE")) return Assistant.GOOSE;
        else if(string.equals("CAT")) return Assistant.CAT;
        else if(string.equals("EAGLE")) return Assistant.EAGLE;
        else if(string.equals("FOX")) return Assistant.FOX;
        else if(string.equals("LIZARD")) return Assistant.LIZARD;
        else if(string.equals("OCTOPUS")) return Assistant.OCTOPUS;
        else if(string.equals("DOG")) return Assistant.DOG;
        else if(string.equals("ELEPHANT")) return Assistant.ELEPHANT;
        else if(string.equals("TURTLE")) return Assistant.TURTLE;
        return Assistant.NONE;
    }

    public void refreshStudentsCloud(int numberOfPlayer){
        if(numberOfPlayer == 2 || numberOfPlayer ==4 ) {
            for (int j = 0; j < numberOfPlayer; j++) {
                for (int i = 0; i < 3; i++) {
                    cloudsManager.refreshCloudStudents(bag.extraction(), j);
                }
            }
        } else if(numberOfPlayer==3) {
            for (int j = 0; j < numberOfPlayer; j++) {
                for (int i = 0; i < 4; i++) {
                    cloudsManager.refreshCloudStudents(bag.extraction(), j);
                }
            }
        }
    }

    public void queueForPlanificationPhase(){ playerManager.queueForPlanificationPhase(numberOfPlayer); }
    public void playCard(int playerRef, String card){ playerManager.playCard(playerRef,stringToAssistant(card)); }
    public void inOrderForActionPhase(){ playerManager.inOrderForActionPhase(); }
    public int readQueue(int pos){ return playerManager.readQueue(pos); }

    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef){
        if(!inSchool){
            playerManager.transferStudent(playerRef, colour, inSchool);
            islandsManager.incStudent(islandRef,colour);
        }
        else if(inSchool)
            playerManager.transferStudent(playerRef, colour, inSchool);
    }

    public boolean moveMotherNature(int queueRef, int desiredMovement){
        int maxMovement;
        boolean victory = false;

        maxMovement = playerManager.readMaxMotherNatureMovement(queueRef);
        if(desiredMovement > 0 && desiredMovement <= maxMovement ){
            islandsManager.moveMotherNature(desiredMovement);
            victory= conquestIsland(islandsManager.getMotherPos());
        }
        return victory;
    }
    private boolean conquestIsland(int islandRef){
        Team teamStronger = highInfluenceTeam();
        Team teamWeaker = Team.NOONE;
        int[] infoTower;    //in the first cell there is the number of towers built, in the second there is the previous owner of the towers
        boolean victory1 = false, victory2 = false;

        if(highInfluenceTeam() != Team.NOONE){
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
    private Team highInfluenceTeam() {
        int[] studentOnIsland = new int[5];
        int professorOwner;
        Team teamOwner;
        int influenceTeamWHITE = 0;
        int influenceTeamBLACK = 0;
        int influenceTeamGREY = 0;

        for (int i = 0; i < 5; i++) {
            studentOnIsland[i] = islandsManager.getStudent(islandsManager.getMotherPos(), i);
            if (studentOnIsland[i] > 0) {
                professorOwner = playerManager.getProfessorPropriety(i);
                if (professorOwner != -1) {
                    teamOwner = playerManager.getTeam(professorOwner);
                    if (teamOwner.equals(Team.WHITE)) influenceTeamWHITE += studentOnIsland[i];
                    else if (teamOwner.equals(Team.BLACK)) influenceTeamBLACK += studentOnIsland[i];
                    else if (teamOwner.equals(Team.GREY)) influenceTeamGREY += studentOnIsland[i];
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

    public void fillEntrance(int playerRef,int cloudRef){
        for(int i = 0; i < 5 ; i++) playerManager.setStudentEntrance(playerRef, cloudsManager.removeStudents(cloudRef)[i]);
    }
}