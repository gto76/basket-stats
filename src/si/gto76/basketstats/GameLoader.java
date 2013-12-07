package si.gto76.basketstats;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.PlayerStats;
import si.gto76.basketstats.coreclasses.Team;

public class GameLoader {
	final static String SPLITTER_STR = " +|\t";

	public static Game createGameFromString(String gameString) {
		String[] lll = gameString.split("\n");
		// line 2
		String dateString = lll[2];

		Date date = null;
		try {
			date = parseDate(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// line 3
		String location = lll[3];
		// line 6, do :, ime prve ekipe
		String team1Name = lll[6].split(":")[0];
		// line 8, do :, ime druge ekipe
		String team2Name = lll[8].split(":")[0];

		Map<Player, PlayerStats> team1Stats = new HashMap<Player, PlayerStats>();
		Map<Player, PlayerStats> team2Stats = new HashMap<Player, PlayerStats>();
		Team team1 = new Team(team1Name, team1Stats);
		Team team2 = new Team(team2Name, team2Stats);

		// line 15 prvi igralec prve ekipe ... Do Totals
		int i = 15;
		for (; !lll[i].split(SPLITTER_STR)[0].equals("Totals"); i++) {
			addPlayerToMap(lll[i], team1Stats, team1);
		}

		// po ---- in /t prvi igralec druge ekipe ... Do Totals
		for (i += 5; !lll[i].split(SPLITTER_STR)[0].equals("Totals"); i++) {
			addPlayerToMap(lll[i], team2Stats, team2);
		}

		team1.addAllPlayersOnTheFloor();
		team2.addAllPlayersOnTheFloor();
		return new Game(team1, team2, date, location);
	}

	private static void addPlayerToMap(String playerString,
			Map<Player, PlayerStats> playersWithStats, Team team) {
		// IGRALEC:
		// FGM-A 3PM-A +/- OFF DEF TOT AST PF ST TO BS PTS
		// 0-0 0-0 0 0 0 0 2 2 0 0 0 0
		String[] s = playerString.split(SPLITTER_STR);

		int i = 0;
		for (String a : s) {
			System.out.println(i + ": " + a);
			i++;
		}

		String firstName = s[0];
		String secondName = s[1];

		String[] fg = s[2].split("-");
		int fgm = Integer.parseInt(fg[0]);
		int fga = Integer.parseInt(fg[1]);

		String[] tp = s[3].split("-");
		int tpm = Integer.parseInt(tp[0]);
		int tpa = Integer.parseInt(tp[1]);

		int pm = Integer.parseInt(s[4]);
		int off = Integer.parseInt(s[5]);
		int def = Integer.parseInt(s[6]);
		// tot s[7] -> redundant information
		int ast = Integer.parseInt(s[8]);
		int pf = Integer.parseInt(s[9]);
		int st = Integer.parseInt(s[10]);
		int to = Integer.parseInt(s[11]);
		int bs = Integer.parseInt(s[12]);
		// pts s[13] -> redundant information

		Player pl = new Player(firstName, secondName);
		PlayerStats plStats = new PlayerStats(team, fgm, fga, tpm, tpa, pm,
				off, def, ast, pf, st, to, bs);

		playersWithStats.put(pl, plStats);
	}

	public static Date parseDate(String dateStr) throws ParseException {
		// Date is in form: Tue Apr 17 21:30:00 CEST 2012
		DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy",
				Locale.ENGLISH);
		return df.parse(dateStr);
	}

}
