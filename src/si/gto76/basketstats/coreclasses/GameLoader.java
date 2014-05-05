package si.gto76.basketstats.coreclasses;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.swingui.Tuple;

public class GameLoader {
	private final static String SPLITTER_STR = " +|\t";
	// Date is in form: Tue Apr 17 21:30:00 CEST 2012
	public final static DateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
	public static final String TOTALS_KEYWORD = "Totals";

	public static Game createGameFromString(String gameString) throws ParseException {
	    try {
	    	return create(gameString);
	    } catch (ParseException parseException) {
	        throw parseException;
	    } catch (Exception e) {
	    	throw new ParseException("Unexpected error occured while loadin game." +
	    			"Maybe this message will help explain it: "+ e.getMessage(), -1);
	    }
	}
		
	private static Game create(String gameString) throws ParseException {
		String[] line = gameString.split("\n");
		
		// DATE, VENUE, TEAM NAMES:
		Date date;
		try {
			date = DATE_FORMAT.parse(line[2]);
		} catch (ParseException e) {
			throw new ParseException(e.getMessage(), 2);
		}
		String venueName = line[3];
		if (venueName.trim().length() == 0) {
			throw new ParseException("Venue name is empty.", 3);
		}
		String team1Name = line[6].split(":")[0];
		if (team1Name.trim().length() == 0) {
			throw new ParseException("Name of first team is empty.", 7);
		}
		String team2Name = line[8].split(":")[0];
		if (team2Name.trim().length() == 0) {
			throw new ParseException("Name of second team is empty.", 8);
		}
		
		// STATS SET:
		String[] statsStrings = line[14].split(SPLITTER_STR);
		if (statsStrings.length < 2) {
			throw new ParseException("Stats row does not have enough elements.", 14);
		}
		statsStrings = Util.removeFirstElement(statsStrings);
		Set<Stat> displayableStats = getDisplayableStats(statsStrings);
		if (displayableStats.size() < 1) {
			throw new ParseException("Stats row does not have enough elements.", 14);
		}
		Set<Stat> recordableStats = Stat.getRecordableStatsFromDisplayables(displayableStats);
		if (!RecordingStats.isValidSet(recordableStats)) {
			throw new ParseException("There is a problem with stats row.", 14);
		}
		RecordingStats recordingStats = new RecordingStats(recordableStats);
		if (Conf.DEBUG) System.out.println("output Stats: " + Arrays.toString(displayableStats.toArray()));
		if (Conf.DEBUG) System.out.println("input Stats: " + recordingStats);

		// NEW GAME:
		// Parse empty lines around scoreboard: number of spaces signify how many points a shot is worth.
		Integer[] shotPoints = getShotPoints(line);
		Map<Stat, Integer> valuesMap = ShotValues.getValuesMap(shotPoints);
		ShotValues shotValues = new ShotValues(valuesMap);
		
		Map<Player, PlayerStatRecorder> team1Stats = new LinkedHashMap<Player, PlayerStatRecorder>();
		Map<Player, PlayerStatRecorder> team2Stats = new LinkedHashMap<Player, PlayerStatRecorder>();
		Game game = new Game(team1Name, team1Stats, team2Name, team2Stats, date, new Venue(venueName), 
				recordingStats, shotValues);
		Team team1 = game.getTeam1();
		Team team2 = game.getTeam2();

		// FILL TEAM 1:
		int i = 15;
		for (; !line[i].split(SPLITTER_STR)[0].equals(TOTALS_KEYWORD); i++) {
			parseOnePlayer(line[i], team1, team1Stats, displayableStats, i);
		}
		// FILLTEAM 2:
		for (i += 5; !line[i].split(SPLITTER_STR)[0].equals(TOTALS_KEYWORD); i++) {
			parseOnePlayer(line[i], team2, team2Stats, displayableStats, i);
		}
		
		game.addAllPlayersOnTheFloor();
		return game;
	}
	
	/*
	 * PER GAME:
	 */

	private static Integer[] getShotPoints(String[] line) throws ParseException {
		int freeThrowWorth = getShotWorth(line, 5, "free throw");
		int twoPointerWorth = getShotWorth(line, 7, "two point");
		int threePointerWorth = getShotWorth(line, 9, "three point");
		Integer[] shotPoints = {freeThrowWorth, twoPointerWorth, threePointerWorth};
		return shotPoints;
	}

	private static int getShotWorth(String[] line, int lineNum, String shotName) throws ParseException {
		isShotPointLineValid(line[lineNum], lineNum, shotName);
		return line[lineNum].length();
	}

