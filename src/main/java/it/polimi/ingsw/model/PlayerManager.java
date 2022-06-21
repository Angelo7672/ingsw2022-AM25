package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.*;

/**
 * PlayerManger keeps track of all information regarding players and their schools.
 * Notify method send changes from PlayerManager to VirtualView.
 */
public class PlayerManager  {
    private final Bag bag;
    private final List<Player> players; //contains information about each player, including their school
    private final int numberOfPlayer;
    private final int[] professorPropriety;   //in each cell it indicates the playerRef of who owns that professor
    //Colour has the following legend: GREEN-->0, RED-->1, YELLOW-->2, PINK-->3, BLUE-->4
    protected TowersListener towersListener;
    protected ProfessorsListener professorsListener;
    protected CoinsListener coinsListener;
    protected PlayedCardListener playedCardListener;
    protected StudentsListener studentsListener;

    /**
     * Constructor PlayerManager creates a new PlayerManager instance.
     * The TEAM is imposed by the game:
     *  -)In the 2-player game the first player is on the white team, the second player is on the black team;
     *  -)In the 3-player game the first player is on the white team, the second player is on the black team and the third player is on the gray team;
     *  -)In the 4-player game the first and second player are on the white team, the third and fourth are on the black team;
     * @param numberOfPlayer is the number of user in this game;
     * @param bag contains the student tokens to be extracted;
     */
    public PlayerManager(int numberOfPlayer, Bag bag) {
        this.bag = bag;
        this.players = new ArrayList<>();
        this.numberOfPlayer = numberOfPlayer;
        this.professorPropriety = new int[]{-1,-1,-1,-1,-1};    //-1 indicates that no one owns that professor

        if (numberOfPlayer == 2) {
            Player g1 = new Player(Team.WHITE);
            players.add(g1);            //insert the firstPlayer in the player list
            Player g2 = new Player(Team.BLACK);
            players.add(g2);
        } else if (numberOfPlayer == 3) {
            Player g1 = new Player(Team.WHITE);
            players.add(g1);
            Player g2 = new Player(Team.BLACK);
            players.add(g2);
            Player g3 = new Player(Team.GREY);
            players.add(g3);
        } else if (numberOfPlayer == 4) {
            Player g1 = new Player(Team.WHITE);
            players.add(g1);
            Player g2 = new Player(Team.WHITE);
            players.add(g2);
            Player g3 = new Player(Team.BLACK);
            players.add(g3);
            Player g4 = new Player(Team.BLACK);
            players.add(g4);
        }
    }

    /**
     * Initialize the school by following the rules of the game:
     *  -)2 or 4 players, 7 students at the entrance and 8 towers in the school;
     *  -)3 players, 9 students at the entrance and 6 towers in the school;
     */
    public void initializeSchool(){
        if(numberOfPlayer == 2 || numberOfPlayer == 4){
            for(int j = 0; j < numberOfPlayer; j++)
                for (int i = 0; i < 7; i++) setStudentEntrance(j,bag.extraction(),1);
        }else if(numberOfPlayer == 3){
            for(int j = 0; j < numberOfPlayer; j++)
                for (int i = 0; i < 9; i++) setStudentEntrance(j,bag.extraction(),1);
        }
        if (numberOfPlayer == 2 || numberOfPlayer == 4){
            placeTower(Team.WHITE,8);
            placeTower(Team.BLACK,8);
        } else{
            placeTower(Team.WHITE,6);
            placeTower(Team.BLACK,6);
            placeTower(Team.GREY,6);
        }
        for(Player p:players)
            p.initializeHand();
    }

