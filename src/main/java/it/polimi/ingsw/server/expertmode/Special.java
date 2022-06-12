package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.server.VirtualClient;

public interface Special {
    //boolean effect(int playerRef, VirtualClient user);
    void setSpecialMessage(Message msg);
}
