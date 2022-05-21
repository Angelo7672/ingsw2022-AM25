package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.model.exception.NotAllowedException;

    public class Server implements Entrance,ControllerServer{
        private int numberOfPlayers;
        private boolean expertMode;
        private ServerController controller;
        private Exit proxy;

        public Server(int port){
            this.proxy = new Proxy_s(port,this);
            proxy.start();
        }

        @Override
        public void startGame(){ controller = new Controller(numberOfPlayers,expertMode,this); }

        @Override
        public boolean userLogin(String nickname, String character, int playerRef){
            //metodo per andare a modificare nick-char in virtual view
            //mandare anche il team
        }

        @Override
        public void goPlayCard(int playerRef){ proxy.goPlayCard(playerRef); }
        @Override
        public void unlockPlanningPhase(int playerRef){ proxy.unlockPlanningPhase(playerRef); }
        @Override
        public boolean userPlayCard(int playerRef, String assistant){
            try {
                controller.playCard(playerRef,assistant);
            }catch (NotAllowedException exception){ return false;}

            return true;
        }

        @Override
        public void startActionPhase(int playerRef){ proxy.startActionPhase(playerRef); }
        @Override
        public void unlockActionPhase(int playerRef){ proxy.unlockActionPhase(playerRef); }
        @Override
        public boolean userMoveStudent(int playerRef, int colour, boolean inSchool, int islandRef){
            try {
                controller.moveStudent(playerRef, colour, inSchool, islandRef);
            }catch (NotAllowedException exception){ return false; }

            return true;
        }
        @Override
        public boolean userMoveMotherNature(int desiredMovement) throws EndGameException {
            try {
                controller.moveMotherNature(desiredMovement);
            } catch (NotAllowedException exception) {
                return false;
            } catch (EndGameException endGameException){
                throw new EndGameException();
            }

            return true;
        }
        @Override
        public boolean userChooseCloud(int playerRef, int cloudRef){
            try {
                controller.chooseCloud(playerRef,cloudRef);
            }catch (NotAllowedException exception){ return false; }

            return true;
        }




        @Override
        public void setNumberOfPlayer(int numberOfPlayer) { this.numberOfPlayers = numberOfPlayer; }
        @Override
        public void setExpertMode(boolean expertMode) { this.expertMode = expertMode; }
        @Override
        public int getNumberOfPlayer() { return numberOfPlayers; }
        @Override
        public boolean isExpertMode() { return expertMode; }

        public static void main(String[] args) {
            Server server = new Server(2525);

        }
    }

