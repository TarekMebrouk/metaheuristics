package Configuration;


@SuppressWarnings("rawtypes")
public class SolutionBBO implements Comparable{
	
     /*-*/ private    int k;    // Nombre d'éspeces 
	 /*-*/ private double λ;    // λk = I ( 1 - k/N )
	 /*-*/ private double µ;    // µk = E *( K/N )
	 /*-*/ private    int N;    // Nombre maximum d'éspeces 
	 /*-*/ private Solution solution; // Solution courante 

	SolutionBBO(Solution s,int n,int K){
		solution=s;   N=n; k=K;
	}
	
	public void Calculeλ(double I) {
		λ= I*(1-((double)k/N));
	}
	
	public void Calculeµ(double E) {
		µ= E*((double)k/N);
	}
	
	public void setK(int nb) {
		k=nb;
	}
	
	public int getK() {
		return k;
	}
	@Override
	public int compareTo(Object O) {
		SolutionBBO o = (SolutionBBO) O;
		if (this.solution.getFitness() == o.solution.getFitness())
			return 0;
		else {
			if (this.solution.getFitness() > o.solution.getFitness())
				return 1;
			else
				return -1;
		}
	}

	public double getλ() {
		return λ;
	}

	public void setλ(double λ) {
		this.λ = λ;
	}

	public double getµ() {
		return µ;
	}

	public void setµ(double μ) {
		µ = μ;
	}
	
	public Solution getSolution() {
		return solution;
	}
	
	public void setSolution(Solution s) {
		solution=s;
	}
	
	public void Afficher() {
		System.out.print("<K="+k+">,<λ="+λ+">,<µ="+µ+">, "); solution.Affichage();
	}
	
}
