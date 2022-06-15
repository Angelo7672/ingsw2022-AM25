package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

public class InfoSpecial5Answer implements Answer {
    private final int cards;

    public InfoSpecial5Answer(int cards) { this.cards = cards; }

    public int getCards() { return cards; }
}
