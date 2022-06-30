package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.View;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import static it.polimi.ingsw.client.GUI.GUI.SPECIALS;


public class MainSceneController implements SceneController {

    private GUI gui;
    private View view;
    private Exit proxy;
    private int numberOfPlayers;
    private boolean expertMode;
    private String yourNickname;
    private int currentPlayer;
    private int actionAllowed;

    private int cardStudent; //special 1 - studente che prendo dalla carta, lo setta la gui
    private int colorToSwap; //special 10, colore studente da scambiare
    private int studentsfromEntrance; //special 10, numero studenti scambiabili tra ingresso e tavolo
    private  int studentFromTable; // special 10, numero studenti scambiabili tra tavolo e ingresso
    private String selectedStudentsEntrance;
    private String selectedStudentsTable;
    private String selectedStudentSpecial7;
    private int studentsToExchange; //special 7,numero studenti scambiabili con la carta
    private ArrayList<Integer> fromEntranceToTable; //special 10, studenti da mettere nella sala
    private ArrayList<Integer> fromTableToEntrance; //special 10, studenti da mettere nell'ingresso
    private ArrayList<Integer> fromEntranceToCard; //special 7, studenti da scambiare con la carta
    private ArrayList<Integer> fromCardToEntrance; //special 7, studenti sulla carta

    private HashMap<Integer, String> nicknamesMap;
    //private HashMap<Integer, String> charactersMap;
    private HashMap<Integer, ImageView> charactersImageMap;
    //private HashMap<Integer, String> playedCards;

    private ArrayList<AnchorPane> islandsList;
    private HashMap<Integer, AnchorPane> schoolMap;
    private HashMap<Integer, AnchorPane> towersMap;
    private HashMap<Integer, AnchorPane> entrancesMap;
    private HashMap<Integer, AnchorPane> tablesMap;
    private HashMap<Integer, AnchorPane> professorsMap;
    private HashMap<Integer, Integer> towersNumber;
    private HashMap<Integer, Integer> noEntryTilesMap;

    private String lastThingClicked;
    private int currentStudentColor;
    private String selectedStudents;
    private int oldStudentsValue;
    private ArrayList<Table> greenTables;
    private ArrayList<Table> redTables;
    private ArrayList<Table> yellowTables;
    private ArrayList<Table> pinkTables;
    private ArrayList<Table> blueTables;

    private final String GREENSTUDENT = "/graphics/wooden_pieces/greenStudent3D.png";
    private final String REDSTUDENT = "/graphics/wooden_pieces/redStudent3D.png";
    private final String YELLOWSTUDENT = "/graphics/wooden_pieces/yellowStudent3D.png";
    private final String PINKSTUDENT = "/graphics/wooden_pieces/pinkStudent3D.png";
    private final String BLUESTUDENT = "/graphics/wooden_pieces/blueStudent3D.png";
    private final String BLACKTOWER = "/graphics/wooden_pieces/black_tower.png";
    private final String WHITETOWER = "/graphics/wooden_pieces/white_tower.png";
    private final String GREYTOWER = "/graphics/wooden_pieces/grey_tower.png";
    private final String NOENTRY = "/graphics/deny_island_icon.png";

    @FXML private Button useSpecialButton;
    @FXML private AnchorPane islandsPane;
    @FXML private AnchorPane school1;
    @FXML private AnchorPane school2;
    @FXML private AnchorPane school3;
    @FXML private AnchorPane school4;
    @FXML private AnchorPane userInfo1;
    @FXML private AnchorPane userInfo2;
    @FXML private AnchorPane userInfo3;
    @FXML private AnchorPane userInfo4;
    @FXML private ImageView character1;
    @FXML private ImageView character2;
    @FXML private ImageView character3;
    @FXML private ImageView character4;
    @FXML private HBox player1Box;
    @FXML private HBox player2Box;
    @FXML private VBox player3Box;
    @FXML private VBox player4Box;
    @FXML private AnchorPane cardPane1;
    @FXML private AnchorPane cardPane2;
    @FXML private AnchorPane cardPane3;
    @FXML private AnchorPane cardPane4;
    @FXML private Label actionLabel;
    @FXML private Label turnLabel;
    @FXML private Label errorLabel;
    @FXML private Button showCardsButton;
    @FXML private Button showCloudsButton;
    @FXML private Button confirmSpecialButton;
    @FXML private Label specialLabel;

