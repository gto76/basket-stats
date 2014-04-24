package si.gto76.basketstats.coreclasses;

public class Action {
	////////////////////////////////////////
	private final Stat stat;
	private final PlayerStats playerStats;
	////////////////////////////////////////
	
	public Action(Stat stat, PlayerStats playersStats) {
		this.stat = stat;
		this.playerStats = playersStats;
	}
	
	////////////////////////////////////////

	public boolean trigger() {
		return playerStats.made(stat);
	}

	public void undo() {
		playerStats.unMade(stat);
	}
	
	public int getStatValue() {
		return playerStats.get(stat);
	}

	public String getStatName() {
		return stat.getName();
	}
	
	public Stat getStat() {
		return stat;
	}

	public Team getTeam() {
		return playerStats.getTeam();
	}

	public Player getPlayer() {
		return playerStats.getPlayer();
	}


}