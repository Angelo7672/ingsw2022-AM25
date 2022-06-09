package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Entrance;

public class CreateSpecial3 implements CreateSpecial{
    @Override
    public Special makeSpecial(Entrance server){
        return new Special3(server);
    }
}
