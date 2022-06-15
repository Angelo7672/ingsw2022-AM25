package it.polimi.ingsw.listeners;

import java.util.ArrayList;

public interface SpecialListener {
    public void notifySpecial(int specialRef, int playerRef);
    public void notifySpecialList(ArrayList<Integer> specialList, ArrayList<Integer> cost);
}
