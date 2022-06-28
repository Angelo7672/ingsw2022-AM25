package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

import java.util.ArrayList;
import java.util.List;

/**
 * SpecialDeck contains the list of Special of this match anf allowed to use their effect from its.
 */
public class SpecialDeck {
    private final List<Special> specialsOfThisMatch;

    public SpecialDeck(){ this.specialsOfThisMatch = new ArrayList<>(); }

    /**
     * Add a special to specialOfThisMatch.
     * @param createSpecial factory create special;
     * @param server server reference;
     */
    public void addSpecial(CreateSpecial createSpecial, Entrance server){ specialsOfThisMatch.add(createSpecial.makeSpecial(server)); }

    /**
     * Use effect of a special character.
     * @param specialRef special index;
     * @param playerRef reference to the player who wants use special;
     * @param user VirtualClient reference;
     * @return if the operation was successful.
     */
    public boolean effect(int specialRef, int playerRef, VirtualClient user){ return specialsOfThisMatch.get(specialRef).effect(playerRef,user); }

    /**
     * Set special message for special1, special3, special5, special7, special9, special10, special11 or special12.
     * @param indexSpecial special index;
     * @param msg message received;
     */
    public void setSpecialMsg(int indexSpecial, Message msg){ specialsOfThisMatch.get(indexSpecial).setSpecialMessage(msg); }

    /**
     * Wake up Special1, Special3, Special5, Special7, Special9, Special10, Special11 or Special12 when special message arrived.
     */
    public void wakeUp(int indexSpecial){ specialsOfThisMatch.get(indexSpecial).wakeUp(); }
}