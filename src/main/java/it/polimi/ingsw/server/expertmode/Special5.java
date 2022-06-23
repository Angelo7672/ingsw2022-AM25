package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special5Message;
import it.polimi.ingsw.server.answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.answer.MoveNotAllowedAnswer;

public class Special5 implements Special{
    private Special5Message special5Msg;
    private Entrance server;

    public Special5(Entrance server) { this.server = server; }

    @Override
    public synchronized boolean effect(int playerRef, VirtualClient user){
        VirtualClient virtualClient = user;
        boolean checker = false;

        try {
            virtualClient.setSpecial5();
            virtualClient.send(new GenericAnswer("ok"));
            this.wait();

            checker = server.useSpecialSimple(5, playerRef, special5Msg.getIslandRef());

            /*if(checker) virtualClient.send(new GenericAnswer("ok"));
            else virtualClient.send(new MoveNotAllowedAnswer());*/
        }catch (InterruptedException e) { e.printStackTrace(); }
        return checker;
    }

    @Override
    public void setSpecialMessage(Message msg) { special5Msg = (Special5Message) msg; }
    @Override
    public synchronized void wakeUp() { this.notify(); }
}
