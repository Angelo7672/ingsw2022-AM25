package it.polimi.ingsw.listeners;

import java.io.IOException;

public interface DisconnectedListener {
    void notifyDisconnected() throws IOException;
}
