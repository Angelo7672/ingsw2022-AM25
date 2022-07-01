package it.polimi.ingsw.client;

import it.polimi.ingsw.client.message.*;
import it.polimi.ingsw.client.message.special.*;
import it.polimi.ingsw.listeners.DisconnectedListener;
import it.polimi.ingsw.listeners.PongListener;
import it.polimi.ingsw.listeners.ServerOfflineListener;
import it.polimi.ingsw.server.answer.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Proxy_s is client's proxy, it manages connections with server.
 */
public class Proxy_c implements Exit, PongListener, DisconnectedListener {
    private final Receiver receiver;

    private final ObjectOutputStream outputStream;
    private Answer tempObj;
    private final View view;
    private Thread ping;
    private final Object initializedViewLock;
    private boolean disconnected;
    private boolean clientDisconnected;
    private Integer pingCounter;
    private ServerOfflineListener serverOfflineListener;

    /**
     * Constructor initialized all the class variable and set some listener.
     * @param socket is the socket create in Client.
     * @throws IOException
     */
    public Proxy_c(Socket socket) throws IOException{
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        initializedViewLock = new Object();
        view = new View();
        pingCounter = 0;
        receiver = new Receiver(initializedViewLock, socket, view);
        receiver.start();
        receiver.setPongListeners(this);

    }

    /**
     * Send a message to server to comunicate that client is ready for login. Server answers with the action tha client have to do.
     * It depends on what the player number is.
     * @return String which corresponds to the actions.
     */
    public String first() {
        startPing();
        send(new GenericMessage("Ready for login!"));
        tempObj = receiver.receive();
        if(tempObj instanceof SoldOutAnswer) return ((SoldOutAnswer) tempObj).getMessage();
        else if(tempObj instanceof SetupGameAnswer) return "SetupGame";
        else if(tempObj instanceof SavedGameAnswer) return "SavedGame";
        else if (tempObj instanceof LoginRestoreAnswer) return "LoginRestore";
        else return "Not first";
    }

    /**
     * It sends to server number of player and expert mode.
     * @param numberOfPlayers is the number of players chosen.
     * @param expertMode Y for expert mode or N for normal game.
     * @return true if server accepts the message.
     */
    public boolean setupGame(int numberOfPlayers, String expertMode) {
        boolean isExpert;

        if(numberOfPlayers<2 || numberOfPlayers >4) return false;
        if(expertMode.equalsIgnoreCase("y")) isExpert = true;
        else if(expertMode.equalsIgnoreCase("n")) isExpert = false;
        else return false;

        send(new SetupGame(numberOfPlayers, isExpert));
        tempObj = receiver.receive();
        if(!((GenericAnswer)tempObj).getMessage().equals("ok")) return false;
        send(new GenericMessage("Ready for login!"));
        tempObj = receiver.receive();
        return true;
    }

    /**
     * It sends to the server nickname and character chosen by the player.
     * @param nickname is the nickname chosen.
     * @param character is the characters chosen.
     * @return true if server accepts nickname and characters.
     */
    public boolean setupConnection(String nickname, String character) {
        if(nickname.length()>10) nickname = nickname.substring(0,9);
        send(new SetupConnection(nickname, character));
        tempObj = receiver.receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage().equals("ok");
        else return false;
    }

    /**
     * It used to get characters already chosen.
     * @return the ArrayList of characters already chosen.
     */
    public ArrayList<String> getChosenCharacters() {
        if(tempObj == null) {
            tempObj = receiver.receive();
        }
        LoginAnswer msg = (LoginAnswer) tempObj;
        tempObj = null;
        return (msg.getCharacterAlreadyChosen());
    }

    /**
     * It sends the decision to the server.
     * @param decision is the decision of the player.
     * @return true if server accepted the message.
     */
    public boolean savedGame(String decision) {
        send(new GenericMessage(decision));
        tempObj = receiver.receive();
        if (tempObj instanceof GenericAnswer) {
            return !((GenericAnswer) tempObj).getMessage().equals("error");
        }
        return true;
    }

    /**
     * Send a message to server to comunicate that client is ready for login.
     * @return true if server is ready for client to login.
     */
    public boolean readyForLogin() {
        send(new GenericMessage("Ready for login!"));
        tempObj = receiver.receive();
        return tempObj instanceof LoginRestoreAnswer;
    }

    /**
     * @return last message that proxy_c received.
     */
    public Answer getMessage(){
        return tempObj;
    }

