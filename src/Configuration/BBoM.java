package Configuration;

import java.util.ArrayList;
import java.util.Collections;

public class BBoM extends MetaEsclave{

/*        ENSEMBLE DE PARAMETRES :
    -1- LA TAILLE DE LA POPULATION      N
    -2- The Maximum Rate Of Emigration  E
    -3- The Maximum Rate Of Immigration I
    -4- Paramétre du Mutation           α
*/
	/*-*/ private int N;    // Taille de la Population
	/*-*/ private double E; // The Maximum Rate Of Emigration  
	/*-*/ private double I; // The Maximum Rate Of Immigration
	/*-*/ private ArrayList<SolutionBBO> Population=new ArrayList<>();
	/*-*/ private double α; // Paramétre du Mutation
	/*-*/ private double Pmax; 
	 
	public BBoM(ArrayList<Solution> population,double e,double i,int NombreSolution,double Alpha) {
		setRang(0);
		// Initialiser les paramétres (I,E,N,α)
		E=e; I=i; N=population.size(); setNombreSol(NombreSolution); α=Alpha;
		// Générer Aléatoirement une Population Initial 
		for (Solution s : population) {
			Population.add(new SolutionBBO(s,N-1,0));
		}
	}
	
	public BBoM(int n,double e,double i,zoneWSN zone,int NombreSolution,double Alpha) {
		setRang(0);
		// Initialiser les paramétres (I,E,N,α)
		N=n; E=e; I=i; setNombreSol(NombreSolution); α=Alpha;
		// Générer Aléatoirement une Population Initial 
		for (int cpt=0;cpt<n;cpt++) {
			Population.add(new SolutionBBO(new Solution(zone),N-1,0));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void CalculeK_λ_µ() {
		// Calculer Le Nombre d'éspéces pour chaque Solution
		int cpt=0;
		Collections.sort(Population);
		for (SolutionBBO sol: Population) {
			sol.setK(cpt);
			sol.Calculeλ(I);
			sol.Calculeµ(E);
			cpt++;
		}
	}
	
	public void ChangeSIV(SolutionBBO SI,SolutionBBO SJ) {
		Solution dest=SI.getSolution();
		Region r1=null,r2=null;
		int i=0;
		boolean fin=false,Basculer=false;
		while(fin!=true && i<10) {
			if (r1 !=null && Basculer==true) { r1.BasculeStatut(); Basculer=false;}
			int x = (int) (Math.random() * dest.getListeRegions().size());
			r1=dest.getListeRegions().get(x);
			r2=SJ.getSolution().getListeRegions().get(x);
			if (r1.isOn() !=r2.isOn()) {
				Basculer=true;
				r1.BasculeStatut();
				if (dest.isValide()) fin=true;
			} 
			else i++; 
		} 
		if (i>=10 && Basculer==true)  r1.BasculeStatut();
		// Mise a jour de la nouvelle solution 
		dest.setNombreCapteurs(dest.CalculeNombreCapteurs());
		dest.CalculeFitness();
	}
	
	public void Mutation(SolutionBBO S) {
		// Calcule probablité du mutation m(s)
		double P=CalculePorbaMutation(S);
		// Mutation avec probabilité m(s)
		if (Math.random() < P) {
	    S=AlgorithmeMutation(S);
		}
	}
	
	public SolutionBBO AlgorithmeMutation(SolutionBBO S) {
		       // remplcer un Capteur par un autre aléatoire
				Solution solution=S.getSolution();
				Region region=null;
				int i=0;
				while(i<10){
					if (region!=null) { region.BasculeStatut(); }
					int x = (int) (Math.random() * solution.getListeRegions().size());
					region=solution.getListeRegions().get(x);
					region.BasculeStatut();
					if(solution.isValide()) break; 
					else i++;
				} 
				if (i>=10)  region.BasculeStatut();
				// Mise a jour de la nouvelle solution 
				solution.setNombreCapteurs(solution.CalculeNombreCapteurs());
				solution.CalculeFitness();
				return S;
	}
	
	public double CalculePorbaMutation(SolutionBBO S) {
		double A=1-CalculePk(S.getK());
		return  α*(A/Pmax);
	}
	
	public double CalculePk(int K) {
		if (K==0) {
			return Rapport1_λµ(K);
		}else {
			double Rapportλµ= Rapportλ(0,K)/Rapportµ(1,K);
			return Rapportλµ*Rapport1_λµ(K);
		}
	}
	
	public double Rapportλ(int Min,int Max) {
		double Totalλ=1;
	   for (int i=Min;i<Max-1;i++) {
		   Totalλ=Totalλ*Population.get(i).getλ();
	   }
	   return Totalλ;
	}
	
	public double Rapportµ(int Min,int Max) {
		double ToTalµ=1;
	   for (int i=Min;i<Max;i++) {
		   ToTalµ=ToTalµ*Population.get(i).getµ();
	   }
	   return ToTalµ;
	}
	
	public double Rapport1_λµ(int Max) {
		double Produitλ=Rapportλ(0,Max);
	    double Somme=0,Produitµ=1;
		for (int l=1;l<Max;l++) {
			Produitµ=Produitµ*Population.get(l).getµ();
			Somme=Somme+(Produitλ/Produitµ);
		}
		return (double)1/(1+Somme);
	}
	
	@SuppressWarnings("unchecked")
	public void AlgorithmeElite(SolutionBBO S) {
		Collections.sort(Population);
		SolutionBBO SM=Population.get(Population.size()-1);
		if (S.compareTo(SM)==1) {
			S.setK(SM.getK());
			Population.remove(0);
			Population.add(S);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void function() {
		int cpt=0;  SolutionBBO precedSol;
		while (cpt < getNombreSol()) {
			// Evaluation du fitness (HSI) de chaque Solution (auto)
			// Mise a jour Nombre d'éspéces pour chaque Solution (K)
			// Mise a jour le Taux d'immigration (λ) pour chaque Solution 
			// Mise a jour le Taux d'émigration  (µ) pour chaque Solution 
			CalculeK_λ_µ();
			// Sauvgarder La meilleur solution courante 
			SolutionBBO sol=Population.get(Population.size()-1);
			Solution solution=sol.getSolution();
			precedSol=new SolutionBBO(new Solution(solution.getZone(),solution.getListeRegions()),N-1,sol.getK());
			// Calcule Pmax pour la probabilité du mutation
			Pmax=CalculePk(N-1);

		for (SolutionBBO SI: Population) {
			// Utiliser λi pour décider d'immigrer à la Solution Courante (Si)
		if (Math.random()<SI.getλ()) {
		      for (SolutionBBO SJ: Population) {
		    // Sélectionner la Solution d'émigration (Sj) avec la probabilité µj
		if (Math.random()<SJ.getµ()) {
			// Choisir SIV aléatoire dans Si pour remplacer par Sj (Region)
			ChangeSIV(SI, SJ);
			// Incrémenter Nombre de Solution
			cpt++;
		}
		}
		}
		}
		// Mutation
		for (SolutionBBO S :Population) {
			Mutation(S);
		}
		// Implémenter l'élitisme 
		AlgorithmeElite(precedSol);
		}
	    // Resultat :: 
		Collections.sort(Population);
	    setMeilleurSOl(Population.get(Population.size()-1).getSolution());
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

	public double getE() {
		return E;
	}

	public void setE(double e) {
		E = e;
	}

	public double getI() {
		return I;
	}

	public void setI(double i) {
		I = i;
	}

	public double getΑ() {
		return α;
	}

	public void setΑ(double α) {
		this.α = α;
	}

	public double getPmax() {
		return Pmax;
	}

	public void setPmax(double P) {
		this.Pmax = P;
	}
	
	public ArrayList<SolutionBBO> getPopulation() {
		return Population;
	}

	public void setPopulation(ArrayList<SolutionBBO> P) {
		this.Population = P;
	}
	
	public void AffichagePopulation() {
		System.out.println("---------------------------------------------------------------------------------------");
		for (SolutionBBO s: Population) {
			s.Afficher();  
		}
		System.out.println("Pmax = "+Pmax);
		System.out.println("---------------------------------------------------------------------------------------\n");
	}

}
