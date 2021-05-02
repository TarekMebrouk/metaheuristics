package Configuration;

import java.util.ArrayList;

public class ReglageParametres {

	/*-*/ private recuitSimuleP RsP;
	/*-*/ private geneticP      AgP;
	/*-*/ private BBoP         BboP;
	/*-*/ private zoneWSN      zone;
	/*-*/ private ArrayList<Solution> InitSolutions=new ArrayList<>();
	
	public ReglageParametres(zoneWSN Zone) {
		zone=Zone;
		RsP=new recuitSimuleP(0,0,0,null);
		AgP=new geneticP(0,0,null);
		BboP=new BBoP(0,0,0,0);
	}
	
	public void PreparerInitSolution() {
		if(InitSolutions.isEmpty()) {
		for(int i=0;i<100;i++) {
			Solution newS=new Solution(zone);
			InitSolutions.add(newS);
		}
		}
	}
	public ReglageParametres(zoneWSN Zone,recuitSimuleP rs,geneticP ag,BBoP bbo) {
		zone=Zone;  RsP=rs; AgP=ag; BboP=bbo;
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
	
	
	/*------------------------------REGLAGE PARAMETRES POUR LE RECUIT SIMULE ------------------------------------*/
	
	public void deepLocalSearchRecuitSimule(TypeRecuitSimule type) {
		int i=0; 
		recuitSimuleP localSearchP,P;
		System.out.println(" \t RECHERCHE PARAMETRE DU RECUIT SIMULER  \n");
		while(i<5) {
			P=new recuitSimuleP(type);
			localSearchP=localSearchRecuitSimule(P,type);
			/********/ System.out.print("\nlocal Search paramétres -> "); localSearchP.affichage();
			if (localSearchP.getFitness() > RsP.getFitness()) {
				RsP=localSearchP;
				/********/ System.out.print("\nSOLUTION AMELIORER -> "); RsP.affichage();
			}
			else {
				/********/ System.out.print("\nSOLUTION NON AMELIORER  AVEC i="+i); 
			}
			i++;
		}
		System.out.println("RESULTAT POUR LE RECUIT SIMULER -> \n"); RsP.affichage();
	}
	
	public recuitSimuleP localSearchRecuitSimule(recuitSimuleP P,TypeRecuitSimule type) {
		int i=0,N=100;
		Solution solutionInitial=getInitSolution();
		MetaEsclave meta=new recuitSimuleM(solutionInitial,P.getT(),N,P.getAlpha(),P.getBeta(),type);
		meta.function();
		recuitSimuleP voisin;
		P.setFitness(meta.getMeilleurSOl().getFitness());
		System.out.print("\n----------------------------------------------------------\n Local Search avec Paramétre -> ");P.affichage();
		while(i<5) {
			voisin=P.VoisinageP();
			meta=new recuitSimuleM(solutionInitial,voisin.getT(),N,voisin.getAlpha(),voisin.getBeta(),type);
			meta.function();
			voisin.setFitness(meta.getMeilleurSOl().getFitness());
			if(voisin.getFitness() > P.getFitness()) {
				P=voisin;
			}
			i++;
			System.out.print("\nIndice = "+i+" Voisin -> ");voisin.affichage();
		}
		System.out.print("\n----------------------------------------------------------\n");
		return P;
	}
	/*------------------------------------------------------------------------------------------------------------*/

	/*------------------------------REGLAGE PARAMETRES POUR L'ALGORITHME GENETIQUE -------------------------------*/
	
	public void deepLocalSearchGenetic(TypeAlgorithmeGenetic type) {
		int i=0; 
		geneticP localSearchP,P;
		System.out.println(" RECHERCHE PARAMETRES POUR L'ALGORITHME GENETIQUE ");
		while(i<5) {
			P=new geneticP(type);
			localSearchP=localSearchGenetic(P,type);
			/********/ System.out.print("\nlocal Search paramétres -> "); localSearchP.affichage();
			if (localSearchP.getFitness() > AgP.getFitness()) {
				AgP=localSearchP;
		    /********/ System.out.print("\nSOLUTION AMELIORER -> "); AgP.affichage();
			}
			else {
			/********/ System.out.print("\nSOLUTION NON AMELIORER  AVEC i="+i); 
			}
			i++;
		}
		System.out.println("\n RESULTAT FINAL GENETIQUE  ->"); AgP.affichage();
	}
	
	public geneticP localSearchGenetic(geneticP P,TypeAlgorithmeGenetic type) {
		int i=0,N=100;
		MetaEsclave meta=new geneticM(getInitPopulation(P.getN()),N,P.getN(),P.getP(),type);
		meta.function();
		geneticP voisin;
		P.setFitness(meta.getMeilleurSOl().getFitness());
		/*********/ System.out.println("\n----------------------------------------------------------");
		while(i<5) {
			voisin=P.VoisinageP();
			meta=new geneticM(N,voisin.getN(),voisin.getP(), zone,type);
			meta.function();
			voisin.setFitness(meta.getMeilleurSOl().getFitness());
			if(voisin.getFitness() > P.getFitness()) {
				P=voisin;
			}
              i++;
			System.out.print("\nIndice = "+i+" Voisin -> ");voisin.affichage();
		}
		/*********/ System.out.println("----------------------------------------------------------\n");
		return P;
	}
	/*------------------------------------------------------------------------------------------------------------*/
	
	
	/*--------------------------REGLAGE PARAMETRES POUR BIOGEOGRAPHY-BASED OPTIMIZATION---------------------------*/
	
	public void deepLocalSearchBBO() {
		int i=0; 
		BBoP localSearchP,P;
		System.out.println(" RECHERCHE PARAMETRES DU BBO ");
		while(i<5) {
			P=new BBoP();
			localSearchP=localSearchBBO(P);
			/******/ System.out.print("\nlocalSearch Parametre -> ");  localSearchP.affichage();
			if (localSearchP.getFitness() > BboP.getFitness()) {
				BboP=localSearchP;
				/******/	System.out.print("\n SOLUTION AMELIORER ..."); BboP.affichage();
			}
			else {
				/******/	System.out.println("\n SOLUTION NON AMELIORER ... AVEC i="+i);
			}
			i++;
		}
		/******/System.out.println("\n RESULTAT BBO -> "); BboP.affichage();
	}
	
	public BBoP localSearchBBO(BBoP P) {
		int i=0,N=100;
		MetaEsclave meta=new BBoM(getInitPopulation(P.getN()),P.getE(),P.getI(),N,P.getα());
		meta.function();
		BBoP voisin;
		P.setFitness(meta.getMeilleurSOl().getFitness());
		System.out.println("\n-----------------------------------------------------------------\n");
		while(i<5) {
			voisin=P.VoisinageP();
			meta=new BBoM(voisin.getN(),voisin.getE(),voisin.getI(), zone,N,voisin.getα());
			meta.function();
			voisin.setFitness(meta.getMeilleurSOl().getFitness());
			if(voisin.getFitness() > P.getFitness()) {
				P=voisin;
			}
			i++;
			System.out.print("\nIndice = "+i+" Voisin -> ");voisin.affichage();
		}
		System.out.println("\n-----------------------------------------------------------------\n");
		return P;
	}
	/*------------------------------------------------------------------------------------------------------------*/
	
	
	
	
	public recuitSimuleP getRsP() {
		return RsP;
	}
	
	public void setRsP(recuitSimuleP rsP) {
		RsP = rsP;
	}
	public geneticP getAgP() {
		return AgP;
	}
	public void setAgP(geneticP agP) {
		AgP = agP;
	}
	public BBoP getBboP() {
		return BboP;
	}
	public void setBboP(BBoP bBoP) {
		BboP = bBoP;
	}
	public zoneWSN getZone() {
		return zone;
	}
	public void setZone(zoneWSN zone) {
		this.zone = zone;
	}
	
	public ArrayList<Solution> getInitSolutions() {
		return InitSolutions;
	}

	public void setInitSolutions(ArrayList<Solution> initSolutions) {
		InitSolutions = initSolutions;
	}
	
}