    public MainSceneController() {
        this.nicknamesMap = new HashMap<>();
        //this.charactersMap = new HashMap<>();
        this.charactersImageMap = new HashMap<>();
        this.numberOfPlayers = 4;
        this.expertMode = false;
        this.islandsList = new ArrayList<>();
        this.towersMap = new HashMap<>();
        this.entrancesMap = new HashMap<>();
        this.tablesMap = new HashMap<>();
        this.professorsMap = new HashMap<>();
        this.schoolMap = new HashMap<>();
        //this.playedCards = new HashMap<>();
        this.towersNumber = new HashMap<>();
        this.greenTables = new ArrayList<>();
        this.redTables = new ArrayList<>();
        this.yellowTables = new ArrayList<>();
        this.pinkTables = new ArrayList<>();
        this.blueTables = new ArrayList<>();
        this.fromEntranceToTable = new ArrayList<>();
        this.fromTableToEntrance = new ArrayList<>();
        this.fromEntranceToCard = new ArrayList<>();
        this.fromCardToEntrance = new ArrayList<>();
        this.noEntryTilesMap = new HashMap<>();

        selectedStudentsEntrance = "";
        selectedStudentsTable = "";
        selectedStudentSpecial7 = "";

        oldStudentsValue = 0;
        actionAllowed = -1;
        currentStudentColor = -1;
        lastThingClicked = "";

    }

    public void useSpecialButtonPressed() {
        gui.loadScene(SPECIALS);
    }

