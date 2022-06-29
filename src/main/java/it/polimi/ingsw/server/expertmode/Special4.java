package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

/**
 * Special4 server class
 */
public class Special4 implements Special{
    private final Entrance server;

    public Special4(Entrance server) { this.server = server; }

    /**
     * Effect of special4.
     * @param playerRef player who use special.
     * @param user VirtualClient reference.
     * @return if the operation was successful.
     */
    @Override
    public boolean effect(int playerRef, VirtualClient user){
        boolean checker;

        checker = server.useSpecialSimple(4, playerRef, playerRef);

        return checker;
    }

    @Override
    public void setSpecialMessage(Message msg) {}
    @Override
    public void wakeUp() {}
}
