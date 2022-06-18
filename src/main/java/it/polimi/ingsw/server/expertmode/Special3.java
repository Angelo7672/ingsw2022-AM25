package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special3Message;
import it.polimi.ingsw.server.answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.answer.MoveNotAllowedAnswer;

public class Special3 implements Special{
    private Special3Message special3Msg;
    private Entrance server;

    public Special3(Entrance server) { this.server = server; }

    @Override
    public void effect(int playerRef, VirtualClient user){
        VirtualClient virtualClient = user;
        boolean checker;

        try {
            virtualClient.setSpecial3();
            virtualClient.send(new GenericAnswer("ok"));
            System.out.println("Il server ha mandato ok per ricevere messaggio ad hoc dello special");
            synchronized (this) { this.wait(); }

            checker = server.useSpecialSimple(3, playerRef, special3Msg.getIslandRef());

            if (checker) virtualClient.send(new GenericAnswer("ok"));
            else virtualClient.send(new MoveNotAllowedAnswer());
        }catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Override
    public void setSpecialMessage(Message msg) { special3Msg = (Special3Message) msg; }
    @Override
    public void wakeUp() { this.notify(); }
}
