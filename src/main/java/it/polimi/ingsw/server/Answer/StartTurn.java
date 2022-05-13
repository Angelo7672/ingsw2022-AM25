package it.polimi.ingsw.server.Answer;

public class StartTurn implements Answer{
    private final String currentPlayer;
    private final String message;

    public StartTurn(String currentPlayer, String message) {
        this.currentPlayer = currentPlayer;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }
}
