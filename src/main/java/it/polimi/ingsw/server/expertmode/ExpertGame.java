package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.server.Entrance;
import it.polimi.ingsw.server.VirtualClient;

import java.util.ArrayList;

public class ExpertGame {
    private Entrance server;
    private SpecialDeck specialDeck;
    private int firstSpecial, secondSpecial, thirdSpecial;


    public ExpertGame(Entrance server, ArrayList<Integer> extractedSpecial){
        this.server = server;
        this.specialDeck = new SpecialDeck();
        this.firstSpecial = extractedSpecial.get(0);
        this.secondSpecial = extractedSpecial.get(1);
        this.thirdSpecial = extractedSpecial.get(2);

        for(Integer special:extractedSpecial){
            switch (special){
                case 1:
                    specialDeck.addSpecial(new CreateSpecial1(), server);
                    break;
                case 2:
                    specialDeck.addSpecial(new CreateSpecial2(), server);
                    break;
                case 3:
                    specialDeck.addSpecial(new CreateSpecial3(), server);
                    break;
                case 4:
                    specialDeck.addSpecial(new CreateSpecial4(), server);
                    break;
                case 5:
                    specialDeck.addSpecial(new CreateSpecial5(), server);
                    break;
                case 6:
                    specialDeck.addSpecial(new CreateSpecial6(), server);
                    break;
                case 7:
                    specialDeck.addSpecial(new CreateSpecial7(), server);
                    break;
                case 8:
                    specialDeck.addSpecial(new CreateSpecial8(), server);
                    break;
                case 9:
                    specialDeck.addSpecial(new CreateSpecial9(), server);
                    break;
                case 10:
                    specialDeck.addSpecial(new CreateSpecial10(), server);
                    break;
                case 11:
                    specialDeck.addSpecial(new CreateSpecial11(), server);
                    break;
                case 12:
                    specialDeck.addSpecial(new CreateSpecial12(), server);
                    break;
            }
        }
    }

    public boolean effect(int specialRef, int playerRef, VirtualClient user){
        if (specialRef == firstSpecial) {
            specialDeck.effect(0, playerRef, user);
            return true;
        }
        else if(specialRef == secondSpecial) {
            specialDeck.effect(1, playerRef, user);
            return true;
        }
        else if (specialRef == thirdSpecial) {
            specialDeck.effect(2, playerRef, user);
            return true;
        }
        System.out.println("MISS!");
        return false;
    }

    public void setSpecialMsg(int indexSpecial, Message msg){
        if(indexSpecial == firstSpecial) specialDeck.setSpecialMsg(0,msg);
        else if(indexSpecial == secondSpecial) specialDeck.setSpecialMsg(1,msg);
        else if(indexSpecial == thirdSpecial) specialDeck.setSpecialMsg(2,msg);
    }
    public void wakeUp(int indexSpecial){
        if(indexSpecial == firstSpecial) specialDeck.wakeUp(0);
        else if(indexSpecial == secondSpecial) specialDeck.wakeUp(1);
        else if(indexSpecial == thirdSpecial) specialDeck.wakeUp(2);
    }
}
