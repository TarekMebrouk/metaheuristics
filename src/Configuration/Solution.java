package Configuration;

import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class Solution implements Comparable {

	/*-*/ private final zoneWSN zone;
	/*-*/ private double fitness;
	/*-*/ private int NombreCapteurs;
	/*-*/ private ArrayList<Region> ListeRegions = new ArrayList<>();

	public Solution(zoneWSN Zone, double Fx) {
		zone = Zone;
		fitness = Fx;
		for (Region r : zone.getRegions()) {
			Region region = new Region(r.getX(), r.getY());
			region.setOn(false);
			ListeRegions.add(region);
		}
		NombreCapteurs = 0;
	}

	public Solution(zoneWSN Zone) {
		zone = Zone;
		// Remplissage de la liste
		for (Region r : zone.getRegions()) {
			Region region = new Region(r.getX(), r.getY());
			if (r.isOn()) { region.setOn(true);}
			ListeRegions.add(region);
		}
		// Generation d'une Solution Aléatoire
		do {
			NombreCapteurs = zone.NombreCapteurAleatoir();
			SolutionAleatoir();
		} while (!isValide());
		CalculeFitness();
	}

	public void CalculeFitness() {
		// Calcule fitness f(Sol)
		double Points = CoveredPoints();
		double Couverture = Points / zone.getTotalePoints();
		fitness = Math.pow(100 * Couverture, 2) / NombreCapteurs;
	}

	public int CoveredPoints() {
		double distance;
		int nb = 0;
		ArrayList<Region> listeRegions = new ArrayList<>();

		for (Region region : ListeRegions) {
			if (region.isOn()) {
				for (int i = (region.getX() - zone.getRsens()); i <= (region.getX() + zone.getRsens()); i++) {
					for (int j = (region.getY() - zone.getRsens()); j <= (region.getY() + zone.getRsens()); j++) {
						Region r = new Region(i, j);
						if (ListeRegions.contains(r)) {
							distance = Math.sqrt(
									Math.pow(region.getX() - r.getX(), 2) + Math.pow(region.getY() - r.getY(), 2));
							if (distance <= zone.getRsens() && !listeRegions.contains(r)) {
								nb++;
								listeRegions.add(r);
							}
						}
					}
				}
			}
		}
		return nb;
	}

	public Solution(zoneWSN Zone, ArrayList<Region> liste) {
		zone = Zone;
		// Remplissage de la liste
		for (Region r : liste) {
			Region region = new Region(r.getX(), r.getY());
			if (r.isOn()) { region.setOn(true); }
			ListeRegions.add(region);
		}
		NombreCapteurs = CalculeNombreCapteurs();
		CalculeFitness();
	}

	public int CalculeNombreCapteurs() {
		int i = 0;
		for (Region reg : ListeRegions) {
			if (reg.isOn())
				i++;
		}
		return i;
	}

	public zoneWSN getZone() {
		return zone;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public int getNombreCapteurs() {
		return NombreCapteurs;
	}

	public void setNombreCapteurs(int nombreCapteurs) {
		NombreCapteurs = nombreCapteurs;
	}

	public ArrayList<Region> getListeRegions() {
		return ListeRegions;
	}

	public void setListeRegions(ArrayList<Region> listeRegions) {
		ListeRegions = listeRegions;
	}

	public void SolutionAleatoir() {
		int i = 0, Max = NombreCapteurs;
		for (Region reg : ListeRegions) {
			reg.setOn(false);
		}
		while (i < Max) {
			Solution s = AddSensor();
			this.fitness = s.getFitness();
			this.ListeRegions = s.getListeRegions();
			this.NombreCapteurs = s.getNombreCapteurs();
			i++;
		}
	}

	public boolean isValide() {
		if (!RegionIsValide(zone.getHECN())) {
			return false;
		}
		for (Region region : ListeRegions) {
			if (region.isOn()) {
				if (!RegionIsValide(region)) return false;
			}
		}
		if (ValideWithHECN()) return true;
		return false;
	}

	public ArrayList<Region> ContainedRegionsCommunication(Region region) {
		double distance;
		int r = zone.getRcomm();
		ArrayList<Region> liste = new ArrayList<>();
		for (int i = (region.getX() - 2 * r); i <= (region.getX() + 2 * r); i++) {
			for (int j = (region.getY() - 2 * r); j <= (region.getY() + 2 * r); j++) {
				Region reg = new Region(i, j);
				if (ListeRegions.contains(reg)) {
					reg = ListeRegions.get(ListeRegions.indexOf(reg));
					distance = Math
							.sqrt(Math.pow(region.getX() - reg.getX(), 2) + Math.pow(region.getY() - reg.getY(), 2));
					if (distance <= ( 2 * r) && !region.equals(reg)) {
						liste.add(reg);
					}
				}
			}
		}
		return liste;
	}

	public int NombreCapteurVoisin(Region region) {
		int y = 0;
		ArrayList<Region> liste = ContainedRegionsCommunication(region);
		for (Region sensor : liste) {
			if (sensor.isOn())
				y++;
		}
		return y;
	}

	public boolean RegionIsValide(Region r) {
		// Rayon Communication
		int y = NombreCapteurVoisin(r);
		// Verfication
		if (y >= 1) return true;
		else {
			double distance = Math.sqrt(
					Math.pow(r.getX() - zone.getHECN().getX(), 2) + Math.pow(r.getY() - zone.getHECN().getY(), 2));
			if (distance <=( 2 * zone.getRcomm()) && !r.equals(zone.getHECN())) {
				return true;
			}
		}
		return false;
	}
	
	/******************************************/
	public ArrayList<Region> voisinRegion(Region region) {
		ArrayList<Region> voisin=ContainedRegionsCommunication(region);
		ArrayList<Region> voisinsOn=new ArrayList<>();
		for (Region r: voisin) {
			if(r.isOn())  voisinsOn.add(r);
		}
		return voisinsOn;
	}
	
    public boolean ValideWithHECN() {
    	// Set Liste Sensors Visité 
    	ArrayList<Region> Regions=new ArrayList<>();
        // Set Liste Voisin 
        ArrayList<Region> Voisins=voisinRegion(zone.getHECN());
    	while(!Voisins.isEmpty()) {
    		for (Region r: Voisins) {
    			if (!Regions.contains(r)) Regions.add(r);
    		}
            ArrayList<Region> tempVoisins=new ArrayList<>();
    		for (int i=0;i<Voisins.size();i++) {
    		Region region=Voisins.get(i);
    		ArrayList<Region> liste=voisinRegion(region);
    		for(Region r: liste) {
    			if (!tempVoisins.contains(r) && !Regions.contains(r)) tempVoisins.add(r);
    		}
    		}
    	    Voisins.clear();
    	    for(Region r: tempVoisins) {
    	    	Voisins.add(r);
    	    }
    	}
    	if (Regions.size()==CalculeNombreCapteurs()) return true;
    	return false;
    }
    /*****************************************/
    
	public Solution Voisinage() {
		Solution sol = null;
		// CHOIX PAR CALCULE
		int total = zone.getTotalePoints() / 2;
		int covred = getNombreCapteurs();
		double P = (double) covred / total;
		// Géneration d'un nombre aléatoire K
		double k = Math.random();
		if (k > P) {
			// Ajouter Capteur
			sol = AddSensor();
		} else {
			// Supprimer Capteur
			sol = removeSensor();
		}

		return sol;
	}

	public int getNumberSensorOFF() {
		int cpt = 0;
		for (Region region : ListeRegions) {
			if (!region.isOn())
				cpt++;
		}
		return cpt;
	}

	public Solution AddSensor() {
		Region region = null;
		Solution sol = new Solution(zone, ListeRegions);
        
		// Verifier si HECN est plein
		int NbvoisinHecn = sol.NombreCapteurVoisin(zone.getHECN());
		ArrayList<Region> HECNvoisins = sol.ContainedRegionsCommunication(zone.getHECN());
		do {
			if (region != null) region.setOn(false);
		if (NbvoisinHecn < zone.getNbSensorHECN()) {
			// Ajouter autour de HECN
			region = null;
			while (region == null) {
				region = sol.getRandomRegionFrom(HECNvoisins, false);
			}
			region.setOn(true);
		} else {
			// Ajouter aleatoirement
			region = null;
			while (region == null) {
				Region RandomSensor =null;
				while(RandomSensor == null) {
					 RandomSensor = sol.getRandomRegionFrom(sol.getListeRegions(), true);
				}
				ArrayList<Region> VoisinRandomSensor = sol.ContainedRegionsCommunication(RandomSensor);
				region = sol.getRandomRegionFrom(VoisinRandomSensor, false);
			}
			region.setOn(true);
		}
		} while(!sol.isValide());
		// Mise a jour du Nouveau fitness et du nombre de Capteur
		sol.setNombreCapteurs(sol.CalculeNombreCapteurs());
		sol.CalculeFitness();
		return sol;
	}

	public Solution removeSensor() {
		int i=0;
		Region region = null;
		Solution sol = new Solution(zone, ListeRegions);

		// Verifier si HECN est plein
		ArrayList<Region> HECNvoisins = sol.ContainedRegionsCommunication(zone.getHECN());
		int NbvoisinHecn = sol.NombreCapteurVoisin(zone.getHECN());
		while (i<50) {
			if (region != null) region.setOn(true);
			if (NbvoisinHecn > zone.getNbSensorHECN()) {
				// Remove autour de HECN
				region=null;
				while(region==null) {
					region = sol.getRandomRegionFrom(HECNvoisins, true);	
				}
				region.setOn(false);
			} else {
				// Remove aleatoirement
				region=null;
				while(region==null) {
					region = sol.getRandomRegionFrom(sol.getListeRegions(), true);
				}
				region.setOn(false);
			}
			i++;
			if (sol.isValide()) break;
		}
		if (i>=10) region.setOn(true);
		// Mise a jour du Nouveau fitness et du nombre de Capteur
		sol.setNombreCapteurs(sol.CalculeNombreCapteurs());
		sol.CalculeFitness();
		return sol;
	}

	public Region getRandomRegionFrom(ArrayList<Region> liste, boolean statut) {
		Region region = null;
		int cpt = 0;
		do {
			int x = (int) ((Math.random() * (liste.size())));
			region = liste.get(x);
			cpt++;
		} while (region.isOn() != statut && cpt < liste.size());
		if (region.isOn() == statut) return region;
		return null;
	}

	@Override
	public int compareTo(Object O) {
		Solution o = (Solution) O;
		if (this.fitness == o.fitness)
			return 0;
		else {
			if (this.fitness > o.fitness)
				return 1;
			else
				return -1;
		}
	}

	public void Affichage() {
		System.out.print("{fitness=" + fitness + "},{NombreCapteurs=" + CalculeNombreCapteurs() + "},{Valide?=" + isValide()
				+ "},{ListeCapteurs=");
		for (Region r : ListeRegions) {
			if (r.isOn())
				System.out.print(r);
		}
		System.out.println("}");
	}

}
