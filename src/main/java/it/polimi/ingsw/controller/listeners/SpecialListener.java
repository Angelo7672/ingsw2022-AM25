package it.polimi.ingsw.controller.listeners;

import java.util.ArrayList;

public interface SpecialListener {
    public void notifySpecial(int specialRef, int playerRef);
    public void notifySpecialList(ArrayList<Integer> specialsList);
}
