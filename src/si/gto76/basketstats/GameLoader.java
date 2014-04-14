package si.gto76.basketstats;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Location;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.PlayerStats;
import si.gto76.basketstats.coreclasses.Stat;
import si.gto76.basketstats.coreclasses.Team;

public class GameLoader {
	private static final boolean DEBUG = true;
	final static String SPLITTER_STR = " +|\t";
	//final static String SPLITTER_STR_2 = " +|\t|-";

	public static Game createGameFromString(String gameString) {
		String[] line = gameString.split("\n");
		// line 2
		String dateString = line[2];
		Date date = null;
		try {
			date = parseDate(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// line 3
		String location = line[3];
		// line 6, until :, name of first team
		String team1Name = line[6].split(":")[0];
		// line 8, until :, name of second team
		String team2Name = line[8].split(":")[0];

		Map<Player, PlayerStats> team1Stats = new LinkedHashMap<Player, PlayerStats>();
		Map<Player, PlayerStats> team2Stats = new LinkedHashMap<Player, PlayerStats>();
		
		String[] statsStrings = line[14].split(SPLITTER_STR);
		statsStrings = Util.removeFirstElement(statsStrings);
		Set<Stat> outputStats = getOutputStats(statsStrings); 
		if (DEBUG)
			System.out.println("output Stats: " + Arrays.toString(outputStats.toArray()));
		Set<Stat> inputStats = Stat.getInputStatsFromOutput(outputStats);
			
		Team team1 = new Team(team1Name, team1Stats, inputStats);
		Team team2 = new Team(team2Name, team2Stats, inputStats);

		if (DEBUG)
			System.out.println("output Stats: " + Arrays.toString(outputStats.toArray()));
	
		// line 15 first player of first team ... until "Totals"
		int i = 15;
		for (; !line[i].split(SPLITTER_STR)[0].equals("Totals"); i++) {
			addPlayerToMap(line[i], team1Stats, team1, outputStats); 
		}

		// after ---- and /t first player of second team ... until "Totals"
		for (i += 5; !line[i].split(SPLITTER_STR)[0].equals("Totals"); i++) {
			addPlayerToMap(line[i], team2Stats, team2, outputStats);
		}

		team1.addAllPlayersOnTheFloor();
		team2.addAllPlayersOnTheFloor();
		return new Game(team1, team2, date, new Location(location), inputStats);
	}

	private static Set<Stat> getOutputStats(String[] statsStrings) {
		Set<Stat> stats = new LinkedHashSet<Stat>();
		if (DEBUG) System.out.println("statsStrings: " + Arrays.toString(statsStrings));
		if (statsStrings[0].equals("FGM-A")) {
			stats.add(Stat.FGM);
			stats.add(Stat.FGA);
		} else {
			stats.add(Stat.FGM);
		}
		if (statsStrings[1].equals("3PM-A")) {
			stats.add(Stat.TPM); 
			stats.add(Stat.TPA);
		} else {
			stats.add(Stat.TPM);
		}
		
		for (int i = 2; i < statsStrings.length; i++) {
			String statName = statsStrings[i];
			stats.add(Stat.getByName(statName));
		}
		
		return stats;
	}

	private static void addPlayerToMap(String playerString,
			Map<Player, PlayerStats> playersWithStats, Team team, Set<Stat> outputStats) {
		// FGM-A 3PM-A +/- OFF DEF TOT AST PF ST TO BS PTS
		// 0-0 0-0 0 0 0 0 2 2 0 0 0 0
		String playersName = playerString.substring(0, Conf.PLAYER_NAME_WIDTH-1).trim();
		String playersStats = playerString.substring(Conf.PLAYER_NAME_WIDTH);
		
		//old: String[] tokens = playersStats.split(SPLITTER_STR);
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

		if (DEBUG) {
			int i = 0;
			for (String s : tokens) {
				System.out.println(i + ": " + s);
				i++;
			}
		}

		Player pl = new Player(playersName);
		Map<Stat,Integer> stats = new HashMap<Stat,Integer>();
		
		Iterator<Stat> ite =  outputStats.iterator();
		for (String token : tokens) {
			Stat next = ite.next();
			if (DEBUG) System.out.println(next + " : " + token);
			stats.put(next, Integer.valueOf(token));
		}
		
		PlayerStats plStats = new PlayerStats(team, stats);
		if (DEBUG) {
			System.out.println("CHECKING PLAYERS SCORED 2P: " + plStats.get(Stat.IIPM));
			System.out.println("CHECKING PLAYERS SCORED AST: " + plStats.get(Stat.AST));
		}
		playersWithStats.put(pl, plStats);

//		OLD:
//		String[] fg = sss[0].split("-");
//		int fgm = Integer.parseInt(fg[0]);
//		int fga = Integer.parseInt(fg[1]);
//
//		String[] tp = sss[1].split("-");
//		int tpm = Integer.parseInt(tp[0]);
//		int tpa = Integer.parseInt(tp[1]);
//
//		int pm = Integer.parseInt(sss[2]);
//		int off = Integer.parseInt(sss[3]);
//		int def = Integer.parseInt(sss[4]);
//		// tot s[5] -> redundant information
//		int ast = Integer.parseInt(sss[6]);
//		int pf = Integer.parseInt(sss[7]);
//		int st = Integer.parseInt(sss[8]);
//		int to = Integer.parseInt(sss[9]);
//		int bs = Integer.parseInt(sss[10]);
//		// pts s[11] -> redundant information
//
//		Player pl = new Player(playersName);
//		PlayerStats plStats = new PlayerStats(team, fgm, fga, tpm, tpa, pm,
//				off, def, ast, pf, st, to, bs);

	}

	public static Date parseDate(String dateStr) throws ParseException {
		// Date is in form: Tue Apr 17 21:30:00 CEST 2012
		DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy",
				Locale.ENGLISH);
		return df.parse(dateStr);
	}

}
