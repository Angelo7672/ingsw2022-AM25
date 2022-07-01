package it.polimi.ingsw.server.answer;

/**
 * PlayCardAnswer invite client to procede with PlanningPhase.
 */
public class PlayCardAnswer implements Answer{
    private final String message;

    /**
     * Create an answer contains the message "Play card!".
     */
    public PlayCardAnswer() { this.message = "Play card!"; }

    public String getMessage() { return message; }
}

