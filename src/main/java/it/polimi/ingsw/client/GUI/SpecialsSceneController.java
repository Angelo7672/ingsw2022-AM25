package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * SpecialsSceneController is the controller of the specials scene.
 */
public class SpecialsSceneController implements SceneController{

    private final String special1 = "/graphics/specials/CarteTOT_front1.jpg";
    private final String special2 = "/graphics/specials/CarteTOT_front2.jpg";
    private final String special3 = "/graphics/specials/CarteTot_front3.jpg";
    private final String special4 = "/graphics/specials/CarteTot_front4.jpg";
    private final String special5 = "/graphics/specials/CarteTot_front5.jpg";
    private final String special6 = "/graphics/specials/CarteTot_front6.jpg";
    private final String special7 = "/graphics/specials/CarteTot_front7.jpg";
    private final String special8 = "/graphics/specials/CarteTot_front8.jpg";
    private final String special9 = "/graphics/specials/CarteTot_front9.jpg";
    private final String special10 = "/graphics/specials/CarteTot_front10.jpg";
    private final String special11 = "/graphics/specials/CarteTot_front11.jpg";
    private final String special12 = "/graphics/specials/CarteTot_front12.jpg";
    private final String GREENSTUDENT = "/graphics/wooden_pieces/greenStudent3D.png";
    private final String REDSTUDENT = "/graphics/wooden_pieces/redStudent3D.png";
    private final String YELLOWSTUDENT = "/graphics/wooden_pieces/yellowStudent3D.png";
    private final String PINKSTUDENT = "/graphics/wooden_pieces/pinkStudent3D.png";
    private final String BLUESTUDENT = "/graphics/wooden_pieces/blueStudent3D.png";

    private Exit proxy;
    private GUI gui;
    private HashMap<Integer, Image> specials;
    private ArrayList<Integer> specialsName;
    private HashMap<Integer, Integer> specialsCost;
    private int specialChosen;
    private int studentChosen;
    private ArrayList<Integer> studentsChosen;

    @FXML private Button confirmButton;
    @FXML private Button addButton;
    @FXML private ImageView coinSpecial1;
    @FXML private ImageView coinSpecial2;
    @FXML private ImageView coinSpecial3;
    @FXML private Button confirmSpecialButton;
    @FXML private Label errorMessage;
    @FXML private AnchorPane paneSpecial1;
    @FXML private AnchorPane paneSpecial2;
    @FXML private AnchorPane paneSpecial3;
    @FXML private Label questionLabel;
    @FXML private Button special1Button;
    @FXML private Label special1Cost;
    @FXML private ImageView special1View;
    @FXML private Button special2Button;
    @FXML private Label special2Cost;
    @FXML private ImageView special2View;
    @FXML private Button special3Button;
    @FXML private Label special3Cost;
    @FXML private ImageView special3View;
    @FXML private Label noEntry1;
    @FXML private Label noEntry2;
    @FXML private Label noEntry3;
    @FXML private ImageView noEntryView1;
    @FXML private ImageView noEntryView2;
    @FXML private ImageView noEntryView3;
    @FXML private Label label2;


    public SpecialsSceneController(){
        specialChosen=-1;
        studentChosen=-1;
    }

    /**
     * Initialized scene with specials list and their cost.
     * @param specialsList is an ArrayList with the 3 specials extracted.
     * @param cost is an ArraList with the cost of each special.
     */
    public void initializedSpecialsScene(ArrayList<Integer> specialsList, ArrayList<Integer> cost){
        this.specials = new HashMap<>();
        this.specialsName = new ArrayList<>();
        this.specialsCost = new HashMap<>();
        for (int i = 0; i < specialsList.size(); i++) {
            specials.put(i, specialFactory(specialsList.get(i)));
            specialsName.add(i, specialsList.get(i));
            specialsCost.put(i, cost.get(i));
            if(specialsList.get(i)==1 || specialsList.get(i)==7 || specialsList.get(i)==11) showStudents(i);
        }
        special1View.setImage(specials.get(0));
        special2View.setImage(specials.get(1));
        special3View.setImage(specials.get(2));
        for (int i = 0; i < 3; i++) {
            if(specialsName.get(i)==1 || specialsName.get(i)==7 || specialsName.get(i)==11) showStudents(i);
            else if(specialsName.get(i)==5){
                if(i==0) {
                    noEntry1.setVisible(true);
                    noEntryView1.setVisible(true);
                } else if(i==1){
                    noEntry2.setVisible(true);
                    noEntryView2.setVisible(true);
                } else if(i==2){
                    noEntry3.setVisible(true);
                    noEntryView3.setVisible(true);
                }
            }
        }
        studentsChosen = new ArrayList<>();
        coinSpecial1.setVisible(false);
        coinSpecial2.setVisible(false);
        coinSpecial3.setVisible(false);
        special1Cost.setVisible(false);
        special2Cost.setVisible(false);
        special3Cost.setVisible(false);
    }

