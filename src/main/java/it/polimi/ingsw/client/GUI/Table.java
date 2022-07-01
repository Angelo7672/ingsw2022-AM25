package it.polimi.ingsw.client.GUI;
import javafx.scene.image.Image;

/**
 * Tabel class is used to handle the creation and deletion of students at the schools' tables
 */
public class Table {
    private final String GREENSTUDENT = "/graphics/wooden_pieces/greenStudent3D.png";
    private final String REDSTUDENT = "/graphics/wooden_pieces/redStudent3D.png";
    private final String YELLOWSTUDENT = "/graphics/wooden_pieces/yellowStudent3D.png";
    private final String PINKSTUDENT = "/graphics/wooden_pieces/pinkStudent3D.png";
    private final String BLUESTUDENT = "/graphics/wooden_pieces/blueStudent3D.png";
    private final int SPACE = 18; //space between the students

    int x;
    int y;
    int studentsNumber;
    int color;
    Image studentImage;

    /**
     * Constructor, sets the initial coordinates for the students, and the color of the table
     * @param initialX of type int - initial x of the students
     * @param initialY of type int - initial y of the students
     * @param color of type int - color of the table
     */
    public Table(int initialX, int initialY, int color){
        this.x=initialX;
        this.y=initialY;
        this.studentsNumber=0;
        this.color=color;
        if(color==0){
            this.studentImage = new Image(GREENSTUDENT);
        } else if(color==1){
            this.studentImage = new Image(REDSTUDENT);
        } else if(color==2){
            this.studentImage = new Image(YELLOWSTUDENT);
        } else if(color==3){
            this.studentImage = new Image(PINKSTUDENT);
        } else if(color==4){
            this.studentImage = new Image(BLUESTUDENT);
        }

    }
    public int getX() {
        return x;
    }

    /**
     * After creating a student or removing one, set the new coordinates of the last student
     * @param schoolRef of type int - is the index of the school
     * @param isAdded of type boolean - is true is the student is added, false if removed
     */
    public void setNewX(int schoolRef, boolean isAdded) {
        if(isAdded){
            if(schoolRef==0)
                this.x = x+SPACE;
            else if(schoolRef==1){
                this.x=x-SPACE;
            }
        } else {
            if(schoolRef==0)
                this.x = x-SPACE;
            else if(schoolRef==1){
                this.x=x+SPACE;
            }
        }
    }
    public int getY() {
        return y;
    }
    /**
     * After creating a student or removing one, set the new coordinates of the last student
     * @param schoolRef of type int - is the index of the school
     * @param isAdded of type boolean - is true is the student is added, false if removed
     */
    public void setNewY(int schoolRef, boolean isAdded) {
        if(isAdded){
            if(schoolRef==2){
                this.y=y+SPACE;
            } else if(schoolRef==3){
                this.y=y-SPACE;
            }
        } else {
            if(schoolRef==2){
                this.y=y-SPACE;
            } else if(schoolRef==3){
                this.y=y+SPACE;
            }
        }

    }
    /**
     * @return the number of students
     */
    public int getStudentsNumber() {
        return studentsNumber;
    }

    /**
     * Add a student at the table
     */
    public void addStudent() {
        this.studentsNumber++;
    }
    /**
     * Removes a student from the table
     */
    public void removeStudent(){
        this.studentsNumber--;
    }

    /**
     * @return the image of the student
     */
    public Image getStudentImage() {
        return studentImage;
    }
}
