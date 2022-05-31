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


public class Proxy_c implements Exit{
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final Socket socket;
    private Answer tempObj;
    private View view;
    private Thread ping;

    public Proxy_c(Socket socket) throws IOException{
        this.socket = socket;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        startPing();
        socket.setSoTimeout(15000);
    }

    public String first() throws IOException, ClassNotFoundException {
        send(new GenericMessage("Ready for login!"));
        tempObj = receive();
        if(tempObj instanceof SoldOutAnswer) return ((SoldOutAnswer) tempObj).getMessage();
        else if(tempObj instanceof SetupGameMessage) return "SetupGame";
        else return "Not first";
    }

    public ArrayList<String> getChosenCharacters() throws IOException, ClassNotFoundException {
        if(tempObj == null) tempObj = receive();
        LoginMessage msg = (LoginMessage) tempObj;
        tempObj = null;
        return (msg.getCharacterAlreadyChosen());
    }

    public View startView() throws IOException, ClassNotFoundException {
        send(new GenericMessage("Ready to start"));
        tempObj = receive();
        return view;
    }

    public boolean setupConnection(String nickname, String character) throws IOException, ClassNotFoundException {
        send(new SetupConnection(nickname, character));
        tempObj = receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage().equals("ok");
        else return false;

    }

    public boolean setupGame(int numberOfPlayers, String expertMode) throws IOException, ClassNotFoundException {
        boolean isExpert;

        if(numberOfPlayers<2 || numberOfPlayers >4) return false;
        if(expertMode.equalsIgnoreCase("y")) isExpert = true;
        else if(expertMode.equalsIgnoreCase("n")) isExpert = false;
        else return false;

        send(new SetupGame(numberOfPlayers, isExpert));
        tempObj = receive();
        if(!((GenericAnswer)tempObj).getMessage().equals("ok")) return false;
        send(new GenericMessage("Ready for login!"));
        tempObj = receive();
        return true;
    }

    public boolean startPlanningPhase() throws ClassNotFoundException, IOException {
        send(new GenericMessage("Ready for Planning Phase"));
        while(true) {
            tempObj = receive();
            if(((PlayCard)tempObj).getMessage().equals("Play card!")){
                return true;
            }
        }
    }

    public String playCard(String card) throws IOException, ClassNotFoundException {
        send(new CardMessage(card));
        tempObj = receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage();
        if(tempObj instanceof MoveNotAllowedAnswer) return ((MoveNotAllowedAnswer) tempObj).getMessage();
        return "Error, try again";
    }

    public boolean startActionPhase() throws IOException, ClassNotFoundException {
        send(new GenericMessage("Ready for Action Phase"));
        while(true) {
            tempObj = receive();
            if(((StartTurn)tempObj).getMessage().equals("Start your Action Phase!")){
                return true;
            }
        }
    }

    public String moveStudent(int color, String where, int islandRef) throws IOException, ClassNotFoundException {
        if(color < 0 || color > 4) return "Error, insert a color";
        boolean inSchool;
        if (where.equalsIgnoreCase("school")) inSchool = true;
        else if(where.equalsIgnoreCase("island")) inSchool = false;
        else return "Error, insert school or island";
        send(new MoveStudent(color, inSchool, islandRef));
        tempObj = receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage();
        if(tempObj instanceof MoveNotAllowedAnswer) return ((MoveNotAllowedAnswer) tempObj).getMessage();
        return "Error, try again";

    }

    public String moveMotherNature(int steps) throws IOException, ClassNotFoundException {
        send(new MoveMotherNature(steps));
        tempObj = receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage();
        if(tempObj instanceof MoveNotAllowedAnswer) return ((MoveNotAllowedAnswer) tempObj).getMessage();
        return "Error, try again";
    }

    public String chooseCloud(int cloud) throws IOException, ClassNotFoundException {
        send(new ChosenCloud(cloud));
        tempObj = receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage();
        if(tempObj instanceof MoveNotAllowedAnswer) return ((MoveNotAllowedAnswer) tempObj).getMessage();
        return "Error, try again";
    }

    public boolean checkSpecial(int special) throws IOException, ClassNotFoundException {
        send(new CheckSpecial(special));
        tempObj = (SpecialMessage) receive();
        return ((SpecialMessage)tempObj).getMessage().equals("ok");
    }

    public boolean useSpecial(int special, int ref, ArrayList<Integer> color1, ArrayList<Integer> color2) throws IOException, ClassNotFoundException {
        send(new UseSpecial(special, ref, color1, color2));
        tempObj = receive();
        return ((GenericAnswer)tempObj).getMessage().equals("ok");
    }

    //send message to server
    public void send(Message message) throws IOException {
        //outputStream = new ObjectOutputStream(socket.getOutputStream());
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e){

        }
    }
    public Answer receive() throws IOException, ClassNotFoundException{
        Answer tmp;
        while (true) {
            tmp = (Answer) inputStream.readObject();
            if(tmp instanceof UserInfoAnswer) {view.setUserInfo((UserInfoAnswer)tmp);}
            else if(tmp instanceof LastCardMessage) {view.setLastCard((LastCardMessage)tmp);}
            else if(tmp instanceof  NumberOfCardsMessage) {view.setNumberOfCards((NumberOfCardsMessage)tmp);}
            else if(tmp instanceof SchoolStudentMessage) {view.setStudents((SchoolStudentMessage)tmp);}
            else if(tmp instanceof ProfessorMessage) {view.setProfessors((ProfessorMessage)tmp);}
            else if(tmp instanceof SchoolTowersMessage) {view.setSchoolTowers((SchoolTowersMessage)tmp);}
            else if(tmp instanceof CoinsMessage) {view.setCoins((CoinsMessage)tmp);}
            else if(tmp instanceof CloudStudentMessage) {view.setClouds((CloudStudentMessage)tmp);}
            else if(tmp instanceof IslandStudentMessage) {view.setStudentsIsland((IslandStudentMessage) tmp);}
            else if(tmp instanceof MotherPositionMessage) {view.setMotherPosition((MotherPositionMessage)tmp);}
            else if(tmp instanceof IslandTowersNumberMessage) {view.setIslandTowers((IslandTowersNumberMessage) tmp);}
            else if(tmp instanceof IslandTowersColorMessage) {view.setTowersColor((IslandTowersColorMessage)tmp);}
            else if(tmp instanceof InhibitedIslandMessage) {view.setInhibited((InhibitedIslandMessage)tmp);}
            else if(tmp instanceof UnifiedIsland) {view.removeUnifiedIsland((UnifiedIsland) tmp);}
            else if(tmp instanceof SetSpecialAnswer) {

            }
            else if(tmp instanceof GameInfoAnswer) {
                view = new View(((GameInfoAnswer) tmp).getNumberOfPlayers(),((GameInfoAnswer) tmp).isExpertMode());
                return null;
            }
            else if(tmp instanceof PongAnswer){socket.setSoTimeout(15000);}
            else if(tmp instanceof SoldOutAnswer){
                System.out.println("sold out");
            }
            else break;
        }
        return tmp;
    }

    private void startPing() {
        ping = new Thread(() -> {
        while (true) {
            try {
                Thread.sleep(5000);
                send(new PingMessage());
            } catch (IOException e) {
                System.err.println("io");
            } catch (InterruptedException e){

            }
        }
        });
        ping.start();
    }

    private void stopPing(){
        ping.interrupt();
    }

}

