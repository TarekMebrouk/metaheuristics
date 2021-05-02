package Configuration;

/*        ENSEMBLE DE PARAMETRES :
                            -1- TEMPERATURE INITIAL T
							-2- LE PARAMETRE DE REFROIDISSEMENT PAR PALIER 0.9 ≤ α ≤ 0.99 
*/
public class recuitSimuleP extends ParametresGeneral{
	
	/*-*/ private double T;
	/*-*/ private double Alpha; // 0.9 ≤ α ≤ 0.99 
	/*-*/ private double Beta;  // 0.1 < Beta < 0.5
	/*-*/ private final TypeRecuitSimule type;
	
	public recuitSimuleP(TypeRecuitSimule t) {
		setT((Math.random() * ((800 - 500) + 1)) + 500);
		setAlpha((Math.random() * ((0.99 - 0.90) + 0.001)) + 0.90);
		setBeta((Math.random() * ((0.5- 0.1) + 0.001)) + 0.1);
        setFitness(0);
        type=t;
	}
	
	public recuitSimuleP(double t,double a,double beta,TypeRecuitSimule typeRs) {
	T=t; Alpha=a;	 setFitness(0); setBeta(beta); type=typeRs;
	} 

	@Override
	public recuitSimuleP VoisinageP() {
		recuitSimuleP voisin=new recuitSimuleP(T,Alpha,Beta,type);
	  // tirer un Nombre aléatoire entre 1-3
		int k=(int)((Math.random() * 2 )+ 1);
		switch (k) {
		case 1:
			voisin.setT((Math.random() * ((800 - 500) + 1)) + 500);
			break;
		case 2:
			voisin.setAlpha((Math.random() * ((0.99 - 0.90) + 0.001)) + 0.90);
			voisin.setBeta((Math.random() * ((0.5- 0.1) + 0.001)) + 0.1);
			break;
		default:
			break;
		}
		return voisin;
	}

	public double getT() {
		return T;
	}

	public void setT(double t) {
		T = t;
	}

	public double getAlpha() {
		return Alpha;
	}

	public void setAlpha(double alpha) {
		Alpha = alpha;
	}

	@Override
	public void affichage() {
		// TODO Auto-generated method stub
		System.out.println("Paramétre du Recuit Simulé -> {T="+T+"} {α="+Alpha+"}{Beta="+Beta+"} {fitness="+getFitness()+"}");
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
		return type;
	}

}
