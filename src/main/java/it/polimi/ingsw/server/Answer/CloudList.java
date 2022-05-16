package it.polimi.ingsw.server.Answer;

import java.util.ArrayList;

public class CloudList implements Answer{
    ArrayList<Integer> availableClouds;
    String Message;

    public CloudList(ArrayList<Integer> availableClouds){
        this.availableClouds = availableClouds;
    }

    public ArrayList<Integer> getAvailableClouds() { return availableClouds; }
    @Override
    public String getMessage() { return Message; }
}
