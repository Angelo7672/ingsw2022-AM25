package it.polimi.ingsw.client.Message;

public class CardMessage implements Message{
    private final int card;

    public CardMessage(int card) {
        this.card = card;
    }

    public int getCard() {
        return card;
    }
}
