package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.client.message.special.Special11Message;
import it.polimi.ingsw.server.answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

public class Special11 implements Special{
    private Special11Message special11Msg;
    private Entrance server;

    public Special11(Entrance server) { this.server = server; }

    public boolean effect(int playerRef, VirtualClient user){
        VirtualClient virtualClient = user;
        boolean checker;

        checker = server.useSpecialSimple(11, playerRef, special11Msg.getColor());

        if(checker) virtualClient.send(new GenericAnswer("ok"));
        else virtualClient.send(new GenericAnswer("error"));

        return checker;
    }

    public void setSpecialMessage(Message msg) { special11Msg = (Special11Message) msg; }
}
