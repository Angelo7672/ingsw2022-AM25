package it.polimi.ingsw.controller.listeners;

public interface SpecialListener {
    public void notifySpecial(int specialRef);
    public void notifySpecialName(String specialName);
    public void notifyPlayedSpecial(int specialRef);
}
