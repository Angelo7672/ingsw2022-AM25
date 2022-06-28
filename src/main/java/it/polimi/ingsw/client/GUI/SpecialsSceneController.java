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

public class SpecialsSceneController implements SceneController{

    private final String special1 = "graphics/specials/CarteTOT_front1.jpg";
    private final String special2 = "graphics/specials/CarteTOT_front2.jpg";
    private final String special3 = "graphics/specials/CarteTot_front3.jpg";
    private final String special4 = "graphics/specials/CarteTot_front4.jpg";
    private final String special5 = "graphics/specials/CarteTot_front5.jpg";
    private final String special6 = "graphics/specials/CarteTot_front6.jpg";
    private final String special7 = "graphics/specials/CarteTot_front7.jpg";
    private final String special8 = "graphics/specials/CarteTot_front8.jpg";
    private final String special9 = "graphics/specials/CarteTot_front9.jpg";
    private final String special10 = "graphics/specials/CarteTot_front10.jpg";
    private final String special11 = "graphics/specials/CarteTot_front11.jpg";
    private final String special12 = "graphics/specials/CarteTot_front12.jpg";
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


    public SpecialsSceneController(){
        specialChosen=-1;
        studentChosen=-1;
    }

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
        }
        studentsChosen = new ArrayList<>();
        coinSpecial1.setVisible(false);
        coinSpecial2.setVisible(false);
        coinSpecial3.setVisible(false);
        special1Cost.setVisible(false);
        special2Cost.setVisible(false);
        special3Cost.setVisible(false);
    }

    public void resetScene(){
        confirmButton.setVisible(true);
        special1Cost.setVisible(true);
        special1View.setVisible(true);
        special1Button.setVisible(true);
        coinSpecial1.setVisible(true);
        paneSpecial1.setVisible(true);
        confirmButton.setVisible(true);
        special2Cost.setVisible(true);
        special2View.setVisible(true);
        special2Button.setVisible(true);
        coinSpecial2.setVisible(true);
        paneSpecial2.setVisible(true);
        confirmButton.setVisible(true);
        special3Cost.setVisible(true);
        special3View.setVisible(true);
        special3Button.setVisible(true);
        coinSpecial3.setVisible(true);
        paneSpecial3.setVisible(true);
        addButton.setVisible(false);
        confirmSpecialButton.setVisible(false);
        specialChosen = -1;
        studentChosen = -1;
        for (int i = 0; i < 3; i++) {
            if(specialsName.get(i)==1 || specialsName.get(i)==7 || specialsName.get(i)==11) showStudents(i);
        }
    }

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

    protected void setCoins(int specialIndex, int newValue){
        if(specialIndex==0) {
            special1Cost.setText(Integer.toString(newValue-specialsCost.get(specialIndex)));
            if(!special1Cost.toString().equals("0")) {
                special1Cost.setVisible(true);
                coinSpecial1.setVisible(true);
            }
        }
        else if(specialIndex==1) {
            special2Cost.setText(Integer.toString(newValue-specialsCost.get(specialIndex)));
            if(!special2Cost.toString().equals("0")) {
                special2Cost.setVisible(true);
                coinSpecial2.setVisible(true);
            }
        }
        else if(specialIndex==2) {
            special3Cost.setText(Integer.toString(newValue-specialsCost.get(specialIndex)));
            if(!special3Cost.toString().equals("0")) {
                special3Cost.setVisible(true);
                coinSpecial3.setVisible(true);
            }
        }
    }

    protected void setStudent(int specialIndex, int color, int value){
        System.out.println(specialIndex+" "+color+" "+value);
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

    public void setNoEntry(int special, int value){

    }

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

    @FXML
    public void confirmPressed(ActionEvent event) {
        if(specialChosen != -1 && gui.constants.isCardPlayed()){
            boolean result = false;
            try {
                System.out.println("chiamo proxy");
                result = proxy.checkSpecial(specialChosen);
                System.out.println(result);
            }catch (IOException | ClassNotFoundException e ){}
            if(result) {
                if(specialChosen!=2 && specialChosen!=4 && specialChosen!=6 && specialChosen!=8) {
                    useSpecial(specialChosen);
                }
                else {
                    gui.setConstants("SpecialUsed");
                    resetScene();
                    Stage stage = (Stage) confirmButton.getScene().getWindow();
                    stage.close();
                }
            }
            else showErrorMessage();
        }
    }

    @FXML
    public void confirmSpecialButtonPressed(ActionEvent event) {
        if(specialChosen==7 && !studentsChosen.isEmpty()){
            System.out.println(studentsChosen);
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
            gui.useSpecial(specialChosen, studentsChosen);
        }
        else if(specialChosen==11 && studentChosen != -1){
            boolean result=false;
            try {
                result = proxy.useSpecial(11, studentChosen);
            }catch (IOException e){}
            if(result){
                gui.setConstants("SpecialUsed");
                Stage stage = (Stage) confirmButton.getScene().getWindow();
                stage.close();
            }
            else showErrorMessage();
        }
        else if(specialChosen == 1 && studentChosen != -1){
            gui.useSpecial(specialChosen, studentChosen);
        }
        else showErrorMessage();
    }

    @FXML
    public void addButtonPressed(ActionEvent event){
        if(studentChosen!=-1 && studentsChosen.size()<3)
            studentsChosen.add(studentChosen);
        System.out.println("add "+studentChosen);
    }

    private void useSpecial(int special){
        if(special == 1) {
            //sposta studenti da carta a isola
            disableImage(specialsName.indexOf(1));
            confirmSpecialButton.setVisible(true);
            questionLabel.setText("Which student do you want to move?");
        }
        else if(special == 3 || special == 4 || special == 5 || special == 9 || special == 12){
            gui.useSpecial(special);
        }
        else if(special == 7){
            //scegli studenti da carta a entrata
            disableImage(specialsName.indexOf(7));
            confirmSpecialButton.setVisible(true);
            addButton.setVisible(true);
            questionLabel.setText("Which student do you want to move?");
        }
        else if(special == 10){
            //sposta studenti da entrata a tavolo
            useSpecial(special);
        }
        else if(special == 11){
            //da carta a sala
            disableImage(specialsName.indexOf(11));
            confirmSpecialButton.setVisible(true);
            questionLabel.setText("Which student do you want to move?");
        }
    }

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
    private void disableImage(int special){
        if(special!=0){
            confirmButton.setVisible(false);
            special1Cost.setVisible(false);
            special1View.setVisible(false);
            special1Button.setVisible(false);
            coinSpecial1.setVisible(false);
            paneSpecial1.setVisible(false);
        }
        if(special!=1){
            confirmButton.setVisible(false);
            special2Cost.setVisible(false);
            special2View.setVisible(false);
            special2Button.setVisible(false);
            coinSpecial2.setVisible(false);
            paneSpecial2.setVisible(false);
        }
        if(special!=2){
            confirmButton.setVisible(false);
            special3Cost.setVisible(false);
            special3View.setVisible(false);
            special3Button.setVisible(false);
            coinSpecial3.setVisible(false);
            paneSpecial3.setVisible(false);
        }
    }

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
