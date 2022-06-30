package it.polimi.ingsw.client;

import it.polimi.ingsw.listeners.DisconnectedListener;
import it.polimi.ingsw.listeners.ServerOfflineListener;
import it.polimi.ingsw.server.answer.*;
import it.polimi.ingsw.server.answer.viewAnswer.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Receiver extends Thread {

    private Thread receive;
    private Thread viewThread;
    private final Object AnswerTmpLock;
    private final Object initializedViewLock;
    private final Object AnswerViewLock;
    private final Object specialLock;
    private final Object setViewLock;
    private final ObjectInputStream inputStream;
    private ArrayList<Answer> answersList;
    private final Socket socket;
    private DisconnectedListener disconnectedListener;
    private ServerOfflineListener serverOfflineListener;
    private boolean disconnected;
    private final View view;
    private boolean initializedView;
    private ArrayList<Answer> viewAnswer;
    private int pingCounter;

    public Receiver(Object initializedViewLock, Socket socket, View view, int pingCounter) throws IOException {
        this.socket = socket;
        this.inputStream = new ObjectInputStream(this.socket.getInputStream());
        //this.socket.setSoTimeout(15000);
        this.view = view;
        this.pingCounter = pingCounter;
        answersList = new ArrayList<>();
        viewAnswer = new ArrayList<>();
        AnswerTmpLock = new Object();
        setViewLock = new Object();
        this.initializedViewLock = initializedViewLock;
        AnswerViewLock = new Object();
        specialLock = new Object();
        disconnected = false;
    }

    public void setViewInitialized(){
        synchronized (setViewLock) {
            initializedView = true;
            System.out.println("view set");
            setViewLock.notifyAll();
        }
    }

    public Answer receive() {
        Answer tmp;
        synchronized (AnswerTmpLock){
            try {
                if (answersList.size() == 0){
                    AnswerTmpLock.wait();
                }
            }catch (InterruptedException e){
            }
            tmp = answersList.get(0);
            answersList.remove(0);
        }
        return tmp;
    }

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

    private void viewCardsMessage(Answer tmp) {
        if (tmp instanceof LastCardAnswer) {
            view.setLastCard(((LastCardAnswer) tmp).getPlayerRef(), ((LastCardAnswer) tmp).getCard());
        } else if (tmp instanceof HandAfterRestoreAnswer) {
            view.restoreCards(((HandAfterRestoreAnswer) tmp).getHand());
        }
    }

    private void viewCloudMessage(Answer tmp){
        view.setClouds(((CloudStudentAnswer) tmp).getCloudRef(), ((CloudStudentAnswer) tmp).getColor(), ((CloudStudentAnswer) tmp).getNewValue());
    }

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
                        } catch (InterruptedException e) {}
                    }
            }
            view.setSpecialStudents(((InfoSpecial1or7or11Answer) tmp).getStudentColor(), ((InfoSpecial1or7or11Answer) tmp).getValue(), ((InfoSpecial1or7or11Answer) tmp).getSpecialIndex());
        } else if (tmp instanceof InfoSpecial5Answer) {
            if (!view.specialSet()) {
                synchronized (specialLock) {
                    try {
                        specialLock.wait();
                    } catch (InterruptedException e) {}
                }
            }
            view.setNoEntry(((InfoSpecial5Answer) tmp).getCards());
        }
    }

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

    private void viewMessage(){
        viewThread = new Thread(() -> {
            Answer tmp;
            while(!disconnected) {
                try {
                    synchronized (setViewLock) {
                        if (!initializedView) setViewLock.wait();
                    }
                    synchronized (AnswerViewLock) {
                        if (viewAnswer.size() == 0) {
                            AnswerViewLock.wait();
                        }
                        tmp = viewAnswer.get(0);
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
                        viewAnswer.remove(0);
                    }
                } catch (InterruptedException e) {}
            }
        });
        viewThread.start();
    }

    private void viewNotInitialized(Answer tmp){
        synchronized (AnswerViewLock) {
            viewAnswer.add(tmp);
            AnswerViewLock.notify();
        }
    }

    public void run(){
        //receive = new Thread(() -> {
        viewMessage();
        try {
            ArrayList<Answer> answersTmpList = new ArrayList<>();
            Answer tmp;
            socket.setSoTimeout(15000);
            while (!disconnected) {
                tmp = (Answer) inputStream.readObject();
                //socket.setSoTimeout(15000);
                if (tmp instanceof PongAnswer) {
                    socket.setSoTimeout(15000);

                }
                else if (tmp instanceof GameInfoAnswer) gameMessage(tmp);
                else if (tmp instanceof UserInfoAnswer) {
                    if(!initializedView) viewNotInitialized(tmp);
                    else gameMessage(tmp);
                }
                else if (tmp instanceof LastCardAnswer) {
                    if(!initializedView) viewNotInitialized(tmp);
                    else viewCardsMessage(tmp);
                }
                else if (tmp instanceof HandAfterRestoreAnswer) {
                    if(!initializedView) viewNotInitialized(tmp);
                    else viewCardsMessage(tmp);
                }
                else if (tmp instanceof SchoolStudentAnswer) {
                    if(!initializedView) viewNotInitialized(tmp);
                    else viewSchoolMessage(tmp);
                }
                else if (tmp instanceof ProfessorAnswer) {
                    if(!initializedView) viewNotInitialized(tmp);
                    else viewSchoolMessage(tmp);
                }
                else if (tmp instanceof SchoolTowersAnswer) {
                    if(!initializedView) viewNotInitialized(tmp);
                    else viewSchoolMessage(tmp);
                }
                else if (tmp instanceof CoinsAnswer) {
                    if(!initializedView)viewNotInitialized(tmp);
                    else viewSchoolMessage(tmp);
                }
                else if (tmp instanceof CloudStudentAnswer){
                    if(!initializedView) viewNotInitialized(tmp);
                    else viewCloudMessage(tmp);
                }
                else if (tmp instanceof IslandStudentAnswer) {
                    if(!initializedView) viewNotInitialized(tmp);
                    else viewIslandMessage(tmp);
                }
                else if (tmp instanceof MotherPositionAnswer) {
                    if(!initializedView) viewNotInitialized(tmp);
                    else viewIslandMessage(tmp);
                }
                else if (tmp instanceof MaxMovementMotherNatureAnswer) {
                    if (!initializedView)viewNotInitialized(tmp);
                    else viewIslandMessage(tmp);
                }
                else if (tmp instanceof IslandTowersNumberAnswer) {
                    if(!initializedView) viewNotInitialized(tmp);
                    else viewIslandMessage(tmp);
                }
                else if (tmp instanceof IslandTowersColorAnswer){
                    if(!initializedView) viewNotInitialized(tmp);
                    else viewIslandMessage(tmp);
                }
                else if (tmp instanceof UnifiedIslandAnswer) {
                    if(!initializedView) viewNotInitialized(tmp);
                    else viewIslandMessage(tmp);
                }
                else if (tmp instanceof InhibitedIslandAnswer) {
                    if(!initializedView) viewNotInitialized(tmp);
                    else viewIslandMessage(tmp);
                }
                else if (tmp instanceof UseSpecialAnswer) {
                    if(!initializedView) viewNotInitialized(tmp);
                    else viewSpecialMessage(tmp);
                }
                else if (tmp instanceof SetSpecialAnswer) {
                    if(!initializedView) viewNotInitialized(tmp);
                    else viewSpecialMessage(tmp);
                }
                else if (tmp instanceof InfoSpecial1or7or11Answer) {
                    if(!initializedView) viewNotInitialized(tmp);
                    else viewSpecialMessage(tmp);
                } else if (tmp instanceof InfoSpecial5Answer) {
                    if(!initializedView) viewNotInitialized(tmp);
                    else viewSpecialMessage(tmp);
                } else if (tmp instanceof DisconnectedAnswer) {
                    disconnectedListener.notifyDisconnected();
                    disconnected = true;
                } else if (tmp instanceof GameOverAnswer) {
                    view.setWinner(((GameOverAnswer) tmp).getWinner());
                    disconnected = true;
                } else answersTmpList.add(tmp);
                synchronized (AnswerTmpLock) {
                    for (int i = 0; i < answersTmpList.size(); i++) {
                        answersList.add(answersTmpList.get(i));
                        answersTmpList.remove(i);
                    }
                    if (answersList.size() != 0) AnswerTmpLock.notify();
                }
                if(initializedView && viewAnswer.isEmpty()) viewThread.interrupt();
            }
        } catch (SocketException e) {
            System.out.println("time out");
                /*try {
                    //serverOfflineListener.notifyServerOffline();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }*/
        } catch (IOException | ClassNotFoundException e) {
                /*try {
                    socket.close();
                    return;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }*/
        }
    }

    public void setDisconnectedListener(DisconnectedListener disconnectedListener){
        this.disconnectedListener = disconnectedListener;
    }
    public void setServerOfflineListener(ServerOfflineListener serverOfflineListener){
        this.serverOfflineListener = serverOfflineListener;
    }

}
