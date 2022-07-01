package it.polimi.ingsw.server.answer.viewAnswer;

import it.polimi.ingsw.server.answer.Answer;

/**
 * SchoolTowersAnswer contains info about the number of towers in a school.
 */
public class SchoolTowersAnswer implements Answer {
    private final int playerRef;
    private final int towers;

    /**
     * Create an answer contains info about the number of towers in a school.
     * @param playerRef player reference;
     * @param towers towers number;
     */
    public SchoolTowersAnswer(int playerRef, int towers) {
        this.playerRef = playerRef;
        this.towers = towers;
    }

    public int getPlayerRef() { return playerRef; }
    public int getTowers() { return towers; }
}