    /**
     * It is used in the case of restore a game. It returns the phase of the game that have to be done.
     * @return the string inside the answer received.
     */
    public String getPhase() {
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

    /**
     * It is used to receive number of players and expertMode. Then, when view is initialized, return view.
     * @return view initialized.
     */
    public View startView() {
        send(new GenericMessage("Ready to start"));
        synchronized (initializedViewLock){
            try {
                if (!view.isInitializedView()) initializedViewLock.wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return view;
    }

    /**
     * It tells to the receiver that listeners are set and cli/gui is ready to received notify.
     */
    public void setView(){
        receiver.setViewInitialized();
    }

    /**
     * It sends a message to server to start planning phase. Server answer when it's player's turn.
     * @return true if it's player's turn.
     */
    public boolean startPlanningPhase() {
        send(new GenericMessage("Ready for Planning Phase"));
        while(true) {
            tempObj = receiver.receive();
            if(((PlayCardAnswer)tempObj).getMessage().equals("Play card!")){
                return true;
            }
        }
    }

    /**
     * Send the chosen card to server and wait its answer.
     * @param card is the card chosen.
     * @return true if card is accepted by server.
     */
    public String playCard(String card) {
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

    /**
     * It sends a message to server to start action phase. Server answer when it's player's turn.
     * @return true if it's player's turn.
     */
    public boolean startActionPhase() {
        while(true) {
            tempObj = receiver.receive();
            if(((StartTurnAnswer)tempObj).getMessage().equals("Start your Action Phase!")){
                return true;
            }
        }
    }

    /**
     * Send the chosen student to server and wait its answer.
     * @param color is the color of the chosen student.
     * @param where is the place where move the student (school or island).
     * @param islandRef is the number of island if the student is moved on an island.
     * @return true if student is accepted by server.
     */
    public String moveStudent(int color, String where, int islandRef) {
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

    /**
     * Send the chosen number of steps to server and wait its answer.
     * @param steps is the number of steps chosen.
     * @return true if steps is accepted by server.
     */
    public String moveMotherNature(int steps) {
        send(new MoveMotherNature(steps));
        tempObj = receiver.receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage();
        if(tempObj instanceof MoveNotAllowedAnswer) return ((MoveNotAllowedAnswer) tempObj).getMessage();
        return "Error, try again";
    }

    /**
     * Send the chosen cloud of steps to server and wait its answer.
     * @param cloud is the number of the cloud.
     * @return true if cloud is accepted by server.
     */
    public String chooseCloud(int cloud) {
        send(new ChosenCloud(cloud));
        tempObj = receiver.receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage();
        if(tempObj instanceof MoveNotAllowedAnswer) return ((MoveNotAllowedAnswer) tempObj).getMessage();
        return "Error, try again";
    }

    /**
     * Send the chosen special to server and wait its answer.
     * @param special is the special chosen.
     * @return true if special is accepted by server.
     */
    public boolean checkSpecial(int special) {
        send(new UseSpecial(special));
        tempObj = receiver.receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer) tempObj).getMessage().equals("ok");
        return false;
    }

    /**
     * It's used by special 7 or 10. It sends a personalized message to server and wait for its answer.
     * @param special is the number of the special.
     * @param color1 is the first ArrayList.
     * @param color2 is the second ArrayList.
     * @return true if ArrayList of students are accepted by server.
     */
    public boolean useSpecial(int special,ArrayList<Integer> color1, ArrayList<Integer> color2) {
        if(special == 7) send(new Special7Message(color1, color2));
        else if(special == 10) send(new Special10Message(color1, color2));
        tempObj = receiver.receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage().equals("ok");
        return false;
    }

    /**
     * It's used by special 3, 5, 9, 11 or 12. It sends a personalized message to server and wait for its answer.
     * @param special is the number of the special.
     * @param ref is the reference of the decision made by player.
     * @return true if ref is accepted by server.
     */
    public boolean useSpecial(int special, int ref) {
        if(special == 3) send(new Special3Message(ref));
        else if(special == 5) send(new Special5Message(ref));
        else if(special == 9) send(new Special9Message(ref));
        else if(special == 11) send(new Special11Message(ref));
        else if(special == 12) send(new Special12Message(ref));
        tempObj = receiver.receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage().equals("ok");
        return false;
    }

    /**
     * It's used by special 1. It sends a personalized message to server and wait for its answer.
     * @param color is the color of the student chosen.
     * @param islandRef is the number of the island chosen.
     * @return true if special is accepted by server.
     */
    public boolean useSpecial(int special, int color, int islandRef) {
        send(new Special1Message(color, islandRef));
        tempObj = receiver.receive();
        if(tempObj instanceof GenericAnswer) return ((GenericAnswer)tempObj).getMessage().equals("ok");
        return false;
    }

    /**
     * It sends message to server.
     * @param message is the message which have to be sent.
     */
    private void send(Message message) {
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e){
            if(!clientDisconnected) serverOfflineListener.notifyServerOffline();
        }
    }

    /**
     * It's a thread which sends ping message during the connection. If ping counter equals 3 it means that server doesn't send pong for 15 sec, so the game is closed.
     */
    private void startPing() {
        ping = new Thread(() -> {
        while (!disconnected) {
            try {
                Thread.sleep(5000);
                //System.out.println("ping");
                pingCounter++;
                if(pingCounter == 3) {
                    disconnected = true;
                    outputStream.close();
                    serverOfflineListener.notifyServerOffline();
                }
                send(new PingMessage());
            } catch (IOException e) {
                if(!clientDisconnected) serverOfflineListener.notifyServerOffline();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        });
        ping.start();
    }

    @Override
    public void setDisconnectedListener(DisconnectedListener disconnectedListener) {
        receiver.setDisconnectedListener(disconnectedListener);
    }

    @Override
    public void setServerOfflineListener(ServerOfflineListener serverOfflineListener) {
        receiver.setServerOfflineListener(serverOfflineListener);
        this.serverOfflineListener = serverOfflineListener;
    }

    @Override
    public void notifyPong() {
        pingCounter=0;
    }

    @Override
    public void notifyDisconnected() {
        clientDisconnected = true;
    }
}

