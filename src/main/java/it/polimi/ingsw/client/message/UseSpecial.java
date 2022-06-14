package it.polimi.ingsw.client.message;

import java.util.ArrayList;

public class UseSpecial implements Message{
    private int indexSpecial;
    private int ref1;
    private int ref2;
    private int color;
    private ArrayList<Integer> color1;
    private ArrayList<Integer> color2;

    public UseSpecial(int indexSpecial, int playerRef) {
        this.indexSpecial = indexSpecial;
        this.ref1 = playerRef;
    }

    public UseSpecial(int indexSpecial, int playerRef, int ref){
        this.indexSpecial = indexSpecial;
        this.ref1 = playerRef;
        this.ref2 = ref;
    }

    public UseSpecial(int indexSpecial, int playerRef, int ref, int color){
        this.indexSpecial = indexSpecial;
        this.ref1 = playerRef;
        this.ref2 = ref;
        this.color = color;
    }

    public UseSpecial(int indexSpecial, ArrayList<Integer> color1, ArrayList<Integer> color2){
        this.indexSpecial = indexSpecial;
        this.color1 = color1;
        this.color2 = color2;
    }


    public int getIndexSpecial() {
        return indexSpecial;
    }

    public int getRef() {
        return ref1;
    }

    public ArrayList<Integer> getColor1() {
        return color1;
    }

    public ArrayList<Integer> getColor2() {
        return color2;
    }
}
