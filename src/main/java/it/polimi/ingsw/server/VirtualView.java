package it.polimi.ingsw.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class VirtualView implements PropertyChangeListener {
    //virtual View class listen to PropertyChanges in GameBoard class, and is listened by the Controller

    private final PropertyChangeSupport viewListeners = new PropertyChangeSupport(this);
    private boolean expertMode;
    private int phase;
    private String playedCard;

    public void addPropertyChangeListener(PropertyChangeListener viewListener){
        this.viewListeners.addPropertyChangeListener(viewListener);
        expertMode = false;
    }

    //probabilmente da spostare
    public void setExpertMode(String isExpert){
        boolean oldValue = this.expertMode;
        if(isExpert == "easy"){
            this.expertMode = true;
        }
        else if(isExpert == "expert") {
            this.expertMode = false;
        }
        this.viewListeners.firePropertyChange("expertMode", oldValue, expertMode);
    }

    public void setPhase(String phase){
        int previousPhase = this.phase;

        if(phase == "planningPhase"){
            this.phase = 1;
        }
        else if(phase == "actionPhase"){
            this.phase = 2;
        }
        this.viewListeners.firePropertyChange("gamePhase", previousPhase, this.phase );
    }

    public void getChosenAssistant(String assistantCard) {
        this.playedCard = assistantCard;
        this.viewListeners.firePropertyChange("assistantCard", playedCard, playedCard);
    }



    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
