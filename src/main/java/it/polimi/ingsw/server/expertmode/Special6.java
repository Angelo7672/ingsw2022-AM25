package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

/**
 * Special6 server class.
 */
public class Special6 implements Special{
    private final Entrance server;

    public Special6(Entrance server) { this.server = server; }

    /**
     * Effect of special6.
     * @param playerRef player who use special.
     * @param user VirtualClient reference.
     * @return if the operation was successful.
     */
    @Override
    public boolean effect(int playerRef, VirtualClient user){
        boolean checker;

        checker = server.useSpecialLite(6, playerRef);

        return checker;
    }

    @Override
    public void setSpecialMessage(Message msg) {}
    @Override
    public void wakeUp() {}
}