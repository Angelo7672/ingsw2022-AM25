package it.polimi.ingsw.client;

import it.polimi.ingsw.listeners.DisconnectedListener;
import it.polimi.ingsw.listeners.ServerOfflineListener;
import it.polimi.ingsw.server.answer.Answer;

import java.io.IOException;
import java.util.ArrayList;

public interface Exit {

    void setDisconnectedListener(DisconnectedListener disconnectedListener);

    void setServerOfflineListener(ServerOfflineListener serverOfflineListener);

    String first();

    ArrayList<String> getChosenCharacters();

    View startView();

    void setView();

    boolean setupConnection(String nickname, String character);

    boolean setupGame(int numberOfPlayers, String expertMode);

    boolean savedGame(String decision);

    String getPhase();

    boolean readyForLogin();

    boolean checkSpecial(int special);

    boolean useSpecial(int special, ArrayList<Integer> color1, ArrayList<Integer> color2);

    boolean useSpecial(int special, int ref);

    boolean useSpecial(int special, int ref, int color);

    String playCard(String card);

    boolean startActionPhase();

    String moveStudent(int colorInt, String where, int islandRef);

    String moveMotherNature(int steps);

    String chooseCloud(int cloud);

    boolean startPlanningPhase();

    Answer getMessage();
}
