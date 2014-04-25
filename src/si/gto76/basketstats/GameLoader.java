package si.gto76.basketstats;

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

import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Venue;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.PlayerStatRecorder;
import si.gto76.basketstats.coreclasses.RecordingStats;
import si.gto76.basketstats.coreclasses.ShotValues;
import si.gto76.basketstats.coreclasses.Stat;
import si.gto76.basketstats.coreclasses.Team;

public class GameLoader {
	final static String SPLITTER_STR = " +|\t";

	public static Game createGameFromString(String gameString) {
		String[] line = gameString.split("\n");
		// DATE, VENUE, TEAM NAMES:
		Date date = parseDate(line[2]);
		String venue = line[3];
		String team1Name = line[6].split(":")[0];
		String team2Name = line[8].split(":")[0];
		// STATS SET:
		String[] statsStrings = line[14].split(SPLITTER_STR);
		statsStrings = Util.removeFirstElement(statsStrings);
		Set<Stat> outputStats = getOutputStats(statsStrings); 
		RecordingStats inputStats = new RecordingStats(Stat.getRecordableStatsFromDisplayables(outputStats));
		if (Conf.DEBUG) System.out.println("output Stats: " + Arrays.toString(outputStats.toArray()));
		if (Conf.DEBUG) System.out.println("input Stats: " + inputStats);
		// NEW GAME:
		Integer[] shotPoints = {line[5].length(), line[7].length(), line[9].length()};
		Map<Stat, Integer> valuesMap = ShotValues.getValuesMap(shotPoints);
		Map<Player, PlayerStatRecorder> team1Stats = new LinkedHashMap<Player, PlayerStatRecorder>();
		Map<Player, PlayerStatRecorder> team2Stats = new LinkedHashMap<Player, PlayerStatRecorder>();
		Game game = new Game(team1Name, team1Stats, team2Name, team2Stats, date, new Venue(venue), 
				inputStats, new ShotValues(valuesMap));
		Team team1 = game.getTeam1();
		Team team2 = game.getTeam2();
		// TEAM 1:
		int i = 15;
		for (; !line[i].split(SPLITTER_STR)[0].equals("Totals"); i++) {
			loadPlayersStats(line[i], team1Stats, team1, outputStats); 
		}
		// TEAM 2:
		for (i += 5; !line[i].split(SPLITTER_STR)[0].equals("Totals"); i++) {
			loadPlayersStats(line[i], team2Stats, team2, outputStats);
		}
		game.addAllPlayersOnTheFloor();
		return game;
	}

	private static Set<Stat> getOutputStats(String[] statsStrings) {
		Set<Stat> stats = new LinkedHashSet<Stat>();
		if (Conf.DEBUG) System.out.println("statsStrings: " + Arrays.toString(statsStrings));
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
		for (int i = 0; i < statsStrings.length; i++) {
			String statName = statsStrings[i];
			Stat stat = Stat.getByNameOrNull(statName);
			if (stat != null) {
				stats.add(stat);
			}
		}
		return Util.getOrderedSet(stats);
	}

	private static void loadPlayersStats(String playerString,
			Map<Player, PlayerStatRecorder> playersWithStats, Team team, Set<Stat> outputStats) {
		// playerString: Name	0-0 0-0 0 0 0 0 2 2 0 0 0 0
		String playersName = playerString.substring(0, Conf.PLAYER_NAME_WIDTH-1).trim();
		// playersStats: 0-0 0-0 0 0 0 0 2 2 0 0 0 0
		String playersStats = playerString.substring(Conf.PLAYER_NAME_WIDTH);

		List<String> tokens = new ArrayList<String>();
		// Need to aditionaly split ones with - (3-4)
		for (String token : playersStats.split(SPLITTER_STR)) {
			if (token.matches("[0-9]+-[0-9]+")) {
				for (String subToken : token.split("-")) {
					tokens.add(subToken);
				}
			} else {
				tokens.add(token);
			}
		}
		if (Conf.DEBUG) System.out.println("loadPlayersStats read values: " + Arrays.toString(tokens.toArray()));
		Player pl = new Player(playersName);
		Map<Stat,Integer> stats = new HashMap<Stat,Integer>();
		
		Iterator<Stat> ite =  outputStats.iterator();
		//System.out.println("Tokens: " + Arrays.toString(tokens.toArray()));
		for (String token : tokens) {
			Stat next = ite.next();
			if (Conf.DEBUG) System.out.println(next + " : " + token);
			stats.put(next, Integer.valueOf(token));
		}
		
		PlayerStatRecorder plStats = new PlayerStatRecorder(team, stats);
		playersWithStats.put(pl, plStats);
	}

	public static Date parseDate(String dateString) {
		// Date is in form: Tue Apr 17 21:30:00 CEST 2012
		DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
		Date date = null;
		try {
			date = df.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
