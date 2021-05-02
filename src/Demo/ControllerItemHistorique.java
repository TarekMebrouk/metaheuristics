package Demo;
import java.io.IOException;

import Configuration.*;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class ControllerItemHistorique {

	zoneWSN Zone;
	
	@FXML Label XYzone;
	@FXML Label RayComm;
	@FXML Label RayCouv;
	@FXML Label XYhecn;
	@FXML Label NbEmpValide;
	
	public void Selected(MouseEvent event) throws IOException {
		System.out.println(Zone.getxAxis()+" "+Zone.getyAxis()+" Rcomm: "+Zone.getRcomm()+" Rsens: "+Zone.getRsens());
		ControllerHome.Zone=Zone;
		ControllerHome.ParamButton.setDisable(true);
		ControllerHome.ExpermButton.setDisable(true);
		ControllerHome.StatButton.setDisable(true);
		
		ControllerHome.parametres=null; ControllerHome.metaMaitre=null; 
		Parent root = FXMLLoader.load(getClass().getResource("/Demo/Initialisation.fxml"));
		Scene scene = ((Node)event.getSource()).getScene();
		root.translateXProperty().set(scene.getWidth());
		    Pane precedent=(Pane) ControllerHome.ContainerPane.getChildren().get(0);
		    ControllerHome.ContainerPane.getChildren().add(root);
	        Timeline timeline = new Timeline();
	        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
	        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
	        timeline.getKeyFrames().add(kf);
	        timeline.setOnFinished(t -> {
	        	ControllerHome.ContainerPane.getChildren().remove(precedent);
	        });
	        timeline.play();
	        ControllerHome.NomEtape.setText("PHASE D'INITIALISATION");
	}
	
	
}
