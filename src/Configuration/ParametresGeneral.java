package Configuration;

public abstract class ParametresGeneral {

	/*-*/private double fitness;
	
	public abstract ParametresGeneral VoisinageP();
	
	public abstract void affichage();

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
}
