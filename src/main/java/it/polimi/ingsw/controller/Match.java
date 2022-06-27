package it.polimi.ingsw.controller;

/**
 * This interface is used by RoundController for comunicate with controller.
 */
public interface Match {

    /**
     * @see Controller
     */
    void oneLastRide();

    /**
     * @see Controller
     */
    void saveGame();

    /**
     * @see Controller
     */
    int getCurrentUser();

    /**
     * @see Controller
     */
    void setCurrentUser(int currentUser);

    /**
     * @see Controller
     */
    void incrCurrentUser();

    /**
     * @see Controller
     */
    String getWinner();
}
