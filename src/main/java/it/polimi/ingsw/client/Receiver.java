package it.polimi.ingsw.client;

import it.polimi.ingsw.listeners.DisconnectedListener;
import it.polimi.ingsw.listeners.PongListener;
import it.polimi.ingsw.listeners.ServerOfflineListener;
import it.polimi.ingsw.server.answer.*;
import it.polimi.ingsw.server.answer.viewAnswer.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * It reads every message in input from the server and sort it.
 */
public class Receiver extends Thread {

    private Thread viewThread;
    private final Object answerTmpLock; //lock the ArrayList answerList when it is empty
    private final Object initializedViewLock;
    private final Object answerViewLock; //lock the ArrayList answerView when it is empty
    private final Object specialLock; //lock the update about specials when special list is not complete
    private final Object setViewLock;
    private final ObjectInputStream inputStream;
    private final ArrayList<Answer> answersList;
    private final Socket socket;
    private DisconnectedListener disconnectedListener;
    private ServerOfflineListener serverOfflineListener;
    private PongListener pongListener;
    private boolean disconnected;
    private final View view;
    private boolean initializedView;
    private final ArrayList<Answer> answerView;
    private boolean lockNotify;

    /**
     * Constructor allocates every variable of the class.
     * @param initializedViewLock Is the lock used to notify to proxy_c when view is initialized.
     * @throws IOException
     */
    public Receiver(Object initializedViewLock, Socket socket, View view) throws IOException {
        this.socket = socket;
        this.inputStream = new ObjectInputStream(this.socket.getInputStream());
        //this.socket.setSoTimeout(15000);
        this.view = view;
        answersList = new ArrayList<>();
        answerView = new ArrayList<>();
        answerTmpLock = new Object();
        setViewLock = new Object();
        this.initializedViewLock = initializedViewLock;
        answerViewLock = new Object();
        specialLock = new Object();
        disconnected = false;
    }

    /**
     * When view is initialized notify it to proxy_c.
     */
    public void setViewInitialized(){
        synchronized (setViewLock) {
            initializedView = true;
            setViewLock.notifyAll();
        }
    }