    /**
     * if the action phase of the player is started the confirm button is visible, else it's set as invisible.
     * @param actionPhase true if action phase is started.
     */
    protected void setConfirmButton(boolean actionPhase){
        if(actionPhase) confirmButton.setVisible(true);
        else confirmButton.setVisible(false);
    }

    /**
     * After the use of some specials who change images they have to be reset with this method.
     */
    public void resetScene(){
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
        questionLabel.setText("Do you want to use a special card?");
        questionLabel.setVisible(true);
        confirmButton.setVisible(true);
        special1View.setVisible(true);
        special1Button.setVisible(true);
        label2.setVisible(false);
        if(!special1Cost.toString().equals("0")) {
            special1Cost.setVisible(true);
            coinSpecial1.setVisible(true);
        }
        if(!special2Cost.toString().equals("0")) {
            special2Cost.setVisible(true);
            coinSpecial2.setVisible(true);
        }
        if(!special3Cost.toString().equals("0")) {
            special3Cost.setVisible(true);
            coinSpecial3.setVisible(true);
        }
        special2View.setVisible(true);
        special2Button.setVisible(true);
        special3View.setVisible(true);
        special3Button.setVisible(true);
        addButton.setVisible(false);
        confirmSpecialButton.setVisible(false);
        errorMessage.setVisible(false);
        specialChosen = -1;
        studentChosen = -1;
        for (int i = 0; i < 3; i++) {
            if(specialsName.get(i)==1 || specialsName.get(i)==7 || specialsName.get(i)==11) showStudents(i);
            else if(specialsName.get(i)==5) showNoEntry(i);
        }

    }

    /**
     * return the image of the special.
     * @param name is the number of the special.
     * @return return the image.
     */
    private Image specialFactory(int name){
        switch (name){
            case 1 -> {return new Image(special1);}
            case 2 -> {return new Image(special2);}
            case 3 -> {return new Image(special3);}
            case 4 -> {return new Image(special4);}
            case 5 -> {return new Image(special5);}
            case 6 -> {return new Image(special6);}
            case 7 -> {return new Image(special7);}
            case 8 -> {return new Image(special8);}
            case 9 -> {return new Image(special9);}
            case 10 -> {return new Image(special10);}
            case 11 -> {return new Image(special11);}
            case 12 -> {return new Image(special12);}
        }
        return null;
    }

    /**
     * Set the new cost of a special.
     * @param specialIndex is the index of the ArrayList of the special.
     * @param newValue is the new cost.
     */
    protected void setCoins(int specialIndex, int newValue){
        if(newValue-specialsCost.get(specialIndex)>0) {
            if (specialIndex == 0) {
                special1Cost.setText(Integer.toString(newValue - specialsCost.get(specialIndex)));
                if (!special1Cost.toString().equals("0")) {
                    special1Cost.setVisible(true);
                    coinSpecial1.setVisible(true);
                }
            } else if (specialIndex == 1) {
                special2Cost.setText(Integer.toString(newValue - specialsCost.get(specialIndex)));
                if (!special2Cost.toString().equals("0")) {
                    special2Cost.setVisible(true);
                    coinSpecial2.setVisible(true);
                }
            } else if (specialIndex == 2) {
                special3Cost.setText(Integer.toString(newValue - specialsCost.get(specialIndex)));
                if (!special3Cost.toString().equals("0")) {
                    special3Cost.setVisible(true);
                    coinSpecial3.setVisible(true);
                }
            }
        }
    }

    /**
     * Set the new student of a special.
     * @param specialIndex is the index of the ArrayList of the special.
     * @param color is the color of the new students.
     * @param value is the new number of students.
     */
    protected void setStudent(int specialIndex, int color, int value){
        if(specialsName.indexOf(specialIndex)==0){
            Label student = (Label) paneSpecial1.getChildren().get(color+5);
            student.setText(String.valueOf(value));
        }
        else if(specialsName.indexOf(specialIndex)==1){
            Label student = (Label) paneSpecial2.getChildren().get(color+5);
            student.setText(String.valueOf(value));
        }
        else if(specialsName.indexOf(specialIndex)==2){
            Label student = (Label) paneSpecial3.getChildren().get(color+5);
            student.setText(String.valueOf(value));
        }
    }

