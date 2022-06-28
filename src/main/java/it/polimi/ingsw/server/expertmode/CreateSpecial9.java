package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Entrance;

/**
 * Create Special9
 */
public class CreateSpecial9 implements CreateSpecial{
    /**
     * @see Special
     * @param server server reference;
     * @return Special9
     */
    @Override
    public Special makeSpecial(Entrance server){
        return new Special9(server);
    }
}
