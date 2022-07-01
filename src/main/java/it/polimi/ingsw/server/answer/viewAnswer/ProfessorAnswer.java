package it.polimi.ingsw.server.answer.viewAnswer;

import it.polimi.ingsw.server.answer.Answer;

/**
 * ProfessorAnswer contains info about holding or not a professor of a color by a player.
 */
public class ProfessorAnswer implements Answer {
    private final int playerRef;
    private final boolean professor;
    private final int color;

    /**
     * Create an answer contains in info about holding or not a professor of a color by a player.
     * @param playerRef player reference;
     * @param professor professor reference;
     * @param color color reference;
     */
    public ProfessorAnswer(int playerRef, boolean professor, int color) {
        this.playerRef = playerRef;
        this.professor = professor;
        this.color = color;
    }

    public int getPlayerRef() {
        return playerRef;
    }
    public boolean isProfessor() {
        return professor;
    }
    public int getColor() {
        return color;
    }
}
