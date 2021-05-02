package Configuration;


/*        ENSEMBLE DE PARAMETRES :
							-1- LA TAILLE DE LA POPULATION      N
							-2- The Maximum Rate Of Emigration  E
							-3- The Maximum Rate Of Immigration I
							-4- Paramétre du Mutation           α
*/

public class BBoP extends ParametresGeneral{

	/*-*/ private int    N; // Taille de la Population
	/*-*/ private double E; // The Maximum Rate Of Emigration  
	/*-*/ private double I; // The Maximum Rate Of Immigration
	/*-*/ private double α; // Paramétre du Mutation
	
	public BBoP() {
		N      =(int)  ((Math.random() * ((100 - 5) + 1)) + 5);
		E      = (Math.random() * 0.9) + 0.1;
		I      = (Math.random() * 0.9) + 0.1;
		α      = Math.random();
		setFitness(0);
	}
	
	public BBoP(int n,double e,double i,double alpha) {
		N=n; E=e; I=i; α=alpha; setFitness(0);
	}
	
	@Override
	public BBoP VoisinageP() {
		// TODO Auto-generated method stub
		BBoP voisin=new BBoP(N,E,I,α);
		// tirer un Nombre aléatoire entre 1-3
				int k=(int)((Math.random() * 4 )+ 1);
						switch (k) {
						case 1:
							voisin.N     = (int)  ((Math.random() * ((100 - 5) + 1)) + 5);
							break;
						case 2:
							voisin.E     = (Math.random() * 0.9) + 0.1;
							break;
						case 3:
							voisin.I     = (Math.random() * 0.9) + 0.1;
							break;
						case 4:
							voisin.α     = Math.random();
							break;
						default:
							break;
						}
						return voisin;
	}

	@Override
	public void affichage() {
		// TODO Auto-generated method stub
		System.out.println("Paramétres Biogeography-based optimization -> {N="+N+"} {E="+E+"} {I="+I+"} {α="+α+"} {fitness="+getFitness()+"}");
	}

	public int getN() {
		return N;
	}
	
	public double getE() {
		return E;
	}
	
	public double getI() {
		return I;
	}
	
	public double getα() {
		return α;
	}
	
	public void setN(int N) {
		this.N=N;
	}
	
	public void setE(double E) {
		this.E=E;
	}
	
	public void setI(double I) {
		this.I=I;
	}
	
	public void setα(double α) {
		this.α=α;
	}
	
}
