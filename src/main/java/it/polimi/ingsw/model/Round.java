package it.polimi.ingsw.model;

public class Round {
    private CloudsManager cloudsManager;
    private IslandsManager islandsManager;
    private PlayerManager playerManager;

    public Round(int numberOfPlayer, String[] playersInfo){
        this.cloudsManager = new CloudsManager(numberOfPlayer);
        this.islandsManager = new IslandsManager();
        this.playerManager = new PlayerManager(numberOfPlayer, playersInfo);
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
