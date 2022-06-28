package it.polimi.ingsw.server.answer;

/**
 * StartTurnAnswer invite client to procede with actionPhase.
 */
public class StartTurnAnswer implements Answer{
    private final String message;

    public StartTurnAnswer() {
        this.message = "Start your Action Phase!";
    }

    public String getMessage() {
        return message;
    }
}