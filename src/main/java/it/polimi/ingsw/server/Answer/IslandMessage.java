package it.polimi.ingsw.server.Answer;

public class IslandMessage implements Answer{
    private final int[] students;
    private final String message;

    public IslandMessage(int[] students, String message) {
        this.students = students;
        this.message = message;
    }

    public int[] getStudents(){
        return students;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
