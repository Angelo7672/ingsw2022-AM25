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
        //this.islandsManager = new IslandsManager(studentsForIsland);
        this.playerManager = new PlayerManager(numberOfPlayer, playersInfo);
        this.numberOfPlayer = numberOfPlayer;
    }

    public Assistant stringToAssistant(String string){
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

    public void playCard(int playerRef, String card){ playerManager.playCard(playerRef,stringToAssistant(card)); }
    public void inOrderForActionPhase(){ playerManager.inOrderForActionPhase(); }
    public int readQueue(int pos){ return playerManager.readQueue(pos); }
    public void queueForPlanification(){ playerManager.queueForPlanification(); }

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

    public void fillEntrance(int playerRef,int cloudRef){
        for(int i = 0; i < 5 ; i++){
            playerManager.setStudentEntrance(playerRef, cloudsManager.removeStudents(cloudRef)[i]);
        }
    };


    private void highInfluence(){
        //playerManager.getProfessor

    }

    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef){
        if(!inSchool){
            playerManager.transferStudent(playerRef, colour, inSchool);
            islandsManager.incStudent(islandRef,colour);
        }
        else if(inSchool)
            playerManager.transferStudent(playerRef, colour, inSchool);
    }
}
