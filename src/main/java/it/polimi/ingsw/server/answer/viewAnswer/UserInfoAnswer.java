package it.polimi.ingsw.server.answer.viewAnswer;

import it.polimi.ingsw.server.answer.Answer;

/**
 * UserInfoAnswer contains info about a player.
 */
public class UserInfoAnswer implements Answer {
    private final int playerRef;
    private final String nickname;
    private final String character;

    /**
     * Create an answer contains info about a player.
     * @param playerRef player reference;
     * @param nickname nickname chosen;
     * @param character character chosen;
     */
    public UserInfoAnswer(int playerRef, String nickname, String character){
        this.playerRef = playerRef;
        this.nickname = nickname;
        this.character = character;
    }
    public int getPlayerRef() { return playerRef; }
    public String getNickname() { return nickname; }
    public String getCharacter() { return character; }
}
