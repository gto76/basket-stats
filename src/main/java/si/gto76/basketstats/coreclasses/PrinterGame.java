package si.gto76.basketstats.coreclasses;

import java.util.Set;

import si.gto76.basketstats.Conf;

public class PrinterGame {
	private final String ONE_TAB_LINE = "--------";
	////////////////////////
	private final Game game;
	////////////////////////
	public static String toString(Game game) {
		PrinterGame gamePrinter = new PrinterGame(game);
		return gamePrinter.gameToString();
	}
	private PrinterGame(Game game) {
		this.game = game;
	}
	////////////////////////

	private String gameToString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n").append(lb())
		.append(game.getDate()).append("\n")
		.append(game.getVenue()).append("\n").append(lb())
		.append(spaces(game.shotValues.values.get(Stat.FTM))).append("\n")
		.append(game.getTeam1().getName()).append(": ").append(game.getTeam1().get(Stat.PTS)).append("\n")
		.append(spaces(game.shotValues.values.get(Stat.IIPM))).append("\n")
		.append(game.getTeam2().getName()).append(": ").append(game.getTeam2().get(Stat.PTS)).append("\n")
		.append(spaces(game.shotValues.values.get(Stat.TPM))).append("\n").append(lb())
		.append("BOX SCORE:\n").append(lb())
		.append(game.getTeam1()).append(lb())
		.append(game.getTeam2()).append(lb());
		return sb.toString();
	}
	
	private static String spaces(int noOfSpaces) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < noOfSpaces; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}
	
	private String lb() {
		StringBuffer sb = new StringBuffer();
		Set<Stat> boxStats = Stat.getDisplayableStatsFromRecordables(game.recordingStats.values);
		boxStats.remove(Stat.FGA);
		boxStats.remove(Stat.TPA);
		boxStats.remove(Stat.FTA);
		for (int i = boxStats.size() + Conf.NUMBER_OF_TABS_FOR_PLAYER_NAME; i > 0; i--) {
			sb.append(ONE_TAB_LINE);
		}
		sb.append("\n");
		return sb.toString();
	}

}
