package si.gto76.basketstats.coreclasses;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import si.gto76.basketstats.Conf;

public class Game {
	////////////////////////////////////////
	private Venue venue;
	private Date date;
	private Team team1, team2;
	public RecordingStats recordingStats;
	protected ShotValues shotValues;
	////////////////////////////////////////
	
	public Game(String team1Name, List<Player> team1Players, 
				String team2Name, List<Player> team2Players,
				Date date, Venue location, RecordingStats recordingStats, ShotValues shotValues) {
		this.team1 = new Team(team1Name, team1Players, this);
		this.team2 = new Team(team2Name, team2Players, this);
		init(date, location, recordingStats, shotValues);
	}
	
	public Game(String team1Name, Map<Player, PlayerStatRecorder> team1PlayersStats, 
				String team2Name, Map<Player, PlayerStatRecorder> team2PlayersStats,
				Date date, Venue location, RecordingStats recordingStats, ShotValues shotValues) {
		this.team1 = new Team(team1Name, team1PlayersStats, this);
		this.team2 = new Team(team2Name, team2PlayersStats, this);
		init(date, location, recordingStats, shotValues);
	}
	
	private void init(Date date, Venue location, RecordingStats recordingStats, ShotValues shotValues) {
		this.date = date;
		this.venue = location;
		this.recordingStats = recordingStats;
		this.shotValues = shotValues;
	}

	////////////////////////////////////////

	/*
	 * GETTERS:
	 */
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public Team getTeam1() {
		return team1;
	}

	public Team getTeam2() {
		return team2;
	}

	public Venue getLocation() {
		return venue;
	}

	public int getNumberOfPlayers() {
		return team1.getNumberOfPlayers() + team2.getNumberOfPlayers();
	}

	public PlayerStatRecorder getPlayersStats(Player player) {
		Team team = getPlayersTeam(player);
		return team.getPlayersStats(player);
	}
	
	public Team getPlayersTeam(Player player) {
		if (team1.hasPlayer(player)) {
			return team1;
		} else {
			return team2;
		}
	}

	public Team getOtherTeam(Team team) {
		if (team == team1) {
			return team2;
		} else {
			return team1;
		}
	}
	
	public Set<Player> getPlayersOnTheFloor() {
		Set<Player> playersOnFloor = new HashSet<Player>(team1.getPlayersOnTheFloor());
		playersOnFloor.addAll(team2.getPlayersOnTheFloor());
		return playersOnFloor;
	}

	private boolean wasAnyUsed(Stat... stats) {
		for (Stat stat : stats) {
			if (wasUsed(stat)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean wasUsed(Stat stat) {
		if (stat == Stat.REB) {
			return hasUsedRebounds();
		}
		if (get(stat) == 0) {
			return false;
		}
		return true;
	}
	
	private int get(Stat stat) {
		return team1.get(stat) + team2.get(stat);
	}
	
	private boolean hasUsedRebounds() {
		return team1.hasUsedRebounds() || team2.hasUsedRebounds();
	}
	
	/*
	 * SETERS:
	 */
	/**
	 * Called from PlayerStats after every score change.
	 * It sets plusMinus stat of all players that are on the floor.
	 */
	protected void setPlusMinus(Integer scoreDelta, Team team) {
		team.changePlusMinus(scoreDelta);
		Team otherTeam = getOtherTeam(team);
		otherTeam.changePlusMinus(scoreDelta * (-1));
	}
	
	public void addAllPlayersOnTheFloor() {
		team1.addAllPlayersOnTheFloor();
		team2.addAllPlayersOnTheFloor();
	}
	
	//////////////
	
	public boolean areValidDependingOnWhatHappenedInTheGame(RecordingStats newRecordingStats) {
		Set<Stat> newValues = newRecordingStats.values;
		if (newValues.isEmpty()) {
			return false;
		}
		if (wasUsed(Stat.REB) && (newValues.contains(Stat.OFF) || newValues.contains(Stat.DEF))) {
			return false;
		}
		if (wasUsed(Stat.TPM) && !newValues.contains(Stat.TPM)) {
			return false;
		}
		if (wasUsed(Stat.FTM) && !newValues.contains(Stat.FTM)) {
			return false;
		}
		if (wasAnyUsed(Stat.IIPM, Stat.FTM, Stat.TPM) && !newValues.contains(Stat.IIPM)) {
			return false;
		}
		return true;
	}
	
	public boolean doBothTeamsHaveSameNumberOfPlayersOnFloor() {
		if (team1.getPlayersOnTheFloor().size() == team2.getPlayersOnTheFloor().size()) {
			return true;
		}
		return false;
	}
	
	////////////////////////////////////////
	
	/*
	 * TO STRING:
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n").append(lb())
		.append(date).append("\n")
		.append(venue).append("\n").append(lb())
		.append(spaces(shotValues.values.get(Stat.FTM))).append("\n")
		.append(team1.getName()).append(": ").append(team1.get(Stat.PTS)).append("\n")
		.append(spaces(shotValues.values.get(Stat.IIPM))).append("\n")
		.append(team2.getName()).append(": ").append(team2.get(Stat.PTS)).append("\n")
		.append(spaces(shotValues.values.get(Stat.TPM))).append("\n").append(lb())
		.append("BOX SCORE:\n").append(lb())
		.append(team1).append(lb())
		.append(team2).append(lb());
		return sb.toString();
	}
	
	private static String spaces(int noOfSpaces) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < noOfSpaces; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	private final String ONE_TAB_LINE = "--------";
	
	private String lb() {
		StringBuffer sb = new StringBuffer();
		Set<Stat> boxStats = Stat.getDisplayableStatsFromRecordables(recordingStats.values);
		boxStats.remove(Stat.FGA);
		boxStats.remove(Stat.TPA);
		boxStats.remove(Stat.FTA);
		for (int i = boxStats.size() + Conf.NUMBER_OF_TABS_FOR_PLAYER_NAME; i > 0; i--) {
			sb.append(ONE_TAB_LINE);
		}
		sb.append("\n");
		return sb.toString();
	}

	////////////////////////////////////

	public boolean oneTeamHasNoPlayersOnTheFloor() {
		if (team1.getPlayersOnTheFloor().size() == 0
				|| team2.getPlayersOnTheFloor().size() == 0) {
			return true;
		}
		return false;
	}
}