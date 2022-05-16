package it.polimi.ingsw.server.Answer;

public class LoginMessage implements Answer{
    private final String message;
    private final int numberOfPlayer;
    private final boolean expertMode;
    private final String team;

    public LoginMessage(int numberOfPlayer, boolean expertMode, String team){
        this.message = "Login!";
        this.numberOfPlayer = numberOfPlayer;
        this.expertMode = expertMode;
        this.team = team;
    }

    @Override
    public String getMessage() {
        return message;
    }
    public int getNumberOfPlayer() { return numberOfPlayer; }
    public boolean isExpertMode() { return expertMode; }
    public String getTeam() { return team; }
}
