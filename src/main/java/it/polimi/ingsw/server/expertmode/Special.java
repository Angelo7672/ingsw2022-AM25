package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.server.VirtualClient;

/**
 * Special interface used by SpecialDeck to use special character's method.
 */
public interface Special {

    /**
     * @see Special you want to use
     */
    boolean effect(int playerRef, VirtualClient user);

    /**
     * @see Special you want to use
     */
    void setSpecialMessage(Message msg);

    /**
     * @see Special you want to use
     */
    void wakeUp();
}
