package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.Message.Message;
import it.polimi.ingsw.client.Message.Special.Special5Message;
import it.polimi.ingsw.server.Answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

public class Special5 implements Special{
    private Special5Message special5Msg;
    private Entrance server;

    public Special5(Entrance server) { this.server = server; }

    public boolean effect(int playerRef, VirtualClient user){
        VirtualClient virtualClient = user;
        boolean checker;

        checker = server.useSpecialSimple(5, playerRef, special5Msg.getIslandRef());

        if(checker) virtualClient.send(new GenericAnswer("ok"));
        else virtualClient.send(new GenericAnswer("error"));

        return checker;
    }

    public void setSpecialMessage(Message msg) { special5Msg = (Special5Message) msg; }
}
