package Configuration;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Region implements Serializable {
	
	/*-*/ private int x, y;
	/*-*/ private boolean On;

	public Region(int X, int Y) {
		setX(X);
		setY(Y);
		On =false;
	}
	
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isOn() {
		return On;
	}

	public void setOn(boolean on) {
		On = on;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void BasculeStatut() {
		if(isOn()) On=false; 
		else On=true; 
	}
    
	@Override 
	public boolean equals(Object o) {
		Region r= (Region) o;
		if (r.x==this.x && r.y==this.y) return true;
		else return false;
	}
	
	@Override 
	public String toString() {
		return " ["+x+","+y+","+On+"] ";
	}
}
