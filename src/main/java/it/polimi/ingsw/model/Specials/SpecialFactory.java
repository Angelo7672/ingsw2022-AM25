package it.polimi.ingsw.model.Specials;

public class SpecialFactory {

    SpecialFactory(){}

    public Special getSpecial(int i){
        Special special;
        switch (i){
            case(1): return new Special1();
            case(2): return new Special2();
            case(3): return new Special3();
            case(4): return new Special4();
            case(5): return new Special5();
            case(6): return new Special6();
            case(7): return new Special7();
            case(8): return new Special8();
            case(9): return new Special9();
            case(10): return new Special10();
            case(11): return new Special11();
            case(12): return new Special12();
            default: return null;
        }
    }

    public void effect(){

    }


}
