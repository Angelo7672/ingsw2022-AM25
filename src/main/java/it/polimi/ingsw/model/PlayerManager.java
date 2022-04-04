package it.polimi.ingsw.model;

import java.util.ArrayList;

public class PlayerManager{
    ArrayList<Player> players;
    ArrayList<Queue> queue;


    private class Queue{
        private String nickname;
        private int valueCard;
    }

    private class Player {
        private final String nickname;  //limited to 10 characters
        private final Character character;
        private final Team team;
        private int coins;
        private Assistant lastCard;     //questo forse ha senso metterlo nel controller (mettere li' una board)
        private Assistant hand[] = new Assistant[]{Assistant.LION, Assistant.GOOSE, Assistant.CAT, Assistant.EAGLE, Assistant.FOX,
                                                   Assistant.LIZARD, Assistant.OCTOPUS, Assistant.DOG, Assistant.ELEPHANT, Assistant.TURTLE};     //questo forse va messo diretto nel client
        private School school;

        private enum Character {WIZARD, KING, WITCH, SAMURAI};
        private enum Assistant {
            LION(1, 1), GOOSE(2, 1), CAT(3, 2), EAGLE(4, 2), FOX(5, 3),
            LIZARD(6, 3), OCTOPUS(7, 4), DOG(8, 4), ELEPHANT(9, 5), TURTLE(10, 5);
            private final int value;
            private final int movement;

            Assistant(int value, int movement) {
                this.value = value;
                this.movement = movement;
            }

            public int getValue() { return value; }
            public int getMovement() { return movement; }
        }

        public Player(String nickname, Character character, Team team, int numberOfPlayer) {
            this.nickname = nickname.substring(0, 10);
            this.character = character;
            this.team = team;
            this.coins = 1;
            this.school = new School(numberOfPlayer);
        }

        public String getNickname() { return nickname; }
        public Character getCharacter() { return character; }
        public Team getTeam() { return team; }
        public int getCoins() { return coins; }
        public Assistant getLastCard() { return lastCard; }
        public void setLastCard(Assistant lastCard) { this.lastCard = lastCard; }

        private class School {
            private int towers;
            private boolean professors[];
            private int studentEntrance[];
            private int studentsTable[];
            private int numberOfPlayer;

            public School(int numberOfPlayer) {
                this.professors = new boolean[]{false, false, false, false, false};
                this.studentEntrance = new int[]{0, 0, 0, 0, 0};
                this.studentsTable = new int[]{0, 0, 0, 0, 0};
                if (numberOfPlayer == 2 || numberOfPlayer == 4) this.towers = 8;
                else this.towers = 6;
                this.numberOfPlayer = numberOfPlayer;
            }

            public void setProfessor(int colour) { professors[colour] = true; }
            public void removeProfessor(int colour) { professors[colour] = false; }

            public void setStudentEntrance(int colour) { if (checkStudentEntrance()) studentEntrance[colour]++; }
            public void removeStudentEntrance(int colour) { if (checkStudentEntrance()) studentEntrance[colour]--; }

            private boolean checkStudentEntrance() {    //Students in the entrance must be in range [0,10]
                int sum = 0;
                if (numberOfPlayer == 2 || numberOfPlayer == 4) {
                    for (int i = 0; i < 5; i++)
                        sum += studentEntrance[i];
                    if (sum > 6 || sum < 1) return false;   //in teoria non dovrebbe mai essere false
                    return true;
                } else if (numberOfPlayer == 3) {
                    for (int i = 0; i < 5; i++)
                        sum += studentEntrance[i];
                    if (sum > 8 || sum < 1) return false;   //in teoria non dovrebbe mai essere false
                    return true;
                }
                return false;
            }

            public void setStudentTable(int colour) { if (checkStudentTable(colour)) studentsTable[colour]++; }
            private boolean checkStudentTable(int colour) {    //Students at the table must be in range [0,10]
                if (studentsTable[colour] > 9) return false;
                return true;
            }
            public int getStudentTable(int colour) { return studentsTable[colour]; }

            public void placeTower() { if (checkTower()) towers++; }
            public void removeTower() { towers--; }
            private boolean checkTower() {
                if (numberOfPlayer == 2 || numberOfPlayer == 4) {
                    if (towers > 7) return false;
                    return true;
                } else if (numberOfPlayer == 3) {
                    if (towers > 5) return false;
                    return true;
                }
                return false;
            }
            public int getTowers() { return towers; }
        }
    }
}
