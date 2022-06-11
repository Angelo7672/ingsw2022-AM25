package it.polimi.ingsw.server.answer;

public class GameOverAnswer implements Answer{
    private String winner;

    public GameOverAnswer(String winner) { this.winner = winner; }

    public String getWinner() { return winner; }
}
