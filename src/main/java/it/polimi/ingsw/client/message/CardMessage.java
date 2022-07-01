package it.polimi.ingsw.client.message;

/**
 * CardMessage contains the chosen card.
 */
public class CardMessage implements Message{
    private final String card;

    public CardMessage(String card) {
        this.card = card;
    }

    public String getCard() {
        return card;
    }
}
