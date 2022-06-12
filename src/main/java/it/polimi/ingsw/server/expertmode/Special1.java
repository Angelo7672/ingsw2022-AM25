package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special1Message;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.answer.GenericAnswer;
import it.polimi.ingsw.server.answer.MoveNotAllowedAnswer;

public class Special1 implements Special{
    private Special1Message special1Msg;
    private Entrance server;

    public Special1(Entrance server) { this.server = server; }

    @Override
    public void effect(int playerRef, VirtualClient user){
        VirtualClient virtualClient = user;
        boolean checker;

        try {
            virtualClient.setSpecial1();
            virtualClient.send(new GenericAnswer("ok"));
            synchronized (this) { this.wait(); }

            checker = server.useSpecialMedium(1, playerRef, special1Msg.getIslandRef(), special1Msg.getColor());

            if(checker) virtualClient.send(new GenericAnswer("ok"));
            else virtualClient.send(new MoveNotAllowedAnswer());
        }catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Override
    public void setSpecialMessage(Message msg) { special1Msg = (Special1Message) msg; }
    @Override
    public void wakeUp() { this.notify(); }
}
