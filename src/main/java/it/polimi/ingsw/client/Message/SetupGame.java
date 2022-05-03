package it.polimi.ingsw.client.Message;

public class SetupGame implements Message{
    private final int playersNumber;
    private boolean expertMode;

    public SetupGame(int playersNumber, boolean expertMode) {
        this.playersNumber = playersNumber;
        this.expertMode = expertMode;
    }
    public int getPlayersNumber(){
        return playersNumber;
    }
    public boolean getExpertMode(){
        return expertMode;
    }

}