    /**
     * Set the new value of the No Entry tiles.
     * @param special is the index of the ArrayList of the special.
     * @param value is the new number of the No Entry tiles.
     */
    protected void setNoEntry(int special, int value){
        if(special==0) noEntry1.setText(String.valueOf(value));
        else if(special==1) noEntry2.setText(String.valueOf(value));
        else if(special==2) noEntry3.setText(String.valueOf(value));
    }

    /**
     * After clicking a special this method set the specialChosen variable with his name.
     * @param event is the click on a special.
     */
    @FXML
    public void setSpecialChosen(ActionEvent event){
        if(event.getSource()==special1Button){
            specialChosen = specialsName.get(0);
        } else if(event.getSource()==special2Button){
            specialChosen = specialsName.get(1);
        } else if(event.getSource()==special3Button){
            specialChosen = specialsName.get(2);
        }
    }

    /**
     * After clicking a student this method set the studentChosen variable with his color.
     * @param event is the click on a student.
     */
    @FXML
    public void setStudentColor(ActionEvent event){
        if(event.getSource()==paneSpecial1.getChildren().get(0) || event.getSource()==paneSpecial2.getChildren().get(0) || event.getSource()==paneSpecial3.getChildren().get(0))
            studentChosen = 0;
        else if(event.getSource()==paneSpecial1.getChildren().get(1) || event.getSource()==paneSpecial2.getChildren().get(1) || event.getSource()==paneSpecial3.getChildren().get(1))
        studentChosen = 1;
        else if(event.getSource()==paneSpecial1.getChildren().get(2) || event.getSource()==paneSpecial2.getChildren().get(2) || event.getSource()==paneSpecial3.getChildren().get(2))
            studentChosen = 2;
        else if(event.getSource()==paneSpecial1.getChildren().get(3) || event.getSource()==paneSpecial2.getChildren().get(3) || event.getSource()==paneSpecial3.getChildren().get(3))
            studentChosen = 3;
        else if(event.getSource()==paneSpecial1.getChildren().get(4) || event.getSource()==paneSpecial2.getChildren().get(4) || event.getSource()==paneSpecial3.getChildren().get(4))
            studentChosen = 4;
    }

    /**
     * When confirm button is clicked the method call the proxy to check if player can use that special.
     * @param event is the click on confirm button.
     */
    @FXML
    public void confirmPressed(ActionEvent event) {
        if(specialChosen != -1 && gui.constants.isActionPhaseStarted() && !gui.constants.isSpecialUsed()){
            errorMessage.setVisible(false);
            boolean result = false;
            try {
                result = proxy.checkSpecial(specialChosen);
            }catch (IOException | ClassNotFoundException e ){}
            if(result) {
                if(specialChosen!=2 && specialChosen!=4 && specialChosen!=6 && specialChosen!=8) {
                    useSpecial(specialChosen);
                }
                else {
                    Stage stage = (Stage) confirmButton.getScene().getWindow();
                    stage.close();
                    gui.setConstants("SpecialUsed");
                }
            }
            else showErrorMessage();
        }
        else showErrorMessage();
    }

    /**
     * When confirm special button is clicked the method call the proxy to check if move is allowed or call
     * a gui method to continue to use the special in other scene.
     * @param event is the click on confirm special button.
     */
    @FXML
    public void confirmSpecialButtonPressed(ActionEvent event) {
        if(specialChosen==7){
            addButton.setVisible(false);
            System.out.println(studentsChosen);
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
            gui.useSpecial(specialChosen, studentsChosen);
        }
        else if(specialChosen==11 && studentChosen != -1){
            boolean result=false;
            addButton.setVisible(false);
            try {
                result = proxy.useSpecial(11, studentChosen);
            }catch (IOException e){}
            if(result){
                gui.setConstants("SpecialUsed");
                Stage stage = (Stage) confirmButton.getScene().getWindow();
                stage.close();
            }
            else {
                gui.specialNotAllowed();
                showErrorMessage();
            }
        }
        else if(specialChosen == 1 && studentChosen != -1){
            gui.useSpecial(specialChosen, studentChosen);
        }
        else showErrorMessage();
    }

