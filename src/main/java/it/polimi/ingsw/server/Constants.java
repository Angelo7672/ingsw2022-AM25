package it.polimi.ingsw.server;

class Constants {
    private static String address;
    private static int port;
    private static boolean expertMode = false;
    private static int numberOfPlayers = 0;

    public static boolean isExpertMode() {
        return expertMode;
    }

    public static int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public static void setExpertMode(boolean expertMode) {
        Constants.expertMode = expertMode;
    }

    public static void setNumberOfPlayers(int numberOfPlayers) {
        Constants.numberOfPlayers = numberOfPlayers;
    }

    public static void setAddress(String address) {
        Constants.address = address;
    }

    public static void setPort(int port) {
        Constants.port = port;
    }

    public static String getAddress() {
        return address;
    }

    public static int getPort() {
        return port;
    }

}
