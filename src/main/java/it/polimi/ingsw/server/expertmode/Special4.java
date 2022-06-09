package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

public class Special4 implements Special{
    private Entrance server;

    public Special4(Entrance server) { this.server = server; }

    /*public boolean effect(int playerRef, VirtualClient user){
        VirtualClient virtualClient = user;
        boolean checker;

        checker = server.useSpecialMedium(4, playerRef,);

        if(checker) virtualClient.send(new GenericAnswer("ok"));
        else virtualClient.send(new GenericAnswer("error"));

        return checker;
    }*/
}
