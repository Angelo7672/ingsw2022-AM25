package it.polimi.ingsw.model;

public class Round {
    private CloudsManager cloudsManager;
    private IslandsManager islandsManager;
    private PlayerManager playerManager;
    private Bag bag;

    public Round(int numberOfPlayer, String[] playersInfo){
        this.bag = new Bag();
        this.cloudsManager = new CloudsManager(numberOfPlayer);
        refreshStudentsCloud(numberOfPlayer);
        this.islandsManager = new IslandsManager();
        this.playerManager = new PlayerManager(numberOfPlayer, playersInfo);
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


    public void moveStudent(int playerRef, int colour, boolean inSchool){
        if(!inSchool){
            playerManager.transferStudent(playerRef, colour, inSchool);
            //metodo di IslandsManager per mettere studente su isola
        }
        else if(inSchool)
            playerManager.transferStudent(playerRef, colour, inSchool);
    }

    public void playCard(int playerRef,int cardRef){
        //togli da hand la carta e aggiorna la queue
    }
}
