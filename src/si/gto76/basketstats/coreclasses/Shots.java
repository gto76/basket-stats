package si.gto76.basketstats.coreclasses;

import java.util.Map;

import si.gto76.basketstats.Util;

public class Shots {
	////////////////////////////////
	Game 	game;
	int 	made2p = 0, 
			missed2p = 0, 
			made3p = 0, 
			missed3p = 0, 
			madeFt = 0, 
			missedFt = 0;
	////////////////////////////////
	
	public Shots(Game game) {
		this.game = game;
	}

	public Shots(Map<Stat,Integer> stats, Game game) {
		this(game);
		Util.assertPositive(stats.values());
		int 
		fgm = getStat(stats, Stat.FGM),
		fga = getStat(stats, Stat.FGA), 
		tpm = getStat(stats, Stat.TPM),
		tpa = getStat(stats, Stat.TPA),
		ftm = getStat(stats, Stat.FTM), 
		fta = getStat(stats, Stat.FTA);
		////
		this.made2p = Math.abs(fgm - tpm);
		this.missed2p = Math.abs(fga - fgm - tpa + tpm);
		this.made3p = Math.abs(tpm);
		this.missed3p = Math.abs(tpa - tpm);
		this.madeFt = Math.abs(ftm);
		this.missedFt = Math.abs(fta - ftm);
	}
	
	////////////////////////////////
	
	/*
	 * SETERS:
	 */

	public int made(Stat stat) {
		return changeState(stat, 1);
	}
	
	public int unMade(Stat stat) {
		return changeState(stat, -1);
	}
	
	private int changeState(Stat stat, int delta) {
		checkIfStatIsRecording(stat);
		switch (stat) {
			case IIPM: made2p += delta; return delta * game.shotPoints.get(Stat.IIPM);
			case IIPF: missed2p += delta; return 0;
			case TPM: made3p += delta; return delta * game.shotPoints.get(Stat.TPM);
			case TPF: missed3p += delta; return 0;
			case FTM: madeFt += delta; return delta * game.shotPoints.get(Stat.FTM);
			case FTF: missedFt += delta; return 0;
			default : throw new IllegalArgumentException("Wrong stat argument.");
		}
	}
	
	/*
	 * GETERS:
	 */
	
	public int get(Stat stat) {
		if (!stat.isScoringValueOrPoints()) {
			throw new IllegalArgumentException("Tried to acces non scoring stat in Shots class.");
		}
		switch (stat) {
			case FGA: return made2p + missed2p + made3p + missed3p;
			case FGM: return made2p + made3p;
			case PTS: return made2p * game.shotPoints.get(Stat.IIPM) 
							+ made3p * game.shotPoints.get(Stat.TPM) 
							+ madeFt * game.shotPoints.get(Stat.FTM);
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

	////////////////////////////////
	
	private void checkIfStatIsRecording(Stat stat) {
		if (!game.recordingStats.values.contains(stat)) {
			throw new IllegalArgumentException("Tried to change non recording stat: " + stat);
		}
	}
	
	private static int getStat(Map<Stat, Integer> stats, Stat stat) {
		Integer statValue = stats.get(stat);
		if (statValue == null) {
			return 0;
		} else {
			return statValue;
		}
	}

}