    /**
     * When add button is clicked the method add the student chosen to an ArrayList.
     * @param event is the click on add button.
     */
    @FXML
    public void addButtonPressed(ActionEvent event){
        if(studentChosen!=-1 && studentsChosen.size()<3) {
            studentsChosen.add(studentChosen);
            String update = printArrayColor(studentsChosen);
            label2.setText("Students moved "+studentsChosen.size()+" of 3:"+ update);
        }

    }

    /**
     * Used to create a String with chosen color.
     * @param students ArrayList with students chosen.
     * @return the string.
     */
    private String printArrayColor(ArrayList<Integer> students){
        String string=" ";
        String color=" ";
        for (int i = 0; i < students.size(); i++) {
            if(students.get(i)==0) color=" Green";
            else if(students.get(i)==1) color=" Red";
            else if(students.get(i)==2) color=" Yellow";
            else if(students.get(i)==3) color=" Pink";
            else if(students.get(i)==4) color=" Blue";
            string = string + color;
        }
        System.out.println(string);
        return string;
    }

    /**
     * Call a gui method to continue to use a special on another scene or set SpecialsScene to use it here.
     * @param special is the special chosen.
     */
    private void useSpecial(int special){
        if(special == 1) {
            disableImage(specialsName.indexOf(1));
            confirmSpecialButton.setVisible(true);
            questionLabel.setText("Which student do you want to move?");
        }
        else if(special == 3 || special == 4 || special == 5 || special == 9 || special == 12){
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
            gui.useSpecial(special);
        }
        else if(special == 7){
            disableImage(specialsName.indexOf(7));
            confirmSpecialButton.setVisible(true);
            addButton.setVisible(true);
            questionLabel.setText("Which student do you want to move?");
            label2.setVisible(true);
            label2.setText("Students moved "+studentsChosen.size()+" of 3:");
        }
        else if(special == 10){
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
            gui.useSpecial(special);
        }
        else if(special == 11){
            disableImage(specialsName.indexOf(11));
            confirmSpecialButton.setVisible(true);
            questionLabel.setText("Which student do you want to move?");
        }
    }

    /**
     * Make students of specials visible.
     * @param specialIndex is the index of the special in the ArrayList.
     */
    private void showStudents(int specialIndex){
        if(specialIndex==0){
            paneSpecial1.setVisible(true);
        }
        else if(specialIndex==1){
            paneSpecial2.setVisible(true);
        }
        else if(specialIndex==2){
            paneSpecial3.setVisible(true);
        }
    }

    /**
     * Make No Entry tiles visible.
     * @param specialIndex is the index of the special in the ArrayList.
     */
    private void showNoEntry(int specialIndex){
        if(specialIndex==0){
            noEntry1.setVisible(true);
            noEntryView1.setVisible(true);
        }
        else if(specialIndex==1){
            noEntry2.setVisible(true);
            noEntryView2.setVisible(true);
        }
        else if(specialIndex==2){
            noEntry3.setVisible(true);
            noEntryView3.setVisible(true);
        }
    }

    /**
     * Disable images of specials not chosen.
     * @param special is the index of the special in the ArrayList.
     */
    private void disableImage(int special){
        if(special!=0){
            confirmButton.setVisible(false);
            special1Cost.setVisible(false);
            special1View.setVisible(false);
            special1Button.setVisible(false);
            coinSpecial1.setVisible(false);
            paneSpecial1.setVisible(false);
            noEntryView1.setVisible(false);
            noEntry1.setVisible(false);
        }
        if(special!=1){
            confirmButton.setVisible(false);
            special2Cost.setVisible(false);
            special2View.setVisible(false);
            special2Button.setVisible(false);
            coinSpecial2.setVisible(false);
            paneSpecial2.setVisible(false);
            noEntryView2.setVisible(false);
            noEntry2.setVisible(false);
        }
        if(special!=2){
            confirmButton.setVisible(false);
            special3Cost.setVisible(false);
            special3View.setVisible(false);
            special3Button.setVisible(false);
            coinSpecial3.setVisible(false);
            paneSpecial3.setVisible(false);
            noEntryView3.setVisible(false);
            noEntry3.setVisible(false);
        }
    }

    /**
     * Make error message visible.
     */
    private void showErrorMessage(){
        errorMessage.setVisible(true);
    }


    @Override
    public void setGUI(GUI gui) {
        this.gui=gui;
    }

    @Override
    public void setProxy(Exit proxy) {
        this.proxy=proxy;
    }
}
