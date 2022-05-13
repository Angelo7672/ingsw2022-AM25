package it.polimi.ingsw.server.Answer;


public class SchoolMessage implements Answer{
    private final int towers;
    private final int[] entranceStudent;
    private final int[] tableStudent;
    private final int[] professor;
    private final int coin;
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

    public int getTowers() {
        return towers;
    }

    public int[] getEntranceStudent() {
        return entranceStudent;
    }

    public int[] getTableStudent() {
        return tableStudent;
    }

    public int[] getProfessor() {
        return professor;
    }

    public int getCoin() {
        return coin;
    }
}
