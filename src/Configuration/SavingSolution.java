package Configuration;

public class SavingSolution {

	private Solution solution;
	private int typeMeta;  // 1-> recuit Simulé 2-> genetic 3-> BBO 
	private int rang; 

	public SavingSolution(MetaEsclave meta,Solution s,int r) {
		solution=s; 
		if(meta instanceof recuitSimuleM) typeMeta=1;
		if(meta instanceof geneticM)      typeMeta=2;
		if(meta instanceof BBoM)          typeMeta=3;
		rang=r;
	}

	/**
	 * @return the solution
	 */
	public Solution getSolution() {
		return solution;
	}

	/**
	 * @param solution the solution to set
	 */
	public void setSolution(Solution solution) {
		this.solution = solution;
	}

	/**
	 * @return the typeMeta
	 */
	public int getTypeMeta() {
		return typeMeta;
	}

	/**
	 * @param typeMeta the typeMeta to set
	 */
	public void setTypeMeta(int typeMeta) {
		this.typeMeta = typeMeta;
	}

	/**
	 * @return the rang
	 */
	public int getRang() {
		return rang;
	}

	/**
	 * @param rang the rang to set
	 */
	public void setRang(int rang) {
		this.rang = rang;
	}
	
}
