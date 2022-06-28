package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special7Message;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.answer.GenericAnswer;

/**
 * Special7 server class.
 */
public class Special7 implements Special{
    private Special7Message special7Msg;
    private final Entrance server;

    public Special7(Entrance server) { this.server = server; }

    /**
     * Effect of special7, wait Special7 message then proceed with effect on server.
     * @param playerRef player who use special.
     * @param user VirtualClient reference.
     * @return if the operation was successful.
     */
    @Override
    public synchronized boolean effect(int playerRef, VirtualClient user){
        boolean checker = false;

        try {
            user.setSpecial7();
            user.send(new GenericAnswer("ok"));
            this.wait();

            checker = server.useSpecialHard(7, playerRef, special7Msg.getEntranceStudent(), special7Msg.getCardStudent());

        }catch (InterruptedException e) { e.printStackTrace(); }
        return checker;
    }

    /**
     * Set special7 message.
     * @param msg special7 message;
     */
    @Override
    public void setSpecialMessage(Message msg) { special7Msg = (Special7Message) msg; }

    /**
     * Wake up Special7 when special7 message arrived.
     */
    @Override
    public synchronized void wakeUp() { this.notify(); }
}
