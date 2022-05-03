package it.polimi.ingsw.server.Answer;


public class SchoolMessage implements Answer{
    private final int towers;
    private final int[] entranceStudent;
    private final int[] tableStudent;
    private final int[] professor;
    private final int coin;

    public SchoolMessage(int towers, int[] entranceStudent, int[] tableStudent, int[] professor, int coin) {
        this.towers = towers;
        this.entranceStudent = entranceStudent;
        this.tableStudent = tableStudent;
        this.professor = professor;
        this.coin = coin;
    }

    @Override
    public Object getMessage() {
        return null;
    }
}
