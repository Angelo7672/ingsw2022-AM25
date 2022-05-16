package it.polimi.ingsw.client.Message;

public class CardMessage implements Message{
    private final String card;

    public CardMessage(String card) {
        this.card = card;
    }

    public String getCard() {
        return card;
    }
}
