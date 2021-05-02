package Configuration;

/*        ENSEMBLE DE PARAMETRES :
								-1- LA TAILLE DE LA POPULATION N
								-2- PORBABILITE DU MUTATION 
*/

public class geneticP extends ParametresGeneral{
	
	/*-*/ private  int N; // LA TAILLE DE LA POPULATION 
	/*-*/ private  double P; // PORBABILITE DU MUTATION 
	/*-*/ private  final TypeAlgorithmeGenetic type; 
	
	public geneticP(TypeAlgorithmeGenetic typeAg) {
		N      =(int)  ((Math.random() * ((100 - 5) + 1)) + 5);
		P      = Math.random();
		setFitness(0);
		type=typeAg;
	}
	
	public geneticP(int n,double p,TypeAlgorithmeGenetic typeAg) {
		N=n; P=p; setFitness(0); type=typeAg;
	}

	@Override
	public geneticP VoisinageP() {
		// TODO Auto-generated method stub
		geneticP voisin=new geneticP(N,P,type);
		// tirer un Nombre aléatoire entre 1-3
		int k=(int)((Math.random() * 2 )+ 1);
				switch (k) {
				case 1:
					voisin.N      =(int)  ((Math.random() * ((100 - 5) + 1)) + 5);
					break;
				case 2:
					voisin.P      = Math.random();
					break;
				default:
					break;
				}
				return voisin;
	}

	@Override
	public void affichage() {
		// TODO Auto-generated method stub
		System.out.println("Paramétres de l'algorithme génétique -> {N="+N+"} {P="+P+"} {fitness="+getFitness()+"}");
	}
	
	public int getN() {
		return N;
	}
	
	public double getP() {
		return P;
	}
	
	public void setN(int n) {
		N=n;
	}
	
	public void setP(double p) {
		P=p;
	}

	public TypeAlgorithmeGenetic getType() {
		return type;
	}

}
