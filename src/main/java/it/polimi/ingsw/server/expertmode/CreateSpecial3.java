package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Entrance;

/**
 * Create Special3
 */
public class CreateSpecial3 implements CreateSpecial{
    /**
     * @see Special
     * @param server server reference;
     * @return Special3
     */
    @Override
    public Special makeSpecial(Entrance server){
        return new Special3(server);
    }
}
