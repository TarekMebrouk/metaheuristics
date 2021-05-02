package Demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import Configuration.*;

@SuppressWarnings("serial")
public class ControllerInitialisation implements Initializable,Serializable{
 
	@FXML TextField xZone;
	@FXML TextField yZone;
	@FXML TextField Rcomm;
	@FXML TextField Rcouv;
	@FXML TextField xHECN;
	@FXML TextField yHECN;
	@FXML  Label NbValide;
	@FXML StackPane stackpane;
	@FXML VBox Historique;
	@FXML Pane ZONE;
	@FXML Pane PaneHECN;
	@FXML Pane PaneCapteur;
	@FXML Button Suivant;
	@FXML Button sauvegarde;
	@FXML Button Zplus;
	@FXML Button Zmoins;
	@FXML Pane Initialisation;
	@FXML TextField NumberNoeudRandom;
	@FXML Pane RandomPositionnement;
	@FXML Button CreateWsn;
    static File BD=new File ("DataSet.obj");
    static  ArrayList<zoneWSN> ListeZones=new ArrayList<>();
    static ObjectOutputStream out;
    static ObjectInputStream in;
	
	static ScatterChart<Number, Number> Zone;
	static Rectangle rect,rectInvisible;
	@FXML Pane pane;
	@FXML ScrollPane spane;
	int height,width;
	@FXML Label Region;
	XYChart.Series<Number, Number> HECN;
	static XYChart.Series<Number, Number>  Sensors;
	@FXML Pane Capteur;
	@FXML TextField x;
	@FXML TextField y;
	@FXML Button Hecn;
	@FXML Button Activer;
	@FXML Button Desactiver;
	double X,Y;
	boolean SuppressionParRegion=false;
	
	static ArrayList<XYChart.Data<Number,Number>> liste=new ArrayList<>();
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Changer CSS du BUTTON 
		ControllerHome.ChangerCssButton(ControllerHome.InitButton);
		// Charger les Testes de la Base de donnée
		
