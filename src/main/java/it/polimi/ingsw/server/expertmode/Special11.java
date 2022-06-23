package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special11Message;
import it.polimi.ingsw.server.answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.answer.MoveNotAllowedAnswer;

public class Special11 implements Special{
    private Special11Message special11Msg;
    private Entrance server;

    public Special11(Entrance server) { this.server = server; }

    @Override
    public synchronized boolean effect(int playerRef, VirtualClient user){
        VirtualClient virtualClient = user;
        boolean checker = false;

        try {
            virtualClient.setSpecial11();
            virtualClient.send(new GenericAnswer("ok"));
            this.wait();

            checker = server.useSpecialSimple(11, playerRef, special11Msg.getColor());

           /* if (checker) virtualClient.send(new GenericAnswer("ok"));
            else virtualClient.send(new MoveNotAllowedAnswer());*/
        }catch (InterruptedException e) { e.printStackTrace(); }
        return checker;
    }

    @Override
    public void setSpecialMessage(Message msg) { special11Msg = (Special11Message) msg; }
    @Override
    public synchronized void wakeUp() { this.notify(); }
}
