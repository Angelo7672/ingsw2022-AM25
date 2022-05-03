package it.polimi.ingsw.client.Message;


public class ChosenAssistant implements Message {

    private final String assistant;

    public ChosenAssistant(String assistant) {
        this.assistant = assistant;
    }

    public String getAssistant() {
        return assistant;
    }
}
