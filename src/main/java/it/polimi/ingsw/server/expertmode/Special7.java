package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.Message.Message;

import it.polimi.ingsw.client.Message.Special.Special7Message;
import it.polimi.ingsw.server.Answer.GenericAnswer;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

public class Special7 implements Special{
    private Special7Message special7Msg;
    private Entrance server;

    public Special7(Entrance server) { this.server = server; }

    /*public boolean effect(int playerRef, VirtualClient user){
        VirtualClient virtualClient = user;
        boolean checker;

        checker = server.useSpecialHard(7, playerRef, special7Msg.getEntranceStudent(), special7Msg.getCardStudent() );

        if(checker) virtualClient.send(new GenericAnswer("ok"));
        else virtualClient.send(new GenericAnswer("error"));

        return checker;
    }*/

    public void setSpecialMessage(Message msg) { special7Msg = (Special7Message) msg; }
}
