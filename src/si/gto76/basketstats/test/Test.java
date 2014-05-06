package si.gto76.basketstats.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.Conf.StatComb;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.GameLoader;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.PlayerStatRecorder;
import si.gto76.basketstats.coreclasses.RecordingStats;
import si.gto76.basketstats.coreclasses.ShotValues;
import si.gto76.basketstats.coreclasses.Stat;
import si.gto76.basketstats.coreclasses.Util;

public class Test {

	public static final boolean SAVE_LOAD_TEST_WITH_ALL_STAT_COMBINATIONS_AND_MOST_SHOT_VALUE_COMBINATIONS = true;
	public static final boolean SAVE_LOAD_TEST_WITH_ALL_STAT_COMBINATIONS = false;
	public static final boolean SAVE_LOAD_TEST_WITH_COMMON_STAT_COMBINATIONS = false;
	public static final boolean RECORDING_STATS_TEST = false;
	
	public static final boolean DISPLAY_TEST_PROGRESS = true;
	public static final boolean DISPLAY_ORIGINAL_GAMES = false;
	public static final boolean DISPLAY_LOADED_GAMES = false;
	
	//////////////////////////////////////////////
	
	public static void main(String[] args) {
		if (SAVE_LOAD_TEST_WITH_COMMON_STAT_COMBINATIONS) {
			printTestResult("Simple Save/Load test", simpleSaveLoadTest());
		}
		if (SAVE_LOAD_TEST_WITH_ALL_STAT_COMBINATIONS) {
			printTestResult("Intermediate Save/Load test", intermediateSaveLoadTest());
		}
		if (SAVE_LOAD_TEST_WITH_ALL_STAT_COMBINATIONS_AND_MOST_SHOT_VALUE_COMBINATIONS) {
			printTestResult("Full Save/Load test", fullSaveLoadTest());
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
	 * FULL SAVE/LOAD TEST
	 */
	private static int fullSaveLoadTest() {
		return saveLoadTest(true);
	}
	
	/*
	 * COMPREHENSIVE SAVE/LOAD TEST
	 */
	public static int intermediateSaveLoadTest() {
		return saveLoadTest(false);
	}
	
	public static int saveLoadTest(boolean full) {
		// for all combinations of input stats
		Set<Set<Stat>> allCombinations = powerSet(new HashSet<Stat>(Arrays.asList(Stat.recordableStats)));
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
			if (DISPLAY_TEST_PROGRESS) System.out.println("### COMPREHENSIVE SAVE LOAD TEST NO " +i+ ": " 
					+recordingStats+ " ###\n");
			int retVal; 
			if (full) {
				retVal = testWithAllShotValues(recordingStats, i);
			} else {
				retVal = testSaveLoadForGame(recordingStats, ShotValues.FULL_COURT);
			}
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
					new ShotValues(ShotValues.getValuesMap(combination.toArray(new Integer[0]))));
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
	
	private static Set<List<Integer>> getCobination(List<Integer> unfinishedCombination, 
			int min, int max, int length) {
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
			if (DISPLAY_TEST_PROGRESS) System.out.println("### TEST NO " +i+ ": " +statsComb.stats+ " ###\n");
			int retVal = testSaveLoadForGame(new RecordingStats(statsComb.stats), ShotValues.FULL_COURT);
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
		PlayerStatRecorder playerStats = getFirstPlayersStats(game);
		for (Stat stat : game.recordingStats.values) {
			if (stat == Stat.PM) {
				continue;
			}
			playerStats.made(stat);
		}
		// create new game by sending game string to game loader
		String gameString = game.toString();
		if (DISPLAY_ORIGINAL_GAMES) System.out.println("# ORIGINAL GAME: #\n"+game);
		Game loadedGame;
		try {
			loadedGame = GameLoader.createGameFromString(gameString);
		} catch (ParseException e) {
			e.printStackTrace();
			return 2; // ERROR: Parse Exception
		}
		if (DISPLAY_LOADED_GAMES) System.out.println("# LOADED GAME: #\n"+loadedGame);
		// and compare strings
		boolean original_and_loaded_game_differ = !loadedGame.toString().equals(gameString);
		if (original_and_loaded_game_differ) {
			System.out.println("## TEST FAILED AT GAME: ##" 
							+ "\n# ORIGINAL GAME: #" + gameString 
							+ "\n# LOADED GAME: #" + loadedGame);
			return 1; // ERROR: Game and loaded game are different
		}
		return 0;
	}

	private static PlayerStatRecorder getFirstPlayersStats(Game game) {
		Player firstPlayer = game.getTeam1().getAllPlayersStatRecorders().keySet().iterator().next();
		return game.getPlayersStatRecorder(firstPlayer);
	}
	
	//////////////////////
	
	/*
	 * RECORDING STATS TEST
	 */
	public static int recordingStatsTest() {
		// for all combinations of Input Stat run getValidSet
		Set<Set<Stat>> allCombinations = powerSet(new HashSet<Stat>(Arrays.asList(Stat.recordableStats)));
		for (Set<Stat> combination : allCombinations) {
			Set<Stat> orderedStats = Util.getOrderedSet(combination);
			Set<Stat> validSet = RecordingStats.getValidSet(combination);
			if (DISPLAY_LOADED_GAMES) System.out.println("# ORIGINAL SET: # " 
					+ Arrays.toString(orderedStats.toArray()));
			if (DISPLAY_LOADED_GAMES) System.out.println("# CORECTED SET: # " 
					+ Arrays.toString(validSet.toArray()) + "\n");
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
