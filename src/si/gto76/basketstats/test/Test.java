package si.gto76.basketstats.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.GameLoader;
import si.gto76.basketstats.Util;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.PlayerStats;
import si.gto76.basketstats.coreclasses.RecordingStats;
import si.gto76.basketstats.coreclasses.Stat;

public class Test {
	
	public static final boolean DISPLAY_TEST_PROGRESS = true;
	public static final boolean DISPLAY_ORIGINAL_GAMES = false;
	public static final boolean DISPLAY_LOADED_GAMES = false;
	
	//////////////////////////////////////////////
	public static Stat[] nbaRecordingStats = {Stat.IIPM, Stat.IIPF, Stat.TPM, Stat.TPF, Stat.FTM, Stat.FTF, Stat.PM, Stat.OFF, Stat.DEF, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA};
	public static Stat[] nbaRecordingStatsSimplified = {Stat.IIPM, Stat.IIPF, Stat.TPM, Stat.TPF, Stat.FTM, Stat.FTF, Stat.PM, Stat.REB, Stat.AST};
	public static Stat[] nbaRecordingStatsSimplifiedNoFtMisses = {Stat.IIPM, Stat.IIPF, Stat.TPM, Stat.TPF, Stat.FTM, Stat.PM, Stat.REB, Stat.AST};
	public static Stat[] nbaRecordingStatsNoMises = {Stat.IIPM, Stat.TPM, Stat.FTM, Stat.PM, Stat.OFF, Stat.DEF, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA};
	public static Stat[] nbaRecordingStatsNoMisesSimplified = {Stat.IIPM, Stat.TPM,  Stat.FTM, Stat.PM, Stat.REB, Stat.AST};
	public static Stat[] streetBallRecordingStats = {Stat.IIPM, Stat.IIPF, Stat.PM, Stat.REB, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA};
	public static Stat[] streetBallRecordingStatsSimplified = {Stat.IIPM, Stat.IIPF, Stat.PM, Stat.REB, Stat.AST};
	public static Stat[] streetBallRecordingStatsNoMisses = {Stat.IIPM, Stat.PM, Stat.REB, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA};
	public static Stat[] streetBallRecordingStatsNoMissesSimplified = {Stat.IIPM, Stat.PM, Stat.REB, Stat.AST};
	public static Stat[] streetBallRecordingStatsNoMissesSimplifiedNoPM = {Stat.IIPM, Stat.REB, Stat.AST};
	public static Stat[] noScorring = {Stat.OFF, Stat.DEF, Stat.AST, Stat.PF, Stat.ST, Stat.TO, Stat.BS, Stat.BA};
	public static Stat[] justFouls = {Stat.PF};
	
	public static Stat[][] differentRecordingArrays = {
		nbaRecordingStats, 
		nbaRecordingStatsSimplified,
		nbaRecordingStatsSimplifiedNoFtMisses,
		nbaRecordingStatsNoMises,
		nbaRecordingStatsNoMisesSimplified,
		streetBallRecordingStats,
		streetBallRecordingStatsSimplified,
		streetBallRecordingStatsNoMisses,
		streetBallRecordingStatsNoMissesSimplified,
		streetBallRecordingStatsNoMissesSimplifiedNoPM,
		noScorring,
		justFouls
	};
	
	//////////////////////////////////////////////
	
	public static void main(String[] args) {
//		int retVal = simpleSaveLoadTest();
//		if (retVal == 0) {
//			print("Save/Load test PASSED.");
//		} else {
//			print("Save/Load test FAILED.");
//		}
		
		int retVal = comprehensiveSaveLoadTest();
		if (retVal == 0) {
			print("Save/Load test PASSED.");
		} else {
			print("Save/Load test FAILED.");
		}
		
//		int retVal = recordingStatsTest();
//		if (retVal == 0) {
//			print("recordingStatsTest test PASSED.");
//		} else {
//			print("recordingStatsTest test FAILED.");
//		}
	}
	
	//////////////////////////////////////////////
	

