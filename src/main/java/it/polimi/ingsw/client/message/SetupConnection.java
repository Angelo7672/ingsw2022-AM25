package it.polimi.ingsw.client.message;

/**
 * SetupConnection contains the chosen nickname and character.
 */
public class SetupConnection implements  Message{
    private final String nickname;
    private final String character;

    public SetupConnection(String nickname, String character) {
        this.nickname = nickname;
        this.character = character;
    }

    public String getNickname() {
        return nickname;
    }
    public String getCharacter(){
        return character;
    }
}
