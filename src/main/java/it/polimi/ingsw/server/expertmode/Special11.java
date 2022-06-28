package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special11Message;
import it.polimi.ingsw.server.answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

/**
 * Special11 server class.
 */
public class Special11 implements Special{
    private Special11Message special11Msg;
    private final Entrance server;

    public Special11(Entrance server) { this.server = server; }

    /**
     * Effect of special11, wait Special11 message then proceed with effect on server.
     * @param playerRef player who use special.
     * @param user VirtualClient reference.
     * @return if the operation was successful.
     */
    @Override
    public synchronized boolean effect(int playerRef, VirtualClient user){
        boolean checker = false;

        try {
            user.setSpecial11();
            user.send(new GenericAnswer("ok"));
            this.wait();

            checker = server.useSpecialMedium(11, playerRef, playerRef, special11Msg.getColor());

        }catch (InterruptedException e) { e.printStackTrace(); }
        return checker;
    }

    /**
     * Set special11 message.
     * @param msg special1 message;
     */
    @Override
    public void setSpecialMessage(Message msg) { special11Msg = (Special11Message) msg; }

    /**
     * Wake up Special11 when special11 message arrived.
     */
    @Override
    public synchronized void wakeUp() { this.notify(); }
}