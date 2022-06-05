package it.polimi.ingsw.server.Answer;

public class GameOverAnswer implements Answer{
    private String winner;

    public GameOverAnswer(String winner) { this.winner = winner; }

    public String getWinner() { return winner; }
}
