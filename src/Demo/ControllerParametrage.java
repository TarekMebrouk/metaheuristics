package Demo;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Configuration.*;

public class ControllerParametrage implements Initializable{

	double X,Y;
    zoneWSN zone;
    ReglageParametres Param;
	
    @FXML Pane Parametrage;
    
    @FXML
    private TextField T;

    @FXML
    private TextField AlphaRS;

    @FXML
    private TextField AgN;

    @FXML
    private TextField P;

    @FXML
    private TextField BBoN;

    @FXML
    private TextField E;

    @FXML
    private TextField I;

    @FXML
    private TextField AlphaBBo;
    
    @FXML
    private TextField beta;
    
    @FXML
    private TextField NombreEvaluation;
    
    @FXML
    private RadioButton refAlea;

    @FXML
    private RadioButton refPalier;

    @FXML
    private RadioButton refMono;
    
    @FXML
    private RadioButton rempAlea;

    @FXML
    private RadioButton rempPop;

    @FXML
    private RadioButton rempInc;

    private ToggleGroup typeRefroidissement;
    private ToggleGroup typeRemplacement;
    
    public void DisplayTypeGenetique(TypeAlgorithmeGenetic type) {
    	if(type.compareTo(TypeAlgorithmeGenetic.remplacement_incremental)==0) typeRemplacement.selectToggle(rempInc);
    	else typeRemplacement.selectToggle(rempPop);
    }
    
    public void DisplayTypeRecuit(TypeRecuitSimule type) {
    	if(type.compareTo(TypeRecuitSimule.Reduction_par_palier)==0) typeRefroidissement.selectToggle(refPalier);
    	else typeRefroidissement.selectToggle(refMono);
    }
    
    @FXML
    void CalculeParamBBO(ActionEvent event) {
    	Param.PreparerInitSolution();
        Param.deepLocalSearchBBO();
        AlphaBBo.setText(""+Param.getBboP().getα());
        BBoN.setText(""+Param.getBboP().getN());
        E.setText(""+Param.getBboP().getE());
        I.setText(""+Param.getBboP().getI());
    }

    @FXML
    void CalculeParamGenetic(ActionEvent event) {
    	if(this.typeRemplacement.getSelectedToggle()==null) {
    		Alert alt = new Alert(Alert.AlertType.WARNING);
    		alt.setTitle("Type de remplacement manquant");
    		alt.setContentText("Vous devez choisir le type de remplacement à utiliser ");
    		alt.setHeaderText(null);
    		alt.showAndWait();
    	}
    	else {
    		        TypeAlgorithmeGenetic typeAg = null;
    		if(this.typeRemplacement.getSelectedToggle().equals(this.rempAlea)) {
        		//remplacement aléatoire 
    			int k=(int)(Math.random()*2);
    			if(k==0)  typeAg=TypeAlgorithmeGenetic.remplacement_incremental;
    			else typeAg=TypeAlgorithmeGenetic.remplacement_population;
        	}
        	if(this.typeRemplacement.getSelectedToggle().equals(this.rempPop)) {
        		//remplacement par population
        		typeAg=TypeAlgorithmeGenetic.remplacement_population;
        	}
        	if(this.typeRemplacement.getSelectedToggle().equals(this.rempInc)) {
        		//remplacement incrémental
        		typeAg=TypeAlgorithmeGenetic.remplacement_incremental;
        	}
        	Param.PreparerInitSolution();
        	Param.deepLocalSearchGenetic(typeAg);
            AgN.setText(""+Param.getAgP().getN());
            P.setText(""+Param.getAgP().getP());
            DisplayTypeGenetique(typeAg);
    	}
    }

