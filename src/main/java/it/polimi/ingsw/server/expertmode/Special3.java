package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special3Message;
import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.server.answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

/**
 * Special3 server class.
 */
public class Special3 implements Special{
    private Special3Message special3Msg;
    private final Entrance server;

    /**
     * Create Special3.
     * @param server server reference;
     */
    public Special3(Entrance server) { this.server = server; }

    /**
     * Effect of Special3, wait Special3 message then proceed with effect on server.
     * @param playerRef player who use special.
     * @param user VirtualClient reference.
     * @return if the operation was successful.
     */
    @Override
    public synchronized boolean effect(int playerRef, VirtualClient user){
        boolean checker = false;

        try {
            user.setSpecial3();
            user.send(new GenericAnswer("ok"));
            this.wait();

            checker = server.useSpecial3(playerRef, special3Msg.getIslandRef());

        }catch (InterruptedException e) { e.printStackTrace();
        }catch (EndGameException endGameException){ server.gameOver(); }
        return checker;
    }

    /**
     * Set special3 message.
     * @param msg special3 message;
     */
    @Override
    public void setSpecialMessage(Message msg) { special3Msg = (Special3Message) msg; }

    /**
     * Wake up Special3 when special3 message arrived.
     */
    @Override
    public synchronized void wakeUp() { this.notify(); }
}
