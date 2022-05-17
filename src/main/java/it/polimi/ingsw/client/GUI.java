package it.polimi.ingsw.client;

import javax.swing.*;
import java.awt.*;

public class GUI {

    //PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public GUI() {
        JFrame frame = new JFrame();
        ImageIcon gameIcon = new ImageIcon("src/main/resources/logo.png");

        frame.setSize(400, 400);
        frame.setTitle("Eryantis");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.green);
        frame.setVisible(true);
        frame.setIconImage(gameIcon.getImage());
    }

    /*
    private class GameBoard extends JPanel {

        public GameBoard(){
            JLabel schoolBoard = new JLabel();

        }
    }
    */

}
