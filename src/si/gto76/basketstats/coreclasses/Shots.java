package si.gto76.basketstats.coreclasses;

public class Shots {
	////////////////////////////////////////
	int made2p = 0, missed2p = 0, made3p = 0, missed3p = 0;
	////////////////////////////////////////
	public Shots() {
	}

	public Shots(int fgm, int fga, int tpm, int tpa) {
		this.made2p = fgm - tpm;
		this.missed2p = fga - fgm - tpa + tpm;
		this.made3p = tpm;
		this.missed3p = tpa - tpm;
	}
	////////////////////////////////////////
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
	public int getTpf() {
		return missed3p;
	}
	public int get2pm() {
		return made2p;
	}
	public int get2pf() {
		return missed2p;
	}
	public int getPts() {
		return made2p * 2 + made3p * 3;
	}

}
