package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special1Message;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.answer.GenericAnswer;

/**
 * Special1 server class.
 */
public class Special1 implements Special{
    private Special1Message special1Msg;
    private final Entrance server;

    public Special1(Entrance server) { this.server = server; }

    /**
     * Effect of special1, wait Special1 message then proceed with effect on server.
     * @param playerRef player who use special.
     * @param user VirtualClient reference.
     * @return if the operation was successful.
     */
    @Override
    public synchronized boolean effect(int playerRef, VirtualClient user){
        boolean checker = false;

        try {
            user.setSpecial1();
            user.send(new GenericAnswer("ok"));
            this.wait();

            checker = server.useSpecialMedium(1, playerRef, special1Msg.getIslandRef(), special1Msg.getColor());

        }catch (InterruptedException e) { e.printStackTrace(); }
        return checker;
    }

    /**
     * Set special1 message.
     * @param msg special1 message;
     */
    @Override
    public void setSpecialMessage(Message msg) { special1Msg = (Special1Message) msg; }

    /**
     * Wake up Special1 when special1 message arrived.
     */
    @Override
    public synchronized void wakeUp() { this.notify(); }
}