package si.gto76.basketstats.coreclasses;

import java.util.Map;
import java.util.Set;


/**
 * Stores players shooting stats.
 */
public class ShotRecorder {
	////////////////////////////////
	Game 	game;
	int 	made2p = 0, 
			missed2p = 0, 
			made3p = 0, 
			missed3p = 0, 
			madeFt = 0, 
			missedFt = 0;
	////////////////////////////////
	
	public ShotRecorder(Game game) {
		this.game = game;
	}

	public ShotRecorder(Map<Stat,Integer> statsWithValues, Game game) {
		this(game);
		boolean mapIsNotValid = !mapIsValid(statsWithValues);
		if (mapIsNotValid) {
			throw new IllegalArgumentException("Some of shot values are negative, "
						+ statsWithValues.values().toString());
		}
		
		int 
		fgm = Util.zeroIfNull(statsWithValues.get(Stat.FGM)),
		fga = Util.zeroIfNull(statsWithValues.get(Stat.FGA)), 
		tpm = Util.zeroIfNull(statsWithValues.get(Stat.TPM)),
		tpa = Util.zeroIfNull(statsWithValues.get(Stat.TPA)),
		ftm = Util.zeroIfNull(statsWithValues.get(Stat.FTM)), 
		fta = Util.zeroIfNull(statsWithValues.get(Stat.FTA));
		// If there are no recorded attemts, then missed values become negative,
		// so in that case they are just set to zero:
		this.made2p = Util.zeroOrPositive(fgm - tpm);
		this.missed2p = Util.zeroOrPositive(fga - fgm - tpa + tpm);
		this.made3p = Util.zeroOrPositive(tpm);
		this.missed3p = Util.zeroOrPositive(tpa - tpm);
		this.madeFt = Util.zeroOrPositive(ftm);
		this.missedFt = Util.zeroOrPositive(fta - ftm);
	}

	
	public static boolean mapIsValid(Map<Stat,Integer> statsWithValues) {
		boolean someValuesAreNotPositive = !Util.areAllPositive(statsWithValues.values());
		if (someValuesAreNotPositive) {
			return false;
		}
		return true;
	}

	////////////////////////////////
	
	/*
	 * SETERS:
	 */

	public int made(Stat stat) {
		assertStatIsRecording(stat);
		return changeState(stat, 1);
	}

	/**
	 * Checks if stat is being teacked. (Its being tracked if it is in games RecordingStats.values set)
	 * Now checked only when made, so we can undo stats that are no longer tracked.
	 */
	private void assertStatIsRecording(Stat stat) {
		boolean stat_is_not_in_recording_stats = !game.recordingStats.values.contains(stat);
		if (stat_is_not_in_recording_stats) {
			throw new IllegalArgumentException("Tried to change non recording stat: " + stat);
		}
	}
	
	public int unMade(Stat stat) {
		return changeState(stat, -1);
	}
	
	private int changeState(Stat stat, int delta) {
		switch (stat) {
			case IIPM: made2p += delta; return delta * game.shotValues.values.get(Stat.IIPM);
			case IIPF: missed2p += delta; return 0;
			case TPM: made3p += delta; return delta * game.shotValues.values.get(Stat.TPM);
			case TPF: missed3p += delta; return 0;
			case FTM: madeFt += delta; return delta * game.shotValues.values.get(Stat.FTM);
			case FTF: missedFt += delta; return 0;
			default : throw new IllegalArgumentException("Wrong stat argument: " +stat);
		}
	}
	
	/*
	 * GETERS:
	 */
	
	public int get(Stat stat) {
		if (!stat.isScoringOrPoints()) {
			throw new IllegalArgumentException("Tried to acces non scoring stat in Shots class.");
		}
		switch (stat) {
			case FGA: return made2p + missed2p + made3p + missed3p;
			case FGM: return made2p + made3p;
			case TPA: return made3p + missed3p;
			case TPM: return made3p;
			case TPF: return missed3p;
			case IIPM: return made2p;
			case IIPF: return missed2p;
			case FTM: return madeFt; 
			case FTF: return missedFt; 
			case FTA: return madeFt + missedFt;
			case PTS: return getScore();
			default : throw new IllegalArgumentException("Scoring stat geter is not implemented for: " +stat);
		}
	}

	private int getScore() {
		int pts = 0;
		Set<Stat> recordingStats = game.recordingStats.values;
		Map<Stat,Integer> shotValues = game.shotValues.values;
		if (recordingStats.contains(Stat.IIPM)) {
			pts += made2p * shotValues.get(Stat.IIPM);
		}
		if (recordingStats.contains(Stat.TPM)) {
			pts += made3p * shotValues.get(Stat.TPM);
		}
		if (recordingStats.contains(Stat.FTM)) {
			pts += madeFt * shotValues.get(Stat.FTM);
		}
		return pts;
	}

	public boolean allValuesAreZero() {
		if (made2p == 0 && missed2p == 0 && made3p == 0 
				&& missed3p == 0 && madeFt == 0 && missedFt == 0) {
			return true;
		}
		return false;
	}

}
