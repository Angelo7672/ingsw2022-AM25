package it.polimi.ingsw.client.message;

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
