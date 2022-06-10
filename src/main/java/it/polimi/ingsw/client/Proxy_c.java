package it.polimi.ingsw.client;

import it.polimi.ingsw.client.Message.*;
import it.polimi.ingsw.server.Answer.*;
import it.polimi.ingsw.server.Answer.ViewMessage.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class Proxy_c implements Exit{
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final Socket socket;
    private Answer tempObj;
    private View view;
    private Thread ping;
    private Thread receive;
    private ArrayList<Answer> answersList;
    private final Object lock1;
    private final Object lock2;
    private String winner;

    public Proxy_c(Socket socket) throws IOException{
        this.socket = socket;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        startPing();
        startReceive();
        socket.setSoTimeout(15000);
        answersList = new ArrayList<>();
        lock1 = new Object();
        lock2 = new Object();
        winner = null;
    }

    public String first() throws IOException, ClassNotFoundException {
        send(new GenericMessage("Ready for login!"));
        tempObj = receive();
        if(tempObj instanceof SoldOutAnswer) return ((SoldOutAnswer) tempObj).getMessage();
        else if(tempObj instanceof SetupGameMessage) return "SetupGame";
        else if(tempObj instanceof SavedGameAnswer) return "SavedGame";
        else return "Not first";
    }

    public Answer getMessage(){
        return tempObj;
    }

    public boolean savedGame(String decision) throws IOException {
        send(new GenericMessage(decision));
        tempObj = receive();
        if (tempObj instanceof GenericAnswer) {
            if(((GenericAnswer)tempObj).getMessage().equals("error"))
            return false;
        }
        return true;

    }

    public ArrayList<String> getChosenCharacters() throws IOException, ClassNotFoundException {
        if(tempObj == null) {
            tempObj = receive();
        }
        LoginMessage msg = (LoginMessage) tempObj;
        tempObj = null;
        return (msg.getCharacterAlreadyChosen());
    }

    public View startView() throws IOException, ClassNotFoundException, InterruptedException {
        send(new GenericMessage("Ready to start"));
        synchronized (lock2){
            if(view == null) lock2.wait();
        }
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
        tempObj = receive();;
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
        if(tempObj instanceof GenericAnswer) {
            view.setCards(card);
            return ((GenericAnswer)tempObj).getMessage();
        }
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
        tempObj = receive();
        return ((SpecialMessage)tempObj).getMessage().equals("ok");
    }

    public boolean useSpecial(int special, int ref, ArrayList<Integer> color1, ArrayList<Integer> color2) throws IOException, ClassNotFoundException {
        send(new UseSpecial(special, ref, color1, color2));
        tempObj = receive();
        return ((GenericAnswer)tempObj).getMessage().equals("ok");
    }

    //send message to server
    public void send(Message message) throws IOException {
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e){

        }
    }
    public Answer receive() throws IOException {
        Answer tmp;
            synchronized (lock1){
                try {
                    if (answersList.size() == 0) lock1.wait();
                }catch (InterruptedException e){
                }
            tmp = answersList.get(0);
            answersList.remove(0);
        }
        return tmp;
    }

    @Override
    public String getWinner(){
        return winner;
    }

    private void startReceive(){
        receive = new Thread(() -> {
            ArrayList<Answer> answersTmpList = new ArrayList<>();
            Answer tmp;
            while (true){
                try {
                    tmp = (Answer) inputStream.readObject();
                    if(tmp instanceof PongAnswer){
                        socket.setSoTimeout(15000);
                    }
                    else if(tmp instanceof GameInfoAnswer) {
                        synchronized (lock2){
                            view = new View(((GameInfoAnswer) tmp).getNumberOfPlayers(), ((GameInfoAnswer) tmp).isExpertMode());
                            lock2.notify();
                        }
                    }
                    else if(tmp instanceof UserInfoAnswer) {
                        synchronized (lock2) {
                            if (view == null) lock2.wait();
                            view.setUserInfo((UserInfoAnswer) tmp);
                        }
                    }
                    else if(tmp instanceof LastCardMessage) {
                        synchronized (lock2) {
                            if (view == null) lock2.wait();
                            view.setLastCard((LastCardMessage) tmp);
                        }
                    }
                    else if(tmp instanceof NumberOfCardsMessage) {
                        synchronized (lock2) {
                            if (view == null) lock2.wait();
                            view.setNumberOfCards((NumberOfCardsMessage) tmp);
                        }
                    }
                    else if(tmp instanceof HandAfterRestoreAnswer){
                        synchronized (lock2){
                            if (view == null) lock2.wait();
                            view.restoreCards(((HandAfterRestoreAnswer) tmp).getHand());
                        }
                    }
                    else if(tmp instanceof SchoolStudentMessage) {
                        synchronized (lock2) {
                            if (view == null) lock2.wait();
                            view.setSchoolStudents((SchoolStudentMessage) tmp);
                        }
                    }
                    else if(tmp instanceof ProfessorMessage) {
                        synchronized (lock2) {
                            if (view == null) lock2.wait();
                            view.setProfessors((ProfessorMessage) tmp);
                        }
                    }
                    else if(tmp instanceof SchoolTowersMessage) {
                        synchronized (lock2) {
                            if (view == null) lock2.wait();
                            view.setSchoolTowers((SchoolTowersMessage) tmp);
                        }
                    }
                    else if(tmp instanceof CoinsMessage) {
                        synchronized (lock2) {
                            if (view == null) lock2.wait();
                            view.setCoins((CoinsMessage) tmp);
                        }
                    }
                    else if(tmp instanceof CloudStudentMessage) {
                        synchronized (lock2) {
                            if (view == null) lock2.wait();
                            view.setClouds((CloudStudentMessage) tmp);
                        }
                    }
                    else if(tmp instanceof IslandStudentMessage) {
                        synchronized (lock2) {
                            if (view == null) lock2.wait();
                            view.setStudentsIsland((IslandStudentMessage) tmp);
                        }
                    }
                    else if(tmp instanceof MotherPositionMessage) {
                        synchronized (lock2) {
                            if (view == null) lock2.wait();
                            view.setMotherPosition((MotherPositionMessage) tmp);
                        }
                    }
                    else if(tmp instanceof IslandTowersNumberMessage) {
                        synchronized (lock2) {
                            if (view == null) lock2.wait();
                            view.setIslandTowers((IslandTowersNumberMessage) tmp);
                        }
                    }
                    else if(tmp instanceof IslandTowersColorMessage) {
                        synchronized (lock2) {
                            if (view == null) lock2.wait();
                            view.setTowersColor((IslandTowersColorMessage) tmp);
                        }
                    }
                    else if(tmp instanceof InhibitedIslandMessage) {
                        synchronized (lock2) {
                            if (view == null) lock2.wait();
                            view.setInhibited((InhibitedIslandMessage) tmp);
                        }
                    }
                    else if(tmp instanceof UnifiedIsland) {
                        synchronized (lock2) {
                            if (view == null) lock2.wait();
                            view.removeUnifiedIsland((UnifiedIsland) tmp);
                        }
                    }
                    else if(tmp instanceof SetSpecialAnswer) {

                    }
                    else if(tmp instanceof SoldOutAnswer){
                        System.err.println("sold out");
                    }
                    else if(tmp instanceof DisconnectedAnswer){
                        System.err.println("Client disconnected, game over.");
                        socket.close();
                    }
                    else if(tmp instanceof GameOverAnswer){
                        winner = ((GameOverAnswer) tmp).getWinner();
                    }
                    else {
                        answersTmpList.add(tmp);
                    }
                }catch (IOException | ClassNotFoundException e) {
                    try {
                        System.err.println("Client disconnected, game over.");
                        socket.close();
                        return;
                    } catch (IOException ex) {
                    ex.printStackTrace();
                    }
                } catch (InterruptedException e) {
                e.printStackTrace();
                }

                synchronized (lock1){
                        for(int i=0; i<answersTmpList.size(); i++) {
                            answersList.add(answersTmpList.get(i));
                            answersTmpList.remove(i);
                        }
                        if(answersList.size()!=0) lock1.notify();
                }
            }
        });
        receive.start();
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

