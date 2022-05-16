package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static it.polimi.ingsw.model.Team.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoundStrategyTest {

    private int numberOfPlayer=3;
    String[] playersInfo = {"Giorgio", "SAMURAI", "Marco", "KING", "Dino", "WIZARD"};
    private CloudsManager cloudsManager = new CloudsManager(numberOfPlayer);
    private IslandsManager islandsManager = new IslandsManager();
    private PlayerManager playerManager = new PlayerManager(numberOfPlayer);
    private Bag bag= new Bag();
    RoundStrategy round = new Round(numberOfPlayer, cloudsManager, islandsManager, playerManager, bag);
    private ArrayList<Integer> color1 = new ArrayList<>();
    private ArrayList<Integer> color2 = new ArrayList<>();

    @Test
    @DisplayName("Test if RoundStrategyFactory return the right strategy")
    void roundStrategyFactory(){
        RoundStrategyFactory factory = new RoundStrategyFactory(numberOfPlayer,cloudsManager,islandsManager,playerManager,bag);
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

    void fastSetConqueror(int playerRef, int islandRef, int color){
        islandsManager.incStudent(islandRef,color);
        islandsManager.incStudent(islandRef,color);
        playerManager.setStudentEntrance(playerRef,color);
        playerManager.setStudentEntrance(playerRef,color);
        round.moveStudent(playerRef,color,true,islandRef);
        round.moveStudent(playerRef,color,true,islandRef);
    }

    @Test
    @DisplayName("Test if moveStudent transfer student correctly")
    void moveStudentTest(){
        playerManager.setStudentEntrance(1,0);
        playerManager.setStudentEntrance(1,0);
        int islandStudent = islandsManager.getStudent(0,0);
        round.moveStudent(1,0,false,0);
        assertEquals(islandStudent+1,islandsManager.getStudent(0,0));
        round.moveStudent(1,0,true,-1);
        assertEquals(1,playerManager.getStudentTable(1,0));
    }

    /*@Test
    @DisplayName("Test if conquestIsland change correctly the island's owner")
    void conquestIslandTest(){

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
        assertEquals(NOONE,islandsManager.getTowerTeam(1));
        fastSetConqueror(0,1,0);
        fastSetConqueror(0,1,0);
        round.conquestIsland(1,-1,0);
        assertEquals(NOONE,islandsManager.getTowerTeam(1));
        assertEquals(WHITE,islandsManager.getTowerTeam(0));
        assertEquals(2,islandsManager.getTowerValue(0));

    }*/

    @Test
    @DisplayName("test if highInfluenceTeam return the right team")
    void highInfluenceTeamTest(){
        Team teamStronger;
        teamStronger = round.highInfluenceTeam(0,0,0);
        assertEquals(NOONE, teamStronger);

        fastSetConqueror(0,0,0);
        teamStronger = round.highInfluenceTeam(0,0,0);
        assertEquals(WHITE,teamStronger);

        fastSetConqueror(1,0,1);
        fastSetConqueror(1,0,1);
        teamStronger = round.highInfluenceTeam(0,0,0);
        assertEquals(BLACK, teamStronger);
    }

    @Test
    @DisplayName("Test if movement of mother nature is correct")
    void moveMotherNatureTest(){
        //if island is inhibited
        round.queueForPlanificationPhase();
        islandsManager.increaseInhibited(islandsManager.circularArray(islandsManager.getMotherPos(), 1));
        round.playCard(0,0,"LION"); round.playCard(1,1,"GOOSE"); round.playCard(2, 2,"CAT");
        round.moveMotherNature(0,1,-1);
        assertEquals(0, islandsManager.getInhibited(islandsManager.circularArray(islandsManager.getMotherPos(), 1)));
        //if island is not inhibited
        round.queueForPlanificationPhase();
        round.playCard(0,1,"GOOSE"); round.playCard(1,0,"LION"); round.playCard(2,2, "DOG");
        boolean victory = round.moveMotherNature(0,1,-1);
        assertEquals(false, victory);
    }

    @Test
    @DisplayName("Test if clouds are filled correct")
    void refreshStudentCloudTest(){
        int[] array = {0,0,0,0,0};
        assertAll(
                ()->assertArrayEquals(array,cloudsManager.getStudents(0)),
                ()->assertArrayEquals(array,cloudsManager.getStudents(1)),
                ()->assertArrayEquals(array,cloudsManager.getStudents(2))
        );
        round.refreshStudentsCloud();
        for(int j=0; j<numberOfPlayer; j++) {
            int numberOfStudent=0;
            for (int i = 0; i < 5; i++) {
                numberOfStudent += cloudsManager.getStudents(j)[i];
            }
            assertEquals(4,numberOfStudent);
        }
        boolean victory=false;
        while(!bag.checkVictory()) victory = round.refreshStudentsCloud();;
        assertEquals(true,victory);
    }

    @Test
    @DisplayName("Test if RoundSpecial1's effect is correct")
    void effectRoundSpecial1Test(){
        RoundStrategy round = new RoundSpecial1(numberOfPlayer, cloudsManager, islandsManager, playerManager, bag);
        int studentsNumber = islandsManager.getStudent(0,0);
        color1.add(0);
        if(round.getStudents(0)<=0){
            round.effect(0, color1, color2);
            assertEquals(studentsNumber,islandsManager.getStudent(0,0));
        }
        else {
            round.effect(0, color1, color2);
            assertEquals(studentsNumber+1,islandsManager.getStudent(0,0));
        }

    }

    @Test
    @DisplayName("Test if RoundSpecial2's moveStudent is correct")
    void moveStudentSpecial2Test(){
        RoundStrategy round = new RoundSpecial2(numberOfPlayer, cloudsManager, islandsManager, playerManager, bag);
        fastSetConqueror(0,0,0);
        playerManager.setStudentEntrance(1,0);
        playerManager.setStudentEntrance(1,0);
        round.moveStudent(1,0,true,0);
        round.moveStudent(1,0,true,0);
        assertEquals(1,playerManager.getProfessorPropriety(0));
    }

    @Test
    @DisplayName("Test if RoundSpecial3' moveMotherNature is correct")
    void moveMotherNatureSpecial3Test(){
        RoundStrategy round = new RoundSpecial3(numberOfPlayer, cloudsManager, islandsManager, playerManager, bag);
        round.queueForPlanificationPhase();
        int motherPos = islandsManager.getMotherPos();
        islandsManager.increaseInhibited(islandsManager.circularArray(islandsManager.getMotherPos(), 1));
        islandsManager.increaseInhibited(islandsManager.circularArray(motherPos,-5));
        round.playCard(0,0,"LION"); round.playCard(1,1,"GOOSE"); round.playCard(2, 2,"CAT");
        round.moveMotherNature(0,1,islandsManager.circularArray(motherPos,-5));
        assertEquals(0, islandsManager.getInhibited(islandsManager.circularArray(islandsManager.getMotherPos(), 1)));
        assertEquals(0, islandsManager.getInhibited(islandsManager.circularArray(motherPos, -5)));
        //if island is not inhibited
        round.queueForPlanificationPhase();
        round.playCard(0,1,"GOOSE"); round.playCard(1,0,"LION"); round.playCard(2,2, "DOG");
        boolean victory = round.moveMotherNature(0,1,0);
        assertEquals(false, victory);
    }

    @Test
    @DisplayName("Test if RoundSpecial4's moveMotherNature is correct")
    void moveMotherNatureSpecial4Test(){
        RoundStrategy round = new RoundSpecial4(numberOfPlayer, cloudsManager, islandsManager, playerManager, bag);
        round.queueForPlanificationPhase();
        round.playCard(0,0,"LION"); round.playCard(1,1,"GOOSE"); round.playCard(2, 2,"CAT");
        int motherPos = islandsManager.getMotherPos();
        round.moveMotherNature(0,3,islandsManager.circularArray(motherPos,-5));
        motherPos = islandsManager.circularArray(motherPos, 3);
        assertEquals(motherPos, islandsManager.getMotherPos());
    }

    @Test
    @DisplayName("Test if roundSpecial5's effect is correct")
    void effectRoundSpecial5Test(){
        RoundStrategy round = new RoundSpecial5(numberOfPlayer, cloudsManager, islandsManager, playerManager, bag);
        round.queueForPlanificationPhase();
        round.playCard(0,0,"LION"); round.playCard(1,1,"GOOSE"); round.playCard(2, 2,"CAT");
        round.moveMotherNature(0,1,-1);
        round.effect(0,null,null);
        assertEquals(1, islandsManager.getInhibited(0));
    }

    @Test
    @DisplayName("Test if RoundSpecial6's highInfluenceTeam is correct")
    void highInfluenceTeamSpecial6(){
        RoundStrategy round = new RoundSpecial6(numberOfPlayer, cloudsManager, islandsManager, playerManager, bag);

        fastSetConqueror(0,0,0);
        round.conquestIsland(0,0,0);
        assertEquals(WHITE,islandsManager.getTowerTeam(0));

        islandsManager.incStudent(0,1);
        fastSetConqueror(1,0,1);
        if(islandsManager.getStudent(0,0)==2) {
            //infl white = 2 student+1 towers, black = 3 student
            round.conquestIsland(0, 0, 1);
            assertEquals(BLACK, islandsManager.getTowerTeam(0));
        }
        else{//infl white 3 stundet+1 tower, black 4 student
            islandsManager.incStudent(0,1);
            round.conquestIsland(0, 0, 1);
            assertEquals(BLACK, islandsManager.getTowerTeam(0));
        }
    }

    @Test
    @DisplayName("Test if RoundSpecial7's effect is correct")
    void effectRoundSpecial7Test() {
        RoundStrategy round = new RoundSpecial7(numberOfPlayer, cloudsManager, islandsManager, playerManager, bag);
        color1.add(0);
        color2.add(1);
        boolean done = round.effect(0, color1,color2);
        assertEquals(false, done);
        playerManager.setStudentEntrance(0,0);
        int entranceStudent0 = playerManager.getStudentEntrance(0,0);
        int entranceStudent1 = playerManager.getStudentEntrance(0,1);
        int cardStudent0 = round.getStudents(0);
        int cardStudent1 = round.getStudents(1);
        if(round.getStudents(1)>0){
            done = round.effect(0, color1,color2);
            assertEquals(cardStudent0+1,round.getStudents(0));
            assertEquals(cardStudent1-1, round.getStudents(1));
            assertEquals(entranceStudent0-1,playerManager.getStudentEntrance(0,0));
            assertEquals(entranceStudent1+1,playerManager.getStudentEntrance(0,1));
            assertEquals(true,done);
        }
        else{
            assertEquals(cardStudent0,round.getStudents(0));
            assertEquals(cardStudent1, round.getStudents(1));
            assertEquals(entranceStudent0, playerManager.getStudentEntrance(0,0));
            assertEquals(entranceStudent1,playerManager.getStudentEntrance(0,1));
            assertEquals(false, done);
        }

    }

    @Test
    @DisplayName("Test if RoundSpecial8's highInfluenceTeam is correct")
    void highInfluenceTeamSpecial8(){
        RoundStrategy round = new RoundSpecial8(numberOfPlayer, cloudsManager, islandsManager, playerManager, bag);

        fastSetConqueror(0,0,0);
        if(islandsManager.getStudent(0,0)==2) { //influence white=2 stud black=0
            round.conquestIsland(0, 0, 1);
            assertEquals(NOONE, islandsManager.getTowerTeam(0));
        }
    }

    @Test
    @DisplayName("Test if RoundSpecial9's highInfluenceTeam is correct")
    void highInfluenceTeamSpecial9(){
        RoundStrategy round = new RoundSpecial9(numberOfPlayer, cloudsManager, islandsManager, playerManager, bag);

        fastSetConqueror(0,0,3);
        //influence white=2 stud black=0, block color 3
        round.conquestIsland(0,3,0);
        assertEquals(NOONE,islandsManager.getTowerTeam(0));

        //influence white=2 stud black=0, no block
        round.conquestIsland(0,-1,0);
        assertEquals(WHITE,islandsManager.getTowerTeam(0));

    }

    @Test
    @DisplayName("Test if RoundSpecial10's effect is correct")
    void effectRoundSpecial10Test() {
        RoundStrategy round = new RoundSpecial10(numberOfPlayer, cloudsManager, islandsManager, playerManager, bag);
        color1.add(0);
        color2.add(1);
        boolean done = round.effect(0, color1,color2);
        assertEquals(false, done);
        playerManager.setStudentEntrance(0,0);
        playerManager.setStudentTable(0,1);
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
            RoundStrategy round = new RoundSpecial11(numberOfPlayer, cloudsManager, islandsManager, playerManager, bag);
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
    }

    @Test
    @DisplayName("Test if RoundSpecial12's effect is correct")
    void effectRoundSpecial12Test() {
        RoundStrategy round = new RoundSpecial12(numberOfPlayer, cloudsManager, islandsManager, playerManager, bag);
        playerManager.setStudentTable(0,0);
        playerManager.setStudentTable(0,0);
        playerManager.setStudentTable(1,0);
        playerManager.setStudentTable(1,0);
        playerManager.setStudentTable(1,0);
        playerManager.setStudentTable(1,0);
        round.effect(0, color1,color2);
        assertEquals(0,playerManager.getStudentTable(0,0));
        assertEquals(1,playerManager.getStudentTable(1,0));
        assertEquals(0,playerManager.getStudentTable(2,0));
    }






}
