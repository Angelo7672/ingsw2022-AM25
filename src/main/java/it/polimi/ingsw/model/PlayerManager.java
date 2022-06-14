package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.listeners.*;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.*;

public class PlayerManager  {
    private Bag bag;
    private List<Player> players;
    private int numberOfPlayer;
    private int[] professorPropriety;

    protected TowersListener towersListener;
    protected ProfessorsListener professorsListener;
    protected CoinsListener coinsListener;
    protected PlayedCardListener playedCardListener;
    protected StudentsListener studentsListener;

    public PlayerManager(int numberOfPlayer, Bag bag) {
        this.bag = bag;
        this.players = new ArrayList<>();
        this.numberOfPlayer = numberOfPlayer;
        this.professorPropriety = new int[]{-1,-1,-1,-1,-1};    //-1 indicates that no one owns that professor

        if (numberOfPlayer == 2) {
            Player g1 = new Player(Team.WHITE, numberOfPlayer);
            players.add(g1);            //insert the firstPlayer in the player list
            Player g2 = new Player(Team.BLACK, numberOfPlayer);
            players.add(g2);
        } else if (numberOfPlayer == 3) {
            Player g1 = new Player(Team.WHITE, numberOfPlayer);
            players.add(g1);
            Player g2 = new Player(Team.BLACK, numberOfPlayer);
            players.add(g2);
            Player g3 = new Player(Team.GREY, numberOfPlayer);
            players.add(g3);
        } else if (numberOfPlayer == 4) {
            Player g1 = new Player(Team.WHITE, numberOfPlayer);
            players.add(g1);
            Player g2 = new Player(Team.WHITE, numberOfPlayer);
            players.add(g2);
            Player g3 = new Player(Team.BLACK, numberOfPlayer);
            players.add(g3);
            Player g4 = new Player(Team.BLACK, numberOfPlayer);
            players.add(g4);
        }
    }

    private String assistantToString(Assistant assistant){
        if(assistant.equals(Assistant.LION)) return "LION";
        else if(assistant.equals(Assistant.GOOSE)) return "GOOSE";
        else if(assistant.equals(Assistant.CAT)) return "CAT";
        else if(assistant.equals(Assistant.EAGLE)) return "EAGLE";
        else if(assistant.equals(Assistant.FOX)) return "FOX";
        else if(assistant.equals(Assistant.LIZARD)) return "LIZARD";
        else if(assistant.equals(Assistant.OCTOPUS)) return "OCTOPUS";
        else if(assistant.equals(Assistant.ELEPHANT)) return "ELEPHANT";
        else if(assistant.equals(Assistant.TURTLE)) return "TURTLE";
        return "NULL";
    }

    public void initializeSchool(){
        if(numberOfPlayer == 2 || numberOfPlayer == 4){
            for(int j = 0; j < numberOfPlayer; j++)
                for (int i = 0; i < 7; i++) setStudentEntrance(j,bag.extraction(),1);
        }else if(numberOfPlayer == 3){
            for(int j = 0; j < numberOfPlayer; j++)
                for (int i = 0; i < 9; i++) setStudentEntrance(j,bag.extraction(),1);
        }
        for(Player p:players)
            p.initializeHand();
    }
    public void restoreSingleSchool(int playerRef, int[] studentsEntrance, int[] studentsTable, int towers, boolean[] professors, Team team){
        //Entrance
        for (int i = 0; i < 5; i++)
            setStudentEntrance(playerRef,i,studentsEntrance[i]);
        //Table
        for (int i = 0; i < 5; i++) {
            try { setStudentTable(playerRef, i, studentsTable[i]);
            } catch (NotAllowedException notAllowedException) {
                System.out.println("Error in loading save!");
                System.exit(-1);
            }
        }
        //Towers
        placeTower(team,towers);
        //Professors
        for (int i = 0; i < 5; i++)
            if(professors[i]) setProfessor(playerRef,i);
    }
    public void restoreHandAndCoins(int playerRef, ArrayList<Assistant> cards, int coins){
        players.get(playerRef).restoreHandAndCoins(cards,coins);
        this.coinsListener.notifyNewCoinsValue(playerRef,getCoins(playerRef));
        ArrayList<String> hand = new ArrayList<>();
        for (Assistant c:cards)
            hand.add(String.valueOf(c));
        this.playedCardListener.notifyHand(playerRef,hand);
    }

