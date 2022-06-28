package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

/**
 * Special8 server class.
 */
public class Special8 implements Special{
    private final Entrance server;

    public Special8(Entrance server) { this.server = server; }

    /**
     * Effect of special8.
     * @param playerRef player who use special.
     * @param user VirtualClient reference.
     * @return if the operation was successful.
     */
    @Override
    public boolean effect(int playerRef, VirtualClient user){
        boolean checker;

        checker = server.useSpecialLite(8, playerRef);

        return checker;
    }

    @Override
    public void setSpecialMessage(Message msg) {}
    @Override
    public void wakeUp() { }
}