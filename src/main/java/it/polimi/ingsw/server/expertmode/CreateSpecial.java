package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Entrance;

/**
 * Factory interface.
 */
public interface CreateSpecial {
    /**
     * Create special
     * @param server server reference;
     * @return special create;
     */
    Special makeSpecial(Entrance server);
}
