package si.gto76.basketstats.test;

import java.util.Arrays;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.GameLoader;
import si.gto76.basketstats.Util;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.PlayerStats;
import si.gto76.basketstats.coreclasses.Stat;

public class Test {
	public static final boolean DISPLAY = true;
	//////////////////////////////////////////////
	public static Stat[] nbaRecordingStats = {Stat.IIPM, Stat.IIPF, Stat.TPM, Stat.TPF, Stat.FTM, Stat.FTF, Stat.PM, Stat.OFF, Stat.DEF, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA};
	public static Stat[] nbaRecordingStatsSimplified = {Stat.IIPM, Stat.IIPF, Stat.TPM, Stat.TPF, Stat.FTM, Stat.FTF, Stat.PM, Stat.REB, Stat.AST};
	public static Stat[] nbaRecordingStatsNoMises = {Stat.IIPM, Stat.TPM, Stat.FTM, Stat.PM, Stat.OFF, Stat.DEF, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA};
	public static Stat[] nbaRecordingStatsNoMisesSimplified = {Stat.IIPM, Stat.TPM,  Stat.FTM, Stat.PM, Stat.REB, Stat.AST};
	public static Stat[] streetBallRecordingStats = {Stat.IIPM, Stat.IIPF, Stat.PM, Stat.REB, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA};
	public static Stat[] streetBallRecordingStatsSimplified = {Stat.IIPM, Stat.IIPF, Stat.PM, Stat.REB, Stat.AST};
	public static Stat[] streetBallRecordingStatsNoMisses = {Stat.IIPM, Stat.PM, Stat.REB, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA};
	public static Stat[] streetBallRecordingStatsNoMissesSimplified = {Stat.IIPM, Stat.PM, Stat.REB, Stat.AST};
	public static Stat[] streetBallRecordingStatsNoMissesSimplifiedNoPM = {Stat.IIPM, Stat.REB, Stat.AST};
	
	public static Stat[][] differentRecordingArrays = {
		nbaRecordingStats, 
		nbaRecordingStatsSimplified,
		nbaRecordingStatsNoMises,
		nbaRecordingStatsNoMisesSimplified,
		streetBallRecordingStats,
		streetBallRecordingStatsSimplified,
		streetBallRecordingStatsNoMisses,
		streetBallRecordingStatsNoMissesSimplified,
		streetBallRecordingStatsNoMissesSimplifiedNoPM
	};
	
	//////////////////////////////////////////////
	
	public static void main(String[] args) {
		int retVal = saveLoadTest();
		if (retVal == 0) {
			print("Save/Load test PASSED.");
		} else {
			print("Save/Load test FAILED.");
		}
	}
	
	//////////////////////////////////////////////
	
	/*
	 * SAVE/LOAD
	 */
	
	public static int saveLoadTest() {
		// For some combinations of recording stats
		int i = 0;
		for (Stat[] stats : differentRecordingArrays) {
			if (DISPLAY) System.out.println("### TEST NO " +i+ ": " +Arrays.toString(stats)+ " ###\n");
			// create new game,
			Game game = Conf.getDefaultGame(Util.arrayToSet(stats));
			// fill one player with each stat once
			PlayerStats playerStats = getFirstPlayersStats(game);
			for (Stat stat : game.recordingStats) {
				if (stat == Stat.PM) {
					continue;
				}
				playerStats.made(stat);
			}
			// create new game by sending game string to game loader
			String gameString = game.toString();
			if (DISPLAY) System.out.println("# ORIGINAL GAME: #\n"+game);
			Game loadedGame = GameLoader.createGameFromString(gameString);
			if (DISPLAY) System.out.println("# LOADED GAME: #\n"+loadedGame);
			// and compare strings
			if (!loadedGame.toString().equals(gameString)) {
				return 1; // Error: Game and loaded game are different
			}
			i++;
		}
		return 0; // Test passed.
	}

	private static PlayerStats getFirstPlayersStats(Game game) {
		Player firstPlayer = game.getTeam1().getAllPlayersStats().keySet().iterator().next();
		return game.getPlayersStats(firstPlayer);
	}
	
	//////////////////////////////////////////////
	
	/*
	 * UTIL:
	 */

	private static void print(String message) {
		StringBuilder sb = new StringBuilder();
		appendDashes(sb, message.length()+4);
		sb.append("\n# ").append(message).append(" #\n");
		appendDashes(sb, message.length()+4);
		System.out.println(sb);
	}

	private static void appendDashes(StringBuilder sb, int length) {
		for (int i = 0; i < length; i++) {
			sb.append("#");
		}
	}


}