		// Verfier si le teste est Sauvgarder Déja (retour Arriére)
		if (ControllerHome.Zone ==null) {
			PaneHECN.setDisable(true);
			Suivant.setDisable(true);
			sauvegarde.setDisable(true);
			Zplus.setVisible(false);
			Zmoins.setVisible(false);
			//RandomPositionnement.setVisible(true);
		} else {
			
			CreateWsn.setDisable(true);
			zoneWSN zone=ControllerHome.Zone;
			int x=zone.getxAxis();
			int y=zone.getyAxis();
			Rcomm.setText(""+zone.getRcomm());
			Rcouv.setText(""+zone.getRsens());
			xZone.setText(""+x);
			yZone.setText(""+y);
			
			Zplus.setVisible(true);
			Zmoins.setVisible(true);
			PaneHECN.setDisable(false);
			sauvegarde.setDisable(false);
			Suivant.setDisable(false);
			RandomPositionnement.setVisible(true);
			
			NbValide.setText(""+zone.getTotalePoints());
			
			/* INITIALISATION */
			height  = 400; 
			width   = 400;
			HECN    = new XYChart.Series<Number, Number> ();
			Sensors = new XYChart.Series<Number, Number> ();
			HECN.setName("HECN");
			Sensors.setName("SENSORs");
			/*----------------*/
			CreateScatterChart(x,y,1);
			addingDataFromZonewsn(zone);
		    Zone.setPrefHeight(height);
		    Zone.setPrefWidth(width);
		    Zone.setMaxSize(width, height);
		    Zone.setMinSize(width, height);
		    Zone.setLegendVisible(false);
		    pane.getChildren().add(Zone);
			stackpane.getChildren().add(pane);
	        SetRectangle();
		}
		// Charger DataSet<zoneWSN>
        try {
			ChargerDataSet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addingDataFromZonewsn(zoneWSN zone) {
			// Saisir HECN 
			setHECN(zone.getHECN().getX(), zone.getHECN().getY());
			xHECN.setText(""+zone.getHECN().getX());
			yHECN.setText(""+zone.getHECN().getY());
			
			// Saisir touts les Solution 
						for (Region region :zone.getRegions()) {
							Sensors.getData().add(new XYChart.Data<>(region.getX(),region.getY()));
						}
	}
	
	@FXML 
	public void CreateZoneWSN(ActionEvent action) {
		if (xZone.getText().compareTo("")==0 || yZone.getText().compareTo("")==0) {
			Alert alt = new Alert(Alert.AlertType.ERROR);
			alt.setTitle("Informations manquantes");
			alt.setContentText("Vous devez remplir toutes les informations concernant la zone");
			alt.setHeaderText(null);
			alt.showAndWait();
		}
		else {
			CreateWsn.setDisable(true);
			int x=Integer.parseInt(xZone.getText());
			int y=Integer.parseInt(yZone.getText());
			
			Zplus.setVisible(true);
			Zmoins.setVisible(true);
			PaneHECN.setDisable(false);
			sauvegarde.setDisable(false);
			Suivant.setDisable(false);
			RandomPositionnement.setVisible(true);
			
			int N=(x*y)+(x+y);
			NbValide.setText(""+N);
			
			/* INITIALISATION */
			height  = 400; 
			width   = 400;
			HECN    = new XYChart.Series<Number, Number> ();
			Sensors = new XYChart.Series<Number, Number> ();
			HECN.setName("HECN");
			Sensors.setName("SENSORs");
			/*----------------*/
			CreateScatterChart(x,y,1);
			addElementsForChart(x,y);
		    Zone.setPrefHeight(height);
		    Zone.setPrefWidth(width);
		    Zone.setMaxSize(width, height);
		    Zone.setMinSize(width, height);
		    Zone.setLegendVisible(false);
		    pane.getChildren().add(Zone);
			stackpane.getChildren().add(pane);
	        SetRectangle();
		}
		
	}
	
	@FXML
	public void RandomPositionZoneWSN(ActionEvent event) {
		int N=Integer.parseInt(NumberNoeudRandom.getText());
		int ZoneX=Integer.parseInt(xZone.getText());
		int ZoneY=Integer.parseInt(yZone.getText());
		int Ntotal=(ZoneX*ZoneY)+(ZoneX+ZoneY);
		if (Ntotal < N) {
			Alert alt = new Alert(Alert.AlertType.WARNING);
    		alt.setTitle("Dépassement de capacitée ");
    		alt.setContentText("Vous devez saisir un Nombre Aléatoire <= "+ZoneX+"x"+ZoneY);
    		alt.setHeaderText(null);
    		alt.showAndWait();
		}
		else {
			if (!CreateWsn.isDisable()) {
				CreateWsn.setDisable(true);
				int x=Integer.parseInt(xZone.getText());
				int y=Integer.parseInt(yZone.getText());
				
				Zplus.setVisible(true);
				Zmoins.setVisible(true);
				PaneHECN.setDisable(false);
				sauvegarde.setDisable(false);
				Suivant.setDisable(false);
				RandomPositionnement.setVisible(true);
				
				/* INITIALISATION */
				height  = 400; 
				width   = 400;
				HECN    = new XYChart.Series<Number, Number> ();
				Sensors = new XYChart.Series<Number, Number> ();
				HECN.setName("HECN");
				Sensors.setName("SENSORs");
				/*----------------*/
				CreateScatterChart(x,y,1);
				// Add Hecn 
				int xhecn,yhecn;
					xhecn=(int) x/2;
					yhecn=(int) y/2;
				setHECN(xhecn, yhecn);
				xHECN.setText(""+xhecn);
				yHECN.setText(""+yhecn);
				// Add Sensors 
				int xRegion = 0,yRegion=0;
				boolean fin;
				for (int i=0;i<N;i++) {
					fin=false; 
					while( fin == false) {
						 xRegion=(int)((Math.random() * ((ZoneX - 0) + 1)) + 0);
						 yRegion=(int)((Math.random() * ((ZoneY - 0) + 1)) + 0);
						 boolean Cond= (xRegion!=xhecn || yRegion!=yhecn);
						 if (getDataXY(xRegion,yRegion)==null && Cond) fin=true;
					} 
					Data<Number,Number> region=new Data<Number,Number>(xRegion,yRegion);
					Sensors.getData().add(region);
				}
				NbValide.setText(""+N);
			    Zone.setPrefHeight(height);
			    Zone.setPrefWidth(width);
			    Zone.setMaxSize(width, height);
			    Zone.setMinSize(width, height);
			    Zone.setLegendVisible(false);
			    pane.getChildren().add(Zone);
				stackpane.getChildren().add(pane);
		        SetRectangle();
			} else {
				int xhecn=Integer.parseInt(xHECN.getText());
				int yhecn=Integer.parseInt(yHECN.getText());
				Sensors.getData().clear();
				int xRegion = 0,yRegion=0;
				boolean fin;
				for (int i=0;i<N;i++) {
					fin=false;
					while( fin != true) {
						 xRegion=(int)((Math.random() * ((ZoneX - 0) + 1)) + 0);
						 yRegion=(int)((Math.random() * ((ZoneY - 0) + 1)) + 0);
						 boolean Cond= (xRegion!=xhecn || yRegion!=yhecn);
						 if (getDataXY(xRegion,yRegion)==null && Cond) fin=true;
					} 
					Data<Number,Number> region=new Data<Number,Number>(xRegion,yRegion);
					Sensors.getData().add(region);
				}
				NbValide.setText(""+N);
			}
		}
	}
	
	@FXML
	public void nextStep(ActionEvent event) throws IOException {
		if(Rcomm.getText().compareTo("")==0 || Rcouv.getText().compareTo("")==0) {
			Alert alt = new Alert(Alert.AlertType.ERROR);
			alt.setTitle("Informations manquantes");
			alt.setContentText("Vous devez saisir les informations concernant les capteurs");
			alt.setHeaderText(null);
			alt.showAndWait();
		}
		else {
			// Zone x*y
			int xzone=Integer.parseInt(xZone.getText());
			int yzone=Integer.parseInt(yZone.getText());
			// Cree HECN 
			int xhecn=Integer.parseInt(xHECN.getText());
			int yhecn=Integer.parseInt(yHECN.getText());
			Region hecn=new Region(xhecn,yhecn);
			// Rayon Comm-Couv
			int Rayoncomm=Integer.parseInt(Rcomm.getText());
			int Rayoncouv=Integer.parseInt(Rcouv.getText());
			// Crée ListeRegions 
			ArrayList<Region> Regions=new ArrayList<>();
			for (XYChart.Data<Number,Number> data :Zone.getData().get(1).getData()) {
				Regions.add(new Region(data.getXValue().intValue(),data.getYValue().intValue()));
			}
			//Crée ZoneWSN
			zoneWSN zone=new zoneWSN(xzone,yzone,Rayoncomm,Rayoncouv,hecn,Regions);
			ControllerHome.Zone=zone;
			ControllerHome.parametres=new ReglageParametres(zone);
			// Ouvrire la nouvelle fenétre 
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Parametrage.fxml"));
			loader.load();
			ControllerParametrage controler= (ControllerParametrage)loader.getController();
			controler.zone=zone;
			Parent root = loader.getRoot();
			Scene scene = ((Node)event.getSource()).getScene();
			root.translateXProperty().set(scene.getWidth());

			 ControllerHome.ContainerPane.getChildren().add(root);

		        Timeline timeline = new Timeline();
		        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
		        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
		        timeline.getKeyFrames().add(kf);
		        timeline.setOnFinished(t -> {
		        	ControllerHome.ContainerPane.getChildren().remove(Initialisation);
		        });
		        timeline.play();
		        ControllerHome.NomEtape.setText("PHASE REGLAGE DE PARAMETRES");
		        ControllerHome.ParamButton.setDisable(false);
		}
		
	}
	
	@FXML 
	public void Archiver(ActionEvent event) {
		System.out.println("sauvgrader . . . ");
		// SAUVGARDER LE JEUX D'ESSAI . . .
		if(Rcomm.getText().compareTo("")==0 || Rcouv.getText().compareTo("")==0) {
			JOptionPane.showMessageDialog(null, "Vous devez remplir les informations concernant les capteurs", "ERREUR !", JOptionPane.ERROR_MESSAGE);
		}
		else {
			// Zone x*y
			int xzone=Integer.parseInt(xZone.getText());
			int yzone=Integer.parseInt(yZone.getText());
			// Cree HECN 
			int xhecn=Integer.parseInt(xHECN.getText());
			int yhecn=Integer.parseInt(yHECN.getText());
			Region hecn=new Region(xhecn,yhecn);
			// Rayon Comm-Couv
			int Rayoncomm=Integer.parseInt(Rcomm.getText());
			int Rayoncouv=Integer.parseInt(Rcouv.getText());
			// Crée ListeRegions 
			ArrayList<Region> Regions=new ArrayList<>();
			for (XYChart.Data<Number,Number> data :Zone.getData().get(1).getData()) {
				Regions.add(new Region(data.getXValue().intValue(),data.getYValue().intValue()));
			}
			//Crée ZoneWSN
			zoneWSN zone=new zoneWSN(xzone,yzone,Rayoncomm,Rayoncouv,hecn,Regions);
			ListeZones.add(zone);
		}
	}
	
	@FXML
	public void ZoomPlus(ActionEvent event) {
		if( height <= 400 && width <= 400) {
			//Nouveau Zoom 
			height = height+100;
			width  = width+100;
			Zone.setMaxSize(width, height);
		    Zone.setMinSize(width, height);
			Zone.setPrefHeight(height);
			Zone.setPrefWidth(width);
			Pane p=new Pane();
			spane.setContent(null);
			p.getChildren().add(pane.getChildren().get(0));
			spane.setContent(p);
			stackpane.getChildren().remove(0);
			stackpane.getChildren().add(spane);	
			SetRectangle();
			spane.getContent().setStyle("-fx-background-color: #F0F4C3;");
		}
		else {
			// Zoom Existe Déja 
			height=height+100;
			width=width+100;
			Zone.setMaxSize(width, height);
		    Zone.setMinSize(width, height);
			Zone.setPrefHeight(height);
			Zone.setPrefWidth(width);
		}
	}
	
	@FXML
    public void ZoomMoins(ActionEvent event) {
		if ( height > 400 && width > 400) {
			height=height-100;
			width=width-100;
			if(height==400 && width==400) {
				// Revenire a Pane 
				pane.getChildren().clear();
				Pane ContentScroll=(Pane)spane.getContent();
				pane.getChildren().add(ContentScroll.getChildren().get(0));
				stackpane.getChildren().remove(0);
				stackpane.getChildren().add(pane);
				SetRectangle();
			}
			Zone.setMaxSize(width, height);
		    Zone.setMinSize(width, height);
			Zone.setPrefHeight(height);
			Zone.setPrefWidth(width);
		}
	}
    
	@FXML
	public void hecnManuelle(ActionEvent event) {
		int xHecn,yHecn;
    	xHecn=HECN.getData().get(0).getXValue().intValue();
    	yHecn=HECN.getData().get(0).getYValue().intValue();
    	HECN.getData().clear();
    	int X=Integer.parseInt(xHECN.getText())
    	,   Y=Integer.parseInt(yHECN.getText());
    	HECN.getData().add(new XYChart.Data<>(X,Y));
    	XYChart.Data<Number,Number> precdHecn=new XYChart.Data<>(xHecn,yHecn);
    	Sensors.getData().add(precdHecn);
    	 XYChart.Data<Number,Number> data=getDataXY(X,Y);
         Sensors.getData().remove(data);
	}
	
	@FXML
    public void Hecn(ActionEvent event) {
    	int xHecn,yHecn;
    	xHecn=HECN.getData().get(0).getXValue().intValue();
    	yHecn=HECN.getData().get(0).getYValue().intValue();
    	HECN.getData().clear();
    	int X=Integer.parseInt(x.getText())
    	,   Y=Integer.parseInt(y.getText());
    	HECN.getData().add(new XYChart.Data<>(X,Y));
    	XYChart.Data<Number,Number> precdHecn=new XYChart.Data<>(xHecn,yHecn);
    	Sensors.getData().add(precdHecn);
    	 XYChart.Data<Number,Number> data=getDataXY(X,Y);
         Sensors.getData().remove(data);
    	xHECN.setText(""+X);
    	yHECN.setText(""+Y);
    }
    
	@FXML
    public void Activer(ActionEvent event) {
    	int X=Integer.parseInt(x.getText())
    	,   Y=Integer.parseInt(y.getText());
    	Sensors.getData().add(new XYChart.Data<>(X,Y));
    	int N=Integer.parseInt(NbValide.getText())+1;
    	NbValide.setText(""+N);
    }
    
	@FXML
    public void Desactiver(ActionEvent event) {
    	int X=Integer.parseInt(x.getText())
    	,   Y=Integer.parseInt(y.getText());
        XYChart.Data<Number,Number> data=getDataXY(X,Y);
        if(data != null) {
        	Sensors.getData().remove(data);
        	int N=Integer.parseInt(NbValide.getText())-1;
        	NbValide.setText(""+N);
        	             }
        
    }
    
    public void OpenCapteurPanel(int X,int Y,double Layoutx,double Layouty) {
    	Capteur.setVisible(true);
    	Historique.setDisable(true);
    	PaneCapteur.setDisable(true);
    	PaneHECN.setDisable(true);
    	RandomPositionnement.setDisable(true);
    	Capteur.setLayoutX(Layoutx+270); 
    	Capteur.setLayoutY(Layouty-20);
    	x.setText(""+X);
    	y.setText(""+Y);
    	XYChart.Data<Number,Number> data=getDataXY(X,Y);
    	if(data==null) {
    		XYChart.Data<Number,Number> station=HECN.getData().get(0);
    	     if (station.getXValue().intValue()==X && station.getYValue().intValue()==Y) {
    	    	 Hecn.setDisable(true);
    	    	 Activer.setDisable(true);
    	    	 Desactiver.setDisable(true);
    	     }
    	     else {
    	    	 Hecn.setDisable(true);	
    	    	 Desactiver.setDisable(true);
    	    	 Activer.setDisable(false);
    	     }
    	}
    	else {
    		Activer.setDisable(true);
    		Hecn.setDisable(false);
    		Desactiver.setDisable(false);
    	}
    }
    
    @FXML
    public void DraggCapteurPanel(MouseEvent event) {
    	Capteur.setOnMousePressed(event1 -> {
            X = event1.getSceneX();
            Y = event1.getSceneY();
        });
        Capteur.setOnMouseDragged(event1 -> {
            Capteur.setLayoutX(event1.getScreenX()-X);
            Capteur.setLayoutY(event1.getScreenY()-Y);
        });
    }
    
    public void OpenRegion(int x,int y,double Layoutx,double Layouty) {
    	Region.setVisible(true);
    	Region.setLayoutX(Layoutx); 
    	Region.setLayoutY(Layouty+40);
    	Region.setText("X= "+x+", Y= "+y);
    }
     
    public void OuvrireConfirmationPanel() throws IOException {
         SuppressionParRegion=true;
         
    	 Stage primaryStage=new Stage();
    	 Parent root = FXMLLoader.load(getClass().getResource("/Demo/ConfirmationPane.fxml"));
    	 Scene scene=new Scene(root);
		 scene.setFill(Color.TRANSPARENT);
		 primaryStage.setScene(scene);
		 primaryStage.initStyle(StageStyle.TRANSPARENT);

					        //drag it here
					        root.setOnMousePressed(event1 -> {
					            X = event1.getSceneX();
					            Y = event1.getSceneY();
					        });
					        root.setOnMouseDragged(event1 -> {
					            primaryStage.setX(event1.getScreenX() - X);
					            primaryStage.setY(event1.getScreenY() - Y);
					        });
					        primaryStage.show();
    	Region.setVisible(false);
    	Capteur.setVisible(false);
    }
    
    @FXML
    public void ExitCapteurPanel(ActionEvent event) {
    	Capteur.setVisible(false);
    	Historique.setDisable(false);
    	PaneCapteur.setDisable(false);
    	PaneHECN.setDisable(false);
    	RandomPositionnement.setDisable(false);
    }
    
	static public void ConfirmerSuppression() {
      	 for (XYChart.Data<Number,Number> region : liste) {
      		 Sensors.getData().remove(region);
      	 }
	}
    
	@SuppressWarnings("unchecked")
	public void CreateScatterChart(int x,int y,int taille) {
		NumberAxis xAxis = new NumberAxis(0,x,taille);
		NumberAxis yAxis = new NumberAxis(0,y,taille);
		Zone = new ScatterChart<Number, Number>(xAxis, yAxis);
		Zone.getData().addAll(HECN,Sensors);
	}
	
	public void setHECN(int x,int y) {
		HECN.getData().add(new XYChart.Data<Number, Number> (x,y));
	}
	
	public void addSensor(int x,int y) {
		Sensors.getData().add(new XYChart.Data<Number,Number>(x,y));
	}
	
	public XYChart.Data<Number,Number> getDataXY(int x,int y){
		for (XYChart.Data<Number,Number> data: Zone.getData().get(1).getData()) {
			if(data.getXValue().intValue()==x && data.getYValue().intValue()==y) return data;
		}
		return null;
	}

	@FXML
	public void SelectRegion(MouseEvent event) {
		if (SuppressionParRegion) {
			NbValide.setText(""+Sensors.getData().size());
			SuppressionParRegion=false;
		}
		
		if(stackpane.getChildren().size() > 0) {
		 SelectionRectangle();
		 
		NumberAxis xAxis=(NumberAxis) Zone.getXAxis();
		NumberAxis yAxis=(NumberAxis) Zone.getYAxis();
		final Node chartBackground = Zone.lookup(".chart-plot-background");
		
	    for (Node n: chartBackground.getParent().getChildrenUnmodifiable()) {
	      if (n != chartBackground && n != xAxis && n != yAxis) {
	        n.setMouseTransparent(true);
	      }
	    }
	    
	    chartBackground.setOnMouseEntered(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	    	Region.setVisible(true);
	      }
	    });

	    chartBackground.setOnMouseMoved(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	    	  int x=pivoter(xAxis.getValueForDisplay(mouseEvent.getX()).doubleValue());
	    	  int y=pivoter(yAxis.getValueForDisplay(mouseEvent.getY()).doubleValue());
	    	   OpenRegion(x,y,mouseEvent.getX(),mouseEvent.getY()); 
	      }
	    });

	    chartBackground.setOnMouseExited(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	        Region.setVisible(false);
	      }
	    });

	    chartBackground.setOnMouseClicked(new EventHandler<MouseEvent>() {
		      @Override public void handle(MouseEvent mouseEvent) {
		    	  Region.setVisible(false);
		    	  int x=pivoter(xAxis.getValueForDisplay(mouseEvent.getX()).doubleValue());
		    	  int y=pivoter(yAxis.getValueForDisplay(mouseEvent.getY()).doubleValue());
		    if (rect.getWidth()==0 && rect.getHeight()==0)	  OpenCapteurPanel(x,y,mouseEvent.getX(),mouseEvent.getY());
			      }
			    });
	    
	    xAxis.setOnMouseEntered(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	        Region.setVisible(true);
	      }
	    });

	    xAxis.setOnMouseMoved(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	    	  int x=pivoter(xAxis.getValueForDisplay(mouseEvent.getX()).doubleValue());
	    	  OpenRegion(x,0,mouseEvent.getX(),mouseEvent.getY());
	      }
	    });

	    xAxis.setOnMouseExited(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	        Region.setVisible(false);
	      }
	    });

	    xAxis.setOnMouseClicked(new EventHandler<MouseEvent>() {
		      @Override public void handle(MouseEvent mouseEvent) {
		    	  Region.setVisible(false);
		    	  int x=pivoter(xAxis.getValueForDisplay(mouseEvent.getX()).doubleValue());
  if (rect.getWidth()==0 && rect.getHeight()==0)   OpenCapteurPanel(x,0,mouseEvent.getX(),mouseEvent.getY());
			      }
			    });
	    
	    yAxis.setOnMouseEntered(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	        Region.setVisible(true);
	      }
	    });

	    yAxis.setOnMouseMoved(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	    	  int y=pivoter(yAxis.getValueForDisplay(mouseEvent.getY()).doubleValue());
	    	  OpenRegion(0,y,mouseEvent.getX(),mouseEvent.getY());
	      }
	    });

	    yAxis.setOnMouseExited(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	        Region.setVisible(false);
	      }
	    });
 
	    yAxis.setOnMouseClicked(new EventHandler<MouseEvent>() {
		      @Override public void handle(MouseEvent mouseEvent) {
		    	  Region.setVisible(false);
		    	  int y=pivoter(yAxis.getValueForDisplay(mouseEvent.getY()).doubleValue());
    	  if (rect.getWidth()==0 && rect.getHeight()==0)	      OpenCapteurPanel(0,y,mouseEvent.getX(),mouseEvent.getY());
			      }
			    });
	}
	}
	
	public void SetRectangle() {
		Node n=stackpane.getChildren().get(0);
		rect = new Rectangle();
		rect.setManaged(false);
		rect.setFill(Color.LIGHTSEAGREEN.deriveColor(0, 1, 1, 0.5));
		 
		rectInvisible=new Rectangle();
	    rectInvisible.setVisible(false);
		
		if(n instanceof ScrollPane) {
			Pane p=(Pane)spane.getContent();
			p.getChildren().add(rect);
		}
		else {
			pane.getChildren().add(rect);
		}
	}
	
	public void SelectionRectangle() {
        
		  final ObjectProperty<Point2D> mouseAnchor1 = new SimpleObjectProperty<>();
		  final ObjectProperty<Point2D> mouseAnchor2 = new SimpleObjectProperty<>();
		  
		  final Node chartBackground = Zone.lookup(".chart-plot-background");
	        Zone.setOnMousePressed(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	            	Region.setVisible(false);
	            	Capteur.setVisible(false);
	                mouseAnchor1.set(new Point2D(event.getX(), event.getY()));
	                rect.setX(event.getScreenX());
	                rect.setY(event.getScreenY());
	                rect.setWidth(0);
	                rect.setHeight(0);
	            }
	        });
	        Zone.setOnMouseDragged(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	            	Region.setVisible(false);
	            	Capteur.setVisible(false);
	                double x = event.getX();
	                double y = event.getY();
	                rect.setX(Math.min(x, mouseAnchor1.get().getX()));
	                rect.setY(Math.min(y, mouseAnchor1.get().getY()));
	                rect.setWidth(Math.abs(x - mouseAnchor1.get().getX()));
	                rect.setHeight(Math.abs(y - mouseAnchor1.get().getY()));
	            }
	        });
	        
	        chartBackground.setOnMousePressed(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	            	Region.setVisible(false);
	            	Capteur.setVisible(false);
	                mouseAnchor2.set(new Point2D(event.getX(), event.getY()));
	            }
	        });
	        chartBackground.setOnMouseDragged(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	            	Region.setVisible(false);
	            	Capteur.setVisible(false);
	                double x=Math.min(event.getX(), mouseAnchor2.get().getX());
	                double y=Math.min(event.getY(), mouseAnchor2.get().getY());
	                rectInvisible.setX(x);
	                rectInvisible.setY(y);
	                rectInvisible.setWidth(Math.abs(event.getX() - mouseAnchor2.get().getX()));
	                rectInvisible.setHeight(Math.abs(event.getY() - mouseAnchor2.get().getY()));
	               
	            }
	        });
	        chartBackground.setOnMouseReleased(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	            	if(rect.getWidth()> 0 && rect.getHeight()>0) {
                       RegionInsideRectangle();
	            	}
	            }
	        });
	}
	
	public int pivoter(double x) {
		x=x*10;
	    int X=(int)x;
	    int i=X%10;
	    X=X/10;
	    if (i>=5) return X+1;
	    return X;
	}
	
	public void RegionInsideRectangle() {
		
		liste.clear();
		
		NumberAxis xAxis=(NumberAxis) Zone.getXAxis();
		NumberAxis yAxis=(NumberAxis) Zone.getYAxis();
		
		for (XYChart.Data<Number,Number> region : Sensors.getData()) {
	    	Bounds pos = region.getNode().getBoundsInParent();
	    	double x=(pos.getMinX()+pos.getMaxX())/2;
	    	double y=(pos.getMinY()+pos.getMaxY())/2;
	    	if (rectInvisible.contains(x,y)) { 
	    		int xData=pivoter(xAxis.getValueForDisplay(x).doubleValue());
		        int yData=pivoter(yAxis.getValueForDisplay(y).doubleValue());
	    	 XYChart.Data<Number,Number> data=getDataXY(xData,yData);
	    	 if(data != null) {
	    		 liste.add(data);
	    	 }
	    	}
	      }
        try {
			OuvrireConfirmationPanel();
		} catch (IOException e) { e.printStackTrace();}
	}
	 
	public void addElementsForChart(int x,int y) {
		// Saisir HECN 
		int xhecn,yhecn;
		if (!xHECN.getText().equals("") && !yHECN.getText().equals("")) {
			 xhecn=Integer.parseInt(xHECN.getText());
			 yhecn=Integer.parseInt(yHECN.getText());
			
		}else {
			xhecn=(int) x/2;
			yhecn=(int) y/2;
		}
		setHECN(xhecn, yhecn);
		xHECN.setText(""+xhecn);
		yHECN.setText(""+yhecn);
		
		// Saisir touts les Solution 
		for (int i=0;i<=x;i++) {
			for (int j=0;j<=y;j++) {
				if (i !=xhecn|| j!=yhecn) {
					Sensors.getData().add(new XYChart.Data<>(i,j));
				}
			}
		}
	}
	
	/*---------------------------FICHIER DATASET -----------------------------*/
	@SuppressWarnings("unchecked")
	public static void Display() { 
		ArrayList<zoneWSN> zones;
		try{
		FileInputStream fis= new FileInputStream(BD);
		 in=new ObjectInputStream(fis);
			if((zones=(ArrayList<zoneWSN>)in.readObject())!=null) ListeZones=zones; 
		}catch (ClassNotFoundException | IOException e) {}
	}

	public static void Save() {
		FileOutputStream fis;
		try {
			fis = new FileOutputStream(BD);
			out=new ObjectOutputStream(fis);
		    out.writeObject(ListeZones);	
		    out.flush();
		    out.close();
		} catch (IOException e) {}
	}
	
	public void ChargerDataSet() throws IOException {
		Display();
		Node nodes;
		for (zoneWSN zone: ListeZones) {
			FXMLLoader loader = new FXMLLoader();
			nodes = loader.load(getClass().getResource("Historique.fxml").openStream());
			ControllerItemHistorique controler = (ControllerItemHistorique) loader.getController();
			controler.NbEmpValide.setText(""+zone.getTotalePoints());
			controler.RayComm.setText(""+zone.getRcomm());
			controler.RayCouv.setText(""+zone.getRsens());
			controler.XYhecn.setText("["+zone.getHECN().getX()+","+zone.getHECN().getY()+"]");
			controler.XYzone.setText(zone.getxAxis()+" x "+zone.getyAxis());
			controler.Zone=zone;
			Historique.getChildren().add(nodes);
		}
	}
	
	/*-----------------------------------------------------------------------*/
}
