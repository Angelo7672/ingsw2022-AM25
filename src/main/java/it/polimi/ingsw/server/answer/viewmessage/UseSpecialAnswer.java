package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

public class UseSpecialAnswer implements Answer {
    private final int specialIndex;
    private final String nickname;
    private final int playerRef;

    public UseSpecialAnswer(String nickname, int specialIndex, int playerRef) {
        this.nickname = nickname;
        this.specialIndex = specialIndex;
        this.playerRef = playerRef;
    }

    public int getSpecialIndex() { return specialIndex; }
    public String getNickname() { return nickname; }

    public int getPlayerRef() {
        return playerRef;
    }
}