	private static void isShotPointLineValid(String line, int lineNum, String shotName) throws ParseException {
		String onlySpaces = Util.padEnd("", line.length(), ' ');
		boolean lineDoesNotContainOnlySpaces = !line.equals(onlySpaces);
		if (lineDoesNotContainOnlySpaces) {
			throw new ParseException("Number of spaces on this line should denote the number of points a "+shotName+
					" shot is worth, but other characters are also present", lineNum);
		}
		if (line.length() < ShotValues.MIN_VALUE) {
			throw new ParseException("Number of spaces on this line that denote the number of points a "+shotName+
					" shot is worth is zero, and thus not valid", lineNum);
		}
		if (line.length() > ShotValues.MAX_VALUE) {
			throw new ParseException("Number of spaces on this line that denote the number of points a "+shotName+
					" shot is worth is "+line.length()+", and thus exceeding maximum permited value of "+ShotValues.MAX_VALUE+".", lineNum);
		}
	}

	private static Set<Stat> getDisplayableStats(String[] statsStrings) {
		Set<Stat> stats = new LinkedHashSet<Stat>();
		if (Conf.DEBUG) System.out.println("statsStrings: " + Arrays.toString(statsStrings));
		checkForSpecialCombinations(statsStrings, stats);
		for (int i = 0; i < statsStrings.length; i++) {
			String statName = statsStrings[i];
			Stat statOrNull = Stat.getByNameOrNull(statName);
			if (statOrNull != null) {
				stats.add(statOrNull);
			}
		}
		return Util.getOrderedSet(stats);
	}

	private static void checkForSpecialCombinations(String[] statsStrings,
			Set<Stat> stats) {
		for (String statLabel : statsStrings) {
			if (statLabel.equals("FGM-A")) {
				stats.add(Stat.FGM);
				stats.add(Stat.FGA);
			}
			if (statLabel.equals("3PM-A")) {
				stats.add(Stat.TPM); 
				stats.add(Stat.TPA);
			}
			if (statLabel.equals("FTM-A")) {
				stats.add(Stat.FTM); 
				stats.add(Stat.FTA);
			}
		}
	}
	
	/*
	 * PER PLAYER:
	 */

	private static void parseOnePlayer(String playersRow, Team team,
			Map<Player, PlayerStatRecorder> teamStats, Set<Stat> displayableStats, 
			int rowNum) throws ParseException {
		try {
			Tuple<Player,PlayerStatRecorder> playerWithStats =
					getPlayerAndPlayersStatRecorder(playersRow, team, displayableStats);
			teamStats.put(playerWithStats.x, playerWithStats.y);
		} catch (Exception e) {
			throw new ParseException("There is a problem with players row.", rowNum);
		}
	}

	/**
	 * playerRow should be in form of: Players Name	0-0 0-0 0 0 0 0 2 2 0 0 0 0
	 */
	private static Tuple<Player,PlayerStatRecorder> getPlayerAndPlayersStatRecorder
			(String playerRow, Team team, Set<Stat> displayableStats) {
		// TODO Throw exceptions with different explanations for different parse problems.
		String playersName = playerRow.substring(0, Conf.PLAYER_NAME_WIDTH-1).trim();
		Player player = new Player(playersName);
		String playersRowWithouthName = playerRow.substring(Conf.PLAYER_NAME_WIDTH);
		PlayerStatRecorder playerStats = getPlayersStatRecorder(playersRowWithouthName, team, displayableStats);
		return new Tuple<Player,PlayerStatRecorder>(player, playerStats);
	}

	private static PlayerStatRecorder getPlayersStatRecorder(String playersStats, Team team, 
			Set<Stat> displayableStats) {
		List<String> tokens = tokenizePlayersRow(playersStats);
		if (Conf.DEBUG) System.out.println("loadPlayersStats read values: " + Arrays.toString(tokens.toArray()));
		Map<Stat,Integer> stats = getStatsWithValues(tokens, displayableStats);
		return new PlayerStatRecorder(team, stats);
	}
	
	private static List<String> tokenizePlayersRow(String playersStats) {
		List<String> tokens = new ArrayList<String>();
		// Need to aditionaly split ones with "-". (example: 3-4)
		for (String token : playersStats.split(SPLITTER_STR)) {
			if (token.matches("[0-9]+-[0-9]+")) {
				for (String subToken : token.split("-")) {
					tokens.add(subToken);
				}
			} else {
				tokens.add(token);
			}
		}
		return tokens;
	}
	
	private static Map<Stat, Integer> getStatsWithValues(List<String> tokens, Set<Stat> displayableStats) {
		Map<Stat,Integer> stats = new HashMap<Stat,Integer>();
		Iterator<Stat> ite =  displayableStats.iterator();
		for (String token : tokens) {
			Stat next = ite.next();
			if (Conf.DEBUG) System.out.println(next + " : " + token);
			stats.put(next, Integer.valueOf(token));
		}
		return stats;
	}

}
