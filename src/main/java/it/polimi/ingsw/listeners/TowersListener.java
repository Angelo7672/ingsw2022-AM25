package it.polimi.ingsw.listeners;

public interface TowersListener {
    public void notifyTowersChange(int place, int componentRef, int towersNumber);
    public void notifyTowerColor(int islandRef, int newColor); //the color of the towers can change only on islands
}
//place: 0 school, 1 island
