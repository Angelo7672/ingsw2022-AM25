package it.polimi.ingsw.server.Answer;

import it.polimi.ingsw.model.Special;

import java.util.ArrayList;

public class SpecialMessage implements Answer{

    private final String message;

    public SpecialMessage(String message) {
        this.message = message;
    }


    @Override
    public String getMessage() {
        return message;
    }

}