    @FXML
    void CalculeParamRecuit(ActionEvent event) {
    	if(this.typeRefroidissement.getSelectedToggle()==null) {
    		Alert alt = new Alert(Alert.AlertType.WARNING);
    		alt.setTitle("Type de refroidissement manquant");
    		alt.setContentText("Vous devez choisir le type de refroidissement à utiliser ");
    		alt.setHeaderText(null);
    		alt.showAndWait();
    	}
    	else {
    		TypeRecuitSimule typeRs = null;
    		if(this.typeRefroidissement.getSelectedToggle().equals(this.refAlea)) {
        		//refroidissement aléatoire
    			int type;
    			type = (int) (Math.random() * 2);
    			if(type==0) typeRs=TypeRecuitSimule.Reduction_par_palier;
    			else typeRs=TypeRecuitSimule.Reduction_monotone;
        	}
        	if(this.typeRefroidissement.getSelectedToggle().equals(this.refPalier)) {
        		//refroidissement par palier
        		typeRs=TypeRecuitSimule.Reduction_par_palier;
        	}
        	if(this.typeRefroidissement.getSelectedToggle().equals(this.refMono)) {
        		//refroidissement monotone
        		typeRs=TypeRecuitSimule.Reduction_monotone;
        	}
        	Param.PreparerInitSolution();
        	Param.deepLocalSearchRecuitSimule(typeRs);
            T.setText(""+Param.getRsP().getT());
            AlphaRS.setText(""+Param.getRsP().getAlpha());
            beta.setText(""+Param.getRsP().getBeta()); 
            DisplayTypeRecuit(typeRs);
    	}
    	
    }

    @FXML
    void playBBo(ActionEvent event) throws IOException {
    	if(BBoN.getText().compareTo("")==0 || E.getText().compareTo("")==0 || I.getText().compareTo("")==0 || AlphaBBo.getText().compareTo("")==0) {
    		Alert alt = new Alert(Alert.AlertType.ERROR);
    		alt.setTitle("Parametres manquants");
    		alt.setContentText("Vous devez remplir les parametres à utiliser ou bien les calculer automatiquement ");
    		alt.setHeaderText(null);
    		alt.showAndWait();
    	}
    	else {
    		// PARAMETRES 
        	int NBBo=Integer.parseInt(BBoN.getText());
        	double BboAlpha=Double.parseDouble(AlphaBBo.getText());
        	double e=Double.parseDouble(E.getText());
        	double i=Double.parseDouble(I.getText());
        	BBoP paramBBo=new BBoP(NBBo, e, i, BboAlpha);
        	ControllerHome.parametres.setBboP(paramBBo);
        	
        	// NOMBRE EVALUATION 
        	int N=Integer.parseInt(NombreEvaluation.getText());
        	
        	// CREATE NEW METAHEURISTIQUE 
        	MetaEsclave meta=new BBoM(NBBo,e,i,zone,N,BboAlpha);
         	meta.function();
        	meta.getMeilleurSOl().Affichage();
        	// OPEN EXPERIMENTATION 
        	OpenExperimentation(meta);
    	}
    }

    @FXML
    void playGenetic(ActionEvent event) throws IOException {
    	if(AgN.getText().compareTo("")==0 || P.getText().compareTo("")==0) {
    		Alert alt = new Alert(Alert.AlertType.ERROR);
    		alt.setTitle("Parametres manquants");
    		alt.setContentText("Vous devez remplir les parametres à utiliser ou bien les calculer automatiquement ");
    		alt.setHeaderText(null);
    		alt.showAndWait();
    	}
    	else {
    		if(this.typeRemplacement.getSelectedToggle()==null) {
        		Alert alt = new Alert(Alert.AlertType.WARNING);
        		alt.setTitle("Type de remplacement manquant");
        		alt.setContentText("Vous devez choisir le type de remplacement à utiliser ");
        		alt.setHeaderText(null);
        		alt.showAndWait();
        	}
    		else {
    			// Type 
    			  TypeAlgorithmeGenetic typeAg = null;
    	    		if(this.typeRemplacement.getSelectedToggle().equals(this.rempAlea)) {
    	        		//remplacement aléatoire 
    	    			int k=(int)(Math.random()*2);
    	    			if(k==0)  typeAg=TypeAlgorithmeGenetic.remplacement_incremental;
    	    			else typeAg=TypeAlgorithmeGenetic.remplacement_population;
    	        	}
    	        	if(this.typeRemplacement.getSelectedToggle().equals(this.rempPop)) {
    	        		//remplacement par population
    	        		typeAg=TypeAlgorithmeGenetic.remplacement_population;
    	        	}
    	        	if(this.typeRemplacement.getSelectedToggle().equals(this.rempInc)) {
    	        		//remplacement incrémental
    	        		typeAg=TypeAlgorithmeGenetic.remplacement_incremental;
    	        	}
    			// PARAMETRES 
            	int Ngenetic=Integer.parseInt(AgN.getText());
            	double Pgenetic=Double.parseDouble(P.getText());
            	geneticP paramAg=new geneticP(Ngenetic,Pgenetic,typeAg);
            	ControllerHome.parametres.setAgP(paramAg);
            	
            	// NOMBRE EVALUATION 
            	int N=Integer.parseInt(NombreEvaluation.getText());
            	
            	// CREATE NEW METAHEURISTIQUE 
            	MetaEsclave meta=new geneticM(N,Ngenetic,Pgenetic,zone,typeAg);
            	meta.function();
            	meta.getMeilleurSOl().Affichage();
            	// OPEN EXPERIMENTATION 
            	OpenExperimentation(meta);
    		}
    	}
    }

