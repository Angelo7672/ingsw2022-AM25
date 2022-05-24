package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class NicknameMessage implements Answer {
    private final int playerRef;
    private final String nickname;

    public NicknameMessage(int playerRef, String nickname) {
        this.playerRef = playerRef;
        this.nickname = nickname;
    }

    @Override
    public String getMessage() {
        return nickname;
    }

    public int getPlayerRef() {
        return playerRef;
    }
}
