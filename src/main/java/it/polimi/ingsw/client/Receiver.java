package it.polimi.ingsw.client;

import it.polimi.ingsw.listeners.DisconnectedListener;
import it.polimi.ingsw.listeners.ServerOfflineListener;
import it.polimi.ingsw.listeners.SoldOutListener;
import it.polimi.ingsw.server.answer.*;
import it.polimi.ingsw.server.answer.viewmessage.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Receiver {

    private Thread receive;
    private final Object lock1;
    private final Object lock2;
    private final Object specialLock;
    private final ObjectInputStream inputStream;
    private ArrayList<Answer> answersList;
    private final Socket socket;
    private DisconnectedListener disconnectedListener;
    private ServerOfflineListener serverOfflineListener;
    private SoldOutListener soldOutListener;
    private boolean disconnected;
    private final View view;
    private boolean initializedView;

    public Receiver(Object lock2, Socket socket, View view) throws IOException {
        this.socket = socket;
        //socket.setSoTimeout(15000);
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.view = view;
        answersList = new ArrayList<>();
        lock1 = new Object();
        this.lock2 = lock2;
        specialLock = new Object();
        disconnected = false;
        startReceive();
    }

    public void setViewInitialized(){
        initializedView = true;
        lock2.notifyAll();
        System.out.println("lock2 notify");
    }

    public void setDisconnectedListener(DisconnectedListener disconnectedListener){
        this.disconnectedListener = disconnectedListener;
    }
    public void setServerOfflineListener(ServerOfflineListener serverOfflineListener){
        this.serverOfflineListener = serverOfflineListener;
    }

    public void setSoldOutListener(SoldOutListener soldOutListener){
        this.soldOutListener = soldOutListener;
    }

    public Answer receive() {
        Answer tmp;
        synchronized (lock1){
            try {
                if (answersList.size() == 0){
                    lock1.wait();
                }
            }catch (InterruptedException e){
            }
            tmp = answersList.get(0);
            answersList.remove(0);
        }
        return tmp;
    }

    public void  setTimeout() throws IOException {
        System.out.println("set time");
        try {
            socket.setSoTimeout(15000);
        }catch (SocketException e){
            disconnected =true;
            serverOfflineListener.notifyServerOffline();
        }
    }

    private void startReceive(){
        receive = new Thread(() -> {
            ArrayList<Answer> answersTmpList = new ArrayList<>();
            Answer tmp;
            System.out.println("thread started");
            try {
                setTimeout();
                while (!disconnected) {
                    tmp = (Answer) inputStream.readObject();
                    if (tmp instanceof PongAnswer) {
                        //socket.setSoTimeout(15000);
                    } else if (tmp instanceof GameInfoAnswer) {
                        synchronized (lock2) {
                            view.initializedView(((GameInfoAnswer) tmp).getNumberOfPlayers(), ((GameInfoAnswer) tmp).isExpertMode());
                            lock2.notify();
                        }
                    } else if (tmp instanceof UserInfoAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.setUserInfo(((UserInfoAnswer) tmpMsg).getPlayerRef(), ((UserInfoAnswer) tmpMsg).getCharacter(), ((UserInfoAnswer) tmpMsg).getNickname());
                                }
                            }); thread.start();
                        } else{
                            view.setUserInfo(((UserInfoAnswer) tmp).getPlayerRef(), ((UserInfoAnswer) tmp).getCharacter(), ((UserInfoAnswer) tmp).getNickname());
                            }
                    } else if (tmp instanceof LastCardAnswer) {
                        if(!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                }
                                view.setLastCard(((LastCardAnswer) tmpMsg).getPlayerRef(), ((LastCardAnswer) tmpMsg).getCard());
                            });thread.start();
                        }
                        else view.setLastCard(((LastCardAnswer) tmp).getPlayerRef(), ((LastCardAnswer) tmp).getCard());
                    } else if (tmp instanceof NumberOfCardsAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.setNumberOfCards(((NumberOfCardsAnswer) tmpMsg).getPlayerRef(), ((NumberOfCardsAnswer) tmpMsg).getNumberOfCards());
                                }
                            });thread.start();
                        } else view.setNumberOfCards(((NumberOfCardsAnswer) tmp).getPlayerRef(), ((NumberOfCardsAnswer) tmp).getNumberOfCards());
                    } else if (tmp instanceof HandAfterRestoreAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.restoreCards(((HandAfterRestoreAnswer) tmpMsg).getHand());
                                }
                            });thread.start();
                        }
                        else {
                            view.restoreCards(((HandAfterRestoreAnswer) tmp).getHand());
                        }
                    } else if (tmp instanceof SchoolStudentAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.setSchoolStudents(((SchoolStudentAnswer) tmpMsg).getPlace(), ((SchoolStudentAnswer) tmpMsg).getComponentRef(), ((SchoolStudentAnswer) tmpMsg).getColor(), ((SchoolStudentAnswer) tmpMsg).getNewValue());
                                }
                            });thread.start();
                        }
                        else view.setSchoolStudents(((SchoolStudentAnswer) tmp).getPlace(), ((SchoolStudentAnswer) tmp).getComponentRef(), ((SchoolStudentAnswer) tmp).getColor(), ((SchoolStudentAnswer) tmp).getNewValue());
                    } else if (tmp instanceof ProfessorAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.setProfessors(((ProfessorAnswer) tmpMsg).getPlayerRef(), ((ProfessorAnswer) tmpMsg).getColor(), ((ProfessorAnswer) tmpMsg).isProfessor());
                                }
                            }); thread.start();
                        } else
                            view.setProfessors(((ProfessorAnswer) tmp).getPlayerRef(), ((ProfessorAnswer) tmp).getColor(), ((ProfessorAnswer) tmp).isProfessor());
                    } else if (tmp instanceof SchoolTowersAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.setSchoolTowers(((SchoolTowersAnswer) tmpMsg).getPlayerRef(), ((SchoolTowersAnswer) tmpMsg).getTowers());
                                }
                            }); thread.start();
                        } else view.setSchoolTowers(((SchoolTowersAnswer) tmp).getPlayerRef(), ((SchoolTowersAnswer) tmp).getTowers());
                    } else if (tmp instanceof CoinsAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.setCoins(((CoinsAnswer) tmpMsg).getPlayerRef(), ((CoinsAnswer) tmpMsg).getCoin());
                                }
                            }); thread.start();
                        } else view.setCoins(((CoinsAnswer) tmp).getPlayerRef(), ((CoinsAnswer) tmp).getCoin());
                    } else if (tmp instanceof CloudStudentAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.setClouds(((CloudStudentAnswer) tmpMsg).getCloudRef(), ((CloudStudentAnswer) tmpMsg).getColor(), ((CloudStudentAnswer) tmpMsg).getNewValue());
                                }
                            }); thread.start();
                        }
                        else view.setClouds(((CloudStudentAnswer) tmp).getCloudRef(), ((CloudStudentAnswer) tmp).getColor(), ((CloudStudentAnswer) tmp).getNewValue());
                    } else if (tmp instanceof IslandStudentAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.setStudentsIsland(((IslandStudentAnswer) tmpMsg).getIslandRef(), ((IslandStudentAnswer) tmpMsg).getColor(), ((IslandStudentAnswer) tmpMsg).getNewValue());
                                }
                            }); thread.start();
                        }
                        else view.setStudentsIsland(((IslandStudentAnswer) tmp).getIslandRef(), ((IslandStudentAnswer) tmp).getColor(), ((IslandStudentAnswer) tmp).getNewValue());
                    } else if (tmp instanceof MotherPositionAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.setMotherPosition(((MotherPositionAnswer) tmpMsg).getMotherPosition());
                                }
                            }); thread.start();
                        } else view.setMotherPosition(((MotherPositionAnswer) tmp).getMotherPosition());
                    } else if (tmp instanceof MaxMovementMotherNatureAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.setMaxStepsMotherNature(((MaxMovementMotherNatureAnswer) tmpMsg).getMaxMovement());
                                }
                            }); thread.start();
                        } else view.setMaxStepsMotherNature(((MaxMovementMotherNatureAnswer) tmp).getMaxMovement());
                    } else if (tmp instanceof IslandTowersNumberAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.setIslandTowers(((IslandTowersNumberAnswer) tmpMsg).getIslandRef(), ((IslandTowersNumberAnswer) tmpMsg).getTowersNumber());
                                }
                            }); thread.start();
                        } else view.setIslandTowers(((IslandTowersNumberAnswer) tmp).getIslandRef(), ((IslandTowersNumberAnswer) tmp).getTowersNumber());
                    } else if (tmp instanceof IslandTowersColorAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.setTowersColor(((IslandTowersColorAnswer) tmpMsg).getIslandRef(), ((IslandTowersColorAnswer) tmpMsg).getColor());
                                }
                            }); thread.start();
                        }else view.setTowersColor(((IslandTowersColorAnswer) tmp).getIslandRef(), ((IslandTowersColorAnswer) tmp).getColor());
                    } else if (tmp instanceof InhibitedIslandAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.setInhibited(((InhibitedIslandAnswer) tmpMsg).getIslandRef(), ((InhibitedIslandAnswer) tmpMsg).getInhibited());
                                }
                            }); thread.start();
                            } else view.setInhibited(((InhibitedIslandAnswer) tmp).getIslandRef(), ((InhibitedIslandAnswer) tmp).getInhibited());
                    } else if (tmp instanceof UnifiedIslandAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.removeUnifiedIsland(((UnifiedIslandAnswer) tmpMsg).getUnifiedIsland());
                                }
                            }); thread.start();
                        } else view.removeUnifiedIsland(((UnifiedIslandAnswer) tmp).getUnifiedIsland());
                    } else if (tmp instanceof UseSpecialAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.setSpecialUsed(((UseSpecialAnswer) tmpMsg).getSpecialIndex(), ((UseSpecialAnswer) tmpMsg).getPlayerRef());
                                }
                            }); thread.start();
                        } else view.setSpecialUsed(((UseSpecialAnswer) tmp).getSpecialIndex(), ((UseSpecialAnswer) tmp).getPlayerRef());
                    } else if (tmp instanceof SetSpecialAnswer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.setSpecial(((SetSpecialAnswer) tmpMsg).getSpecialRef(), ((SetSpecialAnswer) tmpMsg).getCost());
                                    synchronized (specialLock) {
                                        if(view.specialSet()) specialLock.notifyAll();
                                    }
                                }
                            }); thread.start();
                            } else {
                                synchronized (specialLock) {
                                    if (!view.specialSet()) {
                                    try {
                                        specialLock.wait();
                                    } catch (InterruptedException e) {}
                                }
                            }
                            view.setSpecial(((SetSpecialAnswer) tmp).getSpecialRef(), ((SetSpecialAnswer) tmp).getCost());
                        }
                        } else if (tmp instanceof InfoSpecial1or7or11Answer) {
                        if (!initializedView) {
                            final Answer tmpMsg = tmp;
                            Thread thread = new Thread(() -> {
                                if (!view.specialSet()) {
                                    synchronized (specialLock) {
                                        try {
                                            specialLock.wait();
                                        } catch (InterruptedException e) {}
                                    }
                                }
                                synchronized (lock2) {
                                    try {
                                        lock2.wait();
                                    } catch (InterruptedException e) {}
                                    view.setSpecialStudents(((InfoSpecial1or7or11Answer) tmpMsg).getStudentColor(), ((InfoSpecial1or7or11Answer) tmpMsg).getValue(), ((InfoSpecial1or7or11Answer) tmpMsg).getSpecialIndex());
                                }
                            }); thread.start();
                        } else{
                            if (!view.specialSet()) {
                                synchronized (specialLock) {
                                    try {
                                        specialLock.wait();
                                    } catch (InterruptedException e) {}
                                }
                            }
                            view.setSpecialStudents(((InfoSpecial1or7or11Answer) tmp).getStudentColor(), ((InfoSpecial1or7or11Answer) tmp).getValue(), ((InfoSpecial1or7or11Answer) tmp).getSpecialIndex());

                        }
                    } else if (tmp instanceof InfoSpecial5Answer) {
                            if (!initializedView) {
                                final Answer tmpMsg = tmp;
                                Thread thread = new Thread(() -> {
                                    if (!view.specialSet()) {
                                        synchronized (specialLock) {
                                            try {
                                                specialLock.wait();
                                            } catch (InterruptedException e) {}
                                        }
                                    }
                                    synchronized (lock2) {
                                        try {
                                            lock2.wait();
                                        } catch (InterruptedException e) {}
                                        view.setNoEntry(((InfoSpecial5Answer) tmpMsg).getCards());
                                    }
                                }); thread.start();
                            } else {
                                if (!view.specialSet()) {
                                    synchronized (specialLock) {
                                        try {
                                            specialLock.wait();
                                        } catch (InterruptedException e) {}
                                    }
                                }
                                view.setNoEntry(((InfoSpecial5Answer) tmp).getCards());
                            }
                    } else if (tmp instanceof DisconnectedAnswer) {
                        disconnected = true;
                        answersTmpList.clear();
                        disconnectedListener.notifyDisconnected();
                    } else if (tmp instanceof GameOverAnswer) {
                        synchronized (lock2) {
                            if (!initializedView) lock2.wait();
                            view.setWinner(((GameOverAnswer) tmp).getWinner());
                        }
                    }  else if(tmp instanceof SoldOutAnswer){
                        disconnected = true;
                        soldOutListener.notifySoldOut();
                    }
                    else answersTmpList.add(tmp);
                    synchronized (lock1){
                        for(int i=0; i<answersTmpList.size(); i++) {
                            answersList.add(answersTmpList.get(i));
                            answersTmpList.remove(i);
                        }
                        if(answersList.size()!=0) lock1.notify();
                    }
                }
            } catch (SocketException e){
                try {
                    serverOfflineListener.notifyServerOffline();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } catch (IOException | ClassNotFoundException e) {
                try {
                    socket.close();
                    return;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receive.start();
    }
}
