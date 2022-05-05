package it.polimi.ingsw.client;

import it.polimi.ingsw.client.Message.GenericMessage;
import it.polimi.ingsw.client.Message.Message;
import it.polimi.ingsw.client.Message.SetupConnection;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.server.Answer.ConnectionMessage;
import it.polimi.ingsw.server.Answer.SetupGameMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Proxy_c implements Exit{
    private final String address;
    private final int port;
    private ObjectOutputStream outputStream;
    private Socket socket;

    public Proxy_c() {
        this.address = Constants.getAddress();
        this.port = Constants.getPort();
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

    public boolean setup(String nickname) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream =new ObjectInputStream(socket.getInputStream());
        if(inputStream.readObject() instanceof SetupGameMessage){
            while(true) {
                if(setupGame()) break;
            }
            inputStream =new ObjectInputStream(socket.getInputStream());
        }
        ConnectionMessage message = (ConnectionMessage) inputStream.readObject();
        Scanner scanner = new Scanner(System.in);
        String character;
        do {
            System.out.println("Remaining character "+message.getRemainingCharacter()+" pick one.");
            character = scanner.nextLine();
        } while(character==null);
        if(checkSetupConnection((ConnectionMessage) inputStream.readObject(), nickname, character)){
            send(new SetupConnection(nickname, character));
            return true;
        }
        return false;
    }

    public boolean checkSetupConnection(ConnectionMessage message, String nickname, String character){
        if(message.getRemainingCharacter().contains(character)) return false;
        if(!message.getChosenNickname().contains(nickname)) return false;
        return true;
    }

    public boolean setupGame(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many players)");
        try {
            int numberOfPlayers = scanner.nextInt();
        System.out.println("Expert mode? [y/n]");
        String expertMode = scanner.nextLine();
        Constants.setNumberOfPlayers(numberOfPlayers);
        if(expertMode.toLowerCase().equals("y")) Constants.setExpertMode(true);
        else if(expertMode.toLowerCase().equals("n")) Constants.setExpertMode((false));
        else return false;
        }catch (InputMismatchException e){
            return false;
        }
        return true;
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


}

