package it.polimi.ingsw.model;

public class Round {
    private CloudsManager cloudsManager;
    private IslandsManager islandsManager;
    private PlayerManager playerManager;
    private Bag bag;
    private int numberOfPlayer;
    private int[] professorPropriety;

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
        this.numberOfPlayer = numberOfPlayer;
        this.professorPropriety = new int[]{-1,-1,-1,-1,-1};    //-1 indicates that no one owns that professor
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

    public void fillEntrance(int playerRef,int cloudRef){
        for(int i = 0; i < 5 ; i++){
            playerManager.setStudentEntrance(playerRef, cloudsManager.removeStudents(cloudRef)[i]);
        }
    };


    private void highInfluence(){
        //playerManager.getProfessor

    }

    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef){
        int studentTableofThisColour = -1;
        int i;
        boolean stop = false;

        if(!inSchool){
            playerManager.transferStudent(playerRef, colour, inSchool);
            islandsManager.incStudent(islandRef,colour);
        }
        else if(inSchool)
            playerManager.transferStudent(playerRef, colour, inSchool);
            studentTableofThisColour = playerManager.getStudentTable(playerRef,colour);
            for(i = 0; i < numberOfPlayer && !stop; i++){
                if(i != playerRef && studentTableofThisColour < playerManager.getStudentTable(i,colour)) stop = true;  //if it finds someone with more students at the table it stops
                else if(i != playerRef && playerManager.getProfessor(i,colour)){
                    playerManager.removeProfessor(i,colour);    //otherwise check if the other had the professor
                    playerManager.setProfessor(playerRef,colour);
                    professorPropriety[colour] = playerRef;
                    stop = true;
                }
            }
            if(i == numberOfPlayer){    //if no one owned that professor
                playerManager.setProfessor(playerRef,colour);
                professorPropriety[colour] = playerRef;
            }
    }

    public void playCard(int playerRef,int cardRef){
        //togli da hand la carta e aggiorna la queue
    }
}
