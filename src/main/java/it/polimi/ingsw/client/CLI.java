package it.polimi.ingsw.client;

import it.polimi.ingsw.constants.Constants;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class CLI implements Runnable{

    private Proxy_c proxy;
    private Scanner scanner;
    private boolean active;


    public CLI() {
        scanner = new Scanner(System.in);
        active = true;
    }

    public void setup() throws IOException, ClassNotFoundException {
        System.out.println(">Insert the server IP address: ");
        System.out.print(">");
        String address = scanner.nextLine();
        System.out.println(">Insert the server port: ");
        System.out.print(">");
        int port = scanner.nextInt();
        Constants.setAddress(address);
        Constants.setPort(port);
        proxy = new Proxy_c();
        if(!proxy.start()) {
            System.out.println("Some errors occurred, try again.");
            Client.main(null);
        }
        while (true){
            String nickname;
            do {
                System.out.println("Insert your nickname: ");
                nickname = scanner.nextLine();
            }while(nickname == null);
            if(proxy.setup(nickname)) break;
        }

    }

    public void setActive(boolean active){
        this.active = active;
    }

    public void loop(){
        scanner.reset();
    }

    @Override
    public void run() {
        try {
            setup();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        while (active){
            loop();
        }
        scanner.close();

    }
}
