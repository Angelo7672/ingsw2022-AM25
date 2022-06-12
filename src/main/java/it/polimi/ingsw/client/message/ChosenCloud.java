package it.polimi.ingsw.client.Message;

public class ChosenCloud implements Message{
    private final int cloud;

    public ChosenCloud(int cloud) {
        this.cloud = cloud;
    }

    public int getCloud() {
        return cloud;
    }
}
