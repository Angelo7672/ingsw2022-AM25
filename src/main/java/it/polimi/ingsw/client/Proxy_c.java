package it.polimi.ingsw.client;

import it.polimi.ingsw.client.message.*;
import it.polimi.ingsw.client.message.special.*;
import it.polimi.ingsw.listeners.DisconnectedListener;
import it.polimi.ingsw.listeners.ServerOfflineListener;
import it.polimi.ingsw.server.answer.*;
import it.polimi.ingsw.server.answer.viewmessage.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Proxy_c implements Exit {
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
    private DisconnectedListener disconnectedListener;
    private ServerOfflineListener serverOfflineListener;
    private boolean disconnected;
    private boolean initializedView;

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
        view = new View();
        disconnected=false;
    }

    public boolean readyForLogin() throws IOException {
        send(new GenericMessage("Ready for login!"));
        tempObj = receive();
        if(tempObj instanceof LoginRestoreAnswer) return true;
        return false;

    }

    public String first() throws IOException, ClassNotFoundException {
        send(new GenericMessage("Ready for login!"));
        tempObj = receive();
        if(tempObj instanceof SoldOutAnswer) return ((SoldOutAnswer) tempObj).getMessage();
        else if(tempObj instanceof SetupGameMessage) return "SetupGame";
        else if(tempObj instanceof SavedGameAnswer) return "SavedGame";
        else if (tempObj instanceof LoginRestoreAnswer) return "LoginRestore";
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

    public String getPhase() throws IOException {
        send(new GenericMessage("Ready to play!"));
        tempObj = receive();
        if(tempObj instanceof PlayCard){
            return ((PlayCard) tempObj).getMessage();
        }
        if(tempObj instanceof StartTurn){
            return ((StartTurn) tempObj).getMessage();
        }
        return null;
    }

    public boolean setupConnection(String nickname, String character) throws IOException {
        if(nickname.length()>10) nickname = nickname.substring(0,9);
        send(new SetupConnection(nickname, character));
        System.out.println("MESSAGE SENT: setupConnection "+nickname+" "+character);
        tempObj = receive();
        System.out.println("ANSWER: "+tempObj);
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage().equals("ok");
        else return false;
    }

    public boolean setupGame(int numberOfPlayers, String expertMode) throws IOException {
        boolean isExpert;

        if(numberOfPlayers<2 || numberOfPlayers >4) return false;
        if(expertMode.equalsIgnoreCase("y")) isExpert = true;
        else if(expertMode.equalsIgnoreCase("n")) isExpert = false;
        else return false;

        send(new SetupGame(numberOfPlayers, isExpert));
        System.out.println("MESSAGE SENT: setupGame"+numberOfPlayers+" "+expertMode);

        tempObj = receive();
        System.out.println("ANSWER: "+tempObj);
        if(!((GenericAnswer)tempObj).getMessage().equals("ok")) return false;
        send(new GenericMessage("Ready for login!"));
        System.out.println("MESSAGE SENT: Ready for login!");
        tempObj = receive();
        System.out.println("ANSWER: "+tempObj);
        return true;
    }

    public ArrayList<String> getChosenCharacters() {
        if(tempObj == null) {
            tempObj = receive();
        }
        LoginMessage msg = (LoginMessage) tempObj;
        tempObj = null;
        return (msg.getCharacterAlreadyChosen());
    }

    public View startView() throws IOException, InterruptedException {
        send(new GenericMessage("Ready to start"));
        synchronized (lock2){
            if(!view.isInitializedView()) lock2.wait();
        }
        return view;
    }

    public void setView(){
        synchronized (lock2){
            initializedView=true;
            lock2.notify();
        }

    }

    public boolean startPlanningPhase() throws IOException {
        send(new GenericMessage("Ready for Planning Phase"));
        while(true) {
            tempObj = receive();
            System.out.println("ANSWER: "+tempObj);
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
            send(new GenericMessage("Ready for Action Phase"));
            return ((GenericAnswer)tempObj).getMessage();
        }
        if(tempObj instanceof MoveNotAllowedAnswer){
            return ((MoveNotAllowedAnswer) tempObj).getMessage();
        }
        return "Error, try again";
    }

    public boolean startActionPhase() throws IOException, ClassNotFoundException {
        while(true) {
            tempObj = receive();
            if(((StartTurn)tempObj).getMessage().equals("Start your Action Phase!")){
                System.out.println("recived action");
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
        send(new UseSpecial(special));
        tempObj = receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer) tempObj).getMessage().equals("ok");
        return false;
    }

    public boolean useSpecial(int special,ArrayList<Integer> color1, ArrayList<Integer> color2) throws IOException, ClassNotFoundException {
        if(special == 7) send(new Special7Message(color1, color2));
        else if(special == 10) send(new Special10Message(color1, color2));
        tempObj = receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage().equals("ok");
        return false;
    }
    public boolean useSpecial(int special, int ref) throws IOException {
        if(special == 3) send(new Special3Message(ref));
        else if(special == 5) send(new Special5Message(ref));
        else if(special == 9) send(new Special9Message(ref));
        else if(special == 11) send(new Special11Message(ref));
        else if(special == 12) send(new Special12Message(ref));
        tempObj = receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage().equals("ok");
        return false;
    }

    public boolean useSpecial(int special, int playerRef, int ref) throws IOException {
        send(new Special1Message(playerRef, ref));
        tempObj = receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage().equals("ok");
        return false;
    }

    private void send(Message message) throws IOException {
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e){

        }
    }
    private Answer receive() {
        Answer tmp;
            synchronized (lock1){
                try {
                    if (answersList.size() == 0){
                        lock1.wait();
                    }
                }catch (InterruptedException e){
                }
            tmp = answersList.get(0);
            answersList.remove(0);
        }
        return tmp;
    }

    private void startReceive(){
        receive = new Thread(() -> {
            ArrayList<Answer> answersTmpList = new ArrayList<>();
            Answer tmp;
            try {
                this.socket.setSoTimeout(15000);
                while (!disconnected) {
                    tmp = (Answer) inputStream.readObject();
                    if (tmp instanceof PongAnswer) {
                        this.socket.setSoTimeout(15000);
                    } else if (tmp instanceof GameInfoAnswer) {
                        synchronized (lock2) {
                            view.initializedView(((GameInfoAnswer) tmp).getNumberOfPlayers(), ((GameInfoAnswer) tmp).isExpertMode());
                            lock2.notify();
                        }
                    } else if (tmp instanceof UserInfoAnswer) {
                        synchronized (lock2) {
                            if (!initializedView) {
                                lock2.wait();
                            }
                            view.setUserInfo(((UserInfoAnswer) tmp).getPlayerRef(), ((UserInfoAnswer) tmp).getCharacter(),((UserInfoAnswer) tmp).getNickname());
                        }
                    } else if (tmp instanceof LastCardMessage) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setLastCard(((LastCardMessage) tmp).getPlayerRef(), ((LastCardMessage) tmp).getCard());
                        }
                    } else if (tmp instanceof NumberOfCardsMessage) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setNumberOfCards(((NumberOfCardsMessage) tmp).getPlayerRef(), ((NumberOfCardsMessage) tmp).getNumberOfCards());
                        }
                    } else if (tmp instanceof HandAfterRestoreAnswer) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.restoreCards(((HandAfterRestoreAnswer) tmp).getHand());
                        }
                    } else if (tmp instanceof SchoolStudentMessage) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setSchoolStudents(((SchoolStudentMessage) tmp).getPlace(), ((SchoolStudentMessage) tmp).getComponentRef(), ((SchoolStudentMessage) tmp).getColor(), ((SchoolStudentMessage) tmp).getNewValue());
                        }
                    } else if (tmp instanceof ProfessorMessage) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setProfessors(((ProfessorMessage) tmp).getPlayerRef(), ((ProfessorMessage) tmp).getColor(), ((ProfessorMessage) tmp).isProfessor());
                        }
                    } else if (tmp instanceof SchoolTowersMessage) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setSchoolTowers(((SchoolTowersMessage) tmp).getPlayerRef(), ((SchoolTowersMessage) tmp).getTowers());
                        }
                    } else if (tmp instanceof CoinsMessage) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setCoins(((CoinsMessage) tmp).getPlayerRef(), ((CoinsMessage) tmp).getCoin());
                        }
                    } else if (tmp instanceof CloudStudentMessage) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setClouds(((CloudStudentMessage) tmp).getCloudRef(), ((CloudStudentMessage) tmp).getColor(), ((CloudStudentMessage) tmp).getNewValue());
                        }
                    } else if (tmp instanceof IslandStudentMessage) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setStudentsIsland(((IslandStudentMessage) tmp).getIslandRef(), ((IslandStudentMessage) tmp).getColor(), ((IslandStudentMessage) tmp).getNewValue());
                        }
                    } else if (tmp instanceof MotherPositionMessage) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setMotherPosition(((MotherPositionMessage) tmp).getMotherPosition());
                        }
                    } else if (tmp instanceof MaxMovementMotherNatureAnswer) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setMaxStepsMotherNature(((MaxMovementMotherNatureAnswer) tmp).getMaxMovement());
                        }
                    } else if (tmp instanceof IslandTowersNumberMessage) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setIslandTowers(((IslandTowersNumberMessage) tmp).getIslandRef(), ((IslandTowersNumberMessage) tmp).getTowersNumber());
                        }
                    } else if (tmp instanceof IslandTowersColorMessage) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setTowersColor(((IslandTowersColorMessage) tmp).getIslandRef(), ((IslandTowersColorMessage) tmp).getColor());
                        }
                    } else if (tmp instanceof InhibitedIslandMessage) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setInhibited(((InhibitedIslandMessage) tmp).getIslandRef(), ((InhibitedIslandMessage) tmp).getInhibited());
                        }
                    } else if (tmp instanceof UnifiedIsland) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.removeUnifiedIsland(((UnifiedIsland) tmp).getUnifiedIsland());
                        }
                    } else if (tmp instanceof UseSpecialAnswer) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setSpecialUsed(((UseSpecialAnswer) tmp).getSpecialIndex(), ((UseSpecialAnswer) tmp).getPlayerRef());
                        }
                    } else if (tmp instanceof SetSpecialAnswer) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setSpecial(((SetSpecialAnswer) tmp).getSpecialRef(), ((SetSpecialAnswer) tmp).getCost());
                        }
                    } else if (tmp instanceof InfoSpecial1or7or11Answer) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setSpecialStudents(((InfoSpecial1or7or11Answer) tmp).getStudentColor(), ((InfoSpecial1or7or11Answer) tmp).getValue(), ((InfoSpecial1or7or11Answer) tmp).getSpecialIndex());
                        }
                    } else if (tmp instanceof InfoSpecial5Answer) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setNoEntry(((InfoSpecial5Answer) tmp).getCards());
                        }
                    } else if (tmp instanceof DisconnectedAnswer) {
                        disconnected = true;
                        answersTmpList.clear();
                        disconnectedListener.notifyDisconnected();
                    } else if (tmp instanceof GameOverAnswer) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setWinner(((GameOverAnswer) tmp).getWinner());
                        }
                    }
                    else {
                        answersTmpList.add(tmp);
                    }
                    synchronized (lock1){
                        for(int i=0; i<answersTmpList.size(); i++) {
                            answersList.add(answersTmpList.get(i));
                            answersTmpList.remove(i);
                        }
                        if(answersList.size()!=0) lock1.notify();
                    }
                }
            } catch (SocketException e){
                try {
                    serverOfflineListener.notifyServerOffline();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } catch (IOException | ClassNotFoundException e) {
                try {
                    socket.close();
                    return;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receive.start();
    }

    private void startPing() {
        ping = new Thread(() -> {
        while (!disconnected) {
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

    @Override
    public void setDisconnectedListener(DisconnectedListener disconnectedListener) {
        this.disconnectedListener = disconnectedListener;
    }

    @Override
    public void setServerOfflineListener(ServerOfflineListener serverOfflineListener) throws IOException {
        this.serverOfflineListener = serverOfflineListener;
    }

}

