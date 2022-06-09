package it.polimi.ingsw.server.Answer;

public class LoginRestoreAnswer implements Answer{
    private String msg;

    public LoginRestoreAnswer() { this.msg = "Insert nickname for restore last game"; }
    public String getMsg() { return msg; }
}
