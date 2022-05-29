package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class UserInfoAnswer implements Answer {
    private final int playerRef;
    private final String nickname;
    private final String character;

    public UserInfoAnswer(int playerRef, String nickname, String character){
        this.playerRef = playerRef;
        this.nickname = nickname;
        this.character = character;
    }

    @Override
    public String getMessage() {
        return null;
    }
    public int getPlayerRef() {
        return playerRef;
    }
    public String getNickname() { return nickname; }
    public String getCharacter() { return character; }
}
