package si.gto76.basketstats.coreclasses;

import java.text.DecimalFormat;
import java.util.Set;

import si.gto76.basketstats.Conf;

public class PrinterTeam {
	private static final DecimalFormat ONE_DIGIT = new DecimalFormat("#,##0.0");
	////////////////////////
	private final Team team;
	////////////////////////
	public static String toString(Team team) {
		PrinterTeam teamPrinter = new PrinterTeam(team);
		return teamPrinter.teamToString();
	}
	private PrinterTeam(Team team) {
		this.team = team;
	}
	////////////////////////
	
	private String teamToString() {
		StringBuilder sb = new StringBuilder();
		appendHeader(sb);
		appendPlayerStats(sb);
		appendTotals(sb);
		appendPercents(sb);
		return sb.toString();
	}

	private void appendHeader(StringBuilder sb) {
		//FGM-A 3PM-A FTM-A +/- OFF DEF TOT AST PF ST TO BS BA PTS
		sb.append(team.getName()).append("\n").
		append(emptyPlayersName());
		appendScoringHeader(sb);
		appendNonScoringHeader(sb);
	}
	
	private void appendScoringHeader(StringBuilder sb) {
		Set<Stat> stats = team.game.recordingStats.values;
		if (!stats.contains(Stat.IIPM)) {
			return;
		}
		////
		if (stats.contains(Stat.IIPF) || 
				stats.contains(Stat.TPF)) {
			sb.append(Util.padTab("FGM-A"));
		} else {
			sb.append(Util.padTab("FGM"));
		}
		if (stats.contains(Stat.TPM)) {
			if (stats.contains(Stat.TPF)) {
				sb.append(Util.padTab("3PM-A"));
			} else {
				sb.append(Util.padTab("3PM"));
			}
		}
		if (stats.contains(Stat.FTM)) {
			if (stats.contains(Stat.FTF)) {
				sb.append(Util.padTab("FTM-A"));
			} else {
				sb.append(Util.padTab("FTM"));
			}
		}
	}
	
	private void appendNonScoringHeader(StringBuilder sb) {
		for (Stat sc : Stat.getNonScoringDisplayableStatsFromRecordables(team.game.recordingStats.values)) {
			sb.append(Util.padTab(sc.getName().toUpperCase()));
		}
		sb.append("\n");
	}

	private void appendPlayerStats(StringBuilder sb) {
		for (Player player : team.getAllPlayersStatRecorders().keySet()) {
			String playersName = player.getName();
			sb.append(Util.padEnd(playersName, Conf.PLAYER_NAME_WIDTH, ' ')).
			append(team.getAllPlayersStatRecorders().get(player)).append("\n");
		}
	}
	
	private void appendTotals(StringBuilder sb) {
		sb.append(Util.padEnd(GameLoader.TOTALS_KEYWORD, Conf.PLAYER_NAME_WIDTH, ' '));		
		PrinterCommon.appendStatsRow(sb, team, team);
		sb.append("\n");
	}
	
	private void appendPercents(StringBuilder sb) {
		Set<Stat> stats = team.game.recordingStats.values;
		sb.append(emptyPlayersName());
		if (stats.contains(Stat.IIPF)) {				
			sb.append(Util.padTab(ONE_DIGIT.format(team.getFgPercent())+"%") );
		} else {
			sb.append(Util.padTab(""));
		}
		if (stats.contains(Stat.TPF)) {				
			sb.append(Util.padTab(ONE_DIGIT.format(team.getTpPercent())+"%") );
		} else {
			if (stats.contains(Stat.TPM)) {
				sb.append(Util.padTab(""));
			}
		}
		if (stats.contains(Stat.FTF)) {				
			sb.append(Util.padTab(ONE_DIGIT.format(team.getFtPercent())+"%") );
		}
		sb.append("\n");
	}
	
	///////////////////////////////
	
	private static String emptyPlayersName() {
		return Util.padEnd("", Conf.PLAYER_NAME_WIDTH, ' ');
	}
	
}
