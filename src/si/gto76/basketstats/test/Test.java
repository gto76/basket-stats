package si.gto76.basketstats.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.Conf.StatComb;
import si.gto76.basketstats.GameLoader;
import si.gto76.basketstats.Util;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.PlayerStats;
import si.gto76.basketstats.coreclasses.RecordingStats;
import si.gto76.basketstats.coreclasses.ShotValues;
import si.gto76.basketstats.coreclasses.Stat;

public class Test {
	
	public static final boolean COMPREHENSIVE_SAVE_LOAD_TEST = true;
	public static final boolean SIMPLE_SAVE_LOAD_TEST = false;
	public static final boolean RECORDING_STATS_TEST = false;
	
	public static final boolean DISPLAY_TEST_PROGRESS = true;
	public static final boolean DISPLAY_ORIGINAL_GAMES = false;
	public static final boolean DISPLAY_LOADED_GAMES = false;
	
	//////////////////////////////////////////////
	
	public static void main(String[] args) {
		if (SIMPLE_SAVE_LOAD_TEST) {
			printTestResult("Simple Save/Load test", simpleSaveLoadTest());
		}
		if (COMPREHENSIVE_SAVE_LOAD_TEST) {
			printTestResult("Comprehensive Save/Load test", comprehensiveSaveLoadTest());
		}
		if (RECORDING_STATS_TEST) {
			printTestResult("Recording Stats Test", recordingStatsTest());
		}
	}
	
	private static void printTestResult(String testName, int returnValue) {
		if (returnValue == 0) {
			print(testName + " PASSED.");
		} else {
			print(testName + " FAILED.");
		}
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
			//if (DISPLAY_TEST_PROGRESS) System.out.println("### COMPREHENSIVE SAVE LOAD TEST NO " +i+ ": " +recordingStats+ " ###\n");
			//int retVal = testWithAllShotValues(recordingStats, i);
			int retVal = testSaveLoadForGame(recordingStats, ShotValues.DEFAULT);
			if (retVal != 0) {
				return retVal;
			}
			i++;
		}
		return 0;
	}
	
	/////
	
	private static int testWithAllShotValues(RecordingStats recordingStats, int i) {
		int j = 1;
		for (List<Integer> combination : getAllCombinations(3,1,3)) {
			if (DISPLAY_TEST_PROGRESS) System.out.println("### COMPREHENSIVE SAVE LOAD TEST NO " 
					+i+ "-" +j+ ": " +recordingStats+ " " +combination+ " ###\n");
			int retVal = testSaveLoadForGame(recordingStats, 
					new ShotValues(ShotValues.getVluesMap(combination.toArray(new Integer[0]))));
			if (retVal != 0) {
				return retVal;
			}
			j++;
		}
		return 0;
	}

	private static Set<List<Integer>> getAllCombinations(int noOfElements, int minValue, int maxValue) {
		return getCobination(new ArrayList<Integer>(), minValue, maxValue, noOfElements);
	}
	
	private static Set<List<Integer>> getCobination(List<Integer> unfinishedCombination, int min, int max, int length) {
		Set<List<Integer>> resultSet = new HashSet<List<Integer>>();
		for (int i = min; i <=max ; i++) {
			if (length == unfinishedCombination.size()+1) {
				List<Integer> finishedCombination = new ArrayList<Integer>(unfinishedCombination);
				finishedCombination.add(i);
				resultSet.add(finishedCombination);
			} else {
				List<Integer> unfinishedCombinationCopy = new ArrayList<Integer>(unfinishedCombination);
				unfinishedCombinationCopy.add(i);
				resultSet.addAll(getCobination(unfinishedCombinationCopy, min, max, length));
			}
		}
		return resultSet;
	}

	//////////////////////
	
	/*
	 * SIMPLE SAVE/LOAD TEST
	 */
	public static int simpleSaveLoadTest() {
		// For some combinations of recording stats
		int i = 1;
		for (StatComb statsComb : Conf.StatComb.values()) {
			if (DISPLAY_TEST_PROGRESS) System.out.println("### TEST NO " +i+ ": " +Arrays.toString(statsComb.stats)+ " ###\n");
			int retVal = testSaveLoadForGame(new RecordingStats(statsComb), ShotValues.DEFAULT);
			if (retVal != 0) {
				return retVal;
			}
			i++;
		}
		return 0; // Test passed.
	}
	
	//////////////////////
	
	/*
	 * SINGLE GAME SAVE/LOAD TEST
	 */
	public static int testSaveLoadForGame(RecordingStats recordingStats, ShotValues shotValues) {
		// create new game,
		Game game = Conf.getDefaultGame(recordingStats, shotValues);
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
	
	//////////////////////
	
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
