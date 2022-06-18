package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special12Message;
import it.polimi.ingsw.server.answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.answer.MoveNotAllowedAnswer;

public class Special12 implements Special{
    private Special12Message special12Msg;
    private Entrance server;

    public Special12(Entrance server) { this.server = server; }

    @Override
    public void effect(int playerRef, VirtualClient user){
        VirtualClient virtualClient = user;
        boolean checker;

        try {
            virtualClient.setSpecial12();
            virtualClient.send(new GenericAnswer("ok"));
            System.out.println("Il server ha mandato ok per ricevere messaggio ad hoc dello special");
            synchronized (this) { this.wait(); }

            checker = server.useSpecialSimple(12, playerRef, special12Msg.getColor());

            if (checker) virtualClient.send(new GenericAnswer("ok"));
            else virtualClient.send(new MoveNotAllowedAnswer());
        }catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Override
    public void setSpecialMessage(Message msg) { special12Msg = (Special12Message) msg; }
    @Override
    public void wakeUp() { this.notify(); }
}
