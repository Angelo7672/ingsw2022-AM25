package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class WizardMessage implements Answer {
    private final int playerRef;
    private final String wizard;

    public WizardMessage(int playerRef, String wizard) {
        this.playerRef = playerRef;
        this.wizard = wizard;
    }

    @Override
    public String getMessage() {
        return wizard;
    }

    public int getPlayerRef() {
        return playerRef;
    }
}