    public void playCard(int playerRef, Assistant card, ArrayList<Assistant> alreadyPlayedAssistant) throws NotAllowedException{
        if(!players.get(playerRef).hand.contains(card)) throw new NotAllowedException();
        else {
            for (Assistant alreadyPlayedCard:alreadyPlayedAssistant) {
                if (alreadyPlayedCard.equals(card)) {
                    if (players.get(playerRef).hand.size() > alreadyPlayedAssistant.size()) throw new NotAllowedException();    //if the player has more cards than cards played, he definitely has one card different to play
                    else {
                        if(!(players.get(playerRef).hand.containsAll(alreadyPlayedAssistant) &&
                                alreadyPlayedAssistant.containsAll(players.get(playerRef).hand)))
                            throw new NotAllowedException(); //if an already played card list is different from hand list throw exception
                    }
                }
            }
            players.get(playerRef).hand.remove(card);
        }
    }
    public boolean checkIfCardsFinished(int playerRef){  //Check if the player has played his last card
        return players.get(playerRef).hand.isEmpty();
    }
    public Team checkVictory(){
        Team winner = Team.NONE;
        int numberOfTowers = 9;
        int professors1 = 0, professors2;

        for(Player p:players){
            if(p.school.getTowers() < numberOfTowers){  //looking for the player with the most towers
                numberOfTowers = p.school.getTowers();
                winner = p.getTeam();
                professors1 = 0;
                for(int i = 0; i < 5; i++)
                    if (p.school.getProfessor(i)) professors1++;
            } else if(p.school.getTowers() == numberOfTowers){  //if two players have the same number of towers, look at the professors
                professors2 = 0;
                for(int i = 0; i < 5; i++)
                    if(p.school.getProfessor(i)) professors2++;
                if(professors2 > professors1) {
                    numberOfTowers = p.school.getTowers();
                    winner = p.getTeam();
                    professors1 = professors2;
                }
            }
        }
        return winner;
    }

    public void transferStudent(int playerRef,int colour, boolean inSchool, boolean special) throws NotAllowedException{    //it is used to remove the student from the entrance
        int studentTableThisColour;
        int i;
        boolean stop = false;

        if(!inSchool) {
            removeStudentEntrance(playerRef,colour);  //if inSchool is false, it's placed in an island
        }
        else if(inSchool && !special){   //if inSchool is true, it's placed on the table
            removeStudentEntrance(playerRef,colour);
            setStudentTable(playerRef,colour,1);
            players.get(playerRef).checkPosForCoin(colour);    //check the position, in case we have to give a coin to the player
            studentTableThisColour = getStudentTable(playerRef,colour);
            for (i = 0; i < numberOfPlayer && !stop; i++) {
                if (i != playerRef && studentTableThisColour <= getStudentTable(i,colour))
                    stop = true;  //if it finds someone with more or equals students at the table it stops
                else if (i != playerRef && getProfessor(i,colour)) {
                    removeProfessor(i,colour);    //otherwise, check if the other had the professor
                    setProfessor(playerRef,colour);
                    professorPropriety[colour] = playerRef;
                    stop = true;
                }
            }
            if (i == numberOfPlayer && !stop) {    //if no one owned that professor
                setProfessor(playerRef,colour);
                professorPropriety[colour] = playerRef;
            }
        } else if(inSchool && special){   //if special is true, card special is active
            removeStudentEntrance(playerRef,colour);
            setStudentTable(playerRef,colour,1);
            checkPosForCoin(playerRef,colour);    //check the position, in case we have to give a coin to the player
            studentTableThisColour = getStudentTable(playerRef,colour);
            for (i = 0; i < numberOfPlayer && !stop; i++) {
                if (i != playerRef && studentTableThisColour < getStudentTable(i,colour)) stop = true;  //if it finds someone with more students at the table it stops
                else if (i != playerRef && getProfessor(i,colour)) {
                    removeProfessor(i,colour);    //otherwise, check if the other had the professor
                    setProfessor(playerRef,colour);
                    professorPropriety[colour] = playerRef;
                    stop = true;
                }
            }
            if (i == numberOfPlayer && !stop) {    //if no one owned that professor
                setProfessor(playerRef,colour);
                professorPropriety[colour] = playerRef;
            }
        }
    }
    private void checkPosForCoin(int playerRef, int colour){
        players.get(playerRef).checkPosForCoin(colour);
        this.coinsListener.notifyNewCoinsValue(playerRef,getCoins(playerRef));
    }

