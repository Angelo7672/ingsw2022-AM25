package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.client.message.Message;
import it.polimi.ingsw.server.Entrance;

import java.util.ArrayList;

public class ExpertGame extends Thread{
    private Entrance server;
    private SpecialDeck specialDeck;
    private int firstSpecial, secondSpecial, thirdSpecial;


    public ExpertGame(Entrance server, ArrayList<Integer> extractedSpecial){ //voglio che extractedSpecial ordinato
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

    /*public boolean effect(int specialRef, int playerRef, VirtualClient user){
        if (specialRef == firstSpecial)
            return specialDeck.effect(0,playerRef,user);
        else if(specialRef == secondSpecial)
            return specialDeck.effect(1,playerRef,user);
        else if (specialRef == thirdSpecial)
            return specialDeck.effect(2,playerRef,user);

        return false;
    }*/

    public void setSpecialMsg(Message msg){ specialDeck.setSpecialMsg(msg); }

}
