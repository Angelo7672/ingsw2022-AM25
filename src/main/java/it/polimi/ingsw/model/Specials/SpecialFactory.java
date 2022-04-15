package it.polimi.ingsw.model.Specials;

public class SpecialFactory {



    SpecialFactory(){}

    public Special getSpecial(int i){
        Special special;
        switch (i){
            case(1): special = new Special1(); break;
            case(2): special = new Special2(); break;
            case(3): special = new Special3(); break;
            case(4): special = new Special4(); break;
            case(5): special = new Special5(); break;
            case(6): special = new Special6(); break;
            case(7): special = new Special7(); break;
            case(8): special = new Special8(); break;
            case(9): special = new Special9(); break;
            case(10): special = new Special10(); break;
            case(11): special = new Special11(); break;
            case(12): special = new Special12(); break;
            default: special = null;
        }
        return special;
    }

    public void effect(){

    }


}
