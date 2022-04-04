package it.polimi.ingsw.model;

public class Player {
    private final String nickname;  //da limitare
    private final Character character;
    private final Team team;
    private int coins;
    private Assistant lastCard;     //questo forse ha senso metterlo nel controller (mettere li' una board)
    private Assistant hand[] = new Assistant[] { Assistant.LION, Assistant.GOOSE,Assistant.CAT, Assistant.EAGLE, Assistant.FOX,
                                                 Assistant.LIZARD, Assistant.OCTOPUS, Assistant.DOG, Assistant.ELEPHANT, Assistant.TURTLE };     //questo forse va messo diretto nel client
    private School school;

    private enum Character{ WIZARD, KING, WITCH, SAMURAI };

    private enum Assistant{
        LION(1,1), GOOSE(2,1), CAT(3,2), EAGLE(4,2), FOX(5,3),
        LIZARD(6,3), OCTOPUS(7,4), DOG(8,4), ELEPHANT(9,5), TURTLE(10,5);
        private final int value;
        private final int movement;

        Assistant(int value,int movement) {     //ma se faccio cosi' do' la possibilita' di creare nuovi assistenti
            this.value = value;
            this.movement = movement;
        }

        public int getValue() { return value; }
        public int getMovement() { return movement; }
    }

    public Player(String nickname, Character character, Team team, int numberOfPlayer){
        this.nickname = nickname;
        this.character = character;
        this.team = team;
        this.coins = 1;
        school.setTowers(numberOfPlayer);
    }

    public String getNickname() { return nickname; }
    public Character getCharacter() { return character; }
    public Team getTeam() { return team; }
    public int getCoins() { return coins; }
    public Assistant getLastCard() { return lastCard; }
    public void setLastCard(Assistant lastCard) { this.lastCard = lastCard; }

    private class School{
        private int towers;
        private boolean professors[] = new boolean[] {false, false, false, false, false};
        private int studentEntrance[]= {0,0,0,0,0};
        private int studentsTable[] = {0,0,0,0,0};

        public void setProfessors(int colour) { }

        public void setStudentEntrance(int colour) { }


        public void setTowers(int towers) { }
        public int getTowers() { return towers; }
        public void placeTower() { towers++; }
        public void removeTower() { towers--; }
    }


}
