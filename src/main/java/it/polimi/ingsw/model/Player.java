package it.polimi.ingsw.model;

public class Player {
    private String nickname;
    private Character character;
    private Team team;
    private int number;             //serve veramente? Forse basta nickname
    private int coins;
    private Assistant lastCard;     //questo forse ha senso metterlo nel controller (mettere li' una board)
    private Assistant hand[] = new Assistant[] { Assistant.LION, Assistant.GOOSE,Assistant.CAT, Assistant.EAGLE, Assistant.FOX,
                                                 Assistant.LIZARD, Assistant.OCTOPUS, Assistant.DOG, Assistant.ELEPHANT, Assistant.TURTLE };     //questo forse va messo diretto nel client
    private School school;

    private enum Character{ WIZARD, KING, WITCH, SAMURAI };
    private enum Team{ WHITE, BLACK, GREY };

    private enum Assistant{
        LION(1,1), GOOSE(2,1), CAT(3,2), EAGLE(4,2), FOX(5,3),
        LIZARD(6,3), OCTOPUS(7,4), DOG(8,4), ELEPHANT(9,5), TURTLE(10,5);
        private final int value;
        private final int movement;

        Assistant(int value,int movement){ this.value = value; this.movement = movement; }

        public int getValue(Assistant) { return value; }
        public int getMovement(Assistant) { return movement; }
    }

    private class School{
        private int towers;
        private boolean professors[] = new boolean[] {false,false,false,false,false};
        private int studentEntrance[5];
        private int studentsTable[5];

        public void setProfessors(int colour) {...}

        public void setStudentEntrance(int colour) {..}
    }


}
