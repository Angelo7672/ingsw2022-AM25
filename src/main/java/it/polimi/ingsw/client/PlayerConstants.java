package it.polimi.ingsw.client;

public class PlayerConstants {//Meglio farle attivare/disattivare da server
    private boolean cardPlayed;
    private boolean actionPhaseStarted;
    private boolean studentMoved;
    private boolean specialUsed;
    private boolean motherMoved;
    private boolean cloudChosen;
    private boolean endTurn;

    public PlayerConstants(){
        this.cardPlayed = true;
        this.actionPhaseStarted = true;
        this.studentMoved = true;
        this.specialUsed = true;
        this.motherMoved = true;
        this.cloudChosen = true;
        this.endTurn = true;
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

    public boolean isEndTurn() {
        return endTurn;
    }

    public void setEndTurn(boolean endTurn) {
        this.endTurn = endTurn;
    }


    public String lastPhase(){
        if(!isCardPlayed()) return "PlayCard";
        if(!isStudentMoved()) return "MoveStudent";
        if(!isMotherMoved()) return "MoveMother";
        if(!isCloudChosen()) return "ChoseCloud";
        return "EndTurn";
    }

    public void resetAll(){
        this.cardPlayed = false;
        this.actionPhaseStarted = false;
        this.studentMoved = false;
        this.specialUsed = false;
        this.motherMoved = false;
        this.cloudChosen = false;
        this.endTurn = false;
    }


}
