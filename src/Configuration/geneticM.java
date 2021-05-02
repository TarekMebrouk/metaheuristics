package Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

/*        ENSEMBLE DE PARAMETRES :
                      -1- LA TAILLE DE LA POPULATION N
                      -2- PORBABILITE DU MUTATION 
*/

public class geneticM extends MetaEsclave {

	/*-*/ private ArrayList<Solution> Population = new ArrayList<>();
	/*-*/ private TypeAlgorithmeGenetic Type;
	/*-*/ private final int N; // LA TAILLE DE LA POPULATION 
	/*-*/ private final double P; // PORBABILITE DU MUTATION 

	public geneticM(ArrayList<Solution> liste, int nombreEval,int n,double p,TypeAlgorithmeGenetic type) {
		setRang(0);
		setNombreSol(nombreEval);
		setPopulation(liste);
		N=n; 
		P=p;
		Type=type;
	}
	
	public geneticM (int nombreEval,int n,double p, zoneWSN zone,TypeAlgorithmeGenetic type) {
		setRang(0);
		setNombreSol(nombreEval);
		N=n; 
		P=p;
		Type=type;
		// Géneration de N Solution aléatoire pour la Population Initial 
		for( int i=0;i<N;i++) {
			Population.add(new Solution(zone));
		}
	}

	@SuppressWarnings("unchecked")
	public void remplacementPopulation() {
		ArrayList<Solution> NvPopulation;
		int cpt=0;
		while (cpt<getNombreSol()) {
			NvPopulation = new ArrayList<>();
			do {
				ArrayList<Solution> XY = new ArrayList<>();
				// Selection
				XY = Selection(RandomInt(0, 3));
				// Croisement
				XY = CroisementRemplacement(XY);
				// Mutation
				Solution X=XY.get(0),Y=XY.get(1);
			if (Math.random() < P)	 X = Mutation(XY.get(0));
			if (Math.random() < P)	 Y = Mutation(XY.get(1));
				// Insertion des 2 enfants (enfant1,enfant2) dans NvPopulation
			if (X.isValide() && Y.isValide()) {
					NvPopulation.add(X);
					NvPopulation.add(Y);
					// Decrementer Le Nombre de Solution
					cpt=cpt+2; 
				}
			} while (NvPopulation.size() != Population.size() && cpt < getNombreSol());
			Population = NvPopulation;
		}
		Collections.sort(Population);
		setMeilleurSOl(Population.get(Population.size()-1));
	}

