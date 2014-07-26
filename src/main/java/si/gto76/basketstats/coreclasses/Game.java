package si.gto76.basketstats.coreclasses;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import si.gto76.basketstats.Conf;

/**
 * Main class of core classes. It represent state of a game at a particular moment.
 * It does not contain undo functionality, which has to be implemented sepatetely.
 */
public class Game {
	////////////////////////////////////////
	private Date date;
	private final Venue venue;
	private final Team team1, team2;
	protected final ShotValues shotValues;
	public RecordingStats recordingStats;
	////////////////////////////////////////
	
	public Game(String team1Name, List<Player> team1Players, 
				String team2Name, List<Player> team2Players,
				Date date, Venue venue, RecordingStats recordingStats, ShotValues shotValues) {
		this.team1 = new Team(team1Name, team1Players, this);
		this.team2 = new Team(team2Name, team2Players, this);
		this.date = date;
		this.venue = venue;
		this.recordingStats = recordingStats;
		this.shotValues = shotValues;
	}
	
	public Game(String team1Name, Map<Player, PlayerStatRecorder> team1PlayersStatRecorders, 
				String team2Name, Map<Player, PlayerStatRecorder> team2PlayersStatRecorders,
				Date date, Venue venue, RecordingStats recordingStats, ShotValues shotValues) {
		this.team1 = new Team(team1Name, team1PlayersStatRecorders, this);
		this.team2 = new Team(team2Name, team2PlayersStatRecorders, this);
		this.date = date;
		this.venue = venue;
		this.recordingStats = recordingStats;
		this.shotValues = shotValues;
	}
	
	////////////////////////////////////////
	
	/*
	 * SETTERS:
	 */
	
	public void setDate(Date date) {
		this.date = date;
	}
	
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
		team1.putAllPlayersOnTheFloor();
		team2.putAllPlayersOnTheFloor();
	}
	
	/*
	 * GETTERS:
	 */
	
	public Date getDate() {
		return date;
	}

	public Team getTeam1() {
		return team1;
	}

	public Team getTeam2() {
		return team2;
	}

	public Venue getVenue() {
		return venue;
	}

	public int getNumberOfPlayers() {
		return team1.getNumberOfPlayers() + team2.getNumberOfPlayers();
	}

	public PlayerStatRecorder getPlayersStatRecorder(Player player) {
		Team team = getPlayersTeam(player);
		return team.getPlayersStatRecorder(player);
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
		Set<Player> playersOnTheFloor = new HashSet<Player>(team1.getPlayersThatAreOnTheFloor());
		playersOnTheFloor.addAll(team2.getPlayersThatAreOnTheFloor());
		return playersOnTheFloor;
	}

	private boolean wasAnyOfTheStatsUsed(Stat... stats) {
		for (Stat stat : stats) {
			if (wasStatUsed(stat)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean wasStatUsed(Stat stat) {
		if (stat == Stat.REB) {
			return hasUsedRebounds();
		}
        return get(stat) != 0;
	}
	
	private int get(Stat stat) {
		return team1.get(stat) + team2.get(stat);
	}
	
	private boolean hasUsedRebounds() {
		return team1.hasUsedRebounds() || team2.hasUsedRebounds();
	}

	public boolean oneTeamHasNoPlayersOnTheFloor() {
		return team1.getPlayersThatAreOnTheFloor().size() == 0
				|| team2.getPlayersThatAreOnTheFloor().size() == 0;
	}
	
	public boolean areNewStatsValidDependingOnWhatHappenedInTheGame(RecordingStats newRecordingStats) {
		Set<Stat> newStats = newRecordingStats.values;
		if (newStats.isEmpty()) {
			return false;
		}
		boolean generic_rebounds_were_used_but_new_stats_track_either_offensive_or_defensive_rebounds = 
				wasStatUsed(Stat.REB) && (newStats.contains(Stat.OFF) || newStats.contains(Stat.DEF));
		boolean three_pointers_were_used_but_new_stats_dont_track_them = 
				wasStatUsed(Stat.TPM) && !newStats.contains(Stat.TPM);
		boolean free_throws_were_used_but_new_stats_dont_track_them =
				wasStatUsed(Stat.FTM) && !newStats.contains(Stat.FTM);
		boolean some_points_were_scored_but_new_stats_dont_track_them =
				wasAnyOfTheStatsUsed(Stat.IIPM, Stat.FTM, Stat.TPM) && !newStats.contains(Stat.IIPM);

		if (generic_rebounds_were_used_but_new_stats_track_either_offensive_or_defensive_rebounds
				|| three_pointers_were_used_but_new_stats_dont_track_them
				|| free_throws_were_used_but_new_stats_dont_track_them
				|| some_points_were_scored_but_new_stats_dont_track_them) {
			return false;
		}
		return true;
	}
	
	public boolean doBothTeamsHaveSameNumberOfPlayersOnTheFloor() {
		return team1.getPlayersThatAreOnTheFloor().size() == team2.getPlayersThatAreOnTheFloor().size();
	}
	
	/////////////////////
	
	@Override
	public String toString() {
		return PrinterGame.toString(this);
	}
	
}