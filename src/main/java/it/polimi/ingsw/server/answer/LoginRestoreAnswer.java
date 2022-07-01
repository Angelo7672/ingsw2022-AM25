package it.polimi.ingsw.server.answer;

/**
 * LoginRestoreAnswer invite client to procede with login, inserting the nickname of previous game.
 */
public class LoginRestoreAnswer implements Answer{
    private String msg;

    /**
     * Create an answer contains a message "Insert nickname for restore last game".
     */
    public LoginRestoreAnswer() { this.msg = "Insert nickname for restore last game"; }
    public String getMsg() { return msg; }
}
