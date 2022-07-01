package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special10Message;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.answer.GenericAnswer;

/**
 * Special10 server class.
 */
public class Special10 implements Special{
    private Special10Message special10Msg;
    private final Entrance server;

    /**
     * Create Special10.
     * @param server server reference;
     */
    public Special10(Entrance server) { this.server = server; }

    /**
     * Effect of special10, wait Special10 message then proceed with effect on server.
     * @param playerRef player who use special.
     * @param user VirtualClient reference.
     * @return if the operation was successful.
     */
    @Override
    public synchronized boolean effect(int playerRef, VirtualClient user){
        boolean checker = false;

        try {
            user.setSpecial10();
            user.send(new GenericAnswer("ok"));
            this.wait();

            checker = server.useSpecialHard(10, playerRef, special10Msg.getEntranceStudent(), special10Msg.getTableStudent());

        }catch (InterruptedException e) { e.printStackTrace(); }
        return checker;
    }

    /**
     * Set special10 message.
     * @param msg special10 message;
     */
    @Override
    public void setSpecialMessage(Message msg) { special10Msg = (Special10Message) msg; }

    /**
     * Wake up Special10 when special1 message arrived.
     */
    @Override
    public synchronized void wakeUp() { this.notify(); }
}
