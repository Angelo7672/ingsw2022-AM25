package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.View;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.sonatype.guice.bean.binders.SpaceModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class MainSceneController implements SceneController {

    private GUI gui;
    private View view;
    private Exit proxy;
    private int numberOfPlayers;
    private boolean expertMode;
    private String yourNickname;
    private int currentPlayer;
    private int actionAllowed;

    private HashMap<Integer, String> nicknamesMap;
    private HashMap<Integer, String> charactersMap;
    private HashMap<Integer, ImageView> charactersImageMap;
    private HashMap<Integer, String> playedCards;

    private HashMap<Integer, AnchorPane> islandsMap;
    private HashMap<Integer, AnchorPane> schoolMap;
    private HashMap<Integer, AnchorPane> towersMap;
    private HashMap<Integer, AnchorPane> entrancesMap;
    private HashMap<Integer, AnchorPane> tablesMap;
    private HashMap<Integer, AnchorPane> professorsMap;

    private String lastThingClicked;
    private int currentStudentColor;
    private int oldStudentsValue;
    private ArrayList<Table> greenTables;
    private ArrayList<Table> redTables;
    private ArrayList<Table> yellowTables;
    private ArrayList<Table> pinkTables;
    private ArrayList<Table> blueTables;

    private final String WIZARD = "/graphics/character_wizard.png";
    private final String WITCH = "/graphics/character_witch.png";
    private final String SAMURAI = "/graphics/character_samurai.png";
    private final String KING = "/graphics/character_king.png";

    private final String GREENSTUDENT = "/graphics/wooden_pieces/greenStudent3D.png";
    private final String REDSTUDENT = "/graphics/wooden_pieces/redStudent3D.png";
    private final String YELLOWSTUDENT = "/graphics/wooden_pieces/yellowStudent3D.png";
    private final String PINKSTUDENT = "/graphics/wooden_pieces/pinkStudent3D.png";
    private final String BLUESTUDENT = "/graphics/wooden_pieces/blueStudent3D.png";

    private final String BLACKTOWER = "/graphics/wooden_pieces/black_tower.png";
    private final String WHITETOWER = "/graphics/wooden_pieces/white_tower.png";
    private final String GREYTOWER = "/graphics/wooden_pieces/grey_tower.png";

    private final int SPACE = 18;



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
    @FXML private Label card1;
    @FXML private Label card2;
    @FXML private Label card3;
    @FXML private Label card4;

    @FXML private Label actionLabel;
    @FXML private Label turnLabel;
    @FXML private Label errorLabel;

    @FXML private Button showCardsButton;
    @FXML private Button showCloudsButton;




    public MainSceneController() {
        this.nicknamesMap = new HashMap<>();
        this.charactersMap = new HashMap<>();
        this.charactersImageMap = new HashMap<>();
        this.numberOfPlayers = 4;
        this.expertMode = false;
        this.islandsMap = new HashMap<>();
        this.towersMap = new HashMap<>();
        this.entrancesMap = new HashMap<>();
        this.tablesMap = new HashMap<>();
        this.professorsMap = new HashMap<>();
        this.schoolMap = new HashMap<>();
        this.playedCards = new HashMap<>();
        this.greenTables= new ArrayList<>();
        this.redTables= new ArrayList<>();
        this.yellowTables= new ArrayList<>();
        this.pinkTables= new ArrayList<>();
        this.blueTables= new ArrayList<>();

        oldStudentsValue=0;
        actionAllowed=-1;
        currentStudentColor=-1;
        lastThingClicked="";

    }

    private class StudentsClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if(actionAllowed==0) {
                System.out.println("student clicked");

                if(mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(0)||
                    mouseEvent.getSource() ==  entrancesMap.get(currentPlayer).getChildren().get(1)||
                    mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(2)||
                    mouseEvent.getSource() ==  entrancesMap.get(currentPlayer).getChildren().get(3)||
                    mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(4)) {

                    lastThingClicked = "student";
                    for (int i = 0; i < 5; i++) {
                        if (mouseEvent.getSource() == entrancesMap.get(currentPlayer).getChildren().get(i))
                            currentStudentColor = i;
                    }
                } else {
                    lastThingClicked="";
                    errorLabel.setText("Error, move not allowed!");
                    errorLabel.setVisible(true);
                }
            }
            else{
                errorLabel.setText("Error, move not allowed!");
                errorLabel.setVisible(true);
            }
        }
    }

    private class TableClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            AnchorPane table = tablesMap.get(currentPlayer);
            if(actionAllowed==0) {
                if(lastThingClicked.equals("student"))
                lastThingClicked = "table";
                System.out.println("table clicked");
                if((mouseEvent.getSource() == tablesMap.get(currentPlayer))){
                    try {
                        String result = proxy.moveStudent(currentStudentColor, "school", -1);
                        if(result.equalsIgnoreCase("ok")){
                            System.out.println("movement successful");
                            errorLabel.setVisible(false);
                        } else if(result.equalsIgnoreCase("transfer complete")) {
                        System.out.println("movement successful, fine");
                        setActionAllowed(1);
                    } else {
                        System.out.println("mossa non consentita");
                        errorLabel.setText("Error, move not allowed!");
                        errorLabel.setVisible(true);
                    }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
            else{
                errorLabel.setVisible(true);
                errorLabel.setText("Error, move not allowed!");
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
                    System.out.println("island clicked");
                    System.out.println("mouseEvent source: " + mouseEvent.getSource());
                    for (int i = 0; i < 12; i++) {
                        if ((mouseEvent.getSource() == islandsMap.get(i))) {
                            islandRef = i;
                        }
                    }
                    try {
                        String result = proxy.moveStudent(currentStudentColor, "island", islandRef);
                        if (result.equalsIgnoreCase("ok")) {
                            System.out.println("movement successful");
                            errorLabel.setVisible(false);
                        } else if(result.equalsIgnoreCase("transfer complete")) {
                            System.out.println("movement successful, fine");
                            setActionAllowed(1);
                        } else {
                            System.out.println("mossa non consentita");
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
                int motherMov = 0;
                System.out.println("you can move mother nature now");
                for (int i = 0; i < 12; i++) {
                    if ((mouseEvent.getSource() == islandsMap.get(i))) {
                        islandRef = i;
                    }
                    if(view.getMotherPosition()<islandRef){
                        motherMov = islandRef - view.getMotherPosition();
                    } else {
                        motherMov= view.getMotherPosition() - islandRef;
                    }
                }
                try {
                    System.out.println("calling moveMotherNature");
                    String result = proxy.moveMotherNature(motherMov);
                    System.out.println("Result is: "+result);
                    if(result.equalsIgnoreCase("ok")){
                        errorLabel.setVisible(false);
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
                } else {
                    errorLabel.setVisible(true);
                    errorLabel.setText("Error, move not allowed!");
                }
            }
        }


    private class Table{
        int x;
        int y;
        int studentsNumber;
        int color;
        int nodeId;
        ImageView studentImage;

        public Table(int initialX, int initialY, int color){
            this.x=initialX;
            this.y=initialY;
            this.studentsNumber=0;
            this.color=color;
            if(color==0){
                this.studentImage =new ImageView(new Image(GREENSTUDENT));
                this.nodeId=0;
            } else if(color==1){
                this.studentImage =new ImageView(new Image(REDSTUDENT));
                this.nodeId=2;
            } else if(color==2){
                this.studentImage =new ImageView(new Image(YELLOWSTUDENT));
                this.nodeId=3;
            } else if(color==3){
                this.studentImage =new ImageView(new Image(PINKSTUDENT));
                this.nodeId=4;
            } else if(color==4){
                this.studentImage =new ImageView(new Image(BLUESTUDENT));
                this.nodeId=5;
            }
            studentImage.setFitWidth(16.0);
            studentImage.setFitHeight(16.0);

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
           if(schoolRef==3){
               this.y=y+SPACE;
           } else if(schoolRef==4){
               this.y=y-SPACE;
           }
        }
        public int getStudentsNumber() {
            return studentsNumber;
        }
        public void setStudentsNumber() {
            this.studentsNumber++;
        }
        public int getNodeId() {
            return nodeId;
        }
        public void setNodeId() {
            this.nodeId = nodeId+5;
        }

        public ImageView getStudentImage() {
            return studentImage;
        }
    }
    public void initializeScene() {
        System.out.println(Thread.currentThread());
        Platform.runLater(()->{
            System.out.println("initialize scene method");
            System.out.println(Thread.currentThread());
            if (numberOfPlayers == 2) {
                player3Box.setVisible(false);
                player4Box.setVisible(false);

            } else if (numberOfPlayers == 3) {
                player4Box.setVisible(false);
            }
            System.out.println("players ok");
            useSpecialButton.setVisible(expertMode);

            charactersImageMap.put(0, character1);
            charactersImageMap.put(1, character2);
            charactersImageMap.put(2, character3);
            charactersImageMap.put(3, character4);

            schoolMap.put(0, school1);
            schoolMap.put(1, school2);
            schoolMap.put(2, school3);
            schoolMap.put(3, school4);



            for (int i = 0; i < numberOfPlayers; i++) {
            /*setNickname(nicknamesMap.get(i), i);
            charactersImageMap.get(i).setImage(characterToImage(charactersMap.get(i)));*/

                //maps the children of school panes, 0:school imageView, 1: entrance, 2: table 3: professors, 4: towers
                this.entrancesMap.put(i, (AnchorPane) schoolMap.get(i).getChildren().get(1)); //maps all the entrances
                this.tablesMap.put(i, (AnchorPane) schoolMap.get(i).getChildren().get(2)); //maps all the tables
                this.professorsMap.put(i, (AnchorPane) schoolMap.get(i).getChildren().get(3)); //maps all the professors panes
                this.towersMap.put(i, (AnchorPane) schoolMap.get(i).getChildren().get(4));//maps all the towers panes


            }
            System.out.println("Maps set");
            AnchorPane island;
            for (int i = 0; i < 12; i++) {
                island = (AnchorPane) islandsPane.getChildren().get(i);
                islandsMap.put(i, island);
            }

            schoolsInitialization();

            islandsInitialization();
            tablesInitialization();

            gui.isMainSceneInitialized=true;
            actionAllowed=-1; //not your turn
            System.out.println("ALL initialized");
            //gui.phaseHandler("PlanningPhase");
        });


    }



    //inizializza le isole, nascondendo le torri (che non ci sono a inizio partita)
    public void islandsInitialization() {
        System.out.println("island initialization");
        AnchorPane island;
        IslandClickHandler islandClickHandler = new IslandClickHandler();
        for (int i = 0; i < 12; i++) {
            island = islandsMap.get(i);
            island.setOnMouseClicked(islandClickHandler);

            for (int j = 1; j <= 13; j++) {
                island.getChildren().get(j).setVisible(false);
            }
        }
        System.out.println("islands are initialized");
    }

    //inizializza le scuole, mettendo le torri del colore giusto a seconda del numero di giocatori
    public void schoolsInitialization() {
        System.out.println("schoolInitialization");
        ImageView tower;
        if (numberOfPlayers == 2) {
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
            for(int i=0; i<3;i++){
                towersMap.get(i).getChildren().get(6).setVisible(false);
                towersMap.get(i).getChildren().get(7).setVisible(false);
            }
        }
        if (numberOfPlayers == 4) {
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
        TableClickHandler tableClickHandler= new TableClickHandler();
        for(int i=0; i<numberOfPlayers; i++){
            for(int j=0; j<5; j++){
                entrancesMap.get(i).getChildren().get(j).setOnMouseClicked(studentsClickHandler);
                tablesMap.get(i).setOnMouseClicked(tableClickHandler);
            }
        }
        System.out.println("schools are initialized");
    }
    public void tablesInitialization() {
        System.out.println("initialize tables");
        greenTables.add(new Table(10, 4, 0));
        redTables.add(new Table(10, 28, 1));
        yellowTables.add(new Table(10, 52, 2));
        pinkTables.add( new Table(10, 76, 3));
        blueTables.add(new Table(10, 100, 4));

        greenTables.add(new Table(170, 100, 0));
        redTables.add(new Table(170, 76, 1));
        yellowTables.add(new Table(170, 52, 2));
        pinkTables.add( new Table(170, 28, 3));
        blueTables.add(new Table(170,4 , 4));

        greenTables.add(new Table(128, 10, 0));
        redTables.add(new Table(102, 10, 1));
        yellowTables.add(new Table(76, 10, 2));
        pinkTables.add( new Table(50, 10, 3));
        blueTables.add(new Table(24, 10, 4));

        greenTables.add(new Table(24, 176, 0));
        redTables.add(new Table(50, 176, 1));
        yellowTables.add(new Table(76, 176, 2));
        pinkTables.add( new Table(102, 176, 3));
        blueTables.add(new Table(128, 176, 4));
        System.out.println("tables are initialized");
    }


    public Image characterToImage(String characterName) {
        Image image = null;
        if (characterName.equalsIgnoreCase("WIZARD")) {
            image = new Image(WIZARD);
        } else if (characterName.equalsIgnoreCase("WITCH")) {
            image = new Image(WITCH);
        } else if (characterName.equalsIgnoreCase("SAMURAI")) {
            image = new Image(SAMURAI);
        } else if (characterName.equalsIgnoreCase("KING")) {
            image = new Image(KING);
        }
        return image;
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

    public void setCharacter(String character, int playerRef){
        charactersImageMap.put(playerRef, new ImageView(characterToImage(character)));
    }

    public void moveStudent() {

    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void setProxy(Exit proxy) {
        this.proxy = proxy;
    }
    public void setView(View view){
        this.view=view;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void setExpertMode(boolean expertMode) {
        this.expertMode = expertMode;
    }

    public void setUserInfo(String nickname, String character, int playerRef) {
        nicknamesMap.put(playerRef, nickname);
        charactersMap.put(playerRef, character);
    }

    public void setMotherPosition(int islandRef) {
        ImageView motherNature;
        for (int i = 0; i < islandsMap.size(); i++) {
            motherNature = (ImageView) islandsMap.get(i).getChildren().get(1); //children 1 is always MotherNature
            if (islandRef == i) {
                motherNature.setVisible(true);
            } else
                motherNature.setVisible(false);
        }
    }

    public void setStudentsEntrance(int playerRef, int color, int newStudentsValue) {
        Label studentLabel;
        ImageView studentImage;

        studentLabel = (Label) entrancesMap.get(playerRef).getChildren().get(color + 5); //Labels are located 5 position after images
        studentLabel.setText(String.valueOf(newStudentsValue));
        studentImage = (ImageView) entrancesMap.get(playerRef).getChildren().get(color);
        if (newStudentsValue != 0) {
            studentLabel.setVisible(true);
            studentImage.setVisible(true);
        } else {
            studentLabel.setVisible(false);
            studentImage.setVisible(false);
        }
    }


    public void setStudentsTable(int playerRef, int color, int newStudentsValue) {
        AnchorPane tablePane= tablesMap.get(playerRef);
        Table table = null;
        ImageView student;

        if(color==0){
            table= greenTables.get(playerRef);
        }else if (color==1){
            table= redTables.get(playerRef);
        }else if (color==2){
            table= yellowTables.get(playerRef);
        }else if (color==3){
            table= pinkTables.get(playerRef);
        }else if (color==4){
            table= blueTables.get(playerRef);
        }
        tablePane.getChildren().add(table.getStudentImage());
        student= (ImageView) tablePane.getChildren().get(0);
        student.setX(table.getX());
        student.setY(table.getY());
        table.setNewX(playerRef);
        table.setNewY(playerRef);
        table.setNodeId();
        table.setStudentsNumber();

        student.setVisible(true);
}


    public void setStudentsIsland(int islandRef, int color, int newStudentsValue) {
        AnchorPane island = islandsMap.get(islandRef);
        Label studentLabel;
        ImageView studentImage;
        studentImage = (ImageView) island.getChildren().get(color + 3);
        studentLabel = (Label) island.getChildren().get(color + 8);
        studentLabel.setText(String.valueOf(newStudentsValue));
        studentImage.setVisible(true);
        studentLabel.setVisible(true);

    }

    public void setProfessor(int schoolRef, int color, boolean newProfessorValue) {
        AnchorPane professorsTable = professorsMap.get(schoolRef);
        professorsTable.getChildren().get(color).setVisible(newProfessorValue);
    }

    public String colorToImage(int color) {
        String image = null;
            switch (color) {
                case 0 -> image = GREENSTUDENT;
                case 1 -> image = REDSTUDENT;
                case 2 -> image = YELLOWSTUDENT;
                case 3 -> image = PINKSTUDENT;
                case 4 -> image = BLUESTUDENT;
            }

        return image;
    }

    public void setLastPlayedCard(int playerRef, String assistantCard) {
        Label card=null;
        switch (playerRef){
            case 0 -> card=card1;
            case 1 -> card=card2;
            case 2 -> card=card3;
            case 3 -> card=card4;
        }
        card.setText(assistantCard);
        card.setVisible(true);
    }

    public void setTowersSchool(int schoolRef, int towersNumber) {
        int oldTowersNumber = towersMap.get(schoolRef).getChildren().size();
        ImageView tower;
        Boolean ok=false;
        /*if(towersNumber<oldTowersNumber){
            while(!ok){
                for(int i=oldTowersNumber; i>=0; i--){
                    tower= (ImageView) towersMap.get(schoolRef).getChildren().get(i);
                    if(tower.isVisible()){
                        tower.setVisible(false);
                        ok=true;
                    }
                }

            }

        } else {
            while(!ok){
                for(int i=0; i<oldTowersNumber; i++){
                    tower= (ImageView) towersMap.get(schoolRef).getChildren().get(i);
                    if(!tower.isVisible()){
                        tower.setVisible(true);
                        ok=true;
                    }
                }

            }
        }*/
    }

    public void setTowersIsland(int islandRef, int towersNumber) {
        ImageView towerImage = (ImageView) islandsMap.get(islandRef).getChildren().get(2);
        Label towerLabel = (Label) islandsMap.get(islandRef).getChildren().get(13);

        towerLabel.setText(String.valueOf(towersNumber));
        if(towersNumber!=0){
            towerLabel.setVisible(true);
            towerImage.setVisible(true);
        } else {
            towerLabel.setVisible(false);
            towerImage.setVisible(false);
        }
    }

    public void unifyIsland(int islandToDelete){
        AnchorPane island= islandsMap.get(islandToDelete);
        island.setVisible(false);
    }

    public void setYourNickname(String yourNickname){
        this.yourNickname=yourNickname;
    }

    public void setCurrentPlayer(){
        for(int i=0; i<nicknamesMap.size(); i++){
            if(yourNickname.equals(nicknamesMap.get(i))){
                currentPlayer=i;
            }
        }
        turnLabel.setText("It's your turn!");
        setActionAllowed(0); //student movement allowed
    }
    public void setActionAllowed(int actionAllowed){
        this.actionAllowed=actionAllowed;
        if(actionAllowed==0){
            actionLabel.setText("Move a student in your school or on an island!");
            actionLabel.setVisible(true);
            errorLabel.setVisible(false);

        } else if(actionAllowed==1){
            actionLabel.setText("Move Mother Nature on a Island! (Max movement is: "+view.getMaxStepsMotherNature()+")");
            actionLabel.setVisible(true);
        } else if(actionAllowed==2){
            gui.switchScene(GUI.CLOUDS);
        } else if(actionAllowed == -1){
            turnLabel.setText("It's your opponent's turn. Wait...");
            actionLabel.setVisible(false);
        }
    }
}
