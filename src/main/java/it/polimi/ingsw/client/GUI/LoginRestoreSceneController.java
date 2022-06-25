package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Exit;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginRestoreSceneController implements SceneController{
    private Exit proxy;
    private GUI gui;
    private String currentNickname;
    private Service<Boolean> loginService;
    private Service<String> getPhaseService;
    @FXML private Button next;
    @FXML private TextField nicknameBox;
    @FXML private Label errorMessage;
    @FXML private Label insertNickname;

    public LoginRestoreSceneController() {
        this.currentNickname = "";
        this.loginService= new LoginService();
        this.getPhaseService = new GetPhaseService();
        loginService.setOnSucceeded(workerStateEvent -> {
            Boolean result = loginService.getValue();
            if(result){
                gui.setYourNickname(currentNickname);
                gui.phaseHandler("SetView");
                getPhaseService.start();
            }
            else{
                showErrorMessage();
                gui.switchScene(GUI.LOGINRESTORE);
            }
        });
        getPhaseService.setOnSucceeded(workerStateEvent -> {
            String phase = getPhaseService.getValue();
            System.out.println("ph in succ "+phase);
            if(phase.equals("Play card!")) gui.phaseHandler("PlayCard");
            else if(phase.equals("Start your Action Phase!")) gui.phaseHandler("StartTurn");
        });
    }

    public void nextPressed(ActionEvent e) throws IOException, ClassNotFoundException {
        currentNickname = this.nicknameBox.getText();
        if(currentNickname!="") {
            if(loginService.getState()== Worker.State.READY)
                loginService.start();
            else
                loginService.restart();
            gui.switchScene(GUI.WAITING);
        }
        else showErrorMessage();
    }

    private class GetPhaseService extends Service<String>{

        @Override
        protected Task<String> createTask() {
            System.out.println("get phase");
            return new Task<String>(){
                @Override
                protected String call() throws IOException {
                    System.out.println("call phase");
                    String phase = proxy.getPhase();
                    System.out.println(phase);
                    return phase;
                }
            };
        }
    }

    private class LoginService extends Service<Boolean> {

        @Override
        protected Task<Boolean> createTask() {
            System.out.println("loginService started");
            return new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    System.out.println("call login service");
                    Boolean result = proxy.setupConnection(currentNickname, null);
                    System.out.println(result);
                    return result;
                }
            };
        }
    }

    public void showErrorMessage(){
        errorMessage.setVisible(true);
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void setProxy(Exit proxy) {
        this.proxy = proxy;
    }
}
