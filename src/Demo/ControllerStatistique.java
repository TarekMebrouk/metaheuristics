package Demo;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import Configuration.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class ControllerStatistique implements Initializable{

	MetaMaitre Maitre;
	
	@FXML Pane Statistique;
	
    @FXML Label fitness;

    @FXML Label NombreCapteurs;

    @FXML Label Couverture;

    @FXML Pane pane;
    
    @FXML Pane BarChartPanel;
    
    @FXML Pane PieChartPanel;

	private ScheduledExecutorService scheduledExecutorService;

	private int cpt;

	private Series<Number, Number> recuitS=null;

	private Series<Number, Number> geneticM=null;

	private Series<Number, Number> BBoM=null;

	XYChart.Series<String,Number> NumbersSensors=null;
	
	XYChart.Series<String,Number> Evaluation=null;
	
	XYChart.Series<String,Number> CoveredPoints=null;
	
	  @SuppressWarnings("unchecked")
    public void SimulationExecution() {
    	// clear 
    	pane.getChildren().clear();
    	if (recuitS != null)recuitS.getData().clear();
    	if (geneticM != null) geneticM.getData().clear();
    	if (BBoM != null) BBoM.getData().clear();
    	cpt=0;
    	fitness.setText(""); NombreCapteurs.setText(""); Couverture.setText("");
    	// define Max Values 
    	int xMax=Maitre.getSolutions().size();
    	int yMax=(int) (Maitre.getFinalSOl().getFitness()+100);
    	
    	 //defining the axes
        final NumberAxis xAxis = new NumberAxis(0,xMax,1); 
        final NumberAxis yAxis = new NumberAxis(0,yMax,20);
        
        xAxis.setAnimated(false); // axis animations are removed
        yAxis.setLabel("fitness");
        
        yAxis.setAnimated(false); // axis animations are removed

        //creating the line chart with two axis created above
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setAnimated(false); // disable animations

        //defining a series to display data
        recuitS = new XYChart.Series<>();
        recuitS.setName("Recuit Simulé");
        geneticM = new XYChart.Series<>();
        geneticM.setName("  Génétique");
        BBoM = new XYChart.Series<>();
        BBoM.setName("    BBo");

        // add series to chart
        lineChart.getData().addAll(recuitS,geneticM,BBoM);
        lineChart.setMaxSize(500, 295);
        lineChart.setMinSize(500, 295);
        lineChart.setPrefHeight(295);
        lineChart.setPrefWidth(500);
        
        pane.getChildren().add(lineChart);
    
        // Set Liste Of solution 
        ArrayList<SavingSolution> Solutions=Maitre.getSolutions();
        
        // setup a scheduled executor to periodically put data into the chart
         scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // put dummy data onto graph per second
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            	// Update the chart
                 Platform.runLater(() -> {
                	 if (cpt<Solutions.size()) {
            		SavingSolution sol=Solutions.get(cpt);
            		// set Fitness
            		fitness.setText(""+sol.getSolution().getFitness());
            		// set Nombre Capteur 
            		NombreCapteurs.setText(""+sol.getSolution().getNombreCapteurs());
            		// set Taux de Couverture 
            		double TauxCouv=(sol.getSolution().CoveredPoints()*100)/sol.getSolution().getZone().getTotalePoints();
            	    Couverture.setText(TauxCouv+" %");
            	    // set Point in LineChart
            	    switch(sol.getTypeMeta()) {
            	    case 1:
            	    	recuitS.getData().add(new XYChart.Data<>(sol.getRang(),sol.getSolution().getFitness()));
            	    	break;
            	    case 2: 
            	    	geneticM.getData().add(new XYChart.Data<>(sol.getRang(),sol.getSolution().getFitness()));
            	    	break;
            	    case 3:
            	    	BBoM.getData().add(new XYChart.Data<>(sol.getRang(),sol.getSolution().getFitness()));
            	    	break;
            	    default: 
            	    	break;
            	    }
            	    cpt++;
                	 }
                 });  
        },0 ,1500, TimeUnit.MILLISECONDS);
    }

  @SuppressWarnings("unchecked")
	public void SetBarChart() {

		// define Max Values 
    	int yMax=(int) (Maitre.getFinalSOl().getFitness()+100);
    	
    	 //defining the axes
		 final CategoryAxis         xAxis = new CategoryAxis();
	     final NumberAxis           yAxis = new NumberAxis(0,yMax,20);
	     final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
	        xAxis.setLabel("Métaheuristique");       
	        yAxis.setLabel("Evaluation");
	        
	        NumbersSensors = new XYChart.Series<String, Number>();
	        NumbersSensors.setName("Nombre de Capteurs"); 
	        Evaluation = new XYChart.Series<String, Number>();
	        Evaluation.setName("fitness"); 
	        CoveredPoints= new XYChart.Series<String, Number>();
	        CoveredPoints.setName("Couverture"); 
	        
	        // Remplissage BarChart 
	        EvaluateMeta(1);
	        EvaluateMeta(2);
	        EvaluateMeta(3);
	        
	        // Remplire BarChart 
	        bc.getData().addAll(NumbersSensors,CoveredPoints,Evaluation);
	        bc.setPrefSize(360,235);
	        bc.setMaxSize(360,235);
	        bc.setMinSize(360,235);
	        
	        bc.lookup(".chart-legend-item").setStyle("    -fx-text-fill: #7cb342;\n" + 
	        		" -fx-font-size: 1em;");
	        // Add BarChart to Pane for Display
	        BarChartPanel.getChildren().add(bc);
	}
	
	public void SetPieChart() {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		PieChart.Data recuit=new PieChart.Data("Recuit Simulé",getTauxParticipation(1));
		PieChart.Data genetic=new PieChart.Data("Génétique",getTauxParticipation(2));
		PieChart.Data BBO=new PieChart.Data("BBo",getTauxParticipation(3));
		pieChartData.addAll(recuit,genetic,BBO);
        final PieChart chart = new PieChart(pieChartData);
        chart.setPrefSize(360,155);
        chart.setMaxSize(360,155);
        chart.setMinSize(360,155);
        PieChartPanel.getChildren().add(chart);
	}
	
	public double getTauxParticipation(int type) {
		int N=0;
		  for (SavingSolution s: Maitre.getSolutions()) {
	        	if (s.getTypeMeta()==type) {
	        		N++;
	        	}
		  }
		double Taux=(double)(N*100)/Maitre.getSolutions().size();
		return Taux;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void EvaluateMeta(int type) {
		 int cpt=0,N=0;
		 double fx=0,couv=0;
	        // Remplire le Nombre de Capteurs 
	        // Recuit Simulé 
	        for (SavingSolution s: Maitre.getSolutions()) {
	        	if (s.getTypeMeta()==type) {
	        		cpt++; 
	        		N=N+s.getSolution().getNombreCapteurs();
	        		fx=fx+s.getSolution().getFitness();
	        		couv=couv+((s.getSolution().CoveredPoints()*100)/s.getSolution().getZone().getTotalePoints());
	        	}
	        }
	        double Ntot=(double)N/cpt;
	        double fxtot=(double)fx/cpt;
	        double couvtot=(double)couv/cpt;
	        String Meta = null;
	        switch(type) {
	        case 1:  Meta="recuit Simulé";
	        	break;
	        case 2:  Meta="génétique";
	        	break;
	        case 3:  Meta="BBo";
	        	break;
	        default: 
	        	break;
	        }
	        NumbersSensors.getData().add(new XYChart.Data(Meta,Ntot));
	        Evaluation.getData().add(new XYChart.Data(Meta,fxtot));
	        CoveredPoints.getData().add(new XYChart.Data(Meta,couvtot));
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Clear all Panels 
		BarChartPanel.getChildren().clear();
		PieChartPanel.getChildren().clear();
		pane.getChildren().clear();
		fitness.setText("");
		Couverture.setText("");
		NombreCapteurs.setText("");
		
		// Changer CSS BUTTON 
		ControllerHome.ChangerCssButton(ControllerHome.StatButton);
		// Charger Teste Précedent 
		if (ControllerHome.metaMaitre !=null) {
			Maitre=ControllerHome.metaMaitre;
			SetBarChart();
		    SetPieChart();
		    SimulationExecution();
		}
	}
}
