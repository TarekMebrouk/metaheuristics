package Demo;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BubbleChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Configuration.*;

public class ControllerExperimentation implements Initializable{

	MetaMaitre MetaHeuristique;
	BubbleChart<Number,Number> ChartSolutions;
	
    @FXML Label fitness;

    @FXML Label NombreCapteurs;

    @FXML Label TauxCouv;
    
    @FXML Pane Experimentation;

    @FXML Pane pane;
   

    @FXML
    void LancerStatistique(ActionEvent event) throws IOException {
    	if (MetaHeuristique!=null) {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(getClass().getResource("Statistiques.fxml"));
    		loader.load();
    		ControllerStatistique controler= (ControllerStatistique)loader.getController();
    		controler.Maitre=MetaHeuristique;
    		Parent root = loader.getRoot();
			Scene scene = ((Node)event.getSource()).getScene();
			root.translateXProperty().set(scene.getWidth());
			ControllerHome.ContainerPane.getChildren().add(root);
		        Timeline timeline = new Timeline();
		        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
		        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
		        timeline.getKeyFrames().add(kf);
		        timeline.setOnFinished(t -> {
		        	ControllerHome.ContainerPane.getChildren().remove(Experimentation);
		        });
		        timeline.play();
		        ControllerHome.NomEtape.setText("PHASE DES STATISTIQUES");
		        ControllerHome.StatButton.setDisable(false);
    	}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ControllerHome.ChangerCssButton(ControllerHome.ExpermButton);
		if (ControllerHome.metaMaitre != null) {
			MetaHeuristique=ControllerHome.metaMaitre;
			Solution S=ControllerHome.metaMaitre.getFinalSOl();
			// Set Fintess 
			fitness.setText(""+S.getFitness());
			// Set Nombre de Capteurs 
			NombreCapteurs.setText(""+S.getNombreCapteurs());
			// Set Taux de Couverture 
			double couverture=(S.CoveredPoints()*100)/S.getZone().getTotalePoints();
		    TauxCouv.setText(couverture+" %");
			// Display solution 
		    ChartSolutions=ControllerParametrage.DisplaySolution(pane,ControllerHome.metaMaitre);
		 // Display solution 
		    ChartSolutions.lookup(".chart-vertical-grid-lines").setStyle("\n" + 
		    		" 	-fx-stroke: black;\n" + 
		    		"    -fx-stroke-width: 1; ");
		    ChartSolutions.lookup(".chart-horizontal-grid-lines").setStyle("\n" + 
		    		" 	-fx-stroke: black;\n" + 
		    		"    -fx-stroke-width: 1; ");
		}
	}
}