	@SuppressWarnings("unchecked")
	public void remplecementIncremental() {
		ArrayList<Solution> XY = new ArrayList<>();
		Solution X;
		int cpt=0;
		while (cpt<getNombreSol()) {

			// Selection
			XY = Selection(RandomInt(0, 3));
			// Croisement
			// X=CroisementIncremental(RandomInt(0,1), XY);
			X = CroisementIncremental(XY);
			// Mutation
			if( Math.random() < P )  X = Mutation(X); 
			// Insertion de enfant dans Population ( remplacer la mauvaise Solution )
			if (X.isValide()) {
				Collections.sort(Population);
				Population.remove(Population.get(0));
				Population.add(X);
				// Decrementer Le Nombre de Solution
				cpt++;
			}
		}
		Collections.sort(Population);
		setMeilleurSOl(Population.get(Population.size() - 1));
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Solution> Selection(int type) {
		// type=0 :: PAR_TOURNOI | type=1 :: PAR_ROULETTE | type=2 :: PAR_RANG | type=3
		// :: PAR_ELITISME
		ArrayList<Solution> liste = new ArrayList<>();
		switch (type) {
		case 0:
			// Selection Par tournoi
			for (int i = 0; i < 2; i++) {
				liste.add(Population.get((int) (Math.random() * Population.size())));
			}
			return liste;
		case 1:
			// Selection Par Roulette
			return SelectionParRoulette();
		case 2:
			// Selection par Rang
			return SelectionParRang();
		case 3:
			// Selection par l'élitisme
			Collections.sort(Population);
			liste.add(Population.get(Population.size() - 1));
			liste.add(Population.get(Population.size() - 2));
			return liste;
		default:
			break;
		}

		return liste;
	}
	
	public  ArrayList<Solution> CroisementRemplacement(ArrayList<Solution> XY) {
		ArrayList<Solution> liste = new ArrayList<>();
		// CROISEMENT SIMPLE
		Solution solutionX=new Solution(XY.get(0).getZone(),0);
		Solution solutionY=new Solution(XY.get(0).getZone(),0);
		ArrayList<Region> X = XY.get(0).getListeRegions(), Y = XY.get(1).getListeRegions();
		int i = (int) X.size() / 2;
		for (int j = 0; j < X.size(); j++) {
			Region RegionX=X.get(j),RegionY=Y.get(j);
			if (j < i) {
			if(RegionX.isOn())  	solutionX.getListeRegions().get(solutionX.getListeRegions().indexOf(RegionX)).setOn(true);
			if(RegionY.isOn())  	solutionY.getListeRegions().get(solutionY.getListeRegions().indexOf(RegionY)).setOn(true);
			} else {
			if(RegionY.isOn())	solutionX.getListeRegions().get(solutionX.getListeRegions().indexOf(RegionY)).setOn(true);
			if(RegionX.isOn())  solutionY.getListeRegions().get(solutionY.getListeRegions().indexOf(RegionX)).setOn(true);
			}
		}
		// Mise a jour Solution enfant X,Y
		solutionX.setNombreCapteurs(solutionX.CalculeNombreCapteurs());
		solutionX.CalculeFitness();
		solutionY.setNombreCapteurs(solutionY.CalculeNombreCapteurs());
		solutionY.CalculeFitness();

			liste.add(solutionX);
			liste.add(solutionY);

		return liste;
		}

		public  Solution CroisementIncremental(ArrayList<Solution> XY) {
		// CROISEMENT SIMPLE
		Solution solutionX=new Solution(XY.get(0).getZone(),0);
		ArrayList<Region> X = XY.get(0).getListeRegions(), Y = XY.get(1).getListeRegions();
		int i = (int) X.size() / 2;
		for (int j = 0; j < X.size(); j++) {
		Region RegionX=X.get(j),RegionY=Y.get(j);
		if (j < i) {
		if(RegionX.isOn())  	solutionX.getListeRegions().get(solutionX.getListeRegions().indexOf(RegionX)).setOn(true);
		} else {
		if(RegionY.isOn())	    solutionX.getListeRegions().get(solutionX.getListeRegions().indexOf(RegionY)).setOn(true);
		}
		}
		// Mise a jour Solution enfant X
		solutionX.setNombreCapteurs(solutionX.CalculeNombreCapteurs());
		solutionX.CalculeFitness();

		return solutionX;
		}
		

	public Solution Mutation(Solution sol) {
		// remplcer un Capteur par un autre aléatoire
		Solution solution=new Solution(sol.getZone(),sol.getListeRegions());
		Region region=null;
		int i=0,max=solution.getZone().getTotalePoints();
		do {
			if (region!=null) { region.BasculeStatut(); }
			int x = (int) (Math.random() * solution.getListeRegions().size());
			region=solution.getListeRegions().get(x);
			region.BasculeStatut();
			i++;
		} while (!solution.isValide() && i<max);

		// Mise a jour de la nouvelle solution 
		solution.setNombreCapteurs(solution.CalculeNombreCapteurs());
		solution.CalculeFitness();
		return solution;
	}

	public TypeAlgorithmeGenetic getType() {
		return Type;
	}

	public void setType(TypeAlgorithmeGenetic type) {
		Type = type;
	}

	public ArrayList<Solution> getPopulation() {
		return Population;
	}

	public void setPopulation(ArrayList<Solution> population) {
		Population = population;
	}

	public ArrayList<Solution> SelectionParRang() {
		ArrayList<Solution> Solutions = new ArrayList<>();
		int N = 0, S, K;
		// Remplire la Population dans un TreeMap<Rang,Solution>
		TreeMap<Integer, Solution> Rang = new TreeMap<>();
		for (int i = 0; i < Population.size(); i++) {
			Rang.put(i + 1, Population.get(i));
		}
		// Génération d'un nombre Aléatoire 1 < K < N=Somme(1..TreeMap.size())
		// 1- Calcule de Somme(1..TreeMap.size())
		Iterator<Entry<Integer, Solution>> it = Rang.entrySet().iterator();
		while (it.hasNext()) {
			N = N + it.next().getKey();
		}
		// 2- Générer Nombre K aléatoire 2 foix
		Entry<Integer, Solution> sol = Rang.firstEntry();
		for (int i = 0; i < 2; i++) {
			it = Rang.entrySet().iterator();
			K = (int) (Math.random() * (N + 1));
			S = 0;
			while (S < K && it.hasNext()) {
				sol = it.next();
				S = S + sol.getKey();
			}
			Solutions.add(sol.getValue());
		}
		return Solutions;
	}

	public ArrayList<Solution> SelectionParRoulette() {
		ArrayList<Solution> Solutions = new ArrayList<>();
		double N = 0, K, S;
		// Remplire la Population dans un TreeMap<Rang,Solution>
		TreeMap<Double, Solution> Roulette = new TreeMap<>();
		for (Solution sol : Population) {
			Roulette.put(sol.getFitness(), sol);
		}
		// Génération d'un nombre Aléatoire 1 < K < N=Somme(f(x1)..f(xTreeMap.size()))
		// 1- Calcule de Somme(1..TreeMap.size())
		Iterator<Entry<Double, Solution>> it = Roulette.entrySet().iterator();
		while (it.hasNext()) {
			N = N + it.next().getKey();
		}
		// 2- Générer Nombre K aléatoire 2 foix
		Entry<Double, Solution> sol = Roulette.firstEntry();
		for (int i = 0; i < 2; i++) {
			it = Roulette.entrySet().iterator();
			K = (Math.random() * (N + 1));
			S = 0;
			while (S < K && it.hasNext()) {
				sol = it.next();
				S = S + sol.getKey();
			}
			Solutions.add(sol.getValue());
		}
		return Solutions;
	}

	public int RandomInt(int Min, int Max) {
		return (int) ((Math.random() * ((Max - Min) + 1)) + Min);
	}

	@Override
	public void function() {
		if (Type.compareTo(TypeAlgorithmeGenetic.remplacement_population) == 0)
			remplacementPopulation();
		else
			remplecementIncremental();
	}

	public int getN() {
		return N;
	}
	
	public double getP() {
		return P;
	}
}
