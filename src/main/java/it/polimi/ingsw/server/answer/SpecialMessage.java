package it.polimi.ingsw.server.answer;

import it.polimi.ingsw.model.Special;

import java.util.ArrayList;

public class SpecialMessage implements Answer{

    private final String message;

    public SpecialMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
