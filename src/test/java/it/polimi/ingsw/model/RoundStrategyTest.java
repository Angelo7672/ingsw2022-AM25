package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.model.exception.NotAllowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.model.Assistant.*;
import static it.polimi.ingsw.model.Team.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoundStrategyTest {

    Game game = new Game(true, 3);
    private int numberOfPlayer = 3;
    String[] playersInfo = {"Giorgio", "SAMURAI", "Marco", "KING", "Dino", "WIZARD"};
    private IslandsManager islandsManager = new IslandsManager();
    private Bag bag = new Bag();
    private List<Integer>  bagRestore;
    private CloudsManager cloudsManager;
    private PlayerManager playerManager;
    private QueueManager queueManager;
    RoundStrategy round;
    private ArrayList<Integer> color1 = new ArrayList<>();
    private ArrayList<Integer> color2 = new ArrayList<>();
    private ArrayList<Assistant> alreadyPlayedAssistant = new ArrayList<>();

    @BeforeEach
    void initialization(){
        islandsManager.islandListener = new IslandListener() {
            @Override
            public void notifyIslandChange(int islandToDelete) {}
        };
        islandsManager.towersListener = new TowersListener() {
            @Override
            public void notifyTowersChange(int place, int componentRef, int towersNumber) {}

            @Override
            public void notifyTowerColor(int islandRef, int newColor) {}
        };
        islandsManager.motherPositionListener = new MotherPositionListener() {
            @Override
            public void notifyMotherPosition(int newMotherPosition) {}
        };
        islandsManager.inhibitedListener = new InhibitedListener() {
            @Override
            public void notifyInhibited(int islandRef, int isInhibited) {}
        };
        islandsManager.studentListener = new StudentsListener() {
            @Override
            public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {}
        };
        islandsManager.islandsInitialize();

        bagRestore = new ArrayList<>();
        bag = new Bag();
        bag.bagListener = new BagListener() {
            @Override
            public void notifyBagExtraction() {}
            @Override
            public void notifyBag(List<Integer> bag) {
                bagRestore = bag;
            }
        };
        bag.bagInitialize();

        cloudsManager = new CloudsManager(numberOfPlayer, bag);
        cloudsManager.studentsListener = new StudentsListener() {
            @Override
            public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {}
        };


        playerManager = new PlayerManager(numberOfPlayer, bag);
        playerManager.towersListener = new TowersListener() {
            @Override
            public void notifyTowersChange(int place, int componentRef, int towersNumber) {}
            @Override
            public void notifyTowerColor(int islandRef, int newColor) {}
        };
        playerManager.professorsListener = new ProfessorsListener() {
            @Override
            public void notifyProfessors(int playerRef, int color, boolean newProfessorValue) {}
        };
        playerManager.coinsListener = new CoinsListener() {
            @Override
            public void notifyNewCoinsValue(int playerRef, int newCoinsValue) {
            }
        };
        playerManager.playedCardListener = new PlayedCardListener() {
            @Override
            public void notifyPlayedCard(int playerRef, String assistantCard) {}
            @Override
            public void notifyHand(int playerRef, ArrayList<String> hand) {
            }
        };
        playerManager.studentsListener = new StudentsListener() {
            @Override
            public void notifyStudentsChange(int place, int componentRef, int color, int newStudentsValue) {}
        };

        queueManager = new QueueManager(numberOfPlayer, playerManager);
        queueManager.queueListener = new QueueListener() {
            @Override
            public void notifyQueue(int queueRef, int playerRef) {}
            @Override
            public void notifyValueCard(int queueRef, int valueCard) {}
            @Override
            public void notifyMaxMove(int queueRef, int maxMove) {}
        };
        queueManager.playedCardListener = new PlayedCardListener() {
            @Override
            public void notifyPlayedCard(int playerRef, String assistantCard) {}
            @Override
            public void notifyHand(int playerRef, ArrayList<String> hand) {}
        };

        playerManager.initializeSchool();

        round = new Round(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
    }
    @Test
    @DisplayName("Test if RoundStrategyFactory return the right strategy")
    void roundStrategyFactory() {
        RoundStrategyFactory factory = new RoundStrategyFactory(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        RoundStrategy round = factory.getRoundStrategy(1);
        assertEquals("special1", round.getName());
        round = factory.getRoundStrategy(2);
        assertEquals("special2", round.getName());
        round = factory.getRoundStrategy(3);
        assertEquals("special3", round.getName());
        round = factory.getRoundStrategy(4);
        assertEquals("special4", round.getName());
        round = factory.getRoundStrategy(5);
        assertEquals("special5", round.getName());
        round = factory.getRoundStrategy(6);
        assertEquals("special6", round.getName());
        round = factory.getRoundStrategy(7);
        assertEquals("special7", round.getName());
        round = factory.getRoundStrategy(8);
        assertEquals("special8", round.getName());
        round = factory.getRoundStrategy(9);
        assertEquals("special9", round.getName());
        round = factory.getRoundStrategy(10);
        assertEquals("special10", round.getName());
        round = factory.getRoundStrategy(11);
        assertEquals("special11", round.getName());
        round = factory.getRoundStrategy(12);
        assertEquals("special12", round.getName());

    }

    void fastSetConqueror(int playerRef, int islandRef, int color) throws NotAllowedException {
        islandsManager.incStudent(islandRef, color, 1);
        islandsManager.incStudent(islandRef, color, 1);
        playerManager.setStudentEntrance(playerRef, color, 1);
        playerManager.setStudentEntrance(playerRef, color, 1);
        round.moveStudent(playerRef, color, true, islandRef);
        round.moveStudent(playerRef, color, true, islandRef);
    }

    @Test
    @DisplayName("Test if moveStudent transfer student correctly")
    void moveStudentTest() throws NotAllowedException {
        playerManager.setStudentEntrance(1, 0, 1);
        playerManager.setStudentEntrance(1, 0, 1);
        int islandStudent = islandsManager.getStudent(0, 0);
        round.moveStudent(1, 0, false, 0);
        assertEquals(islandStudent + 1, islandsManager.getStudent(0, 0));
        round.moveStudent(1, 0, true, -1);
        assertEquals(1, playerManager.getStudentTable(1, 0));
    }

    /*@Test
    @DisplayName("Test if conquestIsland change correctly the island's owner")
    void conquestIslandTest() throws NotAllowedException {

        fastSetConqueror(1,0,0);
        //TeamWeaker = NOONE TeamStronger = BLACK
        round.conquestIsland(0,-1,1);
        assertEquals(Team.BLACK, islandsManager.getTowerTeam(0));
        assertEquals(5,playerManager.getTowers(1));
        //teamWeaker= BLACK teamStronger = WHITE
        fastSetConqueror(0,0,1);
        fastSetConqueror(0,0,1);
        round.conquestIsland(0,-1,0);
        assertEquals(WHITE, islandsManager.getTowerTeam(0));
        assertEquals(6,playerManager.getTowers(1));
        assertEquals(5,playerManager.getTowers(0));
        assertEquals(1, islandsManager.getTowerValue(0));

        //adjacent islands getting unify
        assertEquals(Team.NONE,islandsManager.getTowerTeam(1));
        fastSetConqueror(0,1,0);
        fastSetConqueror(0,1,0);
        round.conquestIsland(1,-1,0);
        assertEquals(Team.NONE,islandsManager.getTowerTeam(1));
        assertEquals(WHITE,islandsManager.getTowerTeam(0));
        assertEquals(2,islandsManager.getTowerValue(0));

    }*/

    @Test
    @DisplayName("test if highInfluenceTeam return the right team")
    void highInfluenceTeamTest() throws NotAllowedException {
        Team teamStronger;
        teamStronger = round.highInfluenceTeam(0, 0, 0);
        assertEquals(Team.NONE, teamStronger);

        fastSetConqueror(0, 0, 0);
        teamStronger = round.highInfluenceTeam(0, 0, 0);
        assertEquals(WHITE, teamStronger);

        fastSetConqueror(1, 0, 1);
        fastSetConqueror(1, 0, 1);
        teamStronger = round.highInfluenceTeam(0, 0, 0);
        assertEquals(BLACK, teamStronger);
    }

    @Test
    @DisplayName("Test if movement of mother nature is correct")
    void moveMotherNatureTest() throws NotAllowedException {
        //if island is inhibited
        queueManager.queueForPlanificationPhase();
        islandsManager.increaseInhibited(islandsManager.circularArray(islandsManager.getMotherPos(), 1));
        queueManager.playCard(0, 0, LION, alreadyPlayedAssistant);
        queueManager.playCard(1, 1, GOOSE, alreadyPlayedAssistant);
        queueManager.playCard(2, 2, CAT, alreadyPlayedAssistant);
        round.moveMotherNature(0, 1, -1);
        assertEquals(0, islandsManager.getInhibited(islandsManager.circularArray(islandsManager.getMotherPos(), 1)));
        //if island is not inhibited
        queueManager.queueForPlanificationPhase();
        queueManager.playCard(0, 1, GOOSE, alreadyPlayedAssistant);
        queueManager.playCard(1, 0, LION, alreadyPlayedAssistant);
        queueManager.playCard(2, 2, DOG, alreadyPlayedAssistant);
        boolean victory = round.moveMotherNature(0, 1, -1);
        assertEquals(false, victory);
    }

    /*@Test
    @DisplayName("Test if RoundSpecial1's effect is correct")
    void effectRoundSpecial1Test() {
        RoundStrategy round = new RoundSpecial1(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        round.initializeSpecial();
        int studentsNumber = islandsManager.getStudent(0, 0);
        boolean roundEffect = round.effect(0, 0);
        if (roundEffect) {
            assertEquals(studentsNumber+1, islandsManager.getStudent(0, 0));
        } else {
            assertEquals(studentsNumber, islandsManager.getStudent(0, 0));
        }

    }*/

    @Test
    @DisplayName("Test if RoundSpecial2's moveStudent is correct")
    void moveStudentSpecial2Test() throws NotAllowedException {
        RoundStrategy round = new RoundSpecial2(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        fastSetConqueror(0, 0, 0);
        playerManager.setStudentEntrance(1, 0, 1);
        playerManager.setStudentEntrance(1, 0, 1);
        round.moveStudent(1, 0, true, 0);
        round.moveStudent(1, 0, true, 0);
        assertEquals(1, playerManager.getProfessorPropriety(0));
    }

    @Test
    @DisplayName("Test if RoundSpecial3' moveMotherNature is correct")
    void moveMotherNatureSpecial3Test() throws NotAllowedException {
        RoundStrategy round = new RoundSpecial3(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        queueManager.queueForPlanificationPhase();
        int motherPos = islandsManager.getMotherPos();
        islandsManager.increaseInhibited(islandsManager.circularArray(islandsManager.getMotherPos(), 1));
        islandsManager.increaseInhibited(islandsManager.circularArray(motherPos, -5));
        queueManager.playCard(0, 0, LION, alreadyPlayedAssistant);
        queueManager.playCard(1, 1, GOOSE, alreadyPlayedAssistant);
        queueManager.playCard(2, 2, CAT, alreadyPlayedAssistant);
        round.moveMotherNature(0, 1, islandsManager.circularArray(motherPos, -5));
        assertEquals(0, islandsManager.getInhibited(islandsManager.circularArray(islandsManager.getMotherPos(), 1)));
        assertEquals(0, islandsManager.getInhibited(islandsManager.circularArray(motherPos, -5)));
        //if island is not inhibited
        queueManager.queueForPlanificationPhase();
        queueManager.playCard(0, 1, GOOSE, alreadyPlayedAssistant);
        queueManager.playCard(1, 0, LION, alreadyPlayedAssistant);
        queueManager.playCard(2, 2, DOG, alreadyPlayedAssistant);
        boolean victory = round.moveMotherNature(0, 1, 0);
        assertEquals(false, victory);
    }

    @Test
    @DisplayName("Test if RoundSpecial4's moveMotherNature is correct")
    void moveMotherNatureSpecial4Test() throws NotAllowedException {
        RoundStrategy round = new RoundSpecial4(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        queueManager.queueForPlanificationPhase();
        queueManager.playCard(0, 0, LION, alreadyPlayedAssistant);
        queueManager.playCard(1, 1, GOOSE, alreadyPlayedAssistant);
        queueManager.playCard(2, 2, CAT, alreadyPlayedAssistant);
        int motherPos = islandsManager.getMotherPos();
        round.moveMotherNature(0, 3, islandsManager.circularArray(motherPos, -5));
        motherPos = islandsManager.circularArray(motherPos, 3);
        assertEquals(motherPos, islandsManager.getMotherPos());
    }

    /*@Test
    @DisplayName("Test if roundSpecial5's effect is correct")
    void effectRoundSpecial5Test() throws NotAllowedException {
        RoundStrategy round = new RoundSpecial5(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);

        queueManager.queueForPlanificationPhase();
        queueManager.playCard(0, 0, LION, alreadyPlayedAssistant);
        queueManager.playCard(1, 1, GOOSE, alreadyPlayedAssistant);
        queueManager.playCard(2, 2, CAT, alreadyPlayedAssistant);
        round.moveMotherNature(0, 1, -1);
        round.effect(0);
        assertEquals(1, islandsManager.getInhibited(0));
    }*/

    @Test
    @DisplayName("Test if RoundSpecial6's highInfluenceTeam is correct")
    void highInfluenceTeamSpecial6() throws NotAllowedException {
        RoundStrategy round = new RoundSpecial6(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);

        fastSetConqueror(0, 0, 0);
        round.conquestIsland(0, 0, 0);
        assertEquals(WHITE, islandsManager.getTowerTeam(0));

        islandsManager.incStudent(0, 1, 1);
        fastSetConqueror(1, 0, 1);
        if (islandsManager.getStudent(0, 0) == 2) {
            //infl white = 2 student+1 towers, black = 3 student
            round.conquestIsland(0, 0, 1);
            assertEquals(BLACK, islandsManager.getTowerTeam(0));
        } else {//infl white 3 stundet+1 tower, black 4 student
            islandsManager.incStudent(0, 1, 1);
            round.conquestIsland(0, 0, 1);
            assertEquals(BLACK, islandsManager.getTowerTeam(0));
        }
    }

    /*@Test
    @DisplayName("Test if RoundSpecial7's effect is correct")
    void effectRoundSpecial7Test() {
        RoundStrategy round = new RoundSpecial7(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        color1.add(0);
        color2.add(1);
        boolean done = round.effect(0, color1,color2);
        assertEquals(false, done);
        playerManager.setStudentEntrance(0,0, 1);
        int entranceStudent0 = playerManager.getStudentEntrance(0,0);
        int entranceStudent1 = playerManager.getStudentEntrance(0,1);
        int cardStudent0 = round.getStudents(0);
        int cardStudent1 = round.getStudents(1);
        done = round.effect(0, color1,color2);
        if(done){
            assertEquals(cardStudent0+1,round.getStudents(0));
            assertEquals(cardStudent1-1, round.getStudents(1));
            assertEquals(entranceStudent0-1,playerManager.getStudentEntrance(0,0));
            assertEquals(entranceStudent1+1,playerManager.getStudentEntrance(0,1));
        }
        else{
            assertEquals(cardStudent0,round.getStudents(0));
            assertEquals(cardStudent1, round.getStudents(1));
            assertEquals(entranceStudent0, playerManager.getStudentEntrance(0,0));
            assertEquals(entranceStudent1,playerManager.getStudentEntrance(0,1));
        }

    }*/

    @Test
    @DisplayName("Test if RoundSpecial8's highInfluenceTeam is correct")
    void highInfluenceTeamSpecial8() throws NotAllowedException {
        RoundStrategy round = new RoundSpecial8(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);

        fastSetConqueror(0, 0, 0);
        if (islandsManager.getStudent(0, 0) == 2) { //influence white=2 stud black=0
            round.conquestIsland(0, 0, 1);
            assertEquals(Team.NONE, islandsManager.getTowerTeam(0));
        }
    }

    @Test
    @DisplayName("Test if RoundSpecial9's highInfluenceTeam is correct")
    void highInfluenceTeamSpecial9() throws NotAllowedException {
        RoundStrategy round = new RoundSpecial9(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);

        fastSetConqueror(0, 0, 3);
        //influence white=2 stud black=0, block color 3
        round.conquestIsland(0, 3, 0);
        assertEquals(Team.NONE, islandsManager.getTowerTeam(0));

        //influence white=2 stud black=0, no block
        round.conquestIsland(0, -1, 0);
        assertEquals(WHITE, islandsManager.getTowerTeam(0));

    }

    /*@Test
    @DisplayName("Test if RoundSpecial10's effect is correct")
    void effectRoundSpecial10Test() throws NotAllowedException {
        RoundStrategy round = new RoundSpecial10(numberOfPlayer, cloudsManager, islandsManager, playerManager,queueManager, bag);
        color1.add(0);
        color2.add(1);
        boolean done = round.effect(0, color1,color2);
        assertEquals(false, done);
        playerManager.setStudentEntrance(0,0, 1);
        playerManager.setStudentTable(0,1, 1);
        int entranceStudent0 = playerManager.getStudentEntrance(0,0);
        int entranceStudent1 = playerManager.getStudentEntrance(0,1);
        int tableStudent0 = playerManager.getStudentTable(0,0);
        int tableStudent1 = playerManager.getStudentTable(0,1);
        done = round.effect(0, color1, color2);
        assertEquals(tableStudent0+1,playerManager.getStudentTable(0,0));
        assertEquals(tableStudent1-1, playerManager.getStudentTable(0,1));
        assertEquals(entranceStudent0-1,playerManager.getStudentEntrance(0,0));
        assertEquals(entranceStudent1+1,playerManager.getStudentEntrance(0,1));
        assertEquals(true,done);
    }

    @Test
    @DisplayName("Test if RoundSpecial11's effect is correct")
    void effectRoundSpecial11Test() {
            RoundStrategy round = new RoundSpecial11(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            color1.add(0);
            boolean done = false;
            if (round.getStudents(0) > 0) {
                int cardStudent1=0;
                int cardStudent2=0;
                for(int i=0; i<5; i++) cardStudent1+=round.getStudents(i);
                done = round.effect(0, color1, color2);
                assertEquals(true, done);
                assertEquals(1, playerManager.getStudentTable(0, 0));
                for(int i=0; i<5; i++) cardStudent2+=round.getStudents(i);
                assertEquals(cardStudent1, cardStudent2);
            } else {
                done = round.effect(0, color1, color2);
                assertEquals(false, done);
            }
    }*/

    @Test
    @DisplayName("Test if RoundSpecial12's effect is correct")
    void effectRoundSpecial12Test() throws NotAllowedException {
        RoundStrategy round = new RoundSpecial12(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        playerManager.setStudentTable(0, 0, 1);
        playerManager.setStudentTable(0, 0, 1);
        playerManager.setStudentTable(1, 0, 1);
        playerManager.setStudentTable(1, 0, 1);
        playerManager.setStudentTable(1, 0, 1);
        playerManager.setStudentTable(1, 0, 1);
        ArrayList<Integer> studentsTable = new ArrayList<>();
        for (int i = 0; i < numberOfPlayer; i++) {
            studentsTable.add(playerManager.getStudentTable(i,0));
        }
        for(int i=0; i<numberOfPlayer; i++){
            if(studentsTable.get(i)>=3) studentsTable.set(i,(studentsTable.get(i)-3));
            else studentsTable.set(i, 0);
        }
        round.effect(0);
        for (int i = 0; i < numberOfPlayer; i++) {
            assertEquals(studentsTable.get(i), playerManager.getStudentTable(i, 0));
        }

    }



}

