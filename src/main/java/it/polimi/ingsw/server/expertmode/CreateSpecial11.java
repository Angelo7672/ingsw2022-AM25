package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Entrance;

/**
 * Create Special11
 */
public class CreateSpecial11 implements CreateSpecial{
    /**
     * @see Special
     * @param server server reference;
     * @return Special11
     */
    @Override
    public Special makeSpecial(Entrance server){
        return new Special11(server);
    }
}
