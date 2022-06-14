package it.polimi.ingsw.listeners;

public interface IslandListener {
    public void notifyIslandChange(int islandToDelete);
}
// notify the reference to the island which size increases by 1, and the reference to the island tile that will be unified (so
// it is removed from the islands' list)

//notify the reference to the island tile that needs to be deleted after it is unified to another (so it is removed from the islands' list)