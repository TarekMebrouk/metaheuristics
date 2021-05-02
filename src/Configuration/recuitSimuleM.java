package Configuration;

/*        ENSEMBLE DE PARAMETRES :
                         -1- TEMPERATURE INITIAL T
                         -2- LE PARAMETRE DE REFROIDISSEMENT PAR PALIER 0.9 ≤ α ≤ 0.99 
*/

public class recuitSimuleM extends MetaEsclave {

	/*-*/ private Solution InitialSol;
	/*-*/ private  double T;
	/*-*/ private  double Tmin = 0.1;
	/*-*/ private  double Alpha; // 0.9 ≤ α ≤ 0.99 
	/*-*/ private  double Beta;  // 0.1 < Beta < 0.5
	/*-*/ private  TypeRecuitSimule Type; // Type : ReductionParPalier - ReductionMonotone
	
	public recuitSimuleM(Solution sol, double tmax, int nb,double alpha,double Beta,TypeRecuitSimule type) {
		setRang(0);
		setBeta(Beta);
		// initialiser la Temperature initial et la minimal
		InitialSol = sol;
		setT(tmax);
		setNombreSol(nb);
		Alpha=alpha;
		Type=type;
	}

	public double getTmin() {
		return Tmin;
	}

	public double getT() {
		return T;
	}

	public void setT(double tmax) {
		T = tmax;
	}

	@Override
	public void function() {
		 if(Type.compareTo(TypeRecuitSimule.Reduction_par_palier)==0)  AvecReducationPaliers();
		 else  AvecReductionContinue();
		setMeilleurSOl(InitialSol);
	}

	public void AvecReducationPaliers() {
		Solution Sol=InitialSol,precedent;
		double fx, P=0,Temperature=T;
		int I;
		while (Temperature >= Tmin ) {
			I=0; 
			while(I<5) {
				// Generation du voisinage de la solution courante + decrementation du Nombre de
				// Solution NombreSol--;
				precedent=Sol;
				Sol = InitialSol.Voisinage(); 
				// calcule du fx =f(x*)-f(x)
				fx = Sol.getFitness() - InitialSol.getFitness();
				// Comparaison et choix de la meilleur Solution
				if (Sol.getFitness() >= InitialSol.getFitness()) {
					InitialSol = Sol; 
				} else {
					// Calcule P = e^(fx/T)
					P = Math.exp(fx / Temperature);
					// InitialSOl = Sol with P = e^(-fx/T)
					if (Math.random() < P)
						{
						InitialSol = Sol;
						}
				}
				
				// fin du palier si pendant 10 Itération la solution stagne (on diversifie la solution)
				if (precedent.getFitness()>=Sol.getFitness()) I++;
				else I=0;
			}
			// Schema de refroidissement ::
			//double Alpha = (Math.random() * ((0.99 - 0.90) + 0.001)) + 0.90;
			Temperature = Temperature * Alpha;
		}
	}

	public void AvecReductionContinue() {
		Solution Sol;
		double fx, P=0,Temperature=T;
		while (Temperature >= Tmin) {
			// Generation du voisinage de la solution courante + decrementation du Nombre de
			// Solution NombreSol--;
			Sol = InitialSol.Voisinage();
			// calcule du fx =f(x*)-f(x)
			fx = Sol.getFitness() - InitialSol.getFitness();
			// Comparaison et choix de la meilleur Solution
			if (Sol.getFitness() >= InitialSol.getFitness()) {
				InitialSol = Sol;
			} else {
				// Calcule P = e^(fx/T)
				P = Math.exp(fx / Temperature);
				// InitialSOl = Sol with P = e^(-fx/T)
				if (Math.random() < P)
					InitialSol = Sol;
			}
			// Schema de refroidissement ::
			Temperature = Temperature-Beta;
		}
	}

	public double getAlpha() { 
		return Alpha;
	}

	public double getBeta() {
		return Beta;
	}

	public void setBeta(double beta) {
		Beta = beta;
	}

	/**
	 * @return the type
	 */
	public TypeRecuitSimule getType() {
		return Type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(TypeRecuitSimule type) {
		Type = type;
	}
	

}
