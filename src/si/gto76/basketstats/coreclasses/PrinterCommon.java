package si.gto76.basketstats.coreclasses;

import java.util.Set;

public class PrinterCommon {
	
	/*
	 * Used by Team.toString() and PlayerStat.toString()
	 */
	public static void appendStatsRow(StringBuilder sb, HasStats hs, Team team) {
		// FGM-A 3PM-A FTM-A +/- OFF DEF TOT AST PF ST TO BS BA PTS
		Set<Stat> stats = team.game.recordingStats.values;
		// Scoring
		if (stats.contains(Stat.IIPM)) {
			appendScoringValues(sb, hs, team);
		}
		// Non-scoring
		for (Stat sc : Stat.getNonScoringDisplayableStatsFromRecordables(stats)) {
			// For team totals we don't need plus minus
			if (hs instanceof Team && sc == Stat.PM) {
				sb.append(Util.padTab(""));
				continue;
			}
			sb.append(Util.padTab(hs.get(sc) + ""));
		}
	}
	
	private static void appendScoringValues(StringBuilder sb, HasStats hs, Team team) {
		// FGM-A 3PM-A FTM-A +/-
		Set<Stat> stats = team.game.recordingStats.values;
		if (stats.contains(Stat.IIPF) || stats.contains(Stat.TPF)) {
			sb.append(Util.padTab(hs.get(Stat.FGM)+"-"+hs.get(Stat.FGA)));
		} else {
			sb.append(Util.padTab(hs.get(Stat.FGM) + ""));
		}
		if (stats.contains(Stat.TPM)) {
			if (stats.contains(Stat.TPF)) {
				sb.append(Util.padTab(hs.get(Stat.TPM)+"-"+hs.get(Stat.TPA)));
			} else {
				sb.append(Util.padTab(hs.get(Stat.TPM) + ""));
			}
		}
		if (stats.contains(Stat.FTM)) {
			if (stats.contains(Stat.FTF)) {
				sb.append(Util.padTab(hs.get(Stat.FTM)+"-"+hs.get(Stat.FTA)));
			} else {
				sb.append(Util.padTab(hs.get(Stat.FTM) + ""));
			}
		}
	}
	
}