    @FXML
    void playRecuit(ActionEvent event) throws IOException {
    	if(T.getText().compareTo("")==0 || AlphaRS.getText().compareTo("")==0 || beta.getText().compareTo("")==0) {
    		Alert alt = new Alert(Alert.AlertType.ERROR);
    		alt.setTitle("Parametres manquants");
    		alt.setContentText("Vous devez remplir les parametres à utiliser ou bien les calculer automatiquement ");
    		alt.setHeaderText(null);
    		alt.showAndWait();
    	}
    	else {
    		if(this.typeRefroidissement.getSelectedToggle()==null) {
        		Alert alt = new Alert(Alert.AlertType.WARNING);
        		alt.setTitle("Type de refroidissement manquant");
        		alt.setContentText("Vous devez choisir le type de refroidissement à utiliser ");
        		alt.setHeaderText(null);
        		alt.showAndWait();
        	}
    		else {
    			// Type 
    			TypeRecuitSimule typeRs = null;
        		if(this.typeRefroidissement.getSelectedToggle().equals(this.refAlea)) {
            		//refroidissement aléatoire
        			int type;
        			type = (int) (Math.random() * 2);
        			if(type==0) typeRs=TypeRecuitSimule.Reduction_par_palier;
        			else typeRs=TypeRecuitSimule.Reduction_monotone;
            	}
            	if(this.typeRefroidissement.getSelectedToggle().equals(this.refPalier)) {
            		//refroidissement par palier
            		typeRs=TypeRecuitSimule.Reduction_par_palier;
            	}
            	if(this.typeRefroidissement.getSelectedToggle().equals(this.refMono)) {
            		//refroidissement monotone
            		typeRs=TypeRecuitSimule.Reduction_monotone;
            	}
    			// PARAMETRES 
    	    	double Temperature=Double.parseDouble(T.getText());
    	    	double recuitAlpha=Double.parseDouble(AlphaRS.getText());
    	    	double recuitBeta=Double.parseDouble(beta.getText());
    	    	recuitSimuleP paramRs=new recuitSimuleP(Temperature,recuitAlpha,recuitBeta,typeRs);
    	    	ControllerHome.parametres.setRsP(paramRs);
    	    	
    	    	// NOMBRE EVALUATION 
    	    	int N=Integer.parseInt(NombreEvaluation.getText());
    	    	
    	    	// CREATE NEW METAHEURISTIQUE 
    	    	MetaEsclave meta=new recuitSimuleM(new Solution(zone),Temperature,N,recuitAlpha,recuitBeta,typeRs);
    	    	meta.function();
    	    	meta.getMeilleurSOl().Affichage();
    	    	// OPEN EXPERIMENTATION 
    	    	OpenExperimentation(meta);
    		}
    	}
    }

