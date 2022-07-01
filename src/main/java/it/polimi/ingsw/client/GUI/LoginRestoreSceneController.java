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
import javafx.stage.Stage;

public class LoginRestoreSceneController implements SceneController{
    private Exit proxy;
    private GUI gui;
    private String currentNickname;
    private final Service<Boolean> loginService;
    @FXML private Button next;
    @FXML private TextField nicknameBox;
    @FXML private Label errorMessage;
    @FXML private Label insertNickname;

    public LoginRestoreSceneController() {
        this.currentNickname = "";
        this.loginService = new LoginService();
        loginService.setOnSucceeded(workerStateEvent -> {
            Boolean result = loginService.getValue();
            if(result){
                gui.setYourNickname(currentNickname);
                gui.phaseHandler("SetView");
            }
            else{
                showErrorMessage();
                gui.switchScene(GUI.LOGINRESTORE);
            }
        });

    }

    private class LoginService extends Service<Boolean> {

        @Override
        protected Task<Boolean> createTask() {
            return new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    return proxy.setupConnection(currentNickname, null);
                }
            };
        }
    }



    public void nextPressed(ActionEvent e){
        currentNickname = this.nicknameBox.getText();
        if(!currentNickname.equals("")) {
            if(loginService.getState()== Worker.State.READY)
                loginService.start();
            else
                loginService.restart();
            gui.switchScene(GUI.WAITING);
        }
        else showErrorMessage();
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

    public void closeScene(){
        Stage stage = (Stage) next.getScene().getWindow();
        stage.close();
    }
}
