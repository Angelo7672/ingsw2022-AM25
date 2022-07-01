package it.polimi.ingsw.client.message;

import java.io.Serializable;

/**
 * Message interface is implemented by every client's message because it allows to serialize them and server can recognize them.
 */
public interface Message extends Serializable { }