    @FXML
    void LancerExecution(ActionEvent event) throws IOException {
    	if (NombreEvaluation.getText().compareTo("")==0) {
    		Alert alt = new Alert(Alert.AlertType.WARNING);
    		alt.setTitle("Information manquante");
    		alt.setContentText("Vous devez saisir le nombre d'evaluations à effectuer ");
    		alt.setHeaderText(null);
    		alt.showAndWait();
    	}
    	else {
    		if (T.getText().compareTo("")==0 || AlphaRS.getText().compareTo("")==0 || beta.getText().compareTo("")==0 || AgN.getText().compareTo("")==0 || P.getText().compareTo("")==0 || BBoN.getText().compareTo("")==0 || E.getText().compareTo("")==0 || I.getText().compareTo("")==0 || AlphaBBo.getText().compareTo("")==0) {
    			Alert alt = new Alert(Alert.AlertType.ERROR);
        		alt.setTitle("Parametres manquants");
        		alt.setContentText("Vous devez remplir les parametres à utiliser ou bien les calculer automatiquement ");
        		alt.setHeaderText(null);
        		alt.showAndWait();
    		}
    		else {
    			if(this.typeRefroidissement.getSelectedToggle()==null) {
            		Alert alt = new Alert(Alert.AlertType.WARNING);
            		alt.setTitle("Type de refroidissement manquant");
            		alt.setContentText("Vous devez choisir le type de refroidissement à utiliser ");
            		alt.setHeaderText(null);
            		alt.showAndWait();
            	}
        		else {
        			if(this.typeRemplacement.getSelectedToggle()==null) {
                		Alert alt = new Alert(Alert.AlertType.WARNING);
                		alt.setTitle("Type de remplacement manquant");
                		alt.setContentText("Vous devez choisir le type de remplacement à utiliser ");
                		alt.setHeaderText(null);
                		alt.showAndWait();
                	}
            		else {
            			
            			 // RECUIT SIMULE PARAMETRES  
            			// Type 
            			TypeRecuitSimule typeRs = null;
                		if(this.typeRefroidissement.getSelectedToggle().equals(this.refAlea)) {
                    		//refroidissement aléatoire
                			int type;
                			type = (int) (Math.random() * 2);
                			if(type==0) typeRs=TypeRecuitSimule.Reduction_par_palier;
                			else typeRs=TypeRecuitSimule.Reduction_monotone;
                    	}
                    	if(this.typeRefroidissement.getSelectedToggle().equals(this.refPalier)) {
                    		//refroidissement par palier
                    		typeRs=TypeRecuitSimule.Reduction_par_palier;
                    	}
                    	if(this.typeRefroidissement.getSelectedToggle().equals(this.refMono)) {
                    		//refroidissement monotone
                    		typeRs=TypeRecuitSimule.Reduction_monotone;
                    	}
            			// Paramétres 
            	    	double Temperature=Double.parseDouble(T.getText());
            	    	double recuitAlpha=Double.parseDouble(AlphaRS.getText());
            	    	double recuitBeta=Double.parseDouble(beta.getText());
            	    	recuitSimuleP paramRs=new recuitSimuleP(Temperature,recuitAlpha,recuitBeta,typeRs);
            	    	
            	    	// GENETIC PARAMETRES
            	    	// Type 
            	    	TypeAlgorithmeGenetic typeAg = null;
        	    		if(this.typeRemplacement.getSelectedToggle().equals(this.rempAlea)) {
        	        		//remplacement aléatoire 
        	    			int k=(int)(Math.random()*2);
        	    			if(k==0)  typeAg=TypeAlgorithmeGenetic.remplacement_incremental;
        	    			else typeAg=TypeAlgorithmeGenetic.remplacement_population;
        	        	}
        	        	if(this.typeRemplacement.getSelectedToggle().equals(this.rempPop)) {
        	        		//remplacement par population
        	        		typeAg=TypeAlgorithmeGenetic.remplacement_population;
        	        	}
        	        	if(this.typeRemplacement.getSelectedToggle().equals(this.rempInc)) {
        	        		//remplacement incrémental
        	        		typeAg=TypeAlgorithmeGenetic.remplacement_incremental;
        	        	}
            	    	// Paramétres 
            	    	int Ngenetic=Integer.parseInt(AgN.getText());
            	    	double Pgenetic=Double.parseDouble(P.getText());
            	    	geneticP paramAg=new geneticP(Ngenetic,Pgenetic,typeAg);
            	    	
            	    	// BBO PARAMETRES 
            	    	int NBBo=Integer.parseInt(BBoN.getText());
            	    	double BboAlpha=Double.parseDouble(AlphaBBo.getText());
            	    	double e=Double.parseDouble(E.getText());
            	    	double i=Double.parseDouble(I.getText());
            	    	BBoP paramBBo=new BBoP(NBBo, e, i, BboAlpha);
            	    	
            	    	// NOMBRE EVALUATION 
            	    	int N=Integer.parseInt(NombreEvaluation.getText());
            	    	
            	    	// PARAMETRE GENERALE 
            	    	Param=new ReglageParametres(zone, paramRs, paramAg, paramBBo);
            	    	MetaMaitre Maitre=new MetaMaitre(zone,Param,N);
            	    	Maitre.PreparerInitSolutions();
            	    	Maitre.processus(typeRs,typeAg);
            	    	Maitre.getFinalSOl().Affichage();
            	    	
            	    	// Set ControllerHome 
            	    	ControllerHome.parametres=Param;
            	    	ControllerHome.metaMaitre=Maitre;
            	    	
            	    	// OPEN EXPERIMENTATION 
            	    	OpenExperimentation(Maitre);
            		}
        		}
    		}
    	}
    }

