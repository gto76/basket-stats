package si.gto76.basketstats.coreclasses;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.Util;

public class Team implements HasName, HasStats {
	private static final DecimalFormat oneDigit = new DecimalFormat("#,##0.0");
	////////////////////////////////////////
	private String name;
	private Map<Player, PlayerStats> allPlayersStats = new LinkedHashMap<Player, PlayerStats>();
	Set<Player> playersOnTheFloor = new HashSet<Player>();
	protected final Game game;
	////////////////////////////////////////
	
	public Team(String name, List<Player> players, Game game) {
		this.game = game;
		setName(name);
		for (Player player : players) {
			addPlayer(player);
		}
	}	
	
	public Team(String name, Map<Player, PlayerStats> allPlayersStats, Game game) {
		this.game = game;
		setName(name);
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
	
	public RecordingStats getRecordingStats() {
		return game.recordingStats;
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
			return;
			// throw new IllegalArgumentException(); // Not throwing exception,
			// because a player may be deleted in meantime, and then
			// undo calls this function.
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

	protected void changePlusMinus(int points) {
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
		return getPercent(Stat.FGM, Stat.FGA);
	}

	public double getTpPercent() {
		return getPercent(Stat.TPM, Stat.TPA);
	}
	
	public double getFtPercent() {
		return getPercent(Stat.FTM, Stat.FTA);
	}
	
	private double getPercent(Stat madeStat, Stat attemptsStat) {
		int made = get(madeStat);
		int attempts = get(attemptsStat);
		return Util.zeroIfDevideByZero(made, attempts);
	}

	public boolean hasPlayer(Player player) {
		return allPlayersStats.keySet().contains(player);
	}
	
	public boolean hasOnlyReb() {
		if (game.recordingStats.values.contains(Stat.REB) &&
				!game.recordingStats.values.contains(Stat.OFF) &&
				!game.recordingStats.values.contains(Stat.DEF) ) {
			return true;
		}
		return false;
	}
	
	public boolean isPlayerFirst(Player player) {
		if (getPlayersIndex(player) == 0) {
			return true;
		}
		return false;
	}
	
	public boolean isPlayerLast(Player player) {
		if (getPlayersIndex(player) == allPlayersStats.size()-1) {
			return true;
		}
		return false;
	}
	
	private int getPlayersIndex(Player player) {
		List<Player> playerList = new ArrayList<Player>(allPlayersStats.keySet());
		return playerList.indexOf(player);
	}
	
	public void moveUpOneRow(Player player) {
		move(player, true);
	}

	public void moveDownOneRow(Player player) {
		move(player, false);
	}
	
	/**
	 * Moves player one row up or down
	 */
	private void move(Player player, boolean up) {
		List<Player> playerList = new ArrayList<Player>(allPlayersStats.keySet());
		List<Entry<Player, PlayerStats>> entryList = 
				new ArrayList<Entry<Player, PlayerStats>>(allPlayersStats.entrySet());
		int index = playerList.indexOf(player);
		if (index == -1	|| (up && index == 0)
			|| (!up && index == playerList.size()-1) ) {
			return;
		}
		int delta = up ? -1 : 1;
		Collections.swap(entryList, index, index + delta);
		Map<Player, PlayerStats> tempMap = new LinkedHashMap<Player, PlayerStats>();
		for (Entry<Player, PlayerStats> entry : entryList) {
			tempMap.put(entry.getKey(), entry.getValue());
		}
		allPlayersStats = tempMap;
	}
	
	/*
	 * Returns whether player was removed.
	 */
	public boolean removePlayer(Player player) {
		if (!getPlayersStats(player).isEmpty()) {
			return false;
		}
		if (allPlayersStats.size() <= 1) {
			return false;
		}
		allPlayersStats.remove(player);
		playersOnTheFloor.remove(player);
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
		//FGM-A 3PM-A FTM-A +/- OFF DEF TOT AST PF ST TO BS BA PTS
		sb.append(name).append("\n").
		append(emptyPlayersName());
		appendScoringHeader(sb);
		appendNonScoringHeader(sb);
	}
	
	private void appendScoringHeader(StringBuilder sb) {
		if (!game.recordingStats.values.contains(Stat.IIPM)) {
			return;
		}
		////
		if (game.recordingStats.values.contains(Stat.IIPF) || 
				game.recordingStats.values.contains(Stat.TPF)) {
			sb.append(padTab("FGM-A"));
		} else {
			sb.append(padTab("FGM"));
		}
		if (game.recordingStats.values.contains(Stat.TPM)) {
			if (game.recordingStats.values.contains(Stat.TPF)) {
				sb.append(padTab("3PM-A"));
			} else {
				sb.append(padTab("3PM"));
			}
		}
		if (game.recordingStats.values.contains(Stat.FTM)) {
			if (game.recordingStats.values.contains(Stat.FTF)) {
				sb.append(padTab("FTM-A"));
			} else {
				sb.append(padTab("FTM"));
			}
		}
	}
	
	private void appendNonScoringHeader(StringBuilder sb) {
		for (Stat sc : Stat.getNonScoringOutputStatsFromInput(game.recordingStats.values)) {
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
		if (game.recordingStats.values.contains(Stat.IIPF)) {				
			sb.append( padTab(oneDigit.format(getFgPercent())+"%") );
		}
		if (game.recordingStats.values.contains(Stat.TPF)) {				
			sb.append( padTab(oneDigit.format(getTpPercent())+"%") );
		}
		if (game.recordingStats.values.contains(Stat.FTF)) {				
			sb.append( padTab(oneDigit.format(getFtPercent())+"%") );
		}
		sb.append("\n");
	}

	/*
	 * Also used by PlayerStat.toString()
	 */
	protected void appendStatsRow(StringBuilder sb, HasStats hs) {
		// FGM-A 3PM-A FTM-A +/- OFF DEF TOT AST PF ST TO BS BA PTS
		// Scoring
		if (game.recordingStats.values.contains(Stat.IIPM)) {
			appendScoringValues(sb, hs);
		}
		// Non-scoring
		for (Stat sc : Stat.getNonScoringOutputStatsFromInput(game.recordingStats.values)) {
			// For team totals we don't need plus minus
			if (hs instanceof Team && sc == Stat.PM) {
				sb.append(padTab(""));
				continue;
			}
			sb.append(padTab(hs.get(sc) + ""));
		}
	}
	
	private void appendScoringValues(StringBuilder sb, HasStats hs) {
		// FGM-A 3PM-A FTM-A +/-
		if (game.recordingStats.values.contains(Stat.IIPF) || game.recordingStats.values.contains(Stat.TPF)) {
			sb.append(padTab(hs.get(Stat.FGM)+"-"+hs.get(Stat.FGA)));
		} else {
			sb.append(padTab(hs.get(Stat.FGM) + ""));
		}
		if (game.recordingStats.values.contains(Stat.TPM)) {
			if (game.recordingStats.values.contains(Stat.TPF)) {
				sb.append(padTab(hs.get(Stat.TPM)+"-"+hs.get(Stat.TPA)));
			} else {
				sb.append(padTab(hs.get(Stat.TPM) + ""));
			}
		}
		if (game.recordingStats.values.contains(Stat.FTM)) {
			if (game.recordingStats.values.contains(Stat.FTF)) {
				sb.append(padTab(hs.get(Stat.FTM)+"-"+hs.get(Stat.FTA)));
			} else {
				sb.append(padTab(hs.get(Stat.FTM) + ""));
			}
		}
	}

	/*
	 * ##### ##### ##### ##### ##### ##### #####
	 * UTILS UTILS UTILS UTILS UTILS UTILS UTILS 
	 * ##### ##### ##### ##### ##### ##### #####
	 */
	
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
