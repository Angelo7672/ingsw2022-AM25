package it.polimi.ingsw.client;

import java.io.IOException;
import java.util.ArrayList;

public interface Exit {

    String first() throws IOException, ClassNotFoundException;

    ArrayList<String> getChosenCharacters() throws IOException, ClassNotFoundException;

    View startView() throws IOException, ClassNotFoundException, InterruptedException;

    boolean setupConnection(String nickname, String character) throws IOException, ClassNotFoundException ;

    boolean setupGame(int numberOfPlayers, String expertMode) throws IOException, ClassNotFoundException;

    boolean checkSpecial(int special)  throws IOException, ClassNotFoundException ;

    boolean useSpecial(int special, int i, ArrayList<Integer> color1, ArrayList<Integer> color2) throws IOException, ClassNotFoundException ;

    String playCard(String card) throws IOException, ClassNotFoundException;

    boolean startActionPhase() throws IOException, ClassNotFoundException;

    String moveStudent(int colorInt, String where, int islandRef) throws IOException, ClassNotFoundException;

    String moveMotherNature(int steps) throws IOException, ClassNotFoundException ;

    String chooseCloud(int cloud) throws IOException, ClassNotFoundException;

    boolean startPlanningPhase() throws ClassNotFoundException, IOException;

    String getWinner();
}
