package it.polimi.ingsw.server.Answer;

public class StartTurn implements Answer{
    private final String currentPlayer;

    public StartTurn(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public Object getMessage() {
        return currentPlayer;
    }
}