    public void setStudentEntrance(int playerRef, int colour, int studentsOfThisColor){
        for(int i = 0; i < studentsOfThisColor; i++) {
            players.get(playerRef).school.setStudentEntrance(colour);
            this.studentsListener.notifyStudentsChange(0, playerRef, colour, getStudentEntrance(playerRef, colour));
        }
    }
    private int getStudentEntrance(int playerRef, int colour){ return players.get(playerRef).school.getStudentEntrance(colour); }
    public void removeStudentEntrance(int playerRef, int colour) throws NotAllowedException{
        players.get(playerRef).school.removeStudentEntrance(colour);
        this.studentsListener.notifyStudentsChange(0, playerRef, colour, getStudentEntrance(playerRef, colour));
    }

    public int getStudentTable(int playerRef, int colour){ return players.get(playerRef).school.getStudentTable(colour); }
    public void setStudentTable(int playerRef, int colour, int studentsOfThisColor) throws NotAllowedException{
        for(int i = 0; i < studentsOfThisColor; i++) {
            players.get(playerRef).school.setStudentTable(colour);
        }
        this.studentsListener.notifyStudentsChange(1, playerRef, colour, getStudentTable(playerRef, colour));
    }
    public void removeStudentTable(int playerRef, int colour) throws NotAllowedException{
        players.get(playerRef).school.removeStudentTable(colour);
        this.studentsListener.notifyStudentsChange(1, playerRef,colour, getStudentTable(playerRef, colour));
    }

    private void setProfessor(int playerRef, int colour){
        players.get(playerRef).school.setProfessor(colour);
        this.professorsListener.notifyProfessors(playerRef, colour,getProfessor(playerRef, colour));
    }
    private void removeProfessor(int playerRef, int colour){
        players.get(playerRef).school.removeProfessor(colour);
        this.professorsListener.notifyProfessors(playerRef, colour,getProfessor(playerRef, colour));
    }
    private boolean getProfessor(int playerRef, int colour){ return players.get(playerRef).school.getProfessor(colour); }
    public int getProfessorPropriety(int color) { return professorPropriety[color]; }

    public boolean removeTower(Team team, int numberOfTower) {
        boolean victory = false;

        for (Player p : players) {
            if (p.getTeam().equals(team)){
                p.school.removeTower(numberOfTower);
                if(p.school.towerExpired()) victory = true;
                this.towersListener.notifyTowersChange(0, players.indexOf(p), p.school.getTowers());
            }
        }
        return victory;
    }
    public void placeTower(Team team, int numberOfTower){
        for (Player p : players) {
            if (p.getTeam().equals(team)){
                p.school.placeTower(numberOfTower);
                this.towersListener.notifyTowersChange(0, players.indexOf(p), p.school.getTowers());
            }
        }
    }

    public Team getTeam(int playerRef){ return players.get(playerRef).getTeam(); }

    public void removeCoin(int playerRef, int cost){
        players.get(playerRef).removeCoin(cost);
        this.coinsListener.notifyNewCoinsValue(playerRef,getCoins(playerRef));
    }
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

    private class Player {
        private final Team team;
        private int coins;
        private List<Assistant> hand;
        private School school;

        private Player(Team team, int numberOfPlayer) {
            this.team = team;
            this.coins = 1;
            this.hand = new ArrayList<>();
            this.school = new School(numberOfPlayer);
        }

        public void initializeHand(){
            hand.add(Assistant.LION);hand.add(Assistant.GOOSE);hand.add(Assistant.CAT);hand.add(Assistant.EAGLE);hand.add(Assistant.FOX);
            hand.add(Assistant.LIZARD);hand.add(Assistant.OCTOPUS);hand.add(Assistant.DOG);hand.add(Assistant.ELEPHANT);hand.add(Assistant.TURTLE);
        }
        public void restoreHandAndCoins(ArrayList<Assistant> cards, int coins){
            this.hand = cards;
            this.coins = coins;
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

            private void setStudentEntrance(int colour) { studentEntrance[colour]++; }  //the exception is not needed because it is not the player who decides how many students to put
            private void removeStudentEntrance(int colour) throws NotAllowedException {
                if (studentEntrance[colour] > 0) studentEntrance[colour]--;
                else throw new NotAllowedException();
            }
            private int getStudentEntrance(int colour){ return studentEntrance[colour]; }

            private void setStudentTable(int colour) throws NotAllowedException{
                if (checkStudentTablePlus(colour)) studentsTable[colour]++;
                else throw new NotAllowedException();
            }
            private void removeStudentTable(int colour) throws NotAllowedException{ //for special character
                if (checkStudentTableMinus(colour)) studentsTable[colour]--;
                else throw new NotAllowedException();
            }
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