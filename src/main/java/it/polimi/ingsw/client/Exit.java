package it.polimi.ingsw.client;

import it.polimi.ingsw.listeners.DisconnectedListener;
import it.polimi.ingsw.listeners.ServerOfflineListener;
import it.polimi.ingsw.server.answer.Answer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Exit interface is used to send to clients information from Server.
 */
public interface Exit {


    void setDisconnectedListener(DisconnectedListener disconnectedListener);

    void setServerOfflineListener(ServerOfflineListener serverOfflineListener);

    /**
     * @See proxy_c
     */
    String first();

    /**
     * @See proxy_c
     */
    ArrayList<String> getChosenCharacters();

    /**
     * @See proxy_c
     */
    View startView();

    /**
     * @See proxy_c
     */
    void setView();

    /**
     * @See proxy_c
     */
    boolean setupConnection(String nickname, String character);

    /**
     * @See proxy_c
     */
    boolean setupGame(int numberOfPlayers, String expertMode);

    /**
     * @See proxy_c
     */
    boolean savedGame(String decision);

    /**
     * @See proxy_c
     */
    String getPhase();

    /**
     * @See proxy_c
     */
    boolean readyForLogin();

    /**
     * @See proxy_c
     */
    boolean checkSpecial(int special);

    /**
     * @See proxy_c
     */
    boolean useSpecial(int special, ArrayList<Integer> color1, ArrayList<Integer> color2);

    /**
     * @See proxy_c
     */
    boolean useSpecial(int special, int ref);

    /**
     * @See proxy_c
     */
    boolean useSpecial(int special, int ref, int color);

    /**
     * @See proxy_c
     */
    String playCard(String card);

    /**
     * @See proxy_c
     */
    boolean startActionPhase();

    /**
     * @See proxy_c
     */
    String moveStudent(int colorInt, String where, int islandRef);

    /**
     * @See proxy_c
     */
    String moveMotherNature(int steps);

    /**
     * @See proxy_c
     */
    String chooseCloud(int cloud);

    /**
     * @See proxy_c
     */
    boolean startPlanningPhase();

    /**
     * @See proxy_c
     */
    Answer getMessage();
}
