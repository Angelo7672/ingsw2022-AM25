package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ServerController;

import java.io.IOException;
import java.util.ArrayList;

    public class Server implements Entrance{
        private int numberOfPlayer;
        private boolean expertMode;
        private int currentUser;
        private ServerController controller;
        private Exit proxy;

        public Server(int port){
            this.proxy = new Proxy_s(port,this);
            proxy.start();
        }

        @Override
        public void startGame(){ controller = new Controller(numberOfPlayer, expertMode); }

        @Override
        public void userLogin(String nickname, String character, int playerRef){
            //metodo per andare a modificare nick-char in virtual view
        }

        private void planningPhase(){   //OPPURE NEL CONTROLLER?
            controller.refreshStudentsCloud();
            controller.queueForPlanificationPhase();
            for(int i = 0; i < numberOfPlayer; i++){
                proxy.goPlayCard(controller.readQueue(i),controller.getPlayedCardsInThisTurn());
                //TODO: aggiungere un locker
            }
        }

        @Override
        public void userPlayCard(int playerRef, String assistant){
            controller.playCard(playerRef,currentUser,assistant);
        }

        private void actionPhase(){ //OPPURE NEL CONTROLLER
            controller.inOrderForActionPhase();
            for(int i = 0; i < numberOfPlayer; i++){
                proxy.startActionPhase(controller.readQueue(i));
                //TODO: aggiungere un locker
            }
        }

        @Override
        public void userMoveStudent(int playerRef, int colour, boolean inSchool, int islandRef){
            controller.moveStudent(playerRef,colour,inSchool,islandRef);
        }

        @Override
        public void userMoveMotherNature(int desideredMovement){
            controller.moveMotherNature(currentUser,desideredMovement);
        }

        @Override
        public void userChooseCloud(int playerRef, int cloudRef){
            controller.chooseCloud(playerRef,cloudRef);
        }




        @Override
        public void setNumberOfPlayer(int numberOfPlayer) { this.numberOfPlayer = numberOfPlayer; }
        @Override
        public void setExpertMode(boolean expertMode) { this.expertMode = expertMode; }
        @Override
        public int getNumberOfPlayer() { return numberOfPlayer; }
        @Override
        public boolean isExpertMode() { return expertMode; }

        public static void main(String[] args) {
            Server server = new Server(2525);

        }
    }

