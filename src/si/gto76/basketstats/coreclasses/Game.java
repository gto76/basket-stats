package si.gto76.basketstats.coreclasses;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import si.gto76.basketstats.Conf;

public class Game {
	////////////////////////////////////////
	private Location location;
	private Date date;
	private Team team1, team2;
	////////////////////////////////////////

	public Game(Team team1, Team team2, Date date, Location location) {
		this.team1 = team1;
		this.team2 = team2;
		this.date = date;
		this.location = location;
	}
	
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

	public Location getLocation() {
		return location;
	}

	public int getNumberOfPlayers() {
		return team1.getNumberOfPlayers() + team2.getNumberOfPlayers();
	}

	public PlayerStats getPlayersStats(Player player) {
		PlayerStats playersStats = team1.getPlayersStats(player);
		if (playersStats == null) {
			playersStats = team2.getPlayersStats(player);
		}
		return playersStats;
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
	
	////////////////////////////////////////

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n").append(lb).append(date).append("\n").append(location)
				.append("\n").append(lb).append("\n").append(team1.getName())
				.append(": ").append(team1.get(Stat.PTS)).append("\n\n")
				.append(team2.getName()).append(": ")
				.append(team2.get(Stat.PTS)).append("\n\n").append(lb)
				.append("BOX SCORE:\n").append(lb).append(team1).append(lb)
				.append(team2).append(lb);
		return sb.toString();
	}

	private String lb;
	private String oneTabLine = "--------";
	{
		StringBuffer sb = new StringBuffer();
		for (int i = Stat.boxScoreValues().length
				+ Conf.NUMBER_OF_TABS_FOR_PLAYER_NAME - 2; i > 0; i--) {
			sb.append(oneTabLine);
		}
		sb.append("\n");
		lb = sb.toString();
	}

}