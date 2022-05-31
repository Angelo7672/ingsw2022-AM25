package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import it.polimi.ingsw.client.Proxy_c;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.Socket;

//it's the class between the socket and the GUI, it manages the connection and the messages exchange
// and istantiates the controllers
public class GUIManager implements  Exit{
    Socket socket = new Socket();
    Proxy_c proxy;
    LoginSceneController loginSceneController;
    FXMLLoader currentScene;


    public GUIManager(Socket socket, String[] args) throws IOException {
        this.socket=socket;
        this.proxy=new Proxy_c(socket);
        GUI.main(args);

    }

    public void setupConnection(String nickname, String character) throws IOException, ClassNotFoundException {
        Boolean ok = proxy.setupConnection(nickname, character);
        System.out.println(ok);
    }

    /*
    @Override
    public void setupGame() {

    }

    @Override
    public void setupConnection(ArrayList<String> chosenCharacters) throws IOException, ClassNotFoundException {
        ArrayList<String> availableCharacters = new ArrayList<>();
        if(!chosenCharacters.contains("WIZARD")) availableCharacters.add("WIZARD");
        if(!chosenCharacters.contains("KING")) availableCharacters.add("KING");
        if(!chosenCharacters.contains("WITCH")) availableCharacters.add("WITCH");
        if(!chosenCharacters.contains("SAMURAI")) availableCharacters.add("SAMURAI");
        while (true) {
            String nickname;
            String character;
            do {

            } while (nickname == null);
            do {

                if (!availableCharacters.contains(character)) {
                    System.out.println("Error, choose an available character");
                    character=null;
                }
            } while (character == null);
            if (proxy.setupConnection(nickname, character)) break;
        }

    }


    @Override
    public void view(View view) {

    }

    @Override
    public void cli() {

    }*/
}
