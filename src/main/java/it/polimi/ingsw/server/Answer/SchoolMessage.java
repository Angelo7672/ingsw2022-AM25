package it.polimi.ingsw.server.Answer;


public class SchoolMessage implements Answer{
    public final int towers;
    public final int[] entranceStudent;
    public final int[] tableStudent;
    public final int[] professor;
    public final int coin;
    private final String message;

    public SchoolMessage(int towers, int[] entranceStudent, int[] tableStudent, int[] professor, int coin, String message) {
        this.towers = towers;
        this.entranceStudent = entranceStudent;
        this.tableStudent = tableStudent;
        this.professor = professor;
        this.coin = coin;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
