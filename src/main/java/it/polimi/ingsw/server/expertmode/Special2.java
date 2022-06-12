package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.server.answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.answer.MoveNotAllowedAnswer;

public class Special2 implements Special{
    private Entrance server;

    public Special2(Entrance server) { this.server = server; }

    @Override
    public void effect(int playerRef, VirtualClient user){
        VirtualClient virtualClient = user;
        boolean checker;

        checker = server.useSpecialLite(2,playerRef);

        if(checker) virtualClient.send(new GenericAnswer("ok"));
        else virtualClient.send(new MoveNotAllowedAnswer());
    }

    @Override
    public void setSpecialMessage(Message msg){}
    @Override
    public void wakeUp() {}
}
