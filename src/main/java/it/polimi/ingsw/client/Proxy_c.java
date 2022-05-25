package it.polimi.ingsw.client;

import it.polimi.ingsw.client.Message.*;
import it.polimi.ingsw.server.Answer.*;
import it.polimi.ingsw.server.Answer.ViewMessage.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;


public class Proxy_c implements Entrance{
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final Socket socket;
    private Answer tempObj;
    private Thread ping;
    private final Exit cli;
    private View view;

    public Proxy_c(Socket socket, Exit cli) throws IOException {
        this.socket = socket;
        this.cli = cli;
    }

    public void setup() throws IOException, ClassNotFoundException {
        send(new GenericMessage("Ready for login!"));
        tempObj = receive();
        if(tempObj instanceof SetupGameMessage) cli.setupGame();
        else if(tempObj instanceof LoginMessage){
            view = new View(((LoginMessage) tempObj).getNumberOfPlayer(), ((LoginMessage) tempObj).isExpertMode());
            cli.view(view);
        }
    }

    public boolean setupConnection(String nickname, String character) throws IOException, ClassNotFoundException {
        if(!character.equalsIgnoreCase("WIZARD")&&!character.equalsIgnoreCase("KING")
            &&!character.equalsIgnoreCase("WITCH")&&!character.equalsIgnoreCase("SAMURAI")) return false;
        send(new SetupConnection(nickname, character));
        tempObj = receive();
        if(!tempObj.getMessage().equalsIgnoreCase("ok")) return false;
        System.out.println("SetupConnection done");
        return true;
    }

    public boolean setupGame(int numberOfPlayers, String expertMode) throws IOException, ClassNotFoundException {
        boolean isExpert;
        if(numberOfPlayers<2 || numberOfPlayers >4){
            return false;
        }
        if(expertMode.equalsIgnoreCase("y")) isExpert = true;
        else if(expertMode.equalsIgnoreCase("n")) isExpert = false;
        else {
            return false;
        }
        send(new SetupGame(numberOfPlayers, isExpert));
        tempObj = receive();
        if(!tempObj.getMessage().equals("ok")) return false;
        view = new View(numberOfPlayers, isExpert);
        cli.view(view);
        return true;

    }

    public boolean startPlanningPhase() throws ClassNotFoundException, IOException {
        send(new GenericMessage("Ready for Planning Phase"));
        while(true) {
            tempObj = receive();
            if(tempObj.getMessage().equals("Play card!")){
                return true;
            }
        }
    }

    public boolean playCard(String card) throws IOException, ClassNotFoundException {
        send(new CardMessage(card));
        tempObj = receive();
        return tempObj.getMessage().equals("ok");
    }

    public boolean startActionPhase() throws IOException, ClassNotFoundException {
        send(new GenericMessage("Ready for Action Phase"));
        while(true) {
            tempObj = receive();
            if(tempObj.getMessage().equals("Start your Action Phase!")){
                return true;
            }
        }
    }

    public boolean moveStudent(int color, String where, int islandRef) throws IOException, ClassNotFoundException {
        if(color < 0 || color > 4) {
            System.out.println("Error, insert a color");
            return false;
        }
        boolean inSchool;
        if (where.equalsIgnoreCase("school")) inSchool = true;
        else if(where.equalsIgnoreCase("island")) inSchool = false;
        else {
            System.out.println("Error, insert school or island");
            return false;
        }
        send(new MoveStudent(color, inSchool, islandRef));
        tempObj = receive();
        if(tempObj.getMessage().equals("transfer complete")) return true;
        if(tempObj.getMessage().equals("move not allowed")) System.out.println("Error, move not allowed");
        return false;
    }

    public boolean moveMotherNature(int steps) throws IOException, ClassNotFoundException {
        send(new MoveMotherNature(steps));
        tempObj = receive();
        if (tempObj.getMessage().equals("ok")) return true;
        System.out.println("Error, try again");
        return false;
    }

    public boolean chooseCloud(int cloud) throws IOException, ClassNotFoundException {
        send(new ChosenCloud(cloud));
        tempObj = receive();
        if(tempObj.getMessage().equals("ok")) return true;
        System.out.println("Error, try again");
        return false;
    }

    public boolean checkSpecial(int special) throws IOException, ClassNotFoundException {
        send(new CheckSpecial(special));
        tempObj = (SpecialMessage) receive();
        return tempObj.getMessage().equals("ok");
    }

    public boolean useSpecial(int special, int ref, ArrayList<Integer> color1, ArrayList<Integer> color2) throws IOException, ClassNotFoundException {
        send(new UseSpecial(special, ref, color1, color2));
        tempObj = receive();
        return tempObj.getMessage().equals("ok");
    }

    //send message to server
    public void send(Message message) throws IOException {
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e){

        }
    }
    public Answer receive() throws IOException, ClassNotFoundException {
        Answer tmp;
        inputStream = new ObjectInputStream(socket.getInputStream());
        while (true) {
            tmp = (Answer) inputStream.readObject();
            if(tmp instanceof NicknameMessage) {
                view.setNickname((NicknameMessage)tmp);
                cli.cli();
            }
            else if(tmp instanceof WizardMessage) {
                view.setWizard((WizardMessage)tmp);
                cli.cli();
            }
            else if(tmp instanceof LastCardMessage) {
                view.setLastCard((LastCardMessage)tmp);
                cli.cli();
            }
            else if(tmp instanceof  NumberOfCardsMessage) {
                view.setNumberOfCards((NumberOfCardsMessage)tmp);
                cli.cli();
            }
            else if(tmp instanceof SchoolStudentMessage) {
                view.setStudents((SchoolStudentMessage)tmp);
                cli.cli();
            }
            else if(tmp instanceof ProfessorMessage) {
                view.setProfessors((ProfessorMessage)tmp);
                cli.cli();
            }
            else if(tmp instanceof SchoolTowersMessage) {
                view.setSchoolTowers((SchoolTowersMessage)tmp);
                cli.cli();
            }
            else if(tmp instanceof CoinsMessage) {
                view.setCoins((CoinsMessage)tmp);
                cli.cli();
            }
            else if(tmp instanceof CloudStudentMessage) {
                view.setClouds((CloudStudentMessage)tmp);
                cli.cli();
            }
            else if(tmp instanceof IslandStudentMessage) {
                view.setStudentsIsland((IslandStudentMessage) tmp);
                cli.cli();
            }
            else if(tmp instanceof MotherPositionMessage) {
                view.setMotherPosition((MotherPositionMessage)tmp);
                cli.cli();
            }
            else if(tmp instanceof IslandTowersNumberMessage) {
                view.setIslandTowers((IslandTowersNumberMessage) tmp);
                cli.cli();
            }
            else if(tmp instanceof IslandTowersColorMessage) {
                view.setTowersColor((IslandTowersColorMessage)tmp);
                cli.cli();
            }
            else if(tmp instanceof InhibitedIslandMessage) {
                view.setInhibited((InhibitedIslandMessage)tmp);
                cli.cli();
            }
            else if(tmp instanceof UnifiedIsland) {
                view.removeUnifiedIsland((UnifiedIsland) tmp);
                cli.cli();
            }
            else break;
        }
        return tmp;
    }

    private void startPing(){
        ping = new Thread(() -> {

            int counter=0;
            while(true){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PingMessage message = new PingMessage("ping #"+counter);
                try {
                    send(message);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Thread.currentThread().interrupt();
                }
            }
        });
        ping.start();
    }

    private void stopPing(){
        ping.interrupt();
    }


}

