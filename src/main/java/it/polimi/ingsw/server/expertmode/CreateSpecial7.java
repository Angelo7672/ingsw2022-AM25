package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Entrance;

/**
 * Create Special7
 */
public class CreateSpecial7 implements CreateSpecial{
    /**
     * @see Special
     * @param server server reference;
     * @return Special7
     */
    @Override
    public Special makeSpecial(Entrance server){
        return new Special7(server);
    }
}
