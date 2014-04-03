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
	
	public Integer made(Stat stat) {
		switch (stat) {
			case IIPM: made2p++; return 2;
			case IIPF: missed2p++; return null;
			case TPM: made3p++; return 3;
			case TPF: missed3p++; return null;
			default : throw new IllegalArgumentException("Wrong stat argument.");
		}
	}
	
	public Integer unMade(Stat stat) {
		switch (stat) {
			case IIPM: made2p--; return -2;
			case IIPF: missed2p--; return null;
			case TPM: made3p--; return -3;
			case TPF: missed3p--; return null;
			default : throw new IllegalArgumentException("Wrong stat argument.");
		}
	}
	
	public int get(Stat stat) {
		if (!stat.isScoringValue()) {
			throw new IllegalArgumentException("Tried to acces non scoring stat in Shots class.");
		}
		switch (stat) {
			case FGA: return made2p + missed2p + made3p + missed3p;
			case FGM: return made2p + made3p;
			case PTS: return made2p * 2 + made3p * 3;
			case TPA: return made3p + missed3p;
			case TPM: return made3p;
			case TPF: return missed3p;
			case IIPM: return made2p;
			case IIPF: return missed2p;
			default : throw new IllegalArgumentException("The wanted scoring stat geter is not implemented.");
		}
	}

}
