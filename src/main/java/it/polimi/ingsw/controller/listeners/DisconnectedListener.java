package it.polimi.ingsw.controller.listeners;

import java.io.IOException;

public interface DisconnectedListener {
    void notifyDisconnected() throws IOException;
}