    @SuppressWarnings("unchecked")
	public static <Type> BubbleChart<Number,Number> DisplaySolution(Pane pane,Type Maitre) {
    	Solution S;
    	if(Maitre instanceof MetaMaitre) {
    		MetaMaitre M=(MetaMaitre)Maitre;
    		S=M.getFinalSOl();
    	}
    	else {
    		MetaEsclave M=(MetaEsclave)Maitre;
    		S=M.getMeilleurSOl();
    	}
        // Create BubbleChart 
    	BubbleChart<Number,Number> ChartSolutions=CreateBubbleChart(S.getZone().getxAxis(),S.getZone().getyAxis(),1);
    	// Display Solution 
    	Series<Number,Number> HECN=new Series<>();
    	HECN.setName("Couverture HECN");
    	Series<Number,Number> Sensors=new Series<>();
    	Sensors.setName("Couverture Capteurs");
    	
    	// Set HECN 
    	HECN.getData().add(new XYChart.Data<>(S.getZone().getHECN().getX(),S.getZone().getHECN().getY(),S.getZone().getRsens()));
    	// Set Sensors 
    	ArrayList<Region> Regions=S.getListeRegions();
    	for (Region region : Regions) {
    	if (region.isOn())	Sensors.getData().add(new XYChart.Data<>(region.getX(),region.getY(),S.getZone().getRsens()));
    	}
    	ChartSolutions.getData().addAll(HECN,Sensors);
    	
    	// Add Points of Sensors 
    	addNoeuds(ChartSolutions,S);
    	
		pane.getChildren().add(ChartSolutions);
		
		// Adding CSS 
		ChartSolutions.setPrefHeight(448);
		ChartSolutions.setPrefWidth(635);
		ChartSolutions.setMaxSize(635, 448);
		ChartSolutions.setMinSize(635, 448);
		ChartSolutions.setLegendVisible(true);
		
		return ChartSolutions;
    }
	
    public static BubbleChart<Number,Number> CreateBubbleChart(int x,int y,int taille) {
		NumberAxis xAxis = new NumberAxis(0,x,taille);
		NumberAxis yAxis = new NumberAxis(0,y,taille);
		BubbleChart<Number,Number> Zone;
		Zone = new BubbleChart<Number, Number>(xAxis, yAxis);
		Zone.setId("DisplaySolutionChart");
		return Zone;
	}
    
    public <Type> void OpenExperimentation(Type Meta) throws IOException {
    	Solution S;
    	if(Meta instanceof MetaMaitre) {
    		MetaMaitre Maitre=(MetaMaitre)Meta;
    		S=Maitre.getFinalSOl();
    	}
    	else {
    		MetaEsclave Maitre=(MetaEsclave)Meta;
    		S=Maitre.getMeilleurSOl();
    	}
    	// OPEN EXPERIMENTATION 
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Experimentation.fxml"));
		loader.load();
		ControllerExperimentation controler= (ControllerExperimentation)loader.getController();
		// Set Fintess 
		controler.fitness.setText(""+S.getFitness());
		// Set Nombre de Capteurs 
		controler.NombreCapteurs.setText(""+S.getNombreCapteurs());
		// Set Taux de Couverture 
		double couverture=(S.CoveredPoints()*100)/S.getZone().getTotalePoints();
	    controler.TauxCouv.setText(couverture+" %");
		// Display solution 
	    controler.pane.getChildren().clear();
	    controler.ChartSolutions=DisplaySolution(controler.pane,Meta);
	    controler.ChartSolutions.lookup(".chart-vertical-grid-lines").setStyle("\n" + 
	    		" 	-fx-stroke: black;\n" + 
	    		"    -fx-stroke-width: 1; ");
	    controler.ChartSolutions.lookup(".chart-horizontal-grid-lines").setStyle("\n" + 
	    		" 	-fx-stroke: black;\n" + 
	    		"    -fx-stroke-width: 1; ");
	    // Set Meta Maitre 
	    if(Meta instanceof MetaMaitre) {
    		MetaMaitre Maitre=(MetaMaitre)Meta;
    		  controler.MetaHeuristique=Maitre;
    	}
    	else {
    		  controler.MetaHeuristique=null;
    	}
		
		Parent root = loader.getRoot();
		Scene scene = AgN.getScene();
		root.translateXProperty().set(scene.getWidth());
		ControllerHome.ContainerPane.getChildren().add(root);

	        Timeline timeline = new Timeline();
	        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
	        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
	        timeline.getKeyFrames().add(kf);
	        timeline.setOnFinished(t -> {
	       	ControllerHome.ContainerPane.getChildren().remove(Parametrage);
	        });
	        timeline.play();
	        ControllerHome.NomEtape.setText("PHASE D'EXPERIMENTATION");
	        ControllerHome.ExpermButton.setDisable(false);
    }
    
