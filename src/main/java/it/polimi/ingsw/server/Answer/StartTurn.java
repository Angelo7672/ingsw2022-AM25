package it.polimi.ingsw.server.Answer;

public class StartTurn implements Answer{
    public final String currentPlayer;
    private final String message;

    public StartTurn(String currentPlayer, String message) {
        this.currentPlayer = currentPlayer;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
