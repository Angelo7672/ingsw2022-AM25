package it.polimi.ingsw.model;

public class CloudsManager implements GameSetup {

    private Cloud cloud;
    private Bag b = new Bag();

    private class Cloud{
        private int[] students= {0,0,0,0,0};
        private int studentNumber;

        // Cloud constructor
        public Cloud(int studentNumber){
            this.studentNumber = studentNumber;
        }

        public void refreshCloudStudents() {
            for (int i = 1; i <= studentNumber; i++) {
                for(int j = 1; j <= 5; j++){
                    if(b.extraction() == j){
                        students[j-1]++;
                    }
                }
            }
        }

    }

    public int getStudentNumber() {
        return cloud.studentNumber;
    }

    public int[] getStudents() {
        return cloud.students;
    }

    @Override // override GameSetup's method
    public void setup (int playersNumber){
        if(playersNumber == 2 || playersNumber == 4){
            cloud = new Cloud(3);
        }
        else {
            cloud = new Cloud(4);
        }
        cloud.refreshCloudStudents();
        // fill the cloud with the students extracted from the bag
    }

    public int[] removeStudents() {
        int[] students = new int[cloud.students.length];

        for (int i = 0; i < cloud.students.length; i++) {
            students[i]=cloud.students[i];
            cloud.students[i]=0;
        }
        return students;
        // returns the array of the students on the cloud, and empties the cloud
    }
}