    @SuppressWarnings("unchecked")
	public static void addNoeuds(BubbleChart<Number,Number> chart,Solution S) {
    	// Display Solution 
    	Series<Number,Number> HECN=new Series<>();
    	HECN.setName("      Hecn");
    	Series<Number,Number> Sensors=new Series<>();
    	Sensors.setName("      Noeud");
    	
    	// Calcule width Of Noeud 
    	// 1- HECN 
    	double widthHecn=((double)S.getZone().getTotalePoints() * 0.1 )/ 120;
    	// 2- Simple Sensor
    	double widthSensor=((double)S.getZone().getTotalePoints() * 0.08 )/ 120;
    	// Set HECN 
    	HECN.getData().add(new XYChart.Data<>(S.getZone().getHECN().getX(),S.getZone().getHECN().getY(),widthHecn));
    	// Set Sensors 
    	ArrayList<Region> Regions=S.getListeRegions();
    	for (Region region : Regions) {
    	if (region.isOn())	Sensors.getData().add(new XYChart.Data<>(region.getX(),region.getY(),widthSensor));
    	}
    	chart.getData().addAll(HECN,Sensors);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		typeRefroidissement = new ToggleGroup();
		typeRemplacement = new ToggleGroup();
		
		this.refAlea.setToggleGroup(typeRefroidissement);
		this.refMono.setToggleGroup(typeRefroidissement);
		this.refPalier.setToggleGroup(typeRefroidissement);
		
		this.rempAlea.setToggleGroup(typeRemplacement);
		this.rempPop.setToggleGroup(typeRemplacement);
		this.rempInc.setToggleGroup(typeRemplacement);
		// Changer CSS du BUTTON 
		ControllerHome.ChangerCssButton(ControllerHome.ParamButton);
        // Charger Précedent Recherches 
		if (ControllerHome.Zone != null) {
			zone=ControllerHome.Zone;
			
			if (ControllerHome.parametres != null) {
				Param=ControllerHome.parametres;
				if (Param.getRsP() !=null &&  Param.getRsP().getT() > 0) {
					T.setText(""+Param.getRsP().getT());
			        AlphaRS.setText(""+Param.getRsP().getAlpha());
			        beta.setText(""+Param.getRsP().getBeta());
			        DisplayTypeRecuit(Param.getRsP().getType());
				}
				if (Param.getAgP() != null && Param.getAgP().getN() > 0) {
					AgN.setText(""+Param.getAgP().getN());
			        P.setText(""+Param.getAgP().getP());
			        DisplayTypeGenetique(Param.getAgP().getType());
				}
				if (Param.getBboP() !=null && Param.getBboP().getN() > 0) {
					 AlphaBBo.setText(""+Param.getBboP().getα());
				        BBoN.setText(""+Param.getBboP().getN());
				        E.setText(""+Param.getBboP().getE());
				        I.setText(""+Param.getBboP().getI());
				}
				if (Param.getInitSolutions().isEmpty()) {
					Param=new ReglageParametres(zone);
				}
			} else {
			        Param=new ReglageParametres(zone);
			}	
		}
		if(ControllerHome.metaMaitre != null) NombreEvaluation.setText(""+ControllerHome.metaMaitre.getNombreEvaluations());
		
		
	}
    
}
