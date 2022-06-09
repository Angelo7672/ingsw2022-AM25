package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Entrance;

public class CreateSpecial6 implements CreateSpecial{
    @Override
    public Special makeSpecial(Entrance server){
        return new Special6(server);
    }
}
