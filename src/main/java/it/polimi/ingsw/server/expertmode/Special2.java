package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

public class Special2 implements Special{
    private Entrance server;

    public Special2(Entrance server) { this.server = server; }

    public boolean effect(int playerRef, VirtualClient user){
        VirtualClient virtualClient = user;
        boolean checker;

        checker = server.useSpecialLite(2,playerRef);

        if(checker) virtualClient.send(new GenericAnswer("ok"));
        else virtualClient.send(new GenericAnswer("error"));

        return checker;
    }
}
