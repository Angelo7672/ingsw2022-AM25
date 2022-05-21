package it.polimi.ingsw.client;

import it.polimi.ingsw.client.Message.*;
import it.polimi.ingsw.server.Answer.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;


public class Proxy_c implements Entrance{
    private final String address;
    private final int port;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket socket;
    private final InputChecker checker;
    private Answer tempObj;
    private Thread ping;
    private Exit cli;

    public Proxy_c() {
        this.address = "127.0.0.1";
        this.port = 2525;
        checker = new InputChecker();
    }

    public boolean start() throws IOException {
        try {
            socket = new Socket(address, port);
        } catch (SocketException e) {
            return false;
        }
        System.out.println("Connection established");
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        send(new GenericMessage("Ready for Login"));
        return true;
    }

    public boolean setupConnection(String nickname, String character) throws IOException, ClassNotFoundException {
        inputStream = new ObjectInputStream(socket.getInputStream());
        ConnectionMessage message = (ConnectionMessage) inputStream.readObject();
        if(message.getNumberOfPlayers()==0) cli.setupGame();
        if(!checker.checkSetupConnection((ConnectionMessage) tempObj, nickname, character)) return false;
        send(new SetupConnection(nickname, character));
        tempObj = (Answer) inputStream.readObject();
        if(!tempObj.getMessage().equalsIgnoreCase("ok")) return false;
        System.out.println("SetupConnection done");
        return true;
    }

    public boolean setupGame(int numberOfPlayers, String expertMode) throws IOException {
        boolean isExpert;
        if(numberOfPlayers<2 || numberOfPlayers >4){
            System.out.println("Player must be between 2 and 4");
            return false;
        }
        if(expertMode.equalsIgnoreCase("y")) isExpert = true;
        else if(expertMode.equalsIgnoreCase("n")) isExpert = false;
        else {
            System.out.println("error, insert y or n");
            return false;
        }
        send(new SetupGame(numberOfPlayers, isExpert));
        return true;
    }

    public boolean clientWait() throws ClassNotFoundException, IOException {
        send(new GenericMessage("Ready for play card"));
        while(true) {
            tempObj = (Answer) inputStream.readObject();
            if(tempObj instanceof CardsMessage){
                return true;
            }
        }
    }

    public boolean playCard(String card) throws IOException, ClassNotFoundException {
        if(!checker.checkCard((CardsMessage) tempObj, card)){
            return false;
        }
        send(new CardMessage(card));
        tempObj = (Answer) inputStream.readObject();
        return tempObj.getMessage().equals("ok");
    }

    public boolean startActionPhase() throws IOException, ClassNotFoundException {
        send(new GenericMessage("Ready for actionPhase"));
        while(true) {
            tempObj = (Answer) inputStream.readObject();
            if(tempObj.getMessage().equals("ok it's your turn")){
                return true;
            }
        }
    }

    public boolean moveStudent(int color, String where, int islandRef) throws IOException, ClassNotFoundException {
        if(color < 0 || color > 4) {
            System.out.println("Error, try again");
            return false;
        }
        boolean inSchool;
        if (where.equalsIgnoreCase("school")) inSchool = true;
        else if(where.equalsIgnoreCase("island")) inSchool = false;
        else {
            System.out.println("Error, try again");
            return false;
        }
        if (!checker.checkMoveStudent((SchoolMessage) inputStream.readObject(), color, inSchool)){
            System.out.println("Error, try again");
            return false;
        }
        send(new MoveStudent(color, inSchool, islandRef));
        tempObj = (Answer) inputStream.readObject();
        if(tempObj.getMessage().equals("transfer complete")) return true;
        if(tempObj.getMessage().equals("error")) System.out.println("Error, try again");
        return false;
    }

    public boolean moveMotherNature(int steps) throws IOException, ClassNotFoundException {
        /*tempObj = (Answer) inputStream.readObject();
        if(!checker.checkMoveMotherNature((MoveMotherNatureMessage) tempObj, steps)) return false;*/
        send(new MoveMotherNature(steps));
        tempObj = (Answer) inputStream.readObject();
        if (tempObj.getMessage().equals("ok")) return true;
        System.out.println("Error, try again");
        return false;
    }

    public boolean chooseCloud(int cloud) throws IOException, ClassNotFoundException {
        //if(!checker.checkCloud((StudentMessage) inputStream.readObject(), cloud)) return false;
        send(new ChosenCloud(cloud));
        tempObj = (Answer) inputStream.readObject();
        if(tempObj.getMessage().equals("ok")) return true;
        System.out.println("Error, try again");
        return false;
    }

    public boolean checkSpecial(int special) throws IOException, ClassNotFoundException {
        send(new CheckSpecial(special));
        tempObj = (SpecialMessage) inputStream.readObject();
        return tempObj.getMessage().equals("ok");
    }

    public boolean useSpecial(int special, int ref, ArrayList<Integer> color1, ArrayList<Integer> color2) throws IOException, ClassNotFoundException {
        if(!checker.checkSpecial(special, ref, color1, color2, (SpecialMessage) tempObj)) return false;
        send(new UseSpecial(special, ref, color1, color2));
        tempObj = (Answer) inputStream.readObject();
        return tempObj.getMessage().equals("ok");
    }

    //send message to the server
    public void send(Message message) throws IOException {
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e){

        }
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

    public void actionHandler() throws IOException, ClassNotFoundException {
        tempObj = (Answer) inputStream.readObject();
        if(tempObj instanceof CardsMessage) cli.phaseHandler("PlayCard");

    }

    private Answer inputMessage() throws IOException, ClassNotFoundException {
        tempObj = (Answer) inputStream.readObject();
        //while true
        //if answer instanceof viewUpdate chiama metodo view
        //else return answer

        return tempObj;
    }


}
