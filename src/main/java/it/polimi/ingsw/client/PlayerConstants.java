package it.polimi.ingsw.client;


/**
 * This class is used tto know which phase have been done and continue with the next one.
 */
public class PlayerConstants {
    private boolean planningPhaseStarted;
    private boolean cardPlayed;
    private boolean actionPhaseStarted;
    private boolean studentMoved;
    private boolean specialUsed;
    private boolean motherMoved;
    private boolean cloudChosen;
    private boolean startGame;
    private boolean endTurn;

    public PlayerConstants(){
        this.planningPhaseStarted = false;
        this.cardPlayed = false;
        this.actionPhaseStarted = false;
        this.studentMoved = false;
        this.specialUsed = false;
        this.motherMoved = false;
        this.cloudChosen = false;
        this.startGame = false;
        this.endTurn = false;
    }

    public boolean isCardPlayed() {
        return cardPlayed;
    }

    public void setCardPlayed(boolean cardPlayed) {
        this.cardPlayed = cardPlayed;
    }

    public boolean isActionPhaseStarted() {
        return actionPhaseStarted;
    }

    public void setActionPhaseStarted(boolean actionPhaseStarted) {
        this.actionPhaseStarted = actionPhaseStarted;
    }

    public boolean isStudentMoved() {
        return studentMoved;
    }

    public void setStudentMoved(boolean studentMoved) {
        this.studentMoved = studentMoved;
    }

    public boolean isSpecialUsed() {
        return specialUsed;
    }

    public void setSpecialUsed(boolean specialUsed) {
        this.specialUsed = specialUsed;
    }

    public boolean isMotherMoved() {
        return motherMoved;
    }

    public void setMotherMoved(boolean motherMoved) {
        this.motherMoved = motherMoved;
    }

    public boolean isCloudChosen() {
        return cloudChosen;
    }

    public void setCloudChosen(boolean cloudChosen) {
        this.cloudChosen = cloudChosen;
    }

    public boolean isStartGame() {
        return startGame;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }


    /**
     * @return the phase that have to be done.
     */
    public String lastPhase(){
        if(!isCardPlayed()) return "PlayCardAnswer";
        if(!isStudentMoved()) return "MoveStudent";
        if(!isMotherMoved()) return "MoveMother";
        if(!isCloudChosen()) return "ChooseCloud";
        return "EndTurn";
    }

    public void resetAll(){
        this.cardPlayed = false;
        this.actionPhaseStarted = false;
        this.studentMoved = false;
        this.specialUsed = false;
        this.motherMoved = false;
        this.cloudChosen = false;
    }


    public boolean isEndTurn() {
        return endTurn;
    }

    public void setEndTurn(boolean endTurn) {
        this.endTurn = endTurn;
    }

    public boolean isPlanningPhaseStarted() {
        return planningPhaseStarted;
    }

    public void setPlanningPhaseStarted(boolean planningPhaseStarted) {
        this.planningPhaseStarted = planningPhaseStarted;
    }

}
