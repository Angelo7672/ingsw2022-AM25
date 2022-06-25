package it.polimi.ingsw.client.GUI;

import javafx.scene.image.Image;

public class Table {
    private final String GREENSTUDENT = "/graphics/wooden_pieces/greenStudent3D.png";
    private final String REDSTUDENT = "/graphics/wooden_pieces/redStudent3D.png";
    private final String YELLOWSTUDENT = "/graphics/wooden_pieces/yellowStudent3D.png";
    private final String PINKSTUDENT = "/graphics/wooden_pieces/pinkStudent3D.png";
    private final String BLUESTUDENT = "/graphics/wooden_pieces/blueStudent3D.png";

    private final int SPACE = 18;

    int x;
    int y;
    int studentsNumber;
    int color;
    Image studentImage;

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
    public void setNewX(int schoolRef) {
        if(schoolRef==0)
            this.x = x+SPACE;
        else if(schoolRef==1){
            this.x=x-SPACE;
        }
    }
    public int getY() {
        return y;
    }
    public void setNewY(int schoolRef) {
        if(schoolRef==2){
            this.y=y+SPACE;
        } else if(schoolRef==3){
            this.y=y-SPACE;
        }
    }
    public int getStudentsNumber() {
        return studentsNumber;
    }
    public void setStudentsNumber() {
        this.studentsNumber++;
    }
    public Image getStudentImage() {
        return studentImage;
    }
}
