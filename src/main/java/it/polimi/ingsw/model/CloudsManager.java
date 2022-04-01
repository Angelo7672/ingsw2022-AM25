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

        public int[] getStudents() {
            return students;
        }

        public void refreshCloudStudents(){
            for(int i=0; i <= studentNumber; i++){
                int tmp = b.extraction();
                //extraction returns an int which correspond to the student extracted from the bag
                if(tmp == 1)
                    cloud.students[0]++;
                else if(tmp == 2)
                    cloud.students[1]++;
                else if(tmp == 3)
                    cloud.students[2]++;
                else if(tmp == 4)
                    cloud.students[3]++;
                else
                    cloud.students[4]++;
            }

        }
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

        for (int i = 0; i <= cloud.students.length; i++) {
            students[i]=cloud.students[i];
            cloud.students[i]=0;
        }
        return students;
        // returns the array of the students on the cloud, and empties the cloud
    }
}


