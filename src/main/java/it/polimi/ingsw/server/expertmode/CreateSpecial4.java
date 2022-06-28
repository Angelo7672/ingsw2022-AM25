package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Entrance;

/**
 * Create Special4
 */
public class CreateSpecial4 implements CreateSpecial{
    /**
     * @see Special
     * @param server server reference;
     * @return Special4
     */
    @Override
    public Special makeSpecial(Entrance server){
        return new Special4(server);
    }
}
