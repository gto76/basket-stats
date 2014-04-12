package si.gto76.basketstats;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
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
	private static final boolean DEBUG = false;
	final static String SPLITTER_STR = " +|\t";
	final static String SPLITTER_STR_2 = " +|\t|-";

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
		Set<Stat> recordingStats = getRcordingStats(statsStrings); 
			
		Team team1 = new Team(team1Name, team1Stats, recordingStats);
		Team team2 = new Team(team2Name, team2Stats, recordingStats);

		// line 15 first player of first team ... until "Totals"
		int i = 15;
		for (; !line[i].split(SPLITTER_STR)[0].equals("Totals"); i++) {
			addPlayerToMap(line[i], team1Stats, team1, recordingStats); //TODO recording stats!!!
		}

		// after ---- and /t first player of second team ... until "Totals"
		for (i += 5; !line[i].split(SPLITTER_STR)[0].equals("Totals"); i++) {
			addPlayerToMap(line[i], team2Stats, team2, recordingStats);
		}

		team1.addAllPlayersOnTheFloor();
		team2.addAllPlayersOnTheFloor();
		return new Game(team1, team2, date, new Location(location), recordingStats);
	}

	private static Set<Stat> getRcordingStats(String[] statsStrings) {
		Set<Stat> stats = new LinkedHashSet<Stat>();
		//{IIPM, IIPF, TPM, TPF, PM, OFF, DEF, AST, PF, ST, TO, BS};
		if (statsStrings[0] == "FGM-A") {
			stats.add(Stat.IIPM);
			stats.add(Stat.IIPF);
		} else {
			stats.add(Stat.IIPM);
		}
		if (statsStrings[1] == "3PM-A") {
			stats.add(Stat.TPM); 
			stats.add(Stat.TPF);
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
			Map<Player, PlayerStats> playersWithStats, Team team, Set<Stat> recordingStats) {
		// FGM-A 3PM-A +/- OFF DEF TOT AST PF ST TO BS PTS
		// 0-0 0-0 0 0 0 0 2 2 0 0 0 0
		String playersName = playerString.substring(0, Conf.PLAYER_NAME_WIDTH-1).trim();
		String playersStats = playerString.substring(Conf.PLAYER_NAME_WIDTH);
		
		String[] sss = playersStats.split(SPLITTER_STR_2);

		if (DEBUG) {
			int i = 0;
			for (String s : sss) {
				System.out.println(i + ": " + s);
				i++;
			}
		}

		//TODO
		Player pl = new Player(playersName);
//		PlayerStats plStats = new PlayerStats(team, fgm, fga, tpm, tpa, pm,
//				off, def, ast, pf, st, to, bs);
		
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

		playersWithStats.put(pl, plStats);
	}

	public static Date parseDate(String dateStr) throws ParseException {
		// Date is in form: Tue Apr 17 21:30:00 CEST 2012
		DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy",
				Locale.ENGLISH);
		return df.parse(dateStr);
	}

}
