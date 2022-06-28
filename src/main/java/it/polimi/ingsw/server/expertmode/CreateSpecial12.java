package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Entrance;

/**
 * Create Special12
 */
public class CreateSpecial12 implements CreateSpecial{
    /**
     * @see Special
     * @param server server reference;
     * @return Special12
     */
    @Override
    public Special makeSpecial(Entrance server){
        return new Special12(server);
    }
}
