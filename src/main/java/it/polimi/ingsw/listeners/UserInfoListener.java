package it.polimi.ingsw.listeners;

/**
 * Used to notify the nickname and character of a player
 */
public interface UserInfoListener {
    void userInfoNotify(String nickname, String Character, int playerRef);
}
