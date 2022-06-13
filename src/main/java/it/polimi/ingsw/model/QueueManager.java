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
        firstInQueue = rnd.nextInt(numberOfPlayer);
        Queue first = new Queue(firstInQueue,-1,-1);
        queue.add(first);
    }

    private String toString(Assistant assistant){
        if(assistant.equals(Assistant.LION)) return "LION";
        else if(assistant.equals(Assistant.GOOSE)) return "GOOSE";
        else if(assistant.equals(Assistant.CAT)) return "CAT";
        else if(assistant.equals(Assistant.EAGLE)) return "EAGLE";
        else if(assistant.equals(Assistant.FOX)) return "FOX";
        else if(assistant.equals(Assistant.LIZARD)) return "LIZARD";
        else if(assistant.equals(Assistant.OCTOPUS)) return "OCTOPUS";
        else if(assistant.equals(Assistant.DOG)) return "DOG";
        else if(assistant.equals(Assistant.ELEPHANT)) return "ELEPHANT";
        else if(assistant.equals(Assistant.TURTLE)) return "TURTLE";
        return "NONE";
    }

    public void queueRestore(ArrayList<Integer> playerRef, ArrayList<Integer> valueCard, ArrayList<Integer> maxMoveMotherNature){
        queue.remove(0);
        for(int i = 0; i < numberOfPlayer; i++)
            queue.add(new Queue(playerRef.get(i),valueCard.get(i),maxMoveMotherNature.get(i)));
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

        for(int i = 0; i < numberOfPlayer; i++){
            this.queueListener.notifyQueue(i, queue.get(i).getPlayerRef());
            this.queueListener.notifyValueCard(i, queue.get(i).getValueCard());
            this.queueListener.notifyMaxMove(i, queue.get(i).getMaxMoveMotherNature());
        }

    }

    public boolean playCard(int playerRef, int queueRef, Assistant card, ArrayList<Assistant> alreadyPlayedAssistant) throws NotAllowedException {
        playerManager.playCard(playerRef,card,alreadyPlayedAssistant);
        queue.get(queueRef).setValueCard(card.getValue());
        queue.get(queueRef).setMaxMoveMotherNature(card.getMovement());
        this.playedCardListener.notifyPlayedCard(playerRef, toString(card));

        if(playerManager.checkIfCardsFinished(playerRef)) return true;  //game will finish at the end of the turn

        return false;
    }

    public void inOrderForActionPhase(){
        Collections.sort(queue, Queue::compareTo);

        for(int i=0; i<numberOfPlayer; i++){
            this.queueListener.notifyQueue(i, queue.get(i).getPlayerRef());
            this.queueListener.notifyValueCard(i, queue.get(i).getValueCard());
            this.queueListener.notifyMaxMove(i, queue.get(i).getMaxMoveMotherNature());
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
