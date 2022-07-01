package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special9Message;
import it.polimi.ingsw.server.answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

/**
 * Special9 server class.
 */
public class Special9 implements Special{
    private Special9Message special9Msg;
    private final Entrance server;

    /**
     * Create Special9.
     * @param server server reference;
     */
    public Special9(Entrance server) { this.server = server; }

    /**
     * Effect of special9, wait Special9 message then proceed with effect on server.
     * @param playerRef player who use special.
     * @param user VirtualClient reference.
     * @return if the operation was successful.
     */
    @Override
    public synchronized boolean effect(int playerRef, VirtualClient user){
        boolean checker = false;

        try {
            user.setSpecial9();
            user.send(new GenericAnswer("ok"));
            this.wait();

            checker = server.useStrategySimple(9, playerRef, special9Msg.getColor());

        }catch (InterruptedException e) { e.printStackTrace(); }
        return checker;
    }

    /**
     * Set special9 message.
     * @param msg special9 message;
     */
    @Override
    public void setSpecialMessage(Message msg) { special9Msg = (Special9Message) msg; }

    /**
     * Wake up Special9 when special9 message arrived.
     */
    @Override
    public synchronized void wakeUp() { this.notify(); }
}