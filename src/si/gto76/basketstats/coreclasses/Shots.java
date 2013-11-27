package si.gto76.basketstats.coreclasses;

public class Shots {

	int 	made2p = 0, 
			missed2p = 0,
			made3p = 0, 
			missed3p = 0;

	public int getFgm() {
		return made2p + made3p;
	}
	public int getFga() {
		return made2p + missed2p + made3p + missed3p;
	}
	public int getTpm() {
		return made3p;
	}
	public int getTpa() {
		return made3p + missed3p;
	}	
	
	public int getPts() {
		return made2p*2 + made3p*3;
	}
	
}
