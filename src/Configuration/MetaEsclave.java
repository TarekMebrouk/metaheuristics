package Configuration;

public abstract class MetaEsclave implements Comparable<MetaEsclave> {
	/*-*/ private double rang;
	/*-*/ private Solution meilleurSOl;
	/*-*/ private int NombreSol;
	
	public double getRang() {
		return rang;
	}
	
	public void MajRang(double r) {
		this.rang = this.rang+r;
	}
	
	public Solution getMeilleurSOl() {
		return meilleurSOl;
	}
	
	public void setMeilleurSOl(Solution meilleurSOl) {
		this.meilleurSOl = meilleurSOl;
	}
	
	@Override
	public int compareTo(MetaEsclave meta) {
		if (meta.rang==this.rang) return 0;
		else {
			if (this.rang>meta.rang) return 1;
			else return -1;
		}
	}
	
	public int getNombreSol() {
		return NombreSol;
	}
	
	public void setNombreSol(int nombreSol) {
		NombreSol = nombreSol;
	}

	public void setRang (double r) {
		rang=r;
	}
	
	public abstract void function();
}
