package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Entrance;

/**
 * Create Special6
 */
public class CreateSpecial6 implements CreateSpecial{
    /**
     * @see Special
     * @param server server reference;
     * @return Special6
     */
    @Override
    public Special makeSpecial(Entrance server){
        return new Special6(server);
    }
}