package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.listeners.*;
import it.polimi.ingsw.model.exception.NotAllowedException;

import java.util.*;

public class QueueManager {
    private List<Queue> queue;
    private PlayerManager playerManager;
    private int numberOfPlayer;
    protected PlayedCardListener playedCardListener;
    protected QueueListener queueListener;

    public QueueManager(int numberOfPlayer,PlayerManager playerManager) {
        this.playerManager = playerManager;
        this.numberOfPlayer = numberOfPlayer;
        Random rnd = new Random();
        queue = new ArrayList<>();
        int firstInQueue;

        //The first player is chosen randomly, then queueForPlanificationPhase order the queue
        firstInQueue = rnd.nextInt(numberOfPlayer - 1);
        Queue first = new Queue(firstInQueue,-1,-1);
        queue.add(first);
    }

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

        this.queueListener.notifyResetQueue();
        for(Queue q:queue){
            this.queueListener.notifyQueue(q.getPlayerRef());
        }

    }

    public boolean playCard(int playerRef, int queueRef, Assistant card) throws NotAllowedException {
        playerManager.playCard(playerRef,card);
        queue.get(queueRef).setValueCard(card.getValue());
        queue.get(queueRef).setMaxMoveMotherNature(card.getMovement());
        this.playedCardListener.notifyPlayedCard(playerRef,String.valueOf(card));
        if(playerManager.checkIfCardsFinished(playerRef)) return true;

        return false;
    }

    public void inOrderForActionPhase(){
        Collections.sort(queue, new Comparator<Queue>() {   //mettere lmabda
            @Override
            public int compare(Queue q1, Queue q2) {
                return q1.compareTo(q2);
            }
        });

        this.queueListener.notifyResetQueue();
        for(Queue q:queue){
            this.queueListener.notifyQueue(q.getPlayerRef());
        }
    }

    public int readQueue(int queueRef){ return queue.get(queueRef).getPlayerRef(); }
    public int readMaxMotherNatureMovement(int queueRef){ return queue.get(queueRef).getMaxMoveMotherNature(); }

    private class Queue implements Comparable<Queue> {   //it is used both in the planning phase and in the action phase
        private int playerRef;
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

        @Override
        public int compareTo(Queue o) {
            return valueCard.compareTo(o.getValueCard());
        }
    }
}
