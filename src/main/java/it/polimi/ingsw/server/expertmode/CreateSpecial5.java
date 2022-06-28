package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Entrance;

/**
 * Create Special5
 */
public class CreateSpecial5 implements CreateSpecial{
    /**
     * @see Special
     * @param server server reference;
     * @return Special5
     */
    @Override
    public Special makeSpecial(Entrance server){
        return new Special5(server);
    }
}
