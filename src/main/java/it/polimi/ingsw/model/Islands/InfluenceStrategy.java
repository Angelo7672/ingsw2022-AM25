package it.polimi.ingsw.model.Islands;

import it.polimi.ingsw.model.Team;

import java.util.ArrayList;

public interface InfluenceStrategy {
    public Team highestInfluenceTeam(ArrayList<Integer> prof, IslandsManager.Island island, int noColor, int player);
}
