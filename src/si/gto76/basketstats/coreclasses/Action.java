package si.gto76.basketstats.coreclasses;

public class Action {
	////////////////////////////////////////
	private final Stat stat;
	private final PlayerStats playersStats;
	////////////////////////////////////////
	
	public Action(Stat stat, PlayerStats playersStats) {
		this.stat = stat;
		this.playersStats = playersStats;
	}
	
	////////////////////////////////////////
	
	public int getStatValue() {
		return playersStats.get(stat);
	}

	public String getStatName() {
		return stat.getName();
	}
	
	public Stat getStat() {
		return stat;
	}

	public Team getTeam() {
		return playersStats.getTeam();
	}

	public Player getPlayer() {
		return playersStats.getPlayer();
	}

	public boolean trigger() {
		return playersStats.made(stat);
	}

	public void undo() {
		playersStats.unMade(stat);
	}

}