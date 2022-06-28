package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Entrance;

/**
 * Create Special10
 */
public class CreateSpecial10 implements CreateSpecial{
    /**
     * @see Special
     * @param server server reference;
     * @return Special10
     */
    @Override
    public Special makeSpecial(Entrance server){
        return new Special10(server);
    }
}
