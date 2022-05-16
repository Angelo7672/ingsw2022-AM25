package it.polimi.ingsw.server.Answer;

public class CloudMessage implements Answer{
    private final int[] students;
    private final String message;

    public CloudMessage(int[] students, String message) {
            this.students = students;
            this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
    public int[] getStudents(){
            return students;
        }
}