    /**
     * Restore the school of a player.
     * @param playerRef the player reference;
     * @param studentsEntrance students situated in the entrance in the previous game;
     * @param studentsTable students situated in the table in the previous game;
     * @param towers in the previous game;
     * @param professors in the previous game;
     * @param team in the previous game;
     */
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
        if(numberOfPlayer == 4) {
            if (playerRef == 0 || playerRef == 2) placeTower(team, towers);
        }else placeTower(team,towers);
        //Professors
        for (int i = 0; i < 5; i++)
            if(professors[i]) setProfessor(playerRef,i);
    }

    /**
     * Restore cards and coins of a player.
     * @param playerRef the player reference;
     * @param cards int the hand of the player in the previous game;
     * @param coins of the player in the previous game;
     */
    public void restoreHandAndCoins(int playerRef, ArrayList<Assistant> cards, int coins){
        players.get(playerRef).restoreHandAndCoins(cards,coins);
        this.coinsListener.notifyNewCoinsValue(playerRef,getCoins(playerRef));
        ArrayList<String> hand = new ArrayList<>();
        for (Assistant c:cards)
            hand.add(String.valueOf(c));
        this.playedCardListener.notifyHand(playerRef,hand);
    }

    /**
     * Check if the card chosen by the player is valid, then remove it from the list
     * @param playerRef the player reference;
     * @param card the card reference;
     * @param alreadyPlayedAssistant list of just played cards in this turn;
     * @throws NotAllowedException throw in case of an already played card or the player doesn't have the card in his hand;
     */
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

    /**
     * Check if the player has played his last card
     * @param playerRef the player reference;
     * @return if player has finished his cards;
     */
    public boolean checkIfCardsFinished(int playerRef){ return players.get(playerRef).hand.isEmpty(); }

    /**
     * In a condition of game over, this method check which team have won this match.
     * In case of equal number of towers and professors, the player who is positioned in the lower index cell in the 'players' list wins
     * @return the team winner
     */
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

    /**
     * Move student of a colour from entrance to table and check if is case of give the professor of that colour to the players.
     * Otherwise, remove the student of a colour from the school and that's it.
     * @param playerRef the player reference;
     * @param colour the colour reference;
     * @param inSchool boolean indicating if the student have to place in school or not;
     * @param special boolean indicating if special2 is active;
     * @throws NotAllowedException throw in case of player doesn't have that student's colour in his entrance or his table is full of that student's colour;
     */
    public void transferStudent(int playerRef,int colour, boolean inSchool, boolean special) throws NotAllowedException{    //it is used to remove the student from the entrance
        int studentTableThisColour;
        int i;
        boolean stop = false;

        if(!inSchool) removeStudentEntrance(playerRef,colour);  //if inSchool is false, it's placed in an island
        else if(!special){   //if inSchool is true, it's placed on the table
            removeStudentEntrance(playerRef,colour);
            setStudentTable(playerRef,colour,1);
            checkPosForCoin(playerRef,colour);    //check the position, in case we have to give a coin to the player
            studentTableThisColour = getStudentTable(playerRef,colour);
            for (i = 0; i < numberOfPlayer && !stop; i++) {
                if (i != playerRef && studentTableThisColour <= getStudentTable(i,colour)) stop = true;  //if it finds someone with more or equals students at the table it stops
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
        } else {   //if special is true, card special is active
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

    /**
     * Check if is the case of given a coin to the player.
     * @param playerRef the player reference;
     * @param colour the colour reference;
     */
    private void checkPosForCoin(int playerRef, int colour){
        if(players.get(playerRef).checkPosForCoin(colour)) this.coinsListener.notifyNewCoinsValue(playerRef,getCoins(playerRef));
    }

    /**
     * Set student in the entrance of a player.
     * @param playerRef the player reference;
     * @param colour the colour reference;
     * @param studentsOfThisColor is the number of students to set in the entrance;
     */
    public void setStudentEntrance(int playerRef, int colour, int studentsOfThisColor){
        for(int i = 0; i < studentsOfThisColor; i++) {
            players.get(playerRef).school.setStudentEntrance(colour);
            this.studentsListener.notifyStudentsChange(0, playerRef, colour, getStudentEntrance(playerRef, colour));
        }
    }
    private int getStudentEntrance(int playerRef, int colour){ return players.get(playerRef).school.getStudentEntrance(colour); }

    /**
     * Remove student from entrance of a player.
     * @param playerRef the player reference;
     * @param colour the colour reference;
     * @throws NotAllowedException throw in case of player doesn't have that student's colour in his entrance;
     */
    public void removeStudentEntrance(int playerRef, int colour) throws NotAllowedException{
        players.get(playerRef).school.removeStudentEntrance(colour);
        this.studentsListener.notifyStudentsChange(0, playerRef, colour, getStudentEntrance(playerRef, colour));
    }

    public int getStudentTable(int playerRef, int colour){ return players.get(playerRef).school.getStudentTable(colour); }

    /**
     * Set student in the table of a player.
     * @param playerRef the player reference;
     * @param colour the colour reference;
     * @param studentsOfThisColor is the number of students to set in the entrance;
     * @throws NotAllowedException throw in case of player's table is full of that student's colour;
     */
    public void setStudentTable(int playerRef, int colour, int studentsOfThisColor) throws NotAllowedException{
        for(int i = 0; i < studentsOfThisColor; i++) {
            players.get(playerRef).school.setStudentTable(colour);
        }
        this.studentsListener.notifyStudentsChange(1, playerRef, colour, getStudentTable(playerRef, colour));
    }

    /**
     * Remove student from table of a player. Used by special10 and special12.
     * @param playerRef the player reference;
     * @param colour the colour reference;
     */
    public void removeStudentTable(int playerRef, int colour) {
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

    /**
     * Remove a number of towers from every player of a team.
     * @param team from which remove tower;
     * @param numberOfTower to remove;
     * @return a boolean that if is true, the game is over;
     */
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

    /**
     * Add a number of towers to each player of a team.
     * @param team to whom add tower;
     * @param numberOfTower to add;
     */
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

    /**
     * Check if a player has the student chosen in his entrance. Used by special7 and special10.
     * @param student a list of student's colour to check presence in school;
     * @param playerRef the player reference;
     * @return if the operation was successful;
     */
    public boolean checkStudentsEntranceForSpecial(ArrayList<Integer> student, int playerRef){
        for (Integer integer : student)
            if (getStudentEntrance(playerRef, integer) == 0) return false;
        return true;
    }

    /**
     * Check if a player has the student chosen in his table. Used by special10.
     * @param student a list of student's colour
     * @param playerRef the player reference;
     * @return if the operation was successful;
     */
    public boolean checkStudentsTableForSpecial(ArrayList<Integer> student, int playerRef){
        for (Integer integer : student)
            if(getStudentTable(playerRef, integer)==0) return false;
        return true;
    }

    /**
     * This class contain info of a player: his team, his coins, his hand and his school.
     */
    private static class Player {
        private final Team team;
        private int coins;
        private List<Assistant> hand;
        private final School school;

        private Player(Team team) {
            this.team = team;
            this.coins = 1;
            this.hand = new ArrayList<>();
            this.school = new School();
        }

        private void initializeHand(){
            hand.add(Assistant.LION); hand.add(Assistant.GOOSE); hand.add(Assistant.CAT); hand.add(Assistant.EAGLE); hand.add(Assistant.FOX);
            hand.add(Assistant.LIZARD); hand.add(Assistant.OCTOPUS); hand.add(Assistant.DOG); hand.add(Assistant.ELEPHANT); hand.add(Assistant.TURTLE);
        }

        /**
         * Restore the school of a player.
         * @param cards list of cards of last game;
         * @param coins of last game;
         */
        private void restoreHandAndCoins(ArrayList<Assistant> cards, int coins){
            this.hand = cards;
            this.coins = coins;
        }

        private Team getTeam() { return team; }

        private int getCoins() { return coins; }

        /**
         * If there is 3, 6 or 9 students in a specified table, give a coin.
         * @param colour of table to check;
         */
        private boolean checkPosForCoin(int colour){
            if(school.getStudentTable(colour) == 3 || school.getStudentTable(colour) == 6 || school.getStudentTable(colour) == 9){
                coins++;
                System.out.println("add coin");
                return true;
            }
            return false;
        }
        private void removeCoin(int cost) { coins-=cost; }

        /**
         * This class contains info about player's school.
         */
        private static class School {
            private int towers;
            private final boolean[] professors;
            private final int[] studentEntrance;
            private final int[] studentsTable;

            /**
             * Create school empty, without professors, towers and students.
             */
            private School() {
                this.professors = new boolean[]{false, false, false, false, false};
                this.studentEntrance = new int[]{0, 0, 0, 0, 0};
                this.studentsTable = new int[]{0, 0, 0, 0, 0};
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
            private void removeStudentTable(int colour) { //for special character
                if (checkStudentTableMinus(colour)) studentsTable[colour]--;
            }

            /**
             * Students at the table must be in range [0,10].
             * @param colour to check;
             * @return if the operation was successful;
             */
            private boolean checkStudentTablePlus(int colour) { return studentsTable[colour] <= 9; }

            /**
             * Students at the table must be in range [0,10]
             * @param colour to check;
             * @return if the operation was successful;
             */
            private boolean checkStudentTableMinus(int colour) { return studentsTable[colour] != 0; }
            private int getStudentTable(int colour) { return studentsTable[colour]; }

            private void placeTower(int number) { towers+=number; }
            private void removeTower(int number) { towers-=number ; }
            private int getTowers() { return towers; }

            /**
             * Check if the player has built his last tower
             * @return if game is over;
             */
            private boolean towerExpired(){ return getTowers() <= 0; }
        }
    }
}