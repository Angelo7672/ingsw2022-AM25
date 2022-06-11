package it.polimi.ingsw.server.answer.viewMessage;

import it.polimi.ingsw.server.answer.Answer;

public class ProfessorMessage implements Answer {
    private final int playerRef;
    private final boolean professor;
    private final int color;

    public ProfessorMessage(int playerRef, boolean professor, int color) {
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
