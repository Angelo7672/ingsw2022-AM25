package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

public class UseSpecialAnswer implements Answer {
    private final int specialIndex;
    private final String nickname;

    public UseSpecialAnswer(String nickname, int specialIndex) {
        this.nickname = nickname;
        this.specialIndex = specialIndex;
    }

    public int getSpecialIndex() { return specialIndex; }
    public String getNickname() { return nickname; }
}