    /**
     * It conserves all the answer of the server until proxy_c need them.
     * @return the first Answer of the ArrayList.
     */
    public Answer receive() {
        Answer tmp;
        synchronized (answerTmpLock){
            try {
                if (answersList.size() == 0){
                    answerTmpLock.wait();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            tmp = answersList.get(0);
            answersList.remove(0);
        }
        return tmp;
    }


    /**
     * For each type of answer the method call view method to set schools.
     * @param tmp is a view answer received from server
     */
    private void viewSchoolMessage(Answer tmp){
        if (tmp instanceof SchoolStudentAnswer) {
            view.setSchoolStudents(((SchoolStudentAnswer) tmp).getPlace(), ((SchoolStudentAnswer) tmp).getComponentRef(), ((SchoolStudentAnswer) tmp).getColor(), ((SchoolStudentAnswer) tmp).getNewValue());
        }
        else if (tmp instanceof ProfessorAnswer) {
            view.setProfessors(((ProfessorAnswer) tmp).getPlayerRef(), ((ProfessorAnswer) tmp).getColor(), ((ProfessorAnswer) tmp).isProfessor());
        } else if (tmp instanceof SchoolTowersAnswer) {
            view.setSchoolTowers(((SchoolTowersAnswer) tmp).getPlayerRef(), ((SchoolTowersAnswer) tmp).getTowers());
        } else if (tmp instanceof CoinsAnswer) {
            view.setCoins(((CoinsAnswer) tmp).getPlayerRef(), ((CoinsAnswer) tmp).getCoin());
        }
    }

    /**
     * For each type of answer the method call view method to set islands.
     * @param tmp is a view answer received from server
     */
    private void viewIslandMessage(Answer tmp){
        if (tmp instanceof MotherPositionAnswer) {
            view.setMotherPosition(((MotherPositionAnswer) tmp).getMotherPosition());
        } else if (tmp instanceof MaxMovementMotherNatureAnswer) {
            view.setMaxStepsMotherNature(((MaxMovementMotherNatureAnswer) tmp).getMaxMovement());
        } else if (tmp instanceof IslandTowersNumberAnswer) {
            view.setIslandTowers(((IslandTowersNumberAnswer) tmp).getIslandRef(), ((IslandTowersNumberAnswer) tmp).getTowersNumber());
        } else if (tmp instanceof IslandTowersColorAnswer) {
            view.setTowersColor(((IslandTowersColorAnswer) tmp).getIslandRef(), ((IslandTowersColorAnswer) tmp).getColor());
        }
        else if (tmp instanceof UnifiedIslandAnswer) {
            view.removeUnifiedIsland(((UnifiedIslandAnswer) tmp).getUnifiedIsland());
        } else if (tmp instanceof InhibitedIslandAnswer) {
            view.setInhibited(((InhibitedIslandAnswer) tmp).getIslandRef(), ((InhibitedIslandAnswer) tmp).getInhibited());
        } else if (tmp instanceof IslandStudentAnswer) {
            view.setStudentsIsland(((IslandStudentAnswer) tmp).getIslandRef(), ((IslandStudentAnswer) tmp).getColor(), ((IslandStudentAnswer) tmp).getNewValue());
        }
    }

    /**
     * For each type of answer the method call view method to set cards.
     * @param tmp is a view answer received from server
     */
    private void viewCardsMessage(Answer tmp) {
        if (tmp instanceof LastCardAnswer) {
            view.setLastCard(((LastCardAnswer) tmp).getPlayerRef(), ((LastCardAnswer) tmp).getCard());
        } else if (tmp instanceof HandAfterRestoreAnswer) {
            view.restoreCards(((HandAfterRestoreAnswer) tmp).getHand());
        }
    }

    /**
     * For each type of answer the method call view method to set clouds.
     * @param tmp is a view answer received from server
     */
    private void viewCloudMessage(Answer tmp){
        view.setClouds(((CloudStudentAnswer) tmp).getCloudRef(), ((CloudStudentAnswer) tmp).getColor(), ((CloudStudentAnswer) tmp).getNewValue());
    }

    /**
     * For each type of answer the method call view method to set specials.
     * @param tmp is a view answer received from server
     */
    private void viewSpecialMessage(Answer tmp){
        if (tmp instanceof UseSpecialAnswer) {
            view.setSpecialUsed(((UseSpecialAnswer) tmp).getSpecialIndex(), ((UseSpecialAnswer) tmp).getPlayerRef());
        } else if (tmp instanceof SetSpecialAnswer) {
            view.setSpecial(((SetSpecialAnswer) tmp).getSpecialRef(), ((SetSpecialAnswer) tmp).getCost());
            synchronized (specialLock) {
                if(view.specialSet()) specialLock.notifyAll();
            }
        } else if (tmp instanceof InfoSpecial1or7or11Answer) {
            if (!view.specialSet()) {
                    synchronized (specialLock) {
                        try {
                            specialLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            }
            view.setSpecialStudents(((InfoSpecial1or7or11Answer) tmp).getStudentColor(), ((InfoSpecial1or7or11Answer) tmp).getValue(), ((InfoSpecial1or7or11Answer) tmp).getSpecialIndex());
        } else if (tmp instanceof InfoSpecial5Answer) {
            if (!view.specialSet()) {
                synchronized (specialLock) {
                    try {
                        specialLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            view.setNoEntry(((InfoSpecial5Answer) tmp).getCards());
        }
    }

    /**
     * For each type of answer the method call view method to set game info.
     * @param tmp is a view answer received from server
     */
    private void gameMessage(Answer tmp){
        if (tmp instanceof GameInfoAnswer) {
            synchronized (initializedViewLock) {
                view.initializedView(((GameInfoAnswer) tmp).getNumberOfPlayers(), ((GameInfoAnswer) tmp).isExpertMode());
                initializedViewLock.notifyAll();
            }
        }
        else if (tmp instanceof UserInfoAnswer) {
            view.setUserInfo(((UserInfoAnswer) tmp).getPlayerRef(), ((UserInfoAnswer) tmp).getCharacter(), ((UserInfoAnswer) tmp).getNickname());
        }
    }


    /**
     * it's used at the beginning of the game, when view could be not initialized yet. So thread wait till view is initialized and get answer from ArrayList answerView.
     */
    private void viewMessage(){
        viewThread = new Thread(() -> {
            Answer tmp;
            while(!disconnected) {
                try {
                    synchronized (setViewLock) {
                        if (!initializedView) setViewLock.wait();
                    }
                    while(answerView.size() != 0) {
                        tmp = answerView.get(0);
                        if (tmp instanceof UserInfoAnswer) gameMessage(tmp);
                        else if (tmp instanceof LastCardAnswer) viewCardsMessage(tmp);
                        else if (tmp instanceof HandAfterRestoreAnswer) viewCardsMessage(tmp);
                        else if (tmp instanceof SchoolStudentAnswer) viewSchoolMessage(tmp);
                        else if (tmp instanceof ProfessorAnswer) viewSchoolMessage(tmp);
                        else if (tmp instanceof SchoolTowersAnswer) viewSchoolMessage(tmp);
                        else if (tmp instanceof CoinsAnswer) viewSchoolMessage(tmp);
                        else if (tmp instanceof CloudStudentAnswer) viewCloudMessage(tmp);
                        else if (tmp instanceof IslandStudentAnswer) viewIslandMessage(tmp);
                        else if (tmp instanceof MotherPositionAnswer) viewIslandMessage(tmp);
                        else if (tmp instanceof MaxMovementMotherNatureAnswer) viewIslandMessage(tmp);
                        else if (tmp instanceof IslandTowersNumberAnswer) viewIslandMessage(tmp);
                        else if (tmp instanceof IslandTowersColorAnswer) viewIslandMessage(tmp);
                        else if (tmp instanceof UnifiedIslandAnswer) viewIslandMessage(tmp);
                        else if (tmp instanceof InhibitedIslandAnswer) viewIslandMessage(tmp);
                        else if (tmp instanceof UseSpecialAnswer) viewSpecialMessage(tmp);
                        else if (tmp instanceof SetSpecialAnswer) viewSpecialMessage(tmp);
                        else if (tmp instanceof InfoSpecial1or7or11Answer) viewSpecialMessage(tmp);
                        else if (tmp instanceof InfoSpecial5Answer) viewSpecialMessage(tmp);
                        answerView.remove(0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        viewThread.start();
    }

    /**
     * It put the answer in the ArrayList answerView
     * @param tmp is the answer arrived
     */
    private void viewNotInitialized(Answer tmp){
        synchronized (answerViewLock) {
            answerView.add(tmp);
            if(lockNotify) answerViewLock.notify();
        }
    }


    /**
     * It received every message and it sorts it. If view is not initialized call viewNotInitialized for each view message which arrives.
     * If the answer is about the game the method add it in answersTmpList.
     * If game is over or a client is disconnected, the method notify it.
     */
    public void run(){
        viewMessage();
        try {
            ArrayList<Answer> answersTmpList = new ArrayList<>();
            Answer tmp;
            socket.setSoTimeout(15000);
            while (!disconnected) {
                tmp = (Answer) inputStream.readObject();
                //socket.setSoTimeout(15000);
                if (tmp instanceof PongAnswer) {
                    //socket.setSoTimeout(15000);
                    pongListener.notifyPong();
                }
                else if (tmp instanceof GameInfoAnswer) gameMessage(tmp);
                else if (tmp instanceof UserInfoAnswer) {
                    if(!initializedView) answerView.add(tmp);
                    else gameMessage(tmp);
                }
                else if (tmp instanceof LastCardAnswer) {
                    if(!initializedView) answerView.add(tmp);
                    else viewCardsMessage(tmp);
                }
                else if (tmp instanceof HandAfterRestoreAnswer) {
                    if(!initializedView) answerView.add(tmp);
                    else viewCardsMessage(tmp);
                }
                else if (tmp instanceof SchoolStudentAnswer) {
                    if(!initializedView) answerView.add(tmp);
                    else viewSchoolMessage(tmp);
                }
                else if (tmp instanceof ProfessorAnswer) {
                    if(!initializedView) answerView.add(tmp);
                    else viewSchoolMessage(tmp);
                }
                else if (tmp instanceof SchoolTowersAnswer) {
                    if(!initializedView) answerView.add(tmp);
                    else viewSchoolMessage(tmp);
                }
                else if (tmp instanceof CoinsAnswer) {
                    if(!initializedView)answerView.add(tmp);
                    else viewSchoolMessage(tmp);
                }
                else if (tmp instanceof CloudStudentAnswer){
                    if(!initializedView) answerView.add(tmp);
                    else viewCloudMessage(tmp);
                }
                else if (tmp instanceof IslandStudentAnswer) {
                    if(!initializedView) answerView.add(tmp);
                    else viewIslandMessage(tmp);
                }
                else if (tmp instanceof MotherPositionAnswer) {
                    if(!initializedView) answerView.add(tmp);
                    else viewIslandMessage(tmp);
                }
                else if (tmp instanceof MaxMovementMotherNatureAnswer) {
                    if (!initializedView)answerView.add(tmp);
                    else viewIslandMessage(tmp);
                }
                else if (tmp instanceof IslandTowersNumberAnswer) {
                    if(!initializedView) answerView.add(tmp);
                    else viewIslandMessage(tmp);
                }
                else if (tmp instanceof IslandTowersColorAnswer){
                    if(!initializedView) answerView.add(tmp);
                    else viewIslandMessage(tmp);
                }
                else if (tmp instanceof UnifiedIslandAnswer) {
                    if(!initializedView) answerView.add(tmp);
                    else viewIslandMessage(tmp);
                }
                else if (tmp instanceof InhibitedIslandAnswer) {
                    if(!initializedView) answerView.add(tmp);
                    else viewIslandMessage(tmp);
                }
                else if (tmp instanceof UseSpecialAnswer) {
                    if(!initializedView) answerView.add(tmp);
                    else viewSpecialMessage(tmp);
                }
                else if (tmp instanceof SetSpecialAnswer) {
                    if(!initializedView) answerView.add(tmp);
                    else viewSpecialMessage(tmp);
                }
                else if (tmp instanceof InfoSpecial1or7or11Answer) {
                    if(!initializedView) answerView.add(tmp);
                    else viewSpecialMessage(tmp);
                } else if (tmp instanceof InfoSpecial5Answer) {
                    if(!initializedView) answerView.add(tmp);
                    else viewSpecialMessage(tmp);
                } else if (tmp instanceof DisconnectedAnswer) {
                    disconnectedListener.notifyDisconnected();
                    disconnected = true;
                } else if (tmp instanceof GameOverAnswer) {
                    view.setWinner(((GameOverAnswer) tmp).getWinner());
                    disconnected = true;
                } else answersTmpList.add(tmp);
                synchronized (answerTmpLock) {
                    for (int i = 0; i < answersTmpList.size(); i++) {
                        answersList.add(answersTmpList.get(i));
                        answersTmpList.remove(i);
                    }
                    if (answersList.size() != 0) answerTmpLock.notify();
                }
                if(initializedView && answerView.isEmpty()) viewThread.interrupt();
            }
        } catch (SocketException e) {
            serverOfflineListener.notifyServerOffline();
        } catch (IOException e) {
            serverOfflineListener.notifyServerOffline();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void setDisconnectedListener(DisconnectedListener disconnectedListener){
        this.disconnectedListener = disconnectedListener;
    }
    public void setServerOfflineListener(ServerOfflineListener serverOfflineListener){
        this.serverOfflineListener = serverOfflineListener;
    }
    public void setPongListeners(PongListener pongListener){
        this.pongListener = pongListener;
    }

}
