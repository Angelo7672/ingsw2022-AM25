package it.polimi.ingsw.client;

import it.polimi.ingsw.server.Answer.*;

import java.util.ArrayList;

public class InputChecker {

    public InputChecker (){}


    public boolean checkSetupConnection(ConnectionMessage message, String nickname, String character){
        //if(message.getRemainingCharacter().contains(character)) return false;
        if(!message.getChosenNickname().contains(nickname)) return false;
        return true;
    }

    public boolean checkCard(CardsMessage message, int card){
        if(message.getHand().size()>=card) return false;
        if(!message.getPlayedCards().contains(message.getHand().get(card))) return false;
        return true;
    }

    public boolean checkMoveStudent(SchoolMessage message, int color, boolean inSchool){
        if(message.getEntranceStudent()[color]==0) return false;
        if(inSchool && message.getTableStudent()[color]==9) return false;
        return true;
    }

    public boolean checkMoveMotherNature(MoveMotherNatureMessage message, int steps){
        if (steps<=0)return false;
        if(steps>message.getMaxMovement()) return false;
        return true;
    }

    public boolean checkCloud(IslandMessage message, int cloud){
        if(cloud >= message.getGroup().size()) return false;
        return true;
    }

    public boolean checkSpecial(int special, int ref, ArrayList<Integer> color1, ArrayList<Integer> color2, SpecialMessage message){
        if(special == 1) {
            if(message.getColor1()[color1.get(0)]==0) return false;
            if(ref<0 && ref >= message.getRefSize()) return false;
        }
        else if(special == 3 || special == 5){
            if(ref<0 && ref >= message.getRefSize()) return false;
        }
        else if(special == 7 || special == 10){
            for(int i=0; i<color1.size(); i++){
                if(message.getColor1()[color1.get(i)] == 0) return false;
                if(message.getColor2()[color2.get(i)] == 0) return false;
                message.getColor1()[color1.get(i)]--;
                message.getColor2()[color2.get(i)]--;
            }
        }
        else if(special == 9 || special==12){
            if(ref<0 || ref>4) return false;
        }
        else if(special == 11){
            if(message.getColor1()[color1.get(0)]==0) return false;
            if(message.getColor2()[color1.get(0)]>=9) return false;
        }
        return true;
    }
}
