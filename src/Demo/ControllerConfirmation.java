package Demo;

import javafx.event.ActionEvent;
import javafx.scene.Node;

public class ControllerConfirmation {
	
    public void Valider(ActionEvent event) {
    	ControllerInitialisation.ConfirmerSuppression();
    	ControllerInitialisation.rect.setHeight(0); ControllerInitialisation.rect.setWidth(0); ControllerInitialisation.rectInvisible.setHeight(0); ControllerInitialisation.rectInvisible.setHeight(0);
    	((Node)event.getSource()).getScene().getWindow().hide();
    }
    
    public void Annuler(ActionEvent event) {
    	ControllerInitialisation.rect.setHeight(0); ControllerInitialisation.rect.setWidth(0); ControllerInitialisation.rectInvisible.setHeight(0); ControllerInitialisation.rectInvisible.setHeight(0);
    	((Node)event.getSource()).getScene().getWindow().hide();
    }
	
}