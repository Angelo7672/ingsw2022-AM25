package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special10Message;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.answer.GenericAnswer;
import it.polimi.ingsw.server.answer.MoveNotAllowedAnswer;

public class Special10 implements Special{
    private Special10Message special10Msg;
    private Entrance server;

    public Special10(Entrance server) { this.server = server; }

    @Override
    public void effect(int playerRef, VirtualClient user){
        VirtualClient virtualClient = user;
        boolean checker;

        try {
            virtualClient.setSpecial10();
            virtualClient.send(new GenericAnswer("ok"));
            System.out.println("Il server ha mandato ok per ricevere messaggio ad hoc dello special");
            synchronized (this) { this.wait(); }

            checker = server.useSpecialHard(10, playerRef, special10Msg.getEntranceStudent(), special10Msg.getTableStudent());

            if(checker) virtualClient.send(new GenericAnswer("ok"));
            else virtualClient.send(new MoveNotAllowedAnswer());
        }catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Override
    public void setSpecialMessage(Message msg) { special10Msg = (Special10Message) msg; }
    @Override
    public void wakeUp() { this.notify(); }
}
