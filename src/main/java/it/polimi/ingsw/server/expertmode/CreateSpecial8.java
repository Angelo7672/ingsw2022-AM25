package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Entrance;

/**
 * Create Special8
 */
public class CreateSpecial8 implements CreateSpecial{
    /**
     * @see Special
     * @param server server reference;
     * @return Special8
     */
    @Override
    public Special makeSpecial(Entrance server){
        return new Special8(server);
    }
}
