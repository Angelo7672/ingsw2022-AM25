package it.polimi.ingsw.model;

import java.util.*;

public class PlayerManager  {
    private List<Player> players;
    private List<Queue> queue;
    private int numberOfPlayer;
    private int[] professorPropriety;

    private enum Character {WIZARD, KING, WITCH, SAMURAI, NONE};

    public PlayerManager(int numberOfPlayer, String[] playersInfo) {
        players = new ArrayList<>();
        queue = new ArrayList<>();
        this.numberOfPlayer = numberOfPlayer;
        this.professorPropriety = new int[]{-1,-1,-1,-1,-1};    //-1 indicates that no one owns that professor
        Random rnd = new Random();
        int firstInQueue;

        if (numberOfPlayer == 2) {
            Player g1 = new Player(playersInfo[0], stringToCharacter(playersInfo[1]), Team.WHITE, numberOfPlayer);
            players.add(g1);            //insert the firstPlayer in the player list
            Player g2 = new Player(playersInfo[2], stringToCharacter(playersInfo[3]), Team.BLACK, numberOfPlayer);
            players.add(g2);
        } else if (numberOfPlayer == 3) {
            Player g1 = new Player(playersInfo[0], stringToCharacter(playersInfo[1]), Team.WHITE, numberOfPlayer);
            players.add(g1);
            Player g2 = new Player(playersInfo[2], stringToCharacter(playersInfo[3]), Team.BLACK, numberOfPlayer);
            players.add(g2);
            Player g3 = new Player(playersInfo[4], stringToCharacter(playersInfo[5]), Team.GREY, numberOfPlayer);
            players.add(g3);
        } else if (numberOfPlayer == 4) {
            Player g1 = new Player(playersInfo[0], stringToCharacter(playersInfo[1]), Team.WHITE, numberOfPlayer);
            players.add(g1);
            Player g2 = new Player(playersInfo[2], stringToCharacter(playersInfo[3]), Team.WHITE, numberOfPlayer);
            players.add(g2);
            Player g3 = new Player(playersInfo[4], stringToCharacter(playersInfo[5]), Team.BLACK, numberOfPlayer);
            players.add(g3);
            Player g4 = new Player(playersInfo[6], stringToCharacter(playersInfo[7]), Team.BLACK, numberOfPlayer);
            players.add(g4);
        }
        //The first player is chosen randomly, then queueForPlanificationPhase order the queue.
        firstInQueue = rnd.nextInt(numberOfPlayer - 1);
        Queue first = new Queue(firstInQueue,-1,-1);
        queue.add(first);
    }

    private Character stringToCharacter(String string){
        if(string.equals("WIZARD")) return Character.WIZARD;
        else if(string.equals("KING")) return Character.KING;
        else if(string.equals("WITCH")) return Character.WITCH;
        else if(string.equals("SAMURAI")) return Character.SAMURAI;
        return Character.NONE;
    }

    public void queueForPlanificationPhase(int numberOfPlayer){
        int firstInQueue;

        firstInQueue = queue.get(0).getPlayerRef();         //destroy the previous queue
        while (!queue.isEmpty()) queue.remove(0);

        Queue one = new Queue(0,-1,-1);
        Queue two = new Queue(1,-1,-1);
        queue.add(one);queue.add(two);
        if(numberOfPlayer == 3){
            Queue three = new Queue(2,-1,-1);
            queue.add(three);
        }else if(numberOfPlayer == 4){
            Queue three = new Queue(2,-1,-1);
            Queue four = new Queue(3,-1,-1);
            queue.add(three);queue.add(four);
        }
        //The first player is the one who played first in the previous action phase, then proceeds clockwise.The distribution of players at the table is arranged clockwise in this order 1 2 3 4
        if (firstInQueue != 0) {
            if (firstInQueue == 1) {
                Collections.rotate(queue, 1);
            } else if (firstInQueue == 2) {
                Collections.rotate(queue, 2);
            } else if (firstInQueue == 3) {
                Collections.rotate(queue, 3);
            }
        }
    }
    public Team playCard(int playerRef, int queueRef, Assistant card){
        players.get(playerRef).hand.remove(card);
        queue.get(queueRef).setValueCard(card.getValue());
        queue.get(queueRef).setMaxMoveMotherNature(card.getMovement());
        if(checkIfCardsFinished(playerRef)) return checkVictory();
        return Team.NOONE;
    }
    public boolean checkIfCardsFinished(int playerRef){  //Check if the player has played his last card
        return players.get(playerRef).hand.isEmpty();
    }

    public List<Assistant> getHand(int playerRef){ return players.get(playerRef).hand; }

