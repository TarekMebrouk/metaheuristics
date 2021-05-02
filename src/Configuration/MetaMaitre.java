package Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class MetaMaitre {
	/*-*/ private Solution FinalSol;
	/*-*/ private ArrayList<MetaEsclave> InTabouList = new ArrayList<>();
	/*-*/ private ArrayList<MetaEsclave> OutTabouList = new ArrayList<>();
	/*-*/ private final zoneWSN zone;
	/*-*/ private ArrayList<Solution> InitSolutions=new ArrayList<>();
	/*-*/ private ReglageParametres parametre;
	/*-*/ private int NombreEvaluations;
	/*-*/ private ArrayList<SavingSolution> Solutions=new ArrayList<>();
	/*-*/ static int cpt=1;

	public MetaMaitre(zoneWSN Zone,ReglageParametres param,int N) {
		zone = Zone;  FinalSol=null; parametre=param; NombreEvaluations=N;
	}

	public void PreparerInitSolutions() {
		if (InitSolutions.isEmpty()) {
		// Solutions initial Aléatoires . . . . 
				int max=Math.max(parametre.getAgP().getN(), parametre.getBboP().getN());
				for(int i=0;i<max;i++) {
					Solution newS=new Solution(zone);
					InitSolutions.add(newS);
				}
		}
	}
	
	public Solution getFinalSOl() {
		return FinalSol;
	}

	public void setFinalSOl(Solution finalSOl) {
		FinalSol = finalSOl;
	}

	public ArrayList<MetaEsclave> getTabouList() {
		return InTabouList;
	}

	public void setTabouList(ArrayList<MetaEsclave> Liste) {
		InTabouList = Liste;
	}

	public zoneWSN getZone() {
		return zone;
	}

	public ArrayList<MetaEsclave> getOutTabouList() {
		return OutTabouList;
	}

	public void setOutTabouList(ArrayList<MetaEsclave> outTabouList) {
		OutTabouList = outTabouList;
	}
	
	public Solution getInitSolution() {
		int x = (int) (Math.random() * InitSolutions.size());
		return InitSolutions.get(x);
	}
	
	public ArrayList<Solution> getInitPopulation(int N) {
		ArrayList<Solution> P=new ArrayList<>();
		for (int i=0;i<N;i++) {
			P.add(new Solution(InitSolutions.get(i).getZone(),InitSolutions.get(i).getListeRegions()));
		}
		return P;
	}
	
	public void InitMetaheuristique(TypeAlgorithmeGenetic typeAg,TypeRecuitSimule typeRs) {
		OutTabouList.add(new recuitSimuleM(getInitSolution(),parametre.getRsP().getT(),NombreEvaluations, parametre.getRsP().getAlpha(),parametre.getRsP().getBeta(),typeRs));
		OutTabouList.add(new geneticM(getInitPopulation(parametre.getAgP().getN()),NombreEvaluations,parametre.getAgP().getN(), parametre.getAgP().getP(),typeAg)); 
		OutTabouList.add(new BBoM(getInitPopulation(parametre.getBboP().getN()),parametre.getBboP().getN(),parametre.getBboP().getI(),NombreEvaluations,parametre.getBboP().getα()));	

		// Recuit Simulé 
		MetaEsclave Recuit=OutTabouList.get(0);
		Recuit.function();
	    /* Save Solution */	SaveSolution(Recuit,Recuit.getMeilleurSOl(),0);
	 	System.out.print("\t RECUIT SIMULER -> "); Recuit.getMeilleurSOl().Affichage();
		
		// genetic 
		MetaEsclave genetic=OutTabouList.get(1);
		genetic.function();
		/* Save Solution */	SaveSolution(genetic,genetic.getMeilleurSOl(),0);
		System.out.print("\t GENETIC  -> ");    genetic.getMeilleurSOl().Affichage();
		
		// Biogeograpy-based optimization
		MetaEsclave BBO=OutTabouList.get(2);
		BBO.function();
		/* Save Solution */	SaveSolution(BBO,BBO.getMeilleurSOl(),0);
		System.out.print("\t BBO -> ");     BBO.getMeilleurSOl().Affichage();
		
		// Set Meilleur Solution entre les 3
		TreeMap<Double,MetaEsclave> Tmap=new TreeMap<>();
		Tmap.put(Recuit.getMeilleurSOl().getFitness(),Recuit);
		Tmap.put(genetic.getMeilleurSOl().getFitness(),genetic);
		Tmap.put(BBO.getMeilleurSOl().getFitness(),BBO);
		Solution s=Tmap.lastEntry().getValue().getMeilleurSOl();
		FinalSol=new Solution(s.getZone(),s.getListeRegions());
	    
	    // Calcule le Rang de chaque MetaHeuristique 
		/* RS  */ Recuit.setRang(Recuit.getMeilleurSOl().getFitness()-FinalSol.getFitness()); 
		/* AG  */ genetic.setRang(genetic.getMeilleurSOl().getFitness()-FinalSol.getFitness());
		/* BBO */ BBO.setRang(BBO.getMeilleurSOl().getFitness()-FinalSol.getFitness());	
		
	}	
	
	public MetaEsclave ExcuteMeta(MetaEsclave meta,TypeAlgorithmeGenetic typeAg,TypeRecuitSimule typeRs) {
		MetaEsclave nvMeta=null;
		if (meta instanceof recuitSimuleM) {
			// Recuit Simulé 
			nvMeta=new recuitSimuleM(getInitSolution(),((recuitSimuleM)meta).getT(),NombreEvaluations,((recuitSimuleM)meta).getAlpha(),((recuitSimuleM)meta).getBeta(),typeRs);
			nvMeta.setRang(meta.getRang());
		}
		if (meta instanceof geneticM) {
			// Genetique
			nvMeta =new geneticM(getInitPopulation(((geneticM)meta).getN()),NombreEvaluations,((geneticM)meta).getN(),((geneticM)meta).getP(),typeAg);
			nvMeta.setRang(meta.getRang());
		}
		if (meta instanceof BBoM) {
			// Biogeography-based optimization
			nvMeta=new BBoM(getInitPopulation(((BBoM)meta).getN()),((BBoM)meta).getE(),((BBoM)meta).getI(),NombreEvaluations,((BBoM)meta).getΑ());
			nvMeta.setRang(meta.getRang());
		}
		
		if (InTabouList.contains(meta)) {
			InTabouList.remove(meta);
			InTabouList.add(nvMeta);
		}
		if (OutTabouList.contains(meta)) {
			OutTabouList.remove(meta);
			OutTabouList.add(nvMeta);
		}
		return nvMeta;
	}
	
	
	public void BasculeInOutTabouList() {
		System.out.println("BASCULER . . . ");
		for(MetaEsclave meta: InTabouList) {
			OutTabouList.add(meta);
		}
		InTabouList.clear();
	}
	
	public void processus(TypeRecuitSimule typeRs, TypeAlgorithmeGenetic typeAg) {
		double i=0,Alpha;
		boolean isTabou=false;
		MetaEsclave metaCourante;
		Solution SolCourante=new Solution (zone,0);
		Solution SolFound=new Solution (zone,0);
		
		//*--------------Initialisation ----------------*//
		InitMetaheuristique(typeAg,typeRs);
		//*---------------------------------------------*//
		System.out.println("\n--------------------------------------\n");
		while (i<10) {
			if (InTabouList.size()==3) {
		//the TabouList is Full we chose the Meta having the highest Rank 
			Collections.sort(InTabouList);
			metaCourante=InTabouList.get(InTabouList.size()-1);
			isTabou=true;
			System.out.println("\nInTabouList IS FULL ,THE META CHOSSEN :"+metaCourante+" WITH Rang : "+metaCourante.getRang());
			}
			else {
		//the tabouList is not Full we chose the Meta having the highest Rank out of the Tabou List 
			Collections.sort(OutTabouList);
			metaCourante=OutTabouList.get(OutTabouList.size()-1);	
			isTabou=false;
			System.out.println("\nOutTabouList IS NOT FULL ,THE META CHOSSEN : "+metaCourante+" WITH Rang : "+metaCourante.getRang());
			}
		       //Execute The chosen meta  
		       metaCourante=ExcuteMeta(metaCourante,typeAg,typeRs);
		       metaCourante.function();
		       /* Save Solution */	SaveSolution(metaCourante,metaCourante.getMeilleurSOl(),cpt); cpt++;
		       //Sauvgarder la Nouvelle Solution trouvée dans la variable "SolCourante" 
		       SolFound=new Solution(metaCourante.getMeilleurSOl().getZone(),metaCourante.getMeilleurSOl().getListeRegions());
		       System.out.println("\n SOLUTION FOUND => "); SolFound.Affichage();
		       System.out.println("\n FINAL SOLUTION => "); FinalSol.Affichage();
		       // Calcule de alpha 
		       Alpha=SolFound.getFitness()-FinalSol.getFitness();
		if (SolFound.getFitness() > FinalSol.getFitness()) {
			 FinalSol=new Solution(SolFound.getZone(),SolFound.getListeRegions());
		     BasculeInOutTabouList();
		     i=0;
		}
		else {
			i++;
			if (SolCourante.getFitness() > SolFound.getFitness()) {
				if (isTabou==false) {
					InTabouList.add(metaCourante);  OutTabouList.remove(metaCourante);
					} 
			}
		}
		metaCourante.MajRang(Alpha);
		SolCourante=SolFound;
		System.out.println("\t\t\t\t INDICE +> "+i);
		System.out.println("IN TABOU LIST");
		for (MetaEsclave m :InTabouList) {
			System.out.print("{"+m+" , "+m.getRang()+"} ");
		} 
		System.out.println("\nOUT TABOU LIST");
	    for(MetaEsclave m: OutTabouList) {
	    	System.out.print("{"+m+" , "+m.getRang()+"} ");
	    }
		}
		System.out.println("\n--------------------------------------\n");
	}

	public void SaveSolution(MetaEsclave meta,Solution s,int rang) {
		Solutions.add(new SavingSolution(meta,s,rang));
	}
	
	public ArrayList<Solution> getInitSolutions() {
		return InitSolutions;
	}

	public void setInitSolutions(ArrayList<Solution> initSolutions) {
		InitSolutions = initSolutions;
	}

	public ReglageParametres getParametre() {
		return parametre;
	}

	public void setParametre(ReglageParametres parametre) {
		this.parametre = parametre;
	}

	public int getNombreEvaluations() {
		return NombreEvaluations;
	}

	public void setNombreEvaluations(int nombreEvaluations) {
		NombreEvaluations = nombreEvaluations;
	}

	public ArrayList<SavingSolution> getSolutions() {
		return Solutions;
	}

	public void setSolutions(ArrayList<SavingSolution> solutions) {
		Solutions = solutions;
	}
	
}
