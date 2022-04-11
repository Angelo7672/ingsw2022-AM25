package it.polimi.ingsw.model;

import java.util.*;

public class PlayerManager {
    private List<Player> players;
    private List<Queue> queue;

    private enum Character {WIZARD, KING, WITCH, SAMURAI, NONE};

    public PlayerManager(int numberOfPlayer, String[] playersInfo) {
        players = new ArrayList<>();
        queue = new ArrayList<>();
        Random rnd = new Random();
        int firstInQueue;

        if (numberOfPlayer == 2) {
            Player g1 = new Player(playersInfo[1], stringToCharacter(playersInfo[2]), Team.WHITE, numberOfPlayer);
            Queue firstPlayer = new Queue(playersInfo[1].substring(0, 10));
            players.add(g1);            //insert the firstPlayer in the player list
            queue.add(firstPlayer);     //insert the firstPlayer in the queue for planification phase
            Player g2 = new Player(playersInfo[3], stringToCharacter(playersInfo[4]), Team.BLACK, numberOfPlayer);
            Queue secondPlayer = new Queue(playersInfo[3].substring(0, 10));
            players.add(g2);
            queue.add(secondPlayer);
        } else if (numberOfPlayer == 3) {
            Player g1 = new Player(playersInfo[1], stringToCharacter(playersInfo[2]), Team.WHITE, numberOfPlayer);
            Queue firstPlayer = new Queue(playersInfo[1].substring(0, 10));
            players.add(g1);
            queue.add(firstPlayer);
            Player g2 = new Player(playersInfo[3], stringToCharacter(playersInfo[4]), Team.BLACK, numberOfPlayer);
            Queue secondPlayer = new Queue(playersInfo[3].substring(0, 10));
            players.add(g2);
            queue.add(secondPlayer);
            Player g3 = new Player(playersInfo[5], stringToCharacter(playersInfo[6]), Team.GREY, numberOfPlayer);
            Queue thirdPlayer = new Queue(playersInfo[5].substring(0, 10));
            players.add(g3);
            queue.add(thirdPlayer);
        } else if (numberOfPlayer == 4) {
            Player g1 = new Player(playersInfo[1], stringToCharacter(playersInfo[2]), Team.WHITE, numberOfPlayer);
            Queue firstPlayer = new Queue(playersInfo[1].substring(0, 10));
            players.add(g1);
            queue.add(firstPlayer);
            Player g2 = new Player(playersInfo[3], stringToCharacter(playersInfo[4]), Team.WHITE, numberOfPlayer);
            Queue secondPlayer = new Queue(playersInfo[3].substring(0, 10));
            players.add(g2);
            queue.add(secondPlayer);
            Player g3 = new Player(playersInfo[5], stringToCharacter(playersInfo[6]), Team.BLACK, numberOfPlayer);
            Queue thirdPlayer = new Queue(playersInfo[5].substring(0, 10));
            players.add(g3);
            queue.add(thirdPlayer);
            Player g4 = new Player(playersInfo[7], stringToCharacter(playersInfo[8]), Team.BLACK, numberOfPlayer);
            Queue fourthPlayer = new Queue(playersInfo[7].substring(0, 10));
            players.add(g4);
            queue.add(fourthPlayer);
        }

        //The first player is chosen randomly, then proceeds clockwise. The distribution of players at the table is arranged clockwise in this order 1 2 3 4
        firstInQueue = rnd.nextInt(numberOfPlayer - 1);
        if (firstInQueue != 0)
            if (firstInQueue == 1) {
                Collections.rotate(queue, 1);
            } else if (firstInQueue == 2){
                Collections.rotate(queue, 2);
            }else if (firstInQueue == 3){
                Collections.rotate(queue,3);
            }
    }

    private Character stringToCharacter(String string){
        if(string.equals("WIZARD")) return Character.WIZARD;
        else if(string.equals("KING")) return Character.KING;
        else if(string.equals("WITCH")) return Character.WITCH;
        else if(string.equals("SAMURAI")) return Character.SAMURAI;
        return Character.NONE;
    }

    public String readQueue(int ref){ return queue.get(ref).getNickname(); }

    private void checkPosForCoin(Player player, int colour){
        if(player.school.getStudentTable(colour)==3 || player.school.getStudentTable(colour)==6 || player.school.getStudentTable(colour)==9) player.giveCoin();
    }

    public void transferStudent(int playerRef,int colour, boolean inSchool){    //it is used to remove the student from the entrance
        if(!inSchool){  //if inSchool is false, it's placed in a island
            if(players.get(playerRef).school.getStudentEntrance(colour) > 0) {
                players.get(playerRef).school.removeStudentEntrance(colour);
            }
        }
        if(inSchool){   //if inSchool is true, it's placed on the table
            if(players.get(playerRef).school.getStudentEntrance(colour) > 0)
                players.get(playerRef).school.removeStudentEntrance(colour);
                players.get(playerRef).school.setStudentEntrance(colour);
                checkPosForCoin(players.get(playerRef),colour); //check the position, in case we have to give a coin to the player
        }
    }

    public boolean checkVictory(Player player){     //Check if the player has built his last tower
        if(player.school.getTowers()==0) return true;
        return false;
    }

    public boolean checkIfCardsFinished(Player player){  //Check if the player has played his last card
        return player.hand.isEmpty();
    }

    public String noMoreCards(){
        String winner = "none";
        int numberOfTowers = 9;
        int professors1 = 0, professors2;

        for(Player p:players){
            if(p.school.getTowers() < numberOfTowers){
                numberOfTowers = p.school.getTowers();
                winner = p.getNickname();
                professors1 = 0;
                for(int i = 0; i < 5; i++) {
                    if (p.school.getProfessor(i)) professors1++;
                }
            } else if(p.school.getTowers() == numberOfTowers){
                professors2 = 0;
                for(int i = 0; i < 5; i++){
                    if(p.school.getProfessor(i)) professors2++;
                }
                if(professors2 > professors1)
                    numberOfTowers = p.school.getTowers();
                winner = p.getNickname();                       //da fixare con nome squadra
                professors1 = professors2;
            }
        }
        return winner;
    }







    private class Queue{
        private final String nickname;
        private int valueCard;

        public Queue(String nickname) {
            this.nickname = nickname;
            this.valueCard = 0;
        }

        public String getNickname() { return nickname; }
        public int getValueCard() { return valueCard; }
    }

    private class Player {
        private final String nickname;  //limited to 10 characters
        private final Character character;
        private final Team team;
        private int coins;
        private Assistant lastCard;     //questo forse ha senso metterlo nel controller (mettere li' una board)
        private List<Assistant> hand;
        private School school;

        private enum Assistant {        //penso vada tolta da qui
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
            this.hand = new ArrayList<>();
            hand.add(Assistant.LION);hand.add(Assistant.GOOSE);hand.add(Assistant.GOOSE);hand.add(Assistant.CAT);hand.add(Assistant.EAGLE);hand.add(Assistant.FOX);
            hand.add(Assistant.LIZARD);hand.add(Assistant.OCTOPUS);hand.add(Assistant.DOG);hand.add(Assistant.ELEPHANT);hand.add(Assistant.TURTLE);
            this.school = new School(numberOfPlayer);
        }

        public String getNickname() { return nickname; }
        public Character getCharacter() { return character; }
        public Team getTeam() { return team; }
        public int getCoins() { return coins; }
        public void giveCoin() { coins++; }
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
            public boolean getProfessor(int colour){ return professors[colour]; }

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
            public int getStudentEntrance(int colour){ return studentEntrance[colour]; }

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
