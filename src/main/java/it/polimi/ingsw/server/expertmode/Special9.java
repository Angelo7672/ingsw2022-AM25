package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special9Message;
import it.polimi.ingsw.server.answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.answer.MoveNotAllowedAnswer;

public class Special9 implements Special{
    private Special9Message special9Msg;
    private Entrance server;

    public Special9(Entrance server) { this.server = server; }

    @Override
    public synchronized void effect(int playerRef, VirtualClient user){
        VirtualClient virtualClient = user;
        boolean checker;

        try {
            virtualClient.setSpecial9();
            virtualClient.send(new GenericAnswer("ok"));
            this.wait();

            checker = server.useSpecialSimple(9, playerRef, special9Msg.getColor());

            if (checker) virtualClient.send(new GenericAnswer("ok"));
            else virtualClient.send(new MoveNotAllowedAnswer());
        }catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Override
    public void setSpecialMessage(Message msg) { special9Msg = (Special9Message) msg; }
    @Override
    public synchronized void wakeUp() { this.notify(); }
}
