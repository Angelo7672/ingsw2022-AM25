package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Entrance;

/**
 * Create Special1
 */
public class CreateSpecial1 implements CreateSpecial{
    /**
     * @see Special
     * @param server server reference;
     * @return Special1
     */
    @Override
    public Special makeSpecial(Entrance server){
        return new Special1(server);
    }
}
