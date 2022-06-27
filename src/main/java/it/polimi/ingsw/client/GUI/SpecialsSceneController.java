package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private HashMap<Integer, Integer> specialsName;
    private HashMap<Integer, Integer> specialsCost;
    private int specialChosen;

    @FXML private Button confirmButton;
    @FXML private ImageView blueImage1;
    @FXML private ImageView blueImage2;
    @FXML private ImageView blueImage3;
    @FXML private Label blueLabel1;
    @FXML private Label blueLabel2;
    @FXML private Label blueLabel3;
    @FXML private ImageView coinSpecial1;
    @FXML private Label errorMessage;
    @FXML private ImageView greenImage1;
    @FXML private ImageView greenImage2;
    @FXML private ImageView greenImage3;
    @FXML private Label greenLabel1;
    @FXML private Label greenLabel2;
    @FXML private Label greenLabel3;
    @FXML private Button noButton;
    @FXML private ImageView pinkImage1;
    @FXML private ImageView pinkImage2;
    @FXML private ImageView pinkImage3;
    @FXML private Label pinkLabel1;
    @FXML private Label pinkLabel2;
    @FXML private Label pinkLabel3;
    @FXML private ImageView redImage1;
    @FXML private ImageView redImage2;
    @FXML private ImageView redImage3;
    @FXML private Label redLabel1;
    @FXML private Label redLabel2;
    @FXML private Label redLabel3;
    @FXML private Button special1Button;
    @FXML private Label special1Cost;
    @FXML private ImageView special1View;
    @FXML private Button special2Button;
    @FXML private Label special2Cost;
    @FXML private ImageView special2View;
    @FXML private Button special3Button;
    @FXML private Label special3Cost;
    @FXML private ImageView special3View;
    @FXML private ImageView yellowImage1;
    @FXML private ImageView yellowImage2;
    @FXML private ImageView yellowImage3;
    @FXML private Label yellowLabel1;
    @FXML private Label yellowLabel2;
    @FXML private Label yellowLabel3;


    public SpecialsSceneController(){
        specialChosen=-1;
    }

    public void initializedSpecialsScene(ArrayList<Integer> specialsList, ArrayList<Integer> cost){
        this.specials = new HashMap<>();
        this.specialsName = new HashMap<>();
        this.specialsCost = new HashMap<>();
        for (int i = 0; i < specialsList.size(); i++) {
            specials.put(i, specialFactory(specialsList.get(i)));
            specialsName.put(i, specialsList.get(i));
            specialsCost.put(i, cost.get(i));
            if(specialsList.get(i)==1 || specialsList.get(i)==7 || specialsList.get(i)==11) showStudents(i);
        }
        special1View.setImage(specials.get(0));
        special2View.setImage(specials.get(1));
        special3View.setImage(specials.get(2));
        System.out.println(specials);
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

    private void showStudents(int specialIndex){
        if(specialIndex==0){
            greenImage1.setVisible(true);
            greenLabel1.setVisible(true);
            redImage1.setVisible(true);
            redLabel1.setVisible(true);
            pinkImage1.setVisible(true);
            pinkLabel1.setVisible(true);
            yellowImage1.setVisible(true);
            yellowLabel1.setVisible(true);
            blueImage1.setVisible(true);
            blueLabel1.setVisible(true);

        }
        else if(specialIndex==1){
            greenImage2.setVisible(true);
            greenLabel2.setVisible(true);
            redImage2.setVisible(true);
            redLabel2.setVisible(true);
            pinkImage2.setVisible(true);
            pinkLabel2.setVisible(true);
            yellowImage2.setVisible(true);
            yellowLabel2.setVisible(true);
            blueImage2.setVisible(true);
            blueLabel2.setVisible(true);
        }
        else if(specialIndex==2){
            greenImage3.setVisible(true);
            greenLabel3.setVisible(true);
            redImage3.setVisible(true);
            redLabel3.setVisible(true);
            pinkImage3.setVisible(true);
            pinkLabel3.setVisible(true);
            yellowImage3.setVisible(true);
            yellowLabel3.setVisible(true);
            blueImage3.setVisible(true);
            blueLabel3.setVisible(true);
        }
    }

    protected void setCoins(int specialIndex, int newValue){
        if(specialIndex==0) {
            special1Cost.setText(Integer.toString(newValue-specialsCost.get(specialIndex)));
            if(special1Cost.toString().equals("0")) special1Cost.setVisible(true);
        }
        else if(specialIndex==1) {
            special2Cost.setText(Integer.toString(newValue-specialsCost.get(specialIndex)));
            if(special2Cost.toString().equals("0")) special2Cost.setVisible(true);
        }
        else if(specialIndex==2) {
            special3Cost.setText(Integer.toString(newValue-specialsCost.get(specialIndex)));
            if(special3Cost.toString().equals("0")) special3Cost.setVisible(true);
        }
    }

    protected void setStudent(int specialIndex, int color, int value){
        if(specialIndex==0) {
            if(color==0) greenLabel1.setText(Integer.toString(value));
            else if(color==1) redLabel1.setText(Integer.toString(value));
            else if(color==2) yellowLabel1.setText(Integer.toString(value));
            else if(color==3) pinkLabel1.setText(Integer.toString(value));
            else if(color==4) blueLabel1.setText(Integer.toString(value));

        }
        else if(specialIndex==1) {
            if(color==0) greenLabel2.setText(Integer.toString(value));
            else if(color==1) redLabel2.setText(Integer.toString(value));
            else if(color==2) yellowLabel2.setText(Integer.toString(value));
            else if(color==3) pinkLabel2.setText(Integer.toString(value));
            else if(color==4) blueLabel2.setText(Integer.toString(value));
        }
        else if(specialIndex==2) {
            if(color==0) greenLabel3.setText(Integer.toString(value));
            else if(color==1) redLabel3.setText(Integer.toString(value));
            else if(color==2) yellowLabel3.setText(Integer.toString(value));
            else if(color==3) pinkLabel3.setText(Integer.toString(value));
            else if(color==4) blueLabel3.setText(Integer.toString(value));
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
    public void confirmPressed(ActionEvent event) {
        if(specialChosen != -1){
            boolean result = false;
            try {
                result = proxy.checkSpecial(specialChosen);
            }catch (IOException | ClassNotFoundException e ){}
            if(result) {
                if(specialChosen!=2 && specialChosen!=4 && specialChosen!=6 && specialChosen!=8) {
                    useSpecial(specialChosen);
                    Stage stage = (Stage) confirmButton.getScene().getWindow();
                    stage.close();
                }
                else {
                    gui.setConstants("SpecialUsed");
                    Stage stage = (Stage) confirmButton.getScene().getWindow();
                    stage.close();
                }
            }
            else showErrorMessage();
        }
    }

    private void useSpecial(int special){
        if(special == 1) {
            //sposta studenti da carta a isola
        }
        else if(special == 3 || special == 5 || special == 9 || special == 12){
            gui.useSpecial(special);
        }
        else if(special == 7){
            //scegli studenti da carta a entrata
        }
        else if(special == 10){
            //sposta studenti da entrata a tavolo
        }
        else if(special == 11){
            //da carta a sala
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
