package it.polimi.ingsw.client.message.special;

import it.polimi.ingsw.client.message.Message;

import java.util.ArrayList;

/**
 * Special10Message contains all the chosen parameters to use it.
 */
public class Special10Message implements Message {
    private ArrayList<Integer> entranceStudent;
    private ArrayList<Integer> tableStudent;

    public Special10Message(ArrayList<Integer> entranceStudent, ArrayList<Integer> tableStudent) {
        this.entranceStudent = entranceStudent;
        this.tableStudent = tableStudent;
    }

    public ArrayList<Integer> getEntranceStudent() { return entranceStudent; }
    public ArrayList<Integer> getTableStudent() { return tableStudent; }
}
