package it.polimi.ingsw.controller;

import it.polimi.ingsw.server.VirtualView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Controller implements PropertyChangeListener {

    private RoundController roundController;
    private PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    private VirtualView virtualView;
    private boolean isExpert = false;
    private String[] playersInfo = null;
    private String chosenAssistant;


    public Controller(VirtualView virtualView, int numberOfPlayers){
        roundController = new RoundController(isExpert, numberOfPlayers, playersInfo, this);
        this.virtualView = virtualView;
        virtualView.addPropertyChangeListener(this);
        //the class controller is added as a listener for the virtualView class

    }

    //when the virtual View changes that property (evt), the controller changes is state accordingly to the property that has changed
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if(propertyName == "expertMode"){
            isExpert = (boolean) evt.getNewValue();
        }
        else if(propertyName == "gamePhase"){
            if((int) evt.getNewValue() == 1) {
                roundController.startPlanningPhase();
            }
            else if((int) evt.getNewValue() == 2){
                roundController.startActionPhase();
        }
        else if(propertyName == "assistantCards"){
                this.chosenAssistant = (String) evt.getNewValue();
            }
        }

    }

    public String getChosenAssistant(){
        return this.chosenAssistant;

    }
}