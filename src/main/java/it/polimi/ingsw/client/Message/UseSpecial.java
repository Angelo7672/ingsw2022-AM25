package it.polimi.ingsw.client.Message;

import java.util.ArrayList;

public class useSpecial {
    public final int specialIndex;
    public final int ref;
    public final ArrayList<Integer> color1;
    public final ArrayList<Integer> color2;


    //used for noone(-1), special2, special4, special5, special6, special8,
    public useSpecial(int specialIndex) {
        this.specialIndex = specialIndex;
        ref = 0;
        color1 = null;
        color2 = null;
    }

    //used for special3, special9, special12
    public useSpecial(int specialIndex, int ref) {
        this.specialIndex = specialIndex;
        this.ref = ref;
        color1 = null;
        color2 = null;
    }

    //used for special1, special11
    public useSpecial(int specialIndex, int ref, ArrayList<Integer> color1) {
        this.specialIndex = specialIndex;
        this.ref = ref;
        this.color1 = color1;
        color2 = null;
    }

    //used for special7, special10
    public useSpecial(int specialIndex, int ref, ArrayList<Integer> color1, ArrayList<Integer> color2) {
        this.specialIndex = specialIndex;
        this.ref = ref;
        this.color1 = color1;
        this.color2 = color2;
    }


}
