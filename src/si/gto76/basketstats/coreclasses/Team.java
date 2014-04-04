package si.gto76.basketstats.coreclasses;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import si.gto76.basketstats.Conf;

public class Team implements HasName {
	private static final DecimalFormat oneDigit = new DecimalFormat("#,##0.0");
	////////////////////////////////////////
	private String name;
	private Map<Player, PlayerStats> allPlayersStats = new LinkedHashMap<Player, PlayerStats>();
	Set<Player> playersOnTheFloor = new HashSet<Player>();
	////////////////////////////////////////
	
	public Team(String name, List<Player> players) {
		setName(name);
		for (Player player : players) {
			addPlayer(player);
		}
	}	
	
	public Team(String name, Map<Player, PlayerStats> allPlayersStats) {
		this.name = name;
		this.allPlayersStats = allPlayersStats;
	}
	
	////////////////////////////////////////
	@Override
	public void setName(String name) {
		name = name.trim();
		this.name = name.toUpperCase();
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void addPlayer(Player player) {
		PlayerStats ps = new PlayerStats(this);
		allPlayersStats.put(player, ps);
		playersOnTheFloor.add(player);
	}


	public void addAllPlayersOnTheFloor() {
		playersOnTheFloor.addAll(allPlayersStats.keySet());
	}

	public PlayerStats getPlayersStats(Player player) {
		return allPlayersStats.get(player);
	}

	public Map<Player, PlayerStats> getAllPlayersStats() {
		return Collections.unmodifiableMap(allPlayersStats);
	}

	public Player getPlayer(PlayerStats ps) {
		for (Map.Entry<Player, PlayerStats> pair : allPlayersStats.entrySet()) {
			if (pair.getValue().equals(ps)) {
				return pair.getKey();
			}
		}
		throw new IllegalArgumentException();
	}

	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		for (Player player : allPlayersStats.keySet()) {
			players.add(player);
		}
		return players;
	}

	public int getNumberOfPlayers() {
		return allPlayersStats.size();
	}

	public void putPlayerOnTheFloor(Player player) {
		// if there's no passed player among teams players
		if (!allPlayersStats.keySet().contains(player)) {
			throw new IllegalArgumentException();
		}
		playersOnTheFloor.add(player);
	}

	public void putPlayerOffTheFloor(Player player) {
		playersOnTheFloor.remove(player);
	}
	
	public Set<Player> getPlayersOnTheFloor() {
		return Collections.unmodifiableSet(playersOnTheFloor);
	}
	
	public void setPlayersOnTheFloor(Set<Player> players) {
		playersOnTheFloor = new HashSet<Player>(players);
	}

	public void changePlusMinus(int points) {
		for (Player player : playersOnTheFloor) {
			PlayerStats playersStats = allPlayersStats.get(player);
			playersStats.changePlusMinus(points);
		}
	}

	public Integer get(Stat statCat) {
		// plusMinus of team doesn't make sense;
		if (statCat == Stat.PM) {
			return null;
		}
		int sum = 0;
		for (PlayerStats ps : allPlayersStats.values()) {
			sum += ps.get(statCat);
		}
		return sum;
	}

	public double getFgPercent() {
		int fgm = get(Stat.FGM);
		int fga = get(Stat.FGA);
		return zeroIfDevideByZero(fgm, fga);
	}

	public double getTpPercent() {
		int tpm = get(Stat.TPM);
		int tpa = get(Stat.TPA);
		return zeroIfDevideByZero(tpm, tpa);
	}

	public boolean hasPlayer(Player player) {
		return allPlayersStats.keySet().contains(player);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		// HEADER
		//FGM-A	3PM-A +/-	OFF	DEF	TOT	AST	PF	ST	TO	BS	PTS
		sb.append(name).append("\n").
		append(emptyPlayersName()).
		append(padTab("FGM-A")).append(padTab("3PM-A"));
		for (Stat sc : Stat.nonScoringValuesAndPoints()) {
			sb.append(padTab(sc.getName().toUpperCase()));
		}
		sb.append("\n");

		// PLAYER STATS
		for (Player player : allPlayersStats.keySet()) {
			String playersName = player.getName();
			sb.append(padEnd(playersName, Conf.PLAYER_NAME_WIDTH, ' ')).
			append(allPlayersStats.get(player)).append("\n");
		}
		
		// TOTALS
		sb.append(padEnd("Totals", Conf.PLAYER_NAME_WIDTH, ' ')).
		append(padTab(get(Stat.FGM)+"-"+get(Stat.FGA))).
		append(padTab(get(Stat.TPM)+"-"+get(Stat.TPA)));
		for (Stat sc : Stat.nonScoringValuesAndPoints()) {
			if (sc == Stat.PM) {
				sb.append(padTab(""));
				continue;
			}
			sb.append( padTab(get(sc).toString()) );
		}
		sb.append("\n");

		// PERCENTS
		sb.append(emptyPlayersName()).
		append( padTab(oneDigit.format(getFgPercent())+"%") ).
		append(oneDigit.format(getTpPercent())+"%").
		append("\n");

		return sb.toString();
	}
	
	/*
	 * ##### ##### ##### ##### ##### ##### #####
	 * UTILS UTILS UTILS UTILS UTILS UTILS UTILS 
	 * ##### ##### ##### ##### ##### ##### #####
	 */
	
	public static  double zeroIfDevideByZero(int devidee, int devider) {
		if (devider == 0) {
			return 0;
		}
		return ((double) devidee / devider) * 100.0;
	}
	
	public static String emptyPlayersName() {
		return padEnd("", Conf.PLAYER_NAME_WIDTH, ' ');
	}
	
	public static String padTab(String string) {
		return padEnd(string, Conf.TAB_WIDTH, ' ');
	}

	public static String padEnd(String string, int minLength, char padChar) {
	    checkNotNull(string);  // eager for GWT.
	    if (string.length() >= minLength) {
	      return string;
	    }
	    StringBuilder sb = new StringBuilder(minLength);
	    sb.append(string);
	    for (int i = string.length(); i < minLength; i++) {
	      sb.append(padChar);
	    }
	    return sb.toString();
	}
	
	public static <T> T checkNotNull(T reference) {
	    if (reference == null) {
	      throw new NullPointerException();
	    }
	    return reference;
	}
	
}
