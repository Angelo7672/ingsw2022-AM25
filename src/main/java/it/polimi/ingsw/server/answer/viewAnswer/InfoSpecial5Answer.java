package it.polimi.ingsw.server.answer.viewAnswer;

import it.polimi.ingsw.server.answer.Answer;

/**
 * InfoSpecial5Answer contains info about the number of noEntryCards on special5.
 */
public class InfoSpecial5Answer implements Answer {
    private final int cards;

    /**
     * NoEntryCards on special5.
     * @param cards number of noEntryCards;
     */
    public InfoSpecial5Answer(int cards) { this.cards = cards; }

    public int getCards() { return cards; }
}