	/*
	 * COMPREHENSIVE SAVE/LOAD TEST
	 */
	public static int comprehensiveSaveLoadTest() {
		
		// for all combinations of input stats
		Set<Set<Stat>> allCombinations = powerSet(new HashSet<Stat>(Arrays.asList(Stat.inputValues)));
		// get valid recording stats
		Set<Set<Stat>> allValidCombinations = new HashSet<Set<Stat>>();
		for (Set<Stat> combination : allCombinations) {
			Set<Stat> validComb = RecordingStats.getValidSet(combination);
			allValidCombinations.add(validComb);
		}
		// run save load test per game
		int i = 1;
		for (Set<Stat> validComb : allValidCombinations) {
			RecordingStats recordingStats = new RecordingStats(validComb);
			if (DISPLAY_TEST_PROGRESS) System.out.println("### COMPREHENSIVE SAVE LOAD TEST NO " +i+ ": " +recordingStats+ " ###\n");
			int retVal = testSaveLoadForGame(recordingStats);
			if (retVal != 0) {
				return retVal;
			}
			i++;
		}
		return 0;
	}
	
	
	/*
	 * SIMPLE SAVE/LOAD TEST
	 */
	public static int simpleSaveLoadTest() {
		// For some combinations of recording stats
		int i = 1;
		for (Stat[] stats : differentRecordingArrays) {
			if (DISPLAY_TEST_PROGRESS) System.out.println("### TEST NO " +i+ ": " +Arrays.toString(stats)+ " ###\n");
			int retVal = testSaveLoadForGame(new RecordingStats(Util.arrayToSet(stats)));
			if (retVal != 0) {
				return retVal;
			}
			i++;
		}
		return 0; // Test passed.
	}
	
	/*
	 * SINGLE GAME SAVE/LOAD TEST
	 */
	public static int testSaveLoadForGame(RecordingStats recordingStats) {
		// create new game,
		Game game = Conf.getDefaultGame(recordingStats);
		// fill one player with each stat once
		PlayerStats playerStats = getFirstPlayersStats(game);
		for (Stat stat : game.recordingStats.values) {
			if (stat == Stat.PM) {
				continue;
			}
			playerStats.made(stat);
		}
		// create new game by sending game string to game loader
		String gameString = game.toString();
		if (DISPLAY_ORIGINAL_GAMES) System.out.println("# ORIGINAL GAME: #\n"+game);
		Game loadedGame = GameLoader.createGameFromString(gameString);
		if (DISPLAY_LOADED_GAMES) System.out.println("# LOADED GAME: #\n"+loadedGame);
		// and compare strings
		if (!loadedGame.toString().equals(gameString)) {
			System.out.println("## TEST FAILED AT GAME: ##" 
							+ "\n# ORIGINAL GAME: #" + gameString 
							+ "\n# LOADED GAME: #" + loadedGame);
			return 1; // Error: Game and loaded game are different
		}
		return 0;
	}

	private static PlayerStats getFirstPlayersStats(Game game) {
		Player firstPlayer = game.getTeam1().getAllPlayersStats().keySet().iterator().next();
		return game.getPlayersStats(firstPlayer);
	}
	
	/*
	 * RECORDING STATS TEST
	 */
	public static int recordingStatsTest() {
		// for all combinations of Input Stat run getValidSet
		Set<Set<Stat>> allCombinations = powerSet(new HashSet<Stat>(Arrays.asList(Stat.inputValues)));
		for (Set<Stat> combination : allCombinations) {
			Set<Stat> orderedStats = Util.getOrderedSet(combination);
			Set<Stat> validSet = RecordingStats.getValidSet(combination);
			if (DISPLAY_LOADED_GAMES) System.out.println("# ORIGINAL SET: # " + Arrays.toString(orderedStats.toArray()));
			if (DISPLAY_LOADED_GAMES) System.out.println("# CORECTED SET: # " + Arrays.toString(validSet.toArray()) + "\n");
			if (!RecordingStats.isValidSet(validSet)) {
				return 1;
			}
		}
		return 0;
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
	
	public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
	    Set<Set<T>> sets = new HashSet<Set<T>>();
	    if (originalSet.isEmpty()) {
	    	sets.add(new HashSet<T>());
	    	return sets;
	    }
	    List<T> list = new ArrayList<T>(originalSet);
	    T head = list.get(0);
	    Set<T> rest = new HashSet<T>(list.subList(1, list.size())); 
	    for (Set<T> set : powerSet(rest)) {
	    	Set<T> newSet = new HashSet<T>();
	    	newSet.add(head);
	    	newSet.addAll(set);
	    	sets.add(newSet);
	    	sets.add(set);
	    }		
	    return sets;
	}

}
