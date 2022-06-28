package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special12Message;
import it.polimi.ingsw.server.answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

/**
 * Special12 server class.
 */
public class Special12 implements Special{
    private Special12Message special12Msg;
    private final Entrance server;

    public Special12(Entrance server) { this.server = server; }

    /**
     * Effect of special12, wait Special12 message then proceed with effect on server.
     * @param playerRef player who use special.
     * @param user VirtualClient reference.
     * @return if the operation was successful.
     */
    @Override
    public synchronized boolean effect(int playerRef, VirtualClient user){
        boolean checker = false;

        try {
            user.setSpecial12();
            user.send(new GenericAnswer("ok"));
            this.wait();

            checker = server.useSpecialSimple(12, playerRef, special12Msg.getColor());

        }catch (InterruptedException e) { e.printStackTrace(); }
        return checker;
    }

    /**
     * Set special12 message.
     * @param msg special12 message;
     */
    @Override
    public void setSpecialMessage(Message msg) { special12Msg = (Special12Message) msg; }

    /**
     * Wake up Special12 when special1 message arrived.
     */
    @Override
    public synchronized void wakeUp() { this.notify(); }
}
