package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Entrance;

/**
 * Create Special2
 */
public class CreateSpecial2 implements CreateSpecial{
    /**
     * @see Special
     * @param server server reference;
     * @return Special2
     */
    @Override
    public Special makeSpecial(Entrance server){
        return new Special2(server);
    }
}
