package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ServerController;

import it.polimi.ingsw.controller.exception.EndGameException;
import it.polimi.ingsw.model.exception.NotAllowedException;

public class Server implements Entrance,ControllerServer{
        private ServerController controller;
        private Exit proxy;

        public Server(int port){
            this.proxy = new Proxy_s(port,this);
            proxy.start();
        }

        @Override
        public void startController(int numberOfPlayers, boolean expertMode){ controller = new Controller(numberOfPlayers,expertMode,this); }

        @Override
        public boolean userLogin(String nickname, String character, int playerRef){
            String[] characters = new String[]{"WIZARD","KING","WITCH","SAMURAI"};
            boolean checker = false;

            for(int i = 0; i < playerRef; i++)
                if(controller.userLoginNickname(nickname,i)) return false;
            for(int i = 0; i < 4 && !checker; i++)
                if(character.equals(characters[i])) checker = true;
            if(!checker) return false;
            for (int i = 0; i < playerRef; i++)
                if(controller.userLoginCharacter(character,i)) return false;
            controller.addNewPlayer(nickname,character);

            return true;
        }

        //Planning Phase
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

        //Action Phase
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
        public void resumeTurn(){ controller.resumeTurn(); }

        @Override
        public void studentsChangeInSchool(int color, String place, int componentRef, int newStudentsValue){ proxy.studentsChangeInSchool(color, place, componentRef, newStudentsValue); }
        @Override
        public void studentChangeOnIsland(int islandRef, int color, int newStudentsValue){ proxy.studentChangeOnIsland(islandRef, color, newStudentsValue); }
        @Override
        public void studentChangeOnCloud(int cloudRef, int color, int newStudentsValue){ proxy.studentChangeOnCloud(cloudRef, color, newStudentsValue); }
        @Override
        public void professorChangePropriety(int playerRef, int color, boolean newProfessorValue){ proxy.professorChangePropriety(playerRef, color, newProfessorValue);}
        @Override
        public void motherChangePosition(int newMotherPosition){ proxy.motherChangePosition(newMotherPosition); }
        @Override
        public void lastCardPlayedFromAPlayer(int playerRef, String assistantCard){ proxy.lastCardPlayedFromAPlayer(playerRef, assistantCard); }
        @Override
        public void numberOfCoinsChangeForAPlayer(int playerRef, int newCoinsValue){ proxy.numberOfCoinsChangeForAPlayer(playerRef, newCoinsValue); }
        @Override
        public void dimensionOfAnIslandIsChange(int islandToDelete){ proxy.dimensionOfAnIslandIsChange(islandToDelete); }
        @Override
        public void towersChangeInSchool(int playerRef, int towersNumber){ proxy.towersChangeInSchool(playerRef, towersNumber); }
        @Override
        public void towersChangeOnIsland(int islandRef, int towersNumber){ proxy.towersChangeOnIsland(islandRef, towersNumber); }
        @Override
        public void towerChangeColorOnIsland(int islandRef, int newColor){ proxy.towerChangeColorOnIsland(islandRef, newColor); }
        @Override
        public void islandInhibited(int islandRef, int isInhibited){ proxy.islandInhibited(islandRef, isInhibited); }
        @Override
        public void setSpecial(int specialRef){ proxy.setSpecial(specialRef); }

        @Override
        public void exitError(){
            System.exit(-1);
        }


        public static void main(String[] args) {
            Server server = new Server(2525);

        }
    }

