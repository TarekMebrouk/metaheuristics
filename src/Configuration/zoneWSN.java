package Configuration;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class zoneWSN implements Serializable{
	/*-*/ private int xAxis, yAxis;
	/*-*/ private int TotalePoints;
	/*-*/ private int Rcomm, Rsens;
	/*-*/ private Region HECN;
	/*-*/ private final int NbSensorHECN;
	/*-*/ private ArrayList<Region> Regions=new ArrayList<>();

	public zoneWSN(int x, int y,int rcomm,int rsens,Region hecn,ArrayList<Region> regions) {
		setxAxis(x);
		setyAxis(y);
		Regions=regions;
		setTotalePoints(Regions.size());
		Rcomm=rcomm; 
		Rsens=rsens;
		HECN=hecn;
		NbSensorHECN=CalculeNbSensorHECN();
	}

	public int CalculeNbSensorHECN() {
		double rapport=(double)Rsens/Rcomm;
		rapport=rapport*10;
		int x=(int)rapport;
		switch (x) {
		case  5: return 6;
		case  6: return 5;
		case  7: return 4;
		case  8: return 4;
		case  9: return 3;
		case 10: return 3; 
		case 11: return 3;
		case 12: return 3;
		case 13: return 3;
		case 14: return 3;
		case 15: return 3;
		case 16: return 3;
		case 17: return 2;
		case 18: return 2;
		case 19: return 2;
		case 20: return 2;
		case 21: return 2;
		case 22: return 2;
		case 23: return 2;
		case 24: return 2;
		case 25: return 2;
		default: break;
		}
		return -1;
	}
	
	public int getyAxis() {
		return yAxis;
	}

	public void setyAxis(int yAxis) {
		this.yAxis = yAxis;
	}

	public int getxAxis() {
		return xAxis;
	}

	public void setxAxis(int xAxis) {
		this.xAxis = xAxis;
	}

	public int getRcomm() {
		return Rcomm;
	}

	public void setRcomm(int rcomm) {
		Rcomm = rcomm;
	}

	public int getRsens() {
		return Rsens;
	}

	public void setRsens(int rsens) {
		Rsens = rsens;
	}

	public Region getHECN() {
		return HECN;
	}

	public void setHECN(Region hECN) {
		HECN = hECN;
	}
	
	public int NombreCapteurAleatoir() {
		double max, min;
		max = TotalePoints/2;
		min = 1;
		return (int)((Math.random() * ((max - min) + 1)) + min);
	}

	public int getTotalePoints() {
		return TotalePoints;
	}

	public void setTotalePoints(int totalePoints) {
		TotalePoints = totalePoints;
	}

	public ArrayList<Region> getRegions() {
		return Regions;
	}

	public void setRegions(ArrayList<Region> regions) {
		Regions = regions;
	}

	public int getNbSensorHECN() {
		return NbSensorHECN;
	}

}
