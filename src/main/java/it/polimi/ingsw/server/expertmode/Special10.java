package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.Message.Message;
import it.polimi.ingsw.client.Message.special.Special10Message;
import it.polimi.ingsw.server.Entrance;

public class Special10 implements Special{
    private Special10Message special10Msg;
    private Entrance server;

    public Special10(Entrance server) { this.server = server; }

    /*public boolean effect(int playerRef, VirtualClient user){
        VirtualClient virtualClient = user;
        boolean checker;

        checker = server.useSpecialHard(10, playerRef, special10Msg.getEntranceStudent(), special10Msg.getTableStudent());

        if(checker) virtualClient.send(new GenericAnswer("ok"));
        else virtualClient.send(new GenericAnswer("error"));

        return checker;
    }*/

    public void setSpecialMessage(Message msg) { special10Msg = (Special10Message) msg; }
}
