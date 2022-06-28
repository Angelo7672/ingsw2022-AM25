package it.polimi.ingsw.client;

import it.polimi.ingsw.client.message.*;
import it.polimi.ingsw.client.message.special.*;
import it.polimi.ingsw.listeners.DisconnectedListener;
import it.polimi.ingsw.listeners.ServerOfflineListener;
import it.polimi.ingsw.listeners.SoldOutListener;
import it.polimi.ingsw.server.answer.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Proxy_c implements Exit, ServerOfflineListener, DisconnectedListener, SoldOutListener {
    private Receiver receiver;

    private final ObjectOutputStream outputStream;
    private final Socket socket;
    private Answer tempObj;
    private View view;
    private Thread ping;
    private final Object lock2;
    private boolean disconnected;

    public Proxy_c(Socket socket) throws IOException{
        this.socket = socket;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        startPing();
        lock2 = new Object();
        view = new View();
        receiver = new Receiver(lock2, socket, view);
        setDisconnectedListener(this);
        setServerOfflineListener(this);
        setSoldOutListener(this);

    }

    public boolean readyForLogin() throws IOException {
        send(new GenericMessage("Ready for login!"));
        tempObj = receiver.receive();
        if(tempObj instanceof LoginRestoreAnswer) return true;
        return false;

    }

    @Override
    public void setDisconnectedListener(DisconnectedListener disconnectedListener) {
        receiver.setDisconnectedListener(disconnectedListener);
    }

    @Override
    public void setServerOfflineListener(ServerOfflineListener serverOfflineListener) {
        receiver.setServerOfflineListener(serverOfflineListener);
    }

    @Override
    public void setSoldOutListener(SoldOutListener soldOutListener) throws IOException {
        receiver.setSoldOutListener(soldOutListener);
    }

    public String first() throws IOException, ClassNotFoundException {
        send(new GenericMessage("Ready for login!"));
        tempObj = receiver.receive();
        if(tempObj instanceof SoldOutAnswer) return ((SoldOutAnswer) tempObj).getMessage();
        else if(tempObj instanceof SetupGameAnswer) return "SetupGame";
        else if(tempObj instanceof SavedGameAnswer) return "SavedGame";
        else if (tempObj instanceof LoginRestoreAnswer) return "LoginRestore";
        else return "Not first";
    }

    public Answer getMessage(){
        return tempObj;
    }

    public boolean savedGame(String decision) throws IOException {
        send(new GenericMessage(decision));
        tempObj = receiver.receive();
        if (tempObj instanceof GenericAnswer) {
            if(((GenericAnswer)tempObj).getMessage().equals("error"))
            return false;
        }
        return true;
    }

    public String getPhase() throws IOException {
        send(new GenericMessage("Ready to play!"));
        tempObj = receiver.receive();
        if(tempObj instanceof PlayCardAnswer){
            return ((PlayCardAnswer) tempObj).getMessage();
        }
        if(tempObj instanceof StartTurnAnswer){
            return ((StartTurnAnswer) tempObj).getMessage();
        }
        return null;
    }

    public boolean setupConnection(String nickname, String character) throws IOException {
        if(nickname.length()>10) nickname = nickname.substring(0,9);
        send(new SetupConnection(nickname, character));
        tempObj = receiver.receive();
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

        tempObj = receiver.receive();
        System.out.println("ANSWER: "+tempObj);
        if(!((GenericAnswer)tempObj).getMessage().equals("ok")) return false;
        send(new GenericMessage("Ready for login!"));
        System.out.println("MESSAGE SENT: Ready for login!");
        tempObj = receiver.receive();
        System.out.println("ANSWER: "+tempObj);
        return true;
    }

    public ArrayList<String> getChosenCharacters() {
        if(tempObj == null) {
            tempObj = receiver.receive();
        }
        LoginAnswer msg = (LoginAnswer) tempObj;
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
        receiver.setViewInitialized();
    }

    public boolean startPlanningPhase() throws IOException {
        send(new GenericMessage("Ready for Planning Phase"));
        while(true) {
            tempObj = receiver.receive();
            System.out.println("ANSWER: "+tempObj);
            if(((PlayCardAnswer)tempObj).getMessage().equals("Play card!")){
                return true;
            }
        }
    }

    public String playCard(String card) throws IOException, ClassNotFoundException {
        send(new CardMessage(card));
        tempObj = receiver.receive();
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
            tempObj = receiver.receive();
            if(((StartTurnAnswer)tempObj).getMessage().equals("Start your Action Phase!")){
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
        tempObj = receiver.receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage();
        if(tempObj instanceof MoveNotAllowedAnswer) return ((MoveNotAllowedAnswer) tempObj).getMessage();
        return "Error, try again";

    }

    public String moveMotherNature(int steps) throws IOException, ClassNotFoundException {
        send(new MoveMotherNature(steps));
        tempObj = receiver.receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage();
        if(tempObj instanceof MoveNotAllowedAnswer) return ((MoveNotAllowedAnswer) tempObj).getMessage();
        return "Error, try again";
    }

    public String chooseCloud(int cloud) throws IOException, ClassNotFoundException {
        send(new ChosenCloud(cloud));
        tempObj = receiver.receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage();
        if(tempObj instanceof MoveNotAllowedAnswer) return ((MoveNotAllowedAnswer) tempObj).getMessage();
        return "Error, try again";
    }

    public boolean checkSpecial(int special) throws IOException, ClassNotFoundException {
        send(new UseSpecial(special));
        tempObj = receiver.receive();
        System.out.println(tempObj);
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer) tempObj).getMessage().equals("ok");
        return false;
    }

    public boolean useSpecial(int special,ArrayList<Integer> color1, ArrayList<Integer> color2) throws IOException, ClassNotFoundException {
        if(special == 7) send(new Special7Message(color1, color2));
        else if(special == 10) send(new Special10Message(color1, color2));
        tempObj = receiver.receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage().equals("ok");
        return false;
    }
    public boolean useSpecial(int special, int ref) throws IOException {
        if(special == 3) send(new Special3Message(ref));
        else if(special == 5) send(new Special5Message(ref));
        else if(special == 9) send(new Special9Message(ref));
        else if(special == 11) send(new Special11Message(ref));
        else if(special == 12) send(new Special12Message(ref));
        tempObj = receiver.receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage().equals("ok");
        return false;
    }

    public boolean useSpecial(int special, int color, int islandRef) throws IOException {
        send(new Special1Message(color, islandRef));
        tempObj = receiver.receive();
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
    public void notifyDisconnected() throws IOException {
        disconnected = true;
        outputStream.close();
    }

    @Override
    public void notifyServerOffline() throws IOException {
        disconnected = true;
        outputStream.close();
    }

    @Override
    public void notifySoldOut() throws IOException {
        disconnected = true;
        outputStream.close();
    }
}

