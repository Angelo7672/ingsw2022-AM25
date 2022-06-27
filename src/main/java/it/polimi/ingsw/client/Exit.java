package it.polimi.ingsw.client;

import it.polimi.ingsw.listeners.DisconnectedListener;
import it.polimi.ingsw.listeners.ServerOfflineListener;
import it.polimi.ingsw.listeners.SoldOutListener;
import it.polimi.ingsw.server.answer.Answer;

import java.io.IOException;
import java.util.ArrayList;

public interface Exit {

    void setDisconnectedListener(DisconnectedListener disconnectedListener);

    void setServerOfflineListener(ServerOfflineListener serverOfflineListener) throws IOException;

    void setSoldOutListener(SoldOutListener soldOutListener) throws IOException;

    String first() throws IOException, ClassNotFoundException;

    ArrayList<String> getChosenCharacters() throws IOException, ClassNotFoundException;

    View startView() throws IOException, ClassNotFoundException, InterruptedException;

    void setView();

    boolean setupConnection(String nickname, String character) throws IOException, ClassNotFoundException ;

    boolean setupGame(int numberOfPlayers, String expertMode) throws IOException, ClassNotFoundException;

    boolean savedGame(String decision) throws IOException;

    String getPhase() throws IOException;

    boolean readyForLogin() throws IOException;

    boolean checkSpecial(int special)  throws IOException, ClassNotFoundException ;

    boolean useSpecial(int special, ArrayList<Integer> color1, ArrayList<Integer> color2) throws IOException, ClassNotFoundException ;

    boolean useSpecial(int special, int ref) throws IOException;

    boolean useSpecial(int special, int ref, int color) throws IOException;

    String playCard(String card) throws IOException, ClassNotFoundException;

    boolean startActionPhase() throws IOException, ClassNotFoundException;

    String moveStudent(int colorInt, String where, int islandRef) throws IOException, ClassNotFoundException;

    String moveMotherNature(int steps) throws IOException, ClassNotFoundException ;

    String chooseCloud(int cloud) throws IOException, ClassNotFoundException;

    boolean startPlanningPhase() throws ClassNotFoundException, IOException;

    Answer getMessage();
}