    private class StudentsClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (actionAllowed == 0) {
                if (mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(0) ||
                        mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(1) ||
                        mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(2) ||
                        mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(3) ||
                        mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(4)) {

                    lastThingClicked = "student";
                    for (int i = 0; i < 5; i++) {
                        if (mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(i))
                            currentStudentColor = i;
                    }
                } else {
                    lastThingClicked = "";
                    errorLabel.setText("Error, move not allowed!");
                    errorLabel.setVisible(true);
                }
            } else if(actionAllowed == 6) { //special 7
                String color = "";
                if (mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(0) ||
                    mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(1) ||
                    mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(2) ||
                    mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(3) ||
                    mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(4)) {
                        if(studentsToExchange>0){
                            lastThingClicked = "studentToExchange";
                            for (int i = 0; i < 5; i++) {
                                if (mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(i))
                                    currentStudentColor = i;
                            }
                            if(currentStudentColor == 0)
                                color = "green";
                            else if(currentStudentColor == 1)
                                color = "red";
                            else if (currentStudentColor == 2)
                                color = "yellow";
                            else if (currentStudentColor == 3)
                                color = "pink";
                            else if (currentStudentColor == 4)
                                color = "blue";
                            selectedStudentSpecial7 = selectedStudentSpecial7+" "+color;
                            fromEntranceToCard.add(currentStudentColor);
                            actionLabel.setText("Selected students: "+selectedStudentSpecial7);
                        }
                }

            } else if (actionAllowed == 7) { //special 10 che scambia ingresso e tavolo
                if (mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(0) ||
                        mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(1) ||
                        mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(2) ||
                        mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(3) ||
                        mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(4)) {
                    //if(lastThingClicked.equals("studentEntrance") ||lastThingClicked.equals("studentTable")){
                        String color = null;
                        if(studentsfromEntrance > 0){
                            for (int i = 0; i < 5; i++) {
                                if (mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(i))
                                    colorToSwap = i;
                            }
                            fromEntranceToTable.add(colorToSwap);
                            studentsfromEntrance--;
                            if(colorToSwap == 0)
                                color = "Green";
                            else if(colorToSwap == 1 )
                                color = "Red";
                            else if(colorToSwap == 2)
                                color = "Yellow";
                            else if(colorToSwap == 3)
                                color = "Pink";
                            else if(colorToSwap == 4)
                                color = "Blue";
                            selectedStudentsEntrance = selectedStudentsEntrance+" "+color;
                            selectedStudents = "Selected students entrance: "+selectedStudentsEntrance+", selected students table: "+selectedStudentsTable;
                            actionLabel.setText(selectedStudents);
                        }
                }

            } else {
                errorLabel.setText("Error, move not allowed!");
                errorLabel.setVisible(true);
            }
        }
    }

    private class TableClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (actionAllowed == 0) { //move student
                if (lastThingClicked.equals("student"))
                    lastThingClicked = "table";
                if ((mouseEvent.getSource() == tablesMap.get(currentPlayer))) {
                    try {
                        String result = proxy.moveStudent(currentStudentColor, "school", -1);
                        if (result.equalsIgnoreCase("ok")) {
                            errorLabel.setVisible(false);
                        } else if (result.equalsIgnoreCase("transfer complete")) {
                            gui.setConstants("StudentsMoved");
                            setActionAllowed(1);
                        } else {
                            showMoveNotAllowed();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else if (actionAllowed == 7){
                errorLabel.setText("");
            } else
                showMoveNotAllowed();
        }
    }

    private class StudentTableClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            System.out.println("studentTable clicked");
            if (actionAllowed == 7) { //special 10
                if (studentFromTable > 0) {
                    lastThingClicked = "studentTable";
                    ImageView student = (ImageView) mouseEvent.getSource();
                    String color = null;
                    String selectedStudents;
                    if (student.getImage().getUrl().contains(GREENSTUDENT)){
                            fromTableToEntrance.add(0);
                            color = "Green";
                        } else if (student.getImage().getUrl().contains(REDSTUDENT)) {
                            fromTableToEntrance.add(1);
                            color = "Red";
                        } else if (student.getImage().getUrl().contains(YELLOWSTUDENT)) {
                            fromTableToEntrance.add(2);
                            color = "Yellow";
                        } else if (student.getImage().getUrl().contains(PINKSTUDENT)) {
                            fromTableToEntrance.add(3);
                            color = "Pink";
                        } else if (student.getImage().getUrl().contains(BLUESTUDENT)) {
                            fromTableToEntrance.add(4);
                            color = "Blue";
                        }
                        selectedStudentsTable = selectedStudentsTable+" "+color;
                        selectedStudents = "Selected students entrance: "+selectedStudentsEntrance+", selected students table: "+selectedStudentsTable;
                        actionLabel.setText(selectedStudents);
                }
            }
        }
    }

        private class IslandClickHandler implements EventHandler<MouseEvent> {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int islandRef = 0;
                if (actionAllowed == 0) {
                    if (lastThingClicked.equals("student")) {
                        lastThingClicked = "island";
                        for (int i = 0; i < islandsList.size(); i++) {
                            if ((mouseEvent.getSource() == islandsList.get(i))) {
                                islandRef = i;
                            }
                        }
                        try {
                            String result = proxy.moveStudent(currentStudentColor, "island", islandRef);
                            if (result.equalsIgnoreCase("ok")) {
                                errorLabel.setVisible(false);
                            } else if (result.equalsIgnoreCase("transfer complete")) {
                                gui.setConstants("StudentsMoved");
                                setActionAllowed(1);
                            } else {
                                errorLabel.setText("Error, move not allowed!");
                                errorLabel.setVisible(true);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        errorLabel.setText("Error, move not allowed!");
                        errorLabel.setVisible(true);
                    }

                } else if (actionAllowed == 1) {
                    int motherMovement = 0;
                    int currentMotherPos = view.getMotherPosition();
                    for (int i = 0; i < islandsList.size(); i++) {
                        if ((mouseEvent.getSource() == islandsList.get(i))) {
                            islandRef = i; // non sono sicura
                        }
                        if (currentMotherPos < islandRef) {
                            motherMovement = islandRef - currentMotherPos;
                        } else {
                            motherMovement = islandsList.size() - currentMotherPos + islandRef;
                        }
                    }
                    try {
                        String result = proxy.moveMotherNature(motherMovement);
                        if (result.equalsIgnoreCase("ok")) {
                            gui.setConstants("MovedMother");
                            errorLabel.setVisible(false);
                            actionLabel.setText("");
                            setActionAllowed(2);
                        } else {
                            errorLabel.setText("Error, move not allowed!");
                            errorLabel.setVisible(true);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (actionAllowed == 3) { //special 1
                    for (int i = 0; i < islandsList.size(); i++) {
                        if ((mouseEvent.getSource() == islandsList.get(i))) {
                            islandRef = i;
                        }
                    }
                    try {
                        if (proxy.useSpecial(1, cardStudent, islandRef)) {
                            gui.setConstants("SpecialUsed");
                            specialLabel.setVisible(false);
                        } else {
                            showMoveNotAllowed();
                            gui.specialNotAllowed();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (actionAllowed == 4) { //Special 3
                    for (int i = 0; i < islandsList.size(); i++) {
                        if ((mouseEvent.getSource() == islandsList.get(i))) {
                            islandRef = i;
                        }
                    }
                    try {
                        if (proxy.useSpecial(3, islandRef)) {
                            gui.setConstants("SpecialUsed");
                            specialLabel.setVisible(false);
                        } else {
                            errorLabel.setText("Error, move not allowed!");
                            errorLabel.setVisible(true);
                            gui.specialNotAllowed();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (actionAllowed == 5) { //special 5
                    for (int i = 0; i < islandsList.size(); i++) {
                        if ((mouseEvent.getSource() == islandsList.get(i))) {
                            islandRef = i;
                        }
                    }
                    try {
                        if (proxy.useSpecial(5, islandRef)) {
                            gui.setConstants("SpecialUsed");
                        } else {
                            showMoveNotAllowed();
                            gui.specialNotAllowed();
                        }
                    } catch (IOException e) {
                    }
                } else {
                    showMoveNotAllowed();
                }
            }
        }

        public void confirmSpecial() { //special 7
            if (actionAllowed == 6) {
                if (lastThingClicked.equalsIgnoreCase("studentToExchange")) {
                    if(fromEntranceToCard.size()!=0 && fromCardToEntrance.size()!=0){
                        try {
                            if (proxy.useSpecial(7, fromEntranceToCard, fromCardToEntrance)) {
                                gui.setConstants("SpecialUsed");
                                specialLabel.setVisible(false);
                            } else {
                                showMoveNotAllowed();
                                gui.specialNotAllowed();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        fromCardToEntrance.clear();
                        fromEntranceToCard.clear();
                        selectedStudents ="";
                        selectedStudentSpecial7="";
                    }
                    else
                        showMoveNotAllowed();
                }
            } else if (actionAllowed == 7) { //special 10
                if(fromEntranceToTable.size()!=0 && fromTableToEntrance.size()!=0){
                    try {
                        if (proxy.useSpecial(10, fromEntranceToTable, fromTableToEntrance)) {
                            gui.setConstants("SpecialUsed");
                            specialLabel.setVisible(false);
                        } else {
                            showMoveNotAllowed();
                            gui.specialNotAllowed();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    fromEntranceToTable.clear();
                    fromTableToEntrance.clear();
                    selectedStudents = "";
                    selectedStudentsTable = "";
                    selectedStudentsEntrance = "";
                } else{
                    showMoveNotAllowed();
                }
            }
        }

        public void showMoveNotAllowed() {
            errorLabel.setVisible(true);
        }


        public void initializeScene() {
            if (numberOfPlayers == 2) {
                player3Box.setVisible(false);
                cardPane3.setVisible(false);
                player4Box.setVisible(false);
                cardPane4.setVisible(false);

            } else if (numberOfPlayers == 3) {
                player4Box.setVisible(false);
                cardPane4.setVisible(false);
            }
            useSpecialButton.setVisible(expertMode);

            userInfo1.getChildren().get(2).setVisible(expertMode);
            userInfo1.getChildren().get(3).setVisible(expertMode);
            userInfo2.getChildren().get(2).setVisible(expertMode);
            userInfo2.getChildren().get(3).setVisible(expertMode);
            userInfo3.getChildren().get(2).setVisible(expertMode);
            userInfo3.getChildren().get(3).setVisible(expertMode);
            userInfo4.getChildren().get(2).setVisible(expertMode);
            userInfo4.getChildren().get(3).setVisible(expertMode);

            charactersImageMap.put(0, character1);
            charactersImageMap.put(1, character2);
            charactersImageMap.put(2, character3);
            charactersImageMap.put(3, character4);

            schoolMap.put(0, school1);
            schoolMap.put(1, school2);
            schoolMap.put(2, school3);
            schoolMap.put(3, school4);


            for (int i = 0; i < numberOfPlayers; i++) {
                //maps the children of school panes, 0:school imageView, 1: entrance, 2: table 3: professors, 4: towers
                this.entrancesMap.put(i, (AnchorPane) schoolMap.get(i).getChildren().get(1)); //maps all the entrances
                this.tablesMap.put(i, (AnchorPane) schoolMap.get(i).getChildren().get(2)); //maps all the tables
                this.professorsMap.put(i, (AnchorPane) schoolMap.get(i).getChildren().get(3)); //maps all the professors panes
                this.towersMap.put(i, (AnchorPane) schoolMap.get(i).getChildren().get(4));//maps all the towers panes
            }
            AnchorPane island;
            for (int i = 0; i < 12; i++) {
                island = (AnchorPane) islandsPane.getChildren().get(i);
                islandsList.add(island);
            }

            schoolsInitialization();
            islandsInitialization();
            tablesInitialization();

            gui.isMainSceneInitialized = true;
            actionAllowed = -1; //not your turn
        }


        //inizializza le isole, nascondendo le torri (che non ci sono a inizio partita)
        public void islandsInitialization() {
            AnchorPane island;
            IslandClickHandler islandClickHandler = new IslandClickHandler();
            for (int i = 0; i < 12; i++) {
                island = islandsList.get(i);
                island.setOnMouseClicked(islandClickHandler);
                noEntryTilesMap.put(i, 0);
            }
        }

        //inizializza le scuole, mettendo le torri del colore giusto a seconda del numero di giocatori
        public void schoolsInitialization() {
            ImageView tower;
            if (numberOfPlayers == 2) {
                towersNumber.put(0, 8);
                towersNumber.put(1, 8);
                for (int i = 0; i < towersMap.get(0).getChildren().size(); i++) {
                    tower = (ImageView) towersMap.get(0).getChildren().get(i);
                    tower.setImage(new Image(WHITETOWER));
                }
                for (int i = 0; i < towersMap.get(1).getChildren().size(); i++) {
                    tower = (ImageView) towersMap.get(1).getChildren().get(i);
                    tower.setImage(new Image(BLACKTOWER));
                }

            }
            if (numberOfPlayers == 3) {
                towersNumber.put(0, 6);
                towersNumber.put(1, 6);
                towersNumber.put(2, 6);
                for (int i = 0; i < towersMap.get(0).getChildren().size(); i++) {
                    tower = (ImageView) towersMap.get(0).getChildren().get(i);
                    tower.setImage(new Image(WHITETOWER));
                }
                for (int i = 0; i < towersMap.get(1).getChildren().size(); i++) {
                    tower = (ImageView) towersMap.get(1).getChildren().get(i);
                    tower.setImage(new Image(BLACKTOWER));
                }
                for (int i = 0; i < towersMap.get(2).getChildren().size(); i++) {
                    tower = (ImageView) towersMap.get(2).getChildren().get(i);
                    tower.setImage(new Image(GREYTOWER));
                }
                for (int i = 0; i < 3; i++) {
                    towersMap.get(i).getChildren().get(6).setVisible(false);
                    towersMap.get(i).getChildren().get(7).setVisible(false);
                }
            }
            if (numberOfPlayers == 4) {
                towersNumber.put(0, 8);
                towersNumber.put(1, 0);
                towersNumber.put(2, 8);
                towersNumber.put(3, 0);
                for (int i = 0; i < towersMap.get(0).getChildren().size(); i++) {
                    tower = (ImageView) towersMap.get(0).getChildren().get(i);
                    tower.setImage(new Image(WHITETOWER));
                }
                for (int i = 0; i < towersMap.get(1).getChildren().size(); i++) {
                    tower = (ImageView) towersMap.get(1).getChildren().get(i);
                    tower.setVisible(false);
                }
                for (int i = 0; i < towersMap.get(2).getChildren().size(); i++) {
                    tower = (ImageView) towersMap.get(2).getChildren().get(i);
                    tower.setImage(new Image(BLACKTOWER));
                }
                for (int i = 0; i < towersMap.get(3).getChildren().size(); i++) {
                    tower = (ImageView) towersMap.get(3).getChildren().get(i);
                    tower.setVisible(false);
                }
            }
            StudentsClickHandler studentsClickHandler = new StudentsClickHandler();
            TableClickHandler tableClickHandler = new TableClickHandler();
            for (int i = 0; i < numberOfPlayers; i++) {
                for (int j = 0; j < 5; j++) {
                    entrancesMap.get(i).getChildren().get(j).setOnMouseClicked(studentsClickHandler);
                    tablesMap.get(i).setOnMouseClicked(tableClickHandler);
                }
            }
        }

        public void tablesInitialization() {
            greenTables.add(new Table(10, 4, 0));
            redTables.add(new Table(10, 28, 1));
            yellowTables.add(new Table(10, 52, 2));
            pinkTables.add(new Table(10, 76, 3));
            blueTables.add(new Table(10, 100, 4));

            greenTables.add(new Table(170, 100, 0));
            redTables.add(new Table(170, 76, 1));
            yellowTables.add(new Table(170, 52, 2));
            pinkTables.add(new Table(170, 28, 3));
            blueTables.add(new Table(170, 4, 4));

            greenTables.add(new Table(128, 10, 0));
            redTables.add(new Table(102, 10, 1));
            yellowTables.add(new Table(76, 10, 2));
            pinkTables.add(new Table(50, 10, 3));
            blueTables.add(new Table(24, 10, 4));

            greenTables.add(new Table(24, 176, 0));
            redTables.add(new Table(50, 176, 1));
            yellowTables.add(new Table(76, 176, 2));
            pinkTables.add(new Table(102, 176, 3));
            blueTables.add(new Table(128, 176, 4));
        }

        public void showCloudsPressed() {
            gui.loadScene(GUI.CLOUDS);
        }

        public void showCardsPressed() {
            gui.loadScene(GUI.CARDS);
        }

        public void setNickname(String nickname, int playerRef) {
            Label nicknameLabel;
            if (playerRef == 0) {
                nicknameLabel = (Label) userInfo1.getChildren().get(1);
                nicknameLabel.setText(nickname);
            } else if (playerRef == 1) {
                nicknameLabel = (Label) userInfo2.getChildren().get(1);
                nicknameLabel.setText(nickname);
            } else if (playerRef == 2) {
                nicknameLabel = (Label) userInfo3.getChildren().get(1);
                nicknameLabel.setText(nickname);
            } else if (playerRef == 3) {
                nicknameLabel = (Label) userInfo4.getChildren().get(1);
                nicknameLabel.setText(nickname);
            }
            nicknamesMap.put(playerRef, nickname);
        }

        public void setCharacter(String character, int playerRef) {
            Image characterImage = stringToImage(character);
            switch (playerRef) {
                case 0 -> character1.setImage(characterImage);
                case 1 -> character2.setImage(characterImage);
                case 2 -> character3.setImage(characterImage);
                case 3 -> character4.setImage(characterImage);
            }
        }
        @Override
        public void setGUI(GUI gui) {
            this.gui = gui;
        }

        @Override
        public void setProxy(Exit proxy) {
            this.proxy = proxy;
        }

        public void setView(View view) {
            this.view = view;
        }

        public void setNumberOfPlayers(int numberOfPlayers) {
            this.numberOfPlayers = numberOfPlayers;
        }

        public void setExpertMode(boolean expertMode) {
            this.expertMode = expertMode;
        }

        public void setMotherPosition(int islandRef) {
            ImageView motherNature;
            for (int i = 0; i < islandsList.size(); i++) {
                //children 1 is always MotherNature
                motherNature = (ImageView) islandsList.get(i).getChildren().get(1);
                if (islandRef == i) {
                    motherNature.setVisible(true);
                } else
                    motherNature.setVisible(false);
            }
        }

        public void setStudentsEntrance(int playerRef, int color, int newStudentsValue) {
            AnchorPane entrance = entrancesMap.get(playerRef);
            ImageView studentImage;
            String text = String.valueOf(newStudentsValue);

            studentImage = (ImageView) entrance.getChildren().get(color);
            Label studentLabel = (Label) entrance.getChildren().get(color + 5); //Labels are located 5 position after images

            studentLabel.setText(text);
           //System.out.println("Color"+color);
            //System.out.println("student label text: "+text);
            if (newStudentsValue != 0) {
                studentLabel.setVisible(true);
                studentImage.setVisible(true);
            } else {
                studentLabel.setVisible(false);
                studentImage.setVisible(false);
            }
            /*for(int i=5; i<entrance.getChildren().size(); i++){
                System.out.println("Child "+(i-5)+" is "+entrance.getChildren().get(i)+" player "+playerRef);
            }*/
        }

        public void setStudentsTable(int playerRef, int color, int newStudentsValue) {
            AnchorPane tablePane = tablesMap.get(playerRef);
            StudentTableClickHandler studentTableClickHandler = new StudentTableClickHandler();
            Table table = null;
            ImageView student;
            int oldStudentsValue;

            if (color == 0) {
                table = greenTables.get(playerRef);
            } else if (color == 1) {
                table = redTables.get(playerRef);
            } else if (color == 2) {
                table = yellowTables.get(playerRef);
            } else if (color == 3) {
                table = pinkTables.get(playerRef);
            } else if (color == 4) {
                table = blueTables.get(playerRef);
            }
            oldStudentsValue = table.getStudentsNumber();

            if (newStudentsValue > oldStudentsValue) {
                tablePane.getChildren().add(new ImageView(table.getStudentImage()));
                student = (ImageView) tablePane.getChildren().get(tablePane.getChildren().size() - 1);
                student.setFitHeight(16.0);
                student.setFitWidth(16.0);
                student.setX(table.getX());
                student.setY(table.getY());
                table.setNewX(playerRef,true);
                table.setNewY(playerRef, true );
                table.addStudent();
                student.setVisible(true);
                student.setOnMouseClicked(studentTableClickHandler);

            } else if (newStudentsValue<oldStudentsValue){
                oldStudentsValue = table.getStudentsNumber();
                Boolean ok = false;
                for (int i = tablePane.getChildren().size() - 1; i >= 0 && !ok; i--) {
                    student = (ImageView) tablePane.getChildren().get(i);
                    String imageName = student.getImage().getUrl();
                    if (imageName.contains(GREENSTUDENT) && color == 0) {
                        tablePane.getChildren().remove(i);
                        table.removeStudent();
                        ok = true;
                        table.setNewX(playerRef,false);
                        table.setNewY(playerRef, false );
                    } else if (imageName.contains(REDSTUDENT) && color == 1) {
                        tablePane.getChildren().remove(i);
                        table.removeStudent();
                        ok = true;
                        table.setNewX(playerRef,false);
                        table.setNewY(playerRef, false );
                    } else if (imageName.contains(YELLOWSTUDENT) && color == 2) {
                        tablePane.getChildren().remove(i);
                        table.removeStudent();
                        ok = true;
                        table.setNewX(playerRef,false);
                        table.setNewY(playerRef, false );
                    } else if (imageName.contains(PINKSTUDENT) && color == 3) {
                        tablePane.getChildren().remove(i);
                        table.removeStudent();
                        ok = true;
                        table.setNewX(playerRef,false);
                        table.setNewY(playerRef, false );
                    } else if (imageName.contains(BLUESTUDENT) && color == 4) {
                        tablePane.getChildren().remove(i);
                        table.removeStudent();
                        ok = true;
                        table.setNewX(playerRef,false);
                        table.setNewY(playerRef, false );
                    }
                }
            }
        }


        public void setStudentsIsland(int islandRef, int color, int newStudentsValue) {
            AnchorPane island = islandsList.get(islandRef);
            Label studentLabel;
            ImageView studentImage;
            studentImage = (ImageView) island.getChildren().get(color + 3);
            studentLabel = (Label) island.getChildren().get(color + 8);
            studentLabel.setText(String.valueOf(newStudentsValue));
            if(newStudentsValue!=0){
                studentImage.setVisible(true);
                studentLabel.setVisible(true);
            } else {
                studentImage.setVisible(false);
                studentLabel.setVisible(false);
            }

        }

        public void setProfessor(int schoolRef, int color, boolean newProfessorValue) {
            AnchorPane professorsTable = professorsMap.get(schoolRef);
            professorsTable.getChildren().get(color).setVisible(newProfessorValue);
        }

        public Image stringToImage(String assistant) {
            Image image = null;
            switch (assistant) {
                case "LION" -> image = new Image("/graphics/assistants/lion_1.png");
                case "GOOSE" -> image = new Image("/graphics/assistants/goose_2.png");
                case "CAT" -> image = new Image("/graphics/assistants/cat_3.png");
                case "EAGLE" -> image = new Image("/graphics/assistants/eagle_4.png");
                case "FOX" -> image = new Image("/graphics/assistants/fox_5.png");
                case "LIZARD" -> image = new Image("/graphics/assistants/lizard_6.png");
                case "OCTOPUS" -> image = new Image("/graphics/assistants/octopus_7.png");
                case "DOG" -> image = new Image("/graphics/assistants/dog_8.png");
                case "ELEPHANT" -> image = new Image("/graphics/assistants/elephant_9.png");
                case "TURTLE" -> image = new Image("/graphics/assistants/turtle_10.png");
                case "WIZARD" -> image = new Image("/graphics/character_wizard.png");
                case "WITCH" -> image = new Image("/graphics/character_witch.png");
                case "SAMURAI" -> image = new Image("/graphics/character_samurai.png");
                case "KING" -> image = new Image("/graphics/character_king.png");
            }
            return image;
        }

        public void setLastPlayedCard(int playerRef, String assistantCard) {
            ImageView card = null;
            switch (playerRef) {
                case 0 -> card = (ImageView) cardPane1.getChildren().get(0);
                case 1 -> card = (ImageView) cardPane2.getChildren().get(0);
                case 2 -> card = (ImageView) cardPane3.getChildren().get(0);
                case 3 -> card = (ImageView) cardPane4.getChildren().get(0);
            }
            card.setImage(stringToImage(assistantCard));
            card.setVisible(true);
        }

        public void setTowersSchool(int schoolRef, int newTowersNumber) {
            AnchorPane towerPane = towersMap.get(schoolRef);
            int oldTowersNumber = towersNumber.get(schoolRef);

            if (newTowersNumber < oldTowersNumber) {
                for (int i = oldTowersNumber - 1; i >= newTowersNumber; i--) {
                    towerPane.getChildren().get(i).setVisible(false);
                }
            } else if (newTowersNumber > oldTowersNumber) {
                for (int i = oldTowersNumber; i < newTowersNumber; i++) {
                    towerPane.getChildren().get(i).setVisible(true);
                }
            }
        }

        public void setTowersIsland(int islandRef, int towersNumber) {
            ImageView towerImage = (ImageView) islandsList.get(islandRef).getChildren().get(2);
            Label towerLabel = (Label) islandsList.get(islandRef).getChildren().get(13);
            ImageView islandImage = (ImageView) islandsList.get(islandRef).getChildren().get(0);

        /*double currentH = islandImage.getFitHeight();
        double currentW = islandImage.getFitWidth();*/

            towerLabel.setText(String.valueOf(towersNumber));
            /*if (towersNumber != 0 && towerImage!=null) {
                towerLabel.setVisible(true);
                towerImage.setVisible(true);
            } else {
                towerLabel.setVisible(false);
                towerImage.setVisible(false);
                towerLabel.setVisible(false);
            }*/
        /*islandImage.setFitWidth(currentW+2);
        islandImage.setFitHeight(currentH+2);*/
        }

        public void unifyIsland(int islandToDelete) {
            AnchorPane island = islandsList.get(islandToDelete);
            islandsPane.getChildren().remove(island);
            islandsList.remove(islandToDelete);
            System.out.println(islandsPane.getChildren());
        }

        public void setYourNickname(String yourNickname) {
            this.yourNickname = yourNickname;
        }

        public void setCurrentPlayer() {
            for (int i = 0; i < nicknamesMap.size(); i++) {
                if (yourNickname.equals(nicknamesMap.get(i))) {
                    currentPlayer = i;
                }
            }
            turnLabel.setText("It's your turn!");
            setActionAllowed(0); //student movement allowed
        }

        public void setActionAllowed(int actionAllowed) {
            this.actionAllowed = actionAllowed;
            if (actionAllowed == 0) {
                actionLabel.setText("Move a student in your school or on an island!");
                actionLabel.setVisible(true);
                errorLabel.setVisible(false);
                specialLabel.setVisible(false);
            } else if (actionAllowed == 1) {
                actionLabel.setText("Move Mother Nature on a Island! (Max movement is: " + view.getMaxStepsMotherNature() + ")");
                actionLabel.setVisible(true);
                specialLabel.setVisible(false);
            } else if (actionAllowed == 2) {
                gui.phaseHandler("ChooseCloud");
                specialLabel.setVisible(false);
            } else if (actionAllowed == -1) {
                turnLabel.setText("It's your opponent's turn. Wait...");
                actionLabel.setVisible(false);
            } else if (actionAllowed == 3) { //special 1
                actionLabel.setText("Choose an island!");
                actionLabel.setVisible(true);
            } else if (actionAllowed == 4) { //special 3
                actionLabel.setText("Choose an island!");
                actionLabel.setVisible(true);
            } else if (actionAllowed == 5) { //special 5
                actionLabel.setText("Choose an island!");
                actionLabel.setVisible(true);
            } else if (actionAllowed == 6) { //special 7
                confirmSpecialButton.setVisible(true);
                actionLabel.setText("Select students from the entrance");
                studentsToExchange = fromCardToEntrance.size();
                actionLabel.setVisible(true);
            } else if (actionAllowed == 7) { //special 10
                confirmSpecialButton.setVisible(true);
                studentsfromEntrance = 2; //puoi scambiare max 2 studenti
                studentFromTable = 2;
                actionLabel.setText("Select students from the entrance and table (2 max)");
            } else if (actionAllowed == 8){
                actionLabel.setText("Move Mother Nature on a Island! (Max movement is: " + view.getMaxStepsMotherNature() + ")");
            }
        }

        public void setTowerColor(int islandRef, int newColor) {
            ImageView tower = (ImageView) islandsList.get(islandRef).getChildren().get(2);
            Label towerLabel = (Label) islandsList.get(islandRef).getChildren().get(13);

            if (!tower.isVisible()) {
                setTowersIsland(islandRef, 1);
            }
            if (newColor == 0) {
                tower.setImage(new Image(WHITETOWER));
                towerLabel.setTextFill(Color.BLACK);
                tower.setVisible(true);
                towerLabel.setVisible(true);
            } else if (newColor == 1) {
                tower.setImage(new Image(BLACKTOWER));
                towerLabel.setTextFill(Color.WHITE);
                tower.setVisible(true);
                towerLabel.setVisible(true);
            } else if (newColor == 2) {
                tower.setImage(new Image(GREYTOWER));
                towerLabel.setTextFill(Color.BLACK);
                tower.setVisible(true);
                towerLabel.setVisible(true);
            } else if (newColor == -1) {
                tower.setImage(null);
                tower.setVisible(false);
                towerLabel.setVisible(false);
            }
        }

    public void setCardStudent(int color) {
        this.cardStudent = color;
    }

    public void setFromCardToEntrance(ArrayList<Integer> cardsStudents) {
        this.fromCardToEntrance = cardsStudents;
    }


    public void setNewCoinsValue(int playerRef, int newCoinsValue) {
        Label coinsLabel = null;
        switch (playerRef){
            case 0 -> coinsLabel = (Label) userInfo1.getChildren().get(3);
            case 1 -> coinsLabel = (Label) userInfo2.getChildren().get(3);
            case 2 -> coinsLabel = (Label) userInfo3.getChildren().get(3);
            case 3 -> coinsLabel = (Label) userInfo4.getChildren().get(3);
        }
        coinsLabel.setText("coins: "+newCoinsValue);
    }


    public void setInhibitedIsland(int islandRef, int isInhibited){
        AnchorPane island = islandsList.get(islandRef);
        ImageView noEntryTile;
        int oldValue = noEntryTilesMap.get(islandRef);
        noEntryTilesMap.put(islandRef, isInhibited);

        if(isInhibited>oldValue){
            if(isInhibited == 1){
                noEntryTile = new ImageView(new Image(NOENTRY));
                noEntryTile.setFitHeight(40);
                noEntryTile.setFitWidth(40);
                island.getChildren().add(island.getChildren().size(), noEntryTile);
                island.getChildren().get(island.getChildren().size()-1).setLayoutX(50.0);
                island.getChildren().get(island.getChildren().size()-1).setLayoutY(30.0);
                noEntryTile.setVisible(true);
            } else if(isInhibited == 2){
                noEntryTile = new ImageView(new Image(NOENTRY));
                noEntryTile.setFitHeight(40);
                noEntryTile.setFitWidth(40);
                island.getChildren().add(island.getChildren().size(), noEntryTile);
                island.getChildren().get(island.getChildren().size()-1).setLayoutX(100.0);
                island.getChildren().get(island.getChildren().size()-1).setLayoutY(30.0);
                noEntryTile.setVisible(true);
            } else if(isInhibited == 3){
                noEntryTile = new ImageView(new Image(NOENTRY));
                noEntryTile.setFitHeight(40);
                noEntryTile.setFitWidth(40);
                island.getChildren().add(island.getChildren().size(), noEntryTile);
                island.getChildren().get(island.getChildren().size()-1).setLayoutX(50.0);
                island.getChildren().get(island.getChildren().size()-1).setLayoutY(80.0);
                noEntryTile.setVisible(true);
            } else if(isInhibited == 4) {
                noEntryTile = new ImageView(new Image(NOENTRY));
                noEntryTile.setFitHeight(40);
                noEntryTile.setFitWidth(40);
                island.getChildren().add(island.getChildren().size(), noEntryTile);
                island.getChildren().get(island.getChildren().size() - 1).setLayoutX(100.0);
                island.getChildren().get(island.getChildren().size() - 1).setLayoutY(80.0);
                noEntryTile.setVisible(true);
            }
        } else {
            island.getChildren().remove(island.getChildren().size()-1);
        }

    }

    public void setSpecialPlayed(int specialRef, int playerRef){
        String playerName = nicknamesMap.get(playerRef);
        specialLabel.setText(playerName+" played special card "+specialRef+"!");
        specialLabel.setVisible(true);
    }




}
