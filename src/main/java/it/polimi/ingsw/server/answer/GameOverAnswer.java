package it.polimi.ingsw.server.answer;

/**
 * GameOverAnswer contains info about the team winner.
 */
public class GameOverAnswer implements Answer{
    private String winner;

    /**
     * Create an answer contains the Team winner.
     * @param winner team winner;
     */
    public GameOverAnswer(String winner) { this.winner = winner; }

    public String getWinner() { return winner; }
}
