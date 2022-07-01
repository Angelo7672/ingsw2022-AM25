package it.polimi.ingsw.server.answer;

/**
 * MoveNotAllowedAnswer said to a client that last play is not allowed by Eriantys rules.
 */
public class MoveNotAllowedAnswer implements Answer{
    private final String message;

    /**
     * Create an answer contains a message "move not allowed".
     */
    public MoveNotAllowedAnswer() {
        this.message = "move not allowed";
    }

    public String getMessage() {
        return message;
    }
}