    public Team checkVictory(){
        Team winner = Team.NOONE;
        int numberOfTowers = -1;
        int professors1 = 0, professors2;

        for(Player p:players){
            if(p.school.getTowers() > numberOfTowers){  //looking for the player with the most towers
                numberOfTowers = p.school.getTowers();
                winner = p.getTeam();
                professors1 = 0;
                for(int i = 0; i < 5; i++) {
                    if (p.school.getProfessor(i)) professors1++;
                }
            } else if(p.school.getTowers() == numberOfTowers){  //if two players have the same number of towers, look at the professors
                professors2 = 0;
                for(int i = 0; i < 5; i++){
                    if(p.school.getProfessor(i)) professors2++;
                }
                if(professors2 > professors1)
                    numberOfTowers = p.school.getTowers();
                winner = p.getTeam();
                professors1 = professors2;
            }
        }
        return winner;
    }
    public void inOrderForActionPhase(){
        Collections.sort(queue, new Comparator<Queue>() {
            @Override
            public int compare(Queue q1, Queue q2) {
                return q1.compareTo(q2);
            }
        });
    }

    public int readQueue(int queueRef){ return queue.get(queueRef).getPlayerRef(); }
    public int readMaxMotherNatureMovement(int queueRef){ return queue.get(queueRef).getMaxMoveMotherNature(); }

    public void transferStudent(int playerRef,int colour, boolean inSchool, boolean special){    //it is used to remove the student from the entrance
        int studentTableofThisColour = -1;
        int i;
        boolean stop = false;

        if(!inSchool){  //if inSchool is false, it's placed in a island
            if(getStudentEntrance(playerRef, colour) > 0) {
               removeStudentEntrance(playerRef,colour);
            }
        } else if(inSchool && !special){   //if inSchool is true, it's placed on the table
            if(getStudentEntrance(playerRef,colour) > 0) {
                removeStudentEntrance(playerRef,colour);
                setStudentTable(playerRef,colour);
                players.get(playerRef).checkPosForCoin(colour);    //check the position, in case we have to give a coin to the player
                studentTableofThisColour = getStudentTable(playerRef, colour);
                for (i = 0; i < numberOfPlayer && !stop; i++) {
                    if (i != playerRef && studentTableofThisColour <= getStudentTable(i, colour))
                        stop = true;  //if it finds someone with more or equals students at the table it stops
                    else if (i != playerRef && getProfessor(i,colour)) {
                        removeProfessor(i, colour);    //otherwise check if the other had the professor
                        setProfessor(playerRef, colour);
                        professorPropriety[colour] = playerRef;
                        stop = true;
                    }
                }
                if (i == numberOfPlayer) {    //if no one owned that professor
                    setProfessor(playerRef, colour);
                    professorPropriety[colour] = playerRef;
                }
            }
        } else if(inSchool && special){   //if special is true, card special is active
            if(getStudentEntrance(playerRef,colour) > 0) {
                removeStudentEntrance(playerRef,colour);
                setStudentTable(playerRef,colour);
                players.get(playerRef).checkPosForCoin(colour);    //check the position, in case we have to give a coin to the player
                studentTableofThisColour = getStudentTable(playerRef, colour);
                for (i = 0; i < numberOfPlayer && !stop; i++) {
                    if (i != playerRef && studentTableofThisColour < getStudentTable(i, colour))
                        stop = true;  //if it finds someone with more students at the table it stops
                    else if (i != playerRef && getProfessor(i,colour)) {
                        removeProfessor(i, colour);    //otherwise check if the other had the professor
                        setProfessor(playerRef, colour);
                        professorPropriety[colour] = playerRef;
                        stop = true;
                    }
                }
                if (i == numberOfPlayer) {    //if no one owned that professor
                    setProfessor(playerRef, colour);
                    professorPropriety[colour] = playerRef;
                }
            }
        }
    }

    public void setStudentEntrance(int playerRef, int colour){ players.get(playerRef).school.setStudentEntrance(colour); }
    public int getStudentEntrance(int playerRef, int colour){ return players.get(playerRef).school.getStudentEntrance(colour); }
    public void removeStudentEntrance(int playerRef, int colour){ players.get(playerRef).school.removeStudentEntrance(colour); }

    public int getStudentTable(int playerRef, int colour){ return players.get(playerRef).school.getStudentTable(colour); }
    public void setStudentTable(int playerRef, int colour){ players.get(playerRef).school.setStudentTable(colour); }
    public void removeStudentTable(int playerRef, int colour){ players.get(playerRef).school.removeStudentTable(colour); }

    private void setProfessor(int playerRef, int colour){ players.get(playerRef).school.setProfessor(colour); }
    private void removeProfessor(int playerRef, int colour){ players.get(playerRef).school.removeProfessor(colour); }
    public boolean getProfessor(int playerRef, int colour){ return players.get(playerRef).school.getProfessor(colour); }
    public int getProfessorPropriety(int color) { return professorPropriety[color]; }

    public boolean removeTower(Team team, int numberOfTower) {
        boolean victory = false;

        for (Player p : players) {
            if (p.getTeam().equals(team)){
                p.school.removeTower(numberOfTower);
                if(p.school.towerExpired()) victory = true;
            }
        }
        return victory;
    }
    public void placeTower(Team team, int numberOfTower){
        for (Player p : players) {
            if (p.getTeam().equals(team)) p.school.placeTower(numberOfTower);
        }
    }
    public int getTowers(int playerRef){ return players.get(playerRef).school.getTowers(); }

