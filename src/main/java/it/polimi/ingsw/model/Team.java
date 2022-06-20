package it.polimi.ingsw.model;

/**
 * The teams in play during the match.
 * When playing with two or four players, there are only the white and black teams; in three-player games, the gray team is also added.
 */
public enum Team {
    NONE(-1),
    WHITE(0),
    BLACK(1),
    GREY(2);
    private final int team;

    Team(int team){ this.team = team; }

    public int getTeam() { return team; }
}
