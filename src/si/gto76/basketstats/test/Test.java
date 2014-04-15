package si.gto76.basketstats.test;

import java.util.Set;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.GameLoader;
import si.gto76.basketstats.Util;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Stat;

public class Test {
	
	public static int main(String[] args) {
		int retVal = saveLoadTest();
		System.out.println("saveLoadTest: " + retVal);
		return retVal;
	}
	
	public static Stat[] nbaRecordingStats = {Stat.IIPM, Stat.IIPF, Stat.TPM, Stat.TPF, Stat.PM, Stat.OFF, Stat.DEF, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS};
	public static Stat[] nbaRecordingStatsSimplified = {Stat.IIPM, Stat.IIPF, Stat.TPM, Stat.TPF, Stat.PM, Stat.REB, Stat.AST};
	public static Stat[] nbaRecordingStatsNoMises = {Stat.IIPM, Stat.TPM, Stat.PM, Stat.OFF, Stat.DEF, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS};
	public static Stat[] nbaRecordingStatsNoMisesSimplified = {Stat.IIPM, Stat.TPM, Stat.PM, Stat.REB, Stat.AST};
	public static Stat[] streetBallRecordingStats = {Stat.IIPM, Stat.IIPF, Stat.PM, Stat.REB, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS};
	public static Stat[] streetBallRecordingStatsSimplified = {Stat.IIPM, Stat.IIPF, Stat.PM, Stat.REB, Stat.AST};
	public static Stat[] streetBallRecordingStatsNoMisses = {Stat.IIPM, Stat.PM, Stat.REB, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS};
	public static Stat[] streetBallRecordingStatsNoMissesSimplified = {Stat.IIPM, Stat.PM, Stat.REB, Stat.AST};
	
	public static Stat[][] differentRecordingArrays = {
		nbaRecordingStats, 
		nbaRecordingStatsSimplified,
		nbaRecordingStatsNoMises,
		nbaRecordingStatsNoMisesSimplified,
		streetBallRecordingStats,
		streetBallRecordingStatsSimplified,
		streetBallRecordingStatsNoMisses,
		streetBallRecordingStatsNoMissesSimplified
	};
	
	public static int saveLoadTest() {
		// For some kombinations of recording stats
		for (Stat[] stats : differentRecordingArrays) {
			// create new game,
			Game game = Conf.getDefaultGame(Util.arrayToSet(stats));
			// fill one player with each stat once
			Set<Stat> recordingStats = game.getTeam1().getRecordingStats();
			game.getTeam1().getAllPlayersStats().keySet(); //TODO
			// save game, kep string in memory -> dont need to save it actualy, just send string to game loader
			String gameString = game.toString();
			Game loadedGame = GameLoader.createGameFromString(gameString);
			// and compare strings
			if (!loadedGame.toString().equals(gameString)) {
				return 1; // Error: Game and loaded game are different
			}
		}
		return 0; // Test passed.
	}

}
