package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.Message.Message;
import it.polimi.ingsw.client.Message.Special.Special1Message;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

import java.util.ArrayList;
import java.util.List;

public class SpecialDeck {
    private List<Special> specialsOfThisMatch;

    public SpecialDeck(){
        this.specialsOfThisMatch = new ArrayList<Special>();
    }

    public void addSpecial(CreateSpecial createSpecial, Entrance server){ specialsOfThisMatch.add(createSpecial.makeSpecial(server)); }

    //public boolean effect(int specialRef, int playerRef, VirtualClient user){ return specialsOfThisMatch.get(specialRef).effect(playerRef,user); }

    public void setSpecialMsg(Message msg){ specialsOfThisMatch.get(0).setSpecialMessage(msg); }
}