package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.*;

/**
 * QueueManger keeps track of all information regarding turns of player.
 * It also keeps for each player the mac move of mother nature given by its card and the value of his card that is used for sort the queue for action phase.
 * Notify method send changes from QueueManager to VirtualView.
 */
public class QueueManager {
    private final List<Queue> queue;
    private final PlayerManager playerManager;
    private final int numberOfPlayer;
    protected PlayedCardListener playedCardListener;
    protected QueueListener queueListener;

    /**
     * The first player to play is chosen randomly.
     * @param numberOfPlayer in the game;
     * @param playerManager of the game;
     */
    public QueueManager(int numberOfPlayer,PlayerManager playerManager) {
        this.playerManager = playerManager;
        this.numberOfPlayer = numberOfPlayer;
        Random rnd = new Random();
        queue = new ArrayList<>();
        int firstInQueue;

        //The first player is chosen randomly, then queueForPlanificationPhase order the queue
        firstInQueue = rnd.nextInt(numberOfPlayer);
        Queue first = new Queue(firstInQueue,-1,-1);
        queue.add(first);
    }

    /**
     * Restore queue.
     * @param playerRef the player reference;
     * @param valueCard list of value of the card play in the last turn of the last game;
     * @param maxMoveMotherNature list of max movement of mother nature of the card play in the last turn of the last game;
     */
    public void queueRestore(ArrayList<Integer> playerRef, ArrayList<Integer> valueCard, ArrayList<Integer> maxMoveMotherNature){
        queue.remove(0);
        for(int i = 0; i < numberOfPlayer; i++)
            queue.add(new Queue(playerRef.get(i), valueCard.get(i), maxMoveMotherNature.get(i)));
        listenMyQueue();
    }

    /**
     * Order queue for next planning phase.
     * Take the first player of queue then proceeds clockwise: the distribution of players at the table is arranged clockwise in this order 1 2 3 4.
     */
    public void queueForPlanificationPhase(){
        int firstInQueue;

        firstInQueue = readQueue(0);         //destroy the previous queue
        while (!queue.isEmpty()) queue.remove(0);

        Queue one = new Queue(0,-1,-1);
        Queue two = new Queue(1,-1,-1);
        queue.add(one);queue.add(two);
        if(numberOfPlayer == 3){
            Queue three = new Queue(2,-1,-1);
            queue.add(three);
        }else if(numberOfPlayer == 4){
            Queue three = new Queue(2,-1,-1);
            Queue four = new Queue(3,-1,-1);
            queue.add(three);queue.add(four);
        }
        //The first player is the one who played first in the previous action phase, then proceeds clockwise. The distribution of players at the table is arranged clockwise in this order 1 2 3 4
        if (firstInQueue != 0) Collections.rotate(queue,-firstInQueue);
        listenMyQueue();
    }

    /**
     * Play card from PlayerManager.
     * @param playerRef the player reference;
     * @param queueRef the position of the player in queue;
     * @param card the card played from the player;
     * @param alreadyPlayedAssistant list of already played card in this turn;
     * @return if the player has run out of cards;
     * @throws NotAllowedException throw in case player broke rules of playCard in playerManager;
     */
    public boolean playCard(int playerRef, int queueRef, Assistant card, ArrayList<Assistant> alreadyPlayedAssistant) throws NotAllowedException {
        playerManager.playCard(playerRef,card,alreadyPlayedAssistant);
        queue.get(queueRef).setValueCard(card.getValue());
        queue.get(queueRef).setMaxMoveMotherNature(card.getMovement());
        this.playedCardListener.notifyPlayedCard(playerRef, String.valueOf(card));

        return playerManager.checkIfCardsFinished(playerRef);  //if it's true, game will finish at the end of the turn
    }

    /**
     * Order queue for next action phase.
     * It is sorted in ascending order based on the value of the card played.
     */
    public void inOrderForActionPhase(){
        queue.sort(Queue::compareTo);
        listenMyQueue();
    }

    /**
     * This method is used to notify at virtualView changes in queue.
     */
    private void listenMyQueue(){
        for(int i = 0; i < numberOfPlayer; i++){
            this.queueListener.notifyQueue(i, queue.get(i).getPlayerRef());
            this.queueListener.notifyValueCard(i, queue.get(i).getValueCard());
            this.queueListener.notifyMaxMove(i, queue.get(i).getMaxMoveMotherNature());
        }
    }

    public int readQueue(int queueRef){ return queue.get(queueRef).getPlayerRef(); }
    public int readMaxMotherNatureMovement(int queueRef){ return queue.get(queueRef).getMaxMoveMotherNature(); }

    /**
     * This class contain info of an element in queue with his player reference, value of card and max movement of mother nature.
     * It is used both in the planning phase and in the action phase.
     */
    private static class Queue implements Comparable<Queue> {
        private final int playerRef;
        private Integer valueCard;
        private int maxMoveMotherNature;

        private Queue(int playerRef, Integer valueCard, int maxMoveMotherNature) {
            this.playerRef = playerRef;
            this.valueCard = valueCard;
            this.maxMoveMotherNature = maxMoveMotherNature;
        }

        private int getPlayerRef() { return playerRef; }
        private Integer getValueCard() { return valueCard; }
        private int getMaxMoveMotherNature() { return maxMoveMotherNature; }
        private void setValueCard(Integer valueCard) { this.valueCard = valueCard; }
        private void setMaxMoveMotherNature(int maxMoveMotherNature) { this.maxMoveMotherNature = maxMoveMotherNature; }

        /**
         * Compare two element of queue by the valueCard.
         * @param o the other element with compare to.
         * @return the sort between them;
         */
        @Override
        public int compareTo(Queue o) { return valueCard.compareTo(o.getValueCard()); }
    }
}