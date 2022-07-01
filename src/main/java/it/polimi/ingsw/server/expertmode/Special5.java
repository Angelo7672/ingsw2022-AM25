package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special5Message;
import it.polimi.ingsw.server.answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

/**
 * Special5 server class
 */
public class Special5 implements Special{
    private Special5Message special5Msg;
    private final Entrance server;

    /**
     * Create Special5.
     * @param server server reference;
     */
    public Special5(Entrance server) { this.server = server; }

    /**
     * Effect of special5, wait Special5 message then proceed with effect on server.
     * @param playerRef player who use special.
     * @param user VirtualClient reference.
     * @return if the operation was successful.
     */
    @Override
    public synchronized boolean effect(int playerRef, VirtualClient user){
        boolean checker = false;

        try {
            user.setSpecial5();
            user.send(new GenericAnswer("ok"));
            this.wait();

            checker = server.useSpecialSimple(5, playerRef, special5Msg.getIslandRef());

        }catch (InterruptedException e) { e.printStackTrace(); }
        return checker;
    }

    /**
     * Set special5 message.
     * @param msg special5 message;
     */
    @Override
    public void setSpecialMessage(Message msg) { special5Msg = (Special5Message) msg; }

    /**
     * Wake up Special5 when special5 message arrived.
     */
    @Override
    public synchronized void wakeUp() { this.notify(); }
}