    public Team getTeam(int playerRef){ return players.get(playerRef).getTeam(); }

    public void removeCoin(int playerRef, int cost){ players.get(playerRef).removeCoin(cost); }
    public int getCoins(int playerRef){ return players.get(playerRef).getCoins(); }

    public boolean checkStudentsEntrance(ArrayList<Integer> student, int playerRef){
        for(int i=0; i<student.size(); i++){
            if(getStudentEntrance(playerRef,student.get(i))==0) return false;
        }
        return true;
    }
    public boolean checkStudentsTable(ArrayList<Integer> student, int playerRef){
        if(getStudentTable(playerRef,student.get(0))>=student.size()) return true;
        return false;
    }

    private class Queue implements Comparable<Queue>{   //it is used both in the planning phase and in the action phase
        private int playerRef;
        private Integer valueCard;
        private int maxMoveMotherNature;

        public Queue(int playerRef,Integer valueCard,int maxMoveMotherNature) {
            this.playerRef = playerRef;
            this.valueCard = valueCard;
            this.maxMoveMotherNature = maxMoveMotherNature;
        }

        private int getPlayerRef() { return playerRef; }
        private Integer getValueCard() { return valueCard; }
        public int getMaxMoveMotherNature() { return maxMoveMotherNature; }

        public void setValueCard(Integer valueCard) { this.valueCard = valueCard; }
        public void setMaxMoveMotherNature(int maxMoveMotherNature) { this.maxMoveMotherNature = maxMoveMotherNature; }

        @Override
        public int compareTo(Queue o) {
            return valueCard.compareTo(o.getValueCard());
        }
    }

    private class Player {
        private final String nickname;  //limited to 10 characters
        private final Character character;
        private final Team team;
        private int coins;
        //private Assistant lastCard;     //questo forse ha senso metterlo nel controller (mettere li' una board)
        private List<Assistant> hand;
        private School school;

        private Player(String nickname, Character character, Team team, int numberOfPlayer) {
            this.nickname = nickname.substring(Math.min(nickname.length(), 10));
            this.character = character;
            this.team = team;
            this.coins = 1;
            this.hand = new ArrayList<>();
            hand.add(Assistant.LION);hand.add(Assistant.GOOSE);hand.add(Assistant.CAT);hand.add(Assistant.EAGLE);hand.add(Assistant.FOX);
            hand.add(Assistant.LIZARD);hand.add(Assistant.OCTOPUS);hand.add(Assistant.DOG);hand.add(Assistant.ELEPHANT);hand.add(Assistant.TURTLE);
            this.school = new School(numberOfPlayer);
        }

        private Team getTeam() { return team; }

        private int getCoins() { return coins; }
        private void checkPosForCoin(int colour){
            if(school.getStudentTable(colour)==3 || school.getStudentTable(colour)==6 || school.getStudentTable(colour)==9) giveCoin();
        }
        private void giveCoin() { coins++; }
        private void removeCoin(int cost) { coins-=cost; }

        private class School {
            private int towers;
            private boolean professors[];
            private int studentEntrance[];
            private int studentsTable[];

            private School(int numberOfPlayer) {
                this.professors = new boolean[]{false, false, false, false, false};
                this.studentEntrance = new int[]{0, 0, 0, 0, 0};
                this.studentsTable = new int[]{0, 0, 0, 0, 0};
                if (numberOfPlayer == 2 || numberOfPlayer == 4) this.towers = 8;
                else this.towers = 6;
            }

            private void setProfessor(int colour) { professors[colour] = true; }
            private void removeProfessor(int colour) { professors[colour] = false; }
            private boolean getProfessor(int colour){ return professors[colour]; }

            private void setStudentEntrance(int colour) { studentEntrance[colour]++; }
            private void removeStudentEntrance(int colour) { if (studentEntrance[colour] > 0) studentEntrance[colour]--; }
            private int getStudentEntrance(int colour){ return studentEntrance[colour]; }

            private void setStudentTable(int colour) { if (checkStudentTablePlus(colour)) studentsTable[colour]++; }
            private void removeStudentTable(int colour){ if (checkStudentTableMinus(colour)) studentsTable[colour]--;}  //for special character
            private boolean checkStudentTablePlus(int colour) { return studentsTable[colour] <= 9; }    //Students at the table must be in range [0,10]
            private boolean checkStudentTableMinus(int colour) { return studentsTable[colour] != 0; }   //Students at the table must be in range [0,10]
            private int getStudentTable(int colour) { return studentsTable[colour]; }

            private void placeTower(int number) { towers+=number; }
            private void removeTower(int number) { towers-=number ; }
            private int getTowers() { return towers; }
            private boolean towerExpired(){ return getTowers() == 0; } //Check if the player has built his last tower
        }
    }
}