package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.Message.Message;
import it.polimi.ingsw.client.Message.Special.Special1Message;
import it.polimi.ingsw.server.Answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

public class Special6 implements Special{
    private Entrance server;

    public Special6(Entrance server) { this.server = server; }

    public boolean effect(int playerRef, VirtualClient user){
        VirtualClient virtualClient = user;
        boolean checker;

        checker = server.useSpecialLite(6, playerRef);

        if(checker) virtualClient.send(new GenericAnswer("ok"));
        else virtualClient.send(new GenericAnswer("error"));

        return checker;
    }

    @Override
    public void setSpecialMessage(Message msg) {}
}
