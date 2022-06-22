package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special7Message;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.answer.GenericAnswer;
import it.polimi.ingsw.server.answer.MoveNotAllowedAnswer;

public class Special7 implements Special{
    private Special7Message special7Msg;
    private Entrance server;

    public Special7(Entrance server) { this.server = server; }

    @Override
    public synchronized void effect(int playerRef, VirtualClient user){
        VirtualClient virtualClient = user;
        boolean checker;

        try {
            virtualClient.setSpecial7();
            virtualClient.send(new GenericAnswer("ok"));
            this.wait();

            checker = server.useSpecialHard(7, playerRef, special7Msg.getEntranceStudent(), special7Msg.getCardStudent());

            if (checker) virtualClient.send(new GenericAnswer("ok"));
            else virtualClient.send(new MoveNotAllowedAnswer());
        }catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Override
    public void setSpecialMessage(Message msg) { special7Msg = (Special7Message) msg; }
    @Override
    public synchronized void wakeUp() { this.notify(); }
}
