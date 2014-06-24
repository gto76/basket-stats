package si.gto76.basketstats.coreclasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import si.gto76.basketstats.Conf;

/**
 * All stats are kept per player, in players stat recorders. Every time a teams stat is needed,
 * it gets agregated anew.
 */
public class Team implements HasName, HasStats {
	////////////////////////////////////////
	private String name;
	private Map<Player, PlayerStatRecorder> playersWithStatRecorders = new LinkedHashMap<Player, PlayerStatRecorder>();
	Set<Player> playersOnTheFloor = new HashSet<Player>();
	public final Game game;
	////////////////////////////////////////
	
	public Team(String name, List<Player> players, Game game) {
		this.game = game;
		setName(name);
		for (Player player : players) {
			addPlayer(player);
		}
	}	
	
	public Team(String name, Map<Player, PlayerStatRecorder> allPlayersStats, Game game) {
		this.game = game;
		setName(name);
		this.playersWithStatRecorders = allPlayersStats;
	}

	////////////////////////////////////////

	/*
	 * GENERAL:
	 */
	
	/**
	 * Throws illegal argument exception if name is null or empty.
	 */
	@Override
	public void setName(String name) {
		name = Util.checkNameForNullAndTrimIt(name, Conf.MAX_TEAM_NAME_LENGTH);
		this.name = name.toUpperCase();
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public RecordingStats getRecordingStats() {
		return game.recordingStats;
	}
	
	protected void changePlusMinus(int points) {
		for (Player player : playersOnTheFloor) {
			PlayerStatRecorder playersStats = playersWithStatRecorders.get(player);
			playersStats.changePlusMinus(points);
		}
	}

	public int get(Stat stat) {
		// plusMinus of team doesn't make sense
		if (stat == Stat.PM) {
			throw new IllegalArgumentException("Can not return plus minus of a team");
		}
		int sum = 0;
		for (PlayerStatRecorder ps : playersWithStatRecorders.values()) {
			sum += ps.get(stat);
		}
		return sum;
	}
	
	public boolean hasUsedRebounds() {
		for (PlayerStatRecorder ps : playersWithStatRecorders.values()) {
			if (ps.hasUsedRebounds()) {
				return true;
			}
		}
		return false; 
	}

	////////////////////////////////////////
	
	/*
	 * PLAYER:
	 */
	
	public void addPlayer(Player player) {
		PlayerStatRecorder ps = new PlayerStatRecorder(this);
		playersWithStatRecorders.put(player, ps);
		playersOnTheFloor.add(player);
	}

	public Player getPlayer(PlayerStatRecorder ps) {
		for (Map.Entry<Player, PlayerStatRecorder> pair : playersWithStatRecorders.entrySet()) {
			if (pair.getValue().equals(ps)) {
				return pair.getKey();
			}
		}
		throw new IllegalArgumentException("Passed players stat recorder does not belong to any player on this team.");
	}

	public boolean hasPlayer(Player player) {
		return playersWithStatRecorders.keySet().contains(player);
	}
	
	/**
	 * Returns whether player was removed.
	 */
	public boolean removePlayer(Player player) {
		boolean some_players_values_are_not_zero = !getPlayersStatRecorder(player).areAllValuesZero();
		if (some_players_values_are_not_zero) {
			return false;
		}
		if (playersWithStatRecorders.size() <= 1) {
			return false;
		}
		playersWithStatRecorders.remove(player);
		playersOnTheFloor.remove(player);
		return true;
	}

	public PlayerStatRecorder getPlayersStatRecorder(Player player) {
		return playersWithStatRecorders.get(player);
	}
	
	public void putPlayerOnTheFloor(Player player) {
		boolean player_is_not_on_this_team = !hasPlayer(player);
		if (player_is_not_on_this_team) {
			return;
			// Is not throwing exception because a player may be deleted in meantime, 
			// and then undo calls this function.
		}
		playersOnTheFloor.add(player);
	}

	public void putPlayerOffTheFloor(Player player) {
		playersOnTheFloor.remove(player);
	}
	
	///////////////////////////////////////
	
	/*
	 * PLAYERS:
	 */
	
	/**
	 * Rerurns unmodifiable map.
	 */
	public Map<Player, PlayerStatRecorder> getAllPlayersStatRecorders() {
		return Collections.unmodifiableMap(playersWithStatRecorders);
	}


	public int getNumberOfPlayers() {
		return playersWithStatRecorders.size();
	}
	
	public void putAllPlayersOnTheFloor() {
		playersOnTheFloor.addAll(playersWithStatRecorders.keySet());
	}

	public Set<Player> getPlayersThatAreOnTheFloor() {
		return Collections.unmodifiableSet(playersOnTheFloor);
	}
	
	public void setPlayersOnTheFloor(Set<Player> players) {
		playersOnTheFloor = new HashSet<Player>(players);
	}

	////////////////////////////////////////

	/*
	 * SHOTING PERCENTAGES:
	 */
	
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
	
	//////////////////////////////////////////
	
	/*
	 * REPOSITION PLAYER:
	 */
	
	public boolean isPlayerFirst(Player player) {
		if (getPlayersIndex(player) == 0) {
			return true;
		}
		return false;
	}
	
	public boolean isPlayerLast(Player player) {
		if (getPlayersIndex(player) == getNumberOfPlayers()-1) {
			return true;
		}
		return false;
	}
	
	private int getPlayersIndex(Player player) {
		List<Player> playerList = new ArrayList<Player>(playersWithStatRecorders.keySet());
		return playerList.indexOf(player);
	}
	
	public void moveUpOneRow(Player player) {
		move(player, true);
	}

	public void moveDownOneRow(Player player) {
		move(player, false);
	}
	
	/**
	 * Moves player one row up or down.
	 */
	private void move(Player player, boolean up) {
		List<Player> playerList = new ArrayList<Player>(playersWithStatRecorders.keySet());
		List<Entry<Player, PlayerStatRecorder>> entryList = 
				new ArrayList<Entry<Player, PlayerStatRecorder>>(playersWithStatRecorders.entrySet());
		int index = playerList.indexOf(player);
		if (index == -1	|| (up && index == 0)
			|| (!up && index == playerList.size()-1) ) {
			return;
		}
		int delta = up ? -1 : 1;
		Collections.swap(entryList, index, index + delta);
		Map<Player, PlayerStatRecorder> tempMap = new LinkedHashMap<Player, PlayerStatRecorder>();
		for (Entry<Player, PlayerStatRecorder> entry : entryList) {
			tempMap.put(entry.getKey(), entry.getValue());
		}
		playersWithStatRecorders = tempMap;
	}

	/////////////////////
	
	@Override
	public String toString() {
		return PrinterTeam.toString(this);
	}
	
}
