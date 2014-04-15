package si.gto76.basketstats.coreclasses;

import java.util.Map;

public class Shots {
	////////////////////////////////////////
	int 	made2p = 0, 
			missed2p = 0, 
			made3p = 0, 
			missed3p = 0, 
			madeFt = 0, 
			missedFt = 0;
	////////////////////////////////////////
	public Shots() {
	}

	public Shots(int fgm, int fga, int tpm, int tpa, int ftm, int fta) {
		init(fgm, fga, tpm, tpa, ftm, fta);
	}

	public Shots(Map<Stat,Integer> stats) {
		init(getStat(stats,Stat.FGM), getStat(stats,Stat.FGA), 
		getStat(stats,Stat.TPM), getStat(stats,Stat.TPA),
		getStat(stats,Stat.FTM), getStat(stats,Stat.FTA));
	}
	
	private int getStat(Map<Stat, Integer> stats, Stat stat) {
		Integer statValue = stats.get(stat);
		if (statValue == null) {
			return 0;
		} else {
			return statValue;
		}
	}

	private void init(int fgm, int fga, int tpm, int tpa, int ftm, int fta) {
		assertPositive(fgm); assertPositive(fga); assertPositive(tpm); 
		assertPositive(tpa); assertPositive(ftm); assertPositive(fta); 
		this.made2p = Math.abs(fgm - tpm);
		this.missed2p = Math.abs(fga - fgm - tpa + tpm);
		this.made3p = Math.abs(tpm);
		this.missed3p = Math.abs(tpa - tpm);
		this.madeFt = Math.abs(ftm);
		this.missedFt = Math.abs(fta - ftm);
	}
	
	private void assertPositive(int i) {
		if (i < 0) {
			throw new IllegalArgumentException("Some of shot values are negative");
		}
	}
	
	////////////////////////////////////////

	public int made(Stat stat) {
		switch (stat) {
			case IIPM: made2p++; return 2;
			case IIPF: missed2p++; return 0;
			case TPM: made3p++; return 3;
			case TPF: missed3p++; return 0;
			case FTM: madeFt++; return 1;
			case FTF: missedFt++; return 0;
			default : throw new IllegalArgumentException("Wrong stat argument.");
		}
	}
	
	public int unMade(Stat stat) {
		switch (stat) {
			case IIPM: made2p--; return -2;
			case IIPF: missed2p--; return 0;
			case TPM: made3p--; return -3;
			case TPF: missed3p--; return 0;
			case FTM: madeFt--; return -1;
			case FTF: missedFt--; return 0;
			default : throw new IllegalArgumentException("Wrong stat argument.");
		}
	}
	
	public int get(Stat stat) {
		if (!stat.isScoringValueOrPoints()) {
			throw new IllegalArgumentException("Tried to acces non scoring stat in Shots class.");
		}
		switch (stat) {
			case FGA: return made2p + missed2p + made3p + missed3p;
			case FGM: return made2p + made3p;
			case PTS: return made2p * 2 + made3p * 3 + madeFt;
			case TPA: return made3p + missed3p;
			case TPM: return made3p;
			case TPF: return missed3p;
			case IIPM: return made2p;
			case IIPF: return missed2p;
			case FTM: return madeFt; 
			case FTF: return missedFt; 
			case FTA: return madeFt + missedFt;
			default : throw new IllegalArgumentException("The wanted scoring stat geter is not implemented.");
		}
	}

}
