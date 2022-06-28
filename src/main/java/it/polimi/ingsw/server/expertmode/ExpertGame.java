package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

import java.util.ArrayList;

/**
 * ExpertGame create on server the class SpecialDeck ready for clients to use special characters.
 */
public class ExpertGame {
    private final SpecialDeck specialDeck;
    private final int firstSpecial, secondSpecial, thirdSpecial;

    /**
     * Add to SpecialDeck the extracted special characters.
     * @param server server reference;
     * @param extractedSpecial ArrayList contains special characters of this match.
     */
    public ExpertGame(Entrance server, ArrayList<Integer> extractedSpecial){
        this.specialDeck = new SpecialDeck();
        this.firstSpecial = extractedSpecial.get(0);
        this.secondSpecial = extractedSpecial.get(1);
        this.thirdSpecial = extractedSpecial.get(2);

        for(Integer special:extractedSpecial){
            switch (special) {
                case 1 -> specialDeck.addSpecial(new CreateSpecial1(), server);
                case 2 -> specialDeck.addSpecial(new CreateSpecial2(), server);
                case 3 -> specialDeck.addSpecial(new CreateSpecial3(), server);
                case 4 -> specialDeck.addSpecial(new CreateSpecial4(), server);
                case 5 -> specialDeck.addSpecial(new CreateSpecial5(), server);
                case 6 -> specialDeck.addSpecial(new CreateSpecial6(), server);
                case 7 -> specialDeck.addSpecial(new CreateSpecial7(), server);
                case 8 -> specialDeck.addSpecial(new CreateSpecial8(), server);
                case 9 -> specialDeck.addSpecial(new CreateSpecial9(), server);
                case 10 -> specialDeck.addSpecial(new CreateSpecial10(), server);
                case 11 -> specialDeck.addSpecial(new CreateSpecial11(), server);
                case 12 -> specialDeck.addSpecial(new CreateSpecial12(), server);
            }
        }
    }

    /**
     * Use effect of designed special characters.
     * @param specialRef special index.
     * @param playerRef player reference.
     * @param user VirtualClient reference.
     * @return if the operation was successful.
     */
    public boolean effect(int specialRef, int playerRef, VirtualClient user){
        if (specialRef == firstSpecial)
            return specialDeck.effect(0, playerRef, user);
        else if(specialRef == secondSpecial)
            return specialDeck.effect(1, playerRef, user);
        else if (specialRef == thirdSpecial)
            return specialDeck.effect(2, playerRef, user);

        return false;
    }

    /**
     * Set special message for special1, special3, special5, special7, special9, special10, special11 or special12.
     * @param indexSpecial special index;
     * @param msg message received;
     */
    public void setSpecialMsg(int indexSpecial, Message msg){
        if(indexSpecial == firstSpecial) specialDeck.setSpecialMsg(0,msg);
        else if(indexSpecial == secondSpecial) specialDeck.setSpecialMsg(1,msg);
        else if(indexSpecial == thirdSpecial) specialDeck.setSpecialMsg(2,msg);
    }

    /**
     * Wake up special1, special3, special5, special7, special9, special10, special11 or special12 after received message.
     * @param indexSpecial special index;
     */
    public void wakeUp(int indexSpecial){
        if(indexSpecial == firstSpecial) specialDeck.wakeUp(0);
        else if(indexSpecial == secondSpecial) specialDeck.wakeUp(1);
        else if(indexSpecial == thirdSpecial) specialDeck.wakeUp(2);
    }
}