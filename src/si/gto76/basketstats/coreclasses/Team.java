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
import si.gto76.basketstats.Util;

public class Team implements HasName, HasStats {
	private static final DecimalFormat oneDigit = new DecimalFormat("#,##0.0");
	////////////////////////////////////////
	private String name;
	private Map<Player, PlayerStats> allPlayersStats = new LinkedHashMap<Player, PlayerStats>();
	Set<Player> playersOnTheFloor = new HashSet<Player>();
	private final Set<Stat> recordingStats;
	////////////////////////////////////////
	
	public Team(String name, List<Player> players, Set<Stat> recordingStats) {
		setName(name);
		this.recordingStats = recordingStats;
		for (Player player : players) {
			addPlayer(player);
		}
	}	
	
	public Team(String name, Map<Player, PlayerStats> allPlayersStats, Set<Stat> recordingStats) {
		this.name = name;
		this.recordingStats = recordingStats;
		this.allPlayersStats = allPlayersStats;
	}

	////////////////////////////////////////
	
	/*
	 * ############### ############### ###############
	 * GETTERS/SETTERS GETTERS/SETTERS GETTERS/SETTERS
	 * ############### ############### ###############
	 */
	
	@Override
	public void setName(String name) {
		name = name.trim();
		this.name = name.toUpperCase();
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public Set<Stat> getRecordingStats() {
		return Collections.unmodifiableSet(recordingStats);
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

	public int get(Stat stat) {
		// plusMinus of team doesn't make sense
		if (stat == Stat.PM) {
			throw new IllegalArgumentException("Can not return plus minus of a team");
		}
		int sum = 0;
		for (PlayerStats ps : allPlayersStats.values()) {
			sum += ps.get(stat);
		}
		return sum;
	}

	public double getFgPercent() {
		int fgm = get(Stat.FGM);
		int fga = get(Stat.FGA);
		return Util.zeroIfDevideByZero(fgm, fga);
	}

	public double getTpPercent() {
		int tpm = get(Stat.TPM);
		int tpa = get(Stat.TPA);
		return Util.zeroIfDevideByZero(tpm, tpa);
	}

	public boolean hasPlayer(Player player) {
		return allPlayersStats.keySet().contains(player);
	}
	
	public boolean hasOnlyReb() {
		if (recordingStats.contains(Stat.REB) &&
				!recordingStats.contains(Stat.OFF) &&
				!recordingStats.contains(Stat.DEF) ) {
			return true;
		}
		return true;
	}

	/*
	 * ######### ######### ######### ######### #########
	 * TO STRING TO STRING TO STRING TO STRING TO STRING
	 * ######### ######### ######### ######### #########
	 */
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		appendHeader(sb);
		appendPlayerStats(sb);
		appendTotals(sb);
		appendPercents(sb);
		return sb.toString();
	}

	private void appendHeader(StringBuilder sb) {
		//FGM-A	3PM-A +/-	OFF	DEF	TOT	AST	PF	ST	TO	BS	PTS
		sb.append(name).append("\n").
		append(emptyPlayersName());
		appendScoringHeader(sb);
		appendNonScoringHeader(sb);
	}
	
	private void appendScoringHeader(StringBuilder sb) {
		if (recordingStats.contains(Stat.IIPF) || recordingStats.contains(Stat.TPF)) {
			sb.append(padTab("FGM-A"));
		} else {
			sb.append(padTab("FGM"));
		}
		if (recordingStats.contains(Stat.TPM)) {
			if (recordingStats.contains(Stat.TPF)) {
				sb.append(padTab("3PM-A"));
			} else {
				sb.append(padTab("3PM"));
			}
		}
	}
	
	private void appendNonScoringHeader(StringBuilder sb) {
		for (Stat sc : Stat.getNonScoringOutputStatsFromInput(recordingStats)) {
			sb.append(padTab(sc.getName().toUpperCase()));
		}
		sb.append("\n");
	}

	private void appendPlayerStats(StringBuilder sb) {
		for (Player player : allPlayersStats.keySet()) {
			String playersName = player.getName();
			sb.append(padEnd(playersName, Conf.PLAYER_NAME_WIDTH, ' ')).
			append(allPlayersStats.get(player)).append("\n");
		}
	}
	
	private void appendTotals(StringBuilder sb) {
		sb.append(padEnd("Totals", Conf.PLAYER_NAME_WIDTH, ' '));		
		appendStatsRow(sb, this);
		sb.append("\n");
	}
	
	private void appendPercents(StringBuilder sb) {
		sb.append(emptyPlayersName());
		if (recordingStats.contains(Stat.IIPF)) {				
			sb.append( padTab(oneDigit.format(getFgPercent())+"%") );
		}
		if (recordingStats.contains(Stat.TPF)) {				
			sb.append(oneDigit.format(getTpPercent())+"%");
		}
		sb.append("\n");
	}
	
	/*
	 * ##### ##### ##### ##### ##### ##### #####
	 * UTILS UTILS UTILS UTILS UTILS UTILS UTILS 
	 * ##### ##### ##### ##### ##### ##### #####
	 */
	
	protected StringBuilder appendStatsRow(StringBuilder sb, HasStats hs) {
		// Scoring
		if (recordingStats.contains(Stat.IIPF) || recordingStats.contains(Stat.TPF)) {
			sb.append(padTab(hs.get(Stat.FGM)+"-"+hs.get(Stat.FGA)));
		} else {
			sb.append(padTab(hs.get(Stat.FGM) + ""));
		}
		if (recordingStats.contains(Stat.TPM)) {
			if (recordingStats.contains(Stat.TPF)) {
				sb.append(padTab(hs.get(Stat.TPM)+"-"+hs.get(Stat.TPA)));
			} else {
				sb.append(padTab(hs.get(Stat.TPM) + ""));
			}
		}
		// Non-scoring
		for (Stat sc : Stat.getNonScoringOutputStatsFromInput(recordingStats)) {
			// For team totals we don't need plus minus
			if (hs instanceof Team && sc == Stat.PM) {
				sb.append(padTab(""));
				continue;
			}
			sb.append(padTab(hs.get(sc) + ""));
		}
		return sb;
	}
	
	public static String emptyPlayersName() {
		return padEnd("", Conf.PLAYER_NAME_WIDTH, ' ');
	}
	
	public static String padTab(String string) {
		return padEnd(string, Conf.TAB_WIDTH, ' ');
	}

	public static String padEnd(String string, int minLength, char padChar) {
	    Util.checkNotNull(string);  // eager for GWT.
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
	
}
