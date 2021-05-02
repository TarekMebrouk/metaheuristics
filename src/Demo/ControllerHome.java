package Demo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Configuration.*;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ControllerHome implements Initializable {
    @FXML Button InitialisationBtn;
    static Button  InitButton;

    @FXML  Button ParametrageBtn;
    static Button  ParamButton;

    @FXML  Button ExperimentationBtn;
    static Button  ExpermButton;

    @FXML  Button StatistiqueBtn;
    static Button  StatButton;

    @FXML Label PhaseName;
    static Label NomEtape;

    @FXML StackPane stackpane;
    static StackPane ContainerPane;
    
    static MetaMaitre metaMaitre;
    static zoneWSN Zone;
    static ReglageParametres parametres;
    
    @FXML
    void Exit(ActionEvent event) {
    	 ControllerInitialisation.Save();
    	((Node)event.getSource()).getScene().getWindow().hide();
    }
    
    @FXML 
    void newTest(ActionEvent event) throws IOException {
    	 // Set Buttons Disable 
	       ControllerHome.ParamButton.setDisable(true);
	       ControllerHome.ExpermButton.setDisable(true);
	       ControllerHome.StatButton.setDisable(true);
	     // Set All Precedent Data to null 
	     ControllerHome.Zone=null;
	     ControllerHome.metaMaitre=null;
	     ControllerHome.parametres=null;
	     
	  // Open Initialisation
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

    @FXML
    void OpenExperimentation(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/Demo/Experimentation.fxml"));
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
	        ControllerHome.NomEtape.setText("PHASE D'EXPERIMENTATION");
    }

    @FXML
    void OpenInitialisation(ActionEvent event) throws IOException {
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

    @FXML
    void OpenParametrage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/Demo/Parametrage.fxml"));
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
	        ControllerHome.NomEtape.setText("PHASE REGLAGE DE PARAMETRES");
    }

    @FXML
    void OpenStatistique(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/Demo/Statistiques.fxml"));
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
	        ControllerHome.NomEtape.setText("PHASE DES STATISTIQUES");
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialisation static Elements 
		InitButton=InitialisationBtn;
		ParamButton=ParametrageBtn;
		ExpermButton=ExperimentationBtn;
		StatButton=StatistiqueBtn;
		NomEtape=PhaseName;
		ContainerPane=stackpane;
		
       // Set Buttons Disable 
	       ControllerHome.ParamButton.setDisable(true);
	       ControllerHome.ExpermButton.setDisable(true);
	       ControllerHome.StatButton.setDisable(true);
	     
	  // Open Initialisation
	       ControllerHome.NomEtape.setText("PHASE D'INITIALISATION");
	        FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Initialisation.fxml"));
			try {
				loader.load();
				Parent root = loader.getRoot();
				ControllerHome.ContainerPane.getChildren().add(root);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	
	public static void ChangerCssButton(Button Phase) {
		if (InitButton == Phase) {
			ControllerHome.InitButton.setStyle("-fx-background-color: #aed581;\n" + 
		      		"	 	-fx-border-width :  0px 0px 0px 0px;");
		} else {
			ControllerHome.InitButton.setStyle("-fx-background-color:  #F0F4C3;\n" + 
					"	 	-fx-border-color :  #AED581;\n" + 
					"	 	-fx-border-width :  0px 0px 0px 3px;");
		}
		if (ParamButton == Phase) {
			ControllerHome.ParamButton.setStyle("-fx-background-color: #aed581;\n" + 
		      		"	 	-fx-border-width :  0px 0px 0px 0px;");
		} else {
			ControllerHome.ParamButton.setStyle("-fx-background-color:  #F0F4C3;\n" + 
					"	 	-fx-border-color :  #AED581;\n" + 
					"	 	-fx-border-width :  0px 0px 0px 3px;");
		}
		if (ExpermButton == Phase) {
			ControllerHome.ExpermButton.setStyle("-fx-background-color: #aed581;\n" + 
		      		"	 	-fx-border-width :  0px 0px 0px 0px;");
		} else {
			ControllerHome.ExpermButton.setStyle("-fx-background-color:  #F0F4C3;\n" + 
					"	 	-fx-border-color :  #AED581;\n" + 
					"	 	-fx-border-width :  0px 0px 0px 3px;");
		}
		if (StatButton ==  Phase) {
			ControllerHome.StatButton.setStyle("-fx-background-color: #aed581;\n" + 
		      		"	 	-fx-border-width :  0px 0px 0px 0px;");
		} else {
			ControllerHome.StatButton.setStyle("-fx-background-color:  #F0F4C3;\n" + 
					"	 	-fx-border-color :  #AED581;\n" + 
					"	 	-fx-border-width :  0px 0px 0px 3px;");
		}
	}
	